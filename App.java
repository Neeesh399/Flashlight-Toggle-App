package my.flashlight.toggle;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    public static final String CHANNEL_ID = "test ig";

    @Override
    public void onCreate(){
        super.onCreate();

        System.out.println("test 1.2");
        createNotificationChannel();
    }

    public void createNotificationChannel(){
        System.out.println("test 1.1");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            System.out.println("test 1.0");
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

}
