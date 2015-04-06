/**
 * 
 */

package com.asdzheng.customlock;

import android.content.Context;
import android.content.SharedPreferences;

/**
// * @author [zWX232618/郑加波] 2015-3-31
 */
public class PrefencesUtil {

    public static final String PREFENCE_NAME = "Trust_Wifi";
    public static final String PREFENCE_KEY = "Trust_Wifi_Key";

    private SharedPreferences mSharePres;

    private static Context preContext;

    private PrefencesUtil() {
        mSharePres = preContext.getApplicationContext().getSharedPreferences(PREFENCE_NAME,
                Context.MODE_PRIVATE);
    }

    private static class SysSharePresHolder {
        static final PrefencesUtil INSTANCE = new PrefencesUtil();
    }

    public static PrefencesUtil getInstance(Context context) {
        preContext = context;
        return SysSharePresHolder.INSTANCE;
    }

    public void putString(String key, String value) {
        mSharePres.edit().putString(key, value).commit();
    }

    public String getString(String key, String def) {
        String value = mSharePres.getString(key, def);
        return value;
    }
    
    public boolean haveTrustWifi() {
        String value = mSharePres.getString(PREFENCE_KEY, "");
        return !("".equals(value));
    }
    
    public void removeTustWifi() {
    	mSharePres.edit().clear().commit();
    	
    }
}
