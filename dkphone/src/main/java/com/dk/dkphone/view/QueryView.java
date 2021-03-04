package com.dk.dkphone.view;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dk.dkphone.R;
import com.dk.dkphone.presenter.MainPresenter;
import com.dk.dkphone.view.adapter.DeviceAdapter;

import java.util.HashMap;

import intersky.mywidget.PopView;
import xpx.bluetooth.BluetoothSetManager;


public class QueryView extends PopView {

    public HashMap<String,BluetoothDevice> bluetoothDeviceHashMap = new HashMap<String,BluetoothDevice>();
    public ListView listview;
    public DeviceAdapter queryListAdapter;
    public ImageView closebtn;
    public TextView reflash;
    public MainPresenter mainPresenter;
    public AdapterView.OnItemClickListener onItemClickListener;
    public QueryView(Context context,AdapterView.OnItemClickListener onItemClickListener) {
        super(context);
        this.onItemClickListener = onItemClickListener;
        initView();

    }

    @Override
    public void initView() {
        mainView = LayoutInflater.from(context).inflate(R.layout.view_query, null);
        listview = mainView.findViewById(R.id.equiplist);
        reflash = mainView.findViewById(R.id.reflash);
        reflash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothSetManager.bluetoothSetManager.scanLeDevice();
                queryListAdapter.notifyDataSetChanged();
            }
        });
        close =mainView.findViewById(R.id.view_query);
        closebtn = mainView.findViewById(R.id.close);
        queryListAdapter = new DeviceAdapter(context, DkPhoneApplication.mApp.testManager.bluetoothSetManager.deviceslist);
        listview.setAdapter(queryListAdapter);
        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hidView();
            }
        });
        listview.setOnItemClickListener(onItemClickListener);
    }

    @Override
    public void destoryView() {
    }


    public void creatView(View location) {
        queryListAdapter.notifyDataSetChanged();
        BluetoothSetManager.bluetoothSetManager.scanLeDevice();
        super.creatView(location);
    }





    public View.OnClickListener inputListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

}
