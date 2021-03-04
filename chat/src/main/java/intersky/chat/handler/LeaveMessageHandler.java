package intersky.chat.handler;


import android.os.Handler;
import android.os.Message;

import intersky.appbase.BaseActivity;
import intersky.appbase.entity.Attachment;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Conversation;
import intersky.apputils.AppUtils;
import intersky.chat.ChatUtils;
import intersky.chat.database.DBHelper;
import intersky.xpxnet.net.NetObject;

//00
public class LeaveMessageHandler extends Handler {

    public static final int GET_LEAVE_MSG_SUCCESS = 3050000;
    public static final int GET_LEAVE_MSG_START_SUCCESS = 3050001;
    public static final int ADD_CHAT_SOURCE = 3050002;
    public static final int DELETE_CHAT_SOURCE_DETIAL = 3050003;
    public static final int DELETE_CHAT_SOURCE_ALL = 3050004;
    public static final int CHEACK_HTED_RESULT = 3050005;
    public Handler handler;

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case GET_LEAVE_MSG_SUCCESS:
                if(ChatUtils.getChatUtils().mChatFunctions != null)
                ChatUtils.getChatUtils().mChatFunctions.praseLeaveMessage((NetObject) msg.obj);
                if(ChatUtils.getChatUtils().mSampleChatFunctions != null)
                    ChatUtils.getChatUtils().mSampleChatFunctions.praseLeaveMessage((NetObject) msg.obj);
                break;
            case GET_LEAVE_MSG_START_SUCCESS:
                NetObject netObject = (NetObject) msg.obj;
                Contacts contacts = (Contacts) netObject.item;
                ChatUtils.getChatUtils().mChatFunctions.praseLeaveMessage((NetObject) msg.obj);
                ChatUtils.getChatUtils().mChatFunctions.startChart(contacts);
                break;
            case ChatUtils.EVENT_GET_SUCCESS:
                ChatUtils.getChatUtils().finishSource((Conversation) msg.obj);
                break;
            case ChatUtils.EVENT_GET_FAIL:
                ChatUtils.getChatUtils().failSource((Conversation) msg.obj);
                break;
            case ADD_CHAT_SOURCE:
                ChatUtils.getChatUtils().addChatSource((Conversation) msg.obj);
                break;
            case DELETE_CHAT_SOURCE_DETIAL:
                ChatUtils.getChatUtils().deleteSourceDetial((String) msg.obj);
                break;
            case DELETE_CHAT_SOURCE_ALL:
                ChatUtils.getChatUtils().deleteSourceALl();
                break;
            case CHEACK_HTED_RESULT:
                String logo = "";
                NetObject net = (NetObject) msg.obj;
                Attachment attachment = (Attachment) net.item;
                if(ChatUtils.getChatUtils().mChatFunctions != null)
                    ChatUtils.getChatUtils().mChatFunctions.checkHeadResult((NetObject) msg.obj);
                if(ChatUtils.getChatUtils().mSampleChatFunctions != null)
                    ChatUtils.getChatUtils().mSampleChatFunctions.checkHeadResult((NetObject) msg.obj);
                ChatUtils.getChatUtils().addHead(attachment);
                break;
            case ChatUtils.EVENT_HEAD_GET_SUCCESS:
                ChatUtils.getChatUtils().updataHeadFinish((Attachment) msg.obj);
                break;
            case ChatUtils.EVENT_HEAD_GET_FAIL:
                ChatUtils.getChatUtils().updataHeadFail((Attachment) msg.obj);
                break;
            case ChatUtils.EVENT_UPDATA_HEAD_LIST:
                ChatUtils.getChatUtils().upDataHeadList();
                break;
        }

    }
}
