package com.ph.greenkorthaidictionary;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

import com.android.volley.NetworkResponse;
import com.ph.greenkorthaidictionary.common.app.CommonLoadingDialog;
import com.ph.greenkorthaidictionary.data.network.NetworkData;
import com.ph.greenkorthaidictionary.util.DebugUtil;
import com.ph.greenkorthaidictionary.util.KorThaiDicConstantUtil;

/**
 * Created by preparkha on 15. 6. 9..
 */
public abstract class ParentAct extends ActionBarActivity {

    private CommonLoadingDialog loading;
    private ProgressDialog progressDialogForDownloadFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialogForDownloadFile = new ProgressDialog(this);
        progressDialogForDownloadFile.setMessage("downloading....");
        progressDialogForDownloadFile.setCancelable(false);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * @param frag
     * @param dlgIdx
     */
    public void dlgShow(Fragment frag, String dlgIdx) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(dlgIdx);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        ((DialogFragment) frag).show(ft, dlgIdx);
    }

    public void showLoading() {
        if(progressDialogForDownloadFile != null)
            progressDialogForDownloadFile.show();
    }

    public void hideLoading() {

        if(progressDialogForDownloadFile != null)
            progressDialogForDownloadFile.dismiss();
    }

    public void setLoading(Activity activity) {
        if (activity == null) {
            DebugUtil.showDebug("setLoading() : " + KorThaiDicConstantUtil.MSG_INTERNAL_ERROR._5_ACTIVITY_IS_NULL);
            return;
        }
        loading = new CommonLoadingDialog(activity);
    }

}
