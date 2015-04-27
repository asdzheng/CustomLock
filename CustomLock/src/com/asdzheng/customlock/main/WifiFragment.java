
package com.asdzheng.customlock.main;

import java.util.List;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.asdzheng.customlock.R;
import com.asdzheng.customlock.service.KeyGuardService;
import com.asdzheng.customlock.util.WifiUtil;

public class WifiFragment extends Fragment {

    private RecyclerView wifiListView;

    private WifiUtil wifiUtil;
    private List<ScanResult> rangeWifis;
    private RecyclerWifiAdapter adapter;
    private List<WifiConfiguration> saveWifis;

    public WifiFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wifi_layout, container, false);
        wifiListView = (RecyclerView) rootView.findViewById(R.id.recycler_list_wifi);
        wifiListView.setLayoutManager(new LinearLayoutManager(getActivity()));// 这里用线性显示

        wifiUtil = WifiUtil.getInstance();
        addWifiStateReceiver();
        checkWifiHaveOpen();
        return rootView;
    }

    private void checkWifiHaveOpen() {
        if (wifiUtil.isWifiEnabled()) {
            initData();
        } else {
            openWifi();
            Toast.makeText(getActivity(), "正在打开wifi，请等待...", Toast.LENGTH_LONG).show();
        }
    }

    private void initData() {
        rangeWifis = wifiUtil.getScanResults();
        saveWifis = wifiUtil.getWifiConfigNeworks();
        adapter = new RecyclerWifiAdapter(getActivity(), saveWifis, rangeWifis);
        wifiListView.setAdapter(adapter);

        startService();
    }

    private void startService() {
        Intent i = new Intent(getActivity(), KeyGuardService.class);
        getActivity().startService(i);
    }

    // 注册wifi信号的广播接收器
    private void addWifiStateReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        getActivity().registerReceiver(wifiConnectReceiver, filter);
    }

    /**
     * 打开WIFI
     */
    private void openWifi() {
        wifiUtil.setWifiEnabled(true);
    }

    private BroadcastReceiver wifiConnectReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                if (wifiUtil.isWifiEnabled() && adapter == null) {
                    initData();
                }

            } else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                State state = networkInfo.getState();
                if (adapter != null) {
                    if (state == State.CONNECTED) {
                        // 更新wifi信号的状态
                        adapter.scanResultsChange(wifiUtil.getScanResults());
                    } else if (state == State.DISCONNECTED) {
                        adapter.updateWifiDisconnect();
                    }
                }
            }
        }

    };

    public void onDestroy() {
        getActivity().unregisterReceiver(wifiConnectReceiver);
        super.onDestroy();
    };

}
