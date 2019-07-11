package dsppa.com.logtool;

import android.app.Application;

import dsppa.com.library.LogManager;

public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        //debug
        LogManager.getInstance().init(getApplicationContext());
    }
}
