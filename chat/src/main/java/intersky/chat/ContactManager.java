package intersky.chat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import intersky.appbase.BaseActivity;
import intersky.appbase.PermissionCode;
import intersky.appbase.entity.Account;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.ModuleDetial;
import intersky.apputils.AppUtils;
import intersky.apputils.CharacterParser;
import intersky.apputils.GlideConfiguration;
import intersky.chat.view.activity.ContactsDetialActivity;
import intersky.chat.view.activity.ContactsListActivity;
import intersky.chat.view.activity.ContactsListSelectActivity;
import intersky.filetools.FileUtils;
import intersky.json.XpxJSONArray;
import intersky.json.XpxJSONObject;
import xpx.map.MapManager;

public class ContactManager {

    public final static String GET_LOCAL_CONTACTS_FINISH = "GET_LOCAL_CONTACTS_FINISH";
    public static final int SELECT_FIRST_ALL = 1;
    public static final String ACTION_SEND_IM = "ACTION_SEND_IM";
    public Account mAccount;
    public String updataKey = "";
    public Organization mOrganization = new Organization();
    public HashMap<String,Contacts> contactsHashMap = new HashMap<String,Contacts>();
    public HashMap<String,Contacts> friendHashMap = new HashMap<String,Contacts>();
    public HashMap<String,ArrayList<Contacts>> friendHeadHashMap = new HashMap<String,ArrayList<Contacts>>();
    public HashMap<String,Contacts> searchHashMap = new HashMap<String,Contacts>();
    public ModuleDetial searchPage = new ModuleDetial();
    public ModuleDetial contactPage = new ModuleDetial();
    public ModuleDetial friendPage = new ModuleDetial();
    public ArrayList<Contacts> mContactssearch = new ArrayList<Contacts>();
    public ArrayList<Contacts> mContactss = new ArrayList<Contacts>();
    public ArrayList<Contacts> mContactsfs = new ArrayList<Contacts>();
    public HashMap<String,Contacts> mContactsFHashHead = new HashMap<String,Contacts>();
    public ArrayList<Contacts> mContactsFHead = new ArrayList<Contacts>();
    public ArrayList<Contacts> mContactsFall = new ArrayList<Contacts>();
    public boolean[] typeboolfriend = new boolean[27];
    public XpxJSONArray mUsers;
    public Context context;
    public volatile static ContactManager mContactManager;

    public static ContactManager init(Context context) {

        if (mContactManager == null) {
            synchronized (ContactManager.class) {
                if (mContactManager == null) {
                    mContactManager = new ContactManager(context);
                }
                else
                {
                    mContactManager.context = context;
                }
            }
        }
        return mContactManager;
    }

    public void updataContactHeads() {
        HashMap<String,String> heads = new HashMap<String,String>();
        for (String key  : friendHashMap.keySet()) {
            if(!heads.containsKey(key))
            {
                heads.put(key,friendHashMap.get(key).mRecordid);
            }
        }
        for (String key  : contactsHashMap.keySet()) {
            heads.put(key,contactsHashMap.get(key).mRecordid);
        }


    }

    public ContactManager(Context context) {
        this.context = context;
        SharedPreferences sharedPre = context.getSharedPreferences("contacts", 0);
        this.updataKey = sharedPre.getString("updata","");
        if(this.updataKey.length() == 0)
        {
            if(GlideConfiguration.mGlideConfiguration != null)
            {
                FileUtils.delFile2(GlideConfiguration.mGlideConfiguration.fileCache.getPath()+"/"+GlideConfiguration.DISK_CACHE_NAME);
            }
            this.updataKey = String.valueOf(System.currentTimeMillis());
            SharedPreferences.Editor e1 = sharedPre.edit();
            e1.putString("updata",this.updataKey);
            e1.commit();
        }
        else
        {
            if(System.currentTimeMillis() - Long.valueOf(this.updataKey) > 5*60*1000 )
            {
                if(GlideConfiguration.mGlideConfiguration != null)
                {
                    FileUtils.delFile2(GlideConfiguration.mGlideConfiguration.fileCache.getPath()+"/"+GlideConfiguration.DISK_CACHE_NAME);
                }
                this.updataKey = String.valueOf(System.currentTimeMillis());
                SharedPreferences.Editor e1 = sharedPre.edit();
                e1.putString("updata",this.updataKey);
                e1.commit();
            }
        }
    }

    public void updataKey(Context context)
    {
        SharedPreferences sharedPre = context.getSharedPreferences("contacts", 0);
        if(System.currentTimeMillis() - Long.valueOf(this.updataKey) > 5*60*1000 )
        {
            if(GlideConfiguration.mGlideConfiguration != null)
            {
                FileUtils.delFile2(GlideConfiguration.mGlideConfiguration.fileCache.getPath()+"/"+GlideConfiguration.DISK_CACHE_NAME);
            }
            this.updataKey = String.valueOf(System.currentTimeMillis());
            SharedPreferences.Editor e1 = sharedPre.edit();
            e1.putString("updata",this.updataKey);
            e1.commit();
        }


    }


    public void updataKeyEX(Context context)
    {
        SharedPreferences sharedPre = context.getSharedPreferences("contacts", 0);
        if(GlideConfiguration.mGlideConfiguration != null)
        {
            FileUtils.delFile2(GlideConfiguration.mGlideConfiguration.fileCache.getPath()+"/"+GlideConfiguration.DISK_CACHE_NAME);
        }
        this.updataKey = String.valueOf(System.currentTimeMillis());
        SharedPreferences.Editor e1 = sharedPre.edit();
        e1.putString("updata",this.updataKey);
        e1.commit();


    }
    public void setAccount(Account account) {
        cleanAll();
        this.mAccount = account;
    }

    public void cleanAll()
    {
        contactsHashMap.clear();
        friendHashMap.clear();
        friendHeadHashMap.clear();
        searchHashMap.clear();
        mContactssearch.clear();
        mContactss.clear();
        mContactsfs.clear();
        mContactsFHashHead.clear();
        mContactsFHead.clear();
        mContactsFall.clear();
        typeboolfriend = new boolean[27];
    }

    public void removeFriend(Contacts contacts) {
        friendHashMap.remove(contacts);
        mContactsfs.remove(contacts);
        mContactsFall.remove(contacts);
        String s = contacts.getmPingyin().substring(0, 1).toUpperCase();
        ArrayList<Contacts> contacts1 = friendHeadHashMap.get(s);
        int pos = CharacterParser.typeLetter.indexOf(s);
        if(contacts1 != null)
        {
            if(contacts1.size() == 0)
            {
                if (pos != -1) {
                    if (typeboolfriend[pos] == true) {
                        Contacts contactsh = mContactsFHashHead.get(s);
                        if(contactsh != null)
                        {
                            mContactsFHashHead.remove(contactsh);
                            mContactsFHead.remove(contactsh);
                            mContactsFall.remove( contactsh);
                        }
                        typeboolfriend[pos] = false;
                    }
                }
            }
        }
        else
        {
            s = "#";
            pos = CharacterParser.typeLetter.indexOf(s);
            if (pos != -1) {
                if (typeboolfriend[pos] == false) {
                    Contacts contactsh = mContactsFHashHead.get(s);
                    if(contactsh != null)
                    {
                        mContactsFHashHead.remove(contactsh);
                        mContactsFHead.remove(contactsh);
                        mContactsFall.remove(contactsh);
                    }
                    typeboolfriend[pos] = false;
                }
            }
        }
        Collections.sort(mContactsFall, new SortContactComparator());
        Collections.sort(mContactsfs, new SortContactComparator());
        Collections.sort(mContactsFHead, new SortContactComparator());
    }

    public void addFriend(Contacts contacts) {
        friendHashMap.put(contacts.mRecordid,contacts);
        mContactsfs.add(contacts);
        mContactsFall.add(contacts);
        String s = contacts.getmPingyin().substring(0, 1).toUpperCase();
        ArrayList<Contacts> hContacts = friendHeadHashMap.get(s);
        if(hContacts == null)
        {
            hContacts = new ArrayList<Contacts>();
            friendHeadHashMap.put(s,hContacts);
        }
        hContacts.add(contacts);
        int pos = CharacterParser.typeLetter.indexOf(s);
        if (pos != -1) {
            if (typeboolfriend[pos] == false) {
                Contacts contactsh = new Contacts(s);
                mContactsFHead.add(contactsh);
                mContactsFHashHead.put(s,contactsh);
                mContactsFall.add(0, contactsh);
                typeboolfriend[pos] = true;
            }
        }
        else
        {
            s = "#";
            pos = CharacterParser.typeLetter.indexOf(s);
            if (pos != -1) {
                if (typeboolfriend[pos] == false) {
                    Contacts contactsh = new Contacts(s);
                    mContactsFHashHead.put(s,contactsh);
                    mContactsFHead.add(contactsh);
                    mContactsFall.add(0, contactsh);
                    typeboolfriend[pos] = true;

                }
            }
        }
        Collections.sort(mContactsFall, new SortContactComparator());
        Collections.sort(mContactsfs, new SortContactComparator());
        Collections.sort(mContactsFHead, new SortContactComparator());
    }



    public void initContacts() {
        mOrganization.clean();
        initCompanyContacts();
        praseUnderline();
    }

    private void initCompanyContacts() {
        mOrganization.organizationContacts.setName(context.getString(R.string.main_org));
        mOrganization.organizationContacts.mId = "baseroot";
        mOrganization.organizationContacts.mType = Contacts.TYPE_CLASS;
        ContactManager.mContactManager.mOrganization.mAllContactsDepartMap.put(mOrganization.organizationContacts.mRecordid,mOrganization.organizationContacts);
        mOrganization.allClassContacts.add(mOrganization.organizationContacts);
        try {
            XpxJSONObject jo = (XpxJSONObject) mAccount.project.getJSONObject("orgs");
            mUsers = mAccount.project.getJSONArray("users");
            ContactsPrase.praseContacts(jo, mOrganization.organizationContacts,context);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initLocalContacts();
    }

    private void initLocalContacts() {
        mOrganization.typebooleans3 = new boolean[27];
        for (int i = 0; i < mOrganization.allContacts.size(); i++) {
            Contacts mContactModel2 = mOrganization.allContacts.get(i);
            String s = mContactModel2.getmPingyin().substring(0, 1).toUpperCase();
            int pos = CharacterParser.typeLetter.indexOf(s);
            if (pos != -1) {
                if (mOrganization.typebooleans3[pos] == false) {
                    mOrganization.allContactsHead.add(new Contacts(s));
                    mOrganization.typebooleans3[pos] = true;
                }
            }
            else
            {
                s = "#";
                pos = CharacterParser.typeLetter.indexOf(s);
                if (pos != -1) {
                    if (mOrganization.typebooleans3[pos] == false) {
                        mOrganization.allContactsHead.add(new Contacts(s));
                        mOrganization.typebooleans3[pos] = true;
                    }
                }
            }
        }
        mOrganization.allContacts.addAll(0, mOrganization.allContactsHead);
        Collections.sort(mOrganization.allContacts, new SortContactComparator());
        Collections.sort(mOrganization.allContactsHead, new SortContactComparator());
        mOrganization.allClassContacts.addAll(mOrganization.allContacts);
    }

    public GetPhoneContactsPremissionResult getPhoneContacts(Activity context)
    {

        LocalContactsHandler mLocalContactsHandler = new LocalContactsHandler(context);
        AppUtils.getPermission(Manifest.permission.READ_CONTACTS,context, PermissionCode.PERMISSION_REQUEST_READ_CONTACTS,mLocalContactsHandler);
        return new GetPhoneContactsPremissionResult(context,mLocalContactsHandler);
    }


    public void addPhoneContacts(Contacts mContacts) {
        if(mOrganization.allClassContacts.size() > 0)
        {
            if(mOrganization.allClassContacts.size() == 1)
            {
                Contacts contacts = mOrganization.allClassContacts.get(0);
                if(contacts.getName().equals(mContacts.getName()) && contacts.mRecordid.equals(mContacts.mRecordid))
                {

                }
                else
                {
                    mOrganization.allClassContacts.add(1,mContacts);
                }
            }
            else
            {
                Contacts contacts = mOrganization.allClassContacts.get(0);
                if(contacts.getName().equals(mContacts.getName()) && contacts.mRecordid.equals(mContacts.mRecordid))
                {

                }
                else
                {
                    Contacts contacts2 = mOrganization.allClassContacts.get(1);
                    if(contacts2.getName().equals(mContacts.getName()) && contacts2.mRecordid.equals(mContacts.mRecordid))
                    {

                    }
                    else
                    {
                        mOrganization.allClassContacts.add(1,mContacts);
                    }
                }
            }

        }
        else
        {
            mOrganization.allClassContacts.add(mContacts);
        }

    }

    public void praseUnderline() {
        try {
            XpxJSONArray users = mAccount.project.getJSONArray("underlings");
            for (int i = 0; i < users.length(); i++) {
                XpxJSONObject maildata = users.getJSONObject(i);
                Contacts mContactModel2 = mOrganization.getContact(maildata.getString("userid"),context);
                mOrganization.underLineContacts.add(mContactModel2);
                String s = mContactModel2.getmPingyin().substring(0, 1).toUpperCase();
                int pos = CharacterParser.typeLetter.indexOf(s);
                if (pos != -1) {
                    if (mOrganization.typebooleans4[pos] == false) {
                        mOrganization.underLineContactsHead.add(new Contacts(s));
                        mOrganization.typebooleans4[pos] = true;
                    }
                }
                else
                {
                    s = "#";
                    pos = CharacterParser.typeLetter.indexOf(s);
                    if (pos != -1) {
                        if (mOrganization.typebooleans4[pos] == false) {
                            mOrganization.underLineContactsHead.add(new Contacts(s));
                            mOrganization.typebooleans4[pos] = true;
                        }
                    }
                }
            }
            mOrganization.underLineContacts.addAll(0, mOrganization.underLineContactsHead);
            Collections.sort(mOrganization.underLineContacts, new SortContactComparator());
            Collections.sort(mOrganization.underLineContactsHead, new SortContactComparator());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    public static void setContactCycleHead(TextView mhead, Contacts mContacts) {
//        String s;
//        if(mContacts.getName().length() > 0)
//        {
//            if(mContacts.getName().length() > 2)
//            {
//                s = mContacts.getName().substring(0,2);
//                if(AppUtils.strlen(s))
//                {
//                    mhead.setText(mContacts.getName().substring(0,1));
//                }
//                else
//                {
//                    mhead.setText(mContacts.getName().substring(0,2));
//                }
//            }
//            else
//            {
//                s = mContacts.getName().toString();
//                if(AppUtils.strlen(s))
//                {
//                    mhead.setText(mContacts.getName().substring(0,1));
//                }
//                else
//                {
//                    mhead.setText(mContacts.getName());
//                }
//            }
//        }
//        if(mContacts.colorhead == -1)
//        {
//            Random random = new Random();
//            int ranColor = 0xff000000 | random.nextInt(0x00ffffff);
//            mContacts.colorhead = ranColor;
//        }
//        GradientDrawable myGrad = (GradientDrawable)mhead.getBackground();
//        myGrad.setColor(mContacts.colorhead);
//    }

    public static void setContactCycleHead(TextView mhead, Contacts mContacts) {
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




    public boolean chemckLock(Contacts mContact )
    {
        for(int i = 0 ; i < mOrganization.mlocked.size() ; i++)
        {
            if(mContact.mRecordid.equals( mOrganization.mlocked.get(i).mRecordid))
            {
                return true;
            }
        }
        return  false;
    }

    public boolean checkLocked(Contacts item)
    {
        for(int i = 0 ; i < mOrganization.mlocked.size() ; i++)
        {
            if(mOrganization.mlocked.get(i).mRecordid.equals(item.mRecordid))
            {
                return true;
            }
        }
        return false;
    }

    public static void setContacts(Context mContext,ArrayList<Contacts> contacts,ArrayList<Contacts> hcontacts,String title,String action)
    {
        Contacts contactModel = new Contacts("baseroot",title);
        contactModel.mHeadContacts.addAll(hcontacts);
        contactModel.mMyContacts.addAll(contacts);
        contactModel.mContacts.addAll(contacts);
        mContactManager.mOrganization.showContacts = contactModel;
        Intent intent = new Intent(mContext,ContactsListActivity.class);
        intent.putExtra("onSelect",true);
        intent.setAction( action);
        mContext.startActivity(intent);
    }

    public static void setUnderlineContacts(Context mContext,String title,String action)
    {
        Contacts contactModel = new Contacts("baseroot",title);
        contactModel.mHeadContacts.addAll(mContactManager.mOrganization.underLineContactsHead);
        contactModel.mMyContacts.addAll(mContactManager.mOrganization.underLineContacts);
        contactModel.mContacts.addAll(mContactManager.mOrganization.underLineContacts);
        mContactManager.mOrganization.showContacts = contactModel;
        Intent intent = new Intent(mContext,ContactsListActivity.class);
        intent.putExtra("onSelect",true);
        intent.setAction( action);
        mContext.startActivity(intent);
    }



    public void startContactDetial(Context context,Contacts mContacts) {
        Intent intent = new Intent(context, ContactsDetialActivity.class);
        intent.putExtra("contacts",mContacts);
        mContactManager.mOrganization.showContacts = mContacts;
        context.startActivity(intent);
    }

    public void startContactList(Context context,Contacts mContacts) {
        Intent intent = new Intent(context, ContactsListActivity.class);
        mContactManager.mOrganization.showContacts = mContacts;
        context.startActivity(intent);
    }

    public void selectListContact(Context context,ArrayList<Contacts> selects,String action,String title,boolean all) {
        mContactManager.mOrganization.showContacts = mContactManager.mOrganization.organizationContacts;
        for (Contacts mContacts :  mContactManager.mOrganization.mselectitems) {
            mContacts.isselect = false;
        }
        mContactManager.mOrganization.mlocked.clear();
        mContactManager.mOrganization.mselectitems.clear();
        mContactManager.mOrganization.mselectitems.addAll(selects);
        for (Contacts mContacts : mContactManager.mOrganization.mselectitems) {
            mContacts.isselect = true;
        }
        Intent intent = new Intent(context, ContactsListSelectActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("onSelect", true);
        intent.setAction(action);
        if(all)
        intent.putExtra("type", ContactManager.SELECT_FIRST_ALL);
        context.startActivity(intent);
    }

    public void selectListAddContact(Context context,ArrayList<Contacts> selects,String action,String title,boolean all,ArrayList<Contacts> locks) {
        mContactManager.mOrganization.showContacts = mContactManager.mOrganization.organizationContacts;
        for (Contacts mContacts :  mContactManager.mOrganization.mselectitems) {
            mContacts.isselect = false;
        }
        mContactManager.mOrganization.mlocked.clear();
        mContactManager.mOrganization.mselectitems.clear();
        mContactManager.mOrganization.mlocked.addAll(locks);
        mContactManager.mOrganization.mselectitems.addAll(selects);
        for (Contacts mContacts : mContactManager.mOrganization.mselectitems) {
            mContacts.isselect = true;
        }
        Intent intent = new Intent(context, ContactsListSelectActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("onSelect", true);
        intent.setAction(action);
        if(all)
            intent.putExtra("type", ContactManager.SELECT_FIRST_ALL);
        context.startActivity(intent);
    }

    public void getContacts(String records, ArrayList<Contacts> mContactModels) {
        if (records.length() > 0) {
            String[] strs = records.split(",");
            mContactModels.clear();
            for (int i = 0; i < strs.length; i++) {
                Contacts model = mContactManager.mOrganization.getContact(strs[i],context);
                mContactModels.add(model);
            }
        }
    }

    public static String getMemberIds(ArrayList<Contacts> contacts) {
        String ids = "";
        for (int i = 0; i < contacts.size(); i++) {
            Contacts mContacts = contacts.get(i);
            if (ids.length() > 0) {
                ids += ","+mContacts.mRecordid;
            } else {
                ids += mContacts.mRecordid ;
            }

        }
        return ids;
    }
}
