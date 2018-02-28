package com.example.sebastian.sensordata;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.Set;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class BluetoothActivity extends AppCompatActivity {
    private final static String TAG = "BluetoothActivity";
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mDevice;
    private EditText mMessageET;
    private Button mSendBN;

    private void findRaspberry() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        for(BluetoothDevice device : pairedDevices){
            if(device.getName().equals("raspberrypi_101") || device.getName().equals("raspberrypi")){
                this.mDevice=device;
                Log.d(TAG, "paired device found");
            }

        }
    }

    private void initBluetooth(){
        Log.d(TAG, "Checking Bluetooth...");
        if(mBluetoothAdapter == null) {
            Log.d(TAG, "Device does not support Bluetooth");
            mSendBN.setClickable(false);
        }else{
            Log.d(TAG, "Bluetooth supported");
            mSendBN.setClickable(true);
        }
        if(!mBluetoothAdapter.isEnabled()){
            mSendBN.setClickable(false);
            Log.d(TAG, "Bluetooth not enabled");
        }else{
            Log.d(TAG, "Bluetooth enabled");
            mSendBN.setClickable(true);
        }
    }

    public void onSend(View view){
        String message = mMessageET.getText().toString();
        new MessageThread(mDevice, message).start();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        mMessageET = (EditText) findViewById(R.id.message);
        mSendBN = (Button) findViewById(R.id.sendButton);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        initBluetooth();
        findRaspberry();
        if(mDevice == null)
            mSendBN.setClickable(true);

    }
}
