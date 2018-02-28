package com.example.sebastian.sensordata;
/**
 * Created by sebas on 15.02.2018.
 */


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.UUID;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class MessageThread extends Thread {
    private final static String TAG = "MessageThread";
    //
    // private final static String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    private final static String MY_UUID = "7be1fcb3-5776-42fb-91fd-2ee7b5bbb86d";
    private BluetoothSocket mSocket = null;
    private String mMessage;
    private BluetoothDevice testDevice = null;

    public MessageThread(BluetoothDevice device, String message) {
        Log.d(TAG, "Trying to send message...");
        this.testDevice = device;
        this.mMessage = message;
        try{
            UUID uuid = UUID.fromString(MY_UUID);
            //Log.d(TAG, "uuid: " + (String) uuid);
            mSocket = device.createRfcommSocketToServiceRecord(uuid);
            //mSocket = (BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(device,1);

        } catch (IOException e) {
            Log.d(TAG, "failed to create socket");
            e.printStackTrace();
        }
    }

    private void manageConnectedSocket(BluetoothSocket socket) throws IOException {
        Log.d(TAG, "Connection successfull");
        OutputStream os = socket.getOutputStream();
        PrintStream sender = new PrintStream(os);
        sender.print(mMessage);
        Log.d(TAG,"Message sent");
        InputStream is = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        Log.d(TAG, "Received: " + reader.readLine());
    }

    public void run(){
        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
        try{
            mSocket = testDevice.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));
            mSocket.connect();
            manageConnectedSocket(mSocket);
            mSocket.close();

        } catch (IOException e) {
            Log.d(TAG, "socket connect failed, try again...");
            e.printStackTrace();
            try{
                mSocket =(BluetoothSocket) testDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(testDevice,1);
                mSocket.connect();
                Log.d(TAG, "socket connect successful on 2nd try");
            }catch(Exception e2){
                Log.d(TAG, "second try failed");
                e2.printStackTrace();
            }

        }

//        try{
//
//        } catch(IOException e3){
//            Log.d(TAG, "socket error");
//            e3.printStackTrace();
//        }

    }
}
