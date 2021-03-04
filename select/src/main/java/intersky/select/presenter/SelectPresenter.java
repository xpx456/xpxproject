package intersky.select.presenter;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import intersky.appbase.Presenter;
import intersky.select.R;
import intersky.select.SelectManager;
import intersky.select.entity.Select;
import intersky.select.view.activity.SelectActivity;
import intersky.select.view.adapter.SelectAdapter;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class SelectPresenter implements Presenter {

    public SelectActivity mSelectActivity;
    public SelectPresenter(SelectActivity mSelectActivity)
    {
        this.mSelectActivity = mSelectActivity;
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        mSelectActivity.setContentView(R.layout.activity_select);
        ImageView back = mSelectActivity.findViewById(R.id.back);
        back.setOnClickListener(mSelectActivity.mBackListener);
        mSelectActivity.selectView = mSelectActivity.findViewById(R.id.loder_List);
        mSelectActivity.searchView = mSelectActivity.findViewById(R.id.search);
        mSelectActivity.mSelectAdapter = new SelectAdapter(mSelectActivity,SelectManager.getInstance().mSelects);
        mSelectActivity.mSearchAdapter = new SelectAdapter(mSelectActivity, SelectManager.getInstance().mSearchSelects);
        mSelectActivity.selectView.setAdapter(mSelectActivity.mSelectAdapter);
        mSelectActivity.selectView.setOnItemClickListener(mSelectActivity.onItemClickListener);
        mSelectActivity.searchView.mSetOnSearchListener(mSelectActivity.mOnSearchActionListener);
        TextView save = mSelectActivity.findViewById(R.id.save);
        save.setText(mSelectActivity.getString(R.string.button_word_save));
        save.setOnClickListener(mSelectActivity.mSaveListener);
        if(mSelectActivity.getIntent().getBooleanExtra("signal",false) == false)
        {

            save.setVisibility(View.VISIBLE);
        }
        if(mSelectActivity.getIntent().getBooleanExtra("showSearch",false) == true)
        {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mSelectActivity.selectView.getLayoutParams();
            params.addRule(RelativeLayout.BELOW,R.id.search);
            mSelectActivity.selectView.setLayoutParams(params);
            save.setVisibility(View.INVISIBLE);
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

    public void doItemClick(Select sslect)
    {
        if(mSelectActivity.getIntent().getBooleanExtra("signal",false) == true)
        {
            for(int i = 0 ; i < SelectManager.getInstance().mSelects.size() ; i ++)
            {
                SelectManager.getInstance().mSelects.get(i).iselect = false;
            }
            sslect.iselect = true;
            SelectManager.getInstance().mSignal = sslect;
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
            mSelectActivity.mSelectAdapter.notifyDataSetChanged();
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
            if(!mSelectActivity.selectView.getAdapter().equals(mSelectActivity.mSelectAdapter)) {
                mSelectActivity.selectView.setAdapter(mSelectActivity.mSelectAdapter);
            }
        }
        else
        {
            for(int i = 0 ; i < SelectManager.getInstance().mSelects.size() ; i++) {
                Select mSelect = SelectManager.getInstance().mSelects.get(i);
                if(mSelect.mName.contains(keyword)) {
                    SelectManager.getInstance().mSearchSelects.add(mSelect);
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
