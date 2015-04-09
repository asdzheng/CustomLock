/**
 * 
 */

package com.asdzheng.customlock;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;

/**
 */
public class KeyGuardService extends Service {

    WifiManager wifiManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        Log.e("KeyGuardService!!!!!!!!!!!!!", "start!!!!!!!!");

        if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {

            Log.i("KeyGuardService", "connect info " + wifiManager.getConnectionInfo().getSSID());
            if (PrefencesUtil.getInstance().isTrustWifi(wifiManager.getConnectionInfo().getSSID())) {
                Log.d("KeyGuardService", "wifi disableKeyGuard");
                KeyGuardUtil.getInstance().disableKeyGuard();
            } else {
                Log.d("KeyGuardService", "wifi reEnableKeyGuard");
                KeyGuardUtil.getInstance().reEnableKeyGuard();

            }

        } else {
            Log.d("KeyGuardService", "wifi unable");

            KeyGuardUtil.getInstance().reEnableKeyGuard();
        }

        return super.onStartCommand(intent, START_STICKY, startId);
    }

    @Override
    public void onDestroy() {
        Log.e("KeyGuardService", "onDestroy");

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
