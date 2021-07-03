package com.example.emuapp1;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.BreakIterator;
import java.util.UUID;


public class Connection {
    private static final String TAG = "ConnectionServ";
    private static final String appName = "Emulator";
    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("9cdcb420-39dd-43e8-b7df-ade981893896");
    private final BluetoothAdapter mBluetoothAdapter;
    Context mContext;
    private AcceptThread mInSecureAcceptThread;
    private ConnectThread mConnectThread;
    private BluetoothDevice mmDevice;
    private UUID deviceUUID;
    static ProgressDialog mProgressDialog;
    private ConnectedThread mConnectedThread;


    public Connection(Context context) {
        mContext = context;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        start();
    }

    private class AcceptThread extends Thread{
            //local server socket
            private final BluetoothServerSocket mmServerSocket;

            public AcceptThread(){
                BluetoothServerSocket tmp = null;
                //new listening server socket
            try{
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(appName,MY_UUID_INSECURE);
                Log.d(TAG,"AcceptThread: Setting up server using: "+MY_UUID_INSECURE);
            } catch (IOException e) {
                Log.e(TAG,"AcceptThread: Unable to accept: "+e.getMessage());
            }
            mmServerSocket = tmp;
            }
            public void run(){
                Log.d(TAG,"run: AcceptThread Running");
                BluetoothSocket socket = null;
                try{
                    Log.d(TAG,"run: RFCOMM server socket start....");
                    socket = mmServerSocket.accept();
                    Log.d(TAG,"run: RFCOMM server socket started connection");
                } catch (IOException e) {
                    Log.e(TAG,"AcceptThread: Unable to accept: "+e.getMessage());
                }
                if (socket != null){
                    connected(socket,mmDevice);
                }
                Log.i(TAG,"End mAcceptThread");
            }
            public void cancel(){
                Log.d(TAG,"cancel: Cancelling AcceptThread");
                try{
                    mmServerSocket.close();
                } catch (IOException e) {
                    Log.e(TAG,"cancel: Close of acceptthread failed"+e.getMessage());
                }
            }
        }

    private class ConnectThread extends Thread{
        private BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice device, UUID uuid){
            Log.d(TAG,"ConnectThread: Started");
            mmDevice = device;
            deviceUUID = uuid;
        }
        public void run(){
            BluetoothSocket tmp = null;
            Log.i(TAG,"RUN mConnectThread");

            try{
                Log.d(TAG,"ConnectThread: Trying to create InsecureRfcommSocket using UUID:"+MY_UUID_INSECURE);
                tmp = mmDevice.createInsecureRfcommSocketToServiceRecord(deviceUUID);
            } catch (IOException e) {
                Log.e(TAG,"ConnectThread: Could not create InsecureRfCommSocket");
            }
            mmSocket = tmp;
            mBluetoothAdapter.cancelDiscovery();

            try {
                mmSocket.connect();
                Log.d(TAG, "run: ConnectThread connected."+mmSocket.isConnected());
            } catch (IOException e) {
                try {
                    mmSocket.close();
                    Log.d(TAG, "run: Closed Socket.");
                } catch (IOException e1) {
                    Log.e(TAG, "mConnectThread: run: Unable to close connection in socket " + e1.getMessage());
                }
                Log.d(TAG, "run: ConnectThread: Could not connect to UUID: " + MY_UUID_INSECURE );
            }
            connected(mmSocket,mmDevice);
        }
        public void cancel(){
            try{
                Log.d(TAG,"cancel:Closing Client Socket");
                mmSocket.close();
            } catch (IOException e){
                Log.e(TAG,"cancel:close() of mmSocket in Connection failed"+e.getMessage());
            }
        }
    }


    public synchronized void start(){
        Log.d(TAG,"start");
        if (mConnectThread != null){
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if (mInSecureAcceptThread == null){
            mInSecureAcceptThread = new AcceptThread();
            mInSecureAcceptThread.start();
        }
    }
    public void startClient(BluetoothDevice device,UUID uuid){
        Log.d(TAG,"startClient:Started");
        mProgressDialog = ProgressDialog.show(mContext,"Progress","Connecting Bluetooth,Please wait...",true);

     mConnectThread = new ConnectThread(device, uuid);
     mConnectThread.start();
    }

    private class ConnectedThread extends Thread{
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket){
            Log.d(TAG,"ConnectedThread: Starting.");

            mmSocket = socket;
            InputStream tempIn = null;
            OutputStream tempOut = null;

            try{
                mProgressDialog.dismiss();
            } catch (NullPointerException e){
                e.printStackTrace();
            }

            try{
                tempIn = mmSocket.getInputStream();
                tempOut = mmSocket.getOutputStream();
            } catch (IOException e){
                e.printStackTrace();
            }
            mmInStream = tempIn;
            mmOutStream = tempOut;
        }

        public void run(){
            byte[] buffer = new byte[1024];
            int bytes;
            while (true){
                try{
                    bytes = mmInStream.read(buffer);
                    String incomingMessage = new String(buffer,0,bytes);

                    switch (incomingMessage){
                        case "1000":
                            incomingMessage = "LED 1 is OFF";
                            break;
                        case "1111":
                            incomingMessage = "LED 1 is ON";
                            break;

                        case "2000":
                            incomingMessage = "LED 2 is OFF";
                            break;
                        case "2222":
                            incomingMessage = "LED 2 is ON";
                            break;

                        case "3000":
                            incomingMessage = "LED 3 is OFF";
                            break;
                        case "3333":
                            incomingMessage = "LED 3 is ON";
                            break;

                        case "4000":
                            incomingMessage = "LED 4 is OFF";
                            break;
                        case "4444":
                            incomingMessage = "LED 4 is ON";
                            break;

                        default: incomingMessage = new String(buffer,0,bytes);
                            break;

                    }

                    Log.d(TAG, "Input Stream: " + incomingMessage);
                    Intent incomingMessageIntent = new Intent("incomingMessage");
                    incomingMessageIntent.putExtra("theMessage",incomingMessage);
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(incomingMessageIntent);
                } catch (IOException e) {
                    Log.e(TAG,"RUN: Error reading from i/p stream : "+e.getMessage());
                    break;
                }
            }
        }
        public void write(byte[] bytes){
            String text = new String(bytes, Charset.defaultCharset());
            Log.d(TAG,"Write:Writing to OutputStream: "+text);
            try{
                mmOutStream.write(bytes);
            }catch (IOException e){
                Log.e(TAG,"Write: Error writing to o/p stream : "+e.getMessage());
            }
        }
        public void cancel(){
            try{
                mmSocket.close();
            }catch(IOException e){}
        }
    }
    private void connected(BluetoothSocket mmSocket, BluetoothDevice mmDevice) {
        Log.d(TAG,"connected: Starting");
        mConnectedThread = new ConnectedThread(mmSocket);
        mConnectedThread.start();
    }
    public void write(byte[] out){
        ConnectedThread r;
        Log.d(TAG,"Write: write called");
        mConnectedThread.write(out);
    }
}
