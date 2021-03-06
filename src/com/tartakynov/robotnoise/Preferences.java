package com.tartakynov.robotnoise;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;

/**
 * @author Artem Tartakynov
 * Current application preferences
 */
public class Preferences implements OnSharedPreferenceChangeListener {

    public interface OnPreferenceChangeListener {

	void onPreferenceChanged(Preferences pref);

    }

    private static final int DEFAULT_ANGLE = 180;	

    private static volatile Preferences sInstance = null;	

    private final SharedPreferences mPref;

    private final Editor mEdit;

    private final ArrayList<OnPreferenceChangeListener> mOnPrefChangeListeners = 
	    new ArrayList<OnPreferenceChangeListener>();

    public static Preferences Open(Context context) {
	if (sInstance == null) {
	    synchronized (Preferences.class) {
		if (sInstance == null) {
		    sInstance = new Preferences(context);
		}
	    }
	}
	return sInstance;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences pref, String key) {
	for (OnPreferenceChangeListener listener : this.mOnPrefChangeListeners) {
	    listener.onPreferenceChanged(this);
	}
    }

    public void registerPreferenceChangeListener(OnPreferenceChangeListener listener) {
	this.mOnPrefChangeListeners.add(listener);
    }

    private Preferences(Context context) {
	mPref = PreferenceManager.getDefaultSharedPreferences(context);
	mEdit = mPref.edit();
	mPref.registerOnSharedPreferenceChangeListener(this);
    }	

    public int getAngle() {
	return mPref.getInt("angle", DEFAULT_ANGLE);
    }

    public void setAngle(int angle) {
	if (angle >= 0) {
	    mEdit.putInt("angle", angle);		
	    mEdit.commit();				
	}
    }

    public boolean isFeedbackRequested() {
	return mPref.getBoolean("feedbackRequested", false);
    }

    public void setFeedbackRequested() {
	mEdit.putBoolean("feedbackRequested", true);		
	mEdit.commit();					
    }
}
