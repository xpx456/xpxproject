package intersky.mail.presenter;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import intersky.appbase.entity.Contacts;
import intersky.mail.MailManager;
import intersky.mail.R;
import intersky.mail.asks.MailAsks;
import intersky.mail.entity.Mail;
import intersky.mail.entity.MailContact;
import intersky.mail.entity.SortMailContactComparator;
import intersky.mail.handler.MailContactsHandler;
import intersky.mail.view.activity.MailContactsActivity;
import intersky.mail.view.activity.MailEditActivity;
import intersky.mail.view.adapter.MailContactAdapter;
import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.apputils.CharacterParser;
import intersky.mywidget.MySlideBar;
import intersky.mywidget.SearchViewLayout;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class MailContactsPresenter implements Presenter {

    public MailContactAdapter mContactAdapter;
    public MailContactsHandler mMailContactsHandler;
    public ArrayList<MailContact> mSearchItems = new ArrayList<MailContact>();
    public ArrayList<MailContact> mSearchHeadItems = new ArrayList<MailContact>();
    public MailContactAdapter mSearchContactAdapter;
    public boolean isShowSearch = false;
    public MailContactsActivity mMailContactsActivity;
    public Mail mail;
    public ArrayList<MailContact> contacts = new ArrayList<MailContact>();
    public MailContact now;
    public MailContactsPresenter(MailContactsActivity mMailContactsActivity)
    {
        this.mMailContactsActivity = mMailContactsActivity;
        this.mMailContactsHandler = new MailContactsHandler(mMailContactsActivity);
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        mMailContactsActivity.setContentView(R.layout.activity_mail_contacts_list);
        if(mMailContactsActivity.getIntent().hasExtra("mail"))
            mail = mMailContactsActivity.getIntent().getParcelableExtra("mail");
        ImageView back = mMailContactsActivity.findViewById(R.id.back);
        back.setOnClickListener(mBackListener);
        TextView send = (TextView)mMailContactsActivity.findViewById(R.id.send);
        send.setOnClickListener(mMailContactsActivity.mAddListener);
        if(mail == null)
        send.setText("  "+mMailContactsActivity.getString(R.string.mail_add)+"  ");
        else
            send.setText(mMailContactsActivity.getString(R.string.buttom_btn_fengfa));
        mMailContactsActivity.contactList = (ListView)mMailContactsActivity.findViewById(R.id.contacts_List);
        mMailContactsActivity.searchView = mMailContactsActivity.findViewById(R.id.search);
        mMailContactsActivity.msbar = (MySlideBar) mMailContactsActivity.findViewById(R.id.slideBar);
        mMailContactsActivity.mLetterText = (TextView) mMailContactsActivity.findViewById(R.id.letter_text);
        mMailContactsActivity.mRelativeLetter = (RelativeLayout) mMailContactsActivity.findViewById(R.id.letter_layer);
        mMailContactsActivity.msbar.setVisibility(View.INVISIBLE);
        mMailContactsActivity.msbar.setOnTouchLetterChangeListenner(mMailContactsActivity.mOnTouchLetterChangeListenner);
        mMailContactsActivity.searchView.setDotextChange(doTextChange);
        mMailContactsActivity.contactList.setOnScrollListener(mMailContactsActivity.mOnScoll);
        mMailContactsActivity.contactList.setOnItemClickListener(mMailContactsActivity.onContactItemClickListener);
        mMailContactsActivity.msbar.setmRelativeLayout(mMailContactsActivity.mRelativeLetter);
        mMailContactsActivity.msbar.setMletterView(mMailContactsActivity.mLetterText);
        TextView title = mMailContactsActivity.findViewById(R.id.title);
        title.setText(mMailContactsActivity.getString(R.string.select_contact));

        if(mMailContactsActivity.getIntent().getIntExtra("ltype",MailEditActivity.CONTENT_TYPE_SHOUJIAN) == MailEditActivity.CONTENT_TYPE_LCC ||
                mail != null)
        {

            mContactAdapter = new MailContactAdapter(mMailContactsActivity,MailManager.getInstance().mailLContacts,true);
            if(MailManager.getInstance().mailLContacts.size() == 0)
            {
                MailManager.getInstance().initLocal();
            }
        }
        else
        {
            if(MailManager.getInstance().account.iscloud == false)
            {
                mContactAdapter = new MailContactAdapter(mMailContactsActivity,MailManager.getInstance().mailContacts,false);
            }
            else
            {
                contacts.addAll(MailManager.getInstance().mOrganization.organizationMailContact.mContacts);
                mContactAdapter = new MailContactAdapter(mMailContactsActivity,contacts,false);
            }
            if(MailManager.getInstance().mailContacts.size() == 0 && MailManager.getInstance().account.iscloud == false)
            {
                mMailContactsActivity.waitDialog.show();
                MailAsks.getOutGuest(mMailContactsActivity,mMailContactsHandler);
            }
        }
        mSearchContactAdapter = new MailContactAdapter(mMailContactsActivity,mSearchItems,false);
        mMailContactsActivity.contactList.setAdapter(mContactAdapter);
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
        boolean typebooleans[] = new boolean[27];
        ArrayList<MailContact> temps = new ArrayList<MailContact>();
        ArrayList<MailContact> tempheads = new ArrayList<MailContact>();
        for (int i = 0; i < mContactAdapter.getCount(); i++) {
            MailContact mContactModel = mContactAdapter.getItem(i);
            if (mContactModel.type == 0) {
                if (mContactModel.pingyin.contains(keyword.toLowerCase()) || mContactModel.mName.contains(keyword)) {
                    temps.add(mContactModel);

                    String s = mContactModel.pingyin.substring(0, 1).toUpperCase();
                    int pos = CharacterParser.typeLetter.indexOf(s);
                    if (pos != -1) {
                        if (typebooleans[pos] == false) {
                            tempheads.add(new MailContact(s,""));
                            typebooleans[pos] = true;
                        }
                    }
                    else
                    {
                        s = "#";
                        pos = CharacterParser.typeLetter.indexOf(s);
                        if (pos != -1) {
                            if (typebooleans[pos] == false) {
                                tempheads.add(new MailContact(s,""));
                                typebooleans[pos] = true;
                            }
                        }
                    }
                }
            }
        }
        if (temps.size() == 0) {
            AppUtils.showMessage(mMailContactsActivity, mMailContactsActivity.getString(R.string.searchview_search_none));
        } else {
            mSearchItems.clear();
            mSearchHeadItems.clear();
            mSearchItems.addAll(temps);
            mSearchHeadItems.addAll(tempheads);
            mSearchItems.addAll(0, mSearchHeadItems);
            Collections.sort(mSearchItems, new SortMailContactComparator());
            Collections.sort(mSearchHeadItems, new SortMailContactComparator());
            mMailContactsActivity.contactList.setAdapter(mSearchContactAdapter);
            isShowSearch = true;
            updataContactView();
        }
    }

    public void LetterChange(int s) {
        MailContact model = MailManager.getInstance().mLHeadMailPersonItems.get(s);
        int a = MailManager.getInstance().mailLContacts.indexOf(model);
        mMailContactsActivity.contactList.setSelectionFromTop(a, 0);

    }

    public void updataContactView()
    {

        if (mContactAdapter != null) {
            mContactAdapter.notifyDataSetChanged();
            mSearchContactAdapter.notifyDataSetChanged();
        }
        if (mMailContactsActivity.msbar != null) {
            if (isShowSearch == false) {
                if(mMailContactsActivity.getIntent().getIntExtra("ltype",MailEditActivity.CONTENT_TYPE_SHOUJIAN) == MailEditActivity.CONTENT_TYPE_LCC || mail != null)
                {
                    String[] ss = new String[MailManager.getInstance().mLHeadMailPersonItems.size()];
                    for (int i = 0; i < MailManager.getInstance().mLHeadMailPersonItems.size(); i++) {
                        ss[i] = MailManager.getInstance().mLHeadMailPersonItems.get(i).mName;
                    }
                    mMailContactsActivity.msbar.setAddletters(ss);
                    mMailContactsActivity.msbar.requestLayout();
                    mMailContactsActivity.msbar.setVisibility(View.VISIBLE);
                }
                else
                {
                    if(MailManager.getInstance().account.iscloud)
                    {
                        if(now != null)
                        {
                            String[] ss = new String[now.mHeadContacts.size()];
                            for (int i = 0; i < now.mHeadContacts.size(); i++) {
                                ss[i] = now.mHeadContacts.get(i).mName;
                            }
                            mMailContactsActivity.msbar.setAddletters(ss);
                            mMailContactsActivity.msbar.requestLayout();
                            mMailContactsActivity.msbar.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            String[] ss = new String[MailManager.getInstance().mOrganization.organizationMailContact.mHeadContacts.size()];
                            for (int i = 0; i < MailManager.getInstance().mOrganization.organizationMailContact.mHeadContacts.size(); i++) {
                                ss[i] = MailManager.getInstance().mOrganization.organizationMailContact.mHeadContacts.get(i).mName;
                            }
                            mMailContactsActivity.msbar.setAddletters(ss);
                            mMailContactsActivity.msbar.requestLayout();
                            mMailContactsActivity.msbar.setVisibility(View.VISIBLE);
                        }

                    }
                    else
                    {
                        String[] ss = new String[MailManager.getInstance().mHeadMailPersonItems.size()];
                        for (int i = 0; i < MailManager.getInstance().mHeadMailPersonItems.size(); i++) {
                            ss[i] = MailManager.getInstance().mHeadMailPersonItems.get(i).mName;
                        }
                        mMailContactsActivity.msbar.setAddletters(ss);
                        mMailContactsActivity.msbar.requestLayout();
                        mMailContactsActivity.msbar.setVisibility(View.VISIBLE);
                    }

                }

            } else {

                String[] ss = new String[mSearchHeadItems.size()];
                for (int i = 0; i < mSearchHeadItems.size(); i++) {
                    ss[i] = mSearchHeadItems.get(i).mName;
                }
                mMailContactsActivity.msbar.setAddletters(ss);
                mMailContactsActivity.msbar.requestLayout();
                mMailContactsActivity.msbar.setVisibility(View.VISIBLE);
            }

        }
    }

    public void doSelect(MailContact contacts) {
        if(MailManager.getInstance().account.iscloud)
        {
            if (contacts.mType == Contacts.TYPE_CLASS)
            {
                this.contacts.clear();
                if(contacts.mailType == null)
                {
                    now = contacts;
                    this.contacts.clear();
                    this.contacts.addAll(contacts.mContacts);
                    mContactAdapter.notifyDataSetChanged();
                    doSearch(mMailContactsActivity.searchView.getText());
                    updataContactView();
                }
                else
                {
                    if(contacts.mContacts.size() > 0)
                    {
                        now = contacts;
                        this.contacts.clear();
                        this.contacts.addAll(contacts.mContacts);
                        mContactAdapter.notifyDataSetChanged();
                        doSearch(mMailContactsActivity.searchView.getText());
                        updataContactView();
                    }
                    else
                    {
                        mMailContactsActivity.waitDialog.show();
                        MailAsks.getMailCustoms2(mMailContactsHandler,mMailContactsActivity,contacts);
                    }

                }
            }
            else if(contacts.mType == Contacts.TYPE_PERSON)
            {
                if (contacts.isselect) {
                    contacts.isselect = false;
                    MailManager.getInstance().mAdd.remove(contacts);
                } else {
                    contacts.isselect = true;
                    MailManager.getInstance().mAdd.add(contacts);
                }
                TextView send = mMailContactsActivity.findViewById(R.id.send);
                send.setOnClickListener(mMailContactsActivity.mAddListener);
                if(mail == null)
                send.setText("  "+"  "+mMailContactsActivity.getString(R.string.mail_add)+"("+ String.valueOf(MailManager.getInstance().mAdd.size())+")"+"  ");
                else
                    send.setText("  "+"  "+mMailContactsActivity.getString(R.string.buttom_btn_fengfa)+"("+ String.valueOf(MailManager.getInstance().mAdd.size())+")"+"  ");
                mContactAdapter.notifyDataSetChanged();
                mSearchContactAdapter.notifyDataSetChanged();
            }
        }
        else
        {
            if (contacts.type == 0) {
                if (contacts.isselect) {
                    contacts.isselect = false;
                    MailManager.getInstance().mAdd.remove(contacts);
                } else {
                    contacts.isselect = true;
                    MailManager.getInstance().mAdd.add(contacts);
                }
            }
            TextView send = mMailContactsActivity.findViewById(R.id.send);
            send.setOnClickListener(mMailContactsActivity.mAddListener);
            if(mail == null)
                send.setText("  "+"  "+mMailContactsActivity.getString(R.string.mail_add)+"("+ String.valueOf(MailManager.getInstance().mAdd.size())+")"+"  ");
            else
                send.setText("  "+"  "+mMailContactsActivity.getString(R.string.buttom_btn_fengfa)+"("+ String.valueOf(MailManager.getInstance().mAdd.size())+")"+"  ");
            mContactAdapter.notifyDataSetChanged();
            mSearchContactAdapter.notifyDataSetChanged();
        }

    }

    public void hidContacts(MailContact contacts)
    {
        if(contacts.exp)
        {
            contacts.exp = false;
        }

        for(int i = 0 ; i < contacts.mContacts.size() ; i++)
        {
            MailManager.getInstance().mOrganization.allMailContact.remove(contacts.mContacts.get(i));
            hidContacts(contacts.mContacts.get(i));
        }
    }

    public void doAdd()
    {
        for (int i = 0; i < MailManager.getInstance().mAdd.size(); i++)
        {
            MailManager.getInstance().mAdd.get(i).isselect = false;

        }
        Intent intent = new Intent();
        intent.setAction(MailEditActivity.ACTION_ADD_MAIL_CONTACT);
        intent.putExtra("ltype", mMailContactsActivity.getIntent().getIntExtra("ltype", MailEditActivity.CONTENT_TYPE_SHOUJIAN));
        mMailContactsActivity.sendBroadcast(intent);
        mMailContactsActivity.finish();
    }

    public void fengfa() {
        for (int i = 0; i < MailManager.getInstance().mAdd.size(); i++)
        {
            MailManager.getInstance().mAdd.get(i).isselect = false;

        }
        mMailContactsActivity.waitDialog.show();
        MailAsks.fengfa(mMailContactsActivity,mMailContactsHandler,MailManager.getInstance().mAdd,mail);
    }

    public SearchViewLayout.DoTextChange doTextChange = new SearchViewLayout.DoTextChange()
    {

        @Override
        public void doTextChange(boolean visiable) {
            doSearch(mMailContactsActivity.searchView.getText());
        }
    };


    public View.OnClickListener mBackListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            if(now != null)
            {
                if(now.parent != null)
                {
                    now = now.parent;
                    contacts.clear();
                    contacts.addAll(now.mContacts);
                    mContactAdapter.notifyDataSetChanged();
                    doSearch(mMailContactsActivity.searchView.getText());
                    updataContactView();
                }
                else
                {
                    mMailContactsActivity.finish();
                }
            }
            else
            {
                mMailContactsActivity.finish();
            }

        }
    };
}
