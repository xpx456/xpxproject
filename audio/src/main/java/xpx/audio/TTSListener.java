package xpx.audio;

import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class TTSListener implements TextToSpeech.OnInitListener {

    public AudioManager audioManager;

    public TTSListener(AudioManager audioManager)
    {

        this.audioManager = audioManager;
    }
    @Override
    public void onInit(int status) {
        // TODO Auto-generated method stub
        if (status == TextToSpeech.SUCCESS) {
            audioManager.state = true;
            if(audioManager.audioHandler != null)
            audioManager.audioHandler.sendEmptyMessage(AudioHandler.EVENT_CEEAT_TTS_SUCCESS);
        }
        else{
            audioManager.state = false;
        }
    }

}
