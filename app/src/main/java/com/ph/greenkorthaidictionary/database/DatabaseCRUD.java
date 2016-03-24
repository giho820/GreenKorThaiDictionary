package com.ph.greenkorthaidictionary.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ph.greenkorthaidictionary.data.dto.KorThaiDicDto;
import com.ph.greenkorthaidictionary.database.util.DatabaseConstantUtil;
import com.ph.greenkorthaidictionary.util.DebugUtil;

import java.util.ArrayList;

/**
 * Created by preparkha on 15. 6. 20..
 */
public class DatabaseCRUD {

    private static SQLiteDatabase sqLiteDatabase;
    private static Cursor cursor;
    private static String keyword;

    public static boolean checkTable(DatabaseHelper databaseHelper, SQLiteDatabase sqLiteDatabase, String tableName) {

        boolean flag = true;

        if(sqLiteDatabase == null){
            DebugUtil.showDebug("SQLiteDatabase is null");
            flag = false;
            return flag;
        }

        String sql_check_whether_table_exist_or_not = "SELECT count(*) as check_table FROM sqlite_master WHERE type='table' AND name='" + tableName + "'";
        Cursor cursor = sqLiteDatabase.rawQuery(sql_check_whether_table_exist_or_not, null);
        cursor.moveToFirst();

        DebugUtil.showDebug("cursor count : " + cursor.getInt(cursor.getColumnIndex("check_table")));
        if (cursor.getInt(cursor.getColumnIndex("check_table")) != 1) {
            flag = false;
        }
        DebugUtil.showDebug("flag : " + flag);


        return flag;

    }

    public static void insertDd(DatabaseHelper databaseHelper, String tableName, KorThaiDicDto korThaiDicDto) {

        sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("kor", korThaiDicDto.getKor());
        values.put("thai", korThaiDicDto.getThai());
        values.put("pronu", korThaiDicDto.getPronu());

        sqLiteDatabase.insert(tableName, null, values);

    }

    public static String selectDb(DatabaseHelper databaseHelper, String selectSql) {

        sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(selectSql, null);
        cursor.moveToFirst();

        String kor = cursor.getString(cursor.getColumnIndex("kor"));

        return kor;
    }

    public static ArrayList<KorThaiDicDto> selectListDb(DatabaseHelper databaseHelper, String selectSql, String keyword) {

        ArrayList<KorThaiDicDto> list = new ArrayList<KorThaiDicDto>();

        sqLiteDatabase = databaseHelper.getReadableDatabase();

        cursor = sqLiteDatabase.rawQuery(selectSql, null);

        if (cursor == null)
            return null;

        if (cursor.getCount() <= 0)
            return null;

        cursor.moveToFirst();

        KorThaiDicDto korThaiDicDto = new KorThaiDicDto();
        korThaiDicDto.setKor(cursor.getString(cursor.getColumnIndex(DatabaseConstantUtil.COLUMN_NAME_KOREAN)));
        korThaiDicDto.setThai(cursor.getString(cursor.getColumnIndex(DatabaseConstantUtil.COLUMN_NAME_THAI)));
        String tempPronu = cursor.getString(cursor.getColumnIndex(DatabaseConstantUtil.COLUMN_NAME_PRONUNCIATION));
        String pronu = tempPronu.replaceAll(keyword, "<b>" + keyword + "</b>");
        korThaiDicDto.setPronu(pronu);

        //DebugUtil.showDebug("row : " + korThaiDicDto.toString());

        list.add(korThaiDicDto);

        while (cursor.moveToNext()) {

            korThaiDicDto = new KorThaiDicDto();
            korThaiDicDto.setKor(cursor.getString(cursor.getColumnIndex(DatabaseConstantUtil.COLUMN_NAME_KOREAN)));
            korThaiDicDto.setThai(cursor.getString(cursor.getColumnIndex(DatabaseConstantUtil.COLUMN_NAME_THAI)));
            tempPronu = cursor.getString(cursor.getColumnIndex(DatabaseConstantUtil.COLUMN_NAME_PRONUNCIATION));
            pronu = tempPronu.replaceAll(keyword, "<b>" + keyword + "</b>");
            korThaiDicDto.setPronu(pronu);

            //DebugUtil.showDebug("row : " + korThaiDicDto.toString());

            list.add(korThaiDicDto);

        }

        cursor.close();

        return list;
    }
}
