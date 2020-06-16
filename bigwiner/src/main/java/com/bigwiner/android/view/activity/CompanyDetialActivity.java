package com.bigwiner.android.view.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bigwiner.android.entity.Company;
import com.bigwiner.android.presenter.CompanyDetialPresenter;
import com.bigwiner.android.view.adapter.ContactsAdapter;

import java.util.ArrayList;

import intersky.appbase.BaseActivity;
import intersky.appbase.entity.Contacts;
import intersky.conversation.view.adapter.ConversationPageAdapter;
import intersky.mywidget.MyLinearLayout;
import intersky.mywidget.NoScrollViewPager;
import intersky.mywidget.TabHeadView;

/**
 * Created by xpx on 2017/8/18.
 */

public class CompanyDetialActivity extends BaseActivity {
    public ImageView backBtn;
    public ImageView sharBtn;
    public TextView mName;
    public ImageView headImg;
    public Company company;
    public TabHeadView mTabHeadView;
    public ArrayList<ListView> listViews = new ArrayList<ListView>();
    public ArrayList<View> mViews = new ArrayList<View>();
    public NoScrollViewPager mViewPager;
    public ArrayList<ImageView> stars = new ArrayList<ImageView>();
    public ConversationPageAdapter mLoderPageAdapter;
    public ImageView bgImg;
    public TextView city;
    public TextView address;
    public TextView phone;
    public TextView mail;
    public TextView web;
    public TextView cahart;
    public RecyclerView listView;
    public TextView ctxt1;
    public TextView ctxt2;
    public ImageView cimg1;
    public ImageView cimg2;
    public TextView count2;
    public RelativeLayout confirm2;
    public ContactsAdapter contactsAdapter;
    public CompanyDetialPresenter mCompanyDetialPresenter = new CompanyDetialPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCompanyDetialPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mCompanyDetialPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    public View.OnClickListener doShareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCompanyDetialPresenter.doShare();
        }
    };

    public View.OnClickListener confirmListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCompanyDetialPresenter.showConfrim();
        }
    };

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    public ContactsAdapter.OnItemClickListener contactDetitllistener = new ContactsAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(Contacts contacts, int position, View view) {
            mCompanyDetialPresenter.startContact(contacts);
        }

    };
}
