package com.bigwiner.android.presenter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.bigwiner.R;
import com.bigwiner.android.asks.ConversationAsks;
import com.bigwiner.android.entity.Source;
import com.bigwiner.android.entity.UserDefine;
import com.bigwiner.android.handler.SourceSelectHandler;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.AttdenceActivity;
import com.bigwiner.android.view.activity.SourceSelectActivity;
import com.bigwiner.android.view.activity.ChatActivity;
import com.bigwiner.android.view.adapter.SourceAdapter;
import com.umeng.commonsdk.debug.I;

import intersky.appbase.Presenter;
import intersky.appbase.entity.Contacts;
import intersky.appbase.entity.Conversation;
import intersky.apputils.AppUtils;
import intersky.conversation.view.adapter.ConversationPageAdapter;
import intersky.mywidget.SearchViewLayout;
import intersky.select.SelectManager;
import intersky.select.entity.MapSelect;
import intersky.select.entity.Select;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class SourceSelectPresenter implements Presenter {

    public SourceSelectActivity mSourceSelectActivity;
    public SourceSelectHandler mSourceSelectHandler;
    public SourceSelectPresenter(SourceSelectActivity mSourceSelectActivity)
    {
        mSourceSelectHandler =new SourceSelectHandler(mSourceSelectActivity);
        this.mSourceSelectActivity = mSourceSelectActivity;
    }

    @Override
    public void Create() {
        initView();
    }

    @Override
    public void initView() {
        ToolBarHelper.setSutColor(mSourceSelectActivity, Color.argb(0, 255, 255, 255));
        mSourceSelectActivity.setContentView(R.layout.activity_source_select);
        mSourceSelectActivity.mToolBarHelper.hidToolbar(mSourceSelectActivity, (RelativeLayout) mSourceSelectActivity.findViewById(R.id.buttomaciton));
        mSourceSelectActivity.measureStatubar(mSourceSelectActivity, (RelativeLayout) mSourceSelectActivity.findViewById(R.id.stutebar));
        String[] names = new String[4];
        names[0] = mSourceSelectActivity.getString(R.string.source_position);
        names[1] = mSourceSelectActivity.getString(R.string.source_ports);
        names[2] = mSourceSelectActivity.getString(R.string.contacts_head_type);
        names[3] = mSourceSelectActivity.getString(R.string.contacts_head_area);
//        names[2] = mSourceSelectActivity.getString(R.string.source_serices);
//        names[3] = mSourceSelectActivity.getString(R.string.source_routes);
        if(mSourceSelectActivity.getIntent().getIntExtra("type",0) == 0)
        mSourceSelectActivity.sourcePortAdaptert = new SourceAdapter(BigwinerApplication.mApp.ports.list,mSourceSelectActivity
                ,true,BigwinerApplication.mApp.my.ports.hashMap);
        else
            mSourceSelectActivity.sourcePortAdaptert = new SourceAdapter(BigwinerApplication.mApp.ports.list,mSourceSelectActivity
                    ,true,BigwinerApplication.mApp.want.ports.hashMap);
        if(mSourceSelectActivity.getIntent().getIntExtra("type",0) == 0)
            mSourceSelectActivity.sourceTypeAdapter = new SourceAdapter(BigwinerApplication.mApp.businesstypeSelect.list,mSourceSelectActivity
                    ,true,BigwinerApplication.mApp.my.businesstypeSelect.hashMap);
        else
            mSourceSelectActivity.sourceTypeAdapter = new SourceAdapter(BigwinerApplication.mApp.businesstypeSelect.list,mSourceSelectActivity
                    ,true,BigwinerApplication.mApp.want.businesstypeSelect.hashMap);
        if(mSourceSelectActivity.getIntent().getIntExtra("type",0) == 0)
            mSourceSelectActivity.sourceAreaAdapter = new SourceAdapter(BigwinerApplication.mApp.businessareaSelect.list,mSourceSelectActivity
                    ,true,BigwinerApplication.mApp.my.businessareaSelect.hashMap);
        else
            mSourceSelectActivity.sourceAreaAdapter = new SourceAdapter(BigwinerApplication.mApp.businessareaSelect.list,mSourceSelectActivity
                    ,true,BigwinerApplication.mApp.want.businessareaSelect.hashMap);
        mSourceSelectActivity.sourcePositionAdapter = new SourceAdapter(BigwinerApplication.mApp.positions.list,mSourceSelectActivity);


        mSourceSelectActivity.sourceSearchPositionAdapter = new SourceAdapter(mSourceSelectActivity.position,mSourceSelectActivity);
        if(mSourceSelectActivity.getIntent().getIntExtra("type",0) == 0)
            mSourceSelectActivity.sourceSearchPortAdaptert = new SourceAdapter(mSourceSelectActivity.ports,mSourceSelectActivity,true
                    ,BigwinerApplication.mApp.my.ports.hashMap);
        else
            mSourceSelectActivity.sourceSearchPortAdaptert = new SourceAdapter(mSourceSelectActivity.ports,mSourceSelectActivity,true
                    ,BigwinerApplication.mApp.want.ports.hashMap);

        if(mSourceSelectActivity.getIntent().getIntExtra("type",0) == 0)
            mSourceSelectActivity.sourceSearchTypeAdapter = new SourceAdapter(mSourceSelectActivity.types,mSourceSelectActivity,true
                    ,BigwinerApplication.mApp.my.businesstypeSelect.hashMap);
        else
            mSourceSelectActivity.sourceSearchTypeAdapter = new SourceAdapter(mSourceSelectActivity.types,mSourceSelectActivity,true
                    ,BigwinerApplication.mApp.want.businesstypeSelect.hashMap);
        if(mSourceSelectActivity.getIntent().getIntExtra("type",0) == 0)
            mSourceSelectActivity.sourceSearchAreaAdapter = new SourceAdapter(mSourceSelectActivity.areas,mSourceSelectActivity,true
                    ,BigwinerApplication.mApp.my.businessareaSelect.hashMap);
        else
            mSourceSelectActivity.sourceSearchAreaAdapter = new SourceAdapter(mSourceSelectActivity.areas,mSourceSelectActivity,true
                    ,BigwinerApplication.mApp.want.businessareaSelect.hashMap);;
//        mSourceSelectActivity.sourceSericeAdapter = new SourceAdapter(BigwinerApplication.mApp.serices.list,mSourceSelectActivity);
//        mSourceSelectActivity.sourceRoutesAdapter = new SourceAdapter(BigwinerApplication.mApp.routes.list,mSourceSelectActivity);
        initSelect();
        for(int i =0 ; i < names.length ; i++)
        {
            View mView1 = mSourceSelectActivity.getLayoutInflater().inflate(R.layout.source_pager_s, null);
            ListView listView = mView1.findViewById(R.id.busines_List);
            SearchViewLayout searchViewLayout = mView1.findViewById(R.id.search);

            if(mSourceSelectActivity.getIntent().getBooleanExtra("edit",false) == true)
            listView.setOnItemClickListener(mSourceSelectActivity.itemClickListener);

            switch (i)
            {
                case 0:
                    searchViewLayout.setDotextChange(mSourceSelectActivity.mOnPositionSearchActionListener);
                    listView.setAdapter(mSourceSelectActivity.sourcePositionAdapter);
                    searchViewLayout.setOnCancleListener(mSourceSelectActivity.positionClean);
                    break;
                case 1:
                    searchViewLayout.setDotextChange(mSourceSelectActivity.mOnPortSearchActionListener);
                    listView.setAdapter(mSourceSelectActivity.sourcePortAdaptert);
                    searchViewLayout.setOnCancleListener(mSourceSelectActivity.portClean);
                    break;
                case 2:
                    searchViewLayout.setDotextChange(mSourceSelectActivity.mOnTypeSearchActionListener);
                    listView.setAdapter(mSourceSelectActivity.sourceTypeAdapter);
                    searchViewLayout.setOnCancleListener(mSourceSelectActivity.typeClean);
//                    listView.setAdapter(mSourceSelectActivity.sourceSericeAdapter);
                    break;
                case 3:
                    searchViewLayout.setDotextChange(mSourceSelectActivity.mOnAreaSearchActionListener);
                    listView.setAdapter(mSourceSelectActivity.sourceAreaAdapter);
                    searchViewLayout.setOnCancleListener(mSourceSelectActivity.areaClean);
//                    listView.setAdapter(mSourceSelectActivity.sourceRoutesAdapter);
                    break;
            }
            mSourceSelectActivity.searchViewLayouts.add(searchViewLayout);
            mSourceSelectActivity.listViews.add(listView);
            mSourceSelectActivity.mViews.add(mView1);
        }
        mSourceSelectActivity.mLoderPageAdapter = new ConversationPageAdapter(mSourceSelectActivity.mViews,names);
        mSourceSelectActivity.back = mSourceSelectActivity.findViewById(R.id.back);
        mSourceSelectActivity.mNoScrollViewPager =mSourceSelectActivity.findViewById(R.id.datalist);
        mSourceSelectActivity.btnPort = mSourceSelectActivity.findViewById(R.id.ports);
        mSourceSelectActivity.btnPosition = mSourceSelectActivity.findViewById(R.id.position);
        mSourceSelectActivity.btnSerice = mSourceSelectActivity.findViewById(R.id.serice);
        mSourceSelectActivity.btnRoutes = mSourceSelectActivity.findViewById(R.id.routes);
        mSourceSelectActivity.mNoScrollViewPager.setAdapter(mSourceSelectActivity.mLoderPageAdapter);
        mSourceSelectActivity.back.setOnClickListener(mSourceSelectActivity.backListener);
        mSourceSelectActivity.btnPort.setOnClickListener(mSourceSelectActivity.onTebClickListener);
        mSourceSelectActivity.btnPosition.setOnClickListener(mSourceSelectActivity.onTebClickListener);
        mSourceSelectActivity.btnSerice.setOnClickListener(mSourceSelectActivity.onTebClickListener);
        mSourceSelectActivity.btnRoutes.setOnClickListener(mSourceSelectActivity.onTebClickListener);

        if(BigwinerApplication.mApp.ports.list.size() == 0 &&
                BigwinerApplication.mApp.businesstypeSelect.list.size() == 0 &&
                BigwinerApplication.mApp.businessareaSelect.list.size() == 0 &&
                BigwinerApplication.mApp.positions.list.size() == 0)
            ConversationAsks.getBaseData(mSourceSelectActivity,mSourceSelectHandler,"all");
//        ConversationAsks.getSourceData(mSourceSelectActivity,mSourceSelectHandler);
        mSourceSelectActivity.mNoScrollViewPager.setCurrentItem(0);
        clickTeb(mSourceSelectActivity.btnPosition.getId());
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
        cleanSelect();
        saveSource();
        Intent intent = new Intent(AttdenceActivity.ACTION_UPDATA_SOURCE_SELCET);
        intent.setPackage(BigwinerApplication.mApp.getPackageName());
        mSourceSelectActivity.sendBroadcast(intent);
    }

    public void initSelect() {
        if(mSourceSelectActivity.getIntent().getIntExtra("type",0) == 0)
        {
            BigwinerApplication.mApp.my.positions.iselect = true;
//            BigwinerApplication.mApp.my.ports.iselect = true;
//            BigwinerApplication.mApp.my.serices.iselect = true;
//            BigwinerApplication.mApp.my.routes.iselect = true;
        }
        else
        {
            BigwinerApplication.mApp.want.positions.iselect = true;
//            BigwinerApplication.mApp.want.ports.iselect = true;
//            BigwinerApplication.mApp.want.serices.iselect = true;
//            BigwinerApplication.mApp.want.routes.iselect = true;
        }
    }

    public void cleanSelect() {
//        for(int i = 0 ; i < BigwinerApplication.mApp.ports.list.size() ; i++)
//        {
//            BigwinerApplication.mApp.ports.list.get(i).iselect = false;
//        }
        for(int i = 0 ; i < BigwinerApplication.mApp.positions.list.size() ; i++)
        {
            BigwinerApplication.mApp.positions.list.get(i).iselect = false;
        }
//        for(int i = 0 ; i < BigwinerApplication.mApp.serices.list.size() ; i++)
//        {
//            BigwinerApplication.mApp.serices.list.get(i).iselect = false;
//        }
//        for(int i = 0 ; i < BigwinerApplication.mApp.routes.list.size() ; i++)
//        {
//            BigwinerApplication.mApp.routes.list.get(i).iselect = false;
//        }
    }

    public void clickTeb(int id)
    {
        mSourceSelectActivity.btnPort.setBackgroundColor(Color.alpha(0xFFF5F5F5));
        mSourceSelectActivity.btnPosition.setBackgroundColor(Color.alpha(0xFFF5F5F5));
        mSourceSelectActivity.btnSerice.setBackgroundColor(Color.alpha(0xFFF5F5F5));
        mSourceSelectActivity.btnRoutes.setBackgroundColor(Color.alpha(0xFFF5F5F5));
        switch (id)
        {
            case R.id.ports:
                mSourceSelectActivity.btnPort.setBackgroundColor(Color.rgb(255,255,255));
                mSourceSelectActivity.mNoScrollViewPager.setCurrentItem(1);
                break;
            case R.id.position:
                mSourceSelectActivity.btnPosition.setBackgroundColor(Color.rgb(255,255,255));
                mSourceSelectActivity.mNoScrollViewPager.setCurrentItem(0);
                break;
            case R.id.serice:
                mSourceSelectActivity.btnSerice.setBackgroundColor(Color.rgb(255,255,255));
                mSourceSelectActivity.mNoScrollViewPager.setCurrentItem(2);
                break;
            case R.id.routes:
                mSourceSelectActivity.btnRoutes.setBackgroundColor(Color.rgb(255,255,255));
                mSourceSelectActivity.mNoScrollViewPager.setCurrentItem(3);
                break;
        }
    }

    public void onItemClick(SourceAdapter adapter, Select source) {

        switch (mSourceSelectActivity.mNoScrollViewPager.getCurrentItem())
        {
            case 0:
                if(source.iselect == false)
                {
                    source.iselect = true;
                }
                else
                {
                    return;
                }
                if(mSourceSelectActivity.getIntent().getIntExtra("type",0) == 0)
                {
                    BigwinerApplication.mApp.my.positions.iselect = false;
                    BigwinerApplication.mApp.my.positions = source;
                }
                else
                {
                    BigwinerApplication.mApp.want.positions.iselect = false;
                    BigwinerApplication.mApp.want.positions = source;
                }
                break;
            case 1:
                if(mSourceSelectActivity.getIntent().getIntExtra("type",0) == 0)
                {
                    if(!BigwinerApplication.mApp.my.ports.hashMap.containsKey(source.mId))
                    {
                        if(BigwinerApplication.mApp.my.ports.list.size() < SourceSelectActivity.MAX_SELECT)
                        BigwinerApplication.mApp.my.ports.add(source);
                        else
                            AppUtils.showMessage(mSourceSelectActivity,mSourceSelectActivity.getString(R.string.source_max));
                    }
                    else
                    {
                        BigwinerApplication.mApp.my.ports.remove(source.mId);
                    }
//                    BigwinerApplication.mApp.my.ports.iselect = false;
//                    BigwinerApplication.mApp.my.ports = source;
                }
                else
                {
                    if(!BigwinerApplication.mApp.want.ports.hashMap.containsKey(source.mId))
                    {
                        if(BigwinerApplication.mApp.want.ports.list.size() < SourceSelectActivity.MAX_SELECT)
                        BigwinerApplication.mApp.want.ports.add(source);
                        else
                        AppUtils.showMessage(mSourceSelectActivity,mSourceSelectActivity.getString(R.string.source_max));
                    }
                    else
                    {
                        BigwinerApplication.mApp.want.ports.remove(source.mId);
                    }
//                    BigwinerApplication.mApp.want.ports.iselect = false;
//                    BigwinerApplication.mApp.want.ports = source;
                }
                break;
            case 2:
                if(mSourceSelectActivity.getIntent().getIntExtra("type",0) == 0)
                {
                    if(!BigwinerApplication.mApp.my.businesstypeSelect.hashMap.containsKey(source.mId))
                    {
                        if(BigwinerApplication.mApp.my.businesstypeSelect.list.size() < SourceSelectActivity.MAX_SELECT)
                        {
                            if(checkType(BigwinerApplication.mApp.my.businesstypeSelect,source))
                                BigwinerApplication.mApp.my.businesstypeSelect.add(source);
                            else
                                AppUtils.showMessage(mSourceSelectActivity,mSourceSelectActivity.getString(R.string.source_type_error));
                        }
                        else
                            AppUtils.showMessage(mSourceSelectActivity,mSourceSelectActivity.getString(R.string.source_max));
                    }
                    else
                    {
                        BigwinerApplication.mApp.my.businesstypeSelect.remove(source.mId);
                    }
//                    BigwinerApplication.mApp.my.serices.iselect = false;
//                    BigwinerApplication.mApp.my.serices = source;
                }
                else
                {
                    if(!BigwinerApplication.mApp.want.businesstypeSelect.hashMap.containsKey(source.mId))
                    {
                        if(BigwinerApplication.mApp.want.businesstypeSelect.list.size() < SourceSelectActivity.MAX_SELECT)
                        {
                            if(checkType(BigwinerApplication.mApp.want.businesstypeSelect,source))
                            BigwinerApplication.mApp.want.businesstypeSelect.add(source);
                            else
                                AppUtils.showMessage(mSourceSelectActivity,mSourceSelectActivity.getString(R.string.source_type_error));
                        }
                        else
                        AppUtils.showMessage(mSourceSelectActivity,mSourceSelectActivity.getString(R.string.source_max));
                    }
                    else
                    {
                        BigwinerApplication.mApp.want.businesstypeSelect.remove(source.mId);
                    }
//                    BigwinerApplication.mApp.want.serices.iselect = false;
//                    BigwinerApplication.mApp.want.serices = source;
                }
                break;
            case 3:
                if(mSourceSelectActivity.getIntent().getIntExtra("type",0) == 0)
                {
                    if(!BigwinerApplication.mApp.my.businessareaSelect.hashMap.containsKey(source.mId))
                    {
                        if(BigwinerApplication.mApp.my.businessareaSelect.list.size() < SourceSelectActivity.MAX_SELECT)
                        BigwinerApplication.mApp.my.businessareaSelect.add(source);
                        else
                            AppUtils.showMessage(mSourceSelectActivity,mSourceSelectActivity.getString(R.string.source_max));
                    }
                    else
                    {
                        BigwinerApplication.mApp.my.businessareaSelect.remove(source.mId);
                    }
//                    BigwinerApplication.mApp.my.routes.iselect = false;
//                    BigwinerApplication.mApp.my.routes = source;
                }
                else
                {
                    if(!BigwinerApplication.mApp.want.businessareaSelect.hashMap.containsKey(source.mId))
                    {
                        if(BigwinerApplication.mApp.want.businessareaSelect.list.size() < SourceSelectActivity.MAX_SELECT)
                        BigwinerApplication.mApp.want.businessareaSelect.add(source);
                        else
                            AppUtils.showMessage(mSourceSelectActivity,mSourceSelectActivity.getString(R.string.source_max));
                    }
                    else
                    {
                        BigwinerApplication.mApp.want.businessareaSelect.remove(source.mId);
                    }
//                    BigwinerApplication.mApp.want.routes.iselect = false;
//                    BigwinerApplication.mApp.want.routes = source;
                }
                break;
        }
        adapter.notifyDataSetChanged();
    }

    private void saveSource()
    {
        SharedPreferences sharedPre1 = mSourceSelectActivity.getSharedPreferences(BigwinerApplication.mApp.mAccount.mUserName, 0);
        SharedPreferences.Editor e1 = sharedPre1.edit();
        e1.putString(UserDefine.MY_PORT, BigwinerApplication.mApp.my.ports.getString());
        e1.putString(UserDefine.MY_POSI, BigwinerApplication.mApp.my.positions.mName);
        e1.putString(UserDefine.MY_ROUTE, BigwinerApplication.mApp.my.businesstypeSelect.getString());
        e1.putString(UserDefine.MY_SERVICE, BigwinerApplication.mApp.my.businessareaSelect.getString());
        e1.putString(UserDefine.WANT_PORT, BigwinerApplication.mApp.want.ports.getString());
        e1.putString(UserDefine.WANT_POSI, BigwinerApplication.mApp.want.positions.mName);
        e1.putString(UserDefine.WANT_ROUTE, BigwinerApplication.mApp.want.businesstypeSelect.getString());
        e1.putString(UserDefine.WANT_SERVICE, BigwinerApplication.mApp.want.businessareaSelect.getString());
        e1.commit();


    }

    public void doSearchPosition(String keyword) {
        if(keyword.length() == 0)
        {
            if(!mSourceSelectActivity.listViews.get(0).getAdapter().equals(mSourceSelectActivity.sourcePositionAdapter)) {
                mSourceSelectActivity.listViews.get(0).setAdapter(mSourceSelectActivity.sourcePositionAdapter);
            }
        }
        else
        {
            mSourceSelectActivity.position.clear();
            for(int i = 0; i < BigwinerApplication.mApp.positions.list.size() ; i++) {
                Select mSelect = BigwinerApplication.mApp.positions.list.get(i);
                if(mSelect.mName.contains(keyword) || mSelect.mPingyin.contains(keyword)) {
                    mSourceSelectActivity.position.add(mSelect);
                }
            }
            if(!mSourceSelectActivity.listViews.get(0).getAdapter().equals(mSourceSelectActivity.sourceSearchPositionAdapter)){
                mSourceSelectActivity.listViews.get(0).setAdapter(mSourceSelectActivity.sourceSearchPositionAdapter);
            }else {
                mSourceSelectActivity.sourceSearchPositionAdapter.notifyDataSetChanged();
            }
        }

    }

    public void doSearchPort(String keyword) {
        if(keyword.length() == 0)
        {
            if(!mSourceSelectActivity.listViews.get(1).getAdapter().equals(mSourceSelectActivity.sourcePortAdaptert)) {
                mSourceSelectActivity.listViews.get(1).setAdapter(mSourceSelectActivity.sourcePortAdaptert);
            }
        }
        else
        {
            mSourceSelectActivity.ports.clear();
            for(int i = 0; i < BigwinerApplication.mApp.ports.list.size() ; i++) {
                Select mSelect = BigwinerApplication.mApp.ports.list.get(i);
                if(mSelect.mName.contains(keyword)|| mSelect.mPingyin.contains(keyword)) {
                    mSourceSelectActivity.ports.add(mSelect);
                }
            }
            if(!mSourceSelectActivity.listViews.get(1).getAdapter().equals(mSourceSelectActivity.sourceSearchPortAdaptert)){
                mSourceSelectActivity.listViews.get(1).setAdapter(mSourceSelectActivity.sourceSearchPortAdaptert);
            }else {
                mSourceSelectActivity.sourceSearchPortAdaptert.notifyDataSetChanged();
            }
        }

    }

    public void doSearchType(String keyword) {
        if(keyword.length() == 0)
        {
            if(!mSourceSelectActivity.listViews.get(2).getAdapter().equals(mSourceSelectActivity.sourceTypeAdapter)) {
                mSourceSelectActivity.listViews.get(2).setAdapter(mSourceSelectActivity.sourceTypeAdapter);
            }
        }
        else
        {
            mSourceSelectActivity.types.clear();
            for(int i = 0; i < BigwinerApplication.mApp.businesstypeSelect.list.size() ; i++) {
                Select mSelect = BigwinerApplication.mApp.businesstypeSelect.list.get(i);
                if(mSelect.mName.contains(keyword)|| mSelect.mPingyin.contains(keyword)) {
                    mSourceSelectActivity.types.add(mSelect);
                }
            }
            if(!mSourceSelectActivity.listViews.get(2).getAdapter().equals(mSourceSelectActivity.sourceSearchTypeAdapter)){
                mSourceSelectActivity.listViews.get(2).setAdapter(mSourceSelectActivity.sourceSearchTypeAdapter);
            }else {
                mSourceSelectActivity.sourceSearchTypeAdapter.notifyDataSetChanged();
            }
        }

    }

    public void doSearchArea(String keyword) {
        if(keyword.length() == 0)
        {
            if(!mSourceSelectActivity.listViews.get(3).getAdapter().equals(mSourceSelectActivity.sourceAreaAdapter)) {
                mSourceSelectActivity.listViews.get(3).setAdapter(mSourceSelectActivity.sourceAreaAdapter);
            }
        }
        else
        {
            mSourceSelectActivity.areas.clear();
            for(int i = 0; i < BigwinerApplication.mApp.businessareaSelect.list.size() ; i++) {
                Select mSelect = BigwinerApplication.mApp.businessareaSelect.list.get(i);
                if(mSelect.mName.contains(keyword)|| mSelect.mPingyin.contains(keyword)) {
                    mSourceSelectActivity.areas.add(mSelect);
                }
            }
            if(!mSourceSelectActivity.listViews.get(3).getAdapter().equals(mSourceSelectActivity.sourceSearchAreaAdapter)){
                mSourceSelectActivity.listViews.get(3).setAdapter(mSourceSelectActivity.sourceSearchAreaAdapter);
            }else {
                mSourceSelectActivity.sourceSearchAreaAdapter.notifyDataSetChanged();
            }
        }

    }

    public boolean checkType(MapSelect data, Select select) {
        if(data.list.size() > 0)
        {
            Select select1 = data.list.get(data.list.size()-1);
            String a = select1.mName.substring(0,select1.mName.indexOf("/"));
            String b = select.mName.substring(0,select1.mName.indexOf("/"));
            if(a.equals(b))
            {
                return true;
            }
        }
        else
        {
            return true;
        }
        return false;
    }
}
