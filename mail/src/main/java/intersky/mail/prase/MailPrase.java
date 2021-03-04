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
import java.util.Iterator;

import intersky.appbase.entity.Contacts;
import intersky.json.XpxJSONArray;
import intersky.json.XpxJSONObject;
import intersky.mail.MailManager;
import intersky.mail.R;
import intersky.mail.asks.MailAsks;
import intersky.mail.entity.Mail;
import intersky.mail.entity.MailBox;
import intersky.mail.entity.MailContact;
import intersky.mail.entity.MailFile;
import intersky.mail.entity.MailType;
import intersky.mail.entity.SortContactComparator;
import intersky.mail.entity.SortMailContactComparator;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Attachment;
import intersky.apputils.AppUtils;
import intersky.apputils.CharacterParser;
import intersky.mail.presenter.MailPresenter;
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
        else if(MailManager.getInstance().mSelectUser.mRecordid.equals(MailManager.getInstance().account.mRecordId))
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
            mMailBoxModel.mRecordId = MailManager.getInstance().mSelectUser.mRecordid;
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

    public static int addMailBoxDataCloud(NetObject net) {

        String json = net.result;
        String uid = (String) net.item;
        JSONArray data = null;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
        {
            if(uid.equals(MailManager.getInstance().account.mRecordId))
            {
                return -1;
            }
            else{
                return 1;
            }
        }
        if(uid.equals(MailManager.getInstance().account.mRecordId))
        {
            MailManager.getInstance().mMailBoxs.clear();
            MailManager.getInstance().hashMailBox.clear();
        }
        else
        {
            MailManager.getInstance().mOtherMailBoxs.clear();
            MailManager.getInstance().hashOtherMailBox.clear();
        }

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
                            if(uid.equals(MailManager.getInstance().account.mRecordId))
                            {
                                MailManager.getInstance().mMailBoxs.add(mMailBoxModel);
                                MailManager.getInstance().hashMailBox.put(mMailBoxModel.mRecordId,mMailBoxModel);
                            }
                            else
                            {
                                MailManager.getInstance().mOtherMailBoxs.add(mMailBoxModel);
                                MailManager.getInstance().hashOtherMailBox.put(mMailBoxModel.mRecordId,mMailBoxModel);
                            }

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
        if(uid.equals(MailManager.getInstance().account.mRecordId))
        {
            return 0;
        }
        else{
            return 1;
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
        MailContact mUser = new MailContact(MailManager.getInstance().context.getString(R.string.mail_me),"");
        mUser.mRecordid = MailManager.getInstance().account.mRecordId;
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
                        mtUser.mRecordid = maildata.getString("RecordID");
                        if (mtUser.mRecordid.length() > 0) {
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

    public static void addUserc(NetObject net) {

        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
            return;

        XpxJSONObject data = null;
        MailManager.getInstance().mMailUnderlineUsers.clear();
        MailManager.getInstance().typebooleans5 = new boolean[27];
        MailContact mUser = new MailContact(MailManager.getInstance().context.getString(R.string.mail_me),"");
        mUser.mRecordid = MailManager.getInstance().account.mRecordId;
        mUser.isselect = true;
        mUser.mType = Contacts.TYPE_PERSON;
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
                XpxJSONObject jObject = new XpxJSONObject(json);
                if (jObject.has("data")) {

                    data = jObject.getJSONObject("data");
                    Iterator iterator = data.jsonObject.keys();
                    while (iterator.hasNext())
                    {
                        String  key = (String) iterator.next();
                        XpxJSONObject maildata = data.getJSONObject(key);
                        MailContact mtUser = new MailContact(maildata.getString("username"),"");
                        mtUser.mRecordid = maildata.getString("uid");
                        mtUser.mRName = maildata.getString("realname");
                        if (mtUser.mRecordid.length() > 0) {
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
            MailManager.getInstance().allcount0 = data.getInt("un_read_alls");
            MailManager.getInstance().allcount1 = data.getInt("un_read_all");
            MailManager.getInstance().me_approval = data.getInt("me_approval");
            MailManager.getInstance().to_me_approval = data.getInt("to_me_approval");
            MailManager.getInstance().allcount2 = 0;
            MailManager.getInstance().allcount4 = 0;
            MailManager.getInstance().allcount5 = 0;
            MailManager.getInstance().allcount6 = 0;
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
                MailManager.getInstance().allcount4 += jo3.getInt("count");
                MailManager.getInstance().allcount5 += jo4.getInt("count");
                MailManager.getInstance().allcount6 += jo5.getInt("count");
                MailManager.getInstance().mMailBoxs.get(i).count1 = jo1.getInt("count");
                MailManager.getInstance().mMailBoxs.get(i).count2 = jo2.getInt("count");
                MailManager.getInstance().mMailBoxs.get(i).count4 = jo3.getInt("count");
                MailManager.getInstance().mMailBoxs.get(i).count5 = jo4.getInt("count");
                MailManager.getInstance().mMailBoxs.get(i).count6 = jo5.getInt("count");
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
//                    mMailItem.lables = maildata.getJSONArray("label").toString();
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
                    XpxJSONArray jsonArray = maildata.getJSONArray("label");
                    if(jsonArray.length() > 0)
                    mMailItem.lables = jsonArray.getString(jsonArray.length()-1).toString();
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

    public static void addLDatac(NetObject net) {


        try {

            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
                return;
            MailManager.getInstance().mMyLabols.clear();
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            XpxJSONArray data = null;
            data = jsonObject.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                XpxJSONObject maildata = data.getJSONObject(i);
                Select mMailItem = new Select(maildata.getString("record_id"),
                        maildata.getString("name"));
                mMailItem.mColor = maildata.getString("color");
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

    public static boolean praseMailFiles(NetObject net) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
            return false;


        MailManager.getInstance().mailFiles.clear();

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray data = jsonObject.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                JSONObject maildata = data.getJSONObject(i);
                MailFile mailFile = new MailFile();
                mailFile.rid = maildata.getString("_id");
                mailFile.userid = maildata.getString("user_id");
                mailFile.name = maildata.getString("name");
                MailManager.getInstance().mailFiles.add(mailFile);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return true;
    }

    public static boolean praseRship(NetObject net) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
            return false;

        JSONObject jsonObject = null;
        JSONArray module1 = new JSONArray();
        try {
            jsonObject = new JSONObject(json);
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray module = data.getJSONArray("module");
            JSONArray order = data.getJSONArray("order");
            JSONObject tree = data.getJSONObject("tree");
            for(int i = 0 ; i < module.length() ; i++)
            {
                JSONObject jo = module.getJSONObject(i);
                module1.put(jo.get("name"));
            }
            MailManager.getInstance().module = module1;
            MailManager.getInstance().order = order;

            Iterator iterator = tree.keys();
            int leave = -1;
            String group = "";
            int maxleave = -1;
            boolean hasemail = false;
            while(iterator.hasNext()){
              String  key = (String) iterator.next();
              if(key.equals("Email"))
              {
                  hasemail = true;
              }
              if(group.length() == 0)
              {
                  group = key;
                  leave = tree.getInt(key);
              }
              else
              {
                  if(leave > tree.getInt(key))
                  {
                      group = key;
                      leave = tree.getInt(key);
                  }
              }
              if(maxleave < tree.getInt(key))
              {
                  maxleave = tree.getInt(key);
              }
              MailManager.getInstance().tree.put(String.valueOf(tree.getInt(key)),key);
            }
            if(hasemail == false)
            {
                MailManager.getInstance().tree.put(String.valueOf(maxleave+1),"Email");
                MailManager.getInstance().tree.put(String.valueOf(maxleave+2),"item");
            }
            else
            {
                MailManager.getInstance().tree.put(String.valueOf(maxleave+1),"item");
            }
            MailManager.getInstance().mailType.leave = leave;
            MailManager.getInstance().mailType.group = group;
            MailManager.getInstance().mailType.typename = MailManager.getInstance().context.getString(R.string.keyword_typetitle);
            JSONObject jsonObject1 = new JSONObject();
            MailManager.getInstance().mailType.where = jsonObject1;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void praseAllSend(NetObject net){
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
            return;
        try {
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            if(jsonObject.has("data"))
            {
                XpxJSONArray ja = jsonObject.getJSONArray("data");
                for(int i = 0 ; i < ja.length() ; i++)
                {
                    XpxJSONObject jo = ja.getJSONObject(i);
                    MailBox mailBox = new MailBox();
                    mailBox.mRecordId = jo.getString("_id","");
                    mailBox.mAddress = jo.getString("account_name");
                    mailBox.mFullAddress = jo.getString("account_nick");
                    MailManager.getInstance().allSendMailBoxs.add(mailBox);
                    if(i == 0)
                    {
                        mailBox.isSelect = true;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static boolean praseRead(NetObject net,boolean read) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
            return false;
        ArrayList<Mail> mails = (ArrayList<Mail>) net.item;
        for(int i = 0 ; i < mails.size() ; i++)
        {
            mails.get(i).isReaded = read;
        }
        return true;

    }

    public static boolean praseReadAll(NetObject net) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
            return false;
        return true;

    }

    public static boolean praseApprove(NetObject net) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
            return false;
        return true;

    }

    public static boolean praseReply(NetObject net,boolean repeat) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
            return false;
        ArrayList<Mail> mails = (ArrayList<Mail>) net.item;
        for(int i = 0 ; i < mails.size() ; i++)
        {
            mails.get(i).isRepeat = repeat;
        }
        return true;

    }

    public static boolean praseLableDel(NetObject net) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
            return false;

        return true;

    }

    public static boolean praseFileDel(NetObject net) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
            return false;

        return true;

    }

    public static Select praseLableUpdata(NetObject net) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
            return null;

        try {
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            XpxJSONObject data = new XpxJSONObject(jsonObject.getString("data"));
            Select select = (Select) net.item;
            select.mId = data.getString("_id");
            select.mColor = data.getString("color");
            select.mName = data.getString("title");
            return select;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static boolean praseMove(NetObject net) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
            return false;

        return true;

    }

    public static MailFile praseFileUpdata(NetObject net) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
            return null;

        try {
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            XpxJSONObject data = jsonObject.getJSONObject("data");
            XpxJSONObject item = new XpxJSONObject(data.getString("item"));
            MailFile select = (MailFile) net.item;
            select.rid = item.getString("_id");
            select.name = item.getString("name");
            return select;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static boolean praseCustoms(NetObject net) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
            return false;
        MailPresenter.Mailboxobj mailboxobj  = (MailPresenter.Mailboxobj) net.item;
        MailType mailType = (MailType) mailboxobj.object;
        try {
             XpxJSONObject jsonObject = new XpxJSONObject(json);
            XpxJSONObject data = jsonObject.getJSONObject("data");
            XpxJSONArray ja = data.getJSONArray("data");
            int leave = mailType.leave+1;
            String grop = MailManager.getInstance().tree.get(String.valueOf(leave));
            for(int i = 0 ; i < ja.length() ; i++)
            {
                XpxJSONObject jo = ja.getJSONObject(i);
                MailType item = new MailType();
                item.group = grop;
                item.leave = leave;
                item.typename = jo.getString(mailType.group);
                if(item.typename != null)
                {
                    if(!item.typename.equals("null") && item.typename.length() > 0 )
                    {
                        JSONObject where = new JSONObject(mailType.where.toString());
                        where.put(mailType.group,item.typename);
                        item.where = where;
                        mailType.mailTypes.add(item);
                        if(item.group.equals("item"))
                        {
                            item.isMail = true;
                            if(mailboxobj.mailboxid.length() == 0)
                            {
                                mailboxobj.mailboxid +=  item.typename;
                            }
                            else
                            {
                                mailboxobj.mailboxid += (","+ item.typename);
                            }
                        }
                    }
                }

            }
            if(mailType.group.equals("Email"))
            {
                return true;
            }
            else
            {
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }


    }

    public static boolean praseCustoms2(NetObject net) {
        String json = net.result;
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        if(AppUtils.success(json) == false)
            return false;
        MailContact mailboxobj  = (MailContact) net.item;
        MailType mailType = (MailType) mailboxobj.mailType;
        mailboxobj.mContacts.clear();
        mailboxobj.mHeadContacts.clear();
        mailboxobj.mMyContacts.clear();
        try {
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            XpxJSONObject data = jsonObject.getJSONObject("data");
            XpxJSONArray ja = data.getJSONArray("data");
            int leave = mailType.leave+1;
            String grop = MailManager.getInstance().tree.get(String.valueOf(leave));
            for(int i = 0 ; i < ja.length() ; i++)
            {
                XpxJSONObject jo = ja.getJSONObject(i);
                MailContact mailContact = new MailContact();
                MailType item = new MailType();
                item.group = grop;
                item.leave = leave;
                item.typename = jo.getString(mailType.group);
                mailContact.mailType = item;
                mailContact.parent = mailboxobj;
                mailContact.setmName(item.typename);
                mailContact.mType = Contacts.TYPE_CLASS;
                if(item.typename != null)
                {
                    if(!item.typename.equals("null") && item.typename.length() > 0 )
                    {
                        mailboxobj.mMyContacts.add(mailContact);
                        JSONObject where = new JSONObject(mailType.where.toString());
                        where.put(mailType.group,item.typename);
                        item.where = where;
                        mailType.mailTypes.add(item);
                        if(item.group.equals("item"))
                        {
                            item.isMail = true;
                            mailContact.mailAddress = mailType.typename;
                            mailContact.mType = Contacts.TYPE_PERSON;
                        }
                        if(mailContact.mType == Contacts.TYPE_PERSON)
                        {
                            String s = mailContact.getmPingyin().substring(0, 1).toUpperCase();
                            int pos = CharacterParser.typeLetter.indexOf(s);
                            if (pos != -1) {
                                if (mailboxobj.typebooleans[pos] == false) {
                                    mailboxobj.mHeadContacts.add(new MailContact(s,Contacts.TYPE_LETTER));
                                    mailboxobj.typebooleans[pos] = true;
                                }
                            }
                            else
                            {
                                s = "#";
                                pos = CharacterParser.typeLetter.indexOf(s);
                                if (pos != -1) {
                                    if (mailboxobj.typebooleans[pos] == false) {
                                        mailboxobj.mHeadContacts.add(new MailContact(s,Contacts.TYPE_LETTER));
                                        mailboxobj.typebooleans[pos] = true;
                                    }
                                }
                            }
                        }
                    }
                }

            }
            mailboxobj.mMyContacts.addAll(0, mailboxobj.mHeadContacts);
            Collections.sort(mailboxobj.mMyContacts, new SortContactComparator());
            Collections.sort(mailboxobj.mHeadContacts, new SortContactComparator());
            mailboxobj.mContacts.addAll(mailboxobj.mMyContacts);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }


    }

    public static void addGLData(NetObject net) {


        try {

            String json = net.result;
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
                return;
            MailManager.getInstance().mGropLabols.clear();
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            XpxJSONArray data = jsonObject.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                XpxJSONObject maildata = data.getJSONObject(i);
                Select mMailItem = new Select(maildata.getString("_id"),
                        maildata.getString("tag_name"));
                MailManager.getInstance().mGropLabols.add(mMailItem);
            }

        }
        catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static String measureHtml(String html) {
        if(MailManager.getInstance().service.https == false)
            return html.replaceAll("/img","http://" + MailManager.getInstance().service.sAddress+"/image/"+MailManager.getInstance().account.mCompanyId);
        else
            return html.replaceAll("/img","https://" + MailManager.getInstance().service.sAddress+"/image/"+MailManager.getInstance().account.mCompanyId);
    }


    public static int praseContacts(XpxJSONObject xpxJsonObject, MailContact mContactsClass, Context context) {
        int sum = 0;
        try {
            XpxJSONArray ja = xpxJsonObject.getJSONArray("orgs");
            XpxJSONArray ja2 = xpxJsonObject.getJSONArray("users");
            String name = xpxJsonObject.getString("name");
            MailContact mContacts;
            if (name.equals("(Root)")) {
                name = context.getString(R.string.company);
            }
            mContacts = new MailContact();
            mContacts.setName(name);
            mContacts.parent = mContactsClass;
            mContacts.mId = xpxJsonObject.getString("orgid");
            mContacts.mRecordid = xpxJsonObject.getString("orgid");
            mContacts.mType = Contacts.TYPE_CLASS;
            mContacts.type = Contacts.TYPE_CLASS;
            MailManager.getInstance().mOrganization.mAllMailContactDepartMap.put(mContacts.mRecordid,mContacts);
            mContactsClass.mContacts.add(mContacts);



            for (int i = 0; i < ja.length(); i++) {

                XpxJSONObject jo = (XpxJSONObject) ja.getJSONObject(i);
                int count = praseContacts(jo, mContacts,context);
                sum += count;
            }


            for (int i = 0; i < ja2.length(); i++) {
                XpxJSONObject jo22 = ja2.getJSONObject(i);
                MailContact mContacts2;
                String RecordID = "";
                RecordID = jo22.getString("userid");
                XpxJSONObject jo2 = getUserinfo(RecordID);
                if (jo2 == null) {
                    jo2 = jo22;
                }
                String ismale = context.getString(R.string.unknow);
//                if (jo2.has("sex")) {
//                    if (jo2.getInt("sex",0) == 0) {
//                        ismale = context.getString(R.string.male);
//                    } else if (jo2.getInt("sex",0) == 1) {
//                        ismale = context.getString(R.string.femal);
//                    }
//                }
                String id = "";
                String text = "";
                String Phone = "";
                String Mobile = "";
                String Fax = "";
                String Email = "";
                String position = "";
                id = jo2.getString("username");
                if(id.length() == 0)
                {
                    id = "未知";
                }
                if (jo2.getString("realname") != null) {
                    if (jo2.getString("realname").length() > 0) {
                        text = jo2.getString("realname");
                    } else
                        text = id;
                } else
                    text = id;

                Phone = jo2.getString("phone");
                Mobile = jo2.getString("mobile");
                Fax = jo2.getString("fax");
                Email = jo2.getString("email");
                position = jo2.getString("position");

                mContacts2 = new MailContact();
                mContacts2.setName(id);
                mContacts2.mId = id;
                mContacts2.parent = mContactsClass;
                mContacts2.mRName = text;
                mContacts2.mailAddress = Email;
                mContacts2.mRecordid = RecordID;
                mContacts2.mType = Contacts.TYPE_PERSON;
                mContacts2.type = Contacts.TYPE_PERSON;
                MailManager.getInstance().mOrganization.allMailContact.add(mContacts2);
                MailManager.getInstance().mOrganization.mAllMailContactMap.put(mContacts2.mRecordid,mContacts2);
                mContacts.mMyContacts.add(mContacts2);

                String s = mContacts2.getmPingyin().substring(0, 1).toUpperCase();
                int pos = CharacterParser.typeLetter.indexOf(s);
                if (pos != -1) {
                    if (mContacts.typebooleans[pos] == false) {
                        mContacts.mHeadContacts.add(new MailContact(s,Contacts.TYPE_LETTER));
                        mContacts.typebooleans[pos] = true;
                    }
                }
                else
                {
                    s = "#";
                    pos = CharacterParser.typeLetter.indexOf(s);
                    if (pos != -1) {
                        if (mContacts.typebooleans[pos] == false) {
                            mContacts.mHeadContacts.add(new MailContact(s,Contacts.TYPE_LETTER));
                            mContacts.typebooleans[pos] = true;
                        }
                    }
                }
                sum++;

            }
            mContacts.mMyContacts.addAll(0, mContacts.mHeadContacts);
            Collections.sort(mContacts.mMyContacts, new SortContactComparator());
            Collections.sort(mContacts.mHeadContacts, new SortContactComparator());
            mContacts.mContacts.addAll(mContacts.mMyContacts);
//            mContacts.mPersonCount = sum;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sum;
    }

    public static XpxJSONObject getUserinfo(String id) {

        XpxJSONObject jo = null;
        try {
            for (int i = 0; i < MailManager.getInstance().mUsers.length(); i++) {
                jo = MailManager.getInstance().mUsers.getJSONObject(i);
                if (jo.getString("userid").equals(id)) {
                    return jo;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
