package intersky.function.view.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import intersky.function.entity.FunData;
import intersky.function.entity.Function;
import intersky.function.presenter.FunctionModulePresenter;
import intersky.mywidget.ShadeLayout;
import intersky.mywidget.TableCloumArts;
import intersky.mywidget.TableItem;
import intersky.mywidget.TableView;

/**
 * Created by xpx on 2017/8/18.
 */

public class FunctionModuleActivity extends BaseActivity {

    public RelativeLayout searchViewlayer;
    public ImageView mSearchScan;
    public EditText mSearchView;
    public TextView searchText;
    public LinearLayout mSearchlayer;
    public TextView mSearchbtn;
    public TextView mCleanbtn;
    public PopupWindow popupWindow2;
    public PopupWindow popupWindow3;
    public HorizontalScrollView mHorizontalScrollView;
    public LinearLayout mClassManager;
    public ListView mLable;
    public WebView mChart;
    public ShadeLayout mshade;
    public RelativeLayout shade;
    public Function mFunction;
    public FunData mFunData = new FunData();
    public String searchHead = "";
    public TableView mTable;
    public FunctionModulePresenter mFunctionModulePresenter = new FunctionModulePresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFunctionModulePresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mFunctionModulePresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener mSearchListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub

        }
    };

    public View.OnClickListener mCleanListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
        }
    };


    public TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener()
    {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
        {
            // TODO Auto-generated method stub
            if (actionId == EditorInfo.IME_ACTION_SEARCH)
            {
                mFunctionModulePresenter.doSearch();
            }
            return true;
        }
    };

    public View.OnClickListener mScanListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub

            mFunctionModulePresenter.scan();
        }
    };

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

}
