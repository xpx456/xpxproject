package com.exhibition.view;

import android.content.Context;
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
import com.exhibition.view.adapter.QueryListAdapter;

import intersky.apputils.AppUtils;
import intersky.apputils.DoubleDatePickerDialog;
import intersky.apputils.TimeUtils;

public class QueryView {

    public Context context;
    public View mainView;
    public RelativeLayout btnDate;
    public TextView data;
    public TextView btnSearch;
    public EditText keyword;
    public RecyclerView listview;
    public LinearLayout tebLayer;
    public EditText tebpage;
    public TextView btngo;
    public MyPageListData pageListData;
    public QueryListAdapter queryListAdapter;
    public PopupWindow popupWindow;
    public QueryView(Context context) {
        this.context = context;
    }

    public AppUtils.InitView initView = new AppUtils.InitView() {
        @Override
        public void initView(View view) {
            mainView = view;
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
        }
    };

    public void creatView(View location) {
        popupWindow = AppUtils.creatPopView(context,R.layout.view_query,R.id.view_query,location,initView);
        data.setText(TimeUtils.getDate());
        MyPageListData pageListData = new MyPageListData(DBHelper.getInstance(context).scanGuest(TimeUtils.getDate(),""));
        setData(pageListData);
    }

    public void hidView() {
        if(popupWindow != null)
        {
            popupWindow.dismiss();
        }
    }

    private void setData(MyPageListData pageListData) {
        this.pageListData = pageListData;
        queryListAdapter = new QueryListAdapter(pageListData.getShowPage(),context);
        queryListAdapter.setOnItemClickListener(queryItemClickListener);
        listview.setAdapter(queryListAdapter);

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

        }

    };

    public View.OnClickListener pageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}
