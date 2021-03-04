package com.dk.dkhome.view;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dk.dkhome.R;
import com.dk.dkhome.entity.Device;


import intersky.apputils.AppUtils;

public class ProgressView {
    public static final int UPDTAT_PERSENT = 3021501;
    public View view;
    public TextView title1;
    public TextView ptext;
    public Handler handler;
    public Device device;
    public ProgressBar progressBar;
    public PopupWindow popupWindow;
    public DoCancleListener doCancleListener;
    public int persent = 0;
    public ProgressView(Context context, DoCancleListener doCancleListener){
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.progress_view, null);
        title1 = view.findViewById(R.id.title);
        ptext = view.findViewById(R.id.progresstxt);
        ptext.setText("0%");
        progressBar = view.findViewById(R.id.progress);
        progressBar.setMax(100);
        progressBar.setProgress(0);
        this.doCancleListener = doCancleListener;
        TextView cancle = view.findViewById(R.id.cancle);
        cancle.setOnClickListener(cancleListener);
    }

    public void creat(Activity context, RelativeLayout shade, View location,Device device,Handler handler){
        this.device = device;
        this.handler = handler;
        if(popupWindow != null)
        {
            popupWindow.dismiss();
        }
        updata();
        popupWindow = AppUtils.creatButtomView(context,shade,location,view);
    }

    public View.OnClickListener cancleListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(popupWindow != null)
            {
                popupWindow.dismiss();
            }
            doDestory();
            if(doCancleListener != null)
            {
                doCancleListener.CancleListener();
            }
        }
    };

    public void updata() {
        if(DkhomeApplication.mApp.testManager.chekcFind(device.deviceMac) == false)
        {
            if(persent < 30)
            {
                persent++;
            }
            progressBar.setProgress(persent);
            ptext.setText(String.valueOf(persent)+"%");
        }
        else if(DkhomeApplication.mApp.testManager.chekcConnect(device.deviceMac) == false)
        {
            if(persent < 30)
            {
                persent = 30;
            }
            if(persent < 100)
            {
                persent++;
            }
            progressBar.setProgress(persent);
            ptext.setText(String.valueOf(persent)+"%");
        }
        else
        {
            doDestory();
        }
        if(handler != null)
        {
            handler.removeMessages(UPDTAT_PERSENT);
            handler.sendEmptyMessageDelayed(UPDTAT_PERSENT,170);
        }
    }

    public interface DoCancleListener{
        void CancleListener();
    }

    public void success() {
        if(popupWindow != null)
        {
            popupWindow.dismiss();
        }
        doDestory();
    }

    private void doDestory() {
        if(DkhomeApplication.mApp.testManager.chekcFind(device.deviceMac) == false)
        DkhomeApplication.mApp.testManager.stopConnect(device);
    }
}
