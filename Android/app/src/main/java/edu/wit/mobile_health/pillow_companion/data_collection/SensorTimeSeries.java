package edu.wit.mobile_health.pillow_companion.data_collection;

import java.util.LinkedList;
import java.util.List;

public class SensorTimeSeries {

    private String sensorType;
    private List<Integer> values;
    private List<Integer> timeStamps;

    public SensorTimeSeries(String sensor){
        this.sensorType = sensor;
        values = new LinkedList<>();
        timeStamps = new LinkedList<>();
    }

    public void append(Integer value, Integer time){
        values.add(value);
        timeStamps.add(new Integer(time));
    }

    public List<Integer> getValues(){
        return values;
    }

    public List<Integer> getTimes(){
        return timeStamps;
    }

    public String getSensor(){ return sensorType; }

}
