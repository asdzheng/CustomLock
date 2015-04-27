/**
 * 
 */

package com.asdzheng.customlock.main;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.asdzheng.customlock.R;
import com.asdzheng.customlock.main.RecyclerWifiAdapter.WifiItemHolder;
import com.asdzheng.customlock.service.KeyGuardService;
import com.asdzheng.customlock.util.PrefencesUtil;
import com.asdzheng.customlock.view.ToggleButton;
import com.asdzheng.customlock.view.ToggleButton.OnToggleChanged;

/**
 * @author [asdzheng] 2015-4-27
 */
public class RecyclerWifiAdapter extends RecyclerView.Adapter<WifiItemHolder> {

    private LayoutInflater inflater;
    private List<WifiConfiguration> saveWifis;
    private List<ScanResult> rangeWifis;
    // 附件可用wifi的ssid
    private List<String> rangeSsids;

    private Context context;

    public RecyclerWifiAdapter(Context context, List<WifiConfiguration> saveWifis,
            List<ScanResult> rangeWifis) {
        this.inflater = LayoutInflater.from(context);
        this.saveWifis = saveWifis;
        this.rangeWifis = rangeWifis;
        this.context = context;

        rangeSsids = new ArrayList<String>();
        setRangeSsids();
    }

    @Override
    public int getItemCount() {
        return saveWifis.size();
    }

    @Override
    public void onBindViewHolder(WifiItemHolder holder, int position) {
        WifiConfiguration saveWifiInfo = (WifiConfiguration) saveWifis.get(position);
        final String ssid = removeQuotes(saveWifiInfo.SSID);

        holder.textView.setText(ssid);
        setImageRes(holder.imageView, ssid);
        setToggleState(holder.toggle, ssid);
    }

    private void setToggleState(ToggleButton toggle, final String ssid) {
        if (PrefencesUtil.getInstance().isTrustSsid(ssid)) {
            toggle.setToggleOn();
        } else {
            toggle.setToggleOff();
        }
        toggle.setOnToggleChanged(new OnToggleChanged() {

            @Override
            public void onToggle(boolean on) {
                if (on) {
                    Toast.makeText(context, "信任 " + ssid + " wifi", Toast.LENGTH_SHORT).show();
                    PrefencesUtil.getInstance().addSsid(ssid);
                } else {
                    PrefencesUtil.getInstance().removeSsid(ssid);
                }
                startService();
            }

        });
    }

    private void startService() {
        Intent intent = new Intent(context, KeyGuardService.class);
        context.startService(intent);
    }

    private void setImageRes(ImageView imageView, String ssid) {
        // 找出已经扫描到已经连过的wifi
        if (rangeSsids.contains(ssid)) {
            ScanResult result = rangeWifis.get(rangeSsids.indexOf(ssid));
            int level = result.level;
            // 根据获得的信号强度发送信息
            if (level <= 0 && level >= -50) {
                imageView.setImageResource(R.drawable.wifi_level1);
            } else if (level < -50 && level >= -70) {
                imageView.setImageResource(R.drawable.wifi_level2);
            } else if (level < -70 && level >= -80) {
                imageView.setImageResource(R.drawable.wifi_level3);
            } else if (level < -80 && level >= -100) {
                imageView.setImageResource(R.drawable.wifi_level4);
            } else {
                imageView.setImageResource(R.drawable.wifi_less);
            }

        } else {
            imageView.setImageResource(R.drawable.wifi_less);
        }
    }

    private void setRangeSsids() {
        if (rangeWifis.isEmpty()) {
            rangeSsids.clear();
            return;
        }
        for (ScanResult result : rangeWifis) {
            rangeSsids.add(removeQuotes(result.SSID));
        }
    }

    private String removeQuotes(String ssid) {
        return PrefencesUtil.getInstance().removeQuotes(ssid);
    }

    public void scanResultsChange(List<ScanResult> results) {
        rangeWifis.clear();
        rangeWifis.addAll(results);
        setRangeSsids();
        notifyDataSetChanged();
    }

    public void updateWifiDisconnect() {
        rangeWifis.clear();
        rangeSsids.clear();
        notifyDataSetChanged();
    }

    @Override
    public WifiItemHolder onCreateViewHolder(ViewGroup parent, int arg1) {
        return new WifiItemHolder(inflater.inflate(R.layout.item_wifi_list, parent, false));
    }

    public static class WifiItemHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ToggleButton toggle;
        public ImageView imageView;

        public WifiItemHolder(View view) {
            super(view);

            textView = (TextView) view.findViewById(R.id.tv_ssid);
            toggle = (ToggleButton) view.findViewById(R.id.toggle_switch);
            imageView = (ImageView) view.findViewById(R.id.iv_wifi_level);
        }

    }

}
