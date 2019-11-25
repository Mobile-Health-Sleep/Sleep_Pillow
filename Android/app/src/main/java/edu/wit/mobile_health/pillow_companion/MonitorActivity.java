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

import java.util.UUID;

import edu.wit.mobile_health.pillow_companion.data_collection.ArduinoInterface;
import edu.wit.mobile_health.pillow_companion.ui.popups.BluetoothSeachFragment;


public class MonitorActivity extends AppCompatActivity {
    /**
     * Arduino Variable names
     *
     * temp: int
     * light: int
     * accelX: int
     * accelY: int
     * accelZ: int
     *
     * vibrate: boolean
     */

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

    private ArduinoInterface aInt;

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
                end();
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
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);

            if(newState == BluetoothGatt.STATE_CONNECTED){
                Log.v("myApp", "GATT CONNECTED");

                gatt.discoverServices();
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.v("myApp", "Services Discovered");
            super.onServicesDiscovered(gatt, status);

            Log.v("myApp", gatt.getServices().toString());
            Log.v("myApp", gatt.getService(UART_UUID).getCharacteristics().toString());

            tx = gatt.getService(UART_UUID).getCharacteristic(TX_UUID);
            rx = gatt.getService(UART_UUID).getCharacteristic(RX_UUID);


            if (!gatt.setCharacteristicNotification(rx, true)) {
                Log.v("myApp", "Couldn't set notifications for RX characteristic!");
            }
            // Next update the RX characteristic's client descriptor to enable notifications.
            if (rx.getDescriptor(CLIENT_UUID) != null) {
                BluetoothGattDescriptor desc = rx.getDescriptor(CLIENT_UUID);
                desc.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                if (!gatt.writeDescriptor(desc)) {
                    Log.v("myApp", "Couldn't write RX client descriptor value!");
                }
            } else {
                Log.v("myApp", "Couldn't get RX client descriptor!");
            }

            aInt = new ArduinoInterface(gatt, tx, rx);
            aInt.start();
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);

            String sensor = aInt.getCurrentSensor();

            aInt.appendData(sensor.trim(),Integer.parseInt(characteristic.getStringValue(0).trim()));
        }
    };


    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);

            String name = "";

            try{
                name = result.getDevice().getName();
                Log.v("myApp", name);
            }catch(Exception e){
            }finally{
                if(("Pillow").equals(result.getDevice().getName())) {
                    Log.v("myApp", "FOUND");
                    gatt = result.getDevice().connectGatt(getApplicationContext(), false, callback);
                    Log.v("myApp", "CONNECTED");

                    scanner.stopScan(scanCallback);

                    bluetoothSearch.dismiss();
                }
            }
        }
    };


    public void end(){
        aInt.stopCollecting();
        gatt.close();
        finish();
    }
}
