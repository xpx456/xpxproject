package com.accesscontrol.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.accesscontrol.R;
import com.accesscontrol.view.AccessControlApplication;
import com.accesscontrol.view.activity.ChatActivity;
import com.accesscontrol.view.activity.MainActivity;

import xpx.video.MyRoomView;


//01


public class ChatHandler extends Handler {


    public ChatActivity theActivity;
    public static final int CHAT_ACCEPT = 110000;
    public static final int CHAT_REFOUS = 110001;
    public static final int CHAT_EXIST = 110102;
    public static final int ADD_MASTER = 110003;
    public static final int DISCONNECT = 110004;
    public static final int CONNECT_FAIL = 110005;
    public static final int FINISH = 110006;
    public static final int SEND_OFFER = 110007;
    public static final int TIME_OUT = 110008;
    public static final int BUESY = 110009;
    public static final int SET_VOLUE = 110012;

    public static final int CONNECT_CHECK_OPVIEW = 110100;


    public static final int RECEIVER_OFFER = 111000;
    public static final int RECEIVER_ICE = 111002;
    public static final int SEND_ICE_AGAIN = 111003;
    public static final int CHECK_ICE_RECEIVER = 111004;
    public static final int START_CONTACE = 111005;

    public ChatHandler(ChatActivity chatActivity) {
        theActivity = chatActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {


            case CHAT_EXIST:
                theActivity.chatPresenter.doExist((Intent) msg.obj);
                break;
            case FINISH:
                theActivity.waitDialog.hide();
                theActivity.finish();
                break;
            case TIME_OUT:
                theActivity.finish();
                break;


            case CHAT_REFOUS:
                theActivity.chatPresenter.refous();
                break;
            case BUESY:
                theActivity.chatPresenter.mastBusy((Intent) msg.obj);
                break;
            case SET_VOLUE:
                MyRoomView myRoomView = (MyRoomView) msg.obj;
                if(myRoomView.audioTrack == null)
                {
                    Message message = new Message();
                    message.what = ChatHandler.SET_VOLUE;
                    message.obj = myRoomView;
                    if(theActivity.chatPresenter.chatHandler != null)
                        theActivity.chatPresenter.chatHandler.sendMessageDelayed(message,1000);
                }
                else
                    theActivity.chatPresenter.setAudio((MyRoomView) msg.obj);
                break;
            case CHAT_ACCEPT:
                theActivity.chatPresenter.accept((Intent) msg.obj);
                break;
            case DISCONNECT:
                theActivity.chatPresenter.startTimeOut();
                break;
            case RECEIVER_OFFER:
                theActivity.chatPresenter.onOfferReceived((Intent) msg.obj);
                break;
            case RECEIVER_ICE:
                theActivity.chatPresenter.onIceCandidateReceived((Intent) msg.obj);
                break;
            case SEND_ICE_AGAIN:
                theActivity.chatPresenter.sendIceAgain((Intent) msg.obj);
                break;
            case CHECK_ICE_RECEIVER:
                theActivity.chatPresenter.checkIceReceived((MyRoomView) msg.obj);
                break;
            case START_CONTACE:
                theActivity.chatPresenter.startContact((Intent) msg.obj);
                break;
        }

    }
}
