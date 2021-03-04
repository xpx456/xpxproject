package intersky.chat.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;


import java.lang.ref.WeakReference;

import intersky.appbase.PermissionCode;
import intersky.appbase.entity.Conversation;
import intersky.apputils.AppUtils;
import intersky.chat.ChatUtils;
import intersky.chat.R;
import intersky.chat.view.activity.ChatActivity;
import intersky.mywidget.CustomScrollView;
import intersky.xpxnet.net.NetUtils;

public class ChatHandler extends Handler {


    public final static int DOWNLOAD_FIAL = 1230600;
    public final static int DOWNLOAD_UPDATA = 1230601;
    public final static int DOWNLOAD_FINISH = 1230602;
    public final static int ADD_MESSAGE = 1230603;
    public final static int UPDATA_MESSAGE = 1230610;
    public final static int OPEN_ITEM = 1230605;
    public final static int SET_PIC = 1230604;
    public final static int DELETE_MSG = 1230606;
    public final static int CODE_RESULT = 1230607;
    public final static int SCOLL_END = 1230608;
    public final static int SCOLL_END_FINISH = 1230609;
    public final static int SEND_LOCATION = 1230611;
    public final static int DOWNLOAD_IMGFINISH = 1230614;

    public ChatActivity theActivity;

    public ChatHandler(ChatActivity mChatActivity) {
        theActivity = mChatActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {
            case ADD_MESSAGE:
                intent = (Intent) msg.obj;
                if(theActivity.mChatPresenter.checkAdd(intent.<Conversation>getParcelableArrayListExtra("msgs")) == true)
                {
                    theActivity.chatPager.addConversation(intent.getIntExtra("addcount",0));
                    theActivity.mChatPresenter.updataViews(intent.<Conversation>getParcelableArrayListExtra("msgs"));
                    ChatUtils.getChatUtils().mChatFunctions.readMessages(theActivity.mContacts.mRecordid);
                }
                break;
            case UPDATA_MESSAGE:
                intent = (Intent) msg.obj;
                theActivity.mChatPresenter.updataView((Conversation) intent.getParcelableExtra("msg"));
                break;
            case SET_PIC:
                theActivity.mChatPresenter.setPicResult((Intent) msg.obj);
                break;
            case PermissionCode.PERMISSION_REQUEST_AUDIORECORD:
                theActivity.impuArer.canadudio = true;
                break;
            case NetUtils.NO_INTERFACE:
                theActivity.waitDialog.hide();
                theActivity.impuArer.btnSend.setEnabled(true);
                AppUtils.showMessage(theActivity,theActivity.getString(R.string.error_net_network));
                break;
            case NetUtils.NO_NET_WORK:
                theActivity.waitDialog.hide();
                theActivity.impuArer.btnSend.setEnabled(true);
                AppUtils.showMessage(theActivity,theActivity.getString(R.string.im_send_file));
                break;
            case OPEN_ITEM:
                theActivity.mChatPresenter.openItem((Intent) msg.obj);
                break;
            case SCOLL_END:
                //theActivity.scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                theActivity.scrollView.scrollTo(0,theActivity.chatView.getHeight());
                break;
            case CustomScrollView.CODE_TO_END:
                theActivity.mChatPresenter.onHead();
                break;
            case SCOLL_END_FINISH:
//                theActivity.waitDialog.hide();
                int height3= theActivity.chatView.getHeight();
                int height4 = 0;
                if(msg.obj != null)
                {
                    View view = (View) msg.obj;
                    height4 = view.getHeight();
                }
                theActivity.scrollView.scrollTo(0,height3-msg.arg1+height4);
                break;
            case CODE_RESULT:
                theActivity.mChatPresenter.showAgain((String) msg.obj);
                break;
            case DELETE_MSG:
                theActivity.waitDialog.hide();
                theActivity.mChatPresenter.deleteView((Intent) msg.obj);
                break;
            case SEND_LOCATION:
                theActivity.mChatPresenter.sendLocation((Intent) msg.obj);
                break;
        }

    }
}
