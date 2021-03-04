package intersky.select.view.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import intersky.mywidget.SearchViewLayout;
import intersky.select.entity.Select;
import intersky.select.presenter.SelectPresenter;
import intersky.select.view.adapter.SelectAdapter;

/**
 * Created by xpx on 2017/8/18.
 */

public class SelectActivity extends BaseActivity {

    public ListView selectView;
    public SearchViewLayout searchView;
    public SelectAdapter mSelectAdapter;
    public SelectAdapter mSearchAdapter;
    public SelectPresenter mSelectPresenter = new SelectPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSelectPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mSelectPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener mSaveListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mSelectPresenter.doCallBack();
        }
    };

    public AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener()
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mSelectPresenter.doItemClick((Select) parent.getAdapter().getItem(position));
        }
    };

    public TextView.OnEditorActionListener mOnSearchActionListener = new TextView.OnEditorActionListener()
    {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
        {
            // TODO Auto-generated method stub
            if (actionId == EditorInfo.IME_ACTION_SEARCH)
            {
                mSelectPresenter.doSearch(searchView.getText());

            }
            return true;
        }
    };

}
