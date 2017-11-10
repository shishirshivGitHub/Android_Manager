package android.android_wifi_manager;

import android.Manifest;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView wifiList;
    WifiManager wifiManager;
    WifiReceiver wifiReceiver;
    List<ScanResult> scanResults;
    StringBuilder sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wifiList = (TextView)findViewById(R.id.wifiList);

        wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);    // allow to use system service to manager //

        if(wifiManager.isWifiEnabled() == false){
            Toast.makeText(getApplicationContext(),"Wifi is disable ...! ",Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }

        wifiReceiver = new WifiReceiver();

        wifiList.setText("Start Scanning...");

        registerReceiver(wifiReceiver,new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        registerReceiver(wifiReceiver,new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }

    @Override
    protected void onPause() {
        unregisterReceiver(wifiReceiver);
        super.onPause();
    }

    public class WifiReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {

                sb = new StringBuilder();
                scanResults = wifiManager.getScanResults();
                Log.e("check the size of the wifi -->",  scanResults.size() + "  ");

                sb.append("\n wifi Connection : " + scanResults.size() + "\n\n");

                for(int i= 0;i<scanResults.size();i++){
                    sb.append(new Integer(i+1)+"");
                    sb.append(scanResults.get(i).toString());
                    sb.append("\n\n");
                }
            }
            wifiList.setText(sb);
        }
    }
}
