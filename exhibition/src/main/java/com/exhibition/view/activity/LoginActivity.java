package com.exhibition.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.exhibition.R;
import com.exhibition.presenter.LoginPresenter;
import com.iccard.IcCardManager;

import intersky.appbase.BaseActivity;
import intersky.appbase.PadBaseActivity;
import intersky.apputils.AppUtils;

public class LoginActivity extends PadBaseActivity {

    public LoginPresenter mLoginPresenter = new LoginPresenter(this);
    public RelativeLayout botton1;
    public RelativeLayout botton2;
    public ImageView image1;
    public ImageView image2;
    public TextView title1;
    public TextView title2;
    public EditText password;
    public EditText user;
    public TextView btnLogin;
    public TextView logingOut;
    public TextView lastSecond;
    public RelativeLayout fingerlayer;
    public RelativeLayout passwordlayer;
    public ImageView imageView;
    public View hid;
    public boolean cangetdata = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginPresenter.Create();
    }

    @Override
    public void gettouch() {
        mLoginPresenter.updataTimeout();
    }

    @Override
    protected void onDestroy() {
        mLoginPresenter.Destroy();
        super.onDestroy();
    }
    @Override
    protected void onResume(){
        super.onResume();
    }

    public IcCardManager.GetCardId getCardId = new IcCardManager.GetCardId() {
        @Override
        public void getGardId(String id) {
            mLoginPresenter.praseIcCard(id);
        }
    };
}
