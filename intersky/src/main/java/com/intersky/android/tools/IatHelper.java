package com.intersky.android.tools;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.intersky.android.handler.IfHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

import intersky.appbase.CommendManager;
import intersky.appbase.PermissionCode;
import intersky.appbase.PermissionResult;
import intersky.apputils.AppUtils;
import intersky.chat.RecordAudioPremissionResult;
import intersky.json.XpxJSONArray;
import intersky.json.XpxJSONObject;

public class IatHelper {
    public static final String PREFER_NAME = "com.iflytek.setting";
    private SpeechRecognizer mIat;
    private RecognizerDialog mIatDialog;
    private SharedPreferences mSharedPreferences;
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private Activity mContext;
    public Handler handler;
    public CommendManager commendManager;
    public interface doResult
    {
        void doResult();
    }

    public IatHelper(Activity mContext,CommendManager commendManager)
    {
       this.mContext = mContext;
       this.commendManager = commendManager;
    }


    public IatPremissionResult initArs()
    {
        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        handler = new IfHandler(this);
        mIat = SpeechRecognizer.createRecognizer(mContext, mInitListener);
        mIatDialog = new RecognizerDialog(mContext, mInitDialogListener);

        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源

        mSharedPreferences = mContext.getSharedPreferences(PREFER_NAME,
                Activity.MODE_PRIVATE);
        return new IatPremissionResult(this.mContext,this);
    }

    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d("speach", "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
//                showTip("初始化失败，错误码：" + code);
                AppUtils.showMessage(mContext,"初始化失败，错误码：" + code);
            }
        }
    };

    private InitListener mInitDialogListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d("speach", "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
//                showTip("初始化失败，错误码：" + code);
                AppUtils.showMessage(mContext,"初始化失败，错误码：" + code);
            }
        }
    };

    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        String lag = mSharedPreferences.getString("iat_language_preference",
                "mandarin");
        if (lag.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
            mIat.setParameter(SpeechConstant.ACCENT, null);
        } else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);
        }
        //此处用于设置dialog中不显示错误码信息
        //mIat.setParameter("view_tips_plain","false");

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "4000"));

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/iat.wav");
    }

    public IatPremissionResult doStart() {
        AppUtils.getPermission(Manifest.permission.RECORD_AUDIO, mContext, PermissionCode.PERMISSION_REQUEST_AUDIORECORD, handler);
        return new IatPremissionResult(mContext,this);
    }

    public void start()
    {
        //FlowerCollector.onEvent(mContext, "iat_recognize");
        mIatResults.clear();
//        // 设置参数
        setParam();
        boolean isShowDialog = mSharedPreferences.getBoolean(
                "iat_show", true);
        if (isShowDialog) {
            // 显示听写对话框

            if (mIat.isListening()) {
                mIat.stopListening();
            }


            mIatDialog.setListener(mRecognizerDialogListener);
            mIatDialog.show();
        } else {
            // 不显示听写对话框
            mIat.startListening(mRecognizerListener);
//            if (ret != ErrorCode.SUCCESS) {
//                showTip("听写失败,错误码：" + ret);
//            } else {
//                showTip(getString(R.string.text_begin));
//            }
        }
    }

    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            String commend = getWolrdCommend(results.getResultString());
            commendManager.doCommend(mContext,commend);
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            int code = error.getErrorCode();
        }

    };

    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            AppUtils.showMessage(mContext,"开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            AppUtils.showMessage(mContext,"结束说话");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {

            if (isLast) {
                // TODO 最后的结果
                String commend = getWolrdCommend(results.getResultString());
                commendManager.doCommend(mContext,commend);
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            AppUtils.showMessage(mContext,"当前正在说话，音量大小："+ volume);
            Log.d("taggg", "返回音频数据："+data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    public void destory()
    {
        if( null != mIat ){
            // 退出时释放连接
            mIat.cancel();
            mIat.destroy();
        }
    }

    public String getWolrdCommend(String json) {
        String word = "";
        try {

            XpxJSONObject jsonObject = new XpxJSONObject(json);
            XpxJSONArray jsonArray = jsonObject.getJSONArray("ws");
            for(int i = 0 ; i < jsonArray.length() ; i++)
            {
                XpxJSONObject jsonObject1 = jsonArray.getJSONObject(i);
                XpxJSONArray ja = jsonObject1.getJSONArray("cw");
                for(int j = 0 ; j < ja.length() ; j++)
                {
                    XpxJSONObject jo = ja.getJSONObject(j);
                    word += jo.getString("w");
                }
                if(word.length() > 0)
                {
                    word += "";
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return word;
    }
}
