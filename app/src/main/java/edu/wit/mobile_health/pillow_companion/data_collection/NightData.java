package edu.wit.mobile_health.pillow_companion.data_collection;

import java.util.HashMap;

public class NightData {

    private HashMap<String,SensorTimeSeries> values;

    public NightData() {
        values = new HashMap<>();
    }

    public void add(SensorTimeSeries data) {
        values.put(data.getSensor(), data);
    }

    public SensorTimeSeries getData(String sensorName) {
        return values.get(sensorName);
    }
}
