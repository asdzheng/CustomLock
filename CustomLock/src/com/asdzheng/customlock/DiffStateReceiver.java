/**
 * 
 */

package com.asdzheng.customlock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

import com.asdzheng.customlock.util.KeyGuardUtil;
import com.asdzheng.customlock.util.LogUtil;
import com.asdzheng.customlock.util.PrefencesUtil;
import com.asdzheng.customlock.util.WifiUtil;

/**
 */
public class DiffStateReceiver extends BroadcastReceiver {

    private static final String BOOT = "boot_complete";

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if (action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
            startService(context);
            LogUtil.i("NetworkChangeReceiver", "连接状态改变" + WifiUtil.getInstance().getWifiState());
        } else if (action.equals(WifiManager.RSSI_CHANGED_ACTION)) {
            startService(context);
            LogUtil.i("NetworkChangeReceiver", "WIFI信息改变  == "
                    + WifiUtil.getInstance().getWifiState());
        } else if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            LogUtil.e("NetworkChangeReceiver", "开机成功啦啦啦啦");
            startService(context, action);
        } else if (action.equals(Intent.ACTION_SCREEN_ON)) {
            LogUtil.i("NetworkChangeReceiver", "点亮屏幕  ");
            if (KeyGuardUtil.getInstance().inKeyguardRestrictedInputMode()) {
                if (WifiUtil.getInstance().isWifiEnable()
                        && PrefencesUtil.getInstance().isTrustSsid(
                                WifiUtil.getInstance().getCurrentWifiSSID())) {
                    LogUtil.e("NetworkChangeReceiver", "inKeyguardRestrictedInputMode ");
                    PrefencesUtil.getInstance().systemException();
                    startService(context, action);
                }
            }

        } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
            LogUtil.i("NetworkChangeReceiver", "关闭屏幕");
            if (KeyGuardUtil.getInstance().inKeyguardRestrictedInputMode()) {
                // LogUtil.i("NetworkChangeReceiver",
                // "inKeyguardRestrictedInputMode ");
                // if (WifiUtil.getInstance().isWifiEnable()
                // && PrefencesUtil.getInstance().isTrustWifi(
                // WifiUtil.getInstance().getCurrentWifiSSID())) {
                // KeyGuardUtil.getInstance().disableKeyGuard();
                // }
            }
        } else if (action.equals(Intent.ACTION_SHUTDOWN)) {
            LogUtil.e("NetworkChangeReceiver", "关机啦啦啦啦啦");
            PrefencesUtil.getInstance().systemException();
            stopService(context);
        } else if (action.equals(Intent.ACTION_USER_PRESENT)) {
            LogUtil.e("NetworkChangeReceiver", "解锁完毕啦");
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
