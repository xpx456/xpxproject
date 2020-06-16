package intersky.mail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.Html;

import org.json.JSONException;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

import intersky.appbase.BaseActivity;
import intersky.appbase.entity.Account;
import intersky.appbase.entity.Attachment;
import intersky.appbase.entity.Commend;
import intersky.appbase.entity.Register;
import intersky.appbase.utils.XpxShare;
import intersky.apputils.AppUtils;
import intersky.json.XpxJSONArray;
import intersky.json.XpxJSONObject;
import intersky.mail.asks.MailAsks;
import intersky.mail.entity.Mail;
import intersky.mail.entity.MailBox;
import intersky.mail.entity.MailContact;
import intersky.mail.entity.SortMailContactComparator;
import intersky.mail.handler.InitHandler;
import intersky.mail.view.activity.MailActivity;
import intersky.mail.view.activity.MailEditActivity;
import intersky.select.entity.Select;
import intersky.xpxnet.net.Service;

public class MailManager {

    public static final String ACTION_GET_MAIL_BOX_FINISH = "ACTION_GET_MAIL_BOX_FINISH";
    public static final String ACTION_GET_MAIL_HIT = "ACTION_GET_MAIL_HIT";
    public static final String ACTION_GET_MAIL_UNDERLINE_FINISH = "ACTION_GET_MAIL_UNDERLINE_FINISH";
    public static final String EVENT_MAIL_DOWNLOAD_FAIL = "EVENT_MAIL_DOWNLOAD_FAIL";
    public static final String EVENT_MAIL_FINISH_DOWNLOAD = "EVENT_MAIL_FINISH_DOWNLOAD";
    public static final String EVENT_MAIL_UPADA_DOWNLOAD = "EVENT_MAIL_UPADA_DOWNLOAD";
    public static final String ACTION_MAIL_SET_FENFA_PERSON = "EVENT_MAIL_SET_FENFA_PERSON";
    public static final String ACTION_MAIL_LIST_UPDATE= "ACTION_MAIL_LIST_UPDATE";
    public static final int ACTION_NEW = 0;
    public static final int ACTION_EDIT = 1;
    public static final int ACTION_REPEAT = 2;
    public static final int ACTION_REPEATALL = 3;
    public static final int ACTION_RESEND = 4;
    public int shoujiancounts;
    public int xincounts;
    public int laocounts;
    public int shenpikcounts;
    public int fajiancounts = 0;
    public int fajiandaipicounts = 0;
    public int fajiantongguocounts = 0;
    public int fajianbohuicounts = 0;
    public int fenfacounts = 0;
    public int fenfaweilianxicounts = 0;
    public int fenfalianxicounts = 0;
    public int fenfalianxifailcounts = 0;
    public int fenfalianxitongguocounts = 0;
    public int neibuxiangcounts = 0;
    public Context context;
    public static volatile MailManager mailManager;
    public MailContact mSelectUser;
    public MailBox mSelectMailBox;
    public ArrayList<MailBox> mMailBoxs = new ArrayList<MailBox>();
    public ArrayList<MailContact> mMailUnderlineUsers = new ArrayList<MailContact>();
    public ArrayList<MailContact> mMailUnderlineHeadUsers = new ArrayList<MailContact>();
    public ArrayList<MailContact> mailContacts = new ArrayList<MailContact>();
    public ArrayList<MailContact> mailLContacts = new ArrayList<MailContact>();
    public ArrayList<MailContact> mHeadMailPersonItems = new ArrayList<MailContact>();
    public ArrayList<MailContact> mLHeadMailPersonItems = new ArrayList<MailContact>();
    public ArrayList<MailContact> mAdd = new ArrayList<MailContact>();
    public ArrayList<Select> mMyLabols = new ArrayList<Select>();
    public boolean[] typebooleans5 = new boolean[26];
    public boolean[] typebooleans6 = new boolean[26];
    public boolean[] typebooleans7 = new boolean[26];
    public static final String typeLetter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public Account account;
    public Service service;
    private String headrepead = "";
    public InitHandler mInitHandler = new InitHandler();
    public XpxShare xpxShare;
    public int mMailcount = 0;
    public int me_approval = 0;
    public int to_me_approval = 0;

    public int allcount1 = 0;
    public int allcount2 = 0;
    public int allcount3 = 0;
    public int allcount4 = 0;
    public int allcount5 = 0;

    public Register register;

    public ArrayList<AttachmentUploadThread> upthreads = new ArrayList<AttachmentUploadThread>();

    public static MailManager init(Context context,XpxShare xpxShare) {
        if (mailManager == null) {
            synchronized (MailManager.class) {
                if (mailManager == null) {
                    mailManager = new MailManager(context, xpxShare);
                }
                else
                {
                    mailManager.context = context;
                    mailManager.xpxShare = xpxShare;
                }
            }
        }
        return mailManager;
    }

    public MailManager(Context context,XpxShare xpxShare) {
        this.context = context;
        this.xpxShare = xpxShare;
    }

    public void setAccount(Account account) {
        this.account = account;
        this.mSelectUser = new MailContact(account.mAccountId,account.mRecordId,true);
    }

    public void setService(Service service) {
        this.service = service;
    }

    public void setRegister(Register register) {
        this.register = register;
    }

    public void cleanAll() {
        mMailBoxs.clear();
        mMailUnderlineUsers.clear();
        mMailUnderlineHeadUsers.clear();
        mailContacts.clear();
        mailLContacts.clear();
        mHeadMailPersonItems.clear();
        mLHeadMailPersonItems.clear();
        mAdd.clear();
        mMyLabols.clear();
        typebooleans5 = new boolean[26];
        typebooleans6 = new boolean[26];
        typebooleans7 = new boolean[26];
    }

    public static MailManager getInstance() {
        return mailManager;
    }

    public String getheadrepead(Context mContext) {

        if (headrepead.length() == 0) {

            headrepead = readStream(mContext.getResources().openRawResource(R.raw.repeat));
        }

        return headrepead;
    }

    public void initLocal() {
        XpxJSONObject xpxJsonObject = null;
        try {
            xpxJsonObject = new XpxJSONObject(account.logininfo);
            XpxJSONObject project = xpxJsonObject.getJSONObject("project");;
            XpxJSONArray mUsers = project.getJSONArray("users");
            for (int i = 0; i < mUsers.length(); i++) {
                XpxJSONObject jo = mUsers.getJSONObject(i);
                MailContact mailContact = new MailContact(jo.getString("username"),jo.getString("userid"),true);
                mailContact.mailRecordID = jo.getString("userid");
                String text = "";
                if (jo.getString("realname") != null) {
                    if (jo.getString("realname").length() > 0) {
                        text = jo.getString("realname");
                    } else
                        text = jo.getString("username");
                } else
                    text = jo.getString("username");
                mailContact.mRName = text;
                if (MailManager.getInstance().mSelectUser.mailRecordID.equals(account.mRecordId) ) {
                    if (mailContact.getMailAddress().length() > 0) {
                        MailManager.getInstance().mailLContacts.add(mailContact);
                        mailContact.islocal = true;
                        String s = mailContact.pingyin.substring(0, 1).toUpperCase();
                        int pos = MailManager.getInstance().typeLetter.indexOf(s);
                        if (pos != -1) {
                            if (MailManager.getInstance().typebooleans7[pos] == false) {
                                MailManager.getInstance().mLHeadMailPersonItems.add(new MailContact(s, 1));
                                MailManager.getInstance().typebooleans7[pos] = true;
                            }
                        }

                    }
                } else if (!MailManager.getInstance().mSelectUser.mailRecordID.equals(jo.getString("userid"))) {
                    if (mailContact.getMailAddress().length() > 0) {
                        MailManager.getInstance().mailLContacts.add(mailContact);
                        mailContact.islocal = true;
                        String s = mailContact.pingyin.substring(0, 1).toUpperCase();
                        int pos = MailManager.getInstance().typeLetter.indexOf(s);
                        if (pos != -1) {
                            if (MailManager.getInstance().typebooleans7[pos] == false) {
                                MailManager.getInstance().mLHeadMailPersonItems.add(new MailContact(s, 1));
                                MailManager.getInstance().typebooleans7[pos] = true;
                            }
                        }

                    }
                }

            }
            mailLContacts.addAll(MailManager.getInstance().mLHeadMailPersonItems);
            Collections.sort(mailLContacts, new SortMailContactComparator());
            Collections.sort(mLHeadMailPersonItems, new SortMailContactComparator());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getMailBox() {
//        InitHandler mInitHandler = new InitHandler();
        MailAsks.getUserMailBox(context,mInitHandler);
    }

    public void getMailUnderline() {
//        InitHandler mInitHandler = new InitHandler();
        MailAsks.getUserUnderlines(context,mInitHandler);
    }

    public void getReadCount() {
        MailAsks.getReadCount(context,mInitHandler);
    }

    public void sendMail(Context context, String address) {
        Intent intent = new Intent(context, MailEditActivity.class);
        intent.putExtra("action", MailManager.ACTION_NEW);
        intent.putExtra("hasaddress",true);
        intent.putExtra("maildata",new Mail());
        if(address.length() > 0)
        {
            intent.putExtra("mailaddress",address);
            context.startActivity(intent);
        }
        else
        {
            AppUtils.showMessage(context,context.getString(R.string.mail_address_empty));
        }
    }

    public void sendMail(Context context, File file) {
        Intent intent = new Intent(context, MailEditActivity.class);
        intent.putExtra("action", MailManager.ACTION_NEW);
        intent.putExtra("sendfujian",true);
        intent.putExtra("maildata",new Mail());
        Attachment attachment = new Attachment();
        attachment.mRecordid = AppUtils.getguid();
        attachment.mSize = file.length();
        attachment.mName = file.getName();
        attachment.mPath = file.getPath();
        intent.putExtra("attachment",attachment);
        context.startActivity(intent);
    }

    private String readStream(InputStream is) { // 资源流(GBK汉字码)变为串
        String res;
        try {
            byte[] buf = new byte[is.available()];
            is.read(buf);
            res = new String(buf, "GBK"); // 必须将GBK码制转成Unicode
            is.close();
        }
        catch (Exception e) {
            res = "";
        }
        return (res);
        // 把资源文本文件送到String串中
    }

    public static void startMailMain(Context context)
    {
        Intent intent = new Intent(context, MailActivity.class);
        context.startActivity(intent);
    }

    public static String praseMailAddress(String addresss) {
        String temp = "";
        temp = Html.fromHtml(addresss).toString();
        if(temp.length() == 0)
        {
            temp = addresss;
        }
        String mword = "";
        String[] address;
        if(temp.contains(";"))
        {
            address = temp.split(",");
            mword = address[0];
        }
        else
        {
            mword = temp;
        }

        if(mword.startsWith("<"))
        {
            mword = mword.substring(1,mword.length());
            if(mword.contains("@")) {
                mword = mword.substring(0,mword.indexOf("@")-1);
            }
        }
        else
        {
            if(mword.contains("<"))
            {
                mword = mword.substring(0,mword.indexOf("<"));
            }
        }
        return mword;

    }

    public void newMail(Context context)
    {
        Intent mIntent = new Intent();
        mIntent.setClass(context, MailEditActivity.class);
        mIntent.putExtra("maildata",new Mail());
        context.startActivity(mIntent);
    }

    public Commend.CommendFun commendFunStart = new Commend.CommendFun() {
        @Override
        public void doCommend(Context context) {
            startMailMain(context);
        }
    };

    public Commend.CommendFun commendFunNew = new Commend.CommendFun() {
        @Override
        public void doCommend(Context context) {
            newMail(context);
        }
    };
}
