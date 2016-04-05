package com.ph.greenkorthaidictionary.act;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ph.greenkorthaidictionary.ParentAct;
import com.ph.greenkorthaidictionary.R;
import com.ph.greenkorthaidictionary.async.DownloadAsync;
import com.ph.greenkorthaidictionary.data.dto.KorThaiDicDto;
import com.ph.greenkorthaidictionary.data.dto.ServiceInfoDto;
import com.ph.greenkorthaidictionary.data.frag.FragData;
import com.ph.greenkorthaidictionary.data.network.NetworkData;
import com.ph.greenkorthaidictionary.data.network.NetworkResponse;
import com.ph.greenkorthaidictionary.database.DatabaseHelper;
import com.ph.greenkorthaidictionary.fragment.DetailItemFrag;
import com.ph.greenkorthaidictionary.fragment.ListItemFrag;
import com.ph.greenkorthaidictionary.listener.FragListener;
import com.ph.greenkorthaidictionary.network.NetworkExecute;
import com.ph.greenkorthaidictionary.network.listener.NetworkListener;
import com.ph.greenkorthaidictionary.network.util.NetworkConstantUtil;
import com.ph.greenkorthaidictionary.service.BackgroundResultReceiver;
import com.ph.greenkorthaidictionary.service.QueryIntentService;
import com.ph.greenkorthaidictionary.util.DebugUtil;
import com.ph.greenkorthaidictionary.util.KorThaiDicConstantUtil;
import com.ph.greenkorthaidictionary.util.MoveActUtil;
import com.ph.greenkorthaidictionary.util.TextUtil;
import com.util.DeviceUtil;
import com.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainAct extends ParentAct implements NetworkListener, View.OnClickListener, FragListener, BackgroundResultReceiver.Receiver {

    // header
    private FrameLayout backFl;
    private FrameLayout settingFl;
    private TextView titleTv;

    // edittext
    private EditText et;
    private FrameLayout searchFl;
    private TextWatcher textWatcher;

    // body
    private FrameLayout bodyFl;
    private ListItemFrag listItemFrag = null;
    private ArrayList<KorThaiDicDto> korThaiDicDtoList;

    private LinearLayout.LayoutParams layoutParamsForTitleTv;

    public BackgroundResultReceiver mReceiver;

    private FragData fragData;
    private String currentKeyword = null;

    // page
    private int start = 0;
    private int limit = 10;

    // parameter
    private String os = "A";

    // check user's decision that is initialize when database is updated
    private static int isCheckedUserDecision = -1;

    // compare
    // device information
    private int versionCode;
    private String versionName;

    // about download
    private DownloadAsync downloadAsync;
    private ServiceInfoDto serviceInfoDto;

    // about tts
    public static boolean doesTtsExists = false; //구글 tts 패키지가 설치되어있는지의 여부를 확인

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // header
        backFl = (FrameLayout) findViewById(R.id.act_header_back_fl);
        backFl.setOnClickListener(this);
        settingFl = (FrameLayout) findViewById(R.id.act_header_setting_fl);
        settingFl.setOnClickListener(this);
        titleTv = (TextView) findViewById(R.id.act_header_title_tv);

        setHeader(KorThaiDicConstantUtil.TYPE_FRAG_MAIN.LIST_ITEM);

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //executeSearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                start = 0;
                if (!TextUtil.isNull(s.toString())) {
                    currentKeyword = s.toString();
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            executeSearch(currentKeyword);
                        }
                    });
                } else {
                    listItemFrag = null;
                    fragData = null;
                    if (bodyFl.getChildCount() > 0)
                        bodyFl.removeAllViews();
                }
            }
        };

        et = (EditText) findViewById(R.id.act_et);
        et.addTextChangedListener(textWatcher);
        searchFl = (FrameLayout) findViewById(R.id.act_search_fl);
        searchFl.setOnClickListener(this);

        bodyFl = (FrameLayout) findViewById(R.id.act_body_fl);

        mReceiver = new BackgroundResultReceiver(new Handler());
        mReceiver.setReceiver(this);

//        // get package information to know versionCode and versionName
//        try {
//            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
//            versionCode = packageInfo.versionCode;
//            versionName = packageInfo.versionName;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//            versionCode = 0;
//            versionName = null;
//        }
//        isCheckedUserDecision = -1;

        if (checkParams(NetworkConstantUtil.API_IDX._1_GET_SERVICE_INFORMATION))
            request(NetworkConstantUtil.API_IDX._1_GET_SERVICE_INFORMATION);

    }

    // about lifecycle

    @Override
    protected void onResume() {
        super.onResume();

        if (checkIfGoogleTtsHasBeenInstalled() == false) {
            installGoogleTts();
        } else {
            updateGoogleTts();
        }

//        Intent intent = new Intent(this, NetworkIntentService.class);
//        intent.putExtra("os", os);
//        intent.putExtra("receiver", mReceiver);
//        startService(intent);

//        // check database version, apk version code and version name in server
//        if (isCheckedUserDecision == -1) {
//            if (checkParams(NetworkConstantUtil.API_IDX._1_GET_SERVICE_INFORMATION))
//                request(NetworkConstantUtil.API_IDX._1_GET_SERVICE_INFORMATION);
//        }

//        //        //kiho moved this request to onCreate() 15.11.23
//        if(checkParams(NetworkConstantUtil.API_IDX._1_GET_SERVICE_INFORMATION))
//            request(NetworkConstantUtil.API_IDX._1_GET_SERVICE_INFORMATION);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DebugUtil.showDebug("MainAct onDestroy");
        DatabaseHelper.getInstacnce(this).close();
    }

    // about onClick

    @Override
    public void onClick(View v) {
        DebugUtil.showDebug("onClick()");

        switch (v.getId()) {
            case R.id.act_header_back_fl:
                onBackPressed();
                break;
            case R.id.act_header_setting_fl:
                hideKeyboard(et);
                MoveActUtil.chageActivity(this, SettingAct.class, R.anim.right_in, R.anim.right_out, false, true);
                break;
            case R.id.act_search_fl:
                hideKeyboard(et);
                if (bodyFl.getChildCount() > 0)
                    bodyFl.removeAllViews();
                start = 0;
                if (!TextUtil.isNull(et.getText().toString())) {
                    currentKeyword = et.getText().toString();
                    executeSearch(et.getText().toString());
                } else {
                    listItemFrag = null;
                    fragData = null;
                    if (bodyFl.getChildCount() > 0)
                        bodyFl.removeAllViews();
                }
                break;
        }

    }

    @Override
    public void onBackPressed() {
        DebugUtil.showDebug("onBackPressed()");

        if (bodyFl.getChildCount() > 0) {
            DebugUtil.showDebug("getChildCount() : " + bodyFl.getChildCount());
            if (bodyFl.getChildCount() > 1) {
                // detail
                setHeader(KorThaiDicConstantUtil.TYPE_FRAG_MAIN.LIST_ITEM);
                bodyFl.removeViewAt(1);
            } else {
                // list
                setHeader(KorThaiDicConstantUtil.TYPE_FRAG_MAIN.LIST_ITEM);
                initializeUI();
            }
        } else {
            finish();
        }
    }

    @Override
    public void onSendFragData(FragData fragData) {
        DebugUtil.showDebug("MainAct onSendFragData()");

        if (fragData == null)
            return;

        if (bodyFl == null)
            return;

        this.fragData = fragData;

        setHeader(fragData.getFragType());
        setBodyFrag(fragData.getFragType(), null, fragData.getKorThaiDicDto());

    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        DebugUtil.showDebug("resutlCode : " + resultCode);

        if (TextUtil.isNull(et.getText().toString()))
            return;

        if (resultCode == 500 && resultData != null) {
            ArrayList<KorThaiDicDto> tempKorThaiDicDtoList = (ArrayList<KorThaiDicDto>) resultData.getSerializable("KorThaiDicDtoList");
            if (tempKorThaiDicDtoList != null) {
                // setBodyFrag 에서 구현되게 만들어야해 그래야 보기 좋은 코드 된다!!!!
                setBodyFrag(KorThaiDicConstantUtil.TYPE_FRAG_MAIN.LIST_ITEM, tempKorThaiDicDtoList, null);
            } else {
                if (fragData == null || start == 0) {
                    if (bodyFl.getChildCount() > 0)
                        bodyFl.removeAllViews();
                }
            }
        }

    }

    /**
     * check keyword is null, empty or is not null
     *
     * @param keyword
     * @return
     */
    private boolean checkKeyword(String keyword) {
        boolean flag = false;

        if (!TextUtil.isNull(keyword))
            flag = true;

        return flag;
    }

    /**
     * execute search by keyword
     *
     * @param keyword
     */
    private void executeSearch(String keyword) {
        if (checkKeyword(keyword)) {
            if (keyword.charAt(keyword.length() - 1) == ' ') {
                keyword = keyword.substring(0, keyword.length() - 1);
                DebugUtil.showDebug("keyword : " + keyword);
            }
            Intent intent = new Intent(this, QueryIntentService.class);
            intent.putExtra("keyword", keyword);
            intent.putExtra("receiver", mReceiver);
            intent.putExtra("start", start);
            intent.putExtra("limit", limit);
            startService(intent);
        } else {
            DebugUtil.showToast(this.getApplicationContext(), "칮는 단어를 입력해주세요.");
        }
    }

    /**
     *
     */
    private void initializeUI() {
        DebugUtil.showDebug("initializeUI");
        et.setText("");
        if (bodyFl.getChildCount() > 0)
            bodyFl.removeAllViews();
        hideKeyboard(et);
    }

    /**
     * set header ui by type of fragment
     *
     * @param typeFragment
     */
    private void setHeader(int typeFragment) {
        DebugUtil.showDebug("MainAct setHeader()");

        switch (typeFragment) {
            case KorThaiDicConstantUtil.TYPE_FRAG_MAIN.LIST_ITEM:
            case KorThaiDicConstantUtil.TYPE_FRAG_MAIN.UPDATE_LIST_ITEM:
                // without back framelayout
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        backFl.setVisibility(View.GONE);
                        layoutParamsForTitleTv = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (int) getResources().getDimension(R.dimen.dp_26));
                        layoutParamsForTitleTv.setMargins((int) getResources().getDimension(R.dimen.dp_15_33), 0, 0, 0);
                        titleTv.setLayoutParams(layoutParamsForTitleTv);
                    }
                });
                break;
            case KorThaiDicConstantUtil.TYPE_FRAG_MAIN.DETAIL_ITEM:
                // with back framelayout
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        backFl.setVisibility(View.VISIBLE);
                        layoutParamsForTitleTv = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (int) getResources().getDimension(R.dimen.dp_26));
                        layoutParamsForTitleTv.setMargins((int) getResources().getDimension(R.dimen.dp_2_33), 0, 0, 0);
                        titleTv.setLayoutParams(layoutParamsForTitleTv);
                    }
                });
                break;
        }
    }

    /**
     * set body ui
     *
     * @param typeFragment
     * @param korThaiDicDtoList
     * @param korThaiDicDto
     */
    private void setBodyFrag(int typeFragment, final ArrayList<KorThaiDicDto> korThaiDicDtoList, final KorThaiDicDto korThaiDicDto) {
        DebugUtil.showDebug("MainAct setBodyFrag()");

        switch (typeFragment) {
            case KorThaiDicConstantUtil.TYPE_FRAG_MAIN.LIST_ITEM:
                if (start != 0) {
                    // page
                    if (listItemFrag != null) {
                        DebugUtil.showDebug("setBodyFrag, listItemFrag");
                        for (KorThaiDicDto korTai : korThaiDicDtoList) {
                            this.korThaiDicDtoList.add(korTai);
                        }
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                listItemFrag.addKorThaiDicDtoList(MainAct.this.korThaiDicDtoList);
                            }
                        });
                    }
                } else {
                    // first page
                    if (bodyFl.getChildCount() > 0)
                        bodyFl.removeAllViews();
                    this.korThaiDicDtoList = korThaiDicDtoList;
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            listItemFrag = new ListItemFrag().newInstance(MainAct.this, MainAct.this, MainAct.this);
                            listItemFrag.setKorThaiDicDtoList(MainAct.this.korThaiDicDtoList);
                            bodyFl.addView(listItemFrag.getView());
                            fragData = null;
                        }
                    });
                    // initialize start (int) by korThaiDicDtoList.size()
                    if (this.korThaiDicDtoList.size() > 10)
                        start += this.korThaiDicDtoList.size();
                }
                break;
            case KorThaiDicConstantUtil.TYPE_FRAG_MAIN.DETAIL_ITEM:
                hideKeyboard(et);
                // with back framelayout
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        DetailItemFrag detailItemFrag = new DetailItemFrag().newInstance(MainAct.this, MainAct.this, MainAct.this);
                        if (korThaiDicDto != null)
                            detailItemFrag.setKorThaiDicDto(korThaiDicDto, MainAct.this);
                        bodyFl.addView(detailItemFrag.getView());
                    }
                });
                start = 0;
                break;
            case KorThaiDicConstantUtil.TYPE_FRAG_MAIN.UPDATE_LIST_ITEM:
                start = start + limit;
                executeSearch(currentKeyword);
                break;
        }

    }

    /**
     * hide keyboard
     *
     * @param et
     */
    public void hideKeyboard(EditText et) {
        DebugUtil.showDebug("MainAct hideKeyboard()");
        //et.setFocusable(true);
        if (this.getCurrentFocus() == null)
            return;
        DebugUtil.showDebug("getCurrentFocus() not null");
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

    // about network

    /**
     * check parameter
     *
     * @param idx
     * @return
     */
    public boolean checkParams(int idx) {
        DebugUtil.showDebug("MainAct checkParams()");
        boolean flag = true;

        switch (idx) {
            case NetworkConstantUtil.API_IDX._1_GET_SERVICE_INFORMATION:
                if (TextUtil.isNull(os))
                    flag = false;
                break;
        }

        return flag;
    }

    /**
     * request to server
     *
     * @param idx
     */
    public void request(int idx) {
        DebugUtil.showDebug("MainAct request()");
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

    /**
     * check NetworkResponse
     *
     * @param idx
     * @param networkResponse
     */
    public void checkNetworkResponse(int idx, NetworkResponse networkResponse) {
        DebugUtil.showDebug("MainAct checkNetworkResponse()");

        boolean result = networkResponse.isRESULT();
        int resultCode = networkResponse.getRESULTCODE();

        if (result) {
            resultIsTrue(idx, resultCode, networkResponse);
        } else {
            resultIsFalse(idx, resultCode);
        }

    }

    /**
     * when result is true
     *
     * @param idx
     * @param resultCode
     * @param networkResponse
     */
    public void resultIsTrue(int idx, int resultCode, NetworkResponse networkResponse) {
        DebugUtil.showDebug("MainAct resultIsTrue()");
        switch (resultCode) {
            case NetworkConstantUtil.RESULT_CODE.SUCCESS:
                DebugUtil.showDebug("MainAct SUCCESS");
                checkNetworkData(idx, networkResponse);
                break;
        }
    }

    /**
     * when result is false
     *
     * @param idx
     * @param resultCode
     */
    public void resultIsFalse(int idx, int resultCode) {
        DebugUtil.showDebug("MainAct resultIsFalse()");
        switch (resultCode) {
            case NetworkConstantUtil.RESULT_CODE.EMPTY_PARAMETER_THAT_IS_ESSENTIAL:
                DebugUtil.showDebug("MainAct EMPTY_PARAMETER_THAT_IS_ESSENTIAL");
                break;
            case NetworkConstantUtil.RESULT_CODE.ERROR_OF_CONNENTING_WITH_DB_OR_EXECUTE_QUERY:
                DebugUtil.showDebug("MainAct ERROR_OF_CONNENTING_WITH_DB_OR_EXECUTE_QUERY");
                break;
        }

    }

    /**
     * check NetworkData
     *
     * @param idx
     * @param networkResponse
     */
    public void checkNetworkData(int idx, NetworkResponse networkResponse) {
        DebugUtil.showDebug("MainAct checkNetworkData()");

        switch (idx) {
            case NetworkConstantUtil.API_IDX._1_GET_SERVICE_INFORMATION:
                NetworkData serviceInfo = networkResponse.getSERVICEINFO();
                serviceInfoDto = new ServiceInfoDto();
                serviceInfoDto.setCURVERCD_P(serviceInfo.getCURVERCD_P());
                serviceInfoDto.setCURVERNM_P(serviceInfo.getCURVERNM_P());
                serviceInfoDto.setDBCURDTPATH_P(serviceInfo.getDBCURDTPATH_P());
                serviceInfoDto.setDBCURVERCD_P(serviceInfo.getDBCURVERCD_P());
                serviceInfoDto.setOS(serviceInfo.getOS());
                serviceInfoDto.setSTORE_URL_P(serviceInfo.getSTORE_URL_P());

//                DebugUtil.showDebug("MainAct " + serviceInfoDto.toString());
//                DebugUtil.showDebug("MainAct versionCode : " + versionCode + " / versionName : " + versionName + " / databaseversion : " + DatabaseConstantUtil.getDatabaseVersion());
//
//                if (serviceInfo.getDBCURVERCD_P() > DatabaseConstantUtil.getDatabaseVersion()) {
//                    DebugUtil.showDebug("MainAct show alert");
//                    showAlertDialog();
//                }

                /**
                 * compare app's version code
                 */
                //서버 상의 어플 버젼
                DebugUtil.showDebug("MainAct checkNetworkData, app's version code: " + serviceInfoDto.getCURVERCD_P());

                //현재 설치되어있는 사용자의 어플 버젼
                PackageInfo pInfo = null;
                try {
                    pInfo = getPackageManager().getPackageInfo(
                            this.getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                int versionCode = pInfo.versionCode;
                String versionName = pInfo.versionName;
                DebugUtil.showDebug("MainAct checkNetworkData, app's new version code: " + versionCode);

                //비교
                if (versionCode < serviceInfoDto.getCURVERCD_P())
                    showAlertDialog();


                break;
        }

    }

    @Override
    public void onNetworkStart(int idx) {
        DebugUtil.showDebug("MainAct onNetworkStart()");
        switch (idx) {
            case NetworkConstantUtil.API_IDX._1_GET_SERVICE_INFORMATION:
                DebugUtil.showDebug("NetworkIntentService _1_GET_SERVICE_INFORMATION");
                break;
        }

    }

    @Override
    public void onNetworkResult(int idx, NetworkResponse networkResponse) {
        DebugUtil.showDebug("MainAct onNetworkResult()");

        checkNetworkResponse(idx, networkResponse);

    }

    @Override
    public void onNetworkError(int idx, String errorMessage) {
        DebugUtil.showDebug("MainAct onNetworkError()");
        switch (idx) {
            case NetworkConstantUtil.API_IDX._1_GET_SERVICE_INFORMATION:
                DebugUtil.showDebug("NetworkIntentService _1_GET_SERVICE_INFORMATION");
                break;
        }
        DebugUtil.showDebug("MainAct " + errorMessage);
    }

    // about alert dialog

    /**
     * create AlertDialog and show this.
     */
    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(getResources().getString(R.string.alert_dlgfrag_title_type1));
        builder.setMessage(getResources().getString(R.string.alert_dlgfrag_content_type1));

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DebugUtil.showDebug("positive");
                if (serviceInfoDto != null && !TextUtil.isNull(serviceInfoDto.getDBCURDTPATH_P())) {
//                    downloadAsync = new DownloadAsync(MainAct.this, MainAct.this, serviceInfoDto.getDBCURVERCD_P());
//                    downloadAsync.execute(serviceInfoDto.getDBCURDTPATH_P());
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pname:com.ph.greenkorthaidictionary"));
//                    startActivity(intent);
                    Util.setupApp(DeviceUtil.getPkgName(MainAct.this), MainAct.this);
                }

            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DebugUtil.showDebug("nagative");
                isCheckedUserDecision = 1;
            }
        });

        builder.create();
        builder.show();
    }

    public boolean checkIfGoogleTtsHasBeenInstalled() {
        //설치된 패키지 돌면서 google android tts 설치되어있는지 확인
        doesTtsExists = false;
        final PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            if ("com.google.android.tts".equalsIgnoreCase(packageInfo.packageName)) {
                doesTtsExists = true;
                DebugUtil.showDebug("tts, :: com.google.android.tts 설치 되어있음");
                break;
            }
        }

        return doesTtsExists;
    }

    public void installGoogleTts() {
        android.support.v7.app.AlertDialog alertDialog = new android.support.v7.app.AlertDialog.Builder(this).create();
        alertDialog.setTitle("Install Google TTS Engine");
        alertDialog.setMessage("If you want to use a Pronunciation Service, You must install the google tts engine. \nWould you like to go to the install page?");
        alertDialog.setButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //패키지 최신 버젼으로 업데이트하기
                        Intent intentUpdate = new Intent(Intent.ACTION_VIEW);
                        intentUpdate.setData(Uri.parse("market://details?id=" + "com.google.android.tts"));
                        if (this == null) {
                            DebugUtil.showDebug("parentAct is null");
                        } else {
                            DebugUtil.showDebug("parentAct is not null ");
                            MoveActUtil.moveActivity(MainAct.this, intentUpdate, -1, -1, false, false);
                        }
                    }
                });
        alertDialog.setButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE, "No",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void updateGoogleTts() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo("com.google.android.tts", 0);
            DebugUtil.showDebug("com.google.android.tts 버젼 : " + packageInfo.versionName + ", versionCode : " + packageInfo.versionCode);

            //버젼 업데이트하기
            if (packageInfo.versionCode < 210308140) {
                DebugUtil.showDebug("com.google.android.tts 버젼이 낮음 ");

                android.support.v7.app.AlertDialog alertDialog = new android.support.v7.app.AlertDialog.Builder(this).create();
                alertDialog.setTitle("Update Google TTS Engine");
                alertDialog.setMessage("If you want to use a Pronunciation Service, You must update the google tts engine. \n" +
                        "Would you like to go to the update page?");
                alertDialog.setButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE, "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                //패키지 최신 버젼으로 업데이트하기
                                Intent intentUpdate = new Intent(Intent.ACTION_VIEW);
                                intentUpdate.setData(Uri.parse("market://details?id=" + "com.google.android.tts"));
                                if (this == null) {
                                    DebugUtil.showDebug("parentAct is null");
                                } else {
                                    DebugUtil.showDebug("parentAct is not null ");
                                    MoveActUtil.moveActivity(MainAct.this, intentUpdate, -1, -1, false, false);
                                }
                            }
                        });
                alertDialog.setButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE, "No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            } else {
                DebugUtil.showDebug("tts, :: tts버젼이 올바르게 설정되어 있습니다. ");
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

<<<<<<< HEAD



=======



    public static class TtsThaiListener implements TextToSpeech.OnInitListener {
        public static int status;
        @Override
        public void onInit(int status) {
            this.status = status;
        }
    }
>>>>>>> origin/master
}
