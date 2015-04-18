/**
 * 
 */

package com.asdzheng.customlock.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

import com.asdzheng.customlock.service.KeyGuardService;
import com.asdzheng.customlock.util.KeyGuardUtil;
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

        if (action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
            startService(context);
            LogUtil.i(TAG, "连接状态改变" + wifiUtil.getWifiState());
        } else if (action.equals(WifiManager.RSSI_CHANGED_ACTION)) {
            startService(context);
            LogUtil.i(TAG, "WIFI信息改变  == " + wifiUtil.getWifiState());
        } else if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            LogUtil.w(TAG, "开机成功啦啦啦啦");
            startService(context, action);
        } else if (action.equals(Intent.ACTION_SCREEN_ON)) {
            LogUtil.i(TAG, "点亮屏幕  ");
            // 是否是在信任wifi的情况下
            if (wifiUtil.isWifiEnable() && presUtil.isTrustSsid(wifiUtil.getCurrentWifiSSID())) {
                // 是否在信任wifi的情况下还需要解锁，是的话就为异常情况
                if (KeyGuardUtil.getInstance().inKeyguardRestrictedInputMode()) {
                    LogUtil.w(TAG, "inKeyguardRestrictedInputMode ");
                    presUtil.systemException();
                    startService(context);
                }
            }

        } else if (action.equals(Intent.ACTION_SHUTDOWN)) {
            LogUtil.w(TAG, "关机啦啦啦啦啦");
            presUtil.systemException();
            stopService(context);
        } else if (action.equals(Intent.ACTION_USER_PRESENT)) {
            LogUtil.w(TAG, "解锁完毕啦");
            startService(context, action);
        }
    }

    private void startService(Context context) {
        Intent i = new Intent(context, KeyGuardService.class);
        context.startService(i);
    }

    private void startService(Context context, String action) {
        Intent i = new Intent(context, KeyGuardService.class);
        i.setAction(action);
        context.startService(i);
    }

    private void stopService(Context context) {
        Intent i = new Intent(context, KeyGuardService.class);
        context.stopService(i);
    }

}
