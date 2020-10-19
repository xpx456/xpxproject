package com.accessmaster.presenter;

import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.accessmaster.R;
import com.accessmaster.asks.MqttAsks;
import com.accessmaster.handler.AppHandler;
import com.accessmaster.handler.ChatHandler;
import com.accessmaster.receiver.ChatReceiver;
import com.accessmaster.view.AccessMasterApplication;
import com.accessmaster.view.activity.ChatActivity;

import org.json.JSONObject;
import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.EglBase;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.MediaStreamTrack;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.RtpReceiver;
import org.webrtc.SessionDescription;
import org.webrtc.SurfaceTextureHelper;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.filetools.PathUtils;
import intersky.filetools.entity.Video;
import xpx.video.MyRoomView;
import xpx.video.PeerConnectionAdapter;
import xpx.video.ProxyVideoSink;
import xpx.video.SdpAdapter;
import xpx.video.XpxSignalingClient;
import xpx.video.XpxSignalingClient;

public class ChatPresenter implements Presenter {

    public static final int DELY = 2000;
    public static final int DATA_TIME_OUT = 2000;
    public ChatActivity mChatActivity;
    public PeerConnectionFactory peerConnectionFactory;
    public PeerConnection peerConnection;
    public SurfaceViewRenderer remoteView;
    public MediaStream mediaStream;
    public int stat = 0;
    public AudioTrack audioTrack;
    public AudioSource audioSource;
    public ChatHandler chatHandler;
    public AudioTrack showAudioTrack;
    public int time = 0;
    public IceCandidate iceCandidate;
    public String offer = "";
    public String clientid = "";
    public boolean receiverAnswer = false;
    public boolean receiverIce = false;
    public VideoTrack videoTrack;
    public VideoSource videoSource;
    public VideoCapturer videoCapturer;
    public ChatPresenter(ChatActivity mChatActivity) {
        this.mChatActivity = mChatActivity;
        chatHandler = new ChatHandler(mChatActivity);
        mChatActivity.setBaseReceiver(new ChatReceiver(chatHandler));
    }


    @Override
    public void initView() {
        initDetial();
        if(mChatActivity.getIntent().getBooleanExtra("isguest",false) == true)
        {
            call();
            AccessMasterApplication.mApp.audioManager.cycleSpeech(mChatActivity,mChatActivity.getIntent().getStringExtra("cname")+"呼叫",DELY);
            creatOffer();
        }
        else
        {
            call();
            creatOffer();
        }

    }

    public void receiverCall(Intent intent)
    {
        if(intent.getStringExtra("cid").equals(clientid))
        {
            AccessMasterApplication.mApp.audioManager.cycleSpeech(mChatActivity,mChatActivity.getIntent().getStringExtra("cname")+"呼叫",DELY);
        }
        else{
            MqttAsks.doBuesy(mChatActivity,AccessMasterApplication.mApp.aPublic
                    ,intent.getStringExtra("cid"),AccessMasterApplication.mApp.clidenid,true);
        }

    }

    public void initDetial()
    {
        clientid = mChatActivity.getIntent().getStringExtra("cid");
        mChatActivity.flagFillBack = false;
        mChatActivity.setContentView(R.layout.activity_chat);
        mChatActivity.btnConnect = mChatActivity.findViewById(R.id.disconnect);
        mChatActivity.btnOpen = mChatActivity.findViewById(R.id.open);
        mChatActivity.btnOut = mChatActivity.findViewById(R.id.outbtn);
        mChatActivity.btnConnect.setOnClickListener(contactListener);
        mChatActivity.btnOut.setOnClickListener(outListener);
        mChatActivity.btnOpen.setOnClickListener(openListener);
        AccessMasterApplication.mApp.stopFirst = true;
        EglBase.Context eglBaseContext = EglBase.create().getEglBaseContext();
        // create PeerConnectionFactory
        PeerConnectionFactory.initialize(PeerConnectionFactory.InitializationOptions
                .builder(mChatActivity)
                .createInitializationOptions());
        PeerConnectionFactory.Options options = new PeerConnectionFactory.Options();
        DefaultVideoEncoderFactory defaultVideoEncoderFactory =
                new DefaultVideoEncoderFactory(eglBaseContext, true, true);
        DefaultVideoDecoderFactory defaultVideoDecoderFactory =
                new DefaultVideoDecoderFactory(eglBaseContext);
        peerConnectionFactory = PeerConnectionFactory.builder()
                .setOptions(options)
                .setVideoEncoderFactory(defaultVideoEncoderFactory)
                .setVideoDecoderFactory(defaultVideoDecoderFactory)
                .createPeerConnectionFactory();
        SurfaceTextureHelper surfaceTextureHelper = SurfaceTextureHelper.create("CaptureThread", eglBaseContext);
        // create VideoCapturer
        videoCapturer = AccessMasterApplication.mApp.xpxCameraManager.createCameraCapturer(false,"Camera 1");
        videoSource = peerConnectionFactory.createVideoSource(videoCapturer.isScreencast());
        videoCapturer.initialize(surfaceTextureHelper, mChatActivity.getApplicationContext(), videoSource.getCapturerObserver());
        videoCapturer.startCapture(480, 640, 30);
        videoTrack = peerConnectionFactory.createVideoTrack(AppUtils.getguid(), videoSource);

        remoteView = mChatActivity.findViewById(xpx.video.R.id.remoteView);
        remoteView.init(eglBaseContext, null);
        audioSource = peerConnectionFactory.createAudioSource(new MediaConstraints());
        audioTrack = peerConnectionFactory.createAudioTrack(AppUtils.getguid(), audioSource);
        mediaStream = peerConnectionFactory.createLocalMediaStream("mediaStream");
        mediaStream.addTrack(audioTrack);
        mediaStream.addTrack(videoTrack);
        audioTrack.setEnabled(false);
        checckDisconnect();
    }

    public void oncheckAnswerReceiver() {
        if(receiverAnswer == false)
        {
            creatOffer();
        }

    }

    public void oncheckIceReceiver() {
        if(receiverIce == false)
        {
            MqttAsks.sendIceAgain(mChatActivity,AccessMasterApplication.mApp.aPublic,clientid);
            if(chatHandler != null)
            chatHandler.sendEmptyMessageDelayed(ChatHandler.CHECK_ICE_RECEIVER,DATA_TIME_OUT);
        }
    }

    public void creatOffer()
    {
        if(offer.length() == 0)
        {
            peerConnection.createOffer(new SdpAdapter("local offer sdp") {
                @Override
                public void onCreateSuccess(SessionDescription sessionDescription) {
                    super.onCreateSuccess(sessionDescription);
                    peerConnection.setLocalDescription(new SdpAdapter("local set local"), sessionDescription);
                    offer = sessionDescription.description;
                    MqttAsks.sendOffer(mChatActivity,AccessMasterApplication.mApp.aPublic,clientid,sessionDescription.description);
                }
            }, new MediaConstraints());
        }
        else
        {
            MqttAsks.sendOffer(mChatActivity,AccessMasterApplication.mApp.aPublic,clientid,offer);
        }
        if(chatHandler != null)
        chatHandler.sendEmptyMessageDelayed(ChatHandler.CHECK_ANSWER_RECEIVER,DATA_TIME_OUT);
    }


    public void onAnswerReceived(Intent intent) {
        if(receiverAnswer == false)
        {
            String spd = intent.getStringExtra("spd");
            receiverAnswer = true;
            peerConnection.setRemoteDescription(new SdpAdapter("localSetRemote"),
                    new SessionDescription(SessionDescription.Type.ANSWER, spd));
        }

    }

    public void onIceCandidateReceived(Intent intent) {
        if(receiverIce == false)
        {
            receiverIce = true;
            peerConnection.addIceCandidate(new IceCandidate(
                    intent.getStringExtra("id"),
                    intent.getIntExtra("label",0),
                    intent.getStringExtra("candidate")
            ));
        }

    }



    public void call() {
        List<PeerConnection.IceServer> iceServers = new ArrayList<>();
        iceServers.add(PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer());
        peerConnection = peerConnectionFactory.createPeerConnection(iceServers, new PeerConnectionAdapter("localconnection") {
            @Override
            public void onIceCandidate(IceCandidate iceCandidate) {
                super.onIceCandidate(iceCandidate);
                mChatActivity.chatPresenter.iceCandidate = iceCandidate;
                MqttAsks.sendIceCandidate(mChatActivity,AccessMasterApplication.mApp.aPublic,clientid,iceCandidate);
                if(chatHandler != null)
                chatHandler.sendEmptyMessageDelayed(ChatHandler.CHECK_ICE_RECEIVER,DATA_TIME_OUT);
            }

            @Override
            public void onAddStream(MediaStream mediaStream) {
                super.onAddStream(mediaStream);
                AudioTrack audioTrack = mediaStream.audioTracks.get(0);
                showAudioTrack = audioTrack;
                showAudioTrack.setEnabled(true);
                super.onAddStream(mediaStream);
                if(mediaStream.videoTracks.size() > 0)
                {
                    VideoTrack remoteVideoTrack = mediaStream.videoTracks.get(0);
                    mChatActivity.runOnUiThread(() -> {
                        remoteVideoTrack.setEnabled(true);
                        ProxyVideoSink videoSink = new ProxyVideoSink();
                        videoSink.setTarget(remoteView);
                        remoteVideoTrack.addSink(videoSink);
                    });
                }
            }
        });

        peerConnection.addStream(mediaStream);
    }

    public void sendIceAgain(Intent intent) {

        String cid = intent.getStringExtra("cid");
        if(iceCandidate != null && cid.equals(clientid))
            MqttAsks.sendIceCandidate(mChatActivity,AccessMasterApplication.mApp.aPublic,clientid,iceCandidate);
    }

    private View.OnClickListener contactListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(stat == 0)
            {
                MqttAsks.connectAccept(mChatActivity,AccessMasterApplication.mApp.aPublic,mChatActivity.getIntent().getStringExtra("cid"));
            }
            else
            {
                stat = 0;
                mChatActivity.btnConnect.setImageResource(R.drawable.connect);
                audioTrack.setEnabled(false);
                if(showAudioTrack != null)
                    showAudioTrack.setEnabled(false);
                MqttAsks.connectRefous(mChatActivity,AccessMasterApplication.mApp.aPublic,mChatActivity.getIntent().getStringExtra("cid"));
            }
        }
    };

    public void accept(Intent intent) {
        String mid = intent.getStringExtra("mid");
        String mname = intent.getStringExtra("mname");
        AccessMasterApplication.mApp.audioManager.stopCycleSpeech();
        if(mid.equals(AccessMasterApplication.mApp.clidenid))
        {
            stat = 1;
            mChatActivity.btnConnect.setImageResource(R.drawable.disconnect);
            audioTrack.setEnabled(true);

            Message message = new Message();
            message.what = ChatHandler.SET_VOLUE;
            if(chatHandler != null)
                chatHandler.sendMessageDelayed(message,0);
        }
        else
        {
            if(stat == 0)
            {
                if(mname.length() == 0)
                {
                    AppUtils.showMessage(mChatActivity,mChatActivity.getString(R.string.othre_connect2)
                            +mname+mChatActivity.getString(R.string.othre_connect3));
                }
                else
                {
                    AppUtils.showMessage(mChatActivity,mChatActivity.getString(R.string.othre_connect));
                }
                MqttAsks.doExist(mChatActivity,AccessMasterApplication.mApp.aPublic
                        ,mChatActivity.getIntent().getStringExtra("cid"),AccessMasterApplication.mApp.clidenid,false);
                mChatActivity.finish();
            }
        }
    }



    public void checckDisconnect()
    {
        if(stat == 0 && time > AccessMasterApplication.mApp.connectdely)
        {
            AccessMasterApplication.mApp.audioManager.stopCycleSpeech();
            MqttAsks.doExist(mChatActivity,AccessMasterApplication.mApp.aPublic
                    ,mChatActivity.getIntent().getStringExtra("cid"),AccessMasterApplication.mApp.clidenid,false);
            mChatActivity.finish();
        }
        else
        {
            time++;
        }
        if(chatHandler != null)
        {
            chatHandler.removeMessages(ChatHandler.DISCONNECT);
            chatHandler.sendEmptyMessageDelayed(ChatHandler.DISCONNECT,1000);
        }
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
        AccessMasterApplication.mApp.stopFirst = false;
        AccessMasterApplication.mApp.resetFirst();
        AccessMasterApplication.mApp.connectCid = "";
        peerConnection.close();
        if(chatHandler != null)
        {
            chatHandler.removeMessages(ChatHandler.DISCONNECT);
            chatHandler = null;
        }

        Message message = new Message();
        message.obj = true;
        message.what = AppHandler.SET_CHAT_RES;
        if(AccessMasterApplication.mApp.appHandler != null)
        AccessMasterApplication.mApp.appHandler.sendMessage(message);
        Message message1 = new Message();
        message1.obj = false;
        message1.what = AppHandler.SET_CHAT_RES;
        if(AccessMasterApplication.mApp.appHandler != null)
        AccessMasterApplication.mApp.appHandler.sendMessageDelayed(message1,2500);

    }



    private View.OnClickListener outListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AccessMasterApplication.mApp.audioManager.stopCycleSpeech();
            MqttAsks.doExist(mChatActivity,AccessMasterApplication.mApp.aPublic
                    ,mChatActivity.getIntent().getStringExtra("cid"),AccessMasterApplication.mApp.clidenid,false);
            mChatActivity.finish();

        }
    };

    private View.OnClickListener openListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MqttAsks.openDoor(mChatActivity,AccessMasterApplication.mApp.aPublic,mChatActivity.getIntent().getStringExtra("cid"));
        }
    };



    public void stopConnect(Intent intent)
    {
        if(intent.getStringExtra("cid").equals(clientid))
        {
            AccessMasterApplication.mApp.audioManager.stopCycleSpeech();
            mChatActivity.finish();
        }

    }

    public void setAudio(){
        if(showAudioTrack != null)
            showAudioTrack.setEnabled(true);
    }

    public void callMaster(Intent intent) {
        AccessMasterApplication.mApp.audioManager.cycleSpeech(mChatActivity,intent.getStringExtra("cname")+"呼叫",DELY);
    }
}
