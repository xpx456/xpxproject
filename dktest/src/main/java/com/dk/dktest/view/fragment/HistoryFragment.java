package com.dk.dktest.view.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dk.dktest.R;
import com.dk.dktest.database.DBHelper;
import com.dk.dktest.entity.TestRecord;
import com.dk.dktest.view.DkTestApplication;
import com.dk.dktest.view.activity.HistoryDetialActivity;
import com.dk.dktest.view.activity.MainActivity;
import com.dk.dktest.view.adapter.HistoryAdapter;

import java.util.ArrayList;

import intersky.appbase.BaseFragment;
import intersky.apputils.AppUtils;
import intersky.apputils.DoubleDatePickerDialog;
import intersky.apputils.TimeUtils;
import intersky.mywidget.SearchViewLayout;
import intersky.mywidget.conturypick.Adapter;

public class HistoryFragment extends BaseFragment {

//    public MainPresenter mMainPresenter;
    public MainActivity mMainActivity;
    public ListView listView;
    public HistoryAdapter historyAdapter;
    public HistoryAdapter historySAdapter;
    public TextView date;
    public RelativeLayout btnDate;
    public SearchViewLayout searchViewLayout;
    public boolean issearch = false;
    public HistoryFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainActivity = (MainActivity) getActivity();
        View mView = inflater.inflate(R.layout.fragment_history, container, false);
        mMainActivity.measureStatubar(mMainActivity, (RelativeLayout) mView.findViewById(R.id.stutebar));
        listView = mView.findViewById(R.id.historylist);
        date = mView.findViewById(R.id.date);
        btnDate = mView.findViewById(R.id.btndata);
        searchViewLayout = mView.findViewById(R.id.search);
        searchViewLayout.setDotextChange(doTextChange);
        date.setText(TimeUtils.getDate());
        btnDate.setOnClickListener(setDateListener);
        DBHelper.getInstance(mMainActivity).scanRecords(TimeUtils.getDate(),"");
        historyAdapter = new HistoryAdapter(mMainActivity, DkTestApplication.mApp.testRecords,mMainActivity.findViewById(R.id.history));
        historySAdapter = new HistoryAdapter(mMainActivity, DkTestApplication.mApp.testSRecords,mMainActivity.findViewById(R.id.history));
        listView.setAdapter(historyAdapter);
        listView.setOnItemClickListener(onItemClickListener);
        return mView;
    }


    public AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            TestRecord testRecord = (TestRecord) adapterView.getAdapter().getItem(i);
            Intent intent = new Intent(mMainActivity, HistoryDetialActivity.class);
            intent.putExtra("record",testRecord);
            mMainActivity.startActivity(intent);
        }
    };

    public View.OnClickListener setDateListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            setDate();
        }
    };

    private DoubleDatePickerDialog.OnDateSetListener dateResultListener = new DoubleDatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, int hour, int miniute) {
            String time = String.format("%04d-%02d-%02d",startYear,startMonthOfYear,startDayOfMonth);

            date.setText(time);
            DkTestApplication.mApp.testSRecords.clear();
            DkTestApplication.mApp.testRecords.clear();
            DBHelper.getInstance(mMainActivity).scanRecords(time,searchViewLayout.getText());
            historyAdapter.notifyDataSetChanged();
            historySAdapter.notifyDataSetChanged();
        }


    };

    public void setDate()
    {
        AppUtils.creatDataPicker(mMainActivity,date.getText().toString(),mMainActivity.getString(R.string.schdule_select_time)
                , dateResultListener);
    }

    public SearchViewLayout.DoTextChange doTextChange = new SearchViewLayout.DoTextChange()
    {

        @Override
        public void doTextChange(boolean visiable) {
            if(visiable)
            {
                DkTestApplication.mApp.testSRecords.clear();
                for(int i = 0 ; i < DkTestApplication.mApp.testRecords.size() ; i++)
                {
                    if(DkTestApplication.mApp.testRecords.get(i).name.contains(searchViewLayout.getText()))
                    DkTestApplication.mApp.testSRecords.add(DkTestApplication.mApp.testRecords.get(i));
                }

                if(issearch == false) {
                    issearch = true;
                    listView.setAdapter(historySAdapter);
                }
                else
                {
                    historySAdapter.notifyDataSetChanged();
                }
            }
            else {
                if (issearch == true) {
                    issearch = false;
                    listView.setAdapter(historyAdapter);
                }
                else
                {
                    historyAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    public void updata()
    {
        DBHelper.getInstance(mMainActivity).scanRecords(date.getText().toString(),
                searchViewLayout.getText());
    }
}
