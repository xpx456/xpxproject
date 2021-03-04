package com.bigwiner.android.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigwiner.android.entity.Company;
import com.bigwiner.android.presenter.ContactDetialPresenter;

import java.util.ArrayList;

import intersky.appbase.BaseActivity;
import intersky.appbase.entity.Contacts;
import intersky.mywidget.CircleImageView;
import intersky.mywidget.MyLinearLayout;

/**
 * Created by xpx on 2017/8/18.
 */

public class ContactDetialActivity extends BaseActivity {

    public ContactDetialPresenter mContactDetialPresenter = new ContactDetialPresenter(this);
    public Contacts contacts;
    public Company company;
    public ImageView backBtn;
    public ImageView sharBtn;
    public CircleImageView headImg;
    public TextView mName;
    public TextView mAdd;
    public TextView vip;
    public ImageView mAddimg;
    public RelativeLayout addBtn;
    public RelativeLayout chatBtn;
    public ImageView sexImg;
    public MyLinearLayout itemLayers;
    public ArrayList<ImageView> stars = new ArrayList<ImageView>();
    public TextView desTxt;
    public TextView typeValue;
    public TextView areaValue;
    public TextView companyValue;
    public TextView addressValue;
    public TextView dertyValue;
    public TextView companyBtn;
    public TextView complaint;
    public TextView mailValue;
    public TextView mobil;
    public ImageView bgImg;
    public RelativeLayout shade;
    public PopupWindow popupWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContactDetialPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mContactDetialPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    public View.OnClickListener startChatListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mContactDetialPresenter.startChat();
        }
    };

    public View.OnClickListener startCompanyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mContactDetialPresenter.startCompany();
        }
    };

    public View.OnClickListener addFriendListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mContactDetialPresenter.addFriend();
        }
    };

    public View.OnClickListener delFriendListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mContactDetialPresenter.delFriend();
        }
    };
    public View.OnClickListener mTakePhotoListenter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mContactDetialPresenter.takePhoto();
        }
    };

    public View.OnClickListener mAddPicListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mContactDetialPresenter.pickPhoto();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        mContactDetialPresenter.takePhotoResult(requestCode,resultCode,data);

    }
}
