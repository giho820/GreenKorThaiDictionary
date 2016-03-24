package com.ph.greenkorthaidictionary.async;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import com.ph.greenkorthaidictionary.ParentAct;
import com.ph.greenkorthaidictionary.database.util.DatabaseConstantUtil;
import com.ph.greenkorthaidictionary.util.DebugUtil;
import com.ph.greenkorthaidictionary.util.KorThaiDicConstantUtil;
import com.ph.greenkorthaidictionary.util.SharedPreUtil;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by preparkha on 15. 9. 11..
 */
public class DownloadAsync extends AsyncTask<String, String, String> {

    private Context context;
    private ParentAct parentAct;

    private int dbVersion;

    public DownloadAsync(Context context, ParentAct parentAct, int dbVersion) {
        this.context = context;
        this.parentAct = parentAct;
        this.dbVersion = dbVersion;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        DebugUtil.showDebug("DownloadAsync onPreExecute()");

        parentAct.showLoading();

    }

    @Override
    protected String doInBackground(String... params) {
        DebugUtil.showDebug("DownloadAsync doInBackground()");
        DebugUtil.showDebug("params : " + params[0]);

        int count;
        try {
            String root = Environment.getExternalStorageDirectory().toString();
            URL url = new URL(params[0]);
            DebugUtil.showDebug("root : " + root + " / params : " + params[0]);

            URLConnection conection = url.openConnection();
            conection.connect();
            // getting file length
            int lenghtOfFile = conection.getContentLength();

            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            // Path to the just created empty db
            String outFileName = DatabaseConstantUtil.DATABASE_PATH;

            //Open the empty db as the output stream
            OutputStream output = new FileOutputStream(outFileName);

            // Output stream to write file
//            OutputStream output = new FileOutputStream(root+"/Download/KorThaiDictionary_SEARCH_K.sqlite");
            byte data[] = new byte[1024];

            long total = 0;
            while ((count = input.read(data)) != -1) {
                total += count;

                // writing data to file
                output.write(data, 0, count);

            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

        } catch (Exception e) {
            if(e != null)
                DebugUtil.showDebug(e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        DebugUtil.showDebug("DownloadAsync onPostExecute()");

        SharedPreUtil.getInstance().putPreference(KorThaiDicConstantUtil.KEY_SHARED_PREFERENCE.CHECK_DATABASE, dbVersion);
        parentAct.hideLoading();

    }
}
