package intersky.mail.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;

import java.lang.ref.WeakReference;

import intersky.apputils.AppUtils;
import intersky.mail.AttachmentUploadThread;
import intersky.mail.MailManager;
import intersky.mail.R;
import intersky.mail.asks.MailAsks;
import intersky.mail.prase.MailPrase;
import intersky.mail.view.activity.MailEditActivity;
import intersky.xpxnet.net.NetObject;

//02
public class MailEditHandler extends Handler {

    public final static int ATTACHMENT_DOWNLOAD_FINISH = 3200200;
    public final static int ATTACHMENT_DOWNLOAD_UPDATA = 3200201;
    public final static int ATTACHMENT_DOWNLOAD_FAIL = 3200202;
    public final static int ADD_MAIL_CONTACT = 3200203;
    public final static int ADD_MAIL_ATTACHMENT = 3200204;
    public final static int SET_LABLE= 3200205;
    public MailEditActivity theActivity;

    public MailEditHandler(MailEditActivity mMailEditActivity) {
        theActivity = mMailEditActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {
            case MailAsks.MAIL_NEW_MAIL_SUCCESS:
                theActivity.waitDialog.hide();
                if(MailManager.getInstance().account.iscloud == false)
                {
                    if (theActivity.mAction == MailManager.ACTION_NEW)
                        theActivity.mMailEditPresenter.praseMail();
                    else
                        theActivity.mMailEditPresenter.praseMail((NetObject) msg.obj);
                }
                else
                {
                    theActivity.mMailEditPresenter.praseMail();
                }
                break;
            case ATTACHMENT_DOWNLOAD_FINISH:
                theActivity.count++;
                break;
            case ATTACHMENT_DOWNLOAD_FAIL:
                break;
            case ATTACHMENT_DOWNLOAD_UPDATA:
                theActivity.mMailEditPresenter.updateAttachment((Intent) msg.obj);
                break;
            case ADD_MAIL_CONTACT:
                theActivity.mMailEditPresenter.addContact(msg.arg1);
                break;
            case ADD_MAIL_ATTACHMENT:
                theActivity.mMailEditPresenter.addAttchment((Intent) msg.obj);
                break;
            case AttachmentUploadThread.EVENT_UPLOAD_FUJIAN_NEXT:
                theActivity.mMailEditPresenter.praseUploadNext((NetObject) msg.obj);
                break;
            case MailAsks.MAIL_SEND_SUCCESS:
                if(MailPrase.praseMailSend((NetObject) msg.obj,theActivity))
                {
                    NetObject netObject = (NetObject) msg.obj;
                    boolean issend = (boolean) netObject.item;
                    if(issend)
                    {
                        AppUtils.showMessage(theActivity,theActivity.getString(R.string.mail_send_success));
                    }
                    else
                    {
                        AppUtils.showMessage(theActivity,theActivity.getString(R.string.mail_save_success));
                    }
                    theActivity.finish();
                }
                break;
            case MailEditActivity.EVENT_SHOW_BUTTOM_LAYER:
                theActivity.isFujian = true;
                RelativeLayout.LayoutParams mFajianLayerParams = (RelativeLayout.LayoutParams) theActivity.mfujianLayer.getLayoutParams();
                mFajianLayerParams.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                mFajianLayerParams.addRule(RelativeLayout.ABOVE, R.id.fujian_button_layer);
                theActivity.mfujianLayer.setLayoutParams(mFajianLayerParams);
//				theActivity.mfujianLayer.setVisibility(View.INVISIBLE);
                theActivity.mButtomLayer.setVisibility(View.VISIBLE);
                break;
            case SET_LABLE:
                theActivity.mMailEditPresenter.setLable((Intent) msg.obj);
                break;
        }

    }
}
