package com.intersky.android.presenter;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.intersky.R;
import com.intersky.android.database.DBHelper;
import com.intersky.android.handler.ServiceListHandler;
import com.intersky.android.receiver.ServiceListReceiver;
import com.intersky.android.view.activity.ServiceListActivity;
import com.intersky.android.view.activity.ServiceSettingActivity;
import com.intersky.android.view.adapter.ServerListAdapter;

import java.util.ArrayList;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.xpxnet.net.Service;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class ServiceListPresenter implements Presenter {


    public ServiceListHandler mServiceListHandler;
    public ServiceListActivity mServiceListActivity;
    public ServerListAdapter mServerListAdapter;
    public ArrayList<Service> mServices = new ArrayList<Service>();

    public ServiceListPresenter(ServiceListActivity mServiceListActivity)
    {
        this.mServiceListActivity = mServiceListActivity;
        this.mServiceListHandler = new ServiceListHandler(mServiceListActivity);
        mServiceListActivity.setBaseReceiver(new ServiceListReceiver(this.mServiceListHandler));
    }

    @Override
    public void Create() {
        initView();

    }

    @Override
    public void initView() {

        ToolBarHelper.setSutColor(mServiceListActivity, Color.parseColor("#ffffff"));
        mServiceListActivity.setContentView(R.layout.activity_server_list);
        ImageView bask = mServiceListActivity.findViewById(R.id.back);
        mServiceListActivity.flagFillBack = false;
        bask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mServiceListActivity.finish();
            }
        });
        TextView add =mServiceListActivity.findViewById(R.id.add);
        add.setOnClickListener(mServiceListActivity.mAddNewServiceListener);
        mServiceListActivity.serviceList = mServiceListActivity.findViewById(R.id.serverList);
        mServiceListActivity.serviceList.setOnItemClickListener(mServiceListActivity.onServiceItemClickListener);
        //mServiceListActivity.serviceList.setOnItemLongClickListener(mServiceListActivity.onServiceItemLongClickListener);
        mServices.addAll(DBHelper.getInstance(mServiceListActivity).scanServer());
        mServerListAdapter = new ServerListAdapter(mServiceListActivity,mServices,mServiceListActivity.onDeleteClickListener);
        mServiceListActivity.serviceList.setAdapter(mServerListAdapter);
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

    public void upDataList(Intent intent)
    {
        Service service = intent.getParcelableExtra("service");
        if(intent.getBooleanExtra("isnew",false) == true)
        {
            mServices.add(service);
        }
        else
        {
            for(int i = 0 ; i < mServices.size() ; i++)
            {
                if(mServices.get(i).sRecordId.equals(service.sRecordId))
                {
                    mServices.get(i).sName = service.sName;
                    mServices.get(i).sAddress = service.sAddress;
                    mServices.get(i).sType = service.sType;
                    mServices.get(i).sPort = service.sPort;
                    mServices.get(i).sCode = service.sCode;
                    mServices.get(i).https = service.https;
                    break;
                }
            }
        }

        mServerListAdapter.notifyDataSetChanged();
    }

    public void deleteService(Service service)
    {
        AppUtils.creatDialogTowButton(mServiceListActivity,mServiceListActivity.getString(R.string.servicelist_deleteservice),
                mServiceListActivity.getString(R.string.dialog_word_tip),mServiceListActivity.getString(R.string.button_word_cancle),
                mServiceListActivity.getString(R.string.button_word_ok),null,new DialogListener(service));
    }

    public void doDeleteSetvice(Service service)
    {
        mServices.remove(service);
        mServerListAdapter.notifyDataSetChanged();
        DBHelper.getInstance(mServiceListActivity).deleteServer(service);
        Intent intent = new Intent();
        intent.setAction(ServiceListActivity.ACTION_SERVICE_DELETE);
        intent.putExtra("service",service);
        mServiceListActivity.sendBroadcast(intent);
    }

    public void doAddService()
    {
        Intent intent = new Intent(mServiceListActivity, ServiceSettingActivity.class);
        mServiceListActivity.startActivity(intent);
    }

    public void doEditService(Service service)
    {
        Intent intent = new Intent(mServiceListActivity, ServiceSettingActivity.class);
        intent.putExtra("service",service);
        mServiceListActivity.startActivity(intent);
    }

    public class DialogListener implements DialogInterface.OnClickListener {

        public Service item;

        public DialogListener( Service item)
        {
            this.item = item;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {

            doDeleteSetvice(item);
        }
    }

}
