package com.example.user.zhtx.Map;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorManage {

    private OrientationSensorListener orientationSensorListener;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private static SensorManage mSensorManage;

    private boolean mHasStarted = false;

    public static SensorManage newInstance() {
        if (mSensorManage == null) {
            mSensorManage = new SensorManage();
        }
        return mSensorManage;
    }

    public void starts(Context context) {
        if (mHasStarted) {
            return;
        }
        mHasStarted = true;

        //传感器
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        if (mSensor != null){
            orientationSensorListener = new OrientationSensorListener();
            mSensorManager.registerListener(orientationSensorListener,
                    mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void stops() {
        if (!mHasStarted || mSensorManager == null) {
            return;
        }
        mHasStarted = false;
        mSensorManager.unregisterListener(orientationSensorListener);
    }

    public float getLux1() {
        if (orientationSensorListener != null) {
            return orientationSensorListener.lux1;
        }
        return -1.0f;
    }

    private class OrientationSensorListener implements SensorEventListener {

        private float lux1;

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            lux1 = sensorEvent.values[0];
        //    Log.i("方向0：", String.valueOf(lux1));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }
}
