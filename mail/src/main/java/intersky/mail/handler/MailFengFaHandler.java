package intersky.mail.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import intersky.mail.MailManager;
import intersky.mail.asks.MailAsks;
import intersky.mail.prase.MailPrase;
import intersky.mail.view.activity.MailFengFaActivity;
import intersky.mail.view.activity.MailListActivity;
import intersky.apputils.AppUtils;
import intersky.xpxnet.net.NetObject;

//03
public class MailFengFaHandler extends Handler
{
    public static final int EVENT_SET_FENFA_PERSON = 3200300;

    public MailFengFaActivity theActivity;

    public MailFengFaHandler(MailFengFaActivity activity)
    {
        theActivity = activity;
    }

    @Override
    public void handleMessage(Message msg)
    {
        // AppUtils.dissMissDialog();
        if (theActivity != null)
        {
            switch (msg.what)
            {

                case MailAsks.MAIL_FENGFA_SUCCESS:
                    theActivity.waitDialog.hide();
                    if(MailPrase.praseFenfa((NetObject) msg.obj,theActivity))
                    {
                        AppUtils.sendSampleBroadCast(theActivity, MailManager.ACTION_MAIL_LIST_UPDATE);
                        Intent mIntent = new Intent(theActivity, MailListActivity.class);
                        mIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        theActivity.startActivity(mIntent);
                        theActivity.finish();
                    }
                    break;
                case MailAsks.MAIL_LABLE_SUCCESS:
                    theActivity.waitDialog.hide();
                    theActivity.waitDialog.setTitle("");
                    MailPrase.addLData((NetObject) msg.obj);
                    theActivity.mMailSelectAdapter.notifyDataSetChanged();
                    break;
                case EVENT_SET_FENFA_PERSON:
                    theActivity.mMailFenFaPresenter.upDatePerson((Intent) msg.obj);
                    break;
            }
            super.handleMessage(msg);
        }
    }
};
