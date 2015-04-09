
package com.asdzheng.customlock;

import java.util.ArrayList;
import java.util.List;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private WifiManager wifiManager;
    List<ScanResult> rangeWifis;
    SaveWifiAdapter adapter;
    List<WifiConfiguration> saveWifis;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        openWifi();
        rangeWifis = wifiManager.getScanResults();
        saveWifis = wifiManager.getConfiguredNetworks();

        adapter = new SaveWifiAdapter(this, saveWifis, getScanResultSsids(rangeWifis));
        listView = (ListView) findViewById(R.id.listView);

        if (rangeWifis == null) {
            Toast.makeText(this, "wifi未打开！", Toast.LENGTH_LONG).show();
        } else {
            listView.setAdapter(adapter);
        }

        Intent i = new Intent(this, KeyGuardService.class);
        startService(i);
    }

    /**
     * 打开WIFI
     */
    private void openWifi() {
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }

    }

    private List<String> getScanResultSsids(List<ScanResult> results) {
        List<String> ssids = new ArrayList<String>();
        for (ScanResult result : results) {
            ssids.add(result.SSID);
        }
        return ssids;
    }

    // @Override
    // public void onItemClick(AdapterView<?> parent, View view, int position,
    // long id) {
    // WifiConfiguration trustWifi = wifis.get(position);
    // PrefencesUtil.getInstance(this).putString(PrefencesUtil.PREFENCE_KEY,
    // trustWifi.SSID);
    //
    // Toast.makeText(this, "信任wifi" + trustWifi.SSID,
    // Toast.LENGTH_SHORT).show();
    //
    // Intent intent = new Intent(this, KeyGuardService.class);
    // startService(intent);
    //
    // }

    @Override
    protected void onDestroy() {
        Log.e("MainActivity", "onDestroy");
        super.onDestroy();
    }
}
