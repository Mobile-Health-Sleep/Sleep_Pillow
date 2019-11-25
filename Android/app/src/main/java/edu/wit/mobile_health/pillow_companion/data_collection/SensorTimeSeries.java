package edu.wit.mobile_health.pillow_companion.data_collection;

import android.util.Log;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SensorTimeSeries<Integer> {

    private String sensorType;
    private List<Integer> values;
    private List<Long> timeStamps;


    public SensorTimeSeries(String sensor){
        this.sensorType = sensor;
        values = new LinkedList<>();
        timeStamps = new LinkedList<>();
    }

    public void append(Integer value, long time){

        values.add(value);
        timeStamps.add(new Long(time));
    }

    public List<Integer> getValues(){
        return values;
    }

    public List<Long> getTimes(){
        return timeStamps;
    }

    public void exportToCsv(String pathName){
        StringBuilder file = new StringBuilder();

        Iterator iterator = this.values.listIterator();

        while(iterator.hasNext()){
            file.append(iterator.next().toString());
            file.append(",");
        }

        file.append("\n");

        iterator = this.timeStamps.listIterator();

        while(iterator.hasNext()){
            file.append(iterator.next().toString());
            file.append(",");
        }

        file.append("\n");

        try{
            FileWriter CSVWriter = new FileWriter(pathName + '/' + this.sensorType + ".csv");
            CSVWriter.append(file.toString());
            CSVWriter.close();

        }catch (IOException e){
            e.printStackTrace();
        }

    }
    public String getSensor(){ return sensorType; }

}
