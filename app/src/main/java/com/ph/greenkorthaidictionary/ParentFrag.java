package com.ph.greenkorthaidictionary;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.app.Fragment;

/**
 * Created by preparkha on 15. 6. 9..
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public abstract class ParentFrag  extends Fragment {

    public abstract void refreshFrag();

}
