package com.bigwiner.android.prase;

import android.content.Context;
import android.widget.BaseAdapter;

import com.bigwiner.R;
import com.bigwiner.android.entity.Company;
import com.bigwiner.android.view.BigwinerApplication;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;

import intersky.appbase.entity.Attachment;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Conversation;
import intersky.appbase.entity.ModuleDetial;
import intersky.apputils.AppUtils;
import intersky.apputils.CharacterParser;
import intersky.chat.ChatUtils;
import intersky.chat.SortContactComparator;
import intersky.conversation.NotifictionManager;
import intersky.conversation.database.BigWinerDBHelper;
import intersky.json.XpxJSONArray;
import intersky.json.XpxJSONObject;
import intersky.xpxnet.net.NetObject;
import intersky.xpxnet.net.NetUtils;

public class ContactsPrase {

    public static boolean praseContactList(Context context, NetObject net) {
        String json = net.result;
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return false;
        }
        if(AppUtils.measureToken(json) != null) {

        }
        boolean updata = false;
        try {
            XpxJSONObject jsonObject= new XpxJSONObject(json);
            XpxJSONObject jo = jsonObject.getJSONObject("data");
            XpxJSONArray ja = jo.getJSONArray("friendlist");
            for(int i = 0 ; i < ja.length() ; i++)
            {
                XpxJSONObject jo2 = ja.getJSONObject(i);
                Contacts contacts = new Contacts();
                contacts.mPosition = jo2.getString("accreditation");
                contacts.province = jo2.getString("area");
                contacts.location = jo2.getString("area");
                contacts.typearea = jo2.getString("businessarea");
                contacts.typevalue = jo2.getString("businesstype");
                contacts.eMail = jo2.getString("email");
                contacts.mFax = jo2.getString("fax");
                contacts.mName = jo2.getString("loginName");
                contacts.des = jo2.getString("personalprofile",context.getString(R.string.des_temp));
                contacts.mMobile = jo2.getString("mobile");
                contacts.mRName = jo2.getString("name");
                contacts.mPhone = jo2.getString("phone");
                contacts.mPhone2 = jo2.getString("areacode");
                contacts.confrim = jo2.getString("authentication");
                contacts.icon = jo2.getString("logo");
                contacts.cover = jo2.getString("cover");
                contacts.mSex = jo2.getString("sex");
                if(contacts.mSex.equals("男"))
                    contacts.sex = 0;
                else
                    contacts.sex = 1;
                contacts.staue = jo2.getString("status");
                contacts.mRecordid = jo2.getString("rid");
                ChatUtils.getChatUtils().addHead(contacts);
                BigwinerApplication.mApp.contactManager.contactsHashMap.put(contacts.mRecordid,contacts);
                BigwinerApplication.mApp.contactManager.mContactss.add(contacts);
                updata = updataConversationTitle(contacts,context);
            }
            ModuleDetial moduleDetial = BigwinerApplication.mApp.contactManager.contactPage;
            moduleDetial.currentpage = jo.getInt("pageNo",1);
            moduleDetial.pagesize = jo.getInt("page_size",20);
            moduleDetial.totlepage = jo.getInt("totalPage",1);
            moduleDetial.totleszie = jo.getInt("totalcount",0);
            moduleDetial.currentszie = BigwinerApplication.mApp.contactManager.mContactss.size();

            return updata;
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return updata;
        }
    }

    public static boolean praseContactListSearch(Context context, NetObject net) {
        String json = net.result;
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return false;
        }
        if(AppUtils.measureToken(json) != null) {

        }
        boolean updata = false;
        try {
            XpxJSONObject jsonObject= new XpxJSONObject(json);
            XpxJSONObject jo = jsonObject.getJSONObject("data");
            XpxJSONArray ja = jo.getJSONArray("friendlist");
            for(int i = 0 ; i < ja.length() ; i++)
            {
                XpxJSONObject jo2 = ja.getJSONObject(i);
                Contacts contacts = new Contacts();
                contacts.mPosition = jo2.getString("accreditation");
                contacts.province = jo2.getString("area");
                contacts.location = jo2.getString("area");
                contacts.typearea = jo2.getString("businessarea");
                contacts.typevalue = jo2.getString("businesstype");
                contacts.eMail = jo2.getString("email");
                contacts.mFax = jo2.getString("fax");
                contacts.mName = jo2.getString("loginName");
                contacts.des = jo2.getString("personalprofile",context.getString(R.string.des_temp));
                contacts.mMobile = jo2.getString("mobile");
                contacts.mRName = jo2.getString("name");
                contacts.mPhone = jo2.getString("phone");
                contacts.mPhone2 = jo2.getString("areacode");
                contacts.confrim = jo2.getString("authentication");
                contacts.icon = jo2.getString("logo");
                contacts.cover = jo2.getString("cover");
                contacts.mSex = jo2.getString("sex");
                if(contacts.mSex.equals("男"))
                    contacts.sex = 0;
                else
                    contacts.sex = 1;
                contacts.staue = jo2.getString("status");
                contacts.mRecordid = jo2.getString("rid");
                ChatUtils.getChatUtils().addHead(contacts);
                BigwinerApplication.mApp.contactManager.searchHashMap.put(contacts.mRecordid,contacts);
                BigwinerApplication.mApp.contactManager.mContactssearch.add(contacts);
                updata = updataConversationTitle(contacts,context);
            }
            ModuleDetial moduleDetial = BigwinerApplication.mApp.contactManager.searchPage;
            moduleDetial.currentpage = jo.getInt("pageNo",1);
            moduleDetial.pagesize = jo.getInt("page_size",20);
            moduleDetial.totlepage = jo.getInt("totalPage",1);
            moduleDetial.totleszie = jo.getInt("totalcount",0);

            moduleDetial.currentszie = BigwinerApplication.mApp.contactManager.mContactssearch.size();

            return updata;
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return updata;
        }
    }

    public static boolean praseFriendList(Context context, NetObject net) {
        String json = net.result;
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return false;
        }
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        boolean updata = false;
        try {
            XpxJSONObject jsonObject= new XpxJSONObject(json);
            XpxJSONObject jo = jsonObject.getJSONObject("data");
            XpxJSONArray ja = jo.getJSONArray("friendlist");
            for(int i = 0 ; i < ja.length() ; i++)
            {
                XpxJSONObject jo2 = ja.getJSONObject(i);
                Contacts contacts = new Contacts();
                contacts.mPosition = jo2.getString("accreditation");
                contacts.province = jo2.getString("area");
                contacts.location = jo2.getString("area");
                contacts.typearea = jo2.getString("businessarea");
                contacts.typevalue = jo2.getString("businesstype");
                contacts.eMail = jo2.getString("email");
                contacts.mFax = jo2.getString("fax");
                contacts.mName = jo2.getString("loginName");
                contacts.des = jo2.getString("personalprofile",context.getString(R.string.des_temp));
                contacts.mMobile = jo2.getString("mobile");
                contacts.mRName = jo2.getString("name");
                contacts.mPhone = jo2.getString("phone");
                contacts.mPhone2 = jo2.getString("areacode");
                contacts.confrim = jo2.getString("authentication");
                contacts.icon = jo2.getString("logo");
                contacts.cover = jo2.getString("cover");
                contacts.mSex = jo2.getString("sex");
                contacts.staue = jo2.getString("status");
                contacts.mRecordid = jo2.getString("rid");
                ChatUtils.getChatUtils().addHead(contacts);
                if(contacts.mSex.equals("男"))
                    contacts.sex = 0;
                else if(contacts.mSex.equals("女"))
                    contacts.sex = 1;
                else
                    contacts.sex = 2;
                BigWinerDBHelper.getInstance(context).saveContacts(contacts);
                BigwinerApplication.mApp.contactManager.friendHashMap.put(contacts.mRecordid,contacts);
                BigwinerApplication.mApp.contactManager.mContactsfs.add(contacts);
                BigwinerApplication.mApp.contactManager.mContactsFall.add(contacts);
                String s = contacts.getmPingyin().substring(0, 1).toUpperCase();
                ArrayList<Contacts> hContacts = BigwinerApplication.mApp.contactManager.friendHeadHashMap.get(s);
                if(hContacts == null)
                {
                    hContacts = new ArrayList<Contacts>();
                    BigwinerApplication.mApp.contactManager.friendHeadHashMap.put(s,hContacts);
                }
                hContacts.add(contacts);
                int pos = CharacterParser.typeLetter.indexOf(s);
                if (pos != -1) {
                    if (BigwinerApplication.mApp.contactManager.typeboolfriend[pos] == false) {
                        Contacts contacth = new Contacts(s) ;
                        BigwinerApplication.mApp.contactManager.mContactsFHashHead.put(s,contacth);
                        BigwinerApplication.mApp.contactManager.mContactsFHead.add(contacth);
                        BigwinerApplication.mApp.contactManager.mContactsFall.add(contacth);
                        BigwinerApplication.mApp.contactManager.typeboolfriend[pos] = true;
                    }
                }
                else
                {
                    s = "#";
                    pos = CharacterParser.typeLetter.indexOf(s);
                    if (pos != -1) {
                        if (BigwinerApplication.mApp.contactManager.typeboolfriend[pos] == false) {
                            Contacts contacth = new Contacts(s) ;
                            BigwinerApplication.mApp.contactManager.mContactsFHashHead.put(s,contacth);
                            BigwinerApplication.mApp.contactManager.mContactsFHead.add(contacth);
                            BigwinerApplication.mApp.contactManager.mContactsFall.add(contacth);
                            BigwinerApplication.mApp.contactManager.typeboolfriend[pos] = true;
                        }
                    }
                }
                updata = updataConversationTitle(contacts,context);
            }
            Collections.sort(BigwinerApplication.mApp.contactManager.mContactsFall, new SortContactComparator());
            Collections.sort(BigwinerApplication.mApp.contactManager.mContactsFHead, new SortContactComparator());
            Collections.sort(BigwinerApplication.mApp.contactManager.mContactsfs, new SortContactComparator());
            ModuleDetial moduleDetial = BigwinerApplication.mApp.contactManager.friendPage;
            moduleDetial.currentpage = jo.getInt("pageNo",1);
            moduleDetial.pagesize = jo.getInt("page_size",1000);
            moduleDetial.totlepage = jo.getInt("totalPage",1);
            moduleDetial.totleszie = jo.getInt("totalcount",0);
            moduleDetial.currentszie = BigwinerApplication.mApp.contactManager.mContactsfs.size();

            return updata;
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return updata;
        }
    }

    public static boolean updataConversationTitle(Contacts contacts,Context context) {
        boolean updata = false;
        ArrayList<Conversation> conversations = BigwinerApplication.mApp.conversationManager.messageConversation.get(contacts.mRecordid);
        Conversation conversationh = BigwinerApplication.mApp.conversationManager.messageHConversation.get(contacts.mRecordid);
        if(conversations != null)
        {
            if(conversationh != null)
            {
                if(conversationh.mTitle.equals(contacts.mName) || conversationh.mTitle.length() == 0)
                {
                    BigWinerDBHelper.getInstance(context).setConversationMessaegTitle(contacts.mRecordid,contacts.getmRName());
                    for(int i = 0 ; i < conversations.size() ; i++)
                    {
                        conversations.get(i).mTitle = contacts.getmRName();
                    }
                    updata = true;
                }
            }
        }
        return updata;
    }

    public static void paseMeetAttContacts(Context context, NetObject net, ArrayList<Contacts> contactss,ModuleDetial moduleDetial)
    {
        String json = net.result;
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return;
        }
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        XpxJSONObject jsonObject= null;
        try {
            jsonObject = new XpxJSONObject(json);
            XpxJSONArray ja = jsonObject.getJSONArray("list");
            for(int i = 0 ; i < ja.length() ; i++)
            {
                XpxJSONObject jo2 = ja.getJSONObject(i);
                Contacts contacts = new Contacts();
                contacts.mRecordid = jo2.getString("rid");
                contacts.city = jo2.getString("area");
                contacts.typearea = jo2.getString("businessarea");
                contacts.typevalue = jo2.getString("businesstype");
                contacts.mRName = jo2.getString("name");
                contacts.mSex = jo2.getString("sex");
                contacts.confrim = jo2.getString("authentication");
                contacts.des = jo2.getString("personalprofile",context.getString(R.string.des_temp));
                contacts.icon = jo2.getString("logo");
                if(contacts.mSex.equals("男"))
                    contacts.sex = 0;
                else if(contacts.mSex.equals("女"))
                    contacts.sex = 1;
                else
                    contacts.sex = 2;
                ChatUtils.getChatUtils().addHead(contacts);
                contactss.add(contacts);
            }
            moduleDetial.currentpage = jsonObject.getInt("pageNo",1);
            moduleDetial.pagesize = jsonObject.getInt("page_size",1000);
            moduleDetial.totlepage = jsonObject.getInt("totalPage",1);
            moduleDetial.totleszie = jsonObject.getInt("totalcount",0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void paseMeetDateContacts(Context context, NetObject net, ArrayList<Contacts> contactss,ModuleDetial moduleDetial) {
        String json = net.result;
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return;
        }
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        XpxJSONObject jsonObject= null;
        try {
            jsonObject = new XpxJSONObject(json);
            XpxJSONArray ja = jsonObject.getJSONArray("list");
            for(int i = 0 ; i < ja.length() ; i++)
            {
                XpxJSONObject jo2 = ja.getJSONObject(i);
                Contacts contacts = new Contacts();
                contacts.mRecordid = jo2.getString("rid");
                contacts.typearea = jo2.getString("businessarea");
                contacts.typevalue = jo2.getString("businesstype");
                contacts.mRName = jo2.getString("name");
                contacts.city = jo2.getString("city");
                contacts.confrim = jo2.getString("authentication");
                contacts.mSex = jo2.getString("sex");
                contacts.confrim = jo2.getString("authentication");
                contacts.mType = 1;
                contacts.des = jo2.getString("personalprofile",context.getString(R.string.des_temp));
                contacts.icon = jo2.getString("logo");
                if(contacts.mSex.equals("男"))
                    contacts.sex = 0;
                else if(contacts.mSex.equals("女"))
                    contacts.sex = 1;
                else
                    contacts.sex = 2;
                ChatUtils.getChatUtils().addHead(contacts);
                contactss.add(contacts);
            }
            moduleDetial.currentpage = jsonObject.getInt("pageNo",1);
            moduleDetial.totlepage = jsonObject.getInt("totalPage",1);
            moduleDetial.totleszie = jsonObject.getInt("totalcount",0);
            moduleDetial.currentszie = BigwinerApplication.mApp.contactManager.mContactss.size();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void paseMeetApplyContacts(Context context, NetObject net, ArrayList<Contacts> contactss,ModuleDetial moduleDetial) {
        String json = net.result;
        contactss.clear();
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return;
        }
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }
        XpxJSONObject jsonObject= null;
        try {
            jsonObject = new XpxJSONObject(json);
            XpxJSONArray ja = jsonObject.getJSONArray("list");
            for(int i = 0 ; i < ja.length() ; i++)
            {
                XpxJSONObject jo2 = ja.getJSONObject(i);
                Contacts contacts = new Contacts();
                contacts.mRecordid = jo2.getString("rid");
                contacts.typearea = jo2.getString("businessarea");
                contacts.typevalue = jo2.getString("businesstype");
                contacts.mRName = jo2.getString("name");
                contacts.city = jo2.getString("city");
                contacts.confrim = jo2.getString("authentication");
                contacts.mSex = jo2.getString("sex");
                if((int) net.item == 2)
                contacts.mType = 2;
                else
                    contacts.mType = 3;
                contacts.confrim = jo2.getString("authentication");
                contacts.des = jo2.getString("personalprofile",context.getString(R.string.des_temp));
                contacts.icon = jo2.getString("logo");
                if(contacts.mSex.equals("男"))
                    contacts.sex = 0;
                else if(contacts.mSex.equals("女"))
                    contacts.sex = 1;
                else
                    contacts.sex = 2;
                if(jo2.getString("status").equals("需处理"))
                {
                    contacts.staue = context.getString(R.string.meeting_un_accept);
                }
                else
                {
                    contacts.staue = jo2.getString("status");
                }
                ChatUtils.getChatUtils().addHead(contacts);
                contactss.add(contacts);
            }
            moduleDetial.currentpage = jsonObject.getInt("pageNo",1);
            moduleDetial.totlepage = jsonObject.getInt("totalPage",1);
            moduleDetial.totleszie = jsonObject.getInt("totalcount",0);
            moduleDetial.currentszie = BigwinerApplication.mApp.contactManager.mContactss.size();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void praseCompanyMemberList(Context context, NetObject net,Company company) {
        String json = net.result;
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return;
        }
        if(AppUtils.measureToken(json) != null) {
            NetUtils.getInstance().token = AppUtils.measureToken(json);
        }

        try {
            XpxJSONObject jsonObject= new XpxJSONObject(json);
            XpxJSONObject jo = jsonObject.getJSONObject("data");
            XpxJSONArray ja = jo.getJSONArray("userlist");
            for(int i = 0 ; i < ja.length() ; i++)
            {
                XpxJSONObject jo2 = ja.getJSONObject(i);
                Contacts contacts = new Contacts();
                contacts.mPosition = jo2.getString("accreditation");
                contacts.province = jo2.getString("area");
                contacts.location = jo2.getString("area");
                contacts.typearea = jo2.getString("businessarea");
                contacts.typevalue = jo2.getString("businesstype");
                contacts.eMail = jo2.getString("email");
                contacts.mFax = jo2.getString("fax");
                contacts.mName = jo2.getString("loginName");
                contacts.des = jo2.getString("personalprofile",context.getString(R.string.des_temp));
                contacts.mMobile = jo2.getString("mobile");
                contacts.mRName = jo2.getString("name");
                contacts.mPhone = jo2.getString("phone");
                contacts.mPhone2 = jo2.getString("areacode");
                contacts.confrim = jo2.getString("authentication");
                contacts.icon = jo2.getString("logo");
                contacts.cover = jo2.getString("cover");
                contacts.mSex = jo2.getString("sex");
                contacts.staue = jo2.getString("status");
                contacts.mRecordid = jo2.getString("no");
                if(contacts.mSex.equals("男"))
                    contacts.sex = 0;
                else if(contacts.mSex.equals("女"))
                    contacts.sex = 1;
                else
                    contacts.sex = 2;
                ChatUtils.getChatUtils().addHead(contacts);
                company.contacts.add(contacts);
            }
            ModuleDetial moduleDetial = company.moduleDetial;
            moduleDetial.currentpage = jsonObject.getInt("pageNo",1);
            moduleDetial.pagesize = jsonObject.getInt("page_size",20);
            moduleDetial.totlepage = jsonObject.getInt("totalPage",1);
            moduleDetial.totleszie = jsonObject.getInt("totalcount",0);
            moduleDetial.currentszie = jsonObject.getInt("totalcount",BigwinerApplication.mApp.contactManager.mContactss.size());

            return;
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return;
        }
    }

    public static void praseContactDetial(Context context, NetObject net) {
        String json = net.result;
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return;
        }
        if(AppUtils.measureToken(json) != null) {

        }
        Contacts contacts = (Contacts) net.item;
        try {
            XpxJSONObject jsonObject= new XpxJSONObject(json);
            XpxJSONObject jo2 = jsonObject.getJSONObject("data");

            contacts.company = jo2.getString("companyName");
            contacts.address = jo2.getString("companyAddress");
            contacts.companyid = jo2.getString("companyid");
            contacts.icon = jo2.getString("logo");
            contacts.complaint = jo2.getString("complaint");
            contacts.leaves = jo2.getInt("starrating",0);
            contacts.confrim = jo2.getString("authentication");
            contacts.mPosition = jo2.getString("accreditation");
            contacts.province = jo2.getString("area");
            contacts.isadd = jo2.getBoolean("isfriend",false);
            contacts.issail = jo2.getBoolean("issailmember",false);
            contacts.typearea = jo2.getString("businessarea");
            contacts.typevalue = jo2.getString("businesstype");
            contacts.mName = jo2.getString("loginname");
            contacts.des = jo2.getString("personalprofile",context.getString(R.string.des_temp));
            contacts.city = jo2.getString("city");
            contacts.mMobile = jo2.getString("mobile");
            contacts.mRName = jo2.getString("name");
            contacts.mPhone = jo2.getString("phone");
            contacts.mPhone2 = jo2.getString("areacode");
            contacts.vip = jo2.getString("membercategory");
            contacts.cover = jo2.getString("cover");
            contacts.mSex = jo2.getString("sex");
            ChatUtils.getChatUtils().addHead(contacts);
            if(contacts.mSex.equals("男"))
                contacts.sex = 0;
            else if(contacts.mSex.equals("女"))
                contacts.sex = 1;
            else
                contacts.sex = 2;
            return;
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return;
        }
    }

    public static void praseContactHead(Context context, NetObject net) {
        String json = net.result;
        if(AppUtils.success(json) == false)
        {
            return;
        }
        if(AppUtils.measureToken(json) != null) {

        }
        Attachment attachment = (Attachment) net.item;
        try {
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            XpxJSONObject jo2 = jsonObject.getJSONObject("data");
            attachment.mUrl = BigwinerApplication.mApp.sampleChatFunctions.getHeadIcom(jo2.getString("logo"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static boolean praseContactDetial(NetObject net) {
        String json = net.result;
        if(AppUtils.success(json) == false)
        {
            return false;
        }
        Contacts contacts = (Contacts) net.item;
        try {
            XpxJSONObject jsonObject= new XpxJSONObject(json);
            XpxJSONObject jo2 = jsonObject.getJSONObject("data");

            contacts.company = jo2.getString("companyName");
            contacts.address = jo2.getString("companyAddress");
            contacts.companyid = jo2.getString("companyid");
            contacts.issail = jo2.getBoolean("issailmember",false);
            contacts.icon = jo2.getString("logo");
            contacts.cover = jo2.getString("cover");
            contacts.complaint = jo2.getString("complaint");
            contacts.leaves = jo2.getInt("starRating",0);
            contacts.confrim = jo2.getString("authentication");
            contacts.mPosition = jo2.getString("accreditation");
            contacts.province = jo2.getString("area");
            contacts.typearea = jo2.getString("businessarea");
            contacts.typevalue = jo2.getString("businesstype");
            contacts.mName = jo2.getString("loginname");
            contacts.des = jo2.getString("personalprofile",BigwinerApplication.mApp.getString(R.string.des_temp));
            contacts.city = jo2.getString("city");
            contacts.mMobile = jo2.getString("mobile");
            contacts.mRName = jo2.getString("name");
            contacts.mPhone = jo2.getString("phone");
            contacts.mPhone2 = jo2.getString("areacode");
            contacts.cover = jo2.getString("cover");
            contacts.mSex = jo2.getString("sex");
            ChatUtils.getChatUtils().addHead(contacts);
            if(contacts.mSex.equals("男"))
                contacts.sex = 0;
            else if(contacts.mSex.equals("女"))
                contacts.sex = 1;
            else
                contacts.sex = 2;
            BigwinerApplication.mApp.contactManager.contactsHashMap.put(contacts.mRecordid,contacts);
            if(contacts.object != null)
            {
                BaseAdapter baseAdapter = (BaseAdapter) contacts.object;
                if(baseAdapter != null)
                {
                    baseAdapter.notifyDataSetChanged();
                }
            }
            return true;
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return false;
        }
    }

    public static void praseCompany(Context context, NetObject net) {
        String json = net.result;
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return;
        }
        if(AppUtils.measureToken(json) != null) {

        }
        Company company = (Company) net.item;
        try {
            XpxJSONObject jsonObject= new XpxJSONObject(json);
            XpxJSONObject jo = jsonObject.getJSONObject("data");
            company.name  = jo.getString("companyname");
            company.city  = jo.getString("city");
            company.bg = jo.getString("backgroundpic");
            company.address  = jo.getString("address");
            company.country  = jo.getString("country");
            company.mail  = jo.getString("email");
            company.ename  = jo.getString("englishname");
            company.fax  = jo.getString("fax");
            company.issail  = jo.getString("issail");
            company.ischarge  = jo.getString("ischarge");
            company.icon  = jo.getString("logo");
            company.marginamount  = jo.getString("marginamount");
            company.ename  = jo.getString("englishname");
            company.phone  = jo.getString("phone");
            company.province  = jo.getString("province");
            company.taxno  = jo.getString("taxcertificate");
            company.sailno  = jo.getString("businesslicense");
            company.web  = jo.getString("website");
            company.year  = jo.getInt("joinyear",0);
            company.characteristic  = jo.getString("demo");
            company.leavel  = jo.getInt("serviceStar",0);
            return;
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return;
        }
    }

    public static boolean praseData(Context context, NetObject net) {
        String json = net.result;
        if(AppUtils.success(json) == false)
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
            return false;
        }
        else
        {
            AppUtils.showMessage(context,AppUtils.getfailmessage(json));
        }
        return true;
    }

    public static String praseDataImf(Context context, NetObject net) {
        String json = net.result;
        return AppUtils.getfailmessage(json);
    }
}
