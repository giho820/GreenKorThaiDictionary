package com.ph.greenkorthaidictionary.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.ph.greenkorthaidictionary.data.dto.KorThaiDicDto;
import com.ph.greenkorthaidictionary.database.DatabaseCRUD;
import com.ph.greenkorthaidictionary.database.DatabaseHelper;
import com.ph.greenkorthaidictionary.database.util.DatabaseConstantUtil;
import com.ph.greenkorthaidictionary.util.DebugUtil;

import java.util.ArrayList;

/**
 * Created by preparkha on 15. 6. 28..
 */
public class QueryIntentService extends IntentService{

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private ResultReceiver receiver = null;
    private String keyword;
    private int start;
    private int limit;

    private ArrayList<KorThaiDicDto> korThaiDicDtoList;

    private boolean isRunning = true;

    public QueryIntentService() {
        super("Query intent service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        keyword = intent.getStringExtra("keyword");
        receiver = intent.getParcelableExtra("receiver");
        start = intent.getIntExtra("start", 0);
        limit = intent.getIntExtra("number", 10);

        DebugUtil.showDebug("keyword : " + keyword);

        korThaiDicDtoList = getList(keyword);

    }

    @Override
    public void onDestroy() {
        DebugUtil.showDebug("onDestory()");

        Bundle bundle = new Bundle();
        bundle.putSerializable("KorThaiDicDtoList", korThaiDicDtoList);

        receiver.send(500, bundle);

    }

    private ArrayList<KorThaiDicDto> getList(String keyword) {

        for (int i = 0; i < keyword.length(); i++) {
            char tempChar = keyword.charAt(i);
            int tempInt = (int) tempChar;
            if(tempInt == 39) {
                DebugUtil.showDebug("keyword include ''.");
                return null;
            }
        }

        ArrayList<KorThaiDicDto> korThaiDicDtoList;

        DatabaseHelper databaseHelper = DatabaseHelper.getInstacnce(this.getApplicationContext());

        if(keyword.length() > 1 && keyword.charAt(0) == '*' ){
            String reFormKeyword = keyword.substring(1, keyword.length());
            DebugUtil.showDebug("whild / " + reFormKeyword);

            String thirdQuery = "SELECT "
                    + DatabaseConstantUtil.COLUMN_NAME_KOREAN + ", "
                    + DatabaseConstantUtil.COLUMN_NAME_THAI + ", "
                    + DatabaseConstantUtil.COLUMN_NAME_PRONUNCIATION + " "
                    + "FROM " + DatabaseConstantUtil.TABLE_KOR_THAI_NAME + " "
                    + "WHERE TRIM(" + DatabaseConstantUtil.COLUMN_NAME_PRONUNCIATION + ") LIKE '%" + reFormKeyword + "%'" + " ESCAPE '/' "
                    + "LIMIT " + start + ", " + limit;
//                + "AND LIMIT " + start + ", " + limit
//                + "AND IDX LIMIT " + start + ", " + limit;
            DebugUtil.showDebug(thirdQuery);

            korThaiDicDtoList = DatabaseCRUD.selectListDb(databaseHelper, thirdQuery, reFormKeyword);

            return korThaiDicDtoList;
        }

        String secondQuery = "SELECT "
                + DatabaseConstantUtil.COLUMN_NAME_KOREAN + ", "
                + DatabaseConstantUtil.COLUMN_NAME_THAI + ", "
                + DatabaseConstantUtil.COLUMN_NAME_PRONUNCIATION + " "
                + "FROM " + DatabaseConstantUtil.TABLE_KOR_THAI_NAME + " "
                + "WHERE TRIM(" + DatabaseConstantUtil.COLUMN_NAME_PRONUNCIATION + ") LIKE '" + keyword + "%'" + " "
                + "LIMIT " + start + ", " + limit;
//                + "AND LIMIT " + start + ", " + limit
//                + "AND IDX LIMIT " + start + ", " + limit;
        DebugUtil.showDebug(secondQuery);

        korThaiDicDtoList = DatabaseCRUD.selectListDb(databaseHelper, secondQuery, keyword);

        return korThaiDicDtoList;
    }

}
