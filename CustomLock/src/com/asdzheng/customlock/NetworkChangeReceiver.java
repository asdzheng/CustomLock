/**
 * 
 */

package com.asdzheng.customlock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.widget.Toast;

/**
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
            Intent i = new Intent(context, KeyGuardService.class);
            context.startService(i);
//            Toast.makeText(context, "连接状态改变", Toast.LENGTH_SHORT).show();
        } else if (intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION)) {
            Intent i = new Intent(context, KeyGuardService.class);
            context.startService(i);
//            Toast.makeText(context, "WIFI信息改变", Toast.LENGTH_SHORT).show();
        } else if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent i = new Intent(context, KeyGuardService.class);
            context.startService(i);
        }
    }
}
