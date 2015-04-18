/**
 * 
 */

package com.asdzheng.customlock.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.asdzheng.customlock.receiver.DiffStateReceiver;
import com.asdzheng.customlock.util.KeyGuardUtil;
import com.asdzheng.customlock.util.LogUtil;
import com.asdzheng.customlock.util.PrefencesUtil;
import com.asdzheng.customlock.util.WifiUtil;

/**
 * @author [zWX232618/郑加波] 2015-4-16
 */
public class KeyGuardService extends Service {

    private static final String TAG = KeyGuardService.class.getSimpleName();

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

    /**
     * 点亮屏幕的的广播不能再AndroidMainfest里面注册，只能动态注册
     */
    private void addScreenRecevier() {

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);

        stateRecevier = new DiffStateReceiver();
        registerReceiver(stateRecevier, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        LogUtil.i(TAG, "service start !!");

        if (presUtil.isSystemException()) {
            // 每次出现异常情况都要手动解锁一次后才能继续使用。
            if (Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {
                LogUtil.w(TAG, "ACTION_USER_PRESENT !!");

                /**
                 * 出现异常时disableKeyguard是作用的，需要等手动开锁后，重新显示解锁界面后，在隐藏，才有效果
                 */
                presUtil.systemNormal();
                reEnableKeyGuard();
                disableKeyGuard();
            }
            return super.onStartCommand(intent, flags, startId);
        }

        checkKeyGuardable();
        return START_STICKY;
    }

    /**
     * 判断是否需要显示解锁界面
     */
    private void checkKeyGuardable() {
        // wifi是否可用
        if (wifiUtil.isWifiEnable()) {
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

        unregisterReceiver(stateRecevier);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
