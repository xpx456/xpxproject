package intersky.conversation.handler;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.util.HashMap;

import intersky.appbase.Downloadobject;
import intersky.appbase.entity.Conversation;
import intersky.apputils.AppUtils;
import intersky.conversation.R;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

//03
public class SendMessageHandler extends Handler {

    public static final int SEND_UPLOADFILE_SUCCESS = 3060302;
    public static final int SEND_MESSAGE_SUCCESS = 3060301;
    public static final int SEND_UPDATA_SIZE_SUCCESS = 3060303;
    public static final String ACTION_SEND_MESSAGE_SUCCESS = "ACTION_SEND_MESSAGE_SUCCESS";
    public static final String ACTION_SEND_MESSAGE_FAIL = "ACTION_SEND_MESSAGE_FAIL";
    public Context context;
    public UploadFile uploadFile;
    public HashMap<String, Downloadobject> mDownloadThreads = new HashMap<String,Downloadobject>();
    public SendMessageHandler(Context context)
    {
        this.context = context;
    }
    @Override
    public void handleMessage(Message msg) {
        Conversation conversation;
        switch (msg.what) {
            case SEND_UPLOADFILE_SUCCESS:
                if(uploadFile != null) {
                    if(uploadFile.praseFile((NetObject) msg.obj))
                    {
                        uploadFile.sendmessage((NetObject) msg.obj);
                    }
                }
                break;
            case SEND_MESSAGE_SUCCESS:
                if(praseData(context, (NetObject) msg.obj))
                {
                    NetObject netObject = (NetObject) msg.obj;
                    Conversation conversation1 = (Conversation) netObject.item;
                    Intent intent = new Intent(ACTION_SEND_MESSAGE_SUCCESS);
                    intent.putExtra("msg",conversation1);
                    context.sendBroadcast(intent);
                }
                else
                {
                    NetObject netObject = (NetObject) msg.obj;
                    Conversation conversation1 = (Conversation) netObject.item;
                    Intent intent = new Intent(ACTION_SEND_MESSAGE_FAIL);
                    intent.putExtra("msg",conversation1);
                    context.sendBroadcast(intent);
                }
                break;
            case NetUtils.NO_INTERFACE:
                AppUtils.showMessage(context,context.getString(R.string.error_net_network));
                NetObject netObject = (NetObject) msg.obj;
                Conversation conversation1 = (Conversation) netObject.item;
                Intent intent = new Intent(ACTION_SEND_MESSAGE_FAIL);
                intent.putExtra("msg",conversation1);
                context.sendBroadcast(intent);
                break;
            case NetUtils.NO_NET_WORK:
                AppUtils.showMessage(context,context.getString(R.string.no_net_network));
                NetObject netObject1 = (NetObject) msg.obj;
                Conversation conversation11 = (Conversation) netObject1.item;
                Intent intent1 = new Intent(ACTION_SEND_MESSAGE_FAIL);
                intent1.putExtra("msg",conversation11);
                context.sendBroadcast(intent1);
                break;
            case SEND_UPDATA_SIZE_SUCCESS:
                uploadFile.praseFilesize(context, (NetObject) msg.obj);
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

    public interface UploadFile
    {
        boolean praseFile(NetObject netObject);
        void sendmessage(NetObject netObject);
        void praseFilesize(Context context, NetObject netObject);
    }


}
