package com.accessmaster.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.accessmaster.view.activity.ChatActivity;

import xpx.video.MyRoomView;


//01


public class ChatHandler extends Handler {


    public ChatActivity theActivity;
    public static final int CHAT_ACCEPT = 110000;
    public static final int STOP_CONNECT = 110001;
    public static final int DISCONNECT = 110002;
    public static final int FINISH = 110006;
    public static final int GET_AUDIO_PERMISSTION = 110007;
    public static final int SET_VOLUE = 110012;
    public static final int CALL_MASTER = 110013;
    public static final int ACCEPT_ICE = 110014;

    public static final int ANSWER_RECEIVER = 111001;
    public static final int ICE_RECEIVER = 111002;
    public static final int CHECK_ANSWER_RECEIVER = 111004;
    public static final int SEND_ICE_AGAIN = 111005;
    public static final int CHECK_ICE_RECEIVER = 111003;
    public static final int RECEIVER_CALL = 111006;

    public ChatHandler(ChatActivity chatActivity) {
        theActivity = chatActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {


            case STOP_CONNECT:
                theActivity.chatPresenter.stopConnect((Intent) msg.obj);
                break;
            case DISCONNECT:
                theActivity.chatPresenter.checckDisconnect();
                break;
            case FINISH:
                theActivity.waitDialog.hide();
                theActivity.finish();
                break;
            case GET_AUDIO_PERMISSTION:
                break;
            case SET_VOLUE:
                if(theActivity.chatPresenter.showAudioTrack == null)
                {
                    Message message = new Message();
                    message.what = ChatHandler.SET_VOLUE;
                    if(theActivity.chatPresenter.chatHandler != null)
                    theActivity.chatPresenter.chatHandler.sendMessageDelayed(message,1000);
                }
                else
                theActivity.chatPresenter.setAudio();
                break;
            case CALL_MASTER:
                theActivity.chatPresenter.callMaster((Intent) msg.obj);
                break;


            case CHAT_ACCEPT:
                theActivity.chatPresenter.accept((Intent) msg.obj);
                break;
            case ANSWER_RECEIVER:
                theActivity.chatPresenter.onAnswerReceived((Intent) msg.obj);
                break;
            case ICE_RECEIVER:
                theActivity.chatPresenter.onIceCandidateReceived((Intent) msg.obj);
                break;
            case CHECK_ANSWER_RECEIVER:
                theActivity.chatPresenter.oncheckAnswerReceiver();
                break;
            case SEND_ICE_AGAIN:
                theActivity.chatPresenter.sendIceAgain((Intent) msg.obj);
                break;
            case CHECK_ICE_RECEIVER:
                theActivity.chatPresenter.oncheckIceReceiver();
                break;
            case RECEIVER_CALL:
                theActivity.chatPresenter.receiverCall((Intent) msg.obj);
                break;
        }

    }
}
