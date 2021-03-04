package com.exhibition.view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.exhibition.R;
import com.exhibition.database.DBHelper;
import com.exhibition.entity.Guest;
import com.exhibition.entity.MyPageListData;
import com.exhibition.view.activity.RegisterActivity;
import com.exhibition.view.adapter.QueryListAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import intersky.apputils.AppUtils;
import intersky.apputils.DoubleDatePickerDialog;
import intersky.apputils.TimeUtils;
import intersky.mywidget.PopView;

public class QueryView extends PopView {

    public RelativeLayout btnDate;
    public TextView data;
    public TextView btnSearch;
    public EditText keyword;
    public RecyclerView listview;
    public LinearLayout tebLayer;
    public EditText tebpage;
    public TextView btngo;
    public QueryListAdapter queryListAdapter;
    public PopupWindow popupWindow;
    public HashMap<String,Guest> hashMap;
    public ArrayList<Guest> guests = new ArrayList<Guest>();
    public QueryView(Context context) {
        super(context);
        initView();
    }

    @Override
    public void initView() {
        mainView = LayoutInflater.from(context).inflate(R.layout.view_query, null);
        btnDate = mainView.findViewById(R.id.date);
        data = mainView.findViewById(R.id.datevalue);
        btnSearch = mainView.findViewById(R.id.searchbtn);
        keyword = mainView.findViewById(R.id.keywordvalue);
        listview = mainView.findViewById(R.id.querylist);
        tebLayer = mainView.findViewById(R.id.tebiconlist);
        tebpage = mainView.findViewById(R.id.tebeditvalue);
        btngo = mainView.findViewById(R.id.went);
        data.setOnClickListener(detepickListener);
        listview.setLayoutManager(new LinearLayoutManager(context));
        close =mainView.findViewById(R.id.view_query);
        View input = mainView.findViewById(R.id.input);
        input.setOnClickListener(inputListener);
    }

    @Override
    public void destoryView() {
    }


    public void creatView(View location) {
        data.setText(TimeUtils.getDate());
        hashMap = DBHelper.getInstance(context).scanGuest(TimeUtils.getDate(),"",guests);
        //setData(pageListData);

        queryListAdapter = new QueryListAdapter(guests,context);
        queryListAdapter.setOnItemClickListener(queryItemClickListener);
        listview.setAdapter(queryListAdapter);
        super.creatView(location);
    }


    private void setData(MyPageListData pageListData) {


        if(pageListData.getTotalpage() > 1)
        {
            addPageIcon("<",pageListData);
        }
        for (int i = pageListData.getStarpageshow() ; i < pageListData.getEndpageshow() ; i++)
        {
            addPageIcon(String.valueOf(i),pageListData);
        }
        if(pageListData.getTotalpage() > 1)
        {
            addPageIcon(String.valueOf(">"),pageListData);
        }
    }

    private void addPageIcon(String i,MyPageListData pageListData) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View pageicon = inflater.inflate(R.layout.page_item,null);
        TextView icon = pageicon.findViewById(R.id.icon);
        icon.setText(i);
        icon.setTag(i);
        if(String.valueOf(pageListData.getPageid()).equals(i))
        {
            icon.setBackgroundResource(R.drawable.pag_btn_blue);
        }
        else
        {
            icon.setBackgroundResource(R.drawable.pag_btn_gry);
        }
        icon.setOnClickListener(pageClickListener);
    }

    public void onDataPick() {
        AppUtils.creatDataPicker(context, data.getText().toString(), context.getString(R.string.query_time), mOnDaySetListener);
    }

    public View.OnClickListener detepickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onDataPick();
        }
    };

    public DoubleDatePickerDialog.OnDateSetListener mOnDaySetListener = new DoubleDatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth, int hour, int miniute) {
            String textString = String.format("%04d-%02d-%02d", startYear, startMonthOfYear + 1, startDayOfMonth);
            data.setText(textString);
        }
    };

    public QueryListAdapter.OnItemClickListener queryItemClickListener = new QueryListAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(Guest guest, int position, View view) {
            Intent intent = new Intent(context, RegisterActivity.class);
            intent.putExtra("rid",guest.rid);
            context.startActivity(intent);
        }

    };

    public View.OnClickListener pageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    public View.OnClickListener inputListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}
