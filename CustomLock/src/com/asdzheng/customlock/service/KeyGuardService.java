/**
 * 
 */

package com.asdzheng.customlock.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;

import com.asdzheng.customlock.util.KeyGuardUtil;
import com.asdzheng.customlock.util.LogUtil;
import com.asdzheng.customlock.util.PrefencesUtil;
import com.asdzheng.customlock.util.WifiUtil;

/**
 */
public class KeyGuardService extends Service {

    private static final String TAG = KeyGuardService.class.getSimpleName();

    private WifiUtil wifiUtil;
    private PrefencesUtil presUtil;
    private KeyGuardUtil keyGuadrdUtil;

    private boolean isLock;

    private final Handler mHandler = new Handler();

    @Override
    public void onCreate() {
        wifiUtil = WifiUtil.getInstance();
        keyGuadrdUtil = KeyGuardUtil.getInstance();
        presUtil = PrefencesUtil.getInstance();

        isLock = true;
        addUserPresentRecevier();
        super.onCreate();
    }

    /**
     * 点亮屏幕的的广播不能再AndroidMainfest里面注册，只能动态注册
     */
    private void addUserPresentRecevier() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(userPresentReceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        LogUtil.i(TAG, "service start !!");

        // 开机后必须先解锁一次，信任wifi才开启，不然会出现按home键跳到锁屏界面
        if (!presUtil.isJustShutdown()) {
            checkKeyGuardable();
        }
        return START_STICKY;

    }

    /**
     * 判断是否需要显示解锁界面
     */
    private void checkKeyGuardable() {
        // wifi是否可用
        if (wifiUtil.isWifiConnected()) {
            // 连接的wifi是否与pres里保存的ssid是否一致，是就说明是在信任的区域内，不用解锁
            if (presUtil.isTrustSsid(wifiUtil.getCurrentWifiSSID())) {
                LogUtil.i(TAG, "wifi ssid have save");
                disableKeyGuard();
            } else {
                LogUtil.i(TAG, "wifi ssid not the save");
                reEnableKeyGuard();
            }

        } else {
            LogUtil.i(TAG, "wifi unable");
            reEnableKeyGuard();
        }
    }

    /**
     * 如果现在有解锁界面，隐藏掉
     * disableKeyGuard和reEnableKeyGuard需要结对出现，如果多次disabled，然后reenable或者，可能会出现bug
     */
    private void disableKeyGuard() {
        if (isLock) {
            keyGuadrdUtil.disableKeyGuard();
            isLock = false;
            LogUtil.i(TAG, "isLock ======= " + String.valueOf(isLock));
        }
    }

    /**
     * 如果现在解锁界面是隐藏状态，重新显示
     */
    private void reEnableKeyGuard() {
        if (!isLock) {
            keyGuadrdUtil.reEnableKeyGuard();
            isLock = true;
            LogUtil.i(TAG, "isLock ======= " + String.valueOf(isLock));
        }
    }

    /**
     * 停掉服务时需要重新显示解锁界面，和注销掉广播
     */
    @Override
    public void onDestroy() {
        LogUtil.w(TAG, "onDestroy");
        reEnableKeyGuard();
        unregisterReceiver(userPresentReceiver);
        mHandler.removeCallbacks(runDisableKeyguard);

        super.onDestroy();
    }

    private final Runnable runDisableKeyguard = new Runnable() {
        public void run() {
            disableKeyGuard();
        }
    };

    private final BroadcastReceiver userPresentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {
                LogUtil.w(TAG, "mUserPresentReceiver onreceive");

                if (wifiUtil.isWifiConnected() && presUtil.isTrustSsid(wifiUtil.getCurrentWifiSSID())) {
                    // 如果在信任wifi的情况下还需要解锁，是的话就为异常情况
                    LogUtil.w(TAG, "inKeyguardRestrictedInputMode ");

                    // 出现异常时disableKeyguard是无作用的，需要等手动开锁后，重新显示解锁界面后，在隐藏，才有效果
                    reEnableKeyGuard();
                    mHandler.postDelayed(runDisableKeyguard, 300);
                }

                // 开机启动后解锁生效信任wifi的功能
                if (presUtil.isJustShutdown()) {
                    checkKeyGuardable();
                    presUtil.haveStartUp();
                }
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
