package com.asdzheng.customlock;

import java.util.List;

import com.asdzheng.customlock.view.ToggleButton;
import com.asdzheng.customlock.view.ToggleButton.OnToggleChanged;
import com.example.customlock.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private WifiManager wifiManager;
	List<ScanResult> list;
	MyAdapter adapter;
	List<WifiConfiguration> wifis;

	ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	private void init() {
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		// openWifi();
		// list = wifiManager.getScanResults();
		wifis = wifiManager.getConfiguredNetworks();

		adapter = new MyAdapter(this, wifis);
		listView = (ListView) findViewById(R.id.listView);
		// listView.setOnItemClickListener(this);
		if (wifis == null) {
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

	public class MyAdapter extends BaseAdapter {

		LayoutInflater inflater;
		List<WifiConfiguration> list;

		public MyAdapter(Context context, List<WifiConfiguration> list) {
			this.inflater = LayoutInflater.from(context);
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = null;
			ViewHolder holder = null;

			if (convertView == null) {
				view = inflater.inflate(R.layout.item_wifi_list, null);
				holder = new ViewHolder();
				holder.textView = (TextView) view.findViewById(R.id.textView);
				holder.btn = (ToggleButton) view.findViewById(R.id.tbtn_slide);
				holder.imageView = (ImageView) view
						.findViewById(R.id.imageView);
				view.setTag(holder);
			} else {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}

			WifiConfiguration scanResult = (WifiConfiguration) getItem(position);

			holder.textView.setText(scanResult.SSID);
			if (PrefencesUtil.getInstance(MainActivity.this).haveTrustWifi()) {
				if (scanResult.SSID.equals(PrefencesUtil.getInstance(
						MainActivity.this).getString(
						PrefencesUtil.PREFENCE_KEY, ""))) {
					holder.btn.setToggleOn(false);
					holder.btn.setEnabled(true);
				} else {
					holder.btn.setToggleOff(false);
					holder.btn.setEnabled(false);
				}
			} else {
				holder.btn.setEnabled(true);
			}

			holder.btn.setOnToggleChanged(new OnToggleChanged() {

				@Override
				public void onToggle(boolean on) {
					if (on) {
						WifiConfiguration trustWifi = wifis.get(position);
						PrefencesUtil.getInstance(MainActivity.this).putString(
								PrefencesUtil.PREFENCE_KEY, trustWifi.SSID);

						Toast.makeText(MainActivity.this,
								"信任wifi" + trustWifi.SSID, Toast.LENGTH_SHORT)
								.show();

						Intent intent = new Intent(MainActivity.this,
								KeyGuardService.class);
						startService(intent);
						notifyDataSetChanged();
					} else {
						PrefencesUtil.getInstance(MainActivity.this)
								.removeTustWifi();
						Toast.makeText(MainActivity.this, "clear",
								Toast.LENGTH_SHORT).show();
						notifyDataSetChanged();
					}
				}

			});
			// holder.signalStrenth.setText(String.valueOf(Math.abs(scanResult.priority)));
			return view;
		}

	}

	private static class ViewHolder {
		TextView textView;
		ToggleButton btn;
		ImageView imageView;
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
}
