/**
 * 
 */

package com.asdzheng.customlock.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.asdzheng.customlock.MyApplication;

/**
 * // * @author [zWX232618/郑加波] 2015-3-31
 */
public class PrefencesUtil {

    public static final String PREFENCE_NAME = "Trust_Wifi";
    public static final String SYSTEM_SHUTDOWN = "Shutdown_10086";

    private SharedPreferences mSharePres;

    private static PrefencesUtil prefUtil;

    private PrefencesUtil() {
        mSharePres = MyApplication.getContext().getSharedPreferences(PREFENCE_NAME,
                Context.MODE_PRIVATE);
    }

    public synchronized static PrefencesUtil getInstance() {
        if (prefUtil == null) {
            prefUtil = new PrefencesUtil();
        }
        return prefUtil;
    }

    private void putBoolean(String key, boolean value) {
        mSharePres.edit().putBoolean(key, value).commit();
    }

    private void removeKey(String key) {
        mSharePres.edit().remove(removeQuotes(key)).commit();
    }

    private boolean containkey(String key) {
        return mSharePres.contains(removeQuotes(key));
    }

    public boolean isTrustSsid(String ssid) {
        return containkey(ssid);
    }

    public void addSsid(String ssid) {
        putBoolean(removeQuotes(ssid), true);
    }

    public void removeSsid(String ssid) {
        removeKey(removeQuotes(ssid));
    }

    /**
     * 系统异常出现的情况： 1 -->开机时就disableKeyguard，会出现按home键进入解锁界面， 然后隐藏解锁界面的 bug |||||
     * 2 --> wifi状态改变，如移除wifi区域，也有可能出现reenable或者disable失效
     * ||||||失效后需要重新显示锁屏界面，然后再隐藏
     */
    public void systemException() {
        LogUtil.e("PrefencesUtil", "systemException");

        putBoolean(SYSTEM_SHUTDOWN, true);
    }

    public boolean isSystemException() {
        LogUtil.e("PrefencesUtil", "isSystemException");

        return containkey(SYSTEM_SHUTDOWN);
    }

    public void systemNormal() {
        LogUtil.e("PrefencesUtil", "SYSTEM_NORMAL");

        removeKey(SYSTEM_SHUTDOWN);
    }

    /**
     * 有些手机扫描出来的Wifi信息带有双引号，需要去掉
     */
    public String removeQuotes(String ssid) {
        if (ssid == null || ssid.isEmpty()) {
            return "";
        }

        String key = ssid;
        if (ssid.startsWith("\"")) {
            key = ssid.substring(1, ssid.length() - 1);
        }
        // LogUtil.i("key ======== ", key);

        return key;
    }

}
