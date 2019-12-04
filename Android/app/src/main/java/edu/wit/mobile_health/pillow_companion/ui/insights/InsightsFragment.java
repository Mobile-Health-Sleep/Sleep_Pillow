package edu.wit.mobile_health.pillow_companion.ui.insights;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import edu.wit.mobile_health.pillow_companion.R;
import edu.wit.mobile_health.pillow_companion.data_collection.SensorTimeSeries;

public class InsightsFragment extends Fragment {

    private InsightsViewModel insightsViewModel;
    private LinearLayout insights;
    private TextView noInsights;

    private final int TEMPTHRESHOLDMIN = 100;
    private final int TEMPTHRESHOLDMAX = 300;
    private final int LIGHTTHRESHOLD = 100;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        insightsViewModel =
                ViewModelProviders.of(this).get(InsightsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_insights, container, false);

        this.insights = root.findViewById(R.id.insights_layout);
        this.noInsights = root.findViewById(R.id.temp_text);

        Calendar c = Calendar.getInstance();

        updateInsights(c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.YEAR));


        return root;
    }

    //TODO: Add insights

    private boolean checkTempsMax(int month, int day, int year){
        SensorTimeSeries temps = readDataFromFile(String.format("data/data/edu.wit.mobile_health.pillow_companion/files/%d/%d/%d", month, day, year), "temp.csv");
        List<Integer> values = temps.getValues();

        Iterator<Integer> valueIterator = values.iterator();

        while(valueIterator.hasNext()){
            if(valueIterator.next() > TEMPTHRESHOLDMAX){
                return true;
            }
        }

        return false;
    }

    private boolean checkTempsMin(int month, int day, int year){
        SensorTimeSeries temps = readDataFromFile(String.format("data/data/edu.wit.mobile_health.pillow_companion/files/%d/%d/%d", month, day, year), "temp.csv");
        List<Integer> values = temps.getValues();

        Iterator<Integer> valueIterator = values.iterator();

        while(valueIterator.hasNext()){
            if(valueIterator.next() < TEMPTHRESHOLDMIN){
                return true;
            }
        }

        return false;
    }

    private boolean checkLight(int month, int day, int year){
        SensorTimeSeries light = readDataFromFile(String.format("data/data/edu.wit.mobile_health.pillow_companion/files/%d/%d/%d", month, day, year), "light.csv");
        List<Integer> values = light.getValues();

        Iterator<Integer> valueIterator = values.iterator();

        while(valueIterator.hasNext()){
            if(valueIterator.next() < LIGHTTHRESHOLD){
                return true;
            }
        }


        return false;
    }


    private void updateInsights(int month, int day, int year){

        boolean tempsmin = false;
        boolean tempsmax = false;
        boolean light = false;

        try {
            tempsmin = checkTempsMin(month, day, year);
            tempsmax = checkTempsMax(month, day, year);
            light = checkLight(month, day, year);
        }catch(Exception e){

        }

        this.insights.removeAllViews();

        if (tempsmin || tempsmax || light){
            if(tempsmax){
                TextView temp = new TextView(getActivity().getApplicationContext());
                temp.setText("It looks like it got a little cold last night. Try to put on more blankets or turn the heat up a little more for a more restful sleep.");
                temp.setPadding(0, 0, 0, 100);
                temp.setTextSize(20);

                this.insights.addView(temp);
            }

            if(tempsmin){
                TextView temp = new TextView(getActivity().getApplicationContext());
                temp.setText("It looks like it got a little hot last night. Try to turn up the AC or open a window to cool down the room");
                temp.setPadding(0, 0, 0, 100);
                temp.setTextSize(20);

                this.insights.addView(temp);
            }

            if(light){
                TextView temp = new TextView(getActivity().getApplicationContext());
                temp.setText("It was kinda bright last night. Try to close the shades or turn off the lights to get a better sleep.");
                temp.setPadding(0, 0, 0, 100);
                temp.setTextSize(20);

                this.insights.addView(temp);
            }

        }else{
            this.insights.addView(this.noInsights);
        }
    }

    //TODO: Refact this as part of the SensorTimeSeries class
    private SensorTimeSeries readDataFromFile(String filePath, String sensorName) {
        try {
            Log.v("myApp", filePath);
            SensorTimeSeries series = new SensorTimeSeries(sensorName);

            BufferedReader br = new BufferedReader(new FileReader(filePath));

            final String DELIMITER = ",";

            String [] time = br.readLine().split(DELIMITER);
            String [] data = br.readLine().split(DELIMITER);

            for (int i = 1; i < time.length; i++) {
                series.append(Integer.parseInt(time[i]), Integer.parseInt(data[i]));
            }
            br.close();

            return series;
        }
        catch(Exception ex) {
            Log.v("DATA COLLECTION", "FILE COULD NOT BE FOUND");
            ex.printStackTrace();
            return null;
        }


    }
}