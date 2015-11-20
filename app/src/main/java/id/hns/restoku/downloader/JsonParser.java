package id.hns.restoku.downloader;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by HaidarNS on 17/11/2015.
 */
public class JsonParser {
    private static JSONObject jObj = null;
    private static String json = "";
    private HttpURLConnection c = null;

    public JSONObject getJSONFromUrl(String url){
        try{
            URL u = new URL(url);
            c = (HttpURLConnection) u.openConnection();

            c.setRequestMethod("GET");
            c.setUseCaches(false);
            c.connect();

            int status = c.getResponseCode();
            switch (status){
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }

                    br.close();
                    json = sb.toString();
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (c != null) {
                try {
                    c.disconnect();
                    jObj = new JSONObject(json);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return jObj;
    }
}
