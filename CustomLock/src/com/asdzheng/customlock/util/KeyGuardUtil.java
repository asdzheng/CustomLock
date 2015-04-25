/**
 * 
 */

package com.asdzheng.customlock.util;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;

import com.asdzheng.customlock.MyApplication;

/**
 */
public class KeyGuardUtil {

    private static final String TRUST_WIFI = "100";

    private KeyguardManager manager;
    private KeyguardLock lock;

    private static KeyGuardUtil keyGuardUtil;

    private KeyGuardUtil() {
        manager = (KeyguardManager) MyApplication.getContext().getSystemService(
                Context.KEYGUARD_SERVICE);
        lock = manager.newKeyguardLock(TRUST_WIFI);

    }

    public synchronized static KeyGuardUtil getInstance() {
        if (keyGuardUtil == null) {
            keyGuardUtil = new KeyGuardUtil();
        }

        return keyGuardUtil;
    }

    public synchronized void disableKeyGuard() {
        LogUtil.w("KeyGuardUtil ==== ", " disableKeyGuard !!!");
        lock.disableKeyguard();
    }

    public synchronized void reEnableKeyGuard() {
        LogUtil.w("KeyGuardUtil ==== ", " reenableKeyguard !!!");
        lock.reenableKeyguard();
    }

    public synchronized boolean inKeyguardRestrictedInputMode() {
        return manager.inKeyguardRestrictedInputMode();
    }

}
