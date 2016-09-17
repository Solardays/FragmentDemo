package com.jc.fragmentdemo.base;

import android.app.Application;

/**
 * Created by cc on 2016/9/14.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
    }
}
