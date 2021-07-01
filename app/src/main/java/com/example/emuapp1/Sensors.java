package com.example.emuapp1;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.nio.charset.Charset;


public class Sensors extends AppCompatActivity implements SensorEventListener{

    private static final String TAG="Sensors";
    private SensorManager sm;
    private Sensor accelerometer,mMag,mGravity,mLight;
    Connection mBluetoothConnection;

    TextView xvalue , yvalue , zvalue;
    TextView xMagVal,yMagVal,zMagVal,lightval,gravval;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);

        xvalue = (TextView) findViewById(R.id.xvalue);
        yvalue = (TextView) findViewById(R.id.yvalue);
        zvalue = (TextView) findViewById(R.id.zvalue);

        xMagVal = (TextView) findViewById(R.id.xmagval);
        yMagVal = (TextView) findViewById(R.id.ymagval);
        zMagVal = (TextView) findViewById(R.id.zmagval);

        lightval = (TextView) findViewById(R.id.lightval);
        gravval = (TextView) findViewById(R.id.gravval);


        Log.d("onCreate","Initializing Sensor Services");
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(accelerometer != null){
            sm.registerListener(Sensors.this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
            Log.d("onCreate:","Registered Accelo");
        } else {
            xvalue.setText("Accelerometer not supported");
        }

        mMag = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if(mMag != null){
            sm.registerListener(Sensors.this,mMag,SensorManager.SENSOR_DELAY_NORMAL);
            Log.d("onCreate:","Registered Mag");
        } else {
            xMagVal.setText("Mag not supported");
        }

        mLight = sm.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(mLight != null){
            sm.registerListener(Sensors.this,mLight,SensorManager.SENSOR_DELAY_NORMAL);
            Log.d("onCreate:","Registered Light");
        } else {
            lightval.setText("Light sensor not supported");
        }

        mGravity = sm.getDefaultSensor(Sensor.TYPE_GRAVITY);
        if(mGravity != null){
            sm.registerListener(Sensors.this,mGravity,SensorManager.SENSOR_DELAY_NORMAL);
            Log.d("onCreate:","Registered Gravity temp");
        } else {
            gravval.setText("Gravity not supported");
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        Sensor sensor = sensorEvent.sensor;
        if(sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            String xacc = String.valueOf(sensorEvent.values[0]);
            String yacc = String.valueOf(sensorEvent.values[0]);
            String zacc = String.valueOf(sensorEvent.values[0]);
            xvalue.setText("x: "+ xacc);
            yvalue.setText("y: "+ yacc);
            zvalue.setText("z: "+ zacc);
            byte[] bytes = xacc.toString().getBytes(Charset.defaultCharset());
            mBluetoothConnection.write(bytes);
            byte[] bytes1 = yacc.toString().getBytes(Charset.defaultCharset());
            mBluetoothConnection.write(bytes1);
            byte[] bytes2 = zacc.toString().getBytes(Charset.defaultCharset());
            mBluetoothConnection.write(bytes2);
        }

        else if(sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            String xmag = String.valueOf(sensorEvent.values[0]);
            String ymag = String.valueOf(sensorEvent.values[0]);
            String zmag = String.valueOf(sensorEvent.values[0]);
            xMagVal.setText("x: "+ xmag);
            yMagVal.setText("y: "+ ymag);
            zMagVal.setText("z: "+ zmag);
            byte[] bytes = xmag.toString().getBytes(Charset.defaultCharset());
            mBluetoothConnection.write(bytes);
            byte[] bytes1 = ymag.toString().getBytes(Charset.defaultCharset());
            mBluetoothConnection.write(bytes1);
            byte[] bytes2 = zmag.toString().getBytes(Charset.defaultCharset());
            mBluetoothConnection.write(bytes2);
        }

        else if(sensor.getType() == Sensor.TYPE_LIGHT){
            String lit = String.valueOf(sensorEvent.values[0]);
            lightval.setText("Light: "+ lit);
            byte[] bytes = lit.toString().getBytes(Charset.defaultCharset());
            mBluetoothConnection.write(bytes);
        }

        else if(sensor.getType() == Sensor.TYPE_GRAVITY){
            String grav = String.valueOf(sensorEvent.values[0]);
            gravval.setText("Gravity: "+ grav);
            byte[] bytes = grav.toString().getBytes(Charset.defaultCharset());
            mBluetoothConnection.write(bytes);
        }

    }
}

    //byte[] bytes = st4.toString().getBytes(Charset.defaultCharset());
    //               mBluetoothConnection.write(bytes);