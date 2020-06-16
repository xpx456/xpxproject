package intersky.function.view.activity;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import intersky.appbase.BaseActivity;
import intersky.function.receiver.entity.FunData;
import intersky.function.receiver.entity.Function;
import intersky.function.presenter.ChartPresenter;

/**
 * Created by xpx on 2017/8/18.
 */

public class ChartActivity extends BaseActivity {

    public WebView chartShow;
    public HorizontalScrollView mHorizontalScrollView;
    public LinearLayout mClassManager;
    public FunData mFunData = new FunData();
    public Function mFunction;
    public boolean isFirst = false;
    public ChartPresenter mChartPresenter = new ChartPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChartPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mChartPresenter.Destroy();
        super.onDestroy();
    }


}
