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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.asdzheng.customlock.R;
import com.asdzheng.customlock.service.KeyGuardService;
import com.asdzheng.customlock.util.PrefencesUtil;
import com.asdzheng.customlock.view.ToggleButton;
import com.asdzheng.customlock.view.ToggleButton.OnToggleChanged;

/**
 */
public class WifiAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<WifiConfiguration> saveWifis;
    private List<ScanResult> results;
    // 附件可用wifi的ssid
    private List<String> rangeSsids;

    private Context context;

    public WifiAdapter(Context context, List<WifiConfiguration> saveWifis,
            List<ScanResult> rangeWifis) {
        this.inflater = LayoutInflater.from(context);
        this.saveWifis = saveWifis;
        this.results = rangeWifis;
        this.context = context;

        rangeSsids = new ArrayList<String>();
        addAllRangeSsid();
    }

    @Override
    public int getCount() {
        return saveWifis.size();
    }

    @Override
    public Object getItem(int position) {
        return saveWifis.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder = null;

        if (view == null) {
            view = inflater.inflate(R.layout.item_wifi_list, parent, false);
            holder = new ViewHolder();
            holder.textView = (TextView) view.findViewById(R.id.tv_ssid);
            holder.btn = (ToggleButton) view.findViewById(R.id.toggle_switch);
            holder.imageView = (ImageView) view.findViewById(R.id.iv_wifi_level);
            view.setTag(holder);
        }

        holder = (ViewHolder) view.getTag();

        WifiConfiguration saveWifiInfo = (WifiConfiguration) getItem(position);
        final String ssid = saveWifiInfo.SSID;
        holder.textView.setText(ssid);

        setWifiLevelImage(holder, ssid);

        if (PrefencesUtil.getInstance().isTrustSsid(ssid)) {
            holder.btn.setToggleOn();
        } else {
            holder.btn.setToggleOff();
        }

        holder.btn.setOnToggleChanged(new OnToggleChanged() {

            @Override
            public void onToggle(boolean on) {
                if (on) {
                    PrefencesUtil.getInstance().addSsid(ssid);

                    Toast.makeText(context, "信任wifi" + ssid, Toast.LENGTH_SHORT).show();
                } else {
                    PrefencesUtil.getInstance().removeSsid(ssid);
                }
                Intent intent = new Intent(context, KeyGuardService.class);
                context.startService(intent);
            }

        });
        return view;
    }

    private void setWifiLevelImage(ViewHolder holder, String ssid) {

        // 找出已经成功连接的wifi
        if (rangeSsids.contains(removeQuotes(ssid))) {
            ScanResult result = results.get(rangeSsids.indexOf(removeQuotes(ssid)));
            int level = result.level;
            // 根据获得的信号强度发送信息
            if (level <= 0 && level >= -50) {
                holder.imageView.setImageResource(R.drawable.wifi_level1);
            } else if (level < -50 && level >= -70) {
                holder.imageView.setImageResource(R.drawable.wifi_level2);
            } else if (level < -70 && level >= -80) {
                holder.imageView.setImageResource(R.drawable.wifi_level3);
            } else if (level < -80 && level >= -100) {
                holder.imageView.setImageResource(R.drawable.wifi_level4);
            }

        } else {
            holder.imageView.setImageResource(R.drawable.wifi_less);
        }
    }

    public void scanResultsChange(List<ScanResult> results) {
        this.results.clear();
        this.results.addAll(results);
        addAllRangeSsid();
        notifyDataSetChanged();
    }

    private void addAllRangeSsid() {
        if (results.isEmpty()) {
            rangeSsids.clear();
            return;
        }
        for (ScanResult result : results) {
            rangeSsids.add(removeQuotes(result.SSID));
        }
    }

    private String removeQuotes(String ssid) {
        return PrefencesUtil.getInstance().removeQuotes(ssid);
    }

    private static class ViewHolder {
        TextView textView;
        ToggleButton btn;
        ImageView imageView;
    }

}
