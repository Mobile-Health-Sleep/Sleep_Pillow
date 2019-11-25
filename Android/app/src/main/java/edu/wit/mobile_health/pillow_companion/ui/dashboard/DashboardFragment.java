package edu.wit.mobile_health.pillow_companion.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.github.mikephil.charting.charts.LineChart;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Calendar;
//import java.util.Date;

import edu.wit.mobile_health.pillow_companion.MainActivity;
import edu.wit.mobile_health.pillow_companion.R;
import edu.wit.mobile_health.pillow_companion.data_collection.Date;
import edu.wit.mobile_health.pillow_companion.data_collection.DataGraph;
import edu.wit.mobile_health.pillow_companion.data_collection.NightData;
import edu.wit.mobile_health.pillow_companion.data_collection.SensorTimeSeries;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private MainActivity activity;

    private Date selectedDate;
    private TextView date;
    private DatePicker dateView;
    private DataGraph chartView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        initializeCharts(root);

        activity = (MainActivity) getActivity();

        Calendar c = Calendar.getInstance();
        selectedDate = new Date(c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.YEAR));

        date = root.findViewById(R.id.date_display);
        updateDate(selectedDate);
        updateCharts();

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonShowPopupWindowClick(view);
            }
        });

        return root;
    }

    public void onButtonShowPopupWindowClick(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                activity.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_calendar, null);

        // create the popup window
        int width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        int height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        dateView = popupView.findViewById(R.id.datePicker);

        dateView.updateDate(selectedDate.getYear(), selectedDate.getMonth(), selectedDate.getDay());

        dateView.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                selectedDate.setMonth(i1+1);
                selectedDate.setDay(i2);
                selectedDate.setYear(i);
                updateDate(selectedDate);
                updateCharts();
                popupWindow.dismiss();
            }
        });

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);


    }

    public void updateDate(Date currentDate){
        date.setText(currentDate.toString());
    }

    
    public void initializeCharts(View root) {
        LineChart chart = root.findViewById(R.id.chart);
        LineChart chart1 = root.findViewById(R.id.chart1);
        LineChart chart2 = root.findViewById(R.id.chart2);
        LineChart chart3 = root.findViewById(R.id.chart3);

        chartView = new DataGraph(chart, chart1, chart2, chart3);
    }

    public void updateCharts() {
        NightData night = createData();
        chartView.addData(night);
    }

    private NightData createData() {
        String currentDate = ((selectedDate.getMonth() + "-" + selectedDate.getDay() + "-" + selectedDate.getYear()));
        String filePath = String.format("data/data/edu.wit.mobile_health.pillow_companion/files/%s", currentDate);
        NightData data = new NightData();
        final String [] fileNames = {"temp.csv","ECG","EMG","light.csv"};
        final String [] sensorNames = {"Temp", "ECG", "EMG", "Light"};
        for (int i = 0; i < sensorNames.length; i++) {
            data.add(readDataFromFile(String.format("%s/%s", filePath, fileNames[i]), sensorNames[i]));
        }

        return data;
    }

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
