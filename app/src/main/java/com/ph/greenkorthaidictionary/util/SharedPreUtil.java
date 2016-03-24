package com.ph.greenkorthaidictionary.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

import com.ph.greenkorthaidictionary.GreenKorThaiDicApplication;

public class SharedPreUtil implements OnSharedPreferenceChangeListener {

	private SharedPreferences sharedPrefs;
	private SharedPreferences.Editor sharedPrefsEditor;

	private static SharedPreUtil instance = null;

	public final static String DONAS_SHARED_PRE = "EDUvance_shared_pref";

	public static SharedPreUtil getInstance() {
		if (instance == null) {
			instance = new SharedPreUtil();
		}
		return instance;
	}

	public SharedPreferences getSharedPrefs() {
		return sharedPrefs;
	}

	private SharedPreUtil() {
		sharedPrefs = GreenKorThaiDicApplication.getContext().getSharedPreferences(DONAS_SHARED_PRE, Context.MODE_PRIVATE);
		sharedPrefsEditor = sharedPrefs.edit();
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		
	}

	public void putPreference(String key, boolean value) {
		sharedPrefsEditor.putBoolean(key, value);
		sharedPrefsEditor.commit();
	}
	
	public void putPreference(String key, String value) {
		sharedPrefsEditor.putString(key, value);
		sharedPrefsEditor.commit();
	}
	
	public void putPreference(String key, int value) {
		sharedPrefsEditor.putInt(key, value);
		sharedPrefsEditor.commit();
	}
	
	public boolean getBooleanPreference(String key){
		return sharedPrefs.getBoolean(key, false);
	}
	
	public String getStringPreference(String key){
		return sharedPrefs.getString(key, null);
	}
	
	public int getIntPreference(String key){
		return sharedPrefs.getInt(key, 0);
	}
	
	public void removePreference(String key){
		sharedPrefsEditor.remove(key);
		sharedPrefsEditor.commit();
	}
	
	
}
