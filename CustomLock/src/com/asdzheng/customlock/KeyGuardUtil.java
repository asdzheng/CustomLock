/**
 * 
 */

package com.asdzheng.customlock;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;

/**
 * @author [zWX232618/郑加波] 2015-3-31
 */
public class KeyGuardUtil {

    private static final String TRUST_WIFI = "100";

    static KeyguardManager manager;
    static KeyguardLock lock;

    private KeyGuardUtil() {
        manager = (KeyguardManager) MyApplication.getContext().getSystemService(
                Context.KEYGUARD_SERVICE);
        lock = manager.newKeyguardLock(TRUST_WIFI);
    }

    private static class KeyGuardHolder {
        static final KeyGuardUtil INSTANCE = new KeyGuardUtil();
    }

    public static KeyGuardUtil getInstance() {
        return KeyGuardHolder.INSTANCE;
    }

    public void disableKeyGuard() {
        lock.disableKeyguard();
    }

    public void reEnableKeyGuard() {
        lock.reenableKeyguard();
    }

    public boolean isEnableKeyGuard() {
        return manager.isKeyguardSecure();
    }

    public boolean isKeyGuardLocked() {
        return manager.isKeyguardLocked();
    }

}
