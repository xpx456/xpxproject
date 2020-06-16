package intersky.function.view.activity;

import android.os.Bundle;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;

import intersky.appbase.BaseActivity;
import intersky.function.receiver.entity.FunData;
import intersky.function.receiver.entity.Function;
import intersky.function.presenter.LabelPresenter;

/**
 * Created by xpx on 2017/8/18.
 */

public class LabelActivity extends BaseActivity {

    public ListView mListView;
    public LinearLayout classesBar;
    public HorizontalScrollView mHorizontalScrollView;
    public Function mFunction;
    public FunData mFunData = new FunData();
    public LabelPresenter mLabelPresenter = new LabelPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLabelPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mLabelPresenter.Destroy();
        super.onDestroy();
    }
}
