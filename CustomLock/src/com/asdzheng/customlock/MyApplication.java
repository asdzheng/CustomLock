/**
 * 
 */

package com.asdzheng.customlock;

import android.app.Application;
import android.content.Context;

/**
 * @author [zWX232618/郑加波] 2015-4-7
 */
public class MyApplication extends Application {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        context = getApplicationContext();
        super.onCreate();
    }

}
