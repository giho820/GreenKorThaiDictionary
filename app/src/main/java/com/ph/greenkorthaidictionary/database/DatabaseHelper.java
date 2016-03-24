package com.ph.greenkorthaidictionary.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ph.greenkorthaidictionary.database.util.DatabaseConstantUtil;
import com.ph.greenkorthaidictionary.database.util.DatabaseUtil;
import com.ph.greenkorthaidictionary.util.DebugUtil;

/**
 * Created by preparkha on 15. 6. 8..
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper databaseHelper;
    public static SQLiteDatabase sqLiteDatabase;
    private static Context context;

    /**
     * super (Context, String name (datanase name), SQLiteDatabase.CursorFactory factory, int version (com.ph.korthaidictionary.database vesion);
     * 이 부분은 SQLiteOpenHelper 의 constructor 에 대응되게 만들어놓은 것임.
     *
     * @param context
     */
    private DatabaseHelper(Context context) {
        super(context, DatabaseConstantUtil.DATABASE_DB_NAME, null, DatabaseConstantUtil.DATABASE_VERSION);
        DebugUtil.showDebug("constructor : " + DatabaseConstantUtil.DATABASE_DB_NAME + " / " + DatabaseConstantUtil.DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstacnce(Context context) {

        DebugUtil.showDebug("getInstance()");

//        if (databaseHelper == null)
        databaseHelper = new DatabaseHelper(context.getApplicationContext());

        DatabaseHelper.context = context;
        DatabaseHelper.sqLiteDatabase = databaseHelper.getReadableDatabase();
        return databaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        DebugUtil.showDebug("DatabaseHelper onCreate()");

        if (!DatabaseUtil.checkDatabase()) {
            DebugUtil.showDebug("Database is not existed");

        } else {
            DebugUtil.showDebug("Database is existed");
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        DebugUtil.showDebug("DatabaseHelper onUpgrade()");

        db.execSQL("DROP TABLE IF EXISTS " + DatabaseConstantUtil.TABLE_KOR_THAI_NAME);

        //onCreate(db);

    }

    @Override
    public synchronized void close() {

        if (sqLiteDatabase != null)
            sqLiteDatabase.close();

        super.close();

    }


}
