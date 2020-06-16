package intersky.chat.entity;

import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;

import intersky.appbase.entity.Conversation;
import intersky.chat.ContactManager;
import intersky.chat.R;



public class UPlayer {

    private final String TAG = UPlayer.class.getName();
    private String path;
    private Conversation mUserMessageModel;
    public MediaPlayer mPlayer;
    public Handler mHander;
    public onFinish onFinish;
    public UPlayer(String path, Conversation mUserMessageModel, Handler mHander,onFinish onFinish) {
        this.path = path;
        this.mUserMessageModel = mUserMessageModel;
        this.mHander = mHander;
        mPlayer = new MediaPlayer();
        this.onFinish = onFinish;
        mPlayer.setOnCompletionListener(mOnCompletionListener);

    }

    public boolean start() {
        try {
            //设置要播放的文件
            mPlayer.setDataSource(path);
            mPlayer.prepare();
            //播放
            mPlayer.start();
        } catch (Exception e) {
            Log.e(TAG, "prepare() failed");
        }

        return false;
    }

    public boolean stop() {
        mPlayer.stop();
        mPlayer.release();
        mPlayer = null;
        return false;
    }

    public MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer mp) {


            onFinish.onFinish(mUserMessageModel);
            mPlayer.release();
            mPlayer = null;
        }
    };

    public interface onFinish{
        void onFinish(Conversation conversation);
    }

}
