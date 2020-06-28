package intersky.mywidget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import intersky.apputils.AppUtils;

public class PopView implements PopIntFace{

    public Context context;
    public PopupWindow popupWindow;
    public View mainView;
    public View close;
    public View location;
    public PopView(Context context) {
         this.context = context;
    }

    @Override
    public void creatView(View location) {
        this.location = location;
        cleanView();
        popupWindow = new PopupWindow(mainView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
        mainView.setFocusable(true);
        mainView.setFocusableInTouchMode(true);
        popupWindow.setAnimationStyle(intersky.apputils.R.style.PopupAnimation);
        if(close != null)
        {
            close.setFocusable(true);
            close.setFocusableInTouchMode(true);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainView.setFocusableInTouchMode(true);
                    popupWindow.dismiss();
                }
            });
        }
        ColorDrawable dw = new ColorDrawable(0x00ffffff);
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.setOnDismissListener(dismissListener);
        if(location != null)
        {
            popupWindow.showAtLocation(location,
                    Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
    }

    @Override
    public void initView() {

    }

    @Override
    public void destoryView() {

    }


    @Override
    public void hidView() {
        if(popupWindow != null)
        {
            popupWindow.dismiss();
        }

    }

    @Override
    public void cleanView() {

    }

    public PopupWindow.OnDismissListener dismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            destoryView();
        }
    };
}
