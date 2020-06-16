package intersky.mail.prase;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import intersky.appbase.entity.Contacts;
import intersky.json.XpxJSONArray;
import intersky.json.XpxJSONObject;
import intersky.mail.MailManager;
import intersky.mail.R;
import intersky.mail.asks.MailAsks;
import intersky.mail.entity.Mail;
import intersky.mail.entity.MailBox;
import intersky.mail.entity.MailContact;
import intersky.mail.entity.SortMailContactComparator;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Attachment;
import intersky.apputils.AppUtils;
import intersky.apputils.CharacterParser;
import intersky.select.entity.Select;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

public class MailPrase {

    public static final String typeLetter = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static void addMailBoxData(NetObject net) {

        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
            return;

        JSONArray data = null;
        MailManager.getInstance().mMailBoxs.clear();
        if(MailManager.getInstance().mSelectUser == null)
        {
            if (MailManager.getInstance().account.mRealName.length() != 0) {
                MailBox mMailBoxModel = new MailBox();
                mMailBoxModel.mAddress = MailManager.getInstance().account.mRealName;
                mMailBoxModel.mFullAddress = MailManager.getInstance().account.mRealName;
                mMailBoxModel.mRecordId = MailManager.getInstance().account.mRecordId;
                mMailBoxModel.isloacl = true;
                MailManager.getInstance().mMailBoxs.add(mMailBoxModel);
            }
            else {
                MailBox mMailBoxModel = new MailBox();
                mMailBoxModel.mAddress = MailManager.getInstance().account.mAccountId;
                mMailBoxModel.mFullAddress = MailManager.getInstance().account.mAccountId;
                mMailBoxModel.mRecordId = MailManager.getInstance().account.mRecordId;
                mMailBoxModel.isloacl = true;
                MailManager.getInstance().mMailBoxs.add(mMailBoxModel);
            }
        }
        else if(MailManager.getInstance().mSelectUser.mailRecordID.equals(MailManager.getInstance().account.mRecordId))
        {
            if (MailManager.getInstance().account.mRealName.length() != 0) {
                MailBox mMailBoxModel = new MailBox();
                mMailBoxModel.mAddress = MailManager.getInstance().account.mRealName;
                mMailBoxModel.mFullAddress = MailManager.getInstance().account.mRealName;
                mMailBoxModel.mRecordId = MailManager.getInstance().account.mRecordId;
                mMailBoxModel.isloacl = true;
                MailManager.getInstance().mMailBoxs.add(mMailBoxModel);
            }
            else {
                MailBox mMailBoxModel = new MailBox();
                mMailBoxModel.mAddress = MailManager.getInstance().account.mAccountId;
                mMailBoxModel.mFullAddress = MailManager.getInstance().account.mAccountId;
                mMailBoxModel.mRecordId = MailManager.getInstance().account.mRecordId;
                mMailBoxModel.isloacl = true;
                MailManager.getInstance().mMailBoxs.add(mMailBoxModel);
            }
        }
        else
        {
            MailBox mMailBoxModel = new MailBox();
            mMailBoxModel.mAddress = MailManager.getInstance().mSelectUser.mName;
            mMailBoxModel.mFullAddress =  MailManager.getInstance().mSelectUser.mName;
            mMailBoxModel.mRecordId = MailManager.getInstance().mSelectUser.mailRecordID;
            mMailBoxModel.isloacl = true;
            MailManager.getInstance().mMailBoxs.add(mMailBoxModel);
        }
        try {

            JSONObject jObject = new JSONObject(json);
            if (jObject.has("Data")) {
                data = jObject.getJSONArray("Data");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject maildata = data.getJSONObject(i);
                    if (maildata.getString("FullAddress").length() != 0
                            || maildata.getString("Address").length() != 0) {
                        MailBox mMailBoxModel = new MailBox();
                        mMailBoxModel.mAddress = maildata.getString("Address");
                        mMailBoxModel.mFullAddress = maildata.getString("FullAddress");
                        mMailBoxModel.mRecordId = maildata.getString("RecordID");
                        if (mMailBoxModel.mAddress.length() == 0) {
                            String address = maildata.getString("FullAddress");
                            address = address.substring(0, address.indexOf("("));
                            mMailBoxModel.mAddress = address;
                        }

                        if (mMailBoxModel.mFullAddress.length() == 0) {
                            mMailBoxModel.mFullAddress = maildata.getString("Address");
                        }

                        if (maildata.getBoolean("POP3") == true && maildata.getBoolean("SMTP") == true) {
                            MailManager.getInstance().mMailBoxs.add(mMailBoxModel);
                        }

                    }

                }
            }

        }
        catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (MailManager.getInstance().mMailBoxs.size() > 1) {
            MailManager.getInstance().mMailBoxs.get(1).isSelect = true;
            MailManager.getInstance().mSelectMailBox = MailManager.getInstance().mMailBoxs.get(1);
        }
        else {
            MailManager.getInstance().mMailBoxs.get(0).isSelect = true;
            MailManager.getInstance().mSelectMailBox = MailManager.getInstance().mMailBoxs.get(0);
        }

    }

    public static void addMailBoxDataCloud(NetObject net) {

        String json = net.result;

        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
            return;

        JSONArray data = null;
        MailManager.getInstance().mMailBoxs.clear();
        try {

            JSONObject jObject = new JSONObject(json);
            if (jObject.has("data")) {
                data = jObject.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject maildata = data.getJSONObject(i);
                    if (maildata.getString("FullAddress").length() != 0
                            || maildata.getString("Address").length() != 0) {
                        MailBox mMailBoxModel = new MailBox();
                        mMailBoxModel.mAddress = maildata.getString("FullAddress");
                        mMailBoxModel.mFullAddress = maildata.getString("Address");
                        mMailBoxModel.mRecordId = maildata.getString("RecordID");
                        if (mMailBoxModel.mAddress.length() == 0) {
                            String address = maildata.getString("FullAddress");
                            address = address.substring(0, address.indexOf("("));
                            mMailBoxModel.mAddress = address;
                        }

                        if (mMailBoxModel.mFullAddress.length() == 0) {
                            mMailBoxModel.mFullAddress = maildata.getString("Address");
                        }

                        if (maildata.getBoolean("POP3") == true && maildata.getBoolean("SMTP") == true) {
                            MailManager.getInstance().mMailBoxs.add(mMailBoxModel);
                        }

                    }

                }
            }

        }
        catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (MailManager.getInstance().mMailBoxs.size() > 0) {
            MailManager.getInstance().mMailBoxs.get(0).isSelect = true;
            MailManager.getInstance().mSelectMailBox = MailManager.getInstance().mMailBoxs.get(0);
        }

    }

    public static void addUser(NetObject net) {

        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
            return;

        JSONArray data = null;
        MailManager.getInstance().mMailUnderlineUsers.clear();
        MailManager.getInstance().typebooleans5 = new boolean[27];
        MailContact mUser = new MailContact("我","");
        mUser.mailRecordID = MailManager.getInstance().account.mRecordId;
        mUser.isselect = true;
        MailManager.getInstance().mMailUnderlineUsers.add(mUser);
        String s1 = mUser.pingyin.substring(0, 1).toUpperCase();
        int pos1 = CharacterParser.typeLetter.indexOf(s1);
        if (pos1 != -1) {
            if (MailManager.getInstance().typebooleans5[pos1] == false) {
                MailManager.getInstance().mMailUnderlineHeadUsers.add(new MailContact(s1,1));
                MailManager.getInstance().typebooleans5[pos1] = true;
            }
        }
        else
        {
            s1 = "#";
            pos1 = CharacterParser.typeLetter.indexOf(s1);
            if (pos1 != -1) {
                if (MailManager.getInstance().typebooleans5[pos1] == false) {
                    MailManager.getInstance().mMailUnderlineHeadUsers.add(new MailContact(s1,1));
                    MailManager.getInstance().typebooleans5[pos1] = true;
                }
            }
        }
        if (json != null) {
            try {
                JSONObject jObject = new JSONObject(json);
                if (jObject.has("Data")) {
                    data = jObject.getJSONArray("Data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject maildata = data.getJSONObject(i);
                        MailContact mtUser = new MailContact(maildata.getString("Name"),"");
                        mtUser.mailRecordID = maildata.getString("RecordID");
                        if (mtUser.mailRecordID.length() > 0) {
                            MailManager.getInstance().mMailUnderlineUsers.add(mtUser);
                            String s = mtUser.pingyin.substring(0, 1).toUpperCase();
                            int pos = CharacterParser.typeLetter.indexOf(s);
                            if (pos != -1) {
                                if (MailManager.getInstance().typebooleans5[pos] == false) {
                                    MailManager.getInstance().mMailUnderlineHeadUsers.add(new MailContact(s,1));
                                    MailManager.getInstance().typebooleans5[pos] = true;
                                }
                            }
                            else
                            {
                                s = "#";
                                pos = CharacterParser.typeLetter.indexOf(s);
                                if (pos != -1) {
                                    if (MailManager.getInstance().typebooleans5[pos] == false) {
                                        MailManager.getInstance().mMailUnderlineHeadUsers.add(new MailContact(s,1));
                                        MailManager.getInstance().typebooleans5[pos] = true;
                                    }
                                }
                            }
                        }
                    }
                }
                MailManager.getInstance().mMailUnderlineUsers.addAll(0,MailManager.getInstance().mMailUnderlineHeadUsers);
                Collections.sort(MailManager.getInstance().mMailUnderlineHeadUsers, new SortMailContactComparator());
                Collections.sort(MailManager.getInstance().mMailUnderlineUsers, new SortMailContactComparator());

            }
            catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public static void getMailHint(NetObject net) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
            return;
        try {
            JSONObject jObject = new JSONObject(json);
            MailManager.getInstance().shoujiancounts = jObject.getInt("TotalRecordCount") - jObject.getInt("TotalReadedCount");
            MailManager.getInstance().laocounts = jObject.getInt("TotalClientCount") - jObject.getInt("TotalClientReadedCount");
            MailManager.getInstance().xincounts = MailManager.getInstance().shoujiancounts - MailManager.getInstance().laocounts;
            MailManager.getInstance().shenpikcounts = jObject.getInt("TotalApprovalCount");
            MailManager.getInstance().fajiancounts = jObject.getInt("OutboxCount");
            MailManager.getInstance().fajiandaipicounts = jObject.getInt("Outbox1Count");
            MailManager.getInstance().fajianbohuicounts = jObject.getInt("Outbox3Count");
            MailManager.getInstance().fajiantongguocounts = MailManager.getInstance().fajiancounts - MailManager.getInstance().fajianbohuicounts - MailManager.getInstance().fajiandaipicounts;
            MailManager.getInstance().fenfacounts = jObject.getInt("DisCount");
            MailManager.getInstance().fenfalianxicounts = jObject.getInt("Dis1Count");
            MailManager.getInstance().fenfalianxifailcounts = jObject.getInt("Dis2Count");
            MailManager.getInstance().fenfalianxitongguocounts = jObject.getInt("Dis3Count");
            MailManager.getInstance().fenfaweilianxicounts = MailManager.getInstance().fenfacounts - MailManager.getInstance().fenfalianxicounts - MailManager.getInstance().fenfalianxifailcounts - MailManager.getInstance().fenfalianxitongguocounts;
            MailManager.getInstance().neibuxiangcounts = jObject.getInt("LocalCount") - jObject.getInt("LocalReadedCount");
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public static void getMailHintCloud(NetObject net) {
        try {
            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
                return;
            JSONObject jObject = new JSONObject(json);
            JSONObject data = jObject.getJSONObject("data");


            JSONObject unread = data.getJSONObject("un_read_count");
            JSONObject yifa = data.getJSONObject("un_send_count");
            JSONObject caogao = data.getJSONObject("draft_count");
            JSONObject huishou = data.getJSONObject("del_count");
            JSONObject laji = data.getJSONObject("spam_count");
            MailManager.getInstance().allcount1 = data.getInt("un_read_all");
            MailManager.getInstance().me_approval = data.getInt("me_approval");
            MailManager.getInstance().to_me_approval = data.getInt("to_me_approval");
            MailManager.getInstance().allcount2 = 0;
            MailManager.getInstance().allcount3 = 0;
            MailManager.getInstance().allcount4 = 0;
            MailManager.getInstance().allcount5 = 0;
            for(int i = 0 ; i < MailManager.getInstance().mMailBoxs.size(); i++)
            {
                JSONObject jo1 = unread.getJSONObject(MailManager.getInstance().mMailBoxs.get(i).mRecordId);
                JSONObject jo2 = yifa.getJSONObject(MailManager.getInstance().mMailBoxs.get(i).mRecordId);
                JSONObject jo3 = caogao.getJSONObject(MailManager.getInstance().mMailBoxs.get(i).mRecordId);
                JSONObject jo4 = huishou.getJSONObject(MailManager.getInstance().mMailBoxs.get(i).mRecordId);
                JSONObject jo5 = laji.getJSONObject(MailManager.getInstance().mMailBoxs.get(i).mRecordId);
//                getcount(jo1.getInt("count"),mMailActivity.shoujianhits.get(MailManager.getInstance().mMyMailBoxs.get(i).getmMailRecordId()));
//                getcount(jo2.getInt("count"),mMailActivity.yifahits.get(MailManager.getInstance().mMyMailBoxs.get(i).getmMailRecordId()));
//                getcount(jo3.getInt("count"),mMailActivity.caogaohits.get(MailManager.getInstance().mMyMailBoxs.get(i).getmMailRecordId()));
//                getcount(jo4.getInt("count"),mMailActivity.huishouhits.get(MailManager.getInstance().mMyMailBoxs.get(i).getmMailRecordId()));
//                getcount(jo5.getInt("count"),mMailActivity.lajihits.get(MailManager.getInstance().mMyMailBoxs.get(i).getmMailRecordId()));
                MailManager.getInstance().allcount2+= jo2.getInt("count");
                MailManager.getInstance().allcount3+= jo3.getInt("count");
                MailManager.getInstance().allcount4+= jo4.getInt("count");
                MailManager.getInstance().allcount5+= jo5.getInt("count");
                MailManager.getInstance().mMailBoxs.get(i).count1 = jo1.getInt("count");
                MailManager.getInstance().mMailBoxs.get(i).count2 = jo2.getInt("count");
                MailManager.getInstance().mMailBoxs.get(i).count3 = jo3.getInt("count");
                MailManager.getInstance().mMailBoxs.get(i).count4 = jo4.getInt("count");
                MailManager.getInstance().mMailBoxs.get(i).count5 = jo5.getInt("count");
            }


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static int praseMail(NetObject net, ArrayList<Mail> mails) {
        JSONArray data = null;
        String json = net.result;

        if (json != null) {
            try {

                if(AppUtils.measureToken(json) != null) {
                    NetUtils.getInstance().token = AppUtils.measureToken(json);
                }
                if(AppUtils.success(json) == false)
                    return 0;

                JSONObject jObject = new JSONObject(json);
                data = jObject.getJSONArray("Data");

                for (int i = 0; i < data.length(); i++) {
                    JSONObject maildata = data.getJSONObject(i);
                    Mail mMailItem = new Mail(maildata.getString("RecordID"), maildata.getString("MailBoxID"),
                            maildata.getString("UserID"),
                            maildata.getString("SerialID"), maildata.getString("Subject"),
                            maildata.getString("Content"), maildata.getString("From"), maildata.getString("To"),
                            maildata.getString("LocalCC"), maildata.getString("BCC"), maildata.getString("CC"),
                            maildata.getString("Date"), maildata.getString("Attachments"),
                            maildata.getBoolean("Readed"), maildata.getBoolean("IsLocal"),
                            maildata.getBoolean("HaveAttachment"),
                            maildata.getString("CatalogueID"),maildata.getBoolean("Replied"),maildata.getBoolean("Forwarded"));
                    mails.add(mMailItem);
                }
                return data.length();
            }
            catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return 0;
            }
        }
        else
        {
            return 0;
        }
    }

    public static int praseMail(NetObject net, ArrayList<Mail> mails,int currentid) {
        XpxJSONArray data = null;
        String json = net.result;

        if (json != null) {
            try {

                if(AppUtils.measureToken(json) != null) {
                    NetUtils.getInstance().token = AppUtils.measureToken(json);
                }
                if(AppUtils.success(json) == false)
                    return 0;

                XpxJSONObject jObject = new XpxJSONObject(json);
                XpxJSONObject jsonObject = jObject.getJSONObject("data");
                int count = jsonObject.getInt("total",0);
                data = jsonObject.getJSONArray("list");
                for (int i = 0; i < data.length(); i++) {
                    XpxJSONObject maildata = data.getJSONObject(i);
                    Mail mMailItem;
                    if(maildata.has("content"))
                    {
                        mMailItem = new Mail(maildata.getString("mail_user_id"), maildata.getString("mail_box_id"),
                                maildata.getString("user_id"),
                                "", maildata.getString("subject"),
                                maildata.getString("content"), maildata.getString("from_address"), maildata.getString("to_address"),
                                "", maildata.getString("bcc_address"), maildata.getString("cc_address"),
                                maildata.getString("create_time"), "",
                                maildata.getBoolean("is_read",false), false,
                                maildata.getBoolean("is_assets",false),
                                maildata.getString("assets_path"),maildata.getBoolean("is_reply",false),maildata.getBoolean("is_track",false));
                    }
                    else
                    {
                        mMailItem = new Mail(maildata.getString("mail_user_id"), maildata.getString("mail_box_id"),
                                maildata.getString("user_id"),
                                "", maildata.getString("subject"),
                                "", maildata.getString("from_address"), maildata.getString("to_address"),
                                "", maildata.getString("bcc_address"), maildata.getString("cc_address"),
                                maildata.getString("create_time"), "",
                                maildata.getBoolean("is_read",false), false,
                                maildata.getBoolean("is_assets",false),
                                maildata.getString("assets_path"),maildata.getBoolean("is_reply",false),maildata.getBoolean("is_track",false));
                    }
                    mMailItem.state = maildata.getInt("state",0);
                    mMailItem.mailtype = maildata.getInt("mail_type",0);
                    mMailItem.sendstate = maildata.getInt("send_state",0);
                    mails.add(mMailItem);
                }
                if(mails.size() == count) {
                    return -1;
                }
                else {
                    if(mails.size()%40 > 0)
                    {
                        return mails.size()/40+2;
                    }
                    else
                    {
                        return mails.size()/40+1;
                    }
                }
            }
            catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return 0;
            }
        }
        else
        {
            return 0;
        }
    }

    public static void praseMail(NetObject net, Mail mail)
    {
        String html = "";
        try {
            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
                return ;

            JSONObject jsonObject = new JSONObject(json);

            if(MailManager.getInstance().account.iscloud == false)
            {
//                mail.mContentHtml = measureHtml(jsonObject.getString("html"));
                mail.mContentHtml = jsonObject.getString("html");
                if(mail.haveAttachment)
                {
                    mail.mAttachmentsJson = jsonObject.getJSONArray("attachment").toString();
                    praseAttachment(jsonObject.getJSONArray("attachment"),mail);
                }
            }
            else
            {
                JSONObject data1 = jsonObject.getJSONObject("data");
//                mail.mContentHtml = measureHtml(data1.getString("html"));
                mail.mContentHtml = data1.getString("html");
                if(mail.haveAttachment)
                {
                    mail.mAttachmentsJson = data1.getJSONArray("attachment").toString();
                    praseAttachment(jsonObject.getJSONArray("attachment"),mail);

                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static void praseAttachment(JSONArray data, Mail mail) {
        mail.attachments.clear();
        try {

            for (int i = 0; i < data.length(); i++) {

                JSONObject maildata = data.getJSONObject(i);
                Attachment mFuJianItem = new Attachment();
                mFuJianItem.mName = maildata.getString("Name");
                mFuJianItem.mRecordid = maildata.getString("GUID");
                mFuJianItem.mSize = maildata.getLong("Size");
                mFuJianItem.mPath = Bus.callData(MailManager.getInstance().context,"filetools/getfilePath", mFuJianItem.mRecordid)+"/"+ maildata.getString("Name");
                mFuJianItem.mUrl = MailAsks.measureAttachmentUrl(mail.mRecordId,maildata.getString("GUID"));
                mail.attachments.add(mFuJianItem);
            }
        }
        catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static boolean praseMailSend(NetObject net,Context context) {
        JSONObject data = null;
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return false;
        }
        return true;
    }

    public static void addOutGuestData(NetObject net) {



        try {
            JSONArray data = null;
            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
                return;
            MailManager.getInstance().mailContacts.clear();
            MailManager.getInstance().mHeadMailPersonItems.clear();
            MailManager.getInstance().typebooleans6 = new boolean[27];
            JSONObject jObject = new JSONObject(json);
            if (jObject.has("Data")) {
                data = jObject.getJSONArray("Data");
                for (int i = 0; i < data.length(); i++) {
                    Log.d("addGuestData", String.valueOf(i));
                    JSONObject maildata = data.getJSONObject(i);
                    String name = maildata.getString("ContactName");
                    if(!maildata.has("Email")) {
                        continue;
                    }
                    MailContact mMailItem = new MailContact(maildata.getString("ContactName"),
                            maildata.getString("Email"), false);
                    if (name.length() == 0) {
                        mMailItem.setmName(mMailItem.getMailAddress());
                    }
                    // mMailItem.setMailRecordID(maildata.getString("RecordID"));
                    if (mMailItem.getMailAddress().length() > 0) {
                        MailManager.getInstance().mailContacts.add(mMailItem);
                        String s = mMailItem.pingyin.substring(0, 1).toUpperCase();
                        int pos = typeLetter.indexOf(s);
                        if (pos != -1) {
                            if (MailManager.getInstance().typebooleans6[pos] == false) {
                                MailManager.getInstance().mHeadMailPersonItems.add(new MailContact(s, 1));
                                MailManager.getInstance().typebooleans6[pos] = true;
                            }
                        }
                        else
                        {
                            s = "#";
                            pos = CharacterParser.typeLetter.indexOf(s);
                            if (pos != -1) {
                                if (MailManager.getInstance().typebooleans6[pos] == false) {
                                    MailManager.getInstance().mHeadMailPersonItems.add(new MailContact(s, 1));
                                    MailManager.getInstance().typebooleans6[pos] = true;
                                }
                            }
                        }

                    }
                }
                MailManager.getInstance().mailContacts.addAll(MailManager.getInstance().mHeadMailPersonItems);
                Collections.sort(MailManager.getInstance().mailContacts, new SortComparator());
                Collections.sort(MailManager.getInstance().mHeadMailPersonItems, new SortComparator());
            }
        }
        catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static class SortComparator implements Comparator {

        @Override
        public int compare(Object mMailPersonItem1, Object mMailPersonItem2)
        {
            // TODO Auto-generated method stub
            String[] array = new String[2];

            array[0] = ((MailContact) mMailPersonItem1).pingyin;
            array[1] = ((MailContact) mMailPersonItem2).pingyin;
            if (array[0].equals(array[1]))
            {
                return 0;
            }
            Arrays.sort(array);
            if (array[0].equals(((MailContact) mMailPersonItem1).pingyin))
            {
                return -1;
            }
            else if (array[0].equals(((MailContact) mMailPersonItem2).pingyin))
            {
                return 1;
            }
            return 0;
        }
    }

    public static void addLData(NetObject net) {


        try {

            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
                return;
            MailManager.getInstance().mMyLabols.clear();
            JSONArray data = null;
            data = new JSONArray(json);
            for (int i = 0; i < data.length(); i++) {
                JSONObject maildata = data.getJSONObject(i);
                Select mMailItem = new Select(maildata.getString("record_id"),
                        maildata.getString("name"));
                MailManager.getInstance().mMyLabols.add(mMailItem);

            }

        }
        catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static boolean praseFenfa(NetObject net, Context context)
    {
        try {
            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
                return false;

            JSONObject jObject = new JSONObject(json);
            if (jObject.has("success"))
            {
                if (jObject.getBoolean("success") == true)
                {
                    AppUtils.showMessage(context, context.getString(R.string.keyword_fengfasuccess));
                    return true;
                }
                AppUtils.showMessage(context, context.getString(R.string.keyword_fengfafail));
                return false;
            }
            else
            {
                AppUtils.showMessage(context, context.getString(R.string.keyword_fengfafail));
                return false;
            }
        }
        catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    public static boolean praseDate(NetObject net) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
            return false;

        return true;
    }

    public static String measureHtml(String html) {
        if(MailManager.getInstance().service.https == false)
            return html.replaceAll("/img","http://" + MailManager.getInstance().service.sAddress+"/image/"+MailManager.getInstance().account.mCompanyId);
        else
            return html.replaceAll("/img","https://" + MailManager.getInstance().service.sAddress+"/image/"+MailManager.getInstance().account.mCompanyId);
    }
}
