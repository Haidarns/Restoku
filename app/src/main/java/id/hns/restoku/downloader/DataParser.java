package id.hns.restoku.downloader;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.hns.restoku.MenuModel;
import id.hns.restoku.RestoModel;

/**
 * Created by HaidarNS on 17/11/2015.
 */
public class DataParser {
    private static final String HOST = "http://skripsirey.my.id/garpoe/select_restaurants.php";

    public static List<RestoModel> getBasedLongLat(double longitude, double latitude){
        List<RestoModel> list = new ArrayList<RestoModel>();
        JsonParser jParser = new JsonParser();

        JSONObject jsonObject = jParser.getJSONFromUrl(HOST+"?latitude="+latitude+"&longitude="+longitude);

        try{
            JSONArray jArr = jsonObject.getJSONArray("restaurants");

            for (int i = 0; i < jArr.length(); i++) {
                List<MenuModel> menus = new ArrayList<MenuModel>();
                JSONObject obj = jArr.getJSONObject(i);
                RestoModel resto = new RestoModel();

                resto.setId(obj.getString("id"));
                resto.setName(obj.getString("name"));
                resto.setAddress(obj.getString("address"));
                resto.setLatit(obj.getDouble("latitude"));
                resto.setLongit(obj.getDouble("longitude"));
                resto.setDistance(obj.getString("distance"));
                resto.setLinkImg(obj.getString("image"));

                JSONArray jArr2 = obj.getJSONArray("food");

                for (int j = 0; j < jArr2.length(); j++){
                    JSONObject obj2 = jArr2.getJSONObject(j);
                    MenuModel menu = new MenuModel();

                    menu.setId(obj2.getString("id"));
                    menu.setName(obj2.getString("name"));
                    menu.setPrice(obj2.getString("price"));
                    menu.setRestoId(obj2.getString("restaurant_id"));

                    menus.add(menu);
                }

                resto.setListMenu(menus);

                for (int j = 0; j < resto.getListMenu().size(); j++){
                    MenuModel m = resto.getListMenu().get(j);
                    Log.i(m.getName(),m.getPrice());
                }

                list.add(resto);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }

        return list;
    }
}
