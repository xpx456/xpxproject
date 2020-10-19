package com.dk.dkhome.view.fragment;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dk.dkhome.R;
import com.dk.dkhome.view.activity.MainActivity;
import com.dk.dkhome.view.activity.SportDetialActivity;

import intersky.appbase.BaseFragment;
import intersky.function.view.activity.MyChartActivity;

public class SportFragment extends BaseFragment {

//    public MainPresenter mMainPresenter;
    public MainActivity mMainActivity;
    public LinearLayout layer;
    public ImageView btnScan;

    public SportFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_sport, container, false);
        mMainActivity = (MainActivity) getActivity();
        mMainActivity.measureStatubar(mMainActivity, (RelativeLayout) mView.findViewById(R.id.stutebar));
        layer = mView.findViewById(R.id.blue);
        btnScan = mView.findViewById(R.id.scan);
        btnScan.setOnClickListener(scanEquipmentCodeListener);
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

    public void addView(BluetoothDevice bluetoothDevice) {
        View view = LayoutInflater.from(mMainActivity).inflate(R.layout.text_view, null);
        TextView textView = view.findViewById(R.id.main_title);
        textView.setText(bluetoothDevice.getAddress()+"/"+bluetoothDevice.getName());
        layer.addView(view);
    }
}
