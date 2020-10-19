package xpx.audio;

import android.accounts.AbstractAccountAuthenticator;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.os.IBinder;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Locale;

import intersky.apputils.AppUtils;

public class AudioManager {

    public static volatile AudioManager audioManager;
    public Context context;
    public TextToSpeech mSpeech = null;
    public AudioHandler audioHandler;
    public boolean state = false;
    public boolean langueset = false;
    public int cycledely = 500;
    public String word = "";

    public static AudioManager init(Context context) {

        if (audioManager == null) {
            synchronized (AudioManager.class) {
                if (audioManager == null) {
                    audioManager = new AudioManager(context);
                    audioManager.audioHandler = new AudioHandler(audioManager);
                    audioManager.initTTS();
                } else {
                    audioManager.context = context;
                    audioManager.audioHandler = new AudioHandler(audioManager);
                    audioManager.initTTS();
                }
            }
        }
        return audioManager;
    }

    public void initLangue() {
        int supported = mSpeech.setLanguage(Locale.CHINESE);
        //mSpeech.setSpeechRate(0.5f);
        if ((supported != TextToSpeech.LANG_AVAILABLE) && (supported != TextToSpeech.LANG_COUNTRY_AVAILABLE)) {
            langueset = false;
        } else {
            langueset = true;
        }
    }

    public AudioManager(Context context) {
        this.context = context;
        File file = new File(AppUtils.getAssetsCacheFile(context, "google_tts.apk"));
        if (file.exists()) {
            if (AppUtils.isAppInstalled(context, "com.google.android.tts") == false) {
                install(AppUtils.getAssetsCacheFile(context, "google_tts.apk"));
            }
        }
    }

    public void audioSetting(Context context) {
        Intent intent = new Intent("com.android.settings.TTS_SETTINGS");
        intent.putExtra("extra_prefs_show_button_bar", true);
        intent.putExtra("extra_prefs_set_next_text", "完成");
        intent.putExtra("extra_prefs_set_back_text", "返回");
        context.startActivity(intent);

    }

    public void initTTS() {
        mSpeech = new TextToSpeech(context, new TTSListener(audioManager));
    }

    public void speak(Context context, String word) {
        if (state == false) {
            AppUtils.showMessage(context, context.getString(R.string.audio_not_init));
            return;
        }
        if (langueset == false) {
            AppUtils.showMessage(context, context.getString(R.string.audio_langue_no_init));
            return;
        }
        if (word.length() > 0)
            mSpeech.speak(word, TextToSpeech.QUEUE_FLUSH, null, "");
    }

    public boolean install(String apkPath) {
        boolean result = false;
        DataOutputStream dataOutputStream = null;
        BufferedReader errorStream = null;
        try {
            // 申请su权限
            Process process = Runtime.getRuntime().exec("su");
            dataOutputStream = new DataOutputStream(process.getOutputStream());
            // 执行pm install命令
            String command = "pm install -r " + apkPath + "\n";
            dataOutputStream.write(command.getBytes(Charset.forName("utf-8")));
            dataOutputStream.flush();
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            process.waitFor();
            errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String msg = "";
            String line;
            // 读取命令的执行结果
            while ((line = errorStream.readLine()) != null) {
                msg += line;
            }
            Log.d("TAG", "install msg is " + msg);
            // 如果执行结果中包含Failure字样就认为是安装失败，否则就认为安装成功
            if (!msg.contains("Failure")) {
                result = true;
            }
        } catch (Exception e) {
            Log.e("TAG", e.getMessage(), e);
        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                if (errorStream != null) {
                    errorStream.close();
                }
            } catch (IOException e) {
                Log.e("TAG", e.getMessage(), e);
            }
        }
        return result;
    }

    public static String apkProcess(String[] args) {
        String result = null;
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        Process process = null;
        InputStream errIs = null;
        InputStream inIs = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int read = -1;
            process = processBuilder.start();
            errIs = process.getErrorStream();
            while ((read = errIs.read()) != -1) {
                baos.write(read);
            }
            baos.write('\n');
            inIs = process.getInputStream();
            while ((read = inIs.read()) != -1) {
                baos.write(read);
            }
            byte[] data = baos.toByteArray();
            result = new String(data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (errIs != null) {
                    errIs.close();
                }
                if (inIs != null) {
                    inIs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (process != null) {
                process.destroy();
            }
        }
        return result;
    }

    public static boolean uninstall(String packageName) {
        String[] args = {"pm", "uninstall", packageName};
        String result = apkProcess(args);
        //Log.e(TAG, "uninstall log:" + result);
        if (result != null
                && (result.endsWith("Success") || result.endsWith("Success\n"))) {
            return true;
        }
        return false;
    }

    public void cycleSpeech(Context context, String word, int dely) {
        stopCycleSpeech();
        if (state == false) {
            AppUtils.showMessage(context, context.getString(R.string.audio_not_init));
            return;
        }
        if (langueset == false) {
            AppUtils.showMessage(context, context.getString(R.string.audio_langue_no_init));
            return;
        }
        cycledely = dely;
        if (dely == 0) {
            cycledely = 500;
        }
        this.word = word;
        if (word.length() > 0) {
            startCycleSpeech();
        }
    }

    public void startCycleSpeech() {
        if (word.length() > 0) {
            mSpeech.speak(word, TextToSpeech.QUEUE_FLUSH, null, "");
            audioHandler.sendEmptyMessageDelayed(AudioHandler.EVENT_CYCLE_SPEECH, cycledely);
        }
    }

    public void stopCycleSpeech()
    {
        audioHandler.removeMessages(AudioHandler.EVENT_CYCLE_SPEECH);
    }
}
