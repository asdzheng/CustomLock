/**
 * 
 */

package com.asdzheng.customlock.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.asdzheng.customlock.service.KeyGuardService;
import com.asdzheng.customlock.util.LogUtil;
import com.asdzheng.customlock.util.PrefencesUtil;
import com.asdzheng.customlock.util.WifiUtil;

/**
 */
public class DiffStateReceiver extends BroadcastReceiver {

    private static final String TAG = DiffStateReceiver.class.getSimpleName();

    private WifiUtil wifiUtil = WifiUtil.getInstance();
    private PrefencesUtil presUtil = PrefencesUtil.getInstance();

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            LogUtil.w(TAG, "开机成功啦啦啦啦");
            startService(context);
        }

        else if (action.equals(Intent.ACTION_SHUTDOWN)) {
            LogUtil.w(TAG, "关机啦啦啦啦啦");
            presUtil.shutDown();
            stopService(context);
        }

        // 在wifi打开的情况下，移出wifi区域取回会触发广播，从connecting 到 conneted或者disconnected
        else if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
            NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            wifiUtil.setNewWorkState(networkInfo.getState());
            startService(context);

            LogUtil.w(TAG, "NETWORK_STATE_CHANGED_ACTION ====  " + networkInfo.getState());

        }
    }

    private void startService(Context context) {
        Intent i = new Intent(context, KeyGuardService.class);
        context.startService(i);
    }

    private void stopService(Context context) {
        Intent i = new Intent(context, KeyGuardService.class);
        context.stopService(i);
    }

}
