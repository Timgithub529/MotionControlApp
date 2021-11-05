package de.timfraenkert.motioncontrolapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView txt_acceleration ;
    TextView txt_currentAccel ;
    TextView txt_prevAccel ;

    private double acceerationCurrentValue;
    private double acceerationPreviousValue = 0;

    private SensorManager sensorManager;
    private Sensor sensor;


    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            acceerationCurrentValue = Math.sqrt( (x * x + y * y + z * z));
            double changeInAccelleration = Math.abs(acceerationCurrentValue - acceerationPreviousValue);
            acceerationPreviousValue = acceerationCurrentValue;

            // update Text views
            txt_currentAccel.setText("Current = " + acceerationCurrentValue);
            txt_prevAccel.setText("Prev = " + acceerationPreviousValue);
            txt_acceleration.setText("Acceleation change =" + changeInAccelleration);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Linear ACCELERATION sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        txt_acceleration = (TextView) findViewById(R.id.acceleration);
        txt_currentAccel = (TextView)  findViewById(R.id.txt_currentAccel);
        txt_prevAccel = (TextView)  findViewById(R.id.txt_prevAccel);

    }

    public void onStart() {
        super.onStart();
        // enable our sensor when the activity is resumed, ask for
        // 10 ms updates.
        sensorManager.registerListener(sensorEventListener, sensor, 10000);
    }
    public void onStop() {
        super.onStop();
        // make sure to turn our sensor off when the activity is paused
        sensorManager.unregisterListener(sensorEventListener);
    }



}