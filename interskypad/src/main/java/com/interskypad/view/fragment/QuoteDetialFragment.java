package com.interskypad.view.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.interskypad.R;
import com.interskypad.asks.OrderAsks;
import com.interskypad.database.DBHelper;
import com.interskypad.database.OrderDbHelper;
import com.interskypad.entity.Catalog;
import com.interskypad.entity.Customer;
import com.interskypad.entity.Order;
import com.interskypad.handler.MainHandler;
import com.interskypad.manager.CustomerManager;
import com.interskypad.manager.OrderManager;
import com.interskypad.presenter.MainPresenter;
import com.interskypad.view.InterskyPadApplication;
import com.interskypad.view.activity.MainActivity;
import com.interskypad.view.adapter.QuoteDetialAdapter;

import java.util.ArrayList;

import intersky.appbase.BaseFragment;
import intersky.apputils.AppUtils;
import intersky.mywidget.SpinnerView;

@SuppressLint("ValidFragment")
public class QuoteDetialFragment extends BaseFragment {

    public MainPresenter mMainPresenter;
    public TextView mTimeText;
    public SpinnerView spinnerView;
    public EditText mDescribeEditText;
    public TextView mImfmationEditText;
    public EditText mPhone;
    public EditText mMobil;
    public EditText mAddress;
    public ListView mDetialList;
    public TextView submit;
    public TextView save;
    public RelativeLayout back;
    public RelativeLayout shade;
    public ArrayList<Catalog> catalogs = new ArrayList<Catalog>();
    public QuoteDetialAdapter mQuoteDetialAdapter;

    public QuoteDetialFragment(MainPresenter mMainPresenter) {
        // Required empty public constructor
        this.mMainPresenter = mMainPresenter;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_quote_detial, container, false);
        mTimeText = (TextView) mView.findViewById(R.id.quote_detial_text_time);
        spinnerView = (SpinnerView) mView.findViewById(R.id.server_list);
        spinnerView.setdoItemClick(doItemClick);
        mDescribeEditText = (EditText) mView.findViewById(R.id.quote_detial_edit_describe);
        mImfmationEditText = (TextView) mView.findViewById(R.id.quote_detial_edit_customerimf);
        mDetialList = (ListView) mView.findViewById(R.id.quote_detial_list);
        submit = (TextView) mView.findViewById(R.id.quote_detail_submit);
        save = (TextView) mView.findViewById(R.id.quote_detial_save);
        back = mView.findViewById(R.id.quote_back_layer);
        shade = mView.findViewById(R.id.shade);
        spinnerView.setMyData(CustomerManager.getInstance().getCustomData());
        mImfmationEditText.setOnClickListener(mImfListener);
        mQuoteDetialAdapter = new QuoteDetialAdapter(mMainPresenter.mMainActivity, catalogs);
        mDetialList.setAdapter(mQuoteDetialAdapter);
        mDetialList.setOnItemClickListener(productDetialListener);
        mDetialList.setOnItemLongClickListener(deleteProductListener);
        back.setOnClickListener(doBackListener);
        save.setOnClickListener(doSaveListener);
        submit.setOnClickListener(doSubmitListener);

        updataView();
        return mView;
    }


    public void updataView() {

        if(mTimeText != null)
        {
            mTimeText.setText("时间："+OrderManager.getInstance().selectOrder.time);
            mDescribeEditText.setText(OrderManager.getInstance().selectOrder.memo);
            mImfmationEditText.setText("电话：" + OrderManager.getInstance().selectOrder.c_phone + "手机：" + OrderManager.getInstance().selectOrder.c_mobil + "地址：" + OrderManager.getInstance().selectOrder.c_address);
            if(OrderManager.getInstance().selectOrder.issubmit)
            {
                submit.setEnabled(false);
                save.setEnabled(false);
                spinnerView.setEnabled(false);
            }
            else
            {
                submit.setEnabled(true);
                save.setEnabled(true);
                spinnerView.setEnabled(true);
                if(OrderManager.getInstance().selectOrder.isedit)
                {
                    save.setText(mMainPresenter.mMainActivity.getString(R.string.button_word_save));
                }
                else
                {
                    save.setText(mMainPresenter.mMainActivity.getString(R.string.button_word_edit));
                }
            }
        }
        if(OrderManager.getInstance().selectOrder != null)
        {
            catalogs.clear();
            catalogs.addAll(OrderManager.getInstance().selectOrder.getAll());
        }

    }

    public void initImfdata() {
        mPhone.setText(OrderManager.getInstance().selectOrder.c_phone);
        mMobil.setText(OrderManager.getInstance().selectOrder.c_mobil);
        mAddress.setText(OrderManager.getInstance().selectOrder.c_address);
    }

    public View.OnClickListener mImfListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            showImfEdit();
        }
    };

    public AdapterView.OnItemClickListener productDetialListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mMainPresenter.mMainActivity.mCatalogDetialFragment.showDetial(catalogs,position,MainActivity.PAGE_QUTE_DETIAL);
            mMainPresenter.setContent(MainActivity.PAGE_CATALOGE_DETIAL);
        }
    };

    public AdapterView.OnItemLongClickListener deleteProductListener = new AdapterView.OnItemLongClickListener()
    {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            showDelete((Catalog) parent.getAdapter().getItem(position));
            return true;
        }
    };

    public View.OnClickListener doBackListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            if(!OrderManager.getInstance().orderHashMap.containsKey(OrderManager.getInstance().selectOrder.id))
            {
                AppUtils.creatDialogTowButton(mMainPresenter.mMainActivity,"您还未保存订单是否保存？",""
                        ,mMainPresenter.mMainActivity.getString(R.string.button_word_save),mMainPresenter.mMainActivity.getString(R.string.cancle),
                        new SaveOrderExit(OrderManager.getInstance().selectOrder),new Exit());
            }
            else
            {
                doDestory();
                mMainPresenter.setContent(MainActivity.PAGE_QUOTE);
            }
        }
    };

    public View.OnClickListener doSaveListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            if(OrderManager.getInstance().selectOrder.isedit == true)
                checkCustomer(false);
            else
                doEdit();
        }
    };

    public View.OnClickListener doSubmitListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            initOrder();
            mMainPresenter.mMainActivity.waitDialog.show();
            OrderAsks.submitOrder(mMainPresenter.mMainHandler,mMainPresenter.mMainActivity,OrderManager.getInstance().selectOrder);
        }
    };

    public void showDelete(Catalog catalog) {
        AppUtils.creatDialogTowButton(mMainPresenter.mMainActivity,"该产品将被移除购物车","",
                mMainPresenter.mMainActivity.getString(R.string.button_yes),mMainPresenter.mMainActivity.getString(R.string.button_no),
                new RemoveProduct(catalog),null);
    }


    public void initImf() {
        View popupWindowView = LayoutInflater.from(mMainPresenter.mMainActivity).inflate(R.layout.fragment_quote_detial_imf, null);
        RelativeLayout lsyer = (RelativeLayout) popupWindowView.findViewById(R.id.layer);
        lsyer.setFocusable(true);
        lsyer.setFocusableInTouchMode(true);
        mMainPresenter.mMainActivity.popupWindow1 = new PopupWindow(popupWindowView, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT, true);
        popupWindowView.setFocusable(true);
        popupWindowView.setFocusableInTouchMode(true);
        mMainPresenter.mMainActivity.popupWindow1.setAnimationStyle(R.style.PopupAnimation);
        final PopupWindow finalPopupWindow = mMainPresenter.mMainActivity.popupWindow1;
        lsyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalPopupWindow.dismiss();
            }
        });
        ColorDrawable dw = new ColorDrawable(0x00ffffff);
        mPhone = (EditText) popupWindowView.findViewById(R.id.customer_phone_edit);
        mMobil = (EditText) popupWindowView.findViewById(R.id.customer_mobil_edit);
        mAddress = (EditText) popupWindowView.findViewById(R.id.customer_address_edit);
        initImfdata();
        mMainPresenter.mMainActivity.popupWindow1.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                shade.setVisibility(View.INVISIBLE);
                hidImfEdit();
            }
        });
        mMainPresenter.mMainActivity.popupWindow1.showAtLocation(mMainPresenter.mMainActivity.findViewById(R.id.quote_order_detial_describe),
                Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
        shade.setVisibility(View.VISIBLE);

    }

    public void showImfEdit() {
        initImf();
        String temp = mImfmationEditText.getText().toString();
        if (temp.length() == 0)
        {
            mPhone.setText("");
            mMobil.setText("");
            mAddress.setText("");
        }
        else
        {
            mPhone.setText(temp.substring(temp.indexOf("电话：") + 3, temp.indexOf("手机：")));
            mMobil.setText(temp.substring(temp.indexOf("手机：") + 3, temp.indexOf("地址：")));
            mAddress.setText(temp.substring(temp.indexOf("地址：") + 3));
        }
        mPhone.requestFocus();
    }

    public void hidImfEdit() {
        if(mPhone != null)
        mImfmationEditText.setText("电话：" + mPhone.getText().toString() + "手机：" + mMobil.getText().toString() + "地址：" + mAddress.getText().toString());
    }

    public void doSave(boolean exit)
    {
        cancleEdit();
        initOrder();
        if(OrderDbHelper.getInstance(mMainPresenter.mMainActivity).addOreder( OrderManager.getInstance().selectOrder) != -1)
        {
            mMainPresenter.mMainActivity.mQuoteFragment.addOrder();
        }
        else
        {
            mMainPresenter.mMainActivity.mQuoteFragment.updataView();
        }
        if(exit)
        {
            doDestory();
            mMainPresenter.setContent(MainActivity.PAGE_QUOTE);
        }
    }

    public void initOrder()
    {
        OrderManager.getInstance().selectOrder.c_name = spinnerView.mSpinnerText.getText().toString();
        if(mPhone != null)
        {
            OrderManager.getInstance().selectOrder.c_phone = mPhone.getText().toString();
            OrderManager.getInstance().selectOrder.c_address = mAddress.getText().toString();
            OrderManager.getInstance().selectOrder.c_mobil = mMobil.getText().toString();
        }
        OrderManager.getInstance().selectOrder.memo = mDescribeEditText.getText().toString();
    }

    public void doDestory()
    {
        OrderManager.getInstance().selectOrder = null;
        InterskyPadApplication.mApp.sendBroadcast(new Intent(OrderManager.ACTION_UPDATE_CATALOG_COUNT));
    }

    public void doEdit() {
        OrderManager.getInstance().selectOrder.isedit = true;
        mImfmationEditText.setEnabled(true);
        mImfmationEditText.setEnabled(true);
        save.setText(mMainPresenter.mMainActivity.getString(R.string.button_word_save));
        mMainPresenter.mMainActivity.sendBroadcast(new Intent(OrderManager.ACTION_UPDATE_CATALOG_COUNT));
    }

    public void cancleEdit() {
        OrderManager.getInstance().selectOrder.isedit = false;
        hidImfEdit();
        mImfmationEditText.setEnabled(false);
        mImfmationEditText.setEnabled(false);
        save.setText(mMainPresenter.mMainActivity.getString(R.string.button_word_edit));
        mMainPresenter.mMainActivity.sendBroadcast(new Intent(OrderManager.ACTION_UPDATE_CATALOG_COUNT));
    }

    public void submitSuccess(Order order) {
        if(OrderManager.getInstance().selectOrder != null)
        {
            OrderManager.getInstance().selectOrder.issubmit =  true;
            submit.setEnabled(false);
            save.setEnabled(false);
            doSave(true);
        }
        else
        {
            order.issubmit = true;
        }

    }

    public void checkCustomer(boolean exit) {
        if(spinnerView.getText().length() > 0)
        {
            if(CustomerManager.getInstance().hashCustomer.containsKey(spinnerView.getText()))
            {
                doSave(exit);
            }
            else
            {
                AppUtils.creatDialogTowButton(mMainPresenter.mMainActivity,mMainPresenter.mMainActivity.getString(R.string.xml_customer_new_message),mMainPresenter.mMainActivity.getString(R.string.xml_customer_new_title)
                        ,mMainPresenter.mMainActivity.getString(R.string.button_word_ok),mMainPresenter.mMainActivity.getString(R.string.button_no),new SaveCuatomerAndOrder(OrderManager.getInstance().selectOrder,exit),new SaveOrder(OrderManager.getInstance().selectOrder,exit));
            }
        }
        else
        {
            AppUtils.showMessage(mMainPresenter.mMainActivity,"请填写客户名称");
        }
    }

    public class SaveCuatomerAndOrder implements DialogInterface.OnClickListener{

        public Order order;
        public boolean exit;

        public SaveCuatomerAndOrder(Order order,boolean exit) {
            this.order = order;
            this.exit = exit;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            Customer selectCustomer = new Customer();
            selectCustomer.setName(spinnerView.getText());
            if(mPhone != null)
            {
                selectCustomer.address = mAddress.getText().toString();
                selectCustomer.mobil = mMobil.getText().toString();
                selectCustomer.phone = mPhone.getText().toString();
            }
            DBHelper.getInstance(mMainPresenter.mMainActivity).addCustomer(selectCustomer);
            CustomerManager.getInstance().addCustomer(selectCustomer);
            mMainPresenter.mMainHandler.sendEmptyMessage(MainHandler.UPDATE_CUSTOMER_LIST);
            doSave(exit);
        }
    }

    public class SaveOrder implements DialogInterface.OnClickListener{

        public Order order;
        public boolean exit;

        public SaveOrder(Order order,boolean exit) {
            this.order = order;
            this.exit = exit;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            doSave(exit);
        }
    }

    public SpinnerView.DoItemClick doItemClick = new SpinnerView.DoItemClick()
    {
        @Override
        public void doItemClick(String item) {
            Customer customer = CustomerManager.getInstance().hashCustomer.get(item);
            mImfmationEditText.setText("电话：" + customer.phone + "手机：" + customer.mobil + "地址：" + customer.address);
        }
    };

    public class SaveOrderExit implements DialogInterface.OnClickListener{

        public Order order;

        public SaveOrderExit(Order order) {
            this.order = order;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if(OrderManager.getInstance().selectOrder.isedit == true)
                checkCustomer(true);
            else
                doEdit();
        }
    }

    public class Exit implements DialogInterface.OnClickListener{


        @Override
        public void onClick(DialogInterface dialog, int which) {
            doDestory();
            mMainPresenter.setContent(MainActivity.PAGE_QUOTE);
        }
    }

    public class RemoveProduct implements DialogInterface.OnClickListener{

        public Catalog catalog;

        public RemoveProduct(Catalog catalog) {
            this.catalog = catalog;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if(OrderManager.getInstance().selectOrder.count > 0)
            {
                OrderManager.getInstance().selectOrder.remove(catalog.mSerialID);
                mMainPresenter.mMainActivity.sendBroadcast(new Intent(OrderManager.ACTION_UPDATE_CATALOG_COUNT));
            }
            else
            {
                doDestory();
                mMainPresenter.setContent(MainActivity.PAGE_QUOTE);
            }
        }
    }
}
