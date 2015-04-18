
package com.asdzheng.customlock.main;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.asdzheng.customlock.R;
import com.asdzheng.customlock.service.KeyGuardService;
import com.asdzheng.customlock.util.LogUtil;
import com.asdzheng.customlock.util.WifiUtil;

public class WifiFragment extends Fragment {

    private ListView cardsList;

    private WifiUtil wifiUtil;
    private List<ScanResult> rangeWifis;
    private WifiAdapter adapter;
    private List<WifiConfiguration> saveWifis;

    public WifiFragment() {
        // nop
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wifi_layout, container, false);
        cardsList = (ListView) rootView.findViewById(R.id.cards_list);
        wifiUtil = WifiUtil.getInstance();
        checkWifiHaveOpen();
        return rootView;
    }

    private void setupList() {
        cardsList.setAdapter(createAdapter());
        Intent i = new Intent(getActivity(), KeyGuardService.class);
        getActivity().startService(i);

    }

    private void checkWifiHaveOpen() {
        if (wifiUtil.isWifiEnable()) {
            setupList();
        } else {
            openWifi();
            Toast.makeText(getActivity(), "请等待Wifi开启", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    setupList();
                }
            }, 1500);
        }
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

    private WifiAdapter createAdapter() {
        rangeWifis = wifiUtil.getScanResults();
        saveWifis = wifiUtil.getWifiConfigNeworks();
        // saveWifis.addAll(saveWifis);
        adapter = new WifiAdapter(getActivity(), saveWifis, rangeWifis);

        return adapter;
    }

    @Override
    public void onDestroy() {
        LogUtil.w("MainActivity", "onDestroy");

        super.onDestroy();
    }
}
