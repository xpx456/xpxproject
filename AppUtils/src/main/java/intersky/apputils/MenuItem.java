package intersky.apputils;

import android.view.View;

public class MenuItem {

    public String btnName;
    public Object item;
    public boolean select = false;
    public View.OnClickListener mListener;

    public MenuItem()
    {

    }

    public MenuItem(String btnName, View.OnClickListener mListener)
    {
        this.btnName = btnName;
        this.mListener = mListener;
    }

    public MenuItem(String btnName, View.OnClickListener mListener,Object object)
    {
        this.item = object;
        this.btnName = btnName;
        this.mListener = mListener;
    }
}
