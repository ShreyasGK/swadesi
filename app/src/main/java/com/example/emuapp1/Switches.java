package com.example.emuapp1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import java.nio.charset.Charset;

public class Switches extends AppCompatActivity {
    Connection mBluetoothConnection;
    Switch mSwitch1,mSwitch2,mSwitch3,mSwitch4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switches);

        mSwitch1 = findViewById(R.id.switch1);
        mSwitch2 = findViewById(R.id.switch2);
        mSwitch3 = findViewById(R.id.switch3);
        mSwitch4 = findViewById(R.id.switch4);

        mSwitch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSwitch1.isChecked()){
                    byte[] bytes = "1111".getBytes(Charset.defaultCharset());
                    mBluetoothConnection.write(bytes);
                    showToast("Switch 1 is on");
                } else {
                    byte[] bytes = "1000".getBytes(Charset.defaultCharset());
                    mBluetoothConnection.write(bytes);
                    showToast("Switch 1 is off");
                }
            }
        });
        mSwitch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSwitch2.isChecked()){
                    byte[] bytes = "2222".getBytes(Charset.defaultCharset());
                    mBluetoothConnection.write(bytes);
                    showToast("Switch 2 is on");
                } else {
                    byte[] bytes = "2000".getBytes(Charset.defaultCharset());
                    mBluetoothConnection.write(bytes);
                    showToast("Switch 2 is off");
                }
            }
        });
        mSwitch3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSwitch3.isChecked()){
                    byte[] bytes = "3333".getBytes(Charset.defaultCharset());
                    mBluetoothConnection.write(bytes);
                    showToast("Switch 3 is on");
                } else {
                    byte[] bytes = "3000".getBytes(Charset.defaultCharset());
                    mBluetoothConnection.write(bytes);
                    showToast("Switch 3 is off");
                }
            }
        });
        mSwitch4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSwitch4.isChecked()){
                    byte[] bytes = "4444".getBytes(Charset.defaultCharset());
                    mBluetoothConnection.write(bytes);
                    showToast("Switch 4 is on");
                } else {
                    byte[] bytes = "4000".getBytes(Charset.defaultCharset());
                    mBluetoothConnection.write(bytes);
                    showToast("Switch 4 is off");
                }
            }
        });
    }
    private void showToast(String msg) {
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show();
    }
}