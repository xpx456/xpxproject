package intersky.task.view.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import intersky.mywidget.NoScrollViewPager;
import intersky.mywidget.TabHeadView;
import intersky.task.entity.Template;
import intersky.task.entity.TemplateType;
import intersky.task.presenter.TemplatePresenter;
import intersky.task.view.adapter.LoderPageAdapter;

/**
 * Created by xpx on 2017/8/18.
 */

public class TemplateActivity extends BaseActivity {

    public LinearLayout mTabHeadView;
    public TemplateType selectTemplateType;
    public TemplatePresenter mTemplatePresenter = new TemplatePresenter(this);
    public NoScrollViewPager mViewPager;
    public ArrayList<TemplateType> mTemplateTypes = new ArrayList<TemplateType>();
    public ArrayList<View> mViews = new ArrayList<View>();
    public LoderPageAdapter mLoderPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTemplatePresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mTemplatePresenter.Destroy();
        super.onDestroy();
    }

    public AdapterView.OnItemClickListener clickAdapterListener = new AdapterView.OnItemClickListener()
    {


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mTemplatePresenter.onitemcick((Template) parent.getAdapter().getItem(position));
        }
    };

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
}
