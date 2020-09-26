package com.prank.sexygirlcallprank;

import android.content.ContextWrapper;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.facebook.ads.AudienceNetworkAds;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.FirebaseDatabase;
import com.prank.sexygirlcallprank.helper.Prefs;


public class MyApplication extends MultiDexApplication {
    FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(MyApplication.this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(MyApplication.this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        MultiDex.install(MyApplication.this);
        AudienceNetworkAds.initialize(this);
        new Prefs.Builder()
                .setContext(MyApplication.this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
    }
}