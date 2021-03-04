package intersky.function.view.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import intersky.function.entity.FunData;
import intersky.function.entity.Function;
import intersky.function.entity.TableDetial;
import intersky.function.presenter.GridPresenter;
import intersky.mywidget.SearchViewLayout;
import intersky.mywidget.TableView;

/**
 * Created by xpx on 2017/8/18.
 */

public class GridActivity extends BaseActivity {

    public static final String ACTION_GRID_LIST_UPDAT = "ACTION_GRID_LIST_UPDAT";
    public TableView mTable;
    public HorizontalScrollView mHorizontalScrollView;
    public SearchViewLayout mSearchView;
    public LinearLayout mClassManager;
    public LinearLayout mActions;
    public Function mFunction;
    public FunData mFunData = new FunData();
    public String address = "";
    public PopupWindow popupWindow2;
    public TableDetial mTableDetial = new TableDetial();
    public View mPopupWindowView2;
    public RelativeLayout shade;
    public Button back;
    public Button summit;
    public Button accept;
    public Button veto;
    public GridPresenter mGridPresenter = new GridPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGridPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mGridPresenter.Destroy();
        super.onDestroy();
    }
    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    public TextView.OnEditorActionListener mOnSearchActionListener = new TextView.OnEditorActionListener()
    {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
        {
            // TODO Auto-generated method stub
            if (actionId == EditorInfo.IME_ACTION_SEARCH)
            {
                mGridPresenter.doSearch(mSearchView.getText());

            }
            return true;
        }
    };

    public View.OnClickListener mOpenGaodeListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mGridPresenter.onGaode();
        }
    };

    public View.OnClickListener mOpenGaodeDownListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mGridPresenter.onGaodeDown();
        }
    };

    public View.OnClickListener mOpenBaiduListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mGridPresenter.onBaidu();
        }
    };

    public View.OnClickListener mOpenBaiduDownListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mGridPresenter.onBaiduDown();
        }
    };

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    public View.OnClickListener acceptListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mGridPresenter.doAccept();
        }
    };

    public View.OnClickListener vetoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mGridPresenter.doVote();
        }
    };
}
