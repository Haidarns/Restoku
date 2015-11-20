package id.hns.restoku.downloader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import id.hns.restoku.RestoModel;

/**
 * Created by HaidarNS on 17/11/2015.
 */
public class AsyncResto extends AsyncTask<Double, Void, List<RestoModel>> {
    private Context context;
    private ProgressDialog progressDialog;

    public AsyncResto(Activity activity) {
        context = activity;
        progressDialog = new ProgressDialog(context);
    }

    @Override
    protected List<RestoModel> doInBackground(Double... params) {
        return DataParser.getBasedLongLat(params[0], params[1]);
    }

    @Override
    protected void onPostExecute(List<RestoModel> restoModels) {
        super.onPostExecute(restoModels);
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
}
