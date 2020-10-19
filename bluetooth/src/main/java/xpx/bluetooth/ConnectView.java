package xpx.bluetooth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import intersky.mywidget.PopView;

public class ConnectView extends PopView {

    public TextView tip;
    public int state = 0;
    public ConnectView(Context context) {
        super(context);
        initView();
    }

    @Override
    public void initView() {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainView = inflater.inflate(R.layout.view_finger_get,null);
        tip = mainView.findViewById(R.id.tip);
        close =mainView.findViewById(R.id.view_finger);
    }

    @Override
    public void destoryView() {

    }

    public void creatView(View location) {
        super.creatView(location);
        if(popupWindow != null)
        {
            popupWindow.setOnDismissListener(dismissListener);
        }
    }

    public void setText(String imf) {
        if(tip != null)
        tip.setText(imf);
    }

    public void hid()
    {
        if(popupWindow != null)
        {
            popupWindow.dismiss();
        }
    }

    public PopupWindow.OnDismissListener dismissListener = new PopupWindow.OnDismissListener()
    {

        @Override
        public void onDismiss() {
            if(BluetoothSetManager.bluetoothSetManager.blueConnectThread != null)
            {

                BluetoothSetManager.bluetoothSetManager.blueConnectThread.stop = true;
            }
            BluetoothSetManager.bluetoothSetManager.findper = 1;
            BluetoothSetManager.bluetoothSetManager.findaddress = "";
        }
    };

    public void setTip(String persent)
    {
        tip.setText(context.getString(R.string.connect_start)+persent);
    }

    public void setmessage(String persent)
    {
        tip.setText(persent);
    }
}
