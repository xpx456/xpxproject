package com.restaurant.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.restaurant.R;

import intersky.apputils.AppUtils;


public class SuccessView {

    public Context context;
    public View mainView;
    public TextView user;
    public TextView type;
    public TextView cast;
    public TextView last;
    public PopupWindow popupWindow;
    public AppUtils.InitView initView = new AppUtils.InitView() {
        @Override
        public void initView(View view) {
            mainView = view;
            user = mainView.findViewById(R.id.uservalue);
            type = mainView.findViewById(R.id.typevalue);
            cast = mainView.findViewById(R.id.castvalue);
            last = mainView.findViewById(R.id.lastvalue);

        }
    };

    public SuccessView(Context context) {
        this.context = context;
    }

    public void creatView(View location) {
        popupWindow = AppUtils.creatPopView(context,R.layout.view_success,R.id.success,location,initView);
    }

    public void hid() {
        if(popupWindow != null)
        popupWindow.dismiss();
    }


}



