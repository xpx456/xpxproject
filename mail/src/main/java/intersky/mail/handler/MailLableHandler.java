package intersky.mail.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import intersky.apputils.AppUtils;
import intersky.mail.MailManager;
import intersky.mail.R;
import intersky.mail.asks.MailAsks;
import intersky.mail.entity.MailContact;
import intersky.mail.entity.MailFile;
import intersky.mail.prase.MailPrase;
import intersky.mail.view.activity.MailActivity;
import intersky.mail.view.activity.MailLableActivity;
import intersky.select.entity.Select;
import intersky.xpxnet.net.NetObject;

//04
public class MailLableHandler extends Handler {

    public static final int UPDATA_ALL_LABLE = 3200404;
    public MailLableActivity theActivity;
    public MailLableHandler(MailLableActivity mMailActivity) {
        theActivity = mMailActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {
            case MailAsks.MAIL_LABLE_SET_SUCCESS:
                theActivity.waitDialog.hide();
                Select select = MailPrase.praseLableUpdata((NetObject) msg.obj);
                if(select != null)
                theActivity.mMailLablePresenter.updataLable(select);
                break;
            case MailAsks.MAIL_LABLE_MAIL_SUCCESS:
                theActivity.waitDialog.hide();
                if(MailPrase.praseLableDel((NetObject) msg.obj))
                {
                    NetObject netObject = (NetObject) msg.obj;
                    for(int i = 0 ; i < theActivity.mMailLablePresenter.mails.size() ; i++) {
                        theActivity.mMailLablePresenter.mails.get(i).lables = (String) netObject.item;
                    }
                    theActivity.mMailLablePresenter.cleanSelect();
                    Intent intent1 = new Intent(MailActivity.ACTION_UPDATA_MAILS);
                    intent1.putParcelableArrayListExtra("mails",theActivity.mMailLablePresenter.mails);
                    theActivity.sendBroadcast(intent1);
                    theActivity.finish();
                }
                else
                {
                    AppUtils.showMessage(theActivity,"fail");
                }
                break;
        }

    }
}