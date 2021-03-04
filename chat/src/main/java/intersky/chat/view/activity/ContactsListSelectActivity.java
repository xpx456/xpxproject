package intersky.chat.view.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import intersky.appbase.entity.Contacts;
import intersky.chat.presenter.ContactsListSelectPresenter;
import intersky.chat.view.adapter.ContactSelectAdapter;
import intersky.chat.view.adapter.SampleContacttAdapter;
import intersky.mywidget.HorizontalListView;
import intersky.mywidget.MySlideBar;

@SuppressLint("ValidFragment")
public class ContactsListSelectActivity extends BaseActivity {


    public ContactsListSelectPresenter mContactsListPresenter = new ContactsListSelectPresenter(this);
    public HorizontalListView mHorizontalListView;
    public ArrayList<Contacts> mPahtList = new ArrayList<Contacts>();
    public ListView mList;
    public ContactSelectAdapter mContactAdapter;
    public SampleContacttAdapter mSelectAdapter;
    public Contacts mContacts;
    public TextView mLetterText;
    public RelativeLayout mRelativeLayout;
    public MySlideBar msbar;
    public TextView selecttip;
    public TextView save;
    public String depart = "";
    public Boolean isall = false;
    public ArrayList<Contacts> mtemp = new ArrayList<Contacts>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mContactsListPresenter.Create();
    }

    @Override
    public void onResume() {
        mContactsListPresenter.Resume();
        super.onResume();
    }

    @Override
    public void onDestroy()
    {
        mContactsListPresenter.Destroy();
        super.onDestroy();
    }

    public MySlideBar.OnTouchLetterChangeListenner mOnTouchLetterChangeListenner = new MySlideBar.OnTouchLetterChangeListenner()
    {

        @Override
        public void onTouchLetterChange(MotionEvent event, int s)
        {
            // TODO Auto-generated method stub
            mContactsListPresenter.LetterChange(s);

        }
    };

    public AdapterView.OnItemClickListener clickFunction = new AdapterView.OnItemClickListener()
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mContactsListPresenter.clickonContact((Contacts) parent.getAdapter().getItem(position));
        }
    };

    public AdapterView.OnItemClickListener clickSelect = new AdapterView.OnItemClickListener()
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mContactsListPresenter.clickSelect((Contacts) parent.getAdapter().getItem(position));
        }
    };

    public View.OnClickListener dackListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            mContactsListPresenter.doBack();
        }
    };

    public View.OnClickListener saveListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            mContactsListPresenter.doSave();
        }
    };

    public View.OnClickListener selectallListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            mContactsListPresenter.doselectall();
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(mContactsListPresenter.onKeyDown(keyCode, event))
        {
            return true;
        }
        else
        {
            return super.onKeyDown(keyCode, event);
        }

    }

}
