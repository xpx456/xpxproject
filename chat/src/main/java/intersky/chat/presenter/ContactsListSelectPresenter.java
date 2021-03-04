package intersky.chat.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import intersky.appbase.Presenter;
import intersky.chat.ContactManager;
import intersky.appbase.entity.Contacts;
import intersky.chat.R;
import intersky.chat.view.activity.ContactsListSelectActivity;
import intersky.chat.view.adapter.ContactSelectAdapter;
import intersky.chat.view.adapter.SampleContacttAdapter;
import intersky.mywidget.HorizontalListView;
import intersky.mywidget.MySlideBar;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2016/10/27.
 */

public class ContactsListSelectPresenter implements Presenter {

    public ContactsListSelectActivity mContactsListSelectActivity;

    public ContactsListSelectPresenter(ContactsListSelectActivity mContactsListSelectActivity)
    {
        this.mContactsListSelectActivity = mContactsListSelectActivity;
    }

    @SuppressLint("NewApi")
    @Override
    public void initView() {
        mContactsListSelectActivity.setContentView(R.layout.activity_contacts_select);
        ImageView back = mContactsListSelectActivity.findViewById(R.id.back);
        back.setOnClickListener(mContactsListSelectActivity.dackListener);
        mContactsListSelectActivity.depart = "";
        mContactsListSelectActivity.isall = false;
        mContactsListSelectActivity.selecttip = (TextView) mContactsListSelectActivity.findViewById(R.id.selecttext);
        mContactsListSelectActivity.mContacts = ContactManager.mContactManager.mOrganization.showContacts;
        mContactsListSelectActivity.mLetterText = (TextView) mContactsListSelectActivity.findViewById(R.id.letter_text);
        mContactsListSelectActivity.mRelativeLayout = (RelativeLayout) mContactsListSelectActivity.findViewById(R.id.letter_layer);
        mContactsListSelectActivity.msbar = (MySlideBar) mContactsListSelectActivity.findViewById(R.id.slideBar);
        mContactsListSelectActivity.mHorizontalListView = (HorizontalListView) mContactsListSelectActivity.findViewById(R.id.horizon_listview);
        mContactsListSelectActivity.msbar.setVisibility(View.INVISIBLE);
        mContactsListSelectActivity.msbar.setOnTouchLetterChangeListenner(mContactsListSelectActivity.mOnTouchLetterChangeListenner);
        TextView title = mContactsListSelectActivity.findViewById(R.id.title);
        title.setText(mContactsListSelectActivity.getIntent().getStringExtra("title"));
        mContactsListSelectActivity.mList = (ListView) mContactsListSelectActivity.findViewById(R.id.busines_List);
        LayoutInflater mInflater = (LayoutInflater) mContactsListSelectActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContactsListSelectActivity.mContacts = ContactManager.mContactManager.mOrganization.showContacts;
        mContactsListSelectActivity.mContactAdapter=new ContactSelectAdapter(mContactsListSelectActivity.mContacts.mContacts,mContactsListSelectActivity);
        mContactsListSelectActivity.mList.setAdapter(mContactsListSelectActivity.mContactAdapter);
        mContactsListSelectActivity.mList.setOnItemClickListener(mContactsListSelectActivity.clickFunction);
        mContactsListSelectActivity.msbar.setmRelativeLayout(mContactsListSelectActivity.mRelativeLayout);
        mContactsListSelectActivity.msbar.setMletterView(mContactsListSelectActivity.mLetterText);
        mContactsListSelectActivity.mtemp.addAll(ContactManager.mContactManager.mOrganization.mselectitems);
        mContactsListSelectActivity.mSelectAdapter = new SampleContacttAdapter( mContactsListSelectActivity.mtemp,mContactsListSelectActivity);
        mContactsListSelectActivity.mHorizontalListView.setAdapter(mContactsListSelectActivity.mSelectAdapter);
        mContactsListSelectActivity.mPahtList.add(mContactsListSelectActivity.mContacts);
        mContactsListSelectActivity.mHorizontalListView.setOnItemClickListener(mContactsListSelectActivity.clickSelect);
        mContactsListSelectActivity.save = (TextView) mContactsListSelectActivity.findViewById(R.id.save_btn);
        mContactsListSelectActivity.save.setOnClickListener(mContactsListSelectActivity.saveListener);
        if(mContactsListSelectActivity.mtemp.size() > 0)
        {
            mContactsListSelectActivity.selecttip.setVisibility(View.INVISIBLE);
        }
        else
        {
            mContactsListSelectActivity.selecttip.setVisibility(View.VISIBLE);
        }
        initList(mContactsListSelectActivity.mContacts);

        if(mContactsListSelectActivity.mtemp.size() == 0)
        {
            mContactsListSelectActivity.save.setText(mContactsListSelectActivity.getString(R.string.button_word_ok));
            mContactsListSelectActivity.save.setBackgroundResource(R.drawable.btn_style_alert_dialog_button_gray);
            mContactsListSelectActivity.save.setEnabled(false);
        }
        else
        {
            mContactsListSelectActivity.save.setText(mContactsListSelectActivity.getString(R.string.button_word_ok)+"("+ String.valueOf(mContactsListSelectActivity.mtemp.size())+")");
            mContactsListSelectActivity.save.setBackgroundResource(R.drawable.btn_style_alert_dialog_button_blue);
            mContactsListSelectActivity.save.setEnabled(true);
        }

    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void Start() {

    }

    @Override
    public void Resume() {
        if (mContactsListSelectActivity.msbar != null) {
            String[] ss = new String[mContactsListSelectActivity.mContacts.mHeadContacts.size()];
            for (int i = 0; i < mContactsListSelectActivity.mContacts.mHeadContacts.size(); i++) {
                ss[i] = mContactsListSelectActivity.mContacts.mHeadContacts.get(i).mName;
            }
            mContactsListSelectActivity.msbar.setAddletters(ss);
            mContactsListSelectActivity.msbar.requestLayout();
            mContactsListSelectActivity.msbar.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void Pause() {

    }

    @Override
    public void Destroy() {

        if(mContactsListSelectActivity.mContactAdapter.getmContacts().size() > 1 && mContactsListSelectActivity.mContactAdapter.getmContacts().get(1).mType == Contacts.TYPE_TOP)
        {
            mContactsListSelectActivity.mContactAdapter.getmContacts().remove(0);
            mContactsListSelectActivity.mContactAdapter.getmContacts().remove(0);
            mContactsListSelectActivity.mContactAdapter.getmContacts().remove(0);
        }

    }

    private void startContactlist(Contacts mContacts)
    {
        mContactsListSelectActivity.mPahtList.add(mContacts);
        initList(mContacts);
    }

    public void initList(Contacts mContacts)
    {
        Contacts mContactstt = null;
        if(mContactsListSelectActivity.mContactAdapter.getmContacts().size() > 1 && mContactsListSelectActivity.mContactAdapter.getmContacts().get(1).mType == Contacts.TYPE_TOP)
        {
            mContactstt = mContactsListSelectActivity.mContactAdapter.getmContacts().get(1);
            mContactsListSelectActivity.mContactAdapter.getmContacts().remove(0);
            mContactsListSelectActivity.mContactAdapter.getmContacts().remove(0);
            mContactsListSelectActivity.mContactAdapter.getmContacts().remove(0);
        }

        mContacts.mContacts.add(0,new Contacts(""));
        if(mContactstt == null)
        mContacts.mContacts.add(0,new Contacts("",Contacts.TYPE_TOP));
        else
            mContacts.mContacts.add(0,mContactstt);
        mContacts.mContacts.add(0,new Contacts(""));
        mContactsListSelectActivity.mContactAdapter.setmContacts(mContacts.mContacts);
        boolean isall = true;
        if (mContactsListSelectActivity.msbar != null) {
            String[] ss = new String[mContacts.mHeadContacts.size()];
            for (int i = 0; i < mContacts.mHeadContacts.size(); i++) {
                ss[i] = mContacts.mHeadContacts.get(i).mName;
            }
            mContactsListSelectActivity.msbar.setAddletters(ss);
            mContactsListSelectActivity.msbar.requestLayout();
            mContactsListSelectActivity.msbar.setVisibility(View.VISIBLE);

        }
        mContactsListSelectActivity.mContactAdapter.notifyDataSetChanged();
    }


    private void startContact(Contacts mContacts)
    {
        if(ContactManager.mContactManager.checkLocked(mContacts) == true)
            return;

        if(mContacts.isselect == true)
        {
            mContacts.isselect = false;
            mContactsListSelectActivity.mtemp.remove(mContacts);
            if(mContactsListSelectActivity.mContactAdapter.getmContacts().get(1).isselect == true)
            {
                mContactsListSelectActivity.mContactAdapter.getmContacts().get(1).isselect = false;
            }

        }
        else
        {
            mContacts.isselect = true;
            mContactsListSelectActivity.mtemp.add(mContacts);
            boolean isall = true;
            for(int i = 0 ; i < mContactsListSelectActivity.mContactAdapter.getmContacts().size() ; i++)
            {
                if(mContactsListSelectActivity.mContactAdapter.getmContacts().get(i).mType == Contacts.TYPE_PERSON)
                {
                    if(mContactsListSelectActivity.mContactAdapter.getmContacts().get(i).isselect == false)
                    {
                        isall = false;
                        break;
                    }
                }

            }
            if(isall == true)
            {
                mContactsListSelectActivity.mContactAdapter.getmContacts().get(1).isselect = true;
            }
        }
        if(mContactsListSelectActivity.mtemp.size() == 0 && mContactsListSelectActivity.selecttip.getVisibility() == View.INVISIBLE)
        {
            mContactsListSelectActivity.selecttip.setVisibility(View.VISIBLE);
        }
        else if(mContactsListSelectActivity.mtemp.size() > 0 && mContactsListSelectActivity.selecttip.getVisibility() == View.VISIBLE)
        {
            mContactsListSelectActivity.selecttip.setVisibility(View.INVISIBLE);
        }
        mContactsListSelectActivity.mSelectAdapter.notifyDataSetChanged();
        mContactsListSelectActivity.mContactAdapter.notifyDataSetChanged();

        if(mContactsListSelectActivity.mtemp.size() == 0)
        {
            mContactsListSelectActivity.save.setText(mContactsListSelectActivity.getString(R.string.button_word_ok));
            mContactsListSelectActivity.save.setBackgroundResource(R.drawable.btn_style_alert_dialog_button_gray);
            mContactsListSelectActivity.save.setEnabled(false);
        }
        else
        {
            mContactsListSelectActivity.save.setText(mContactsListSelectActivity.getString(R.string.button_word_ok)+"("+ String.valueOf(mContactsListSelectActivity.mtemp.size())+")");
            mContactsListSelectActivity.save.setBackgroundResource(R.drawable.btn_style_alert_dialog_button_blue);
            mContactsListSelectActivity.save.setEnabled(true);
        }
    }


    public void clickonContact(Contacts mContacts) {
        if (mContacts.mType == Contacts.TYPE_PERSON) {
            if(ContactManager.mContactManager.checkLocked(mContacts)==false)
            startContact(mContacts);
        } else if (mContacts.mType == Contacts.TYPE_CLASS) {
            startContactlist(mContacts);
        }
        else if(mContacts.mType == Contacts.TYPE_TOP)
        {
            doselectall();
        }
    }

    public void clickSelect(Contacts mContacts)
    {
        if(ContactManager.mContactManager.checkLocked(mContacts) == true)
            return;

        mContacts.isselect = false;
        mContactsListSelectActivity.mtemp.remove(mContacts);
        if(mContactsListSelectActivity.mContactAdapter.getmContacts().indexOf(mContacts) != -1)
        {
            mContactsListSelectActivity.mContactAdapter.getmContacts().get(1).isselect = false;
        }
        mContactsListSelectActivity.mSelectAdapter.notifyDataSetChanged();
        mContactsListSelectActivity.mContactAdapter.notifyDataSetChanged();
        if(mContactsListSelectActivity.mtemp.size() == 0 && mContactsListSelectActivity.selecttip.getVisibility() == View.INVISIBLE)
        {
            mContactsListSelectActivity.selecttip.setVisibility(View.VISIBLE);
        }

        if(mContactsListSelectActivity.mtemp.size() == 0)
        {
            mContactsListSelectActivity.save.setText(mContactsListSelectActivity.getString(R.string.button_word_ok));
            mContactsListSelectActivity.save.setBackgroundResource(R.drawable.btn_style_alert_dialog_button_gray);
            mContactsListSelectActivity.save.setEnabled(false);
        }
        else
        {
            mContactsListSelectActivity.save.setText(mContactsListSelectActivity.getString(R.string.button_word_ok)+"("+ String.valueOf(mContactsListSelectActivity.mtemp.size())+")");
            mContactsListSelectActivity.save.setBackgroundResource(R.drawable.btn_style_alert_dialog_button_blue);
            mContactsListSelectActivity.save.setEnabled(true);
        }
        if(mContactsListSelectActivity.mtemp.size() > 0)
        {
            mContactsListSelectActivity.selecttip.setVisibility(View.INVISIBLE);
        }
        else
        {
            mContactsListSelectActivity.selecttip.setVisibility(View.VISIBLE);
        }
    }

    public void LetterChange(int s) {
//        String word = s.toLowerCase();
        Contacts model = ContactManager.mContactManager.mOrganization.allContactsHead.get(s);
        int a = ContactManager.mContactManager.mOrganization.allContacts.indexOf(model);
        mContactsListSelectActivity.mList.setSelectionFromTop(a, 0);

    }

    public void doSave()
    {
        checkDepart();
        ContactManager.mContactManager.mOrganization.mselectitems.clear();
        ContactManager.mContactManager.mOrganization.mselectitems.addAll(mContactsListSelectActivity.mtemp);
        Intent mintent = new Intent();
        mintent.setAction(mContactsListSelectActivity.getIntent().getAction());
        mintent.putExtra("depart",mContactsListSelectActivity.depart);
        mContactsListSelectActivity.sendBroadcast(mintent);
        mContactsListSelectActivity.finish();

        ContactManager.mContactManager.mOrganization.mlocked.clear();
    }

    public void doBack()
    {
        if(mContactsListSelectActivity.mPahtList.size() == 1)
        {
            for(int i = 0 ; i < mContactsListSelectActivity.mtemp.size() ; i++)
            {
                if(ContactManager.mContactManager.mOrganization.mselectitems.indexOf(mContactsListSelectActivity.mtemp.get(i)) == -1)
                {
                    mContactsListSelectActivity.mtemp.get(i).isselect = false;
                }
            }
            for(int i = 0 ; i < ContactManager.mContactManager.mOrganization.mselectitems.size() ; i++)
            {
                if(mContactsListSelectActivity.mtemp.indexOf(ContactManager.mContactManager.mOrganization.mselectitems.get(i)) == -1)
                {
                    ContactManager.mContactManager.mOrganization.mselectitems.get(i).isselect = true;
                }
            }
            ContactManager.mContactManager.mOrganization.mlocked.clear();
            mContactsListSelectActivity.finish();
        }
        else
        {
            mContactsListSelectActivity.mPahtList.remove(mContactsListSelectActivity.mPahtList.size()-1);
            initList(mContactsListSelectActivity.mPahtList.get(mContactsListSelectActivity.mPahtList.size()-1));
        }

    }

    public void setAllselect(Contacts mContacts)
    {
        for(Contacts mContacts1 : mContacts.mContacts)
        {

            if(mContacts1.mType == Contacts.TYPE_PERSON)
            {
                if(ContactManager.mContactManager.checkLocked(mContacts1)==false)
                {
                    if(mContacts1.isselect == false)
                    {
                        mContacts1.isselect = true;
                        mContactsListSelectActivity.mtemp.add(mContacts1);
                    }
                }

            }
            else if(mContacts1.mType == Contacts.TYPE_CLASS)
            {
                setAllselect(mContacts1);
            }
        }
    }

    public void setAllselectf(Contacts mContacts)
    {
        for(Contacts mContacts1 : mContacts.mContacts)
        {
            if(mContacts1.mType == Contacts.TYPE_PERSON)
            {
                if(ContactManager.mContactManager.checkLocked(mContacts1)==false)
                {
                    if(mContacts1.isselect == true)
                    {
                        mContacts1.isselect = false;
                        mContactsListSelectActivity.mtemp.remove(mContacts1);
                    }
                }

            }
            else if(mContacts.mType == Contacts.TYPE_CLASS)
            {
                setAllselectf(mContacts1);
            }
        }
    }

    public void addDepartword(String word)
    {
        if(mContactsListSelectActivity.depart.length() == 0)
        {
            mContactsListSelectActivity.depart += word;
        }
        else
        {
            mContactsListSelectActivity.depart+=","+word;
        }
    }

    public boolean checkContect(Contacts mContacts1)
    {
        boolean has = false;
        for(Contacts mContacts:mContacts1.mContacts)
        {
            if(mContacts.mType == Contacts.TYPE_PERSON)
            {
                if(ContactManager.mContactManager.checkLocked(mContacts1)==false)
                {
                    if(mContacts.isselect == true)
                    {
                        has = true;
                    }
                    else
                    {
                        mContactsListSelectActivity.isall = false;
                    }
                }

            }
            else if(mContacts.mType == Contacts.TYPE_CLASS)
            {
                if(checkContect(mContacts) == true)
                {
                    addDepartword(mContacts.getName());
                }
            }
        }
        return has;
    }

    public void checkDepart()
    {
        mContactsListSelectActivity.isall = true;
        for(Contacts mContacts:mContactsListSelectActivity.mContacts.mContacts)
        {
            if(mContacts.mType == Contacts.TYPE_PERSON)
            {
                if(ContactManager.mContactManager.checkLocked(mContacts)==false)
                {
                    if(mContacts.isselect == false)
                    {
                        mContactsListSelectActivity.isall = false;
                    }
                }
            }
            else if(mContacts.mType == Contacts.TYPE_CLASS)
            {
                if(checkContect(mContacts) == true)
                {
                    addDepartword(mContacts.getName());
                }
            }
        }
        if(mContactsListSelectActivity.isall == true)
        {
            mContactsListSelectActivity.depart = mContactsListSelectActivity.getString(R.string.xml_mail_all);
        }
    }

    public void doselectall()
    {
        if(mContactsListSelectActivity.mContactAdapter.getmContacts().get(1).isselect == false)
        {
            if((mContactsListSelectActivity.getIntent().getIntExtra("type", 0) == ContactManager.SELECT_FIRST_ALL)
                    && mContactsListSelectActivity.mPahtList.size() == 1)
            {
                for(Contacts mContacts : mContactsListSelectActivity.mPahtList.get(mContactsListSelectActivity.mPahtList.size()-1).mContacts)
                {
                    if(mContacts.mType == Contacts.TYPE_PERSON)
                    {
                        if(ContactManager.mContactManager.checkLocked(mContacts)==false)
                        {
                            if(mContacts.isselect == false)
                            {
                                mContacts.isselect = true;
                                mContactsListSelectActivity.mtemp.add(mContacts);
                            }
                        }

                    }
                    else if(mContacts.mType == Contacts.TYPE_CLASS)
                    {
                        setAllselect(mContacts);
                    }

                }
            }
            else
            {
                for(Contacts mContacts : mContactsListSelectActivity.mPahtList.get(mContactsListSelectActivity.mPahtList.size()-1).mContacts)
                {
                    if(mContacts.mType == Contacts.TYPE_PERSON)
                    {
                        if(ContactManager.mContactManager.checkLocked(mContacts)==false)
                        {
                            if(mContacts.isselect == false)
                            {
                                mContacts.isselect = true;
                                mContactsListSelectActivity.mtemp.add(mContacts);
                            }
                        }

                    }

                }
            }

            mContactsListSelectActivity.mContactAdapter.getmContacts().get(1).isselect = true;
        }
        else
        {
            if((mContactsListSelectActivity.getIntent().getIntExtra("type", 0) == ContactManager.SELECT_FIRST_ALL)
                    && mContactsListSelectActivity.mPahtList.size() == 1)
            {
                for(Contacts mContacts : mContactsListSelectActivity.mPahtList.get(mContactsListSelectActivity.mPahtList.size()-1).mContacts)
                {
                    if(mContacts.mType == Contacts.TYPE_PERSON)
                    {
                        if(ContactManager.mContactManager.checkLocked(mContacts)==false)
                        {
                            if(mContacts.isselect == true)
                            {
                                mContacts.isselect = false;
                                mContactsListSelectActivity.mtemp.remove(mContacts);
                            }
                        }

                    }
                    else if(mContacts.mType == Contacts.TYPE_CLASS)
                    {
                        setAllselectf(mContacts);
                    }
                }
            }
            else
            {
                for(Contacts mContacts : mContactsListSelectActivity.mPahtList.get(mContactsListSelectActivity.mPahtList.size()-1).mContacts)
                {
                    if(mContacts.mType == Contacts.TYPE_PERSON)
                    {
                        if(ContactManager.mContactManager.checkLocked(mContacts)==false)
                        {
                            if(mContacts.isselect == true)
                            {
                                mContacts.isselect = false;
                                mContactsListSelectActivity.mtemp.remove(mContacts);
                            }
                        }

                    }

                }

            }
            mContactsListSelectActivity.mContactAdapter.getmContacts().get(1).isselect = false;
        }
        mContactsListSelectActivity.mSelectAdapter.notifyDataSetChanged();
        mContactsListSelectActivity.mContactAdapter.notifyDataSetChanged();

        if(mContactsListSelectActivity.mtemp.size() == 0)
        {
            mContactsListSelectActivity.save.setText(mContactsListSelectActivity.getString(R.string.button_word_ok));
            mContactsListSelectActivity.save.setBackgroundResource(R.drawable.btn_style_alert_dialog_button_gray);
            mContactsListSelectActivity.save.setEnabled(false);
        }
        else
        {
            mContactsListSelectActivity.save.setText(mContactsListSelectActivity.getString(R.string.button_word_ok)+"("+ String.valueOf(mContactsListSelectActivity.mtemp.size())+")");
            mContactsListSelectActivity.save.setBackgroundResource(R.drawable.btn_style_alert_dialog_button_blue);
            mContactsListSelectActivity.save.setEnabled(true);
        }
        if(mContactsListSelectActivity.mtemp.size() > 0)
        {
            mContactsListSelectActivity.selecttip.setVisibility(View.INVISIBLE);
        }
        else
        {
            mContactsListSelectActivity.selecttip.setVisibility(View.VISIBLE);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            doBack();

            return true;
        }
        else {
            return false;
        }
    }

}
