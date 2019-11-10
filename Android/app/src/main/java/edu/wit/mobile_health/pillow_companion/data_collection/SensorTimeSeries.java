package edu.wit.mobile_health.pillow_companion.data_collection;

import java.util.LinkedList;
import java.util.List;

public class SensorTimeSeries<DataType> {

    private String sensorType;
    private List<DataType> values;
    private List<Integer> timeStamps;

    SensorTimeSeries(String sensor){
        this.sensorType = sensor;
        values = new LinkedList<>();
        timeStamps = new LinkedList<>();
    }

    public void append(DataType value, int time){
        values.add(value);
        timeStamps.add(new Integer(time));
    }

    public List<DataType> getValues(){
        return values;
    }

    public List<Integer> getTimes(){
        return timeStamps;
    }
}
