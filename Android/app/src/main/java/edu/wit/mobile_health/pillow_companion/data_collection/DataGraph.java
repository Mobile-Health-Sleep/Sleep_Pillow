package edu.wit.mobile_health.pillow_companion.data_collection;

import android.renderscript.Element;

import androidx.annotation.IntegerRes;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class DataGraph {
    private LineChart tempChart;
    private LineChart emgChart;
    private LineChart ecgChart;
    private LineChart lightChart;
    //private LineChart accelerChart;


    public DataGraph(LineChart chart1, LineChart chart2, LineChart chart3, LineChart chart4) {
        this.tempChart = chart1;
        this.emgChart = chart2;
        this.ecgChart = chart3;
        this.lightChart = chart4;
        initializeTempChart();
        initializeEmgChart();
        initializeEcgChart();
        initializeLightChart();
    }

    /**
     * Input collection of data from the night to graph
     * Sets up the graphs with the data
     */
    public void addData(NightData data) {
        tempChart.setData(createLineDataSet(data.getTempData()));
        emgChart.setData(createLineDataSet(data.getEmgData()));
        ecgChart.setData(createLineDataSet(data.getEcgData()));
        lightChart.setData(createLineDataSet(data.getLightData()));
        //accelerChart.setData(createLineDataSet(data.getData("Accelerometer")));

    }

    private LineData createLineDataSet(SensorTimeSeries series) {
        ArrayList<Entry> entries = new ArrayList();
        if (series == null) {
            return null;
        }
        List<Integer> x = series.getTimes();
        List<Integer> y = series.getValues();
        for (int i = 0; i < x.size(); i++) {
            entries.add(new Entry(x.get(i), y.get(i)));
        }
        LineDataSet dataSet = new LineDataSet(entries, String.format("%s Sensor Data", series.getSensor()));

        return new LineData(dataSet);
    }

    private void initializeTempChart() {
        tempChart.getDescription().setEnabled(false);
        tempChart.getAxisRight().setEnabled(false);
        XAxis xAxis = tempChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    }
    private void initializeEmgChart() {
        emgChart.getDescription().setEnabled(false);
        emgChart.getAxisRight().setEnabled(false);
        XAxis xAxis = emgChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    }
    private void initializeEcgChart() {
        ecgChart.getDescription().setEnabled(false);
        ecgChart.getAxisRight().setEnabled(false);
        XAxis xAxis = ecgChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    }
    private void initializeLightChart() {
        lightChart.getDescription().setEnabled(false);
        lightChart.getAxisRight().setEnabled(false);
        XAxis xAxis = ecgChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    }
}
