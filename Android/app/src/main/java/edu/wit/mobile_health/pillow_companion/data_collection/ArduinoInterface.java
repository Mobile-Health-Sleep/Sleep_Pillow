package edu.wit.mobile_health.pillow_companion.data_collection;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;

import static java.lang.System.currentTimeMillis;

public class ArduinoInterface extends Thread {

    private boolean collecting;

    private final int DELAY = 500;

    private BluetoothGatt gatt;
    private BluetoothGattCharacteristic tx;
    private BluetoothGattCharacteristic rx;

    private SensorTimeSeries temp = new SensorTimeSeries("temp");
    private SensorTimeSeries light = new SensorTimeSeries("light");

    //Acceleration sensors
//    private SensorTimeSeries accelX = new SensorTimeSeries("accelX");
//    private SensorTimeSeries accelY = new SensorTimeSeries("accelY");
//    private SensorTimeSeries accelZ = new SensorTimeSeries("accelZ");

    private SensorTimeSeries emg = new SensorTimeSeries("emg");

    private File directory;

    private Calendar c;

    private long startTime;
    private String currentSensor;


    public ArduinoInterface(BluetoothGatt gatt, BluetoothGattCharacteristic tx, BluetoothGattCharacteristic rx){
        this.gatt = gatt;

        this.tx = tx;
        this.rx = rx;

        this.collecting = true;

        c = Calendar.getInstance();

        directory = new File("data/data/edu.wit.mobile_health.pillow_companion/files",
                ((c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH) + "-" + c.get(Calendar.YEAR)));

        if(!directory.exists()){
            directory.mkdir();
        }

    }

    public String getCurrentSensor() {
        return currentSensor;
    }

    @Override
    public void run() {
        c = Calendar.getInstance();

        startTime = currentTimeMillis();

        super.run();

        Log.v("myApp", "starting");
        Log.v("myApp",tx.toString());
        Log.v("myApp",rx.toString());

        while(collecting){

            currentSensor="Temp";
            tx.setValue(("/temp /").getBytes(Charset.forName("UTF-8")));
            if(gatt.writeCharacteristic((tx))){
                try{
                    Thread.sleep(this.DELAY);

                }catch(Exception e){
                    e.printStackTrace();
                }
            }

            currentSensor="Light";
            tx.setValue(("/light /").getBytes(Charset.forName("UTF-8")));
            if(gatt.writeCharacteristic((tx))){
                try{
                    Thread.sleep(this.DELAY);

                }catch(Exception e){
                    e.printStackTrace();
                }
            }



//            tx.setValue(("/accelX").getBytes(Charset.forName("UTF-8")));
//            if(gatt.writeCharacteristic((tx))){
//                gatt.readCharacteristic(rx);
//                Log.v("myApp", rx.getValue().toString());
//
//                accelX.append(10, (currentTimeMillis()-startTime));
//            }
//
//
//            tx.setValue(("/accelY").getBytes(Charset.forName("UTF-8")));
//            if(gatt.writeCharacteristic((tx))){
//                gatt.readCharacteristic(rx);
//                Log.v("myApp", rx.getValue().toString());
//
//                accelY.append(10, (currentTimeMillis()-startTime));
//            }
//
//
//            tx.setValue(("/accelZ").getBytes(Charset.forName("UTF-8")));
//            if(gatt.writeCharacteristic((tx))){
//                gatt.readCharacteristic(rx);
//                Log.v("myApp", rx.getValue().toString());
//
//                accelZ.append(10, (currentTimeMillis()-startTime));
//            }


//            tx.setValue(("/emg").getBytes(Charset.forName("UTF-8")));
//            if(gatt.writeCharacteristic((tx))){
//                gatt.readCharacteristic(rx);
//                Log.v("myApp", rx.getValue().toString());
//
//                emg.append(10, (currentTimeMillis()-startTime));
//            }
        }
    }

    public void stopCollecting(){
        this.collecting = false;

        exportToCSV();

    }

    public void appendData(String sensor, int value){
        switch(sensor){
            case "Temp":
                temp.append(value, currentTimeMillis()-startTime);
                break;

            case "Light":
                light.append(value, currentTimeMillis()-startTime);
                break;
        }
    }

    private void exportToCSV(){
        this.temp.exportToCsv(this.directory.toString());
        this.light.exportToCsv(this.directory.toString());
//        this.accelX.exportToCsv(this.directory.toString());
//        this.accelY.exportToCsv(this.directory.toString());
//        this.accelZ.exportToCsv(this.directory.toString());
//        this.emg.exportToCsv(this.directory.toString());

    }
}
