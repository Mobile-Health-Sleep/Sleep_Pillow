package edu.wit.mobile_health.pillow_companion.data_collection;

import java.util.List;

public class Sensor<DataType> {

    private String sensor;
    private List<DataType> values;
    private List<Integer> timeStamps;

    Sensor(String sensor){
        this.sensor = sensor;
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
