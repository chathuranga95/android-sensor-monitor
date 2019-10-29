package com.example.sensormonitor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor sensor;
    private TextView txtGyro;
    private boolean isRunning;
    private TextView txtX;
    private TextView txtY;
    private TextView txtZ;
    private Button btnStartStop;
    private DecimalFormat decimalFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Sensor Monitor");
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        isRunning = false;
        txtX = findViewById(R.id.txtX);
        txtY = findViewById(R.id.txty);
        txtZ = findViewById(R.id.txtz);
        btnStartStop = findViewById(R.id.btnStartStop);
        decimalFormat = new DecimalFormat("##.0000 uT");
    }

    private void updateDisplay(SensorEvent sensorEvent) {
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];
        if (isRunning) {
            if (x > 0) {
                txtX.setText("+".concat(decimalFormat.format(x)));
            } else {
                txtX.setText(decimalFormat.format(x));
            }
            if (y > 0) {
                txtY.setText("+".concat(decimalFormat.format(y)));
            } else {
                txtY.setText(decimalFormat.format(y));
            }
            if (z > 0) {
                txtZ.setText("+".concat(decimalFormat.format(z)));
            } else {
                txtZ.setText(decimalFormat.format(z));
            }
        }
    }

    public void startStop(View view) {

        if (sensor == null) {
            txtGyro = findViewById(R.id.txtGyro);
            txtGyro.setText("Sensor not Found");
        } else {
            SensorEventListener sensorEventListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {
                    updateDisplay(sensorEvent);
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {
                }
            };

            if (isRunning) {
                isRunning = false;
                ((Button) view.findViewById(R.id.btnStartStop)).setText("Start");
                sensorManager.unregisterListener(sensorEventListener);
            } else {
                isRunning = true;
                btnStartStop.setText("Stop");
                sensorManager.registerListener(sensorEventListener,
                        sensor, 100 * 1000);
            }
        }
    }
}
