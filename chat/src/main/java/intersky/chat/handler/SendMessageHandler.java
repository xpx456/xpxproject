package intersky.chat.handler;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.util.HashMap;

import intersky.appbase.Downloadobject;
import intersky.appbase.entity.Conversation;
import intersky.apputils.AppUtils;
import intersky.chat.ChatUtils;
import intersky.xpxnet.net.NetObject;

//01
public class SendMessageHandler extends Handler {

    public static final int SEND_UPDATA_SIZE_SUCCESS = 3050103;
    public static final int SEND_UPLOADFILE_SUCCESS = 3050102;
    public static final int SEND_MESSAGE_SUCCESS = 3050101;
    public Context context;
    public HashMap<String, Downloadobject> mDownloadThreads = new HashMap<String,Downloadobject>();
    public SendMessageHandler(Context context)
    {
        this.context = context;
    }
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SEND_UPLOADFILE_SUCCESS:
                if(ChatUtils.getChatUtils().mChatFunctions != null) {
                    if(ChatUtils.getChatUtils().mChatFunctions.praseFile((NetObject) msg.obj))
                    {
                        ChatUtils.getChatUtils().mChatFunctions.sendMessage(context,(NetObject) msg.obj);
                    }
                }
                break;
            case SEND_MESSAGE_SUCCESS:
                if(praseData(context, (NetObject) msg.obj))
                {
                    NetObject netObject = (NetObject) msg.obj;
                    Conversation conversation = (Conversation) netObject.item;
                    ChatUtils.getChatUtils().mChatFunctions.sendMessageSuccess(context,conversation);
                }
                else
                {
                    NetObject netObject = (NetObject) msg.obj;
                    Conversation conversation = (Conversation) netObject.item;
                    ChatUtils.getChatUtils().mChatFunctions.sendMessageFail(context,conversation);
                }
                break;
            case SEND_UPDATA_SIZE_SUCCESS:
                ChatUtils.getChatUtils().mChatFunctions.praseFilesize(context, (NetObject) msg.obj);
                break;

        }

    }

    public static boolean praseData(Context context, NetObject net) {
        String json = net.result;
        if(AppUtils.success(json) == false)
        {
//            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return false;
        }
        else
        {
//            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
        }
        return true;
    }

    public static boolean size(Context context, NetObject net) {
        String json = net.result;
        if(AppUtils.success(json) == false)
        {
//            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return false;
        }
        else
        {
//            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
        }
        return true;
    }
}
