package xpx.sound;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

public class SoundManager {


    public static volatile SoundManager soundManager;
    public Context context;
    public SoundPool soundPool;
    public int voiceId;
    public boolean prepared = false;
    public boolean isfirst = true;
    public static SoundManager init(Context context)
    {
        if (soundManager == null) {
            synchronized (SoundManager.class) {
                if (soundManager == null) {
                    soundManager = new SoundManager(context);
                } else {

                }
            }
        }
        return soundManager;
    }

    public SoundManager(Context context){
        this.context = context;
        if (Build.VERSION.SDK_INT >= 21) {
            SoundPool.Builder builder = new SoundPool.Builder();
            //传入最多播放音频数量,
            builder.setMaxStreams(1);
            //AudioAttributes是一个封装音频各种属性的方法
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            //设置音频流的合适的属性
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
            //加载一个AudioAttributes
            builder.setAudioAttributes(attrBuilder.build());
            soundPool = builder.build();
        } else {
            /**
             * 第一个参数：int maxStreams：SoundPool对象的最大并发流数
             * 第二个参数：int streamType：AudioManager中描述的音频流类型
             *第三个参数：int srcQuality：采样率转换器的质量。 目前没有效果。 使用0作为默认值。
             */
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (status == 0) {
                    //第一个参数soundID
                    //第二个参数leftVolume为左侧音量值（范围= 0.0到1.0）
                    //第三个参数rightVolume为右的音量值（范围= 0.0到1.0）
                    //第四个参数priority 为流的优先级，值越大优先级高，影响当同时播放数量超出了最大支持数时SoundPool对该流的处理
                    //第五个参数loop 为音频重复播放次数，0为值播放一次，-1为无限循环，其他值为播放loop+1次
                    //第六个参数 rate为播放的速率，范围0.5-2.0(0.5为一半速率，1.0为正常速率，2.0为两倍速率)\]

                    //soundPool.play(voiceId, 1, 1, 1, 0, 1);
                    prepared = true;
                }
            }
        });
    }


    public void setSound(int id) {
       this.voiceId = soundPool.load(context, id, 1);

    }

    public void doPlay()
    {
        if(voiceId > 0 && prepared)
        {
            if(isfirst)
            {
                isfirst = false;
                soundPool.play(voiceId, 1, 1, 1, -1, 1);
            }
            else
            {
                soundPool.resume(voiceId);
            }

        }

    }

    public void doPause()
    {
        if(voiceId > 0 && prepared)
        {
            soundPool.pause(voiceId);
        }

    }
}
