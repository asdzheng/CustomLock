/**
 * 
 */

package com.asdzheng.customlock.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.asdzheng.customlock.MyApplication;

/**
 */
public class PrefencesUtil {

    public static final String PREFENCE_NAME = "Trust_Wifi";
    public static final String SYSTEM_SHUTDOWN = "shutdown";
    public static final String SYSTEM_EXCEPTION = "system_exception";

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

    public synchronized void addSsid(String ssid) {
        putBoolean(removeQuotes(ssid), true);
    }

    public synchronized void removeSsid(String ssid) {
        removeKey(removeQuotes(ssid));
    }

    public void shutDown() {
        LogUtil.e("PrefencesUtil", "shutdown");

        putBoolean(SYSTEM_SHUTDOWN, true);
    }

    /**
     * 系统异常出现的情况： 1 -->开机时就disableKeyguard，会出现按home键进入解锁界面， 然后隐藏解锁界面的 bug |||||
     * 2 --> wifi状态改变，如移除wifi区域，也有可能出现reenable或者disable失效
     * ||||||失效后需要重新显示锁屏界面，然后再隐藏
     */
    public boolean isJustShutdown() {
        LogUtil.e("PrefencesUtil", "isJustShutdown");

        return containkey(SYSTEM_SHUTDOWN);
    }

    public void haveStartUp() {
        LogUtil.e("PrefencesUtil", "haveStartUp");

        removeKey(SYSTEM_SHUTDOWN);
    }

    /**
     * 有些手机扫描出来的Wifi信息带有双引号，有些没有，统一去掉
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
