package com.dk.dkpad.view.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dk.dkpad.R;
import com.dk.dkpad.database.DBHelper;
import com.dk.dkpad.entity.SportData;
import com.dk.dkpad.entity.User;
import com.dk.dkpad.handler.MainHandler;
import com.dk.dkpad.view.DkPadApplication;
import com.dk.dkpad.view.activity.MainActivity;
import com.dk.dkpad.view.activity.OptationActivity;
import com.dk.dkpad.view.adapter.PkUserAdapter;
import com.dk.dkpad.view.adapter.UserAdapter;

import java.io.File;

import intersky.appbase.MySimpleTarget;
import intersky.apputils.UtilBitmap;
import intersky.apputils.UtilScreenCapture;
import intersky.echartoption.ArrayData;
import intersky.echartoption.ObjectData;
import intersky.mywidget.conturypick.DbHelper;

public class PersonFragment extends BaseFragment {

    public MainActivity mMainActivity;
    public WebView chartShow;
    public RelativeLayout pkUser;
    public ListView pkusrList;
    public ImageView btnPk;
    public ImageView myHead;
    public ImageView pkHead;
    public TextView pkName;
    public RelativeLayout bg;
    public ImageView bg2;
    public SportData sportData;

    public PersonFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this
        mMainActivity = (MainActivity) getActivity();
        View mView = inflater.inflate(R.layout.fragment_person, container, false);
        bg = mView.findViewById(R.id.bg);
        bg2 = mView.findViewById(R.id.bg2);
        pkUser = mView.findViewById(R.id.pkuser);
        pkusrList = mView.findViewById(R.id.headlist);
        pkusrList.setAdapter(mMainActivity.mMainPresenter.pkUserAdapter);
        pkusrList.setOnItemClickListener(userItemListener);
        btnPk = mView.findViewById(R.id.btnpk);
        myHead = mView.findViewById(R.id.headmy);
        pkHead = mView.findViewById(R.id.head);
        pkName = mView.findViewById(R.id.cardname);
        chartShow = mView.findViewById(R.id.chart);
        chartShow.getSettings().setAllowFileAccess(true);
        chartShow.getSettings().setJavaScriptEnabled(true);
        chartShow.setWebViewClient(mMainActivity.mWebViewClient);
        chartShow.setVisibility(View.VISIBLE);
        chartShow.setBackgroundColor(0); // 设置背景色
        chartShow.getBackground().setAlpha(0);
        chartShow.loadUrl("file:///android_asset/echart/myechart.html");
        mMainActivity.mMainPresenter.mainHandler.sendEmptyMessageDelayed(MainHandler.INIT_PERSION_CHART_VIEW, 800);
        btnPk.setOnClickListener(showHeadListener);
        mMainActivity.mMainPresenter.mainHandler.sendEmptyMessageDelayed(MainHandler.UPDATA_SET_MY_VIEW, 800);
        return mView;
    }

    public View.OnClickListener showHeadListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (pkUser.getVisibility() == View.VISIBLE) {
                pkUser.setVisibility(View.INVISIBLE);
            } else {
                pkUser.setVisibility(View.VISIBLE);
                mMainActivity.mMainPresenter.userAdapter.notifyDataSetChanged();

            }

        }
    };

    public AdapterView.OnItemClickListener userItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            PkUserAdapter userAdapter = (PkUserAdapter) adapterView.getAdapter();
            User user = userAdapter.getItem(i);
            if (DkPadApplication.mApp.pk != null) {
                if (user.select == true) {
                    user.select = false;
                    hidCardView();
                } else {
                    DkPadApplication.mApp.pk.select = false;
                    DkPadApplication.mApp.pk = user;
                    DkPadApplication.mApp.pk.select = true;
                    showCardView();
                }

            } else {
                DkPadApplication.mApp.pk = user;
                DkPadApplication.mApp.pk.select = true;
                showCardView();
            }

        }
    };

    public void showCardView() {

        if (DkPadApplication.mApp.pk.headpath.length() > 0) {
            pkHead.setVisibility(View.VISIBLE);
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.default_user);
            Glide.with(mMainActivity).load(new File(DkPadApplication.mApp.pk.headpath)).apply(options).into(new MySimpleTarget(pkHead));
        } else {
            pkHead.setVisibility(View.INVISIBLE);
        }
        pkName.setText(DkPadApplication.mApp.pk.name);
        sportData = DBHelper.getInstance(mMainActivity).scanRecords(DkPadApplication.mApp.pk.uid);
        if(mMainActivity.othervalue == null)
        {
            mMainActivity.othervalue = new ObjectData();
            mMainActivity.seriesData.add(mMainActivity.othervalue);
        }
        ArrayData value = new ArrayData();
        mMainActivity.othervalue.put("value", value);
        value.add(sportData.getdayTime());
        value.add(sportData.getDayDistence());
        value.add(sportData.getaverLeavel());
        value.add(sportData.getrat());
        value.add(sportData.gettopspeed());
        value.add(sportData.getaspeed());
        mMainActivity.othervalue.put("name", DkPadApplication.mApp.pk.name);
        mMainActivity.personopLegend.add(DkPadApplication.mApp.pk.name);
        mMainActivity.seriesItem.put("name",DkPadApplication.mApp.selectUser.name+" VS "+
                DkPadApplication.mApp.pk.name);
        pkUser.setVisibility(View.INVISIBLE);
        initView();
    }

    public void hidCardView() {
        pkHead.setVisibility(View.INVISIBLE);
        pkName.setText("");
        if(mMainActivity.seriesData.array.size() > 1)
        {
            mMainActivity.seriesData.array.remove(1);
            mMainActivity.othervalue = null;
        }
        if(mMainActivity.personopLegend.array.size() > 1)
            mMainActivity.personopLegend.array.remove(1);
        mMainActivity.personTitle.put("text",DkPadApplication.mApp.selectUser.name);
        pkUser.setVisibility(View.INVISIBLE);
        initView();
    }

    public void initView() {
        if (chartShow != null) {
            String url = "javascript:createChart('orther'," + mMainActivity.personoption.toString() + ");";
            chartShow.loadUrl(url);
        }
    }

    public void setMyView() {
        if (DkPadApplication.mApp.selectUser != null) {
            if (DkPadApplication.mApp.selectUser.headpath.length() > 0) {
                File file = new File(DkPadApplication.mApp.selectUser.headpath);
                if (file.exists()) {
                    Glide.with(mMainActivity).load(new File(DkPadApplication.mApp.selectUser.headpath)).into(new MySimpleTarget(myHead));
                }
            }
            else
            {
                myHead.setImageDrawable(null);
            }
            Bitmap bitmap = UtilScreenCapture.getDrawing(bg);
            bg2.setImageBitmap(bitmap);
            UtilBitmap.blurImageView(mMainActivity, bg2, 5, 0x55ffffff);
        }
    }

    public void updataSport() {
        if(mMainActivity != null)
        {
            setMyView();
            initView();
            if(DkPadApplication.mApp.pk == null)
            {
                hidCardView();
            }
            else
            {
                showCardView();
            }
        }
    }
}
