package intersky.function.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import intersky.function.entity.BussinessWarnItem;
import intersky.function.entity.FunData;
import intersky.function.entity.Function;
import intersky.function.presenter.BusinessWarnPresenter;

/**
 * Created by xpx on 2017/8/18.
 */

public class BusinessWarnActivity extends BaseActivity {

    public ListView mListView;
    public Function mFunction;
    public FunData mFunData = new FunData();
    public BusinessWarnPresenter mBusinessWarnPresenter = new BusinessWarnPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBusinessWarnPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mBusinessWarnPresenter.Destroy();
        super.onDestroy();
    }

    public AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mBusinessWarnPresenter.doClickListener((BussinessWarnItem) parent.getAdapter().getItem(position));
        }
    };

    public AdapterView.OnItemLongClickListener onItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            mBusinessWarnPresenter.doLongClickListener((BussinessWarnItem) parent.getAdapter().getItem(position));
            return true;
        }
    };
}
