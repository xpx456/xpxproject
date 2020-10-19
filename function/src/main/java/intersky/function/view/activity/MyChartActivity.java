package intersky.function.view.activity;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import intersky.appbase.BaseActivity;
import intersky.echartoption.ArrayData;
import intersky.echartoption.ObjectData;
import intersky.function.presenter.ChartPresenter;
import intersky.function.presenter.MyChartPresenter;
import intersky.function.receiver.entity.ChartData;
import intersky.function.receiver.entity.FunData;
import intersky.function.receiver.entity.Function;

/**
 * Created by xpx on 2017/8/18.
 */

public class MyChartActivity extends BaseActivity {

    public int max = 30000;
    public int now = 0;
    public WebView chartShow;;
    public ChartData chartData;
    public boolean isFirst = false;
    public String url = "";
    public ObjectData option;
    public ArrayData x;
    public ArrayData y;
    public MyChartPresenter mChartPresenter = new MyChartPresenter(this);

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
