package com.exhibition.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.exhibition.entity.Guest;
import com.exhibition.presenter.MainPresenter;
import com.exhibition.presenter.RegisterPresenter;
import com.iccard.IcCardManager;

import java.util.ArrayList;
import java.util.List;

import intersky.appbase.PadBaseActivity;
import intersky.mywidget.MyLinearLayout;

public class RegisterActivity extends PadBaseActivity {

    public RegisterPresenter mRegisterPresenter = new RegisterPresenter(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRegisterPresenter.Create();
    }

    @Override
    public void gettouch() {
        mRegisterPresenter.updataTimeout();
    }

    @Override
    protected void onDestroy() {
        mRegisterPresenter.Destroy();
        super.onDestroy();
    }

    public IcCardManager.GetCardId getCardId = new IcCardManager.GetCardId() {
        @Override
        public void getGardId(String id) {
            mRegisterPresenter.praseIcCard(id);
        }
    };
}
