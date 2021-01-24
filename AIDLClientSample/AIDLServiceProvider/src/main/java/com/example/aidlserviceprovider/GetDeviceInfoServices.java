package com.example.aidlserviceprovider;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class GetDeviceInfoServices extends Service implements SensorEventListener {

    private static final String TAG = "GetDeviceInfoService";
    private Sensor mRotationSensor;
    private static final int SENSOR_DELAY = 30 * 1000; // 500ms
    private static final int FROM_RADS_TO_DEGS = -57;
    private SensorManager mSensorManager;
    private PushResultAIDLInterface pushResultAIDLInterface;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate()");
        try {
            mSensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
            mRotationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            mSensorManager.registerListener(this, mRotationSensor, SENSOR_DELAY);
        } catch (Exception e) {
            Toast.makeText(this, "Hardware compatibility issue", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new ActionAIDLInterface.Stub() {
            @Override
            public void register(PushResultAIDLInterface pushResultAIDLInterface) throws RemoteException {

                GetDeviceInfoServices.this.pushResultAIDLInterface = pushResultAIDLInterface;

            }

        };
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor == mRotationSensor) {
            if (event.values.length > 4) {
                float[] truncatedRotationVector = new float[4];
                System.arraycopy(event.values, 0, truncatedRotationVector, 0, 4);
                try {
                    update(truncatedRotationVector);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    update(event.values);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void update(float[] vectors) throws RemoteException {
        float[] rotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(rotationMatrix, vectors);
        int worldAxisX = SensorManager.AXIS_X;
        int worldAxisZ = SensorManager.AXIS_Z;
        float[] adjustedRotationMatrix = new float[9];
        SensorManager.remapCoordinateSystem(rotationMatrix, worldAxisX, worldAxisZ, adjustedRotationMatrix);
        float[] orientation = new float[3];

        SensorManager.getOrientation(adjustedRotationMatrix, orientation);
        float x = orientation[0] * FROM_RADS_TO_DEGS;
        float y = orientation[1] * FROM_RADS_TO_DEGS;
        float z = orientation[2] * FROM_RADS_TO_DEGS;

        if(pushResultAIDLInterface!=null) {

            pushResultAIDLInterface.publishResult(" IMU Values X :: " + x + "  Y ::" + y + "  Z :: "+z);

        }

}

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {


    }
}