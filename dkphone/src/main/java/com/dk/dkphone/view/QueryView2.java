package com.dk.dkphone.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dk.dkphone.R;
import com.dk.dkphone.view.adapter.CodeDataAdapter;

import intersky.mywidget.PopView;


public class QueryView2 extends PopView {

    public ListView listview;
    public ListView listview2;
    public CodeDataAdapter queryListAdapter;
    public CodeDataAdapter queryListAdapter2;
    public ImageView closebtn;
    public TextView reflash;
    public QueryView2(Context context) {
        super(context);
        initView();

    }

    @Override
    public void initView() {
        mainView = LayoutInflater.from(context).inflate(R.layout.view_query, null);
        listview = mainView.findViewById(R.id.equiplist);
        listview2 = mainView.findViewById(R.id.equiplist2);
        reflash = mainView.findViewById(R.id.reflash);
        reflash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryListAdapter.notifyDataSetChanged();
            }
        });
        close =mainView.findViewById(R.id.view_query);
        closebtn = mainView.findViewById(R.id.close);
        queryListAdapter = new CodeDataAdapter(context, DkPhoneApplication.mApp.testManager.datagetList);
        listview.setAdapter(queryListAdapter);
        queryListAdapter2 = new CodeDataAdapter(context, DkPhoneApplication.mApp.testManager.datagetList2);
        listview2.setAdapter(queryListAdapter2);
        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hidView();
            }
        });
    }

    @Override
    public void destoryView() {
    }


    public void creatView(View location) {
        queryListAdapter.notifyDataSetChanged();
        super.creatView(location);
    }


}
