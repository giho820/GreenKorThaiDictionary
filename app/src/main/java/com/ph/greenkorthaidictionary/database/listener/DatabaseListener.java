package com.ph.greenkorthaidictionary.database.listener;

/**
 * Created by preparkha on 15. 6. 1..
 */
public interface DatabaseListener {

    void onDatabaseStart(int idx);
    void onDatabaseResult(int idx, Object result);
    void onDatabaseError(int idx, String errorMessage);

}
