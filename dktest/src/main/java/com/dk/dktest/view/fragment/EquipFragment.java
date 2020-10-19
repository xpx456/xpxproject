package com.dk.dktest.view.fragment;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dk.dktest.R;
import com.dk.dktest.handler.MainHandler;
import com.dk.dktest.view.DkTestApplication;
import com.dk.dktest.view.activity.MainActivity;
import com.dk.dktest.view.adapter.DeviceAdapter;

import java.util.Map;

import intersky.appbase.BaseFragment;
import xpx.bluetooth.BluetoothSetManager;

public class EquipFragment extends BaseFragment {

//    public MainPresenter mMainPresenter;
    public MainActivity mMainActivity;
    public ListView epuipList;
    public DeviceAdapter deviceAdapter;
    public ImageView reFlash;
    public boolean initfinish = false;
    public EquipFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainActivity = (MainActivity) getActivity();
        View mView = inflater.inflate(R.layout.fragment_equip, container, false);
        mMainActivity.measureStatubar(mMainActivity, (RelativeLayout) mView.findViewById(R.id.stutebar));
        epuipList = mView.findViewById(R.id.equiplist);
        reFlash = mView.findViewById(R.id.reflash);
        deviceAdapter = new DeviceAdapter(mMainActivity,
                DkTestApplication.mApp.bluetoothSetManager.deviceslist,mMainActivity.findViewById(R.id.activity_main));
        epuipList.setAdapter(deviceAdapter);
        reFlash.setOnClickListener(reFlashListener);
        return mView;
    }



    public void updataView(Intent intent) {
        if (deviceAdapter != null)
        {
            deviceAdapter.notifyDataSetChanged();
        }
    }

    public void cleanViews(Intent intent) {
        if (deviceAdapter != null)
        {
            deviceAdapter.notifyDataSetChanged();
        }
    }

    public View.OnClickListener reFlashListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View view) {
            mMainActivity.waitDialog.show();
            DkTestApplication.mApp.bluetoothSetManager.scanLeDevice();
            mMainActivity.mMainPresenter.mainHandler.sendEmptyMessageDelayed(MainHandler.WAIT_HID,20000);
        }
    };


}
