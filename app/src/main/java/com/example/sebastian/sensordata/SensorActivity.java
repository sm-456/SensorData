package com.example.sebastian.sensordata;

import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.InputStream;
import java.lang.Object;
import android.app.Fragment;

public class SensorActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public int selection = 0;
    public int date_selection = 0;
    public int graph_selection = 0;

    public String filepath = new String();
    public String csv_file_date = new String();
    public ArrayList<String> timeList = new ArrayList<>();
    public ArrayList<Integer> temperatureList = new ArrayList<>();
    public ArrayList<Integer> pressureList = new ArrayList<>();
    public ArrayList<Integer> humidityList = new ArrayList<>();
    public ArrayList<Integer> moistureList = new ArrayList<>();

    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_overview);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        int actual_sensors = Integer.parseInt(message);
        TextView sensorMessage = findViewById(R.id.sensorMessage);
        sensorMessage.setText("active sensor devices: " + message);

        Spinner spinner1 = (Spinner) findViewById(R.id.sensorSelect);
        Spinner spinner2 = (Spinner) findViewById(R.id.dateSelect);
        Spinner spinner3 = (Spinner) findViewById(R.id.graphSelect);

        spinner1.setOnItemSelectedListener(this);
        spinner2.setOnItemSelectedListener(this);
        spinner3.setOnItemSelectedListener(this);

        List<Integer> spinnerArray = new ArrayList<>();
        for (int i = 0; i < actual_sensors; i++) {
            spinnerArray.add(i);
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(
                this,
                android.R.layout.simple_spinner_item,
                spinnerArray
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                this,
                R.array.parameter_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter2);


    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Spinner spin = (Spinner) parent;
        int tmp_int = 0;
        if (spin.getId() == R.id.sensorSelect) {
            String temp = new String();
            selection = (Integer) parent.getItemAtPosition(pos);
            Log.d("Spinner", "selection: " + Integer.toString(selection));

            if (selection < 10)
                temp = "0" + Integer.toString(selection);
            else
                temp = Integer.toString(selection);


            String path = Environment.getExternalStorageDirectory().toString() + File.separator + "sensordata" + File.separator + "sensor" + temp;
            File directory = new File(path);
            File file[] = directory.listFiles();
            String[] filesInDir = directory.list();
            tmp_int = filesInDir.length;
            String[] dates_string = new String[tmp_int];
            int[] dates_int = new int[tmp_int];
            for (int i = 0; i < tmp_int; i++) {
                dates_string[i] = filesInDir[i].substring(9, 17);
                dates_int[i] = Integer.parseInt(dates_string[i]);
                Log.d("dates", dates_string[i]);
            }

            List<Integer> spinnerArray = new ArrayList<>();
            for (int i = 0; i < tmp_int; i++) {
                spinnerArray.add(dates_int[i]);
            }
            ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(
                    this,
                    android.R.layout.simple_spinner_item,
                    spinnerArray
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Spinner spinner2 = (Spinner) findViewById(R.id.dateSelect);
            spinner2.setAdapter(adapter);
        } else if (spin.getId() == R.id.dateSelect) {
            date_selection = (Integer) parent.getItemAtPosition(pos);
            Log.d("Spinner", "selection: " + Integer.toString(date_selection));
            String temp = new String();
            String temp2 = new String();

            if (selection < 10)
                temp = "0" + Integer.toString(selection);
            else
                temp = Integer.toString(selection);

            temp2 = Integer.toString(date_selection);
            csv_file_date = temp2;
            String subpath = "sensor" + temp + File.separator + "sensor" + temp + "_" + temp2 + ".csv";
            filepath = Environment.getExternalStorageDirectory().toString() + File.separator + "sensordata" + File.separator + subpath;


        } else if (spin.getId() == R.id.graphSelect) {
            graph_selection = pos;
            Log.d("Spinner", "pos: " + Integer.toString(pos));
        }


    }

    public void onNothingSelected(AdapterView<?> parent) {
        // interface callback
    }

    private List<CSVdataset> datasets = new ArrayList<>();

    public void readFile(View view) throws IOException {
        int i = 0;
        Log.d("CSV", filepath);

        File csv_file = new File (filepath);
        CSVReader reader = new CSVReader(new FileReader(csv_file));
        String [] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            // nextLine[] is an array of values from the line
            //System.out.println(nextLine[0] + nextLine[1] + "etc...");
            Log.d("CSV", "Value: " + nextLine[1]);
            if(i==0) {
                i = 1;
            } else{
                timeList.add(nextLine[0]);
                temperatureList.add(Integer.parseInt(nextLine[1]));
                pressureList.add(Integer.parseInt(nextLine[2]));
                humidityList.add(Integer.parseInt(nextLine[3]));
                moistureList.add(Integer.parseInt(nextLine[4]));
            }

        }

    }

    public void drawGraph(View view) {
        int listCount = temperatureList.size();
        Log.d("CSV", "length: " + Integer.toString(listCount));
        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.removeAllSeries();
        DataPoint temp_data = null;
        Calendar timestamp = Calendar.getInstance();
        Date d = null;
        Date lower_bound = new Date();
        Date upper_bound = new Date();
        float minY = 9999999;
        float maxY = 0;
        float diffY = 0;
        int year, month, day, hour, min ,sec;
        String time_string = new String();
        year = Integer.parseInt(csv_file_date.substring(0,4));
        month = Integer.parseInt(csv_file_date.substring(4,6));
        day = Integer.parseInt(csv_file_date.substring(6,8));
        Log.d("date", "date: " + Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day));
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {});
        if (graph_selection == 0){
            // temperature
            for(int k = 0; k<listCount; k++){
                time_string = timeList.get(k);
                hour = Integer.parseInt(time_string.substring(0,2));
                min = Integer.parseInt(time_string.substring(3,5));
                sec = Integer.parseInt(time_string.substring(6,8));
                Log.d("date", "time: " + Integer.toString(hour) + "-" + Integer.toString(min) + "-" + Integer.toString(sec));
                timestamp.set(year, month, day, hour, min, sec);
                d = timestamp.getTime();
                temp_data = new DataPoint(d, (float)temperatureList.get(k)/100);
                series.appendData(temp_data, true, listCount);
                if (k == 0) {
                    lower_bound = d;
                    minY = temperatureList.get(k);
                    maxY = minY;
                }
                else {
                    if (k == (listCount - 1)) {
                        upper_bound = d;
                    }
                    if(temperatureList.get(k)<minY)
                        minY = temperatureList.get(k);
                    else{
                        if(temperatureList.get(k)>maxY)
                            maxY = temperatureList.get(k);
                    }
                }
            }
            minY = minY/100;
            maxY = maxY/100;
        }else if(graph_selection == 1) {
            // pressure
            for(int k = 0; k<listCount; k++){
                time_string = timeList.get(k);
                hour = Integer.parseInt(time_string.substring(0,2));
                min = Integer.parseInt(time_string.substring(3,5));
                sec = Integer.parseInt(time_string.substring(6,8));
                timestamp.set(year, month, day, hour, min, sec);
                d = timestamp.getTime();
                temp_data = new DataPoint(d, (float)pressureList.get(k)/10);
                series.appendData(temp_data, true, listCount);
                if (k == 0) {
                    lower_bound = d;
                    minY = pressureList.get(k);
                    maxY = minY;
                }
                else {
                    if (k == (listCount - 1)) {
                        upper_bound = d;
                    }
                    if(pressureList.get(k)<minY)
                        minY = pressureList.get(k);
                    else{
                        if(pressureList.get(k)>maxY)
                            maxY = pressureList.get(k);
                    }
                }
            }
            maxY = maxY/10;
            minY = minY/10;

        }else if(graph_selection == 2) {
            // humidity
            for(int k = 0; k<listCount; k++){
                time_string = timeList.get(k);
                hour = Integer.parseInt(time_string.substring(0,2));
                min = Integer.parseInt(time_string.substring(3,5));
                sec = Integer.parseInt(time_string.substring(6,8));
                timestamp.set(year, month, day, hour, min, sec);
                d = timestamp.getTime();
                temp_data = new DataPoint(d, (float)humidityList.get(k)/100);
                series.appendData(temp_data, true, listCount);
                if (k == 0) {
                    lower_bound = d;
                    minY = humidityList.get(k);
                    maxY = minY;
                }
                else {
                    if (k == (listCount - 1)) {
                        upper_bound = d;
                    }
                    if(humidityList.get(k)<minY)
                        minY = humidityList.get(k);
                    else{
                        if(humidityList.get(k)>maxY)
                            maxY = humidityList.get(k);
                    }
                }
                maxY = maxY/100;
                minY = minY/100;
            }

        }else if(graph_selection == 3) {
            // moisture
            for(int k = 0; k<listCount; k++){
                time_string = timeList.get(k);
                hour = Integer.parseInt(time_string.substring(0,2));
                min = Integer.parseInt(time_string.substring(3,5));
                sec = Integer.parseInt(time_string.substring(6,8));
                timestamp.set(year, month, day, hour, min, sec);
                d = timestamp.getTime();
                temp_data = new DataPoint(d, moistureList.get(k));
                series.appendData(temp_data, true, listCount);
                if (k == 0)
                    lower_bound = d;
                else if (k == (listCount-1)){
                    upper_bound = d;
                }
            }

        }
        else{}

        series.setBackgroundColor(Color.WHITE);
        series.setColor(Color.BLUE);
        series.setThickness(10);
        graph.addSeries(series);

        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // show normal x values
                    return sdf.format(new Date((long) value));
                } else {
                    // show currency for y values
                    return super.formatLabel(value, isValueX);
                }
            }
        });
        Log.d("axis", "min: " + Float.toString(minY) + " max: " + Float.toString(maxY) );
        if(maxY>minY){
            diffY = (float) (maxY-minY);
            minY = (float)(minY - 0.1*diffY);
            maxY = (float)(maxY + 0.1*diffY);
        }

        series.setColor(Color.BLUE);
        graph.getGridLabelRenderer().setNumHorizontalLabels(4);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(minY);
        graph.getViewport().setMaxY(maxY);
        Log.d("axis", "min: " + Float.toString(minY) + " max: " + Float.toString(maxY) );

//        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(SensorActivity.this, SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT)));
//        graph.getGridLabelRenderer().setNumHorizontalLabels(4); // only 4 because of the space
//        graph.getGridLabelRenderer().setNumVerticalLabels(6);
//        graph.getViewport().setBackgroundColor(Color.WHITE);
//
//        // set manual x bounds to have nice steps
//     graph.getViewport().setXAxisBoundsManual(true);
//     graph.getViewport().setMinX(lower_bound.getTime());
//     graph.getViewport().setMaxX(upper_bound.getTime());
//        //graph.getViewport().setXAxisBoundsManual(true);
//
//        // as we use dates as labels, the human rounding to nice readable numbers
//        // is not necessary
//        graph.getGridLabelRenderer().setHumanRounding(true);
//        //graph.getLegendRenderer().setVisible(false);

    }
}


