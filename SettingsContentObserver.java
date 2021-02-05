package my.flashlight.toggle;

import android.content.Context;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;

import androidx.annotation.RequiresApi;

public class SettingsContentObserver extends ContentObserver {
    private int previousVolume;
    private Context context;

    SettingsContentObserver(Context c, Handler handler){
        super(handler);
        context = c;

        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        previousVolume = audio.getStreamVolume(AudioManager.STREAM_RING);
    }

    @Override
    public boolean deliverSelfNotifications(){
        return super.deliverSelfNotifications();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onChange(boolean selfChange){
        super.onChange(selfChange);

        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        assert audio != null;
        int currentVolume = audio.getStreamVolume(AudioManager.STREAM_RING);

        int delta=previousVolume-currentVolume;
        int test = previousVolume;

        if(delta<0)
        {
            // volume decreased.
            System.out.println(delta + " decrease " + test);
            serviceClass.volUp = true;
            previousVolume=currentVolume;
        }

        else if(delta>0)
        {
            // volume increased.
            System.out.println(delta + " increase " + test);
            serviceClass.volUp = false;
            previousVolume=currentVolume;
        }

    }
}
