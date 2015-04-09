/**
 * 
 */

package com.asdzheng.customlock;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * // * @author [zWX232618/郑加波] 2015-3-31
 */
public class PrefencesUtil {

    public static final String PREFENCE_NAME = "Trust_Wifi";

    private SharedPreferences mSharePres;

    private PrefencesUtil() {
        mSharePres = MyApplication.getContext().getSharedPreferences(PREFENCE_NAME,
                Context.MODE_PRIVATE);
    }

    private static class SysSharePresHolder {
        static final PrefencesUtil INSTANCE = new PrefencesUtil();
    }

    public static PrefencesUtil getInstance() {
        return SysSharePresHolder.INSTANCE;
    }

    public void putString(String key, String value) {
        mSharePres.edit().putString(key, value).commit();
    }

    public String getString(String key, String def) {
        String value = mSharePres.getString(key, def);
        return value;
    }

    public boolean isTrustWifi(String ssid) {
        return mSharePres.contains(removeQuotes(ssid));
    }

    public void addWifi(String ssid) {
        mSharePres.edit().putBoolean(removeQuotes(ssid), true).commit();
    }

    public String removeQuotes(String ssid) {
        String key = ssid;
        if (ssid.startsWith("\"")) {
            key = ssid.substring(1, ssid.length() - 1);
        }
        // Log.i("key ======== ", key);

        return key;
    }

    public void removeWifi(String ssid) {
        mSharePres.edit().remove(removeQuotes(ssid)).commit();
    }
}
