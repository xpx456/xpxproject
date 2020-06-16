package intersky.mail.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import intersky.apputils.AppUtils;
import intersky.mail.MailManager;
import intersky.mail.asks.MailAsks;
import intersky.mail.prase.MailPrase;
import intersky.mail.view.activity.MailListActivity;
import intersky.xpxnet.net.NetObject;

//05
public class MailListHandler extends Handler {

    public static final int EVENT_LIST_UPDATE = 3200500;

    public MailListActivity theActivity;

    public MailListHandler(MailListActivity mMailListActivity) {
        theActivity = mMailListActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {
            case MailAsks.MAIL_LIST_SUCCESS:
                int count = 0;
                if(MailManager.getInstance().account.iscloud == false) {
                    count = MailPrase.praseMail((NetObject) msg.obj,theActivity.mails);
                    if(count == 0) {
                        theActivity.isAll = true;
                    }
                    else {
                        theActivity.startPos += count;
                    }
                }
                else {
                    if(theActivity.type2 != -1)
                    {
                        count = MailPrase.praseMail((NetObject) msg.obj,theActivity.mails,0);
                        if(count == -1)
                        {
                            if(theActivity.mails.size()/40 > 0)
                            {
                                theActivity.startPos = theActivity.mails.size()/40+1;
                            }
                            else
                            {
                                theActivity.startPos = theActivity.mails.size()/40;
                            }
                            theActivity.isAll = true;
                        }
                        else if(count == 0)
                        {
                            AppUtils.showMessage(theActivity,"");
                        }
                        else
                        {
                            theActivity.startPos = count;
                        }
                        theActivity.mMailItemAdapter.notifyDataSetChanged();
                    }
                    else
                    {
                        NetObject netObject = (NetObject) msg.obj;
                        int currentid = (int) netObject.item;
                        switch (currentid) {
                            case 0:
                                count = MailPrase.praseMail((NetObject) msg.obj,theActivity.mMailItems1,theActivity.mStartpos1);
                                if(count == -1)
                                {
                                    if(theActivity.mMailItems1.size()/40 > 0)
                                    {
                                        theActivity.mStartpos1 = theActivity.mMailItems1.size()/40+1;
                                    }
                                    else
                                    {
                                        theActivity.mStartpos1 = theActivity.mMailItems1.size()/40;
                                    }
                                }
                                else if(count == 0)
                                {
                                    AppUtils.showMessage(theActivity,"");
                                }
                                else
                                {
                                    theActivity.mStartpos1 = count;
                                }
                                theActivity.mMailItemAdapter1.notifyDataSetChanged();
                                break;
                            case 1:
                                count = MailPrase.praseMail((NetObject) msg.obj,theActivity.mMailItems2,theActivity.mStartpos2);
                                if(count == -1)
                                {
                                    if(theActivity.mMailItems2.size()/40 > 0)
                                    {
                                        theActivity.mStartpos2 = theActivity.mMailItems2.size()/40+1;
                                    }
                                    else
                                    {
                                        theActivity.mStartpos2 = theActivity.mMailItems2.size()/40;
                                    }
                                    theActivity.isall2 = true;
                                }
                                else if(count == 0)
                                {
                                    AppUtils.showMessage(theActivity,"");
                                }
                                else
                                {
                                    theActivity.mStartpos2 = count;
                                }
                                theActivity.mMailItemAdapter2.notifyDataSetChanged();
                                break;
                            case 2:
                                count = MailPrase.praseMail((NetObject) msg.obj,theActivity.mMailItems3,theActivity.mStartpos3);
                                if(count == -1)
                                {
                                    if(theActivity.mMailItems3.size()/40 > 0)
                                    {
                                        theActivity.mStartpos3 = theActivity.mMailItems3.size()/40+1;
                                    }
                                    else
                                    {
                                        theActivity.mStartpos3 = theActivity.mMailItems3.size()/40;
                                    }
                                    theActivity.isall3 = true;
                                }
                                else if(count == 0)
                                {
                                    AppUtils.showMessage(theActivity,"");
                                }
                                else
                                {
                                    theActivity.mStartpos3 = count;
                                }
                                theActivity.mMailItemAdapter3.notifyDataSetChanged();
                                break;
                            case 3:
                                count = MailPrase.praseMail((NetObject) msg.obj,theActivity.mMailItems4,theActivity.mStartpos4);
                                if(count == -1)
                                {
                                    if(theActivity.mMailItems4.size()/40 > 0)
                                    {
                                        theActivity.mStartpos4 = theActivity.mMailItems4.size()/40+1;
                                    }
                                    else
                                    {
                                        theActivity.mStartpos4 = theActivity.mMailItems4.size()/40;
                                    }
                                    theActivity.isall4 = true;
                                }
                                else if(count == 0)
                                {
                                    AppUtils.showMessage(theActivity,"");
                                }
                                else
                                {
                                    theActivity.mStartpos4 = count;
                                }
                                theActivity.mMailItemAdapter4.notifyDataSetChanged();
                                break;

                        }
                    }
                }
                theActivity.waitDialog.hide();
                break;
            case MailAsks.MAIL_DELETE_SUCCESS_FINAL:
                theActivity.mMailListPresenter.onHead();
                break;
            case EVENT_LIST_UPDATE:
                theActivity.mMailListPresenter.onHead();
                break;
        }

    }
}
