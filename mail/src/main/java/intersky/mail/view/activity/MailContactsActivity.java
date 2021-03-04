package intersky.mail.view.activity;

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

import intersky.mail.entity.MailContact;
import intersky.mail.presenter.MailContactsPresenter;
import intersky.mywidget.MySlideBar;
import intersky.mywidget.SearchViewLayout;

/**
 * Created by xpx on 2017/8/18.
 */

public class MailContactsActivity extends BaseActivity {

    public ListView contactList;
    public SearchViewLayout searchView;
    public MySlideBar msbar;
    public TextView mLetterText;
    public RelativeLayout mRelativeLetter;
    public MailContactsPresenter mMailContactsPresenter = new MailContactsPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMailContactsPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mMailContactsPresenter.Destroy();
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
                mMailContactsPresenter.doSearch(searchView.getText());

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
            mMailContactsPresenter.LetterChange(s);

        }
    };

    public AdapterView.OnItemClickListener onContactItemClickListener = new AdapterView.OnItemClickListener()
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mMailContactsPresenter.doSelect((MailContact) parent.getAdapter().getItem(position));
        }
    };

    public View.OnClickListener mAddListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            if(mMailContactsPresenter.mail == null)
            mMailContactsPresenter.doAdd();
            else
                mMailContactsPresenter.fengfa();
        }
    };
}
