
package com.asdzheng.customlock;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;
import android.widget.Toast;

import com.asdzheng.customlock.util.LogUtil;
import com.asdzheng.customlock.util.WifiUtil;

public class MainActivity extends Activity {

    private WifiUtil wifiUtil;
    private List<ScanResult> rangeWifis;
    private SaveWifiAdapter adapter;
    private List<WifiConfiguration> saveWifis;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wifiUtil = WifiUtil.getInstance();

        checkWifiHaveOpen();
    }

    private void checkWifiHaveOpen() {
        if (wifiUtil.isWifiEnable()) {
            initData();
        } else {
            openWifi();
            Toast.makeText(MainActivity.this, "请等待Wifi开启", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    initData();
                }
            }, 1500);
        }
    }

    private void initData() {
        rangeWifis = wifiUtil.getScanResults();
        saveWifis = wifiUtil.getWifiConfigNeworks();
        listView = (ListView) findViewById(R.id.listView);

        Intent i = new Intent(this, KeyGuardService.class);
        startService(i);
        adapter = new SaveWifiAdapter(this, saveWifis, getScanResultSsids(rangeWifis));
        listView.setAdapter(adapter);

    }

    /**
     * 打开WIFI
     */
    private void openWifi() {
        wifiUtil.setWifiEnabled(true);
    }

    private List<String> getScanResultSsids(List<ScanResult> results) {

        List<String> ssids = new ArrayList<String>();
        for (ScanResult result : results) {
            ssids.add(result.SSID);
        }
        return ssids;
    }

    @Override
    protected void onDestroy() {
        LogUtil.w("MainActivity", "onDestroy");
        super.onDestroy();
    }
}
