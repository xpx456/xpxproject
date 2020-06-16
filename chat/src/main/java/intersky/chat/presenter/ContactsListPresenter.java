package intersky.chat.presenter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import intersky.appbase.Actions;
import intersky.appbase.Presenter;
import intersky.appbase.bus.Bus;
import intersky.apputils.AppUtils;
import intersky.apputils.CharacterParser;
import intersky.apputils.TimeUtils;
import intersky.chat.ChatUtils;
import intersky.chat.ContactManager;
import intersky.appbase.entity.Contacts;
import intersky.chat.R;
import intersky.chat.SortContactComparator;
import intersky.chat.handler.ContactListHandler;
import intersky.chat.handler.SendMessageHandler;
import intersky.chat.view.activity.ContactsDetialActivity;
import intersky.chat.view.activity.ContactsListActivity;
import intersky.chat.view.adapter.ContactsAdapter;
import intersky.appbase.entity.Conversation;
import intersky.mywidget.MySlideBar;

/**
 * Created by xpx on 2017/8/18.
 */

public class ContactsListPresenter implements Presenter {

    public ContactsAdapter mContactAdapter;
    public ArrayList<Contacts> mSearchItems = new ArrayList<Contacts>();
    public ArrayList<Contacts> mSearchHeadItems = new ArrayList<Contacts>();
    public ContactsAdapter mSearchContactAdapter;
    public boolean isShowSearch = false;
    public ContactsListActivity mContactsListActivity;
    public ContactListHandler mContactListHandler = new ContactListHandler(mContactsListActivity);
    public ContactsListPresenter(ContactsListActivity mContactsListActivity)
    {
        this.mContactsListActivity = mContactsListActivity;
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        mContactsListActivity.setContentView(R.layout.activity_contacts_list);
        mContactsListActivity.contactList = (ListView)mContactsListActivity.findViewById(R.id.contacts_List);
        mContactsListActivity.searchView = mContactsListActivity.findViewById(R.id.search);
        mContactsListActivity.msbar = (MySlideBar) mContactsListActivity.findViewById(R.id.slideBar);
        mContactsListActivity.mLetterText = (TextView) mContactsListActivity.findViewById(R.id.letter_text);
        mContactsListActivity.mRelativeLetter = (RelativeLayout) mContactsListActivity.findViewById(R.id.letter_layer);
        mContactsListActivity.msbar.setVisibility(View.INVISIBLE);
        mContactsListActivity.msbar.setOnTouchLetterChangeListenner(mContactsListActivity.mOnTouchLetterChangeListenner);
        mContactsListActivity.searchView.mSetOnSearchListener(mContactsListActivity.mOnSearchActionListener);
        mContactsListActivity.contactList.setOnScrollListener(mContactsListActivity.mOnScoll);
        mContactsListActivity.contactList.setOnItemClickListener(mContactsListActivity.onContactItemClickListener);
        mContactsListActivity.msbar.setmRelativeLayout(mContactsListActivity.mRelativeLetter);
        mContactsListActivity.msbar.setMletterView(mContactsListActivity.mLetterText);
        mContactAdapter = new ContactsAdapter(ContactManager.mContactManager.mOrganization.showContacts.mContacts,mContactsListActivity);
        mSearchContactAdapter = new ContactsAdapter(mSearchItems,mContactsListActivity);
        mContactsListActivity.contactList.setAdapter(mContactAdapter);
        updataContactView();
    }

    @Override
    public void Start() {

    }

    @Override
    public void Resume() {

    }

    @Override
    public void Pause() {

    }

    @Override
    public void Destroy() {

    }

    public void doSearch(String keyword)
    {
        if(keyword.length() == 0)
        {
            if(isShowSearch == true)
            {
                isShowSearch = false;
                updataContactView();
            }
            return;
        }
        boolean typebooleans[] = new boolean[26];
        ArrayList<Contacts> temps = new ArrayList<Contacts>();
        ArrayList<Contacts> tempheads = new ArrayList<Contacts>();
        for (int i = 0; i < ContactManager.mContactManager.mOrganization.allContacts.size(); i++) {
            Contacts mContactModel = ContactManager.mContactManager.mOrganization.allContacts.get(i);
            if (mContactModel.mType == Contacts.TYPE_PERSON) {
                if (mContactModel.getmPingyin().contains(keyword.toLowerCase()) || mContactModel.getName().contains(keyword)) {
                    temps.add(mContactModel);

                    String s = mContactModel.getmPingyin().substring(0, 1).toUpperCase();
                    int pos = CharacterParser.typeLetter.indexOf(s);
                    if (pos != -1) {
                        if (typebooleans[pos] == false) {
                            tempheads.add(new Contacts(s));
                            typebooleans[pos] = true;
                        }
                    }
                    else
                    {
                        s = "#";
                        pos = CharacterParser.typeLetter.indexOf(s);
                        if (pos != -1) {
                            if (typebooleans[pos] == false) {
                                tempheads.add(new Contacts(s));
                                typebooleans[pos] = true;
                            }
                        }
                    }
                }
            }
        }
        if (temps.size() == 0) {
            AppUtils.showMessage(mContactsListActivity, mContactsListActivity.getString(R.string.searchview_search_none));
        } else {
            mSearchItems.clear();
            mSearchHeadItems.clear();
            mSearchItems.addAll(temps);
            mSearchHeadItems.addAll(tempheads);
            mSearchItems.addAll(0, mSearchHeadItems);
            Collections.sort(mSearchItems, new SortContactComparator());
            Collections.sort(mSearchHeadItems, new SortContactComparator());
            if (ContactManager.mContactManager.mOrganization.phoneContacts != null) {
                mSearchItems.add(0, ContactManager.mContactManager.mOrganization.phoneContacts);
            }
            mSearchItems.add(0, ContactManager.mContactManager.mOrganization.organizationContacts);
            mContactsListActivity.contactList.setAdapter(mSearchContactAdapter);
            isShowSearch = true;
            updataContactView();
        }
    }

    public void LetterChange(int s) {
        Contacts model = ContactManager.mContactManager.mOrganization.allContactsHead.get(s);
        int a = ContactManager.mContactManager.mOrganization.allContacts.indexOf(model);
        mContactsListActivity.contactList.setSelectionFromTop(a, 0);

    }

    public void updataContactView()
    {

        if (mContactAdapter != null) {
            mContactAdapter.notifyDataSetChanged();
            mSearchContactAdapter.notifyDataSetChanged();
        }
        if (mContactsListActivity.msbar != null) {
            if (isShowSearch == false) {
                String[] ss = new String[ContactManager.mContactManager.mOrganization.showContacts.mHeadContacts.size()];
                for (int i = 0; i < ContactManager.mContactManager.mOrganization.showContacts.mHeadContacts.size(); i++) {
                    ss[i] = ContactManager.mContactManager.mOrganization.showContacts.mHeadContacts.get(i).getName();
                }
                mContactsListActivity.msbar.setAddletters(ss);
                mContactsListActivity.msbar.requestLayout();
                mContactsListActivity.msbar.setVisibility(View.VISIBLE);
            } else {

                String[] ss = new String[mSearchHeadItems.size()];
                for (int i = 0; i < mSearchHeadItems.size(); i++) {
                    ss[i] = mSearchHeadItems.get(i).getName();
                }
                mContactsListActivity.msbar.setAddletters(ss);
                mContactsListActivity.msbar.requestLayout();
                mContactsListActivity.msbar.setVisibility(View.VISIBLE);
            }

        }
    }

    public void doSelect(Contacts contacts)
    {
        if(contacts.mType == Contacts.TYPE_PERSON)
        {
            Intent intent = new Intent();
            intent.setAction(mContactsListActivity.getIntent().getAction());
            intent.putExtra("contacts",contacts);
            mContactsListActivity.sendBroadcast(intent);
            mContactsListActivity.finish();
        }
    }

    public void contactsClick(Contacts contacts)
    {
        if(contacts.mType == Contacts.TYPE_PERSON)
        {
            if(mContactsListActivity.getIntent().getAction() != null)
            {
                if(mContactsListActivity.getIntent().getAction().equals(ContactManager.ACTION_SEND_IM)) {
                    sendIm(contacts);
                }
                else {
                    startContactDetial(contacts);
                }
            }
            else
            {
                startContactDetial(contacts);
            }

        }
        else if(contacts.mType == Contacts.TYPE_CLASS)
        {
            startContactList(contacts);
        }
    }

    public void startContactDetial(Contacts contacts)
    {
        Intent intent = new Intent(mContactsListActivity, ContactsDetialActivity.class);
        intent.putExtra("contacts",contacts);
        ContactManager.mContactManager.mOrganization.showContacts = contacts;
        mContactsListActivity.startActivity(intent);
    }

    public void startContactList(Contacts contacts)
    {
        Intent intent = new Intent(mContactsListActivity, ContactsListActivity.class);
        ContactManager.mContactManager.mOrganization.showContacts = contacts;
        mContactsListActivity.startActivity(intent);
    }

    private void sendIm(Contacts contacts) {

        if(mContactsListActivity.getIntent().hasExtra("path")) {
            File mfile = new File(mContactsListActivity.getIntent().getStringExtra("path"));
            if(mfile.exists())
            {
                Conversation msg = new Conversation();
                msg.mType = Conversation.CONVERSATION_TYPE_MESSAGE;
                msg.mRecordId = AppUtils.getguid();
                msg.mTime = TimeUtils.getDate()+" "+TimeUtils.getTime();
                msg.mTitle = contacts.getName();
                msg.detialId = contacts.mRecordid;
                msg.sourceName = mfile.getName();
                msg.sourcePath = mfile.getPath();
                msg.isRead = true;
                msg.isSendto = true;
                if(Integer.valueOf((Integer) Bus.callData(mContactsListActivity,"filetools/getFileType",mfile.getName())) == Actions.FILE_TYPE_PICTURE)
                {
                    msg.mSubject = "[图片]";
                    msg.sourceType = Conversation.MESSAGE_TYPE_IMAGE;
                }
                else
                {
                    msg.sourceType = Conversation.MESSAGE_TYPE_FILE;
                    msg.mSubject = "[文件]";
                }
                msg.sourceSize = mfile.length();
                ChatUtils.getChatUtils().mChatFunctions.sendMessage(mContactsListActivity,msg,mfile);

            }
            else
            {
                AppUtils.showMessage(mContactsListActivity,mContactsListActivity.getString(R.string.attachment_downloadununexist));
            }
        }
        else if(mContactsListActivity.getIntent().hasExtra("json"))
        {
            sendLocation(mContactsListActivity,contacts,mContactsListActivity.getIntent().getStringExtra("json"));
        }
        else
        {
            Conversation msg = new Conversation();
            msg.mType = Conversation.CONVERSATION_TYPE_MESSAGE;
            msg.mRecordId = AppUtils.getguid();
            msg.mTime = TimeUtils.getDate()+" "+TimeUtils.getTime();
            msg.mTitle = contacts.getName();
            msg.detialId = contacts.mRecordid;
            msg.isRead = true;
            msg.isSendto = true;
            msg.sourceType = Conversation.MESSAGE_TYPE_NOMAL;
            msg.mSubject = mContactsListActivity.getIntent().getStringExtra("text");
            ChatUtils.getChatUtils().mChatFunctions.sendMessage(mContactsListActivity,msg);
        }
    }

    public void sendLocation(Context context, Contacts contacts, String json) {
        String name = "";
        String path = "";
        try {
            JSONObject jsonObject = new JSONObject(json);
            name = jsonObject.getString("locationName");
            path = jsonObject.getString("path");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        File mfile = new File(path);
        Conversation msg = new Conversation();
        msg.mType = Conversation.CONVERSATION_TYPE_MESSAGE;
        msg.mRecordId = AppUtils.getguid();
        msg.mTime = TimeUtils.getDate() + " " + TimeUtils.getTimeSecond();
        msg.mTitle = contacts.getmRName();
        msg.detialId = contacts.mRecordid;
        msg.sourcePath = path;
        msg.isRead = true;
        msg.isSendto = true;
        msg.mSubject = "位置:"+name;
        msg.sourceName = json;
        msg.sourceType = Conversation.MESSAGE_TYPE_MAP;
        if (mfile.exists()) {
            msg.sourceSize = mfile.length();
            ChatUtils.getChatUtils().mChatFunctions.sendMessage(mContactsListActivity,msg,mfile);
        } else {
            AppUtils.showMessage(context, "找不到文件");
        }
    }
}
