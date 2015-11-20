package id.hns.restoku;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import id.hns.restoku.downloader.AsyncResto;

public class MainActivity extends AppCompatActivity implements LocationListener{
    private List<RestoModel> listResto;
    private double lng, lat;
    private LocationManager lm;
    private Location curLoc;
    private ListView listMain;

    private void getLoc() {
        super.onStart();

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try{
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 10, this);
            Location gpsLoc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (gpsLoc != null){
                curLoc = gpsLoc;
            } else {
                Location netLoc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                if(netLoc != null){
                    curLoc = netLoc;
                } else {
                    curLoc = new Location("FIXED");
                    curLoc.setLongitude(107.626776);
                    curLoc.setLatitude(-6.967543);
                }
            }
            onLocationChanged(curLoc);
        } catch (SecurityException e){
            e.printStackTrace();
        }

        Log.i("Longitude", String.valueOf(lng));
        Log.i("Langitude",String.valueOf(lat));

        try{
            listResto = new AsyncResto(this).execute(lng, lat).get();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        getLoc();
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        try{
            lm.removeUpdates(this);
        } catch (SecurityException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoc();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listMain = (ListView) findViewById(R.id.listMain);

        if (listResto != null) {
            listMain.setAdapter(new RestoListAdapter(this, 1, listResto));
            listMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    RestoModel restoModel = listResto.get(position);
                    Intent i = new Intent(getApplicationContext(), RestoActivity.class);
                    i.putExtra("data", restoModel);
                    i.putExtra("data1", restoModel.getListMenu().get(0));
                    i.putExtra("data2", restoModel.getListMenu().get(1));
                    startActivity(i);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private static class RestoListAdapter extends ArrayAdapter<RestoModel>{
        Context context;
        List<RestoModel> listResto;
        RestoModel resto;

        public RestoListAdapter(Context context, int resource, List<RestoModel> objects) {
            super(context, resource, objects);
            this.context = context;
            listResto = objects;
        }

        private class ViewHolder{
            ImageView gambar;
            TextView nama, alamat, jarak;

        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public RestoModel getItem(int position) {
            return listResto.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh = new ViewHolder();

            resto = getItem(position);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.cus_main_list, parent, false);

            vh.nama   = (TextView) convertView.findViewById(R.id.titleMainList);
            vh.alamat = (TextView) convertView.findViewById(R.id.addressListMain);
            vh.jarak  = (TextView) convertView.findViewById(R.id.distMainList);
            vh.gambar = (ImageView) convertView.findViewById(R.id.imgListMain);

            vh.nama.setText(resto.getName());
            vh.alamat.setText(resto.getAddress());

            String tmpS = resto.getDistance();
            String[] tmp = tmpS.split("\\.");
            String newTmp = tmp[0]+","+tmp[1].substring(0,2);
            vh.jarak.setText(newTmp);

            String url = "http://skripsirey.my.id/garpoe/"+resto.getLinkImg();

            Picasso.with(context)
                    .load(url)
                    .resize(70,70)
                    .into(vh.gambar);

            return convertView;
        }
    }
}
