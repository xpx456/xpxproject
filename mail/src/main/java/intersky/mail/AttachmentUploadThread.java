package intersky.mail;

import android.os.Handler;
import android.os.Message;

import org.json.JSONException;
import org.json.JSONObject;

import intersky.appbase.entity.Attachment;
import intersky.mail.asks.MailAsks;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.ResposeResult;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AttachmentUploadThread extends Thread{

    public static final int EVENT_UPLOAD_FUJIAN_NEXT = 1000;

    public Attachment attachment;
    public MultipartBody.Builder builder;
    public Handler mHandler;

    public AttachmentUploadThread(Attachment attachment, MultipartBody.Builder builder, Handler handler) {
        this.attachment = attachment;
        this.builder = builder;
        this.mHandler = handler;
    }

    @Override
    public void run() {
        String urlString = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,MailAsks.ATTACHMENT_PATH_CLOUD);
        RequestBody formBody = builder.setType(MultipartBody.FORM).build();
        ResposeResult resposeResult = NetUtils.getInstance().post(urlString, formBody);
        NetObject netObject = new NetObject();
        if(resposeResult != null)
        {
            String result = resposeResult.result;
            netObject.result = result;
            netObject.item = attachment;
            if (result == null) {
                if (mHandler != null) {
                    Message msg2 = new Message();
                    msg2.what = EVENT_UPLOAD_FUJIAN_NEXT;
                    msg2.obj = netObject;
                    mHandler.sendMessage(msg2);
                }

            }
            else {
                try {
                    JSONObject mobj = new JSONObject(result);
                    if (mHandler != null) {
                        Message msg = new Message();
                        msg.what = EVENT_UPLOAD_FUJIAN_NEXT;
                        msg.obj = netObject;
                        mHandler.sendMessage(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        else
        {
            netObject.result = null;
            netObject.item = attachment;
            if (mHandler != null) {
                Message msg2 = new Message();
                msg2.what = EVENT_UPLOAD_FUJIAN_NEXT;
                msg2.obj = netObject;
                mHandler.sendMessage(msg2);
            }
        }


    }

}
