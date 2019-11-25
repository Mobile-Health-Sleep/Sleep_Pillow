package edu.wit.mobile_health.pillow_companion.data_collection;

import java.util.HashMap;

public class NightData {

    private SensorTimeSeries tempSensor;
    private SensorTimeSeries emgSensor;
    private SensorTimeSeries ecgSensor;
    private SensorTimeSeries lightSensor;


    public NightData() {

    }

    public void add(SensorTimeSeries data) {
        if (data != null) {
            switch (data.getSensor()) {
                case "Temp":
                    tempSensor = data;
                case "EMG":
                    emgSensor = data;
                case "ECG":
                    ecgSensor = data;
                case "lightSensor":
                    lightSensor = data;
            }
        }

    }

    public SensorTimeSeries getTempData() {
        return this.tempSensor;
    }
    public SensorTimeSeries getEmgData() {
        return this.emgSensor;
    }
    public SensorTimeSeries getEcgData() {
        return this.ecgSensor;
    }
    public SensorTimeSeries getLightData() {
        return this.lightSensor;
    }
}
