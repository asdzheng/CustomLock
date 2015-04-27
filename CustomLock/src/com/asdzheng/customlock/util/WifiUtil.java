/**
 * 
 */

package com.asdzheng.customlock.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import com.asdzheng.customlock.MyApplication;

/**
 */
public class WifiUtil {

    private State state;

    private static WifiUtil wifiUtil;

    private WifiManager wifiManager;

    private ConnectivityManager connectManager;

    private WifiUtil() {
        wifiManager = (WifiManager) MyApplication.getContext().getSystemService(
                Context.WIFI_SERVICE);

        connectManager = (ConnectivityManager) MyApplication.getContext().getSystemService(
                Context.CONNECTIVITY_SERVICE);

        state = connectManager.getNetworkInfo(connectManager.TYPE_WIFI).getState();
    }

    public synchronized static WifiUtil getInstance() {
        if (wifiUtil == null) {
            wifiUtil = new WifiUtil();
        }

        return wifiUtil;
    }

    // 是否wifi已经开启，并且已经连接上可用wifi
    public boolean isWifiConnected() {
        LogUtil.w("WifiUtil", state.toString());

        return wifiManager.isWifiEnabled()
                && (state == State.CONNECTED || state == State.CONNECTING);
    }

    // wifi是否已打开
    public boolean isWifiEnabled() {
        return wifiManager.isWifiEnabled();
    }

    public void setNewWorkState(State state) {
        this.state = state;
    }

    public String getCurrentWifiSSID() {
        return wifiManager.getConnectionInfo().getSSID();
    }

    public int getWifiState() {
        return wifiManager.getWifiState();
    }

    public List<ScanResult> getScanResults() {
        List<ScanResult> results = wifiManager.getScanResults();
        return results == null ? new ArrayList<ScanResult>() : results;
    }

    public List<WifiConfiguration> getWifiConfigNeworks() {
        return wifiManager.getConfiguredNetworks();
    }

    public void setWifiEnabled(boolean enabled) {
        wifiManager.setWifiEnabled(enabled);
    }

}
