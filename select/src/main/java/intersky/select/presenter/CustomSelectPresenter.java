package intersky.select.presenter;

import android.content.Intent;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import intersky.appbase.Presenter;
import intersky.mywidget.PullToRefreshView;
import intersky.select.R;
import intersky.select.SelectHandler;
import intersky.select.SelectManager;
import intersky.select.SelectReceiver;
import intersky.select.entity.CustomSelect;
import intersky.select.view.activity.CustomSelectActivity;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class CustomSelectPresenter implements Presenter{
    ;
    public CustomSelectActivity mSelectActivity;
    public SelectHandler selectHandler;
    public CustomSelectPresenter(CustomSelectActivity mSelectActivity)
    {
        this.mSelectActivity = mSelectActivity;
        selectHandler = new SelectHandler(mSelectActivity);
        mSelectActivity.setBaseReceiver(new SelectReceiver(selectHandler));
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        mSelectActivity.setContentView(R.layout.activity_select_custom);
        ImageView back = mSelectActivity.findViewById(R.id.back);
        back.setOnClickListener(mSelectActivity.mBackListener);
        mSelectActivity.selectView = mSelectActivity.findViewById(R.id.loder_List);
        mSelectActivity.searchView = mSelectActivity.findViewById(R.id.search);
        mSelectActivity.selectView.setAdapter(SelectManager.getInstance().selectAdapter);
        mSelectActivity.selectView.setOnItemClickListener(mSelectActivity.onItemClickListener);
        mSelectActivity.searchView.mSetOnSearchListener(mSelectActivity.mOnSearchActionListener);
        mSelectActivity.mPullToRefreshView = (PullToRefreshView) mSelectActivity.findViewById(R.id.task_pull_refresh_view);
        mSelectActivity.mPullToRefreshView.getmFooterView().setBackgroundColor(Color.rgb(255, 255, 255));
        mSelectActivity.mPullToRefreshView.getmHeaderView().setBackgroundColor(Color.rgb(255, 255, 255));
        mSelectActivity.mPullToRefreshView.setOnHeaderRefreshListener(mSelectActivity);
        mSelectActivity.mPullToRefreshView.setOnFooterRefreshListener(mSelectActivity);
        TextView save = mSelectActivity.findViewById(R.id.save);
        save.setText(mSelectActivity.getString(R.string.button_word_save));
        save.setOnClickListener(mSelectActivity.mSaveListener);
        //if(mSelectActivity.getIntent().getBooleanExtra("signal",false) == false)
//        {
//            ToolBarHelper.setRightBtnText(mSelectActivity.mActionBar,mSelectActivity.mSaveListener,mSelectActivity.getString(R.string.button_word_save));
//        }
        if(mSelectActivity.getIntent().getBooleanExtra("showSearch",false) == true)
        {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mSelectActivity.mPullToRefreshView.getLayoutParams();
            params.addRule(RelativeLayout.BELOW,R.id.search);
            mSelectActivity.selectView.setLayoutParams(params);
        }
        TextView title = mSelectActivity.findViewById(R.id.title);
        title.setText(mSelectActivity.getIntent().getStringExtra("title"));
    }

    @Override
    public void Start() {

    }

    @Override
    public void Resume() {

    }

    @Override
    public void Pause() {

    }

    @Override
    public void Destroy() {

    }

    public void doItemClick(CustomSelect sslect)
    {
        if(mSelectActivity.getIntent().getBooleanExtra("signal",false) == true)
        {
            if(SelectManager.getInstance().selectAdapter.setSelect(sslect))
            {
                SelectManager.getInstance().mCustomSignal = sslect;
            }
            else
            {
                SelectManager.getInstance().mCustomSignal = null;
            }
            SelectManager.getInstance().selectAdapter.notifyDataSetChanged();
            doCallBack();
        }
        else
        {
            if(sslect.iselect == true)
            {
                sslect.iselect = false;
//                SelectManager.getInstance().mSelects.remove(sslect);
            }
            else
            {
                sslect.iselect = true;
//                SelectManager.getInstance().mSelects.add(sslect);
            }
            SelectManager.getInstance().selectAdapter.notifyDataSetChanged();

        }
    }

    public void doCallBack()
    {
        Intent intent = new Intent();
        intent.setAction(mSelectActivity.getIntent().getAction());
        mSelectActivity.sendBroadcast(intent);
        mSelectActivity.finish();
    }

    public void doSearch(String keyword)
    {
        if(keyword.length() == 0)
        {
            if(!mSelectActivity.selectView.getAdapter().equals(SelectManager.getInstance().selectAdapter)) {
                mSelectActivity.selectView.setAdapter(SelectManager.getInstance().selectAdapter);
            }
        }
        else
        {
            for(int i = 0 ; i < SelectManager.getInstance().selectAdapter.getCount() ; i++) {
                CustomSelect mSelect = SelectManager.getInstance().selectAdapter.getItem(i);
                if(mSelect.mName.contains(keyword)) {
                    SelectManager.getInstance().mCustomSearchSelects.add(mSelect);
                }
            }
            if(!mSelectActivity.selectView.getAdapter().equals(mSelectActivity.mSearchAdapter)){
                mSelectActivity.selectView.setAdapter(mSelectActivity.mSearchAdapter);
            }else {
                mSelectActivity.mSearchAdapter.notifyDataSetChanged();
            }
        }

    }

}
