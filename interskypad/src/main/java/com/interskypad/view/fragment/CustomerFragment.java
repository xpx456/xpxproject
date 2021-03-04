package com.interskypad.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.interskypad.R;
import com.interskypad.database.DBHelper;
import com.interskypad.entity.Customer;
import com.interskypad.manager.CustomerManager;
import com.interskypad.presenter.MainPresenter;
import com.interskypad.view.adapter.CustomerAdapter;

import java.util.ArrayList;
import java.util.Collections;

import intersky.appbase.BaseFragment;
import intersky.apputils.AppUtils;
import intersky.apputils.CharacterParser;
import intersky.mywidget.MySlideBar;
import intersky.mywidget.SearchViewLayout;

@SuppressLint("ValidFragment")
public class CustomerFragment extends BaseFragment {

    public MainPresenter mMainPresenter;
    public SearchViewLayout searchView;
    private ListView contactList;
    public boolean isShowSearch = false;
    public MySlideBar msbar;
    public CustomerAdapter mContactAdapter;
    public CustomerAdapter mSearchContactAdapter;
    private ImageView mCustomerAdd;
    private EditText mNameEdit;
    private EditText mPhoneEdit;
    private EditText mMobilEdit;
    private EditText mMailEdit;
    private EditText mAddressEdit;
    private EditText mBackupEdit;
    public Customer selectCustomer;
    private TextView mEdit;
    public boolean isedit = false;
    public RelativeLayout mRelativeLetter;
    public TextView mLetterText;
    public CustomerFragment(MainPresenter mMainPresenter) {
        // Required empty public constructor
        this.mMainPresenter = mMainPresenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_customer, container, false);
        contactList = (ListView) mView.findViewById(R.id.contacts_List);
        searchView = (SearchViewLayout) mView.findViewById(R.id.search);
        mCustomerAdd = (ImageView) mView.findViewById(R.id.customer_customer_add);
        mNameEdit = (EditText) mView.findViewById(R.id.customer_name_edit);
        mPhoneEdit = (EditText) mView.findViewById(R.id.customer_phone_edit);
        mMobilEdit = (EditText) mView.findViewById(R.id.customer_mobil_edit);
        mMailEdit = (EditText) mView.findViewById(R.id.customer_mail_edit);
        msbar = (MySlideBar) mView.findViewById(R.id.slideBar);
        mAddressEdit = (EditText) mView.findViewById(R.id.customer_address_edit);
        mBackupEdit = (EditText) mView.findViewById(R.id.customer_backup_edit);
        mEdit = (TextView) mView.findViewById(R.id.customer_content_edit);
        mLetterText = (TextView) mView.findViewById(R.id.letter_text);
        mRelativeLetter = (RelativeLayout) mView.findViewById(R.id.letter_layer);
        searchView.mSetOnSearchListener(mOnSearchActionListener);
        contactList.setOnScrollListener(mOnScoll);
        msbar.setVisibility(View.INVISIBLE);
        msbar.setOnTouchLetterChangeListenner(mOnTouchLetterChangeListenner);
        msbar.setmRelativeLayout(mRelativeLetter);
        msbar.setMletterView(mLetterText);
        if(selectCustomer == null)
        {
            mEdit.setVisibility(View.INVISIBLE);
            setUnable();
        }
        else
        {
            mEdit.setVisibility(View.VISIBLE);
            setEable();
        }
        mContactAdapter = new CustomerAdapter(CustomerManager.getInstance().allcustomers,mMainPresenter.mMainActivity);
        mSearchContactAdapter = new CustomerAdapter(CustomerManager.getInstance().mSearchItems,mMainPresenter.mMainActivity);
        contactList.setAdapter(mContactAdapter);
        contactList.setOnItemClickListener(onItemClickListener);
        mCustomerAdd.setOnClickListener(addCustomerListner);
        mEdit.setOnClickListener(editCustomerListner);
        return mView;
    }

    public void updataView()
    {
        if(mContactAdapter != null)
        {
            mContactAdapter.notifyDataSetChanged();
        }
        if(mSearchContactAdapter != null)
        {
            mSearchContactAdapter.notifyDataSetChanged();
        }
    }

    public TextView.OnEditorActionListener mOnSearchActionListener = new TextView.OnEditorActionListener()
    {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
        {
            // TODO Auto-generated method stub
            if (actionId == EditorInfo.IME_ACTION_SEARCH)
            {
                doSearch(searchView.getText());

            }
            return true;
        }
    };

    public AbsListView.OnScrollListener mOnScoll = new AbsListView.OnScrollListener()
    {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if(searchView.ishow)
            {
                if(searchView.getText().length() == 0)
                {
                    searchView.hidEdit();
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    };

    public MySlideBar.OnTouchLetterChangeListenner mOnTouchLetterChangeListenner = new MySlideBar.OnTouchLetterChangeListenner()
    {

        @Override
        public void onTouchLetterChange(MotionEvent event, int s)
        {
            // TODO Auto-generated method stub
            LetterChange(s);

        }
    };

    public void LetterChange(int s) {
        Customer model = CustomerManager.getInstance().customersHead.get(s);
        int a =CustomerManager.getInstance().allcustomers.indexOf(model);
        contactList.setSelectionFromTop(a, 0);

    }

    public void doSearch(String keyword) {
        if(keyword.length() == 0)
        {
            if(isShowSearch == true)
            {
                isShowSearch = false;
                contactList.setAdapter(mContactAdapter);
            }
            return;
        }
        boolean typebooleans[] = new boolean[26];
        ArrayList<Customer> temps = new ArrayList<Customer>();
        ArrayList<Customer> tempheads = new ArrayList<Customer>();
        for (int i = 0; i < CustomerManager.getInstance().allcustomers.size(); i++) {
            Customer mContactModel = CustomerManager.getInstance().allcustomers.get(i);
            if (mContactModel.type == 0) {
                if (mContactModel.mPingyin.contains(keyword.toLowerCase()) || mContactModel.getName().contains(keyword)) {
                    temps.add(mContactModel);

                    String s = mContactModel.mPingyin.substring(0, 1).toUpperCase();
                    int pos = CharacterParser.typeLetter.indexOf(s);
                    if (pos != -1) {
                        if (typebooleans[pos] == false) {
                            tempheads.add(new Customer(s));
                            typebooleans[pos] = true;
                        }
                    }
                }
            }
        }
        if (temps.size() == 0) {
            AppUtils.showMessage(mMainPresenter.mMainActivity, mMainPresenter.mMainActivity.getString(R.string.searchview_search_none));
        } else {
            CustomerManager.getInstance().mSearchItems.clear();
            CustomerManager.getInstance().mSearchHeadItems.clear();
            CustomerManager.getInstance().mSearchItems.addAll(temps);
            CustomerManager.getInstance().mSearchHeadItems.addAll(tempheads);
            CustomerManager.getInstance().mSearchItems.addAll(0, CustomerManager.getInstance().mSearchHeadItems);
            Collections.sort(CustomerManager.getInstance().mSearchItems, new CustomerManager.SortContactComparator());
            Collections.sort(CustomerManager.getInstance().mSearchHeadItems, new CustomerManager.SortContactComparator());
            contactList.setAdapter(mSearchContactAdapter);
            isShowSearch = true;
        }


    }

    public View.OnClickListener addCustomerListner = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            addCustomer();
        }
    };

    public View.OnClickListener editCustomerListner = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            doEdit();
        }
    };

    public void addCustomer() {
        Customer customer = new Customer();
        mNameEdit.requestFocus();
        InputMethodManager imm = (InputMethodManager) mMainPresenter.mMainActivity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mNameEdit, InputMethodManager.SHOW_FORCED);
        if(isedit) {
            if(selectCustomer != null)
            showSaveDialog(customer);
            else
            {
                mEdit.setVisibility(View.VISIBLE);
                selectCustomer = customer;
                selectCustomer.isSelect = true;
                setEable();
                doEdit(selectCustomer);
            }
        }
        else
        {
            isedit = true;
            doClean();
            if(selectCustomer != null)
            {
                selectCustomer.isSelect = false;
            }
            mEdit.setVisibility(View.VISIBLE);
            selectCustomer = customer;
            selectCustomer.isSelect = true;
            doEdit(customer);
            setEable();

        }
    }

    public void doEdit() {
        if(isedit)
        {
            if(mNameEdit.getText().toString().length() > 0)
            {
                isedit = false;
                setUnable();
                doSave();
            }
            else
            {
                AppUtils.showMessage(mMainPresenter.mMainActivity,mMainPresenter.mMainActivity.getString(R.string.xml_customer_name_empty));
            }

        }
        else
        {
            isedit = true;
            doClean();
            doEdit(selectCustomer);
            setEable();
            mNameEdit.requestFocus();
            InputMethodManager imm = (InputMethodManager) mMainPresenter.mMainActivity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(mNameEdit, InputMethodManager.SHOW_FORCED);
        }
    }

    public void setEable() {
        mNameEdit.setEnabled(true);
        mPhoneEdit.setEnabled(true);
        mMobilEdit.setEnabled(true);
        mMailEdit.setEnabled(true);
        mAddressEdit.setEnabled(true);
        mBackupEdit.setEnabled(true);
        mEdit.setText(mMainPresenter.mMainActivity.getString(R.string.button_word_finish));
    }

    public void setUnable() {
        mNameEdit.setEnabled(false);
        mPhoneEdit.setEnabled(false);
        mMobilEdit.setEnabled(false);
        mMailEdit.setEnabled(false);
        mAddressEdit.setEnabled(false);
        mBackupEdit.setEnabled(false);
        mEdit.setText(mMainPresenter.mMainActivity.getString(R.string.button_word_edit));
    }

    public void showSaveDialog(Customer customer) {
        AppUtils.creatDialogTowButton(mMainPresenter.mMainActivity,mMainPresenter.mMainActivity.getString(R.string.xml_customer_unsave),"","放弃保持"
                ,mMainPresenter.mMainActivity.getString(R.string.button_word_save),mMainPresenter.mMainActivity.getString(R.string.button_word_cancle)
                ,new UnSaveListener(customer),new SaveListener(customer),null
                );
    }

    public class SaveListener implements DialogInterface.OnClickListener
    {

        public Customer icustomer;

        public SaveListener(Customer customer)
        {
            icustomer = customer;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if(selectCustomer.getName().length() > 0)
            {
                doSave();
                isedit = true;
                setEable();
                selectCustomer.isSelect = false;
                selectCustomer = icustomer;
                selectCustomer.isSelect = true;
                doEdit(selectCustomer);
            }
            else
            {
                AppUtils.showMessage(mMainPresenter.mMainActivity,mMainPresenter.mMainActivity.getString(R.string.xml_customer_name_empty));
            }

        }
    };

    public class UnSaveListener implements DialogInterface.OnClickListener
    {

        public Customer icustomer;

        public UnSaveListener(Customer customer)
        {
            icustomer = customer;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            isedit = true;
            setEable();
            selectCustomer.isSelect = false;
            selectCustomer = icustomer;
            selectCustomer.isSelect = true;
            doEdit(selectCustomer);
            mContactAdapter.notifyDataSetChanged();
            mSearchContactAdapter.notifyDataSetChanged();
        }
    };


    public AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Customer customer = (Customer) parent.getAdapter().getItem(position);
            if(customer.type == 0)
            {
                if(selectCustomer == null)
                {
                    selectCustomer = customer;
                    customer.isSelect = true;
                    doEdit(selectCustomer);
                    mEdit.setVisibility(View.VISIBLE);
                    mContactAdapter.notifyDataSetChanged();
                    mSearchContactAdapter.notifyDataSetChanged();
                }
                else
                {
                    if(isedit)
                    {
                        showSaveDialog(customer);
                    }
                    else
                    {
                        selectCustomer.isSelect = false;
                        selectCustomer = customer;
                        customer.isSelect = true;
                        doEdit(selectCustomer);
                        mContactAdapter.notifyDataSetChanged();
                        mSearchContactAdapter.notifyDataSetChanged();
                    }
                }

            }
        }
    };

    public void doSave()
    {
        if(selectCustomer.getName().length() > 0 && !selectCustomer.getName().equals(mNameEdit.getText().toString()))
        {
            DBHelper.getInstance(mMainPresenter.mMainActivity).deleteCustomer(selectCustomer);
            CustomerManager.getInstance().removeCustomer(selectCustomer);
        }
        selectCustomer.setName(mNameEdit.getText().toString());
        selectCustomer.memo = mBackupEdit.getText().toString();
        selectCustomer.address = mAddressEdit.getText().toString();
        selectCustomer.mail = mMailEdit.getText().toString();
        selectCustomer.mobil = mMobilEdit.getText().toString();
        selectCustomer.phone = mPhoneEdit.getText().toString();
        DBHelper.getInstance(mMainPresenter.mMainActivity).addCustomer(selectCustomer);
        CustomerManager.getInstance().addCustomer(selectCustomer);
        updataView();
    }

    public void doClean()
    {
        mNameEdit.setText("");
        mBackupEdit.setText("");
        mAddressEdit.setText("");
        mMailEdit.setText("");
        mMobilEdit.setText("");
        mPhoneEdit.setText("");
    }

    public void doEdit(Customer customer)
    {
        mNameEdit.setText(customer.getName());
        mBackupEdit.setText(customer.memo);
        mAddressEdit.setText(customer.address);
        mMailEdit.setText(customer.mail);
        mMobilEdit.setText(customer.mobil);
        mPhoneEdit.setText(customer.phone);
    }
}
