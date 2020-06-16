package com.intersky.strang.android.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.intersky.strang.android.presenter.ServiceListPresenter;

import intersky.appbase.BaseActivity;
import intersky.xpxnet.net.Service;

/**
 * Created by xpx on 2017/8/18.
 */

public class ServiceListActivity extends BaseActivity {

    public static final String ACTION_SERVICE_DELETE = "ACTION_SERVICE_DELETE";
    public ListView serviceList;
    public ServiceListPresenter mServiceListPresenter = new ServiceListPresenter(this);


    public ServiceListActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mServiceListPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mServiceListPresenter.Destroy();
        super.onDestroy();
    }

    public AdapterView.OnItemClickListener onServiceItemClickListener = new AdapterView.OnItemClickListener()
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mServiceListPresenter.doEditService((Service) parent.getAdapter().getItem(position));
        }
    };

    public AdapterView.OnItemLongClickListener onServiceItemLongClickListener = new AdapterView.OnItemLongClickListener()
    {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            mServiceListPresenter.deleteService((Service) parent.getAdapter().getItem(position));
            return true;
        }

    };

    public View.OnClickListener mAddNewServiceListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mServiceListPresenter.doAddService();
        }
    };




}
