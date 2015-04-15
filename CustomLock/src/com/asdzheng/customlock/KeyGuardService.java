/**
 * 
 */

package com.asdzheng.customlock;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.asdzheng.customlock.util.KeyGuardUtil;
import com.asdzheng.customlock.util.LogUtil;
import com.asdzheng.customlock.util.PrefencesUtil;
import com.asdzheng.customlock.util.WifiUtil;

/**
 */
public class KeyGuardService extends Service {

    private WifiUtil wifiUtil;
    private PrefencesUtil presUtil;
    private KeyGuardUtil keyGuadrdUtil;
    private BroadcastReceiver stateRecevier;

    private boolean isLock;

    @Override
    public void onCreate() {
        wifiUtil = WifiUtil.getInstance();
        keyGuadrdUtil = KeyGuardUtil.getInstance();
        presUtil = PrefencesUtil.getInstance();

        isLock = true;

        addScreenRecevier();

        super.onCreate();

    }

    private void addScreenRecevier() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);

        stateRecevier = new DiffStateReceiver();
        registerReceiver(stateRecevier, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        LogUtil.i("KeyGuardService == ", "service start !!");

        if (presUtil.isSystemException()) {
            if (Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {
                LogUtil.w("KeyGuardService == ", "ACTION_USER_PRESENT !!");
                presUtil.systemNormal();
            }
            return super.onStartCommand(intent, flags, startId);
        }

        if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
            reEnableKeyGuard();
            disableKeyGuard();
        }

        checkKeyGuardable();
        return START_STICKY;
    }

    private void checkKeyGuardable() {
        if (wifiUtil.isWifiEnable()) {
            if (presUtil.isTrustSsid(wifiUtil.getCurrentWifiSSID())) {
                LogUtil.i("KeyGuardService", "wifi ssid have save");
                disableKeyGuard();
            } else {
                LogUtil.i("KeyGuardService", "wifi ssid not the save");
                reEnableKeyGuard();
            }

        } else {
            LogUtil.i("KeyGuardService", "wifi unable");
            reEnableKeyGuard();
        }
    }

    private void disableKeyGuard() {
        if (isLock) {
            keyGuadrdUtil.disableKeyGuard();
            isLock = false;
            LogUtil.i("KeyGuardService", "isLock ======= " + String.valueOf(isLock));

        }
    }

    private void reEnableKeyGuard() {
        if (!isLock) {
            keyGuadrdUtil.reEnableKeyGuard();
            isLock = true;
            LogUtil.i("KeyGuardService", "isLock ======= " + String.valueOf(isLock));
        }
    }

    @Override
    public void onDestroy() {
        LogUtil.w("KeyGuardService", "onDestroy");
        reEnableKeyGuard();

        unregisterReceiver(stateRecevier);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
