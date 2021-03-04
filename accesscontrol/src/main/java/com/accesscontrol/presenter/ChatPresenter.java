package com.accesscontrol.presenter;

import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.accesscontrol.R;
import com.accesscontrol.asks.MqttAsks;
import com.accesscontrol.entity.Device;
import com.accesscontrol.handler.AppHandler;
import com.accesscontrol.handler.ChatHandler;
import com.accesscontrol.receiver.ChatReceiver;
import com.accesscontrol.view.AccessControlApplication;
import com.accesscontrol.view.activity.ChatActivity;

import org.json.JSONObject;
import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.EglBase;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SessionDescription;
import org.webrtc.SurfaceTextureHelper;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

import java.security.interfaces.DSAKeyPairGenerator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import retrofit2.http.PUT;
import xpx.video.MyRoomView;
import xpx.video.PeerConnectionAdapter;
import xpx.video.SdpAdapter;
import xpx.video.XpxSignalingClient;

public class ChatPresenter implements Presenter {

    public static final int DELY = 2000;
    public static final int CHECK_TIME_OUT = 2000;
    public ChatActivity mChatActivity;
    public ChatHandler chatHandler;
    public PeerConnectionFactory peerConnectionFactory;
    public MediaStream mediaStream;
    public AudioTrack audioTrack;
    public VideoTrack videoTrack;
    public VideoSource videoSource;
    public AudioSource audioSource;
    public TextView imf;
    public VideoCapturer videoCapturer;
    public EglBase.Context eglBaseContext;
    public HashMap<String, MyRoomView> hashMap = new HashMap<String, MyRoomView>();
    public boolean callbell = false;
    public int time = 0;
    public int connecttime = 0;
    public String talkid = "";
    public String talkname = "";
    public int mastcount = 0;
    public boolean exist = false;
    public boolean isguest = false;
    public HashMap<String,String> busy = new HashMap<String,String>();
    public ChatPresenter(ChatActivity mChatActivity) {
        this.mChatActivity = mChatActivity;
        chatHandler = new ChatHandler(mChatActivity);
        mChatActivity.setBaseReceiver(new ChatReceiver(chatHandler));
    }

    @Override
    public void initView() {
        initDetial();
        if(exist == false)
        {
            if (isguest== true) {
                mastcount = mChatActivity.getIntent().getIntExtra("count",1);
                AccessControlApplication.mApp.audioManager.cycleSpeech(mChatActivity, "请等待主机接听", DELY);
                callbell = true;
                MqttAsks.startContact(mChatActivity, AccessControlApplication.mApp.aPublic);
            }
            else {
                mastcount = 1;
                mChatActivity.btnDisConntact.setImageResource(R.drawable.connect);
                MqttAsks.startContact(mChatActivity, AccessControlApplication.mApp.aPublic,mChatActivity.getIntent().getStringExtra("mid"));
            }
            startTimeOut();
        }
        else
        {
            mChatActivity.finish();
        }

    }

    public void startContact(Intent intent)
    {
        MqttAsks.startContact(mChatActivity, AccessControlApplication.mApp.aPublic,intent.getStringExtra("mid"));
    }

    public void initDetial()
    {
        isguest = mChatActivity.getIntent().getBooleanExtra("isguest", false);
        videoCapturer = AccessControlApplication.mApp.xpxCameraManager.createCameraCapturer(false,"Camera 1");
        if(videoCapturer == null)
        {
            if (isguest == false)
            {
                MqttAsks.clientExist(mChatActivity, AccessControlApplication.mApp.aPublic,mChatActivity.getIntent().getStringExtra("mid")
                        ,"被查看设备的摄像头被占用或者正在释放资源，请稍后再试或检擦设备");
            }
            else
            {
                AppUtils.showMessage(mChatActivity,"未找到可用的摄像头，或摄像头被占用，请等待资源释放2秒后重新尝试，或者重启设备");
            }
            exist = true;
        }
        else
        {
            AccessControlApplication.mApp.appSetShowFirst(true);
            mChatActivity.flagFillBack = false;
            mChatActivity.setContentView(R.layout.activity_chat);
            mChatActivity.btnDisConntact = mChatActivity.findViewById(R.id.disconnect);
            mChatActivity.btnDisConntact.setOnClickListener(disconnectlistener);
            imf = mChatActivity.findViewById(R.id.imf);
            hashMap.clear();

            eglBaseContext = EglBase.create().getEglBaseContext();
            PeerConnectionFactory.initialize(PeerConnectionFactory.InitializationOptions
                    .builder(mChatActivity)
                    .createInitializationOptions());
            PeerConnectionFactory.Options options = new PeerConnectionFactory.Options();
            DefaultVideoEncoderFactory defaultVideoEncoderFactory = new DefaultVideoEncoderFactory(eglBaseContext, true, true);
            DefaultVideoDecoderFactory defaultVideoDecoderFactory = new DefaultVideoDecoderFactory(eglBaseContext);
            peerConnectionFactory = PeerConnectionFactory.builder()
                    .setOptions(options)
                    .setVideoEncoderFactory(defaultVideoEncoderFactory)
                    .setVideoDecoderFactory(defaultVideoDecoderFactory)
                    .createPeerConnectionFactory();
            SurfaceTextureHelper surfaceTextureHelper = SurfaceTextureHelper.create("CaptureThread", eglBaseContext);

            videoSource = peerConnectionFactory.createVideoSource(videoCapturer.isScreencast());
            videoCapturer.initialize(surfaceTextureHelper, mChatActivity.getApplicationContext(), videoSource.getCapturerObserver());
            videoCapturer.startCapture(800, 1280, 30);
            audioSource = peerConnectionFactory.createAudioSource(new MediaConstraints());
            videoTrack = peerConnectionFactory.createVideoTrack(AppUtils.getguid(), videoSource);
            audioTrack = peerConnectionFactory.createAudioTrack(AppUtils.getguid(), audioSource);
            mediaStream = peerConnectionFactory.createLocalMediaStream("mediaStream");
            mediaStream.addTrack(videoTrack);
            mediaStream.addTrack(audioTrack);
            audioTrack.setEnabled(false);

        }

    }


    public void startTimeOut() {

        if(callbell == true)
        {
            if(time > AccessControlApplication.mApp.connectdely)
            {
                doExist(null);
            }
            switch (time % 3) {
                case 0:
                    imf.setText("呼叫中.");
                    break;
                case 1:
                    imf.setText("呼叫中..");
                    break;
                case 2:
                    imf.setText("呼叫中...");
                    break;
            }
            time++;
        }
        else
        {
            if(talkid.length() != 0)
            {
                MyRoomView myRoomView = hashMap.get(talkid);
                if(myRoomView.audioTrack != null)
                {
                    int m = (connecttime / 60) % 60;
                    int s = connecttime % 60;
                    imf.setText(String.format("%02d:%02d", m, s));
                    time++;
                    connecttime++;
                }
                else
                {
                    time++;
                    if(isguest == true)
                    imf.setText("对方已接听,请等待音频连接");
                    else
                        imf.setText("");
                    if(time > AccessControlApplication.mApp.connectdely)
                    {
                        doExist(null);
                    }
                }
            }
            else
            {
                time++;
                if(isguest == true)
                imf.setText("对方已接听,请等待音频连接");
                else
                    imf.setText("");
                if(time > AccessControlApplication.mApp.connectdely)
                {
                    doExist(null);
                }
            }
        }

        if((busy.size()+hashMap.size()) >= mastcount && isguest == true)
        {
            if(hashMap.size() == 0)
            {
                AppUtils.showMessage(mChatActivity,"所有应答端都在通话中");
                chatHandler = null;
                mChatActivity.baseReceiver.handler = null;
                mChatActivity.finish();

            }
        }

        if (chatHandler != null) {
            chatHandler.removeMessages(ChatHandler.DISCONNECT);
            chatHandler.sendEmptyMessageDelayed(ChatHandler.DISCONNECT, 1000);
        }
    }

    public void mastBusy(Intent intent)
    {
        String mid = intent.getStringExtra("mid");
        busy.put(mid,mid);
    }

    private void call(MyRoomView room) {
        List<PeerConnection.IceServer> iceServers = new ArrayList<>();
        iceServers.add(PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer());
        room.peerConnection = peerConnectionFactory.createPeerConnection(iceServers, new PeerConnectionAdapter("localconnection") {
            @Override
            public void onIceCandidate(IceCandidate iceCandidate) {
                super.onIceCandidate(iceCandidate);
                room.iceCandidate = iceCandidate;
                room.IceCandidateSend = true;
                MqttAsks.sendIceCandidate(mChatActivity,AccessControlApplication.mApp.aPublic,room.mid,room.iceCandidate);
                Message message = new Message();
                message.obj = room;
                message.what = ChatHandler.CHECK_ICE_RECEIVER;
                if(chatHandler != null)
                chatHandler.sendMessageDelayed(message,CHECK_TIME_OUT);
            }

            @Override
            public void onAddStream(MediaStream mediaStream) {
                if (mediaStream.audioTracks.size() > 0) {
                    room.audioTrack = mediaStream.audioTracks.get(0);
                    room.closeAudio();
                    room.connected = true;
                }
                super.onAddStream(mediaStream);
            }

        });
        room.peerConnection.addStream(mediaStream);
    }

    public void sendIceAgain(Intent intent) {

        String mid = intent.getStringExtra("mid");
        MyRoomView roomView = hashMap.get(mid);
        if(roomView != null)
        {
            if(roomView.iceCandidate != null)
            MqttAsks.sendIceCandidate(mChatActivity,AccessControlApplication.mApp.aPublic,roomView.mid,roomView.iceCandidate);
        }
    }

    public void checkIceReceived(MyRoomView myRoomView) {
        if(myRoomView.IceCandidate == false)
        {
            MqttAsks.sendIceAgain(mChatActivity,AccessControlApplication.mApp.aPublic,myRoomView.mid,myRoomView.answer);
            Message message = new Message();
            message.obj = myRoomView;
            message.what = ChatHandler.CHECK_ICE_RECEIVER;
            if(chatHandler != null)
            chatHandler.sendMessageDelayed(message,CHECK_TIME_OUT);
        }
    }

    public void onOfferReceived(Intent intent) {

        mChatActivity.runOnUiThread(() -> {
            String mid = intent.getStringExtra("mid");
            String ispd = intent.getStringExtra("spd");
            MyRoomView myRoomView = hashMap.get(mid);
            if(myRoomView == null)
            {
                myRoomView = new MyRoomView(mid);
                hashMap.put(mid,myRoomView);
                call(myRoomView);
            }

            if(myRoomView.offerreceive == false)
            {
                myRoomView.offerreceive = true;
                myRoomView.peerConnection.setRemoteDescription(new SdpAdapter("localSetRemote"),
                        new SessionDescription(SessionDescription.Type.OFFER, ispd));
                myRoomView.peerConnection.createAnswer(new SdpAdapter("localAnswerSdp") {
                    @Override
                    public void onCreateSuccess(SessionDescription sdp) {
                        super.onCreateSuccess(sdp);
                        hashMap.get(mid).peerConnection.setLocalDescription(new SdpAdapter("localSetLocal"), sdp);
                        hashMap.get(mid).answer = sdp.description;
                        MqttAsks.sendAnswer(mChatActivity,AccessControlApplication.mApp.aPublic,
                                mid,sdp.description);
                    }
                }, new MediaConstraints());
            }
            else
            {
                if(hashMap.get(mid).answer.length() > 0)
                MqttAsks.sendAnswer(mChatActivity,AccessControlApplication.mApp.aPublic,
                        mid,hashMap.get(mid).answer);
            }
        });
    }


    public void onIceCandidateReceived(Intent intent) {
        String mid = intent.getStringExtra("mid");
        MyRoomView myRoomView = hashMap.get(mid);
        if (myRoomView != null) {
            if(myRoomView.IceCandidate == false)
            {
                myRoomView.IceCandidate = true;
                myRoomView.peerConnection.addIceCandidate(new IceCandidate(
                        intent.getStringExtra("id"),
                        intent.getIntExtra("label",0),
                        intent.getStringExtra("candidate")
                ));
            }
        }

    }


    public void accept(Intent intent) {
        if (talkid.length() == 0) {
            talkid = intent.getStringExtra("cid");
            talkname = intent.getStringExtra("cname");
            audioTrack.setEnabled(true);
            MyRoomView roomView = hashMap.get(talkid);
            if(roomView != null)
            {
                Message message = new Message();
                message.what = ChatHandler.SET_VOLUE;
                message.obj = roomView;
                if(chatHandler != null)
                    chatHandler.sendMessageDelayed(message,1000);
            }
        }
        mChatActivity.btnDisConntact.setImageResource(R.drawable.disconnect);
        MqttAsks.startContactBack(mChatActivity, talkid,
                talkname, AccessControlApplication.mApp.aPublic);
        callbell = false;
        AccessControlApplication.mApp.audioManager.stopCycleSpeech();

    }


    @Override
    public void Create() {
        initView();
    }

    @Override
    public void Start() {

    }

    @Override
    public void Resume() {

    }

    @Override
    public void Pause() {

    }




    @Override
    public void Destroy() {
        AccessControlApplication.mApp.appSetShowFirst(false);
        AccessControlApplication.mApp.resetFirst();
        for (Map.Entry<String, MyRoomView> temp : hashMap.entrySet()) {
            temp.getValue().peerConnection.close();
        }

        if (chatHandler != null) {
            chatHandler.removeMessages(ChatHandler.DISCONNECT);
            chatHandler = null;
            mChatActivity.baseReceiver.handler = null;
        }
        Message message = new Message();
        message.obj = true;
        message.what = AppHandler.SET_CHAT_RES;
        if(AccessControlApplication.mApp.appHandler != null)
        AccessControlApplication.mApp.appHandler.sendMessage(message);
        Message message1 = new Message();
        message1.obj = false;
        message1.what = AppHandler.SET_CHAT_RES;
        if(AccessControlApplication.mApp.appHandler != null)
        AccessControlApplication.mApp.appHandler.sendMessageDelayed(message1,2500);
        Message msg = new Message();
        msg.obj = false;
        msg.what = AppHandler.SET_CHAT_SHOW;
        if(AccessControlApplication.mApp.appHandler != null)
            AccessControlApplication.mApp.appHandler.sendMessage(msg);
    }


    public void doExist(Intent intent) {
        if (intent != null) {
            String mid = intent.getStringExtra("mid");
            if (talkid.equals(mid)) {
                if (audioTrack != null)
                    audioTrack.setEnabled(false);
                mChatActivity.btnDisConntact.setImageResource(R.drawable.connect);
            }
            if (hashMap.containsKey(mid)) {
                MyRoomView myRoomView = hashMap.get(mid);
                myRoomView.peerConnection.close();
                hashMap.remove(mid);
            }
            if (hashMap.size() == 0) {
                callbell = false;
                AccessControlApplication.mApp.audioManager.stopCycleSpeech();
                mChatActivity.finish();
            }
        } else {
            callbell = false;
            AccessControlApplication.mApp.audioManager.stopCycleSpeech();
            MqttAsks.stopConnect(mChatActivity, AccessControlApplication.mApp.aPublic);
            mChatActivity.finish();
        }
    }


    private View.OnClickListener disconnectlistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(isguest == true)
            {
                doExist(null);
            }
            else
            {
                if (talkid.length() > 0) {
                    doExist(null);
                } else {
                    callbell = true;
                    isguest = true;
                    mChatActivity.btnDisConntact.setImageResource(R.drawable.disconnect);
                    MqttAsks.startContact(mChatActivity, AccessControlApplication.mApp.aPublic);
                    AccessControlApplication.mApp.audioManager.cycleSpeech(mChatActivity, "请等待主机接听", DELY);
                }
            }

        }
    };


    public void setAudio(MyRoomView roomView){
        roomView.openAudio();
    }

    public void refous() {
        AccessControlApplication.mApp.audioManager.stopCycleSpeech();
        audioTrack.setEnabled(false);
        mChatActivity.btnDisConntact.setImageResource(R.drawable.connect);
        MyRoomView roomView = hashMap.get(talkid);
        if(roomView != null)
        roomView.closeAudio();
        talkid = "";
        isguest = false;
    }

}
