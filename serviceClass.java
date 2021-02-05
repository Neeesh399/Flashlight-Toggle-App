package my.flashlight.toggle;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;



import static my.flashlight.toggle.App.CHANNEL_ID;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class serviceClass extends Service implements SensorEventListener {
    private static final String TAG = "Test I guess";

    public static boolean volUp = false;
    private Sensor mySensor;
    private SensorManager SM;
    private SettingsContentObserver mSettingsContentObserver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate(){
        SM = (SensorManager)getSystemService(SENSOR_SERVICE);
        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);

        mSettingsContentObserver = new SettingsContentObserver(this, new Handler());
        getApplicationContext().getContentResolver().registerContentObserver(Settings.System.CONTENT_URI, true, mSettingsContentObserver);


        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Flashlight Toggle Is On")
                //.setContentText("Flash")
                .setSmallIcon(R.drawable.ic_android)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId){
        return START_NOT_STICKY;
    }


    public void onDestroy(){
        getApplicationContext().getContentResolver().unregisterContentObserver(mSettingsContentObserver);
        super.onDestroy();
    }



    public int doesDeviceHaveFrontCamera(){
        int cameraId = -1;
        int numOfCams = Camera.getNumberOfCameras();
        for (int i=0; i<numOfCams; i++){
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK){
                cameraId = i;
                return cameraId;
            }
        }
        return cameraId;
    }
    public void toggleFlashlightOn() throws CameraAccessException {
        int cameraID = doesDeviceHaveFrontCamera();
        if (cameraID == -1){
            System.out.println("no front facing Camera");
        }
        else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                camManager.setTorchMode(cameraID+"", true);

            }

        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    public void toggleFlashlightOff() throws CameraAccessException {
        int cameraID = doesDeviceHaveFrontCamera();
        if (cameraID == -1){
            System.out.println("no front facing Camera");
        }
        else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                camManager.setTorchMode(cameraID+"", false);

            }

        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (volUp && Math.abs(event.values[0]) > 10){
            try {
                toggleFlashlightOn();

            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
        else if (!volUp && Math.abs(event.values[0]) > 10){
            try {
                toggleFlashlightOff();
            } catch (CameraAccessException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //not in use
    }

}
