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
import intersky.mail.entity.MailType;
import intersky.mail.prase.MailPrase;
import intersky.mail.presenter.MailPresenter;
import intersky.mail.view.activity.MailActivity;
import intersky.select.entity.Select;
import intersky.xpxnet.net.NetObject;

//04
public class MailHandler extends Handler {

    public static final int UPDATE_MAIL_HIT = 3200400;
    public static final int UPDATE_MAIL_BOX = 3200401;
    public static final int RECEIVE_MAIL_COUNT = 3200402;
    public static final int UPDATA_ALL_SEND = 3200403;
    public static final int UPDATA_ALL_LABLE = 3200404;
    public static final int UPDATA_ALL_FILE = 3200405;
    public static final int UPDATA_MAILS = 3200406;
    public static final int UPDATA_MAILS_ALL = 3200408;
    public static final int UPDATE_MAIL_OTHER_BOX = 3200407;
    public MailActivity theActivity;
    public MailHandler(MailActivity mMailActivity) {
        theActivity = mMailActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {
            case MailAsks.MAIL_SETUSER_SUCCESS:
                NetObject netObject1 = (NetObject) msg.obj;
                MailManager.getInstance().mSelectUser = (MailContact) netObject1.item;
                if(MailManager.getInstance().account.iscloud == false)
                {
                    if(MailManager.getInstance().mSelectUser != null)
                    {
                        if(MailManager.getInstance().mSelectUser.mRecordid.equals(MailManager.getInstance().account.mAccountId))
                        {
                            theActivity.mMailPresenter.title.setText(theActivity.getString(R.string.keyword_mymailbox));
                        }
                        else
                        {
                            theActivity.mMailPresenter.title.setText(MailManager.getInstance().mSelectUser.mName+
                                    theActivity.getString(R.string.keyword_mymailbox2));
                        }
                    }

                }
                MailManager.getInstance().getMailBox();
                MailManager.getInstance().getMailPush();
                MailManager.getInstance().getLables();
                MailManager.getInstance().getMailFiles();
                MailManager.getInstance().getMailType();
                break;
            case MailAsks.MAIL_SETUSER_FAIL:
                break;
            case MailAsks.MAIL_MAILBOX_FAIL:
                break;
            case UPDATE_MAIL_HIT:
                if(MailManager.getInstance().account.iscloud == false)
                theActivity.mMailPresenter.setnewcountView();
                else
                    theActivity.mMailPresenter.setnewcountViewCloud();
                break;
            case UPDATE_MAIL_BOX:
                if(MailManager.getInstance().account.iscloud == false)
                theActivity.mMailPresenter.checkMailBoxView();
                else
                    theActivity.mMailPresenter.checkMailBoxViewc(false);
                break;
            case UPDATE_MAIL_OTHER_BOX:
                if(MailManager.getInstance().account.iscloud == false)
                    theActivity.mMailPresenter.checkMailBoxView();
                else
                    theActivity.mMailPresenter.checkMailBoxViewc(true);
                break;
//            case MailAsks.MAIL_LIST_SUCCESS:
//                if(MailManager.getInstance().account.iscloud == false)
//                MailPrase.praseMail((NetObject) msg.obj,theActivity.mMailItems);
//                else
//                    MailPrase.praseMail((NetObject) msg.obj,theActivity.mMailItems,0);
//                break;
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
            case MailAsks.MAIL_DELETE_SUCCESS:
            case MailListHandler.EVENT_LIST_UPDATE:
            case UPDATA_MAILS_ALL:
                theActivity.mMailPresenter.onHead();
                break;
            case RECEIVE_MAIL_COUNT:
                theActivity.mMailPresenter.onReceiveUpdata();
                break;
            case UPDATA_ALL_SEND:
                theActivity.mMailPresenter.updataAllsendView();
                break;
            case UPDATA_ALL_LABLE:
                theActivity.mMailPresenter.updataAllLableView();
                break;
            case UPDATA_ALL_FILE:
                theActivity.mMailPresenter.updataAlFileView();
                break;
            case MailAsks.MAIL_LABLE_DELETE_SUCCESS:
                theActivity.waitDialog.hide();
                if(MailPrase.praseLableDel((NetObject) msg.obj))
                {
                    NetObject  netObject = (NetObject) msg.obj;
                    Select select = (Select) netObject.item;
                    theActivity.mMailPresenter.delLable(select);
                }
                break;
            case MailAsks.MAIL_LABLE_SET_SUCCESS:
                theActivity.waitDialog.hide();
                Select select = MailPrase.praseLableUpdata((NetObject) msg.obj);
                if(select != null)
                theActivity.mMailPresenter.updataLable(select);
                break;
            case MailAsks.MAIL_FILE_DELETE_SUCCESS:
                theActivity.waitDialog.hide();
                if(MailPrase.praseFileDel((NetObject) msg.obj))
                {
                    NetObject  netObject2 = (NetObject) msg.obj;
                    MailFile select2 = (MailFile) netObject2.item;
                    theActivity.mMailPresenter.delFile(select2);
                }
                break;
            case MailAsks.MAIL_FILE_SET_SUCCESS:
                theActivity.waitDialog.hide();
                MailFile select2 =MailPrase.praseFileUpdata((NetObject) msg.obj);
                if(select2 != null)
                theActivity.mMailPresenter.updataFile(select2);
                break;
            case UPDATA_MAILS:
                theActivity.mMailPresenter.updataMails((Intent) msg.obj);
                break;
            case MailAsks.MAIL_MANAGE_MAIL_SUCCESS:
                theActivity.waitDialog.hide();
                if(MailPrase.praseLableDel((NetObject) msg.obj))
                {
                    theActivity.mMailPresenter.onHead();
                }
                break;
            case MailAsks.MAIL_CUSTOMS_SUCCESS:
                theActivity.waitDialog.hide();
                NetObject netObject4 = (NetObject) msg.obj;
                MailPresenter.Mailboxobj mailboxobj = (MailPresenter.Mailboxobj) netObject4.item;
                MailType mailType = (MailType) mailboxobj.object;
                theActivity.mMailPresenter.cleanListView(mailType);
                if(MailPrase.praseCustoms((NetObject) msg.obj))
                {
                    theActivity.mMailPresenter.startMailList(mailboxobj);
                }
                theActivity.mMailPresenter.showMailTypeView(mailType);
                break;
            case MailAsks.MAIL_SET_READ_SUCCESS:
                theActivity.waitDialog.hide();
                if(MailPrase.praseRead((NetObject) msg.obj,true))
                {
                    theActivity.mMailPresenter.updataMails();
                }
                break;
            case MailAsks.MAIL_SET_UNREAD_SUCCESS:
                theActivity.waitDialog.hide();
                if(MailPrase.praseRead((NetObject) msg.obj,false))
                {
                    theActivity.mMailPresenter.updataMails();
                }
                break;
            case MailAsks.MAIL_SET_NOREPLY_SUCCESS:
                theActivity.waitDialog.hide();
                if(MailPrase.praseReply((NetObject) msg.obj,true))
                {
                    theActivity.mMailPresenter.updataMails();
                }
                break;
            case MailAsks.MAIL_SET_UNNOREPLY_SUCCESS:
                theActivity.waitDialog.hide();
                if(MailPrase.praseReply((NetObject) msg.obj,false))
                {
                    theActivity.mMailPresenter.updataMails();
                }
                break;
            case MailAsks.MAIL_SET_READALL_SUCCESS:
                theActivity.waitDialog.hide();
                if(MailPrase.praseReadAll((NetObject) msg.obj))
                {
                    theActivity.mMailPresenter.onHead();
                }
                break;
            case MailAsks.MAIL_SET_APPROVE_SUCCESS:
            case MailAsks.MAIL_SET_VOTE_SUCCESS:
                theActivity.waitDialog.hide();
                if(MailPrase.praseApprove((NetObject) msg.obj))
                {
                    theActivity.mMailPresenter.onHead();
                }
                break;
            case MailAsks.MAIL_GROP_LABLE_SUCCESS:
                theActivity.waitDialog.hide();
                MailPrase.addGLData((NetObject) msg.obj);
                break;
        }

    }
}