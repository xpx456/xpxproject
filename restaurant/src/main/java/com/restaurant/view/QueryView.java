package com.restaurant.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.restaurant.R;
import com.restaurant.database.DBHelper;
import com.restaurant.entity.Guest;
import com.restaurant.view.adapter.QueryListAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import intersky.mywidget.PopView;

public class QueryView extends PopView {

    public TextView btnSearch;
    public EditText keyword;
    public RecyclerView listview;
    public LinearLayout tebLayer;
    public EditText tebpage;
    public TextView btngo;
    public QueryListAdapter queryListAdapter;
    public QueryListAdapter queryListAdapter2;
    public ArrayList<Guest> guests = new ArrayList<Guest>();
    public ArrayList<Guest> guests2 = new ArrayList<Guest>();
    public ImageView finger;
    public RelativeLayout btnfinger;
    public ImageView iccard;
    public RelativeLayout btniccard;
    public boolean isfinger = false;
    public boolean iscard = false;
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
        input.setOnClickListener(inputListener);
        finger = mainView.findViewById(R.id.finger);
        btnfinger = mainView.findViewById(R.id.query_can_finger);
        btnfinger.setOnClickListener(fingerLisetner);
        iccard = mainView.findViewById(R.id.iccard);
        btniccard = mainView.findViewById(R.id.query_can_card);
        btniccard.setOnClickListener(cardLisetner);
        keyword.addTextChangedListener(textWatcher);
    }

    @Override
    public void destoryView() {
    }


    public void creatView(View location) {
        //setData(pageListData);
        DBHelper.getInstance(context).scanGuest(guests);
        queryListAdapter = new QueryListAdapter(guests,context);
        queryListAdapter2 = new QueryListAdapter(guests2,context);
        queryListAdapter.setOnItemClickListener(queryItemClickListener);
        listview.setAdapter(queryListAdapter);
        super.creatView(location);
    }






    public QueryListAdapter.OnItemClickListener queryItemClickListener = new QueryListAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(Guest guest, int position, View view) {

        }

    };


    public View.OnClickListener inputListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

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

    public TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(keyword.getText().toString().length() > 0)
            {
                guests2.clear();
                for(int i = 0 ; i < guests.size() ; i++)
                {
                    if(guests.get(i).name.contains(keyword.getText().toString()) || guests.get(i).licence.contains(keyword.getText().toString()))
                        guests2.add(guests.get(i));
                }
                if(queryListAdapter2 != null)
                    listview.setAdapter(queryListAdapter2);
            }
            else
            {
                if(queryListAdapter != null)
                    listview.setAdapter(queryListAdapter);
            }
        }
    };

}
