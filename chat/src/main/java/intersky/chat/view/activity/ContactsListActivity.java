package intersky.chat.view.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import intersky.appbase.BaseActivity;
import intersky.mywidget.MySlideBar;
import intersky.mywidget.SearchViewLayout;
import intersky.appbase.entity.Contacts;
import intersky.chat.presenter.ContactsListPresenter;

/**
 * Created by xpx on 2017/8/18.
 */

public class ContactsListActivity extends BaseActivity {

    public ListView contactList;
    public SearchViewLayout searchView;
    public MySlideBar msbar;
    public TextView mLetterText;
    public RelativeLayout mRelativeLetter;
    public ContactsListPresenter mContactsListPresenter = new ContactsListPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContactsListPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mContactsListPresenter.Destroy();
        super.onDestroy();
    }

    public TextView.OnEditorActionListener mOnSearchActionListener = new TextView.OnEditorActionListener()
    {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
        {
            // TODO Auto-generated method stub
            if (actionId == EditorInfo.IME_ACTION_SEARCH)
            {
                mContactsListPresenter.doSearch(searchView.getText());

            }
            return true;
        }
    };

    public AbsListView.OnScrollListener mOnScoll = new AbsListView.OnScrollListener()
    {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if(searchView.ishow)
            {
                if(searchView.getText().length() == 0)
                {
                    searchView.hidEdit();
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    };

    public MySlideBar.OnTouchLetterChangeListenner mOnTouchLetterChangeListenner = new MySlideBar.OnTouchLetterChangeListenner()
    {

        @Override
        public void onTouchLetterChange(MotionEvent event, int s)
        {
            // TODO Auto-generated method stub
            mContactsListPresenter.LetterChange(s);

        }
    };

    public AdapterView.OnItemClickListener onContactItemClickListener = new AdapterView.OnItemClickListener()
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(mContactsListPresenter.mContactsListActivity.getIntent().getBooleanExtra("onSelect",false) == true)
            mContactsListPresenter.doSelect((Contacts) parent.getAdapter().getItem(position));
            else
                mContactsListPresenter.contactsClick((Contacts) parent.getAdapter().getItem(position));
        }
    };
}
