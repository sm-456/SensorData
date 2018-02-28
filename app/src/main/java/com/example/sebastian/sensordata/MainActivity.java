package com.example.sebastian.sensordata;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_MESSAGE = "com.example.sebastian.sensordata.MESSAGE";
    public static final int MAX_DEVICES = 10;
    TextView textPath;
    int sensors[] = new int[MAX_DEVICES];
    public int actual_sensors = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Button fileButton = (Button) findViewById(R.id.fileButton);
        //fileButton.setOnClickListener(this);
        textPath = (TextView) findViewById(R.id.textPath);
    }

    @Override
    public void onClick(View v) {
        /*
        EditText etZaehler = (EditText) findViewById(R.id.zaehler);
        EditText etNenner = (EditText) findViewById(R.id.nenner);
        String sz = etZaehler.getText().toString();
        String sn = etNenner.getText().toString();
        if (sz.length() == 0  ||  sn.length() == 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Bitte Zähler und Nenner eintragen");
            AlertDialog dialog = builder.create();
            dialog.show();
            return;
        }

        int z = Integer.parseInt(sz);
        int n = Integer.parseInt(sn);
        if (z!=0) {
            int rest;
            int ggt = Math.abs(z);
            int divisor = Math.abs(n);
            do {
                rest = ggt % divisor;
                ggt = divisor;
                divisor = rest;
            } while(rest > 0);
            z /= ggt;
            n /= ggt;
        } else
            n = 1;
        etZaehler.setText(Integer.toString(z));
        etNenner.setText(Integer.toString(n));
        */
    }
    public void getData(View view){
        Intent intent = new Intent(this, BluetoothActivity.class);
        //EditText editText = (EditText) findViewById(R.id.editText);
        String message = "test";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
        //intent.putExtra(EXTRA_MESSAGE, message);
        //startActivity(intent);
    }

    public void readData(View view){
        //String path = Environment.getExternalStorageDirectory().toString()+"/Download/sensordata";
        String path = Environment.getExternalStorageDirectory().toString() + File.separator + "sensordata";
        int counter = 0;
        Log.d("Files","Path1: " + path);
        File directory = new File(path);
        File file[] = directory.listFiles();
        String[] filesInDir = directory.list();
        for(int i = 0; i < filesInDir.length; i++){
            Log.d("Files", "Filename: " + filesInDir[i]);
            if(filesInDir[i].length()<10)
                counter++;
        }

        Log.d("Files","path1 exists: " + directory.exists());
        actual_sensors = counter;

        //for (int k = 0; k<file.length; k++){
        //    Log.d("Files", "Filename: " + file[k].toString());
        //}
        //Log.d("Files", filenames[0]);


        /*
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: " + files.length);
        for (int i = 0; i<files.length; i++){
            Log.d("Files", "FileName: " + files[i].getName());
        }
        */
    }

    public void sensors(View view){
        Intent intent = new Intent(this, SensorActivity.class);
        //EditText editText = (EditText) findViewById(R.id.editText);
        String message = Integer.toString(actual_sensors);
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
        //intent.putExtra(EXTRA_MESSAGE, message);
        //startActivity(intent);
    }

    public void createFile(View view){
        String filename = "sensor00_20180202.csv";
        File file = new File(this.getFilesDir(), filename);
        String string = "Hello world!";
        textPath.setText(this.getFilesDir().toString());
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("test", "test");

    }

}
