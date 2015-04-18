/**
 * 
 */

package com.asdzheng.customlock.util;

import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import com.asdzheng.customlock.MyApplication;

/**
 * @author [zWX232618/郑加波] 2015-4-14
 */
public class WifiUtil {

    private static WifiUtil wifiUtil;

    WifiManager wifiManager;

    private WifiUtil() {
        wifiManager = (WifiManager) MyApplication.getContext().getSystemService(
                Context.WIFI_SERVICE);
    }

    public static WifiUtil getInstance() {
        if (wifiUtil == null) {
            wifiUtil = new WifiUtil();
        }

        return wifiUtil;
    }

    public boolean isWifiEnable() {
        return wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED;
    }

    public String getCurrentWifiSSID() {
        return wifiManager.getConnectionInfo().getSSID();
    }

    public int getWifiState() {
        return wifiManager.getWifiState();
    }

    public List<ScanResult> getScanResults() {
        return wifiManager.getScanResults();
    }

    public List<WifiConfiguration> getWifiConfigNeworks() {
        return wifiManager.getConfiguredNetworks();
    }

    public void setWifiEnabled(boolean enabled) {
        wifiManager.setWifiEnabled(enabled);
    }

}
