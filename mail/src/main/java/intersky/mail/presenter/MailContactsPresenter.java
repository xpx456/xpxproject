package intersky.mail.presenter;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import intersky.appbase.entity.Contacts;
import intersky.mail.MailManager;
import intersky.mail.R;
import intersky.mail.asks.MailAsks;
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
        mMailContactsActivity.setContentView(R.layout.activity_contacts_list);
        mMailContactsActivity.contactList = (ListView)mMailContactsActivity.findViewById(R.id.contacts_List);
        mMailContactsActivity.searchView = mMailContactsActivity.findViewById(R.id.search);
        mMailContactsActivity.msbar = (MySlideBar) mMailContactsActivity.findViewById(R.id.slideBar);
        mMailContactsActivity.mLetterText = (TextView) mMailContactsActivity.findViewById(R.id.letter_text);
        mMailContactsActivity.mRelativeLetter = (RelativeLayout) mMailContactsActivity.findViewById(R.id.letter_layer);
        mMailContactsActivity.msbar.setVisibility(View.INVISIBLE);
        mMailContactsActivity.msbar.setOnTouchLetterChangeListenner(mMailContactsActivity.mOnTouchLetterChangeListenner);
        mMailContactsActivity.searchView.mSetOnSearchListener(mMailContactsActivity.mOnSearchActionListener);
        mMailContactsActivity.contactList.setOnScrollListener(mMailContactsActivity.mOnScoll);
        mMailContactsActivity.contactList.setOnItemClickListener(mMailContactsActivity.onContactItemClickListener);
        mMailContactsActivity.msbar.setmRelativeLayout(mMailContactsActivity.mRelativeLetter);
        mMailContactsActivity.msbar.setMletterView(mMailContactsActivity.mLetterText);
        ToolBarHelper.setRightBtnText(mMailContactsActivity.mActionBar, mMailContactsActivity.mAddListener, "  "+mMailContactsActivity.getString(R.string.mail_add)+"  ");
        if(mMailContactsActivity.getIntent().getIntExtra("ltype",MailEditActivity.CONTENT_TYPE_SHOUJIAN) == MailEditActivity.CONTENT_TYPE_LCC)
        {

            mContactAdapter = new MailContactAdapter(mMailContactsActivity,MailManager.getInstance().mailLContacts,true);
            if(MailManager.getInstance().mailLContacts.size() == 0)
            {
                MailManager.getInstance().initLocal();
            }
        }
        else
        {
            mContactAdapter = new MailContactAdapter(mMailContactsActivity,MailManager.getInstance().mailContacts,false);
            if(MailManager.getInstance().mailContacts.size() == 0)
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
                if(mMailContactsActivity.getIntent().getIntExtra("ltype",MailEditActivity.CONTENT_TYPE_SHOUJIAN) == MailEditActivity.CONTENT_TYPE_LCC)
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
                    String[] ss = new String[MailManager.getInstance().mHeadMailPersonItems.size()];
                    for (int i = 0; i < MailManager.getInstance().mHeadMailPersonItems.size(); i++) {
                        ss[i] = MailManager.getInstance().mHeadMailPersonItems.get(i).mName;
                    }
                    mMailContactsActivity.msbar.setAddletters(ss);
                    mMailContactsActivity.msbar.requestLayout();
                    mMailContactsActivity.msbar.setVisibility(View.VISIBLE);
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
        if (contacts.type == 0) {
            if (contacts.isselect) {
                contacts.isselect = false;
                MailManager.getInstance().mAdd.remove(contacts);
            } else {
                contacts.isselect = true;
                MailManager.getInstance().mAdd.add(contacts);
            }
        }
        ToolBarHelper.setRightBtnText(mMailContactsActivity.mActionBar, mMailContactsActivity.mAddListener, "  "+mMailContactsActivity.getString(R.string.mail_add)+"("+ String.valueOf(MailManager.getInstance().mAdd.size())+")"+"  ");
        mContactAdapter.notifyDataSetChanged();
        mSearchContactAdapter.notifyDataSetChanged();
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
}
