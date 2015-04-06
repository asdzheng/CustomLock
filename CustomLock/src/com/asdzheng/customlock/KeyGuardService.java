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
        	if(PrefencesUtil.getInstance(this).haveTrustWifi()) {
        		  String save_wifi = PrefencesUtil.getInstance(this).getString(
                          PrefencesUtil.PREFENCE_KEY, "");
//                  save_wifi = save_wifi.substring(1, save_wifi.length() - 1);
                  Log.i("KeyGuardService", "PREFENCE_KEY " + save_wifi);
                  if (save_wifi.equals(wifiManager.getConnectionInfo().getSSID())) {
                      Log.d("KeyGuardService", "wifi disableKeyGuard");

                      KeyGuardUtil.getInstance(this).disableKeyGuard();
                  } else {
                      Log.d("KeyGuardService", "wifi reEnableKeyGuard");

                      KeyGuardUtil.getInstance(this).reEnableKeyGuard();
                  }

        	}
          
        } else {
            Log.d("KeyGuardService", "wifi unable");

            KeyGuardUtil.getInstance(this).reEnableKeyGuard();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
