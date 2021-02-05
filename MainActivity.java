package my.flashlight.toggle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;

import android.os.Build;
import android.os.Bundle;

import android.view.View;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void startService(View v){
        Intent serviceIntent = new Intent(this, serviceClass.class);
        String input = "test";
        serviceIntent.putExtra("inputExtra", input);

        startService(serviceIntent);
        System.out.println("started");
    }

    public void stopService(View v){
        Intent serviceIntent = new Intent(this, serviceClass.class);

        stopService(serviceIntent);
        System.out.println("stopped");
    }
}
