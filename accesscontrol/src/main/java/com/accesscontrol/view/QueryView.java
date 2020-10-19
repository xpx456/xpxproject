package com.accesscontrol.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accesscontrol.R;
import com.accesscontrol.database.DBHelper;
import com.accesscontrol.entity.Guest;
import com.accesscontrol.entity.UserDefine;
import com.accesscontrol.view.adapter.QueryListAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import intersky.apputils.AppUtils;
import intersky.apputils.DoubleDatePickerDialog;
import intersky.apputils.TimeUtils;
import intersky.mywidget.PopView;

public class QueryView extends PopView {

    public TextView btnSearch;
    public EditText keyword;
    public RecyclerView listview;
    public LinearLayout tebLayer;
    public EditText tebpage;
    public TextView btngo;
    public QueryListAdapter queryListAdapter;
    public ImageView finger;
    public RelativeLayout btnfinger;
    public ImageView iccard;
    public RelativeLayout btniccard;
    public boolean isfinger = false;
    public boolean iscard = false;
    public ArrayList<Guest> guests = new ArrayList<Guest>();
    public QueryView(Context context) {
        super(context);
        initView();
    }

    @Override
    public void initView() {
        mainView = LayoutInflater.from(context).inflate(R.layout.view_query, null);
        btnSearch = mainView.findViewById(R.id.searchbtn);
        keyword = mainView.findViewById(R.id.keywordvalue);
        listview = mainView.findViewById(R.id.querylist);
        tebLayer = mainView.findViewById(R.id.tebiconlist);
        tebpage = mainView.findViewById(R.id.tebeditvalue);
        btngo = mainView.findViewById(R.id.went);
        listview.setLayoutManager(new LinearLayoutManager(context));
        close =mainView.findViewById(R.id.view_query);
        View input = mainView.findViewById(R.id.input);
        btnSearch.setOnClickListener(searchListener);
        input.setOnClickListener(inputListener);
        finger = mainView.findViewById(R.id.finger);
        btnfinger = mainView.findViewById(R.id.query_can_finger);
        btnfinger.setOnClickListener(fingerLisetner);
        iccard = mainView.findViewById(R.id.iccard);
        btniccard = mainView.findViewById(R.id.query_can_card);
        btniccard.setOnClickListener(cardLisetner);
    }

    @Override
    public void destoryView() {
    }


    public void creatView(View location) {
        DBHelper.getInstance(context).scanGuest(guests);
        queryListAdapter = new QueryListAdapter(guests,context);
        queryListAdapter.setOnItemClickListener(queryItemClickListener);
        listview.setAdapter(queryListAdapter);
        super.creatView(location);
    }


    public View.OnClickListener searchListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DBHelper.getInstance(context).scanGuest(guests,keyword.getText().toString(),isfinger,iscard);
            queryListAdapter = new QueryListAdapter(guests,context);
            queryListAdapter.setOnItemClickListener(queryItemClickListener);
            listview.setAdapter(queryListAdapter);
        }
    } ;



    public QueryListAdapter.OnItemClickListener queryItemClickListener = new QueryListAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(Guest guest, int position, View view) {

        }

    };


    public View.OnClickListener fingerLisetner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onFingerChange();
        }
    };

    public View.OnClickListener cardLisetner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onCardChange();
        }
    };

    public void onFingerChange()
    {
        if(isfinger == true)
        {
            isfinger = false;
            finger.setImageResource(R.drawable.bunselect);
        }
        else
        {
            isfinger = true;
            finger.setImageResource(R.drawable.bselect);
        }
    }


    public void onCardChange()
    {
        if(iscard == true)
        {
            iscard = false;
            iccard.setImageResource(R.drawable.bunselect);
        }
        else
        {
            iscard = true;
            iccard.setImageResource(R.drawable.bselect);
        }
    }


    public View.OnClickListener inputListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}
