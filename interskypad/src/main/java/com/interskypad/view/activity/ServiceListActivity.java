package com.interskypad.view.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.interskypad.presenter.ServiceListPresenter;

import intersky.appbase.PadBaseActivity;
import intersky.xpxnet.net.Service;

/**
 * Created by xpx on 2017/8/18.
 */

public class ServiceListActivity extends PadBaseActivity {

    public static final String ACTION_SERVICE_DELETE = "ACTION_SERVICE_DELETE";
    public ListView serviceList;
    public RelativeLayout root;
    public ServiceListPresenter mServiceListPresenter = new ServiceListPresenter(this);


    public ServiceListActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mServiceListPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mServiceListPresenter.Destroy();
        super.onDestroy();
    }

    public AdapterView.OnItemClickListener onServiceItemClickListener = new AdapterView.OnItemClickListener()
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mServiceListPresenter.doEditService((Service) parent.getAdapter().getItem(position));
        }
    };

    public AdapterView.OnItemLongClickListener onServiceItemLongClickListener = new AdapterView.OnItemLongClickListener()
    {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            mServiceListPresenter.deleteService((Service) parent.getAdapter().getItem(position));
            return false;
        }

    };

    public View.OnClickListener mAddNewServiceListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mServiceListPresenter.doAddService();
        }
    };


    public boolean onTouchEvent(MotionEvent event) {

        //获得触摸的坐标
        float x = event.getX();
        float y = event.getY(); switch (event.getAction())
        {
            //触摸屏幕时刻
            case MotionEvent.ACTION_DOWN:

                break;
            //触摸并移动时刻
            case MotionEvent.ACTION_MOVE:

                break;
            //终止触摸时刻
            case MotionEvent.ACTION_UP:
                if(!(root.getX() < x && root.getX()+root.getWidth() > x
                        && root.getY() < y && root.getY()+root.getHeight() > y))
                {
                    finish();
                }
                break;
        }
        return true;
    }

}
