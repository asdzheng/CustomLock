/**
 * 
 */

package com.asdzheng.customlock;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.asdzheng.customlock.util.PrefencesUtil;
import com.asdzheng.customlock.view.ToggleButton;
import com.asdzheng.customlock.view.ToggleButton.OnToggleChanged;

/**
 * @author [zWX232618/郑加波] 2015-4-8
 */
public class SaveWifiAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<WifiConfiguration> saveWifis;
    private List<String> rangeSsids;

    Context context;

    public SaveWifiAdapter(Context context, List<WifiConfiguration> saveWifis,
            List<String> rangeWifis) {
        this.inflater = LayoutInflater.from(context);
        this.saveWifis = saveWifis;
        this.rangeSsids = rangeWifis;
        this.context = context;
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
        View view = null;
        ViewHolder holder = null;

        if (convertView == null) {
            view = inflater.inflate(R.layout.item_wifi_list, null);
            holder = new ViewHolder();
            holder.textView = (TextView) view.findViewById(R.id.textView);
            holder.btn = (ToggleButton) view.findViewById(R.id.tbtn_slide);
            holder.imageView = (ImageView) view.findViewById(R.id.imageView);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        WifiConfiguration saveWifiInfo = (WifiConfiguration) getItem(position);
        final String ssid = saveWifiInfo.SSID;

        haveSaveWIfi(holder, ssid);

        holder.textView.setText(ssid);
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

    private void haveSaveWIfi(ViewHolder holder, final String ssid) {
        if (rangeSsids == null || rangeSsids.isEmpty()) {
            return;
        }

        if (rangeSsids.contains(PrefencesUtil.getInstance().removeQuotes(ssid))) {
            holder.imageView.setImageResource(R.drawable.wifi_save_range);
        } else {
            holder.imageView.setImageResource(R.drawable.wifi_less);
        }
    }

    private static class ViewHolder {
        TextView textView;
        ToggleButton btn;
        ImageView imageView;
    }
}
