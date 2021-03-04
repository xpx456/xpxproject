package intersky.mail.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import intersky.apputils.AppUtils;
import intersky.mail.asks.MailAsks;
import intersky.mail.entity.MailFile;
import intersky.mail.prase.MailPrase;
import intersky.mail.view.activity.MailActivity;
import intersky.mail.view.activity.MailFileActivity;
import intersky.select.entity.Select;
import intersky.xpxnet.net.NetObject;

//04
public class MailFileHandler extends Handler {

    public static final int UPDATA_ALL_LABLE = 3200404;
    public MailFileActivity theActivity;
    public MailFileHandler(MailFileActivity mMailActivity) {
        theActivity = mMailActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {
            case MailAsks.MAIL_FILE_SET_SUCCESS:
                theActivity.waitDialog.hide();
                MailPrase.praseFileUpdata((NetObject) msg.obj);
                NetObject  netObject2 = (NetObject) msg.obj;
                MailFile select2 = (MailFile) netObject2.item;
                theActivity.mMailFilePresenter.updataFile(select2);
                break;
            case MailAsks.MAIL_SET_MOVE_SUCCESS:
                theActivity.waitDialog.hide();
                if(MailPrase.praseMove((NetObject) msg.obj))
                {
                    Intent intent1 = new Intent(MailActivity.ACTION_UPDATA_ALL_MAILS);
                    intent1.putParcelableArrayListExtra("mails",theActivity.mMailFilePresenter.mails);
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