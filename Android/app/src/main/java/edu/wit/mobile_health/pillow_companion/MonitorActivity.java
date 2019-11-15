package edu.wit.mobile_health.pillow_companion;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import edu.wit.mobile_health.pillow_companion.ui.popups.BluetoothSeachFragment;
import edu.wit.mobile_health.pillow_companion.ui.popups.TimePickerFragment;


public class MonitorActivity extends AppCompatActivity {

    private final static int REQUEST_ENABLE_BT = 1;
    private final static int REQUEST_ENABLE_GPS = 2;

    // UUIDs for UART service and associated characteristics.
    public static UUID UART_UUID = UUID.fromString("6E400001-B5A3-F393-E0A9-E50E24DCCA9E");
    public static UUID TX_UUID = UUID.fromString(  "6E400002-B5A3-F393-E0A9-E50E24DCCA9E");
    public static UUID RX_UUID = UUID.fromString(  "6E400003-B5A3-F393-E0A9-E50E24DCCA9E");

    // UUID for the BTLE client characteristic which is necessary for notifications.
    public static UUID CLIENT_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    private BluetoothManager manager;
    private BluetoothAdapter adapter;
    private BluetoothLeScanner scanner;

    private BluetoothGatt gatt;
    private BluetoothGattCharacteristic tx;
    private BluetoothGattCharacteristic rx;

    private DialogFragment bluetoothSearch;

    TextView alarm;
    Button done;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);

        Bundle bundle = this.getIntent().getExtras();

        alarm = findViewById(R.id.alarm_time);
        done = findViewById(R.id.done_button);

        alarm.setText(bundle.getInt("hour") + ":" + String.format("%02d", bundle.getInt("min")) + " " + bundle.getString("period"));


        done.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

        bluetoothSearch = new BluetoothSeachFragment();
        bluetoothSearch.show(getSupportFragmentManager(), "bluetooth search");

        manager =  (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        adapter = manager.getAdapter();

        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_ENABLE_GPS);

        if (adapter == null || !adapter.isEnabled()) {
            Intent enableBtIntent =
                    new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            Intent enableLocationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(enableLocationIntent, REQUEST_ENABLE_GPS);
        }

        scanner = adapter.getBluetoothLeScanner();

        scanner.startScan(scanCallback);




    }

    private BluetoothGattCallback callback = new BluetoothGattCallback() {
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);

            // Save reference to each characteristic.
            tx = gatt.getService(UART_UUID).getCharacteristic(TX_UUID);
            rx = gatt.getService(UART_UUID).getCharacteristic(RX_UUID);

            Log.v("BLE", "tx:" + tx);
            Log.v("BLE", "rx:" + rx.getDescriptor(CLIENT_UUID));


            // Next update the RX characteristic's client descriptor to enable notifications.
            if (rx.getDescriptor(CLIENT_UUID) != null) {
                BluetoothGattDescriptor desc = rx.getDescriptor(CLIENT_UUID);
                desc.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            }
        }
    };

    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);

            Log.d("myApp", "Scan Result");
            Log.d("myApp", result.getDevice().getAddress());

            String name = "";

            try{
                name = result.getDevice().getName();
                Log.v("myApp", name);
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                if(result.getDevice().getName() == "Group 4") {
                    gatt = result.getDevice().connectGatt(getApplicationContext(), false, callback);
                    scanner.stopScan(scanCallback);
                    bluetoothSearch.dismiss();
                }
            }
        }
    };

    public void end(){
        scanner.stopScan(scanCallback);
        finish();
    }
}
