package com.ph.greenkorthaidictionary.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by preparkha on 15. 6. 9..
 */
public class KorThaiDicUtil {

    //SharePreference Get
    public synchronized static int GetShareIntData(Context mCon, String mKey) {
        int mReturnInt = -1;
        int mDefaultInt = -1;

//        if (mKey == Common.LOCKSCREEN_NUM) mDefaultInt = 0;
//        if (mKey == Common.LOCKSCREEN_STATUS) mDefaultInt = Common.LOCK_ON;
//        if (mKey == Common.LOCKSCREEN_MASK_NUM) mDefaultInt = Common.Zipper_Shape_Type02;
//        if (mKey == Common.LOCKSCRREN_MASK_SHAPE_NUM) mDefaultInt = Common.Zipper_Shape_Type02;
//        if (mKey == Common.LOCKSCREEN_PASSWORD_FLAG) mDefaultInt = Common.PASSWORD_OFF;
//        if (mKey == Common.LOCKSCREEN_EFFECT_FLAG) mDefaultInt = Common.VIBRATION;
//        if (mKey == Common.LOCKSCREEN_PAY_RESULT) mDefaultInt = Common.LOCK_PAY_NO;
//        if (mKey == Common.LOCKSCRREN_OPEN_POSITION) mDefaultInt = Common.Zipper_Open_Pos_Type02;
//        if (mKey == Common.PHONE_FLAG) mDefaultInt = Common.PHONE_OFF;
//        if (mKey == Common.LOCKSCREEN_HOURFORMAT)
//            mDefaultInt = Common.LOCKSCREEN_HOURFORMAT_24;

        SharedPreferences mBabyPref = null;
        mBabyPref = SharedPreUtil.getInstance().getSharedPrefs();
        mReturnInt = mBabyPref.getInt(mKey, mDefaultInt);
        mBabyPref = null;

        //-1, 0, 1

        return mReturnInt;
    }
}
