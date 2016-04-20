package com.ph.greenkorthaidictionary.act;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.vending.licensing.LicenseChecker;
import com.google.android.vending.licensing.LicenseCheckerCallback;
import com.google.android.vending.licensing.Policy;
import com.ph.greenkorthaidictionary.ParentAct;
import com.ph.greenkorthaidictionary.R;
import com.ph.greenkorthaidictionary.data.dto.ServiceInfoDto;
import com.ph.greenkorthaidictionary.data.network.NetworkData;
import com.ph.greenkorthaidictionary.data.network.NetworkResponse;
import com.ph.greenkorthaidictionary.database.DatabaseCRUD;
import com.ph.greenkorthaidictionary.database.DatabaseHelper;
import com.ph.greenkorthaidictionary.database.util.DatabaseConstantUtil;
import com.ph.greenkorthaidictionary.database.util.DatabaseUtil;
import com.ph.greenkorthaidictionary.network.NetworkExecute;
import com.ph.greenkorthaidictionary.network.listener.NetworkListener;
import com.ph.greenkorthaidictionary.network.util.NetworkConstantUtil;
import com.ph.greenkorthaidictionary.util.DebugUtil;
import com.ph.greenkorthaidictionary.util.KorThaiDicConstantUtil;
import com.ph.greenkorthaidictionary.util.MoveActUtil;
import com.ph.greenkorthaidictionary.util.SharedPreUtil;
import com.ph.greenkorthaidictionary.util.TextUtil;

import java.io.IOException;
import java.util.HashMap;

/**
 * function
 * - check updata database from server
 * - check is there database in database
 */
public class IntroAct extends ParentAct implements NetworkListener{

    private DatabaseHelper databaseHelper;

    private String os = "A";
    private int retryCount;

    // control licence

    //공개키
    private static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA2929NzLHskKPG32ZDean1jpnn3lqTmWCFQ64u3+2mu19hJlGertK5jmN5LsxH1HLQX0Nw6WazZ55WcJHX7LB04mXNzXc0C8nVfK8eFwGo5Jkbd8TsdrjZFfDUGqM4v/bj75p3VmMBoUTV0VgAQUK5fIMK6VKm+rO4i4qae6UIrosOND2rItC78yumY6Gq0WubjYpZ3dsRjUVpYdpHmZK3mYnGRpX9StfNrgbDPvjT5De0s7fT5P2etIb4LHcOi2A88yl/kdQZo15y/kxr1cEKs4ku5Q1oGodPapiHInjVe0CFv0MflmnsV1oHX5+WaOtPrP3WAMfTG8GZkrrQAgJtQIDAQAB";
    // 20개의 랜덤한 byte
    private static final byte[] SALT = {-46, 65, 30, -128, -103, -57, 74, -64, 51, 88, -95, -45, 77, -117, -36, -113, -11, 32, -64, 89};

    private LicenseCheckerCallback mLicenseCheckerCallback;
    private LicenseChecker mChecker;
    private Context mContext;
    String deviceId;
    private ProgressDialog progressDialog;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // 1. request server to check database version
        // if sqlite data vesion < server sqlite data vesion
        //      download sqlite file which name is ThaiKorDictionary_SEARCH_T
        //      set database version (call onUpgrade to drop)
        // 2.2. check table

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Check License");
        DatabaseConstantUtil.DATABASE_VERSION = 20;

        // (debug)
        setBase();

//        // 라이센스 인증 여부는 내부 프리퍼런스로 저장을 해서 판단한다. (release)
//        if (KorThaiDicUtil.GetShareIntData(this, KorThaiDicConstantUtil.KEY_SHARED_PREFERENCE.CHECK_LICENSE) != 1) {
//            //인증을 받지 못한 경우
//            progressDialog.show();
//            mHandler = new Handler();
//            mLicenseCheckerCallback = new MyLicenseCheckerCallback();
//            deviceId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
//            mChecker = new LicenseChecker(this, new ServerManagedPolicy(this, new AESObfuscator(SALT, getPackageName(), deviceId)), BASE64_PUBLIC_KEY);
//            mChecker.checkAccess(mLicenseCheckerCallback);
//        } else {
//            //SharedPreUtil.getInstance().putPreference(KorThaiDicConstantUtil.KEY_SHARED_PREFERENCE.CHECK_DATABASE, 1);
//
////        if (SharedPreUtil.getInstance().getIntPreference(KorThaiDicConstantUtil.KEY_SHARED_PREFERENCE.CHECK_DATABASE) != 0)
////            DatabaseConstantUtil.setDatabaseVersion(SharedPreUtil.getInstance().getIntPreference(KorThaiDicConstantUtil.KEY_SHARED_PREFERENCE.CHECK_DATABASE));
//
//
//            setBase();
//        }

    }

    private void copyDatabase(SQLiteDatabase sqLiteDatabase) {

        if (!DatabaseCRUD.checkTable(databaseHelper, sqLiteDatabase, DatabaseConstantUtil.TABLE_KOR_THAI_NAME)) {
            DebugUtil.showDebug("table is not existed");
            try {
                DatabaseUtil.copyDataBase(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            DebugUtil.showDebug("table is existed");

        }
    }

    //----------------------------------------------------------------------------//

    // about network

    public boolean checkParams(int idx) {
        DebugUtil.showDebug("IntroAct checkParams()");
        boolean flag = true;

        switch (idx) {
            case NetworkConstantUtil.API_IDX._1_GET_SERVICE_INFORMATION:
                if (TextUtil.isNull(os))
                    flag = false;
                break;
        }

        return flag;
    }

    public void request(int idx) {
        DebugUtil.showDebug("IntroAct request()");
        // parameter
        HashMap<String, String> params = new HashMap<>();
        params.put("OS", os);

        NetworkExecute execute = new NetworkExecute(this.getApplicationContext());
        switch (idx) {
            case NetworkConstantUtil.API_IDX._1_GET_SERVICE_INFORMATION:
                execute.setInfoNetwork(NetworkConstantUtil.API_IDX._1_GET_SERVICE_INFORMATION, NetworkConstantUtil.API_URL._1_GET_SERVICE_INFORMATION, params, this);
                break;
        }
        execute.executeNetwork();
    }

    public void checkNetworkResponse(int idx, NetworkResponse networkResponse) {
        DebugUtil.showDebug("IntroAct checkNetworkResponse()");

        boolean result = networkResponse.isRESULT();
        int resultCode = networkResponse.getRESULTCODE();

        if (result) {
            resultIsTrue(idx, resultCode, networkResponse);
        } else {
            resultIsTrue(idx, resultCode, networkResponse);
        }

    }

    public void checkNetworkData(int idx, NetworkResponse networkResponse) {
        DebugUtil.showDebug("IntroAct checkNetworkData()");

        switch (idx) {
            case NetworkConstantUtil.API_IDX._1_GET_SERVICE_INFORMATION:
                NetworkData serviceInfo = networkResponse.getSERVICEINFO();
                ServiceInfoDto serviceInfoDto = new ServiceInfoDto();
                serviceInfoDto.setCURVERCD_K(serviceInfo.getCURVERCD_K());
                serviceInfoDto.setCURVERNM_K(serviceInfo.getCURVERNM_P());
                serviceInfoDto.setDBCURDTPATH_K(serviceInfo.getDBCURDTPATH_K());
                serviceInfoDto.setDBCURVERCD_K(serviceInfo.getDBCURVERCD_K());
                serviceInfoDto.setOS(serviceInfo.getOS());
                serviceInfoDto.setSTORE_URL_K(serviceInfo.getSTORE_URL_K());

                DebugUtil.showDebug("IntroAct " + serviceInfoDto.toString());
                break;
        }

    }

    public void resultIsTrue(int idx, int resultCode, NetworkResponse networkResponse) {
        DebugUtil.showDebug("MainAct resultIsTrue()");
        switch (resultCode) {
            case NetworkConstantUtil.RESULT_CODE.SUCCESS:
                DebugUtil.showDebug("MainAct SUCCESS");
                checkNetworkData(idx, networkResponse);
                break;
        }
    }

    public void resultIsFalse(int idx, int resultCode) {
        DebugUtil.showDebug("IntroAct resultIsFalse()");
        switch (resultCode) {
            case NetworkConstantUtil.RESULT_CODE.EMPTY_PARAMETER_THAT_IS_ESSENTIAL:
                DebugUtil.showDebug("IntroAct EMPTY_PARAMETER_THAT_IS_ESSENTIAL");
                break;
            case NetworkConstantUtil.RESULT_CODE.ERROR_OF_CONNENTING_WITH_DB_OR_EXECUTE_QUERY:
                DebugUtil.showDebug("IntroAct ERROR_OF_CONNENTING_WITH_DB_OR_EXECUTE_QUERY");
                break;
        }

    }

    @Override
    public void onNetworkStart(int idx) {
        DebugUtil.showDebug("IntroAct onNetworkStart()");
        switch (idx) {
            case NetworkConstantUtil.API_IDX._1_GET_SERVICE_INFORMATION:
                DebugUtil.showDebug("IntroAct _1_GET_SERVICE_INFORMATION");
                break;
        }

    }

    @Override
    public void onNetworkResult(int idx, NetworkResponse networkResponse) {
        DebugUtil.showDebug("IntroAct onNetworkResult()");

        checkNetworkResponse(idx, networkResponse);

    }

    @Override
    public void onNetworkError(int idx, String errorMessage) {
        DebugUtil.showDebug("IntroAct onNetworkError()");
        switch (idx) {
            case NetworkConstantUtil.API_IDX._1_GET_SERVICE_INFORMATION:
                DebugUtil.showDebug("IntroAct _1_GET_SERVICE_INFORMATION");
                break;
        }
        DebugUtil.showDebug("IntroAct " + errorMessage);
    }

    // about alert dialog

    /**
     * create AlertDialog and show this.
     */
    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
//        builder.setTitle(getResources().getString(R.string.alert_dlgfrag_title_type1));
//        builder.setMessage(getResources().getString(R.string.alert_dlgfrag_content_type1));

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DebugUtil.showToast(getApplicationContext(), "downlog database");
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DebugUtil.showToast(getApplicationContext(), "cancel databse");
            }
        });

        builder.create();
        builder.show();
    }


    private void showAppFinishedAlertDialog() {
        android.app.AlertDialog.Builder appFinishDialog = new android.app.AlertDialog.Builder(this);
        appFinishDialog.setTitle(R.string.dialog_nocert_title_txt);
        appFinishDialog.setMessage(R.string.dialog_nocert_title_msg);
        appFinishDialog.setCancelable(false);
        appFinishDialog.setPositiveButton(R.string.dialog_nocert_confirm_btn_txt, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        appFinishDialog.create();
        appFinishDialog.show();
    }

    private void setBase() {

        databaseHelper = DatabaseHelper.getInstacnce(IntroAct.this.getApplicationContext());
        copyDatabase(DatabaseHelper.sqLiteDatabase);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MoveActUtil.chageActivity(IntroAct.this, MainAct.class, 0, 0, true, false);
            }
        }, 2000);

    }


    // about callback

    private class MyLicenseCheckerCallback implements LicenseCheckerCallback {

        @Override
        public void allow(int reason) {
            DebugUtil.showDebug("MainAct MyLicenseCheckerCallback allow()");
            //성공했을 경우 프로그래스바 다이얼로그 취소

//            CommonFunction.ShowDebug(LockScreenMainView.LOCKSCRREN_LOG, "OK");
//            //락스크린 서비스 시작
//            PendingIntent mAlarmSender = PendingIntent.getService(ItteLockScreenSeasonOneActivity.this, 0, new Intent(ItteLockScreenSeasonOneActivity.this, LockScreenService.class), 0);
//            long firstTime = SystemClock.elapsedRealtime();
//            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//            am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, 30 * 1000, mAlarmSender);
//            //인증 프로그래스바 팝업 취소
//            progressDialogCancel();
//            //인증 성공값 저장
//            CommonFunction.SetShareData(mContext, Common.LOCKSCREEN_PAY_RESULT, Common.LOCK_PAY_YES);

            SharedPreUtil.getInstance().putPreference(KorThaiDicConstantUtil.KEY_SHARED_PREFERENCE.CHECK_LICENSE, 1);
            setBase();
        }

        @Override
        public void dontAllow(int reason) {
            DebugUtil.showDebug("MainAct MyLicenseCheckerCallback dontAllow()");

            appDontAllowDisPlayDialog(-1);

//            if (reason == Policy.RETRY) {
//
//                if (retryCount >= 3) {
//                    //3번 이상일 경우 종료팝업
//                    appDontAllowDisPlayDialog(-1);
//                } else {
//                    //인증 재시도 팝업
//                    retryCount++;
//                    appDontAllowDisPlayDialog(reason);
//                }
//
//            } else {
//                //라이센스 사용자가 아닐경우
//                appDontAllowDisPlayDialog(reason);
//            }

        }

        @Override
        public void applicationError(int errorCode) {
            DebugUtil.showDebug("applicationError");
            //에러코드 바로 종료팝업
//            progressDialogCancel();
            appDontAllowDisPlayDialog(-1);
        }

    }

    public void appCheckMarket() {
        if (mChecker != null) {
            mChecker.checkAccess(mLicenseCheckerCallback);
        }
    }

    public void appDontAllowDisPlayDialog(int reson) {
        switch (reson) {
            case Policy.RETRY:
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        android.app.AlertDialog.Builder certGoDialog = new android.app.AlertDialog.Builder(mContext);
//                        certGoDialog.setTitle(R.string.dialog_retry_title_txt);
//                        certGoDialog.setMessage(R.string.dialog_retry_title_msg);
//                        certGoDialog.setCancelable(false);
//                        certGoDialog.setPositiveButton(R.string.dialog_retry_cert_btn_txt, new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                progressDialog.show();
//                                appCheckMarket();
//                                dialog.dismiss();
//                            }
//                        });
//                        certGoDialog.setNegativeButton(R.string.dialog_retry_cancel_btn_txt, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                finish();
//                            }
//                        });
//                        certGoDialog.show();
//
//                    }
//                });
//                break;
            case Policy.NOT_LICENSED:
//                mHandler.post(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        android.app.AlertDialog.Builder payAndroidMarketGoDialog = new android.app.AlertDialog.Builder(mContext);
//
//                        payAndroidMarketGoDialog.setTitle(R.string.dialog_title_txt);
//                        payAndroidMarketGoDialog.setMessage(R.string.dialog_title_msg);
//                        payAndroidMarketGoDialog.setCancelable(false);
//                        payAndroidMarketGoDialog.setPositiveButton(R.string.dialog_pay_btn_txt, new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                                MoveActUtil.DoExternalInternetConnectionIntent(mContext, KorThaiDicConstantUtil.PLAY_STORE_URL);
//                                finish();
//                            }
//                        });
//
//                        payAndroidMarketGoDialog.setNegativeButton(R.string.dialog_cancel_btn_txt, new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                finish();
//
//                            }
//                        });
//
//                        payAndroidMarketGoDialog.show();
//
//                    }
//                });
//                break;
            case -1:
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
//                        AlertDialog.Builder appFinishDialog = new AlertDialog.Builder(IntroAct.this.getApplicationContext());
//                        appFinishDialog.setTitle(R.string.dialog_nocert_title_txt);
//                        appFinishDialog.setMessage(R.string.dialog_nocert_title_msg);
//                        appFinishDialog.setCancelable(false);
//                        appFinishDialog.setPositiveButton(R.string.dialog_nocert_confirm_btn_txt, new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                                finish();
//                            }
//                        });
//                        appFinishDialog.create();
//                        appFinishDialog.show();
                        showAppFinishedAlertDialog();
                    }
                });
                break;
        }
    }



}
