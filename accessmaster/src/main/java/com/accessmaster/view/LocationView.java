package com.accessmaster.view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.accessmaster.R;
import com.accessmaster.entity.Location;

import java.util.HashMap;

import intersky.mywidget.PopView;


public class LocationView extends PopView {

    public static final String ACTION_GET_LOCATION = "ACTION_GET_LOCATION";
    public TextView text;
    public RelativeLayout input;
    public LinearLayout listView;
    public HashMap<String, View> viewHashMap = new HashMap<String, View>();
    public LocationView(Context context) {
        super(context);
        initView();
    }

    @Override
    public void initView(){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainView = inflater.inflate(R.layout.location_view, null);
        close = mainView.findViewById(R.id.location);
        input = mainView.findViewById(R.id.input);
        listView = mainView.findViewById(R.id.locationlist);
        input.setOnClickListener(hidInputListener);
    }

    public void creatView(View location) {
        initList();
        super.creatView(location);
    }

    public void openLocation(Location location) {
        location.isopen = true;
        View my = viewHashMap.get(location.addressid);
        TextView textView = my.findViewById(R.id.open);
        textView.setText("-");
        int index = listView.indexOfChild(my);
        initLocatons(location,index+1);
    }

    public void closeLocation(Location location) {
        location.isopen = false;
        View my = viewHashMap.get(location.addressid);
        TextView textView = my.findViewById(R.id.open);
        textView.setText("+");
        removViews(location);
    }

    public void removViews(Location location)
    {
        for(int i = 0 ; i < location.locations.size() ; i++)
        {
            removeLocation(location.locations.get(i));
            if(location.locations.get(i).isopen)
            removViews(location.locations.get(i));
        }
    }

    public void removeLocation(Location location)
    {
        View my = viewHashMap.get(location.addressid);
        if(my != null)
        {
            listView.removeView(my);
        }
    }

    public void initList() {
        listView.removeAllViews();
        for(int i = 0 ; i < AccessMasterApplication.mApp.locations.size() ;i++)
        {
            Location location = AccessMasterApplication.mApp.locations.get(i);
            addView(location);
            if(location.isopen)
            {
                initLocatons(location);
            }
        }
    }

    public void initLocatons(Location location) {
        for(int i = 0 ; i < location.locations.size() ;i++)
        {
            Location temp = location.locations.get(i);
            addView(temp);
            if(temp.isopen)
            {
                initLocatons(temp);
            }
        }
    }

    public int initLocatons(Location location,int index) {
        int count = 0;
        for(int i = 0 ; i < location.locations.size() ;i++)
        {
            Location temp = location.locations.get(i);
            addView(temp,index+count);
            count++;
            if(temp.isopen)
            {
                count += initLocatons(temp,index+count);
            }
        }
        return count;
    }

    public void addView(Location location) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.location_item, null);
        TextView open = view.findViewById(R.id.open);
        TextView title = view.findViewById(R.id.title);
        title.setText(location.address);
        view.setTag(location);
        viewHashMap.put(location.addressid,view);
        view.setOnClickListener(locationListener);
        if(location.locations.size() == 0) {
            open.setVisibility(View.INVISIBLE);
        }
        else {
            open.setVisibility(View.VISIBLE);
            open.setTag(location);
            open.setOnClickListener(experListener);
            if(location.isopen) {
                open.setText("-");
            }
            else {
                open.setText("+");

            }
        }
        listView.addView(view);

    }

    public void addView(Location location,int index) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.location_item, null);
        TextView open = view.findViewById(R.id.open);
        TextView title = view.findViewById(R.id.title);
        title.setText(location.address);
        view.setTag(location);
        viewHashMap.put(location.addressid,view);
        view.setOnClickListener(locationListener);
        if(location.locations.size() == 0) {
            open.setVisibility(View.INVISIBLE);
        }
        else {
            open.setVisibility(View.VISIBLE);
            open.setTag(location);
            open.setOnClickListener(experListener);
            if(location.isopen) {
                open.setText("-");
            }
            else {
                open.setText("+");

            }
        }
        listView.addView(view,index);

    }

    public View.OnClickListener locationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Location location = (Location) v.getTag();
            Intent intent = new Intent(LocationView.ACTION_GET_LOCATION);
            intent.putExtra("id",location.addressid);
            intent.putExtra("name",location.address);
            context.sendBroadcast(intent);
            hidView();
        }
    };

    public View.OnClickListener experListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Location location = (Location) v.getTag();
            if(location.isopen)
            {
                closeLocation(location);
            }
            else
            {
                openLocation(location);

            }
        }
    };

    private View.OnClickListener hidInputListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            InputMethodManager imm2 = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm2.hideSoftInputFromWindow(name.getWindowToken(), 0);

        }
    };
}



