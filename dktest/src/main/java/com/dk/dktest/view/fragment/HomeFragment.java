package com.dk.dktest.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dk.dktest.R;
import com.dk.dktest.entity.Define;
import com.dk.dktest.view.DkTestApplication;
import com.dk.dktest.view.activity.MainActivity;
import com.dk.dktest.view.activity.SportDetialActivity;

import intersky.appbase.BaseFragment;
import intersky.apputils.AppUtils;
import intersky.function.view.activity.MyChartActivity;
import xpx.bluetooth.BluetoothSetManager;

public class HomeFragment extends BaseFragment {

//    public MainPresenter mMainPresenter;
    public MainActivity mMainActivity;
    public RelativeLayout btn1;
    public RelativeLayout btn2;
    public HomeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this
        mMainActivity = (MainActivity) getActivity();
        View mView = inflater.inflate(R.layout.fragment_home, container, false);
        mMainActivity.measureStatubar(mMainActivity, (RelativeLayout) mView.findViewById(R.id.stutebar));
        btn1 = mView.findViewById(R.id.btn1);
        btn2 = mView.findViewById(R.id.btn2);
        btn1.setOnClickListener(startbtn1Listener);
        btn2.setOnClickListener(startbtn2Listener);
        return mView;
    }


    public View.OnClickListener scanEquipmentCodeListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mMainActivity, MyChartActivity.class);
            mMainActivity.startActivity(intent);
        }
    };

    public View.OnClickListener startbtn1Listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            Intent intent1 = new Intent(mMainActivity, SportDetialActivity.class);
//            intent1.putExtra("type", Define.ATUO_CYCLE_TEST);
//            mMainActivity.startActivity(intent1);
            if(DkTestApplication.mApp.bluetoothSetManager.mBluetoothGatt != null)
            {
                if(DkTestApplication.mApp.bluetoothSetManager.mConnectionState == BluetoothSetManager.STATE_CONNECTED)
                {
                    Intent intent = new Intent(mMainActivity, SportDetialActivity.class);
                    intent.putExtra("type", Define.ATUO_CYCLE_TEST);
                    mMainActivity.startActivity(intent);
//                    Intent intent = new Intent(mMainActivity, MyChartActivity.class);
//                    mMainActivity.startActivity(intent);
                }
                else
                {
                    AppUtils.showMessage(mMainActivity,mMainActivity.getString(R.string.no_device_connect));
                }
            }
            else{
                AppUtils.showMessage(mMainActivity,mMainActivity.getString(R.string.no_device_connect));
            }

        }
    };

    public View.OnClickListener startbtn2Listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(DkTestApplication.mApp.bluetoothSetManager.mBluetoothGatt != null)
            {
                if(DkTestApplication.mApp.bluetoothSetManager.mConnectionState == BluetoothSetManager.STATE_CONNECTED)
                {
                    Intent intent = new Intent(mMainActivity, SportDetialActivity.class);
                    intent.putExtra("type", Define.EXTER_TEST);
                    mMainActivity.startActivity(intent);
                }
                else
                {
                    AppUtils.showMessage(mMainActivity,mMainActivity.getString(R.string.no_device_connect));
                }
            }
            else{
                AppUtils.showMessage(mMainActivity,mMainActivity.getString(R.string.no_device_connect));
            }

        }
    };
}
