package intersky.mail;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import intersky.appbase.entity.Account;
import intersky.appbase.entity.Attachment;
import intersky.appbase.entity.Commend;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Register;
import intersky.appbase.utils.XpxShare;
import intersky.apputils.AppUtils;
import intersky.apputils.CharacterParser;
import intersky.json.XpxJSONArray;
import intersky.json.XpxJSONObject;
import intersky.mail.asks.MailAsks;
import intersky.mail.entity.Mail;
import intersky.mail.entity.MailBox;
import intersky.mail.entity.MailContact;
import intersky.mail.entity.MailFile;
import intersky.mail.entity.MailType;
import intersky.mail.entity.Organization;
import intersky.mail.entity.SortContactComparator;
import intersky.mail.entity.SortMailContactComparator;
import intersky.mail.handler.InitHandler;
import intersky.mail.prase.MailPrase;
import intersky.mail.view.activity.MailActivity;
import intersky.mail.view.activity.MailEditActivity;
import intersky.select.entity.Select;
import intersky.xpxnet.net.Service;

public class MailManager {

    public static final String ACTION_GET_MAIL_BOX_FINISH = "ACTION_GET_MAIL_BOX_FINISH";
    public static final String ACTION_GET_MAIL_OTHERBOX_FINISH = "ACTION_GET_MAIL_OTHERBOX_FINISH";
    public static final String ACTION_GET_MAIL_HIT = "ACTION_GET_MAIL_HIT";
    public static final String ACTION_GET_MAIL_UNDERLINE_FINISH = "ACTION_GET_MAIL_UNDERLINE_FINISH";
    public static final String EVENT_MAIL_DOWNLOAD_FAIL = "EVENT_MAIL_DOWNLOAD_FAIL";
    public static final String EVENT_MAIL_FINISH_DOWNLOAD = "EVENT_MAIL_FINISH_DOWNLOAD";
    public static final String EVENT_MAIL_UPADA_DOWNLOAD = "EVENT_MAIL_UPADA_DOWNLOAD";
    public static final String ACTION_MAIL_SET_FENFA_PERSON = "EVENT_MAIL_SET_FENFA_PERSON";
    public static final String ACTION_MAIL_LIST_UPDATE= "ACTION_MAIL_LIST_UPDATE";
    public static final String ACTION_MAIL_ALL_SEND_UPDATE= "ACTION_MAIL_ALL_SEND_UPDATE";
    public static final String ACTION_MAIL_LABLE_UPDATE= "ACTION_MAIL_LABLE_UPDATE";
    public static final String ACTION_MAIL_FILE_UPDATE= "ACTION_MAIL_FILE_UPDATE";
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
    public HashMap<String,MailBox> hashMailBox = new HashMap<String,MailBox>();
    public ArrayList<MailBox> mOtherMailBoxs = new ArrayList<MailBox>();
    public HashMap<String,MailBox> hashOtherMailBox = new HashMap<String,MailBox>();
    public ArrayList<MailBox> mPushMailBoxs = new ArrayList<MailBox>();
    public HashMap<String,MailBox> hashPushMailBox = new HashMap<String,MailBox>();
    public ArrayList<MailContact> mMailUnderlineUsers = new ArrayList<MailContact>();
    public ArrayList<MailContact> mMailUnderlineHeadUsers = new ArrayList<MailContact>();
    public ArrayList<MailContact> mailContacts = new ArrayList<MailContact>();
    public ArrayList<MailContact> mailLContacts = new ArrayList<MailContact>();
    public ArrayList<MailContact> mHeadMailPersonItems = new ArrayList<MailContact>();
    public ArrayList<MailContact> mLHeadMailPersonItems = new ArrayList<MailContact>();
    public ArrayList<MailBox> allSendMailBoxs = new ArrayList<MailBox>();
    public Organization mOrganization = new Organization();
    public MailType mailType = new MailType();
    public ArrayList<MailContact> mAdd = new ArrayList<MailContact>();
    public ArrayList<Select> mMyLabols = new ArrayList<Select>();
    public ArrayList<Select> mGropLabols = new ArrayList<Select>();
    public ArrayList<Select> mMySelectLabols = new ArrayList<Select>();
    public HashMap<String,Select> closrhash = new HashMap<String,Select>();
    public ArrayList<MailFile> mailFiles = new ArrayList<MailFile>();
    public boolean[] typebooleans5 = new boolean[26];
    public boolean[] typebooleans6 = new boolean[26];
    public boolean[] typebooleans7 = new boolean[26];
    public static final String typeLetter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String colors[] = {
            "#B54143", "#47A91C", "#DB8933", "#3796BF", "#DF7BA6",
            "#E5ACAE", "#AEDFA3", "#A5DAEA", "#F3D1A8", "#F4C9DF",
            "#43BC97", "#C7AD24", "#CF69E2", "#9D9D9D", "#373737",
            "#ABE7D9", "#ECE0A5", "#EFC0F6", "#D9D9D9", "#F1F1F1"
    };
    public JSONArray order = new JSONArray();
    public JSONArray module = new JSONArray();
    public HashMap<String,String> tree = new HashMap<String,String>();
    public Account account;
    public Service service;
    private String headrepead = "";
    public InitHandler mInitHandler = new InitHandler();
    public XpxShare xpxShare;
    public int mMailcount = 0;
    public int me_approval = 0;
    public int to_me_approval = 0;
    public int allcount0 = 0;
    public int allcount1 = 0;
    public int allcount2 = 0;
    public int allcount4 = 0;
    public int allcount5 = 0;
    public int allcount6 = 0;
    public XpxJSONArray mUsers;
    public Register register;
    public AttachmentUploadThread nowAttachmentUploadThread;
    public boolean showPush = false;
    public ArrayList<AttachmentUploadThread> upthreads = new ArrayList<AttachmentUploadThread>();

    public static MailManager init(Context context,XpxShare xpxShare) {
        if (mailManager == null) {
            synchronized (MailManager.class) {
                if (mailManager == null) {
                    mailManager = new MailManager(context, xpxShare);
                    mailManager.initMailColors();
                }
                else
                {
                    mailManager.context = context;
                    mailManager.xpxShare = xpxShare;
                    mailManager.initMailColors();
                }
            }
        }
        return mailManager;
    }

    public void initMailColors() {
        mMySelectLabols.clear();
        closrhash.clear();
        for(int i = 0 ; i < colors.length ; i++)
        {
            Select select = new Select(colors[i],colors[i]);
            select.mColor = colors[i];
            mMySelectLabols.add(select);
            closrhash.put(select.mId,select);
        }
    }

    public Select getLable(String id) {
        for(int i = 0 ; i < mMyLabols.size() ; i++)
        {
            if(mMyLabols.get(i).mId.equals(id))
            {
                return mMyLabols.get(i);
            }
        }
        return null;
    }

    public Select getGLable(String id) {
        for(int i = 0 ; i < mGropLabols.size() ; i++)
        {
            if(mGropLabols.get(i).mId.equals(id))
            {
                return mGropLabols.get(i);
            }
        }
        return null;
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
        hashMailBox.clear();
        mOtherMailBoxs.clear();
        hashOtherMailBox.clear();
        mMailUnderlineUsers.clear();
        mMailUnderlineHeadUsers.clear();
        mailContacts.clear();
        mailLContacts.clear();
        mHeadMailPersonItems.clear();
        mLHeadMailPersonItems.clear();
        mAdd.clear();
        mMyLabols.clear();
        mGropLabols.clear();
        mailFiles.clear();
        mPushMailBoxs.clear();
        hashPushMailBox.clear();
        typebooleans5 = new boolean[26];
        typebooleans6 = new boolean[26];
        typebooleans7 = new boolean[26];
    }

    public void getAllData() {
        getMailBox();
        getMailPush();
        getLables();
        getMailFiles();
        getMailType();
        getMailUnderline();
        getGropLables();
        if(account!=null)
        {
            if(account.iscloud == true)
            {
                getMailContacts();
            }
        }

    }

    public void getMailContacts() {
        MailAsks.getMailContacts(context,mInitHandler);
    }

    public void resetUser() {
        getMailBox();
        getMailFiles();
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

    public void setMailBox(String rid) {
        for(int i = 0 ; i < mMailBoxs.size() ; i++)
        {
            if(mMailBoxs.get(i).mRecordId.equals(rid))
            {
                if(mSelectMailBox != null)
                {
                    mSelectMailBox.isSelect = false;
                }
                mSelectMailBox = mMailBoxs.get(i);
                mSelectMailBox.isSelect = true;
            }
        }
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
                mailContact.mRecordid = jo.getString("userid");
                mailContact.mailAddress2 = jo.getString("username")+"@local.com";
                if(account.iscloud)
                {
                    mailContact.mType = Contacts.TYPE_PERSON;
                }

                String text = "";
                if (jo.getString("realname") != null) {
                    if (jo.getString("realname").length() > 0) {
                        text = jo.getString("realname");
                    } else
                        text = jo.getString("username");
                } else
                    text = jo.getString("username");
                mailContact.mRName = text;
                if (MailManager.getInstance().mSelectUser.mRecordid.equals(account.mRecordId) ) {
                    if (mailContact.getMailAddress().length() > 0) {
                        MailManager.getInstance().mailLContacts.add(mailContact);
                        mailContact.islocal = true;
                        String s = mailContact.pingyin.substring(0, 1).toUpperCase();
                        int pos = MailManager.getInstance().typeLetter.indexOf(s);
                        if (pos != -1) {
                            if (MailManager.getInstance().typebooleans7[pos] == false) {
                                if(account.iscloud)
                                MailManager.getInstance().mLHeadMailPersonItems.add(new MailContact(s, Contacts.TYPE_LETTER));
                                else
                                    MailManager.getInstance().mLHeadMailPersonItems.add(new MailContact(s, 1));
                                MailManager.getInstance().typebooleans7[pos] = true;
                            }
                        }

                    }
                } else if (!MailManager.getInstance().mSelectUser.mRecordid.equals(jo.getString("userid"))) {
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
        initMailContacts();
    }

    public void initMailContacts() {
        mOrganization.clean();
        initCompanyContacts();
    }

    private void initCompanyContacts() {
        mOrganization.organizationMailContact.setName(context.getString(R.string.main_org));
        mOrganization.organizationMailContact.mId = "baseroot";
        mOrganization.organizationMailContact.mType = Contacts.TYPE_CLASS;
        mOrganization.mAllMailContactDepartMap.put(mOrganization.organizationMailContact.mRecordid,mOrganization.organizationMailContact);
        mOrganization.allClassMailContact.add(mOrganization.organizationMailContact);
        try {
            XpxJSONObject jo = (XpxJSONObject) account.project.getJSONObject("orgs");
            mUsers = account.project.getJSONArray("users");
            MailPrase.praseContacts(jo, mOrganization.organizationMailContact,context);
            MailContact mailContact = new MailContact();
            mailContact.parent = mOrganization.organizationMailContact;
            mailContact.setmName(context.getString(R.string.keyword_typetitle));
            mailContact.mType = Contacts.TYPE_CLASS;
            mailContact.type = Contacts.TYPE_CLASS;
            mailContact.mailType = mailType;
            mOrganization.organizationMailContact.mContacts.add(mailContact);
            mOrganization.allClassMailContact.add(mailContact);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //initLocalContacts();
    }

    private void initLocalContacts() {
        mOrganization.typebooleans3 = new boolean[27];
        for (int i = 0; i < mOrganization.allMailContact.size(); i++) {
            MailContact mContactModel2 = mOrganization.allMailContact.get(i);
            String s = mContactModel2.getmPingyin().substring(0, 1).toUpperCase();
            int pos = CharacterParser.typeLetter.indexOf(s);
            if (pos != -1) {
                if (mOrganization.typebooleans3[pos] == false) {
                    mOrganization.allMailContactHead.add(new MailContact(s));
                    mOrganization.typebooleans3[pos] = true;
                }
            }
            else
            {
                s = "#";
                pos = CharacterParser.typeLetter.indexOf(s);
                if (pos != -1) {
                    if (mOrganization.typebooleans3[pos] == false) {
                        mOrganization.allMailContactHead.add(new MailContact(s));
                        mOrganization.typebooleans3[pos] = true;
                    }
                }
            }
        }
        mOrganization.allMailContact.addAll(0, mOrganization.allMailContactHead);
        Collections.sort(mOrganization.allMailContact, new SortContactComparator());
        Collections.sort(mOrganization.allMailContactHead, new SortContactComparator());
        mOrganization.allClassMailContact.addAll(mOrganization.allMailContact);
    }

    public void getMailBox() {
//        InitHandler mInitHandler = new InitHandler();
        if(mSelectUser != null)
        MailAsks.getUserMailBox(context,mInitHandler,mSelectUser.mRecordid);
        else
            MailAsks.getUserMailBox(context,mInitHandler,account.mRecordId);
    }

    public void getMailPush() {
//        InitHandler mInitHandler = new InitHandler();
        if(mSelectUser != null)
        MailAsks.pushCheck(context,mInitHandler,mSelectUser.mRecordid);
        else
            MailAsks.pushCheck(context,mInitHandler,account.mRecordId);
    }

    public void getMailUnderline() {
//        InitHandler mInitHandler = new InitHandler();
        if(mSelectUser != null)
        MailAsks.getUserUnderlines(context,mInitHandler,mSelectUser.mRecordid);
        else
            MailAsks.getUserUnderlines(context,mInitHandler,account.mRecordId);
    }

    public void getReadCount() {
        if(mSelectUser != null)
        MailAsks.getReadCount(context,mInitHandler,mSelectUser.mRecordid);
        else
            MailAsks.getReadCount(context,mInitHandler,account.mRecordId);
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

    public void getLables() {
        if(mSelectUser != null)
        MailAsks.getUserLable(mInitHandler,context,mSelectUser.mRecordid);
        else
            MailAsks.getUserLable(mInitHandler,context,account.mRecordId);
    }

    public void getGropLables() {
        if(mSelectUser != null)
            MailAsks.getGropLable(context,mInitHandler);
        else
            MailAsks.getGropLable(context,mInitHandler);
    }

    public void getMailFiles() {
        MailAsks.getFiles(mInitHandler,context);
    }

    public void getMailType() {
        if(mSelectUser != null)
        MailAsks.getTypes(mInitHandler,context,mSelectUser.mRecordid);
        else
            MailAsks.getTypes(mInitHandler,context,account.mRecordId);
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

    public static void setContactCycleHead(TextView mhead, MailContact mContacts) {
        String s;
        if(mContacts.getName().length() > 0)
        {
            if(mContacts.getName().length() > 2)
            {
                s = mContacts.getName().substring(0,2);
                mhead.setText(mContacts.getName().substring(0,2));
            }
            else
            {
                s = mContacts.getName().toString();
                if(s != null)
                    mhead.setText(mContacts.getName().substring(0,s.length()));
            }
        }
        mhead.setBackgroundResource(R.drawable.contact_head);
    }
}
