package xpx.audio;

import android.os.Handler;
import android.os.Message;

public class AudioHandler extends Handler {


    public AudioManager audioManager;
    public static final int EVENT_CEEAT_TTS_SUCCESS = 10010;
    public static final int EVENT_CEEAT_TTS_FAIL = 10011;
    public static final int EVENT_CYCLE_SPEECH = 10012;
    public AudioHandler(AudioManager audioManager) {
        this.audioManager = audioManager;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case EVENT_CEEAT_TTS_SUCCESS:
                audioManager.initLangue();
                break;
            case EVENT_CYCLE_SPEECH:
                audioManager.startCycleSpeech();
                break;
            case EVENT_CEEAT_TTS_FAIL:

                break;
        }
    }
}
