package de.timfraenkert.motioncontrolapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    TextView txt_accelerationX ;
    TextView txt_accelerationY ;
    TextView txt_accelerationZ ;

    TextView txt_rotationX ;
    TextView txt_rotationY ;
    TextView txt_rotationZ ;



    private static final DecimalFormat df = new DecimalFormat("0.00");

    private double acceerationCurrentValue;
    private double acceerationPreviousValue = 0;

    float[] mGravity = new float[3];
    float[] mGeomagnetic = new float[3];

    private SensorManager sensorManager;
    private Sensor sensor;
    private Sensor linearSensor;
    private Sensor geomagneticSensor;


    @Override
    public void onSensorChanged(SensorEvent event) {
        handleRotation(event);
        handleAccelaration(event);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void handleAccelaration(SensorEvent event){
        if(event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
            float[] accel = new float[3];
            accel = event.values;
            float x = accel[0];
            float y = accel[1];
            float z = accel[2];

            txt_accelerationX.setText("X: "+ x + "m/S^2");
            txt_accelerationY.setText("Y: "+ y + "m/S^2");
            txt_accelerationZ.setText("Z: "+ z + "m/S^2");
        }

    }

    private void handleRotation(SensorEvent event){
        // norden, vertikal, horizontal
        float azimuth,pitch, roll;

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;

        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];

            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                azimuth = orientation[0]; // orientation contains: azimuth, pitch and roll
                pitch = orientation[1];
                roll = orientation[2];

                txt_rotationX.setText("Azimuth : " +  Math.toDegrees(azimuth));
                txt_rotationY.setText("Pitch : " + (Math.toDegrees(pitch) + 90.0f));
                txt_rotationZ.setText("roll : " + Math.toDegrees(roll));
            }
        }

    }


    private SensorEventListener rotationListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {

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
        linearSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        geomagneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        txt_accelerationX = (TextView) findViewById(R.id.txt_accelerationX);
        txt_accelerationY = (TextView) findViewById(R.id.txt_accelerationY);
        txt_accelerationZ = (TextView) findViewById(R.id.txt_accelerationZ);

        txt_rotationX = (TextView) findViewById(R.id.txt_xRotation);
        txt_rotationY = (TextView) findViewById(R.id.txt_yRotation);
        txt_rotationZ = (TextView) findViewById(R.id.txt_zRotation);

    }

    public void onStart() {
        super.onStart();
        // enable our sensor when the activity is resumed, ask for
        sensorManager.registerListener(this, sensor, 100000);
        sensorManager.registerListener(this, linearSensor, 100000);
        sensorManager.registerListener(this, geomagneticSensor, 100000);
        // 10 ms updates.


    }
    public void onStop() {
        super.onStop();
        // make sure to turn our sensor off when the activity is paused
        sensorManager.unregisterListener(this, sensor);
        sensorManager.unregisterListener(this, linearSensor);
        sensorManager.unregisterListener(this, geomagneticSensor);
    }



}