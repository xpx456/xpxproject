package com.interskypad.view.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.interskypad.R;
import com.interskypad.asks.OrderAsks;
import com.interskypad.database.OrderDbHelper;
import com.interskypad.entity.Order;
import com.interskypad.manager.OrderManager;
import com.interskypad.presenter.MainPresenter;
import com.interskypad.view.activity.MainActivity;
import com.interskypad.view.adapter.QuoteItemAdapter;
import com.interskypad.view.adapter.QuotePageAdapter;

import java.util.ArrayList;

import intersky.appbase.BaseFragment;
import intersky.apputils.AppUtils;
import intersky.mywidget.ScanSearchViewLayout;

@SuppressLint("ValidFragment")
public class QuoteFragment extends BaseFragment {

    public MainPresenter mMainPresenter;
    private QuotePageAdapter mQuotePageAdapter;
    private ViewPager mViewPager;
    private ArrayList<View> mViews = new ArrayList<View>();
    private ListView mListView[] = new ListView[3];
    private TextView mAll;
    private TextView mSubmit;
    private TextView mUnSubmit;

    public QuoteItemAdapter mQuoteItemAdapterAll;
    public QuoteItemAdapter mQuoteItemAdapterSubmit;
    public QuoteItemAdapter mQuoteItemAdapterUnSubmit;
    public QuoteItemAdapter mQuoteItemAdapterSearch;
    public QuoteItemAdapter mQuoteItemAdapterSearchUn;
    public QuoteItemAdapter mQuoteItemAdapterSearchSub;
    public ScanSearchViewLayout mSearchViewLayout;


    public QuoteFragment(MainPresenter mMainPresenter) {
        // Required empty public constructor
        this.mMainPresenter = mMainPresenter;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_quote, container, false);
        mAll = (TextView) mView.findViewById(R.id.quote_all);
        mSubmit = (TextView) mView.findViewById(R.id.quote_submit);
        mUnSubmit = (TextView) mView.findViewById(R.id.quote_unsubmit);
        mViewPager = (ViewPager) mView.findViewById(R.id.quote_pager);
        mSearchViewLayout = mView.findViewById(R.id.catalog_search_layer);
        mSearchViewLayout.setSacn(false);
        mSearchViewLayout.sethit(mMainPresenter.mMainActivity.getString(R.string.xml_quote_searchhit));
        mSearchViewLayout.mSetOnSearchListener(searchListner);
        View mpager1 = inflater.inflate(R.layout.order_list, null);
        mListView[0] = (ListView) mpager1.findViewById(R.id.quote_list);
        View mpager2 = inflater.inflate(R.layout.order_list, null);
        mListView[1] = (ListView) mpager2.findViewById(R.id.quote_list);
        View mpager3 = inflater.inflate(R.layout.order_list, null);
        mListView[2] = (ListView) mpager3.findViewById(R.id.quote_list);
        mViews.clear();
        mViews.add(mpager1);
        mViews.add(mpager2);
        mViews.add(mpager3);
        mQuotePageAdapter = new QuotePageAdapter(mViews);
        mViewPager.setAdapter(mQuotePageAdapter);
        mQuoteItemAdapterAll = new QuoteItemAdapter(mMainPresenter.mMainActivity,OrderManager.getInstance().mAllOrders,mMainPresenter.mMainHandler);
        mQuoteItemAdapterSubmit = new QuoteItemAdapter(mMainPresenter.mMainActivity, OrderManager.getInstance().mSubmitOrders,mMainPresenter.mMainHandler);
        mQuoteItemAdapterUnSubmit = new QuoteItemAdapter(mMainPresenter.mMainActivity, OrderManager.getInstance().mUnSubmitOrders,mMainPresenter.mMainHandler);
        mQuoteItemAdapterSearch = new QuoteItemAdapter(mMainPresenter.mMainActivity, OrderManager.getInstance().mSAllOrders,mMainPresenter.mMainHandler);
        mQuoteItemAdapterSearchUn = new QuoteItemAdapter(mMainPresenter.mMainActivity, OrderManager.getInstance().mSSubmitOrders,mMainPresenter.mMainHandler);
        mQuoteItemAdapterSearchSub = new QuoteItemAdapter(mMainPresenter.mMainActivity, OrderManager.getInstance().mSUnSubmitOrders,mMainPresenter.mMainHandler);
        mListView[0].setAdapter(mQuoteItemAdapterAll);
        mListView[1].setAdapter(mQuoteItemAdapterSubmit);
        mListView[2].setAdapter(mQuoteItemAdapterUnSubmit);
        setTebls();
        mAll.setOnClickListener(mAllListener);
        mSubmit.setOnClickListener(mSubmitListener);
        mUnSubmit.setOnClickListener(mUnSubmitListener);
        mListView[0].setOnItemClickListener(mOnItemClickListener);
        mListView[0].setOnItemLongClickListener(mOnItemLongClickListener);
        //mListView[0].setOnTouchListener(mlist);
        mListView[1].setOnItemClickListener(mOnItemClickListener);
        mListView[1].setOnItemLongClickListener(mOnItemLongClickListener);
        //mListView[1].setOnTouchListener(mlist);
        mListView[2].setOnItemClickListener(mOnItemClickListener);
        mListView[2].setOnItemLongClickListener(mOnItemLongClickListener);
        return mView;
    }


    public View.OnClickListener mAllListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            setTebls();
        }
    };

    public View.OnClickListener mSubmitListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            setTebcs();
        }
    };

    public View.OnClickListener mUnSubmitListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            setTebrs();
        }
    };

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener()
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            // TODO Auto-generated method stub
            OrderManager.getInstance().selectOrder = (Order) parent.getAdapter().getItem(position);
            mMainPresenter.setContent(MainActivity.PAGE_QUTE_DETIAL);
        }
    };

    private AdapterView.OnItemLongClickListener mOnItemLongClickListener = new AdapterView.OnItemLongClickListener()
    {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id)
        {
            // TODO Auto-generated method stub
            AppUtils.creatDialogTowButton(mMainPresenter.mMainActivity,mMainPresenter.mMainActivity.getString(R.string.xml_quote_delete_order)
                    ,mMainPresenter.mMainActivity.getString(R.string.dialog_word_tip),mMainPresenter.mMainActivity.getString(R.string.button_yes)
                    ,mMainPresenter.mMainActivity.getString(R.string.button_no),new DeleteListener((Order)parent.getAdapter().getItem(position)),null);
            return true;
        }
    };

    public TextView.OnEditorActionListener searchListner = new TextView.OnEditorActionListener()
    {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

            if (actionId == EditorInfo.IME_ACTION_SEARCH)
            {
                doSearch(v.getText().toString());
            }
            return true;
        }
    };

    public void doSearch(String keyword) {
        OrderManager.getInstance().mSAllOrders.clear();
        OrderManager.getInstance().mSSubmitOrders.clear();
        OrderManager.getInstance().mSUnSubmitOrders.clear();
        if(keyword.length() > 0)
        {
            for(int i = 0 ; i < OrderManager.getInstance().mAllOrders.size() ; i++)
            {
                if(OrderManager.getInstance().mAllOrders.get(i).id.contains(keyword))
                {
                    OrderManager.getInstance().mSAllOrders.add(OrderManager.getInstance().mAllOrders.get(i));
                    if(OrderManager.getInstance().mAllOrders.get(i).issubmit)
                    {
                        OrderManager.getInstance().mSSubmitOrders.add(OrderManager.getInstance().mAllOrders.get(i));
                    }
                    else
                    {
                        OrderManager.getInstance().mSUnSubmitOrders.add(OrderManager.getInstance().mAllOrders.get(i));
                    }
                }
            }
            mListView[0].setAdapter(mQuoteItemAdapterSearch);
            mListView[1].setAdapter(mQuoteItemAdapterSearchUn);
            mListView[2].setAdapter(mQuoteItemAdapterSearchSub);
        }
        else
        {
            mListView[0].setAdapter(mQuoteItemAdapterAll);
            mListView[1].setAdapter(mQuoteItemAdapterSubmit);
            mListView[2].setAdapter(mQuoteItemAdapterUnSubmit);
        }
    }

    private void setTebls()
    {
        mViewPager.setCurrentItem(0);
        mAll.setTextColor(Color.argb(255, 255, 255, 255));
        mAll.setBackgroundResource(R.drawable.shape_quote_lefts);
        mSubmit.setTextColor(Color.argb(255, 70, 126, 183));
        mSubmit.setBackgroundResource(R.drawable.shape_quote_center);
        mUnSubmit.setTextColor(Color.argb(255, 70, 126, 183));
        mUnSubmit.setBackgroundResource(R.drawable.shape_quote_right);
    }

    private void setTebcs()
    {
        mViewPager.setCurrentItem(1);
        mAll.setTextColor(Color.argb(255, 70, 126, 183));
        mAll.setBackgroundResource(R.drawable.shape_quote_left);
        mSubmit.setTextColor(Color.argb(255, 255, 255, 255));
        mSubmit.setBackgroundResource(R.drawable.shape_quote_centers);
        mUnSubmit.setTextColor(Color.argb(255, 70, 126, 183));
        mUnSubmit.setBackgroundResource(R.drawable.shape_quote_right);
    }

    private void setTebrs()
    {
        mViewPager.setCurrentItem(2);
        mAll.setTextColor(Color.argb(255, 70, 126, 183));
        mAll.setBackgroundResource(R.drawable.shape_quote_left);
        mSubmit.setTextColor(Color.argb(255, 70, 126, 183));
        mSubmit.setBackgroundResource(R.drawable.shape_quote_center);
        mUnSubmit.setTextColor(Color.argb(255, 255, 255, 255));
        mUnSubmit.setBackgroundResource(R.drawable.shape_quote_rights);
    }

    public class DeleteListener implements DialogInterface.OnClickListener{

        public Order order;

        public DeleteListener(Order order) {
            this.order = order;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {

            if(order.issubmit)
            {
                mMainPresenter.mMainActivity.waitDialog.show();
                OrderAsks.deleteOrder(mMainPresenter.mMainHandler,mMainPresenter.mMainActivity,order);
            }
            OrderDbHelper.getInstance(mMainPresenter.mMainActivity).deleteOrder(order);
            OrderManager.getInstance().mAllOrders.remove(order);
            OrderManager.getInstance().mSubmitOrders.remove(order);
            OrderManager.getInstance().mUnSubmitOrders.remove(order);
            OrderManager.getInstance().mSAllOrders.remove(order);
            OrderManager.getInstance().mSSubmitOrders.remove(order);
            OrderManager.getInstance().mSUnSubmitOrders.remove(order);
            updataView();
        }
    }

    public void updataView()
    {
        if(mQuoteItemAdapterAll != null)
        {
            mQuoteItemAdapterAll.notifyDataSetChanged();
            mQuoteItemAdapterSubmit.notifyDataSetChanged();
            mQuoteItemAdapterUnSubmit.notifyDataSetChanged();
            mQuoteItemAdapterSearch.notifyDataSetChanged();
            mQuoteItemAdapterSearchUn.notifyDataSetChanged();
            mQuoteItemAdapterSearchSub.notifyDataSetChanged();
        }
    }

    public void addOrder() {
        if(!OrderManager.getInstance().orderHashMap.containsKey(OrderManager.getInstance().selectOrder.id))
        {
            OrderManager.getInstance().mAllOrders.add(OrderManager.getInstance().selectOrder);
            if(OrderManager.getInstance().selectOrder.issubmit)
            {
                OrderManager.getInstance().mSubmitOrders.add(OrderManager.getInstance().selectOrder);
            }
            else
            {
                OrderManager.getInstance().mUnSubmitOrders.add(OrderManager.getInstance().selectOrder);
            }
        }
        else
        {
            if(OrderManager.getInstance().selectOrder.id.contains(mSearchViewLayout.getText()))
            {
                if(!OrderManager.getInstance().orderHashMap.containsKey(OrderManager.getInstance().selectOrder.id))
                {
                    OrderManager.getInstance().mSAllOrders.add(OrderManager.getInstance().selectOrder);
                    if(OrderManager.getInstance().selectOrder.issubmit)
                    {
                        OrderManager.getInstance().mSSubmitOrders.add(OrderManager.getInstance().selectOrder);
                    }
                    else
                    {
                        OrderManager.getInstance().mSUnSubmitOrders.add(OrderManager.getInstance().selectOrder);
                    }
                }
            }
        }
        updataView();
    }
}
