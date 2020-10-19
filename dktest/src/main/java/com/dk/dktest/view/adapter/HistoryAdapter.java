package com.dk.dktest.view.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dk.dktest.R;
import com.dk.dktest.database.DBHelper;
import com.dk.dktest.entity.TestRecord;
import com.dk.dktest.view.DkTestApplication;

import java.util.ArrayList;

import intersky.mywidget.conturypick.DbHelper;
import xpx.bluetooth.BluetoothSetManager;

public class HistoryAdapter extends BaseAdapter {


    public ArrayList<TestRecord> testRecords;
    public Context context;
    private LayoutInflater mInflater;
    public View root;
    public HistoryAdapter(Context context, ArrayList<TestRecord> testRecords, View root) {
        this.testRecords = testRecords;
        this.context = context;
        this.root = root;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return testRecords.size();
    }

    @Override
    public TestRecord getItem(int i) {
        return testRecords.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        TestRecord record = getItem(position);
        ViewHoder mview = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_record, null);
            mview = new ViewHoder();
            mview.mtitle = (TextView) convertView.findViewById(R.id.record_name);
            mview.mac = (TextView) convertView.findViewById(R.id.record_time);
            mview.connect = convertView.findViewById(R.id.delete);
            convertView.setTag(mview);
        } else {
            mview = (ViewHoder) convertView.getTag();
        }
        mview.mtitle.setText(record.name);
        mview.mac.setText("测试时间："+record.time);

        mview.connect.setTag(record);
        mview.connect.setOnClickListener(deletetListener);
        return convertView;
    }

    public String measureDur(int dur) {
        if(dur/1000/60/60 > 0 )
        {
            int h = dur/1000/60/60;
            int m = (dur/1000/60)%60;
            int s = (dur/1000)%60;
            return String.format("%d小时%02d分%02d秒",h,m,s);
        }
        else if(dur/1000/60 > 0 )
        {
            int m = (dur/1000/60)%60;
            int s = (dur/1000)%60;
            return String.format("%02d分%02d秒",m,s);
        }
        else
        {
            int s = (dur/1000)%60;
            return String.format("%02d秒",s);
        }


    }
    public  class ViewHoder {
        TextView mac;
        TextView mtitle;
        TextView connect;
    };

    public View.OnClickListener deletetListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TestRecord testRecord = (TestRecord) view.getTag();
            DBHelper.getInstance(context).deleteServer(testRecord);
            testRecords.remove(testRecord);
            notifyDataSetChanged();
        }
    };
}
