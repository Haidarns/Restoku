package id.hns.restoku;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RestoActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ImageView img;
    private RestoModel resto;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resto);

        img = (ImageView) findViewById(R.id.image);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView tvAlamat = (TextView)findViewById(R.id.alamatTv);
        TextView tvJarak  = (TextView)findViewById(R.id.jarakTv);

        resto = (RestoModel) getIntent().getSerializableExtra("data");

        String tmpS = resto.getDistance();
        String[] tmp = tmpS.split("\\.");
        String newTmp = tmp[0]+","+tmp[1].substring(0,2);

        Log.i("address",resto.getAddress());
        Log.i("distance",resto.getDistance());
        tvAlamat.setText(resto.getAddress());
        tvJarak.setText(newTmp+" km");

        String url = "http://skripsirey.my.id/garpoe/"+resto.getLinkImg();

        Picasso.with(this)
                .load(url)
                .resize(300,300)
                .into(img);

        collapsingToolbarLayout.setTitle(resto.getName());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogMenu();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng restoLoc = new LatLng(resto.getLatit(), resto.getLongit());
        mMap.addMarker(new MarkerOptions().position(restoLoc).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(restoLoc));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }

    private void dialogMenu(){
        final Dialog dialog = new Dialog(RestoActivity.this);
        dialog.setContentView(R.layout.simple_list);
        dialog.setTitle(resto.getName() + "'s Menu");

        ListView lv = (ListView) dialog.findViewById(R.id.menuList);

        lv.setAdapter(new MenuListAdapter(RestoActivity.this, 1, resto.getListMenu()));

        dialog.show();
    }

    private static class MenuListAdapter extends ArrayAdapter<MenuModel> {
        Context context;
        List<MenuModel> listMenu;
        MenuModel menu;

        public MenuListAdapter(Context context, int resource, List<MenuModel> objects) {
            super(context, resource, objects);
            this.context = context;
            listMenu = objects;
        }

        private class ViewHolder {
            TextView nama, harga;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public MenuModel getItem(int position) {
            return listMenu.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh = new ViewHolder();

            menu = getItem(position);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.cus_menu_list, parent, false);

            vh.nama = (TextView) convertView.findViewById(R.id.namaMenuTv);
            vh.harga = (TextView) convertView.findViewById(R.id.hargaMenuTv);

            int tmpI = Integer.parseInt(menu.getPrice());
            int tmp1 = tmpI / 1000;
            String tmp2 = "";

            if(tmpI-(tmp1*1000) == 0){
                tmp2 = "000";
            } else {
                tmp2 = String.valueOf(tmpI-(tmp1*1000));
            }

            vh.nama.setText(menu.getName());
            vh.harga.setText("Rp "+tmp1+"."+tmp2+" ,-");
            //String newTmp = tmp[0] + "," + tmp[1].substring(0, 2);

            return convertView;
        }
    }
}
