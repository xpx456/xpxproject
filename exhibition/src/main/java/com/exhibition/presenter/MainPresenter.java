package com.exhibition.presenter;

import android.content.Intent;
import android.view.View;

import com.exhibition.R;
import com.exhibition.view.ExhibitionApplication;
import com.exhibition.view.QueryView;
import com.exhibition.view.RegisterView;
import com.exhibition.view.activity.AboutActivity;
import com.exhibition.view.activity.MainActivity;

import org.json.JSONException;

import intersky.appbase.Presenter;

public class MainPresenter implements Presenter {

    public MainActivity mMainActivity;

    public MainPresenter(MainActivity MainActivity) {
        mMainActivity = MainActivity;
    }

    @Override
    public void initView() {
        mMainActivity.setContentView(R.layout.activity_main);
        mMainActivity.title = mMainActivity.findViewById(R.id.maintitle);
        mMainActivity.btn1 = mMainActivity.findViewById(R.id.main_btn1);
        mMainActivity.btn2 = mMainActivity.findViewById(R.id.main_btn2);
        mMainActivity.btn3 = mMainActivity.findViewById(R.id.main_btn3);
        mMainActivity.btn4 = mMainActivity.findViewById(R.id.main_btn4);
        mMainActivity.btn5 = mMainActivity.findViewById(R.id.main_btn5);
        mMainActivity.btn6 = mMainActivity.findViewById(R.id.main_btn6);
        mMainActivity.exist = mMainActivity.findViewById(R.id.exist);
        mMainActivity.btn1.setOnClickListener(registerListener);
        mMainActivity.btn2.setOnClickListener(aboutListener);
        mMainActivity.btn3.setOnClickListener(querListener);
        mMainActivity.btn4.setOnClickListener(baseSettingListener);
        mMainActivity.btn5.setOnClickListener(netSettingListener);
        mMainActivity.btn6.setOnClickListener(updateListener);
        mMainActivity.exist.setOnClickListener(existListner);
        mMainActivity.queryView = new QueryView(mMainActivity);
        mMainActivity.registerView = new RegisterView(mMainActivity);
        String name = "";
        try {
             name = ExhibitionApplication.mApp.setjson.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mMainActivity.title.setText(mMainActivity.getString(R.string.main_title1)+name+mMainActivity.getString(R.string.main_title2));
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void Start() {

    }

    @Override
    public void Resume() {

    }

    @Override
    public void Pause() {

    }

    @Override
    public void Destroy() {

    }
    //1168 689
    public View.OnClickListener registerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMainActivity.registerView.creatView(mMainActivity.findViewById(R.id.activity_main));
        }
    };

    public View.OnClickListener aboutListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startAbout();
        }
    };

    public View.OnClickListener querListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMainActivity.queryView.creatView(mMainActivity.findViewById(R.id.activity_main));
        }
    };

    public View.OnClickListener baseSettingListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };

    public View.OnClickListener netSettingListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    public View.OnClickListener updateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    public View.OnClickListener existListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMainActivity.finish();
        }
    };

    public void updataTimeout() {
        ExhibitionApplication.mApp.setTimeMax();
    }

    private void startAbout() {
        Intent intent = new Intent(mMainActivity, AboutActivity.class);
        mMainActivity.startActivity(intent);
    }
}
