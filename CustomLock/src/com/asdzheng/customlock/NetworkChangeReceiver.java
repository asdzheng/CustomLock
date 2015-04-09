/**
 * 
 */

package com.asdzheng.customlock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

/**
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
            startService(context);
//            Toast.makeText(context, "连接状态改变", Toast.LENGTH_SHORT).show();
        } else if (intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION)) {
            startService(context);
//            Toast.makeText(context, "WIFI信息改变", Toast.LENGTH_SHORT).show();
        } else if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
//            Toast.makeText(context, "开机成功", Toast.LENGTH_SHORT).show();

            startService(context);
        } else if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
//            Toast.makeText(context, "显示屏幕", Toast.LENGTH_SHORT).show();
            Log.d("NetworkChangeReceiver", "显示屏幕");
            startService(context);

        }
    }

    private void startService(Context context) {
        Intent i = new Intent(context, KeyGuardService.class);
        context.startService(i);
    }

}
