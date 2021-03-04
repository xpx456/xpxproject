package com.dk.dkpad.presenter;


import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.PopupWindow;

import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dk.dkpad.R;
import com.dk.dkpad.TestManager;
import com.dk.dkpad.database.DBHelper;
import com.dk.dkpad.entity.Optation;
import com.dk.dkpad.entity.User;
import com.dk.dkpad.entity.UserDefine;
import com.dk.dkpad.handler.MainHandler;
import com.dk.dkpad.receiver.MainReceiver;
import com.dk.dkpad.view.DkPadApplication;
import com.dk.dkpad.view.QueryView;
import com.dk.dkpad.view.activity.MainActivity;
import com.dk.dkpad.view.activity.OptationActivity;
import com.dk.dkpad.view.activity.SettingActivity;
import com.dk.dkpad.view.activity.UserActivity;
import com.dk.dkpad.view.adapter.OptationAdapter;
import com.dk.dkpad.view.adapter.PagerMainAdapter;
import com.dk.dkpad.view.adapter.PkUserAdapter;
import com.dk.dkpad.view.adapter.UserAdapter;
import com.dk.dkpad.view.fragment.DetialSportFragment;
import com.dk.dkpad.view.fragment.HomeFragment;
import com.dk.dkpad.view.fragment.PersonFragment;


import java.io.File;

import intersky.appbase.MySimpleTarget;
import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.apputils.TimeUtils;
import intersky.echartoption.ArrayData;
import intersky.echartoption.ObjectData;
import xpx.bluetooth.BluetoothSetManager;

public class MainPresenter implements Presenter {


    public MainActivity mMainActivity;
    public MainHandler mainHandler;
    public UserAdapter userAdapter;
    public PkUserAdapter pkUserAdapter;
    public OptationAdapter optationAdapter;
    public boolean showhead = false;
    public boolean showoptation = false;
    public boolean tvshow = false;
    public QueryView queryView;
    private AudioManager mAudioManager;
    public PopupWindow popupWindow;
    public int currentVolume;
    public int nat;
    public MainPresenter(MainActivity MainActivity) {
        mMainActivity = MainActivity;
        mainHandler = new MainHandler(MainActivity);
        mMainActivity.setBaseReceiver(new MainReceiver(mainHandler));
    }

    @Override
    public void initView() {
        mMainActivity.flagFillBack = false;
        mMainActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        mMainActivity.setContentView(R.layout.activity_main);
        DkPadApplication.mApp.testManager.bluetoothSetManager.scanLeDevice();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mAudioManager = (AudioManager) mMainActivity.getSystemService(Context.AUDIO_SERVICE);
        }
        currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mMainActivity.timevalue1 = mMainActivity.findViewById(R.id.timevalue1);
        mMainActivity.timevalue2 = mMainActivity.findViewById(R.id.timevalue2);
        mMainActivity.speedvalue = mMainActivity.findViewById(R.id.speedvalue);
        mMainActivity.hertvalue = mMainActivity.findViewById(R.id.hertvalue);
        mMainActivity.selectvalue = mMainActivity.findViewById(R.id.selectvalue);
        mMainActivity.btnStart2 = mMainActivity.findViewById(R.id.start2);
        mMainActivity.btnAdd2 = mMainActivity.findViewById(R.id.add2);
        mMainActivity.btnDes2 = mMainActivity.findViewById(R.id.des2);
        mMainActivity.btntv2 = mMainActivity.findViewById(R.id.tv2);
        mMainActivity.btnlanya = mMainActivity.findViewById(R.id.bluetooth);
        mMainActivity.showlayer = mMainActivity.findViewById(R.id.seriesdata);
        mMainActivity.showlayer.setVisibility(View.INVISIBLE);
        initOptionSport();
        initOptionHome();
        initOptionPerson();
        queryView = new QueryView(mMainActivity,onItemClickListener);
        mMainActivity.showActionUp = AnimationUtils.loadAnimation(mMainActivity, R.anim.slide_in_top);
        mMainActivity.hideActionUp = AnimationUtils.loadAnimation(mMainActivity, R.anim.slide_out_top);
        mMainActivity.showActionDown = AnimationUtils.loadAnimation(mMainActivity, R.anim.slide_in_buttom);
        mMainActivity.hideActionDown = AnimationUtils.loadAnimation(mMainActivity, R.anim.slide_out_buttom);
        mMainActivity.btntv = mMainActivity.findViewById(R.id.tv);
        mMainActivity.tv = mMainActivity.findViewById(R.id.video);
        mMainActivity.buttomBtn = mMainActivity.findViewById(R.id.buttom_layer);
        mMainActivity.youku = mMainActivity.findViewById(R.id.youku);
        mMainActivity.youku.setOnClickListener(youkuListener);
        mMainActivity.tenxun = mMainActivity.findViewById(R.id.tx);
        mMainActivity.tenxun.setOnClickListener(txListener);
        mMainActivity.aiqiyi = mMainActivity.findViewById(R.id.aqy);
        mMainActivity.aiqiyi.setOnClickListener(aiqiyListener);

        mMainActivity.tv.setWebclientOptation(mMainActivity.webclientOptation);
        nat = 0;
        mMainActivity.tv.loadUrl("https://search.youku.com/search_video?keyword=%E5%81%A5%E8%BA%AB");

        mMainActivity.mlayoutHead = mMainActivity.findViewById(R.id.headimf);
        mMainActivity.viewPager = mMainActivity.findViewById(R.id.pager);

        mMainActivity.btnleft = mMainActivity.findViewById(R.id.left);
        mMainActivity.btnright = mMainActivity.findViewById(R.id.right);

        mMainActivity.personFragment = new PersonFragment();
        mMainActivity.homeFragment = new HomeFragment();
        mMainActivity.detialSportFragment = new DetialSportFragment();
        mMainActivity.headlayer = mMainActivity.findViewById(R.id.head);
        mMainActivity.oplayer = mMainActivity.findViewById(R.id.optation);
        mMainActivity.head = mMainActivity.findViewById(R.id.headselect);
        mMainActivity.head.setOnClickListener(headListener);
        mMainActivity.optation = mMainActivity.findViewById(R.id.optationselect);
        mMainActivity.optation.setOnClickListener(optationListener);
        mMainActivity.setting = mMainActivity.findViewById(R.id.setting);
        mMainActivity.setting.setOnClickListener(settingListener);
        mMainActivity.pagerMainAdapter = new PagerMainAdapter(mMainActivity.getSupportFragmentManager());
        mMainActivity.pagerMainAdapter.frags.add(mMainActivity.personFragment);
        mMainActivity.pagerMainAdapter.frags.add(mMainActivity.homeFragment);
        mMainActivity.pagerMainAdapter.frags.add(mMainActivity.detialSportFragment);
        mMainActivity.btnStart = mMainActivity.findViewById(R.id.start);
        mMainActivity.btnAdd = mMainActivity.findViewById(R.id.add);
        mMainActivity.btnDes = mMainActivity.findViewById(R.id.des);
        mMainActivity.btnMap = mMainActivity.findViewById(R.id.map);
        mMainActivity.btnWatch = mMainActivity.findViewById(R.id.watch);
        mMainActivity.optationList = mMainActivity.findViewById(R.id.optationlist);
        mMainActivity.sHead = mMainActivity.findViewById(R.id.headicon);
        mMainActivity.sHeadName = mMainActivity.findViewById(R.id.headname);
        mMainActivity.sOptation = mMainActivity.findViewById(R.id.optationicon);
        mMainActivity.sOptationName = mMainActivity.findViewById(R.id.optationname);
        mMainActivity.userList = mMainActivity.findViewById(R.id.headlist);
        mMainActivity.headarray = mMainActivity.findViewById(R.id.headarray);
        mMainActivity.optationArray = mMainActivity.findViewById(R.id.optationarray);
        mMainActivity.time = mMainActivity.findViewById(R.id.time);
        mMainActivity.btnShowHead = mMainActivity.findViewById(R.id.btnheadshow);
        mMainActivity.btnShowOptation = mMainActivity.findViewById(R.id.btnoptationshow);
        mMainActivity.btnStart.setOnClickListener(startListener);
        mMainActivity.btnAdd.setOnClickListener(addListener);
        mMainActivity.btnDes.setOnClickListener(desListener);
        mMainActivity.btnStart2.setOnClickListener(startListener);
        mMainActivity.btnAdd2.setOnClickListener(addListener);
        mMainActivity.btnDes2.setOnClickListener(desListener);
        //mMainActivity.btnWatch.setOnClickListener(watchListener);
        mMainActivity.btnShowHead.setOnClickListener(showHeadListener);
        mMainActivity.btnShowOptation.setOnClickListener(showOptationListener);
        mMainActivity.btnleft.setOnClickListener(leftListener);
        mMainActivity.btnright.setOnClickListener(rightListener);
        mMainActivity.btntv.setOnClickListener(showTvListener);
        mMainActivity.btntv2.setOnClickListener(showTvListener);
        mMainActivity.btnlanya.setOnClickListener(deviceListener);
        userAdapter = new UserAdapter(mMainActivity, DkPadApplication.mApp.users);
        pkUserAdapter = new PkUserAdapter(mMainActivity, DkPadApplication.mApp.users);
        optationAdapter = new OptationAdapter(mMainActivity, DkPadApplication.mApp.optations);
        DkPadApplication.mApp.testManager.sendData.add(sendData);
        updataTime();
        permission();
        mMainActivity.viewPager.setAdapter(mMainActivity.pagerMainAdapter);
        mMainActivity.viewPager.setCurrentItem(1);
        mMainActivity.viewPager.addOnPageChangeListener(onPageChangeListener);
        setUserView(DkPadApplication.mApp.selectUser);
        mMainActivity.sOptationName.setText(DkPadApplication.mApp.selectOptation.name);
        if(TestManager.testManager.checkDeviceConnect() == true)
        {
            mMainActivity.btnlanya.setImageResource(R.drawable.lanyas);
        }
        else
        {
            mMainActivity.btnlanya.setImageResource(R.drawable.lanya);
        }
        mainHandler.sendEmptyMessageDelayed(MainHandler.AUTO_CONNECT,500);
    }

    public void checkBlue()
    {
        if(TestManager.testManager.checkDeviceConnect() == false)
        {
            if(DkPadApplication.mApp.testManager.bluetoothSetManager.getLast().length() > 0)
            {
                DkPadApplication.mApp.testManager.bluetoothSetManager.
                        autoFindConnectDevice(mMainActivity,mMainActivity.findViewById(R.id.activity_main),DkPadApplication.mApp.testManager.bluetoothSetManager.getLast());
            }
        }
    }


    public void updateBluestate(Intent intent)
    {
        if(intent.getBooleanExtra("blue",false) == true)
        {
            mMainActivity.btnlanya.setImageResource(R.drawable.lanyas);
        }
        else
        {
            mMainActivity.btnlanya.setImageResource(R.drawable.lanya);
        }
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
        if(mMainActivity.tv != null)
        mMainActivity.tv.destroy();
    }


    public AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            BluetoothDevice bluetoothDevice = DkPadApplication.mApp.testManager.bluetoothSetManager.deviceslist.get(position);
            if(DkPadApplication.mApp.testManager.checkDeviceConnect() == false)
            {
                DkPadApplication.mApp.testManager.stopConnect();
            }
            else
            {
                DkPadApplication.mApp.testManager.connectDevice(mMainActivity,mMainActivity.findViewById(R.id.activity_main),bluetoothDevice.getAddress());
            }
            queryView.hidView();

        }
    };

    public View.OnClickListener showTvListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (tvshow) {
                tvshow = false;
                mMainActivity.btntv.setImageResource(R.drawable.tv);
                mMainActivity.btntv2.setImageResource(R.drawable.tv);
                hideUpload();
            } else {
                tvshow = true;
                mMainActivity.btntv.setImageResource(R.drawable.tvs);
                mMainActivity.btntv2.setImageResource(R.drawable.tvs);
                showUpload();
            }
        }
    };

    public View.OnClickListener youkuListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            nat = 0;
            mMainActivity.tv.loadUrl("https://search.youku.com/search_video?keyword=%E5%81%A5%E8%BA%AB");
        }
    };

    public View.OnClickListener txListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            nat = 1;
            mMainActivity.tv.loadUrl("https://m.v.qq.com/search.html?act=102&keyWord=%E5%81%A5%E8%BA%AB");
        }
    };

    public View.OnClickListener aiqiyListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            nat = 2;
            mMainActivity.tv.loadUrl("https://m.iqiyi.com/search.html?source=input&key=%E5%81%A5%E8%BA%AB&pos=&vfrm=2-3-0-1");
        }
    };



    public void showUpload() {
        if (mMainActivity.mlayoutHead.getVisibility() == View.INVISIBLE) {
            mMainActivity.mlayoutHead.startAnimation(mMainActivity.showActionUp);
            mMainActivity.mlayoutHead.setVisibility(View.VISIBLE);
            mMainActivity.showlayer.setVisibility(View.VISIBLE);

            //mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM,currentVolume,AudioManager.FLAG_PLAY_SOUND);
        }
    }

    public void hideUpload() {
        if (mMainActivity.mlayoutHead.getVisibility() == View.VISIBLE) {
            mMainActivity.mlayoutHead.startAnimation(mMainActivity.hideActionUp);
            mMainActivity.mlayoutHead.setVisibility(View.INVISIBLE);
            mMainActivity.showlayer.setVisibility(View.INVISIBLE);
            if(nat == 0)
            {
                mMainActivity.first = true;
                mMainActivity.tv.loadUrl("https://search.youku.com/search_video?keyword=%E5%81%A5%E8%BA%AB");

            }
            else if(nat == 1)
            {
                mMainActivity.first = true;
                mMainActivity.tv.loadUrl("https://m.v.qq.com/search.html?act=102&keyWord=%E5%81%A5%E8%BA%AB");
            }
            else
            {
                mMainActivity.first = true;
                mMainActivity.tv.loadUrl("https://m.iqiyi.com/search.html?source=input&key=%E5%81%A5%E8%BA%AB&pos=&vfrm=2-3-0-1");
            }
            //currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
           // mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM,1,AudioManager.FLAG_PLAY_SOUND);

        }
    }

    public void permission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(mMainActivity)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + mMainActivity.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("extra_prefs_show_button_bar", true);//是否显示button bar
                intent.putExtra("extra_prefs_set_next_text", "完成");
                intent.putExtra("extra_prefs_set_back_text", "返回");
                mMainActivity.startActivity(intent);
                return;
            } else {

            }
        } else {

        }
    }


    public View.OnClickListener settingListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(!Settings.canDrawOverlays(mMainActivity))
                {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,Uri.parse("package:" + mMainActivity.getPackageName()));
                    intent.putExtra("extra_prefs_show_button_bar", true);//是否显示button bar
                    intent.putExtra("extra_prefs_set_next_text", "完成");
                    intent.putExtra("extra_prefs_set_back_text", "返回");
                    mMainActivity.startActivity(intent);

                }
                else if ( !Settings.System.canWrite(mMainActivity))
                {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                    intent.putExtra("extra_prefs_show_button_bar", true);//是否显示button bar
                    intent.putExtra("extra_prefs_set_next_text", "完成");
                    intent.putExtra("extra_prefs_set_back_text", "返回");
                    mMainActivity.startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(mMainActivity, SettingActivity.class);
                    mMainActivity.startActivity(intent);
                }
            }
            else{
                Intent intent = new Intent(mMainActivity, SettingActivity.class);
                mMainActivity.startActivity(intent);
            }
        }
    };


    public View.OnClickListener startListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(DkPadApplication.mApp.testManager.xpxUsbManager.deviceerror == true)
            {
                AppUtils.showMessage(mMainActivity,mMainActivity.getString(R.string.setting_deive_error));
                return;
            }

            if(DkPadApplication.mApp.testManager.xpxUsbManager.connect)
            {
                if (DkPadApplication.mApp.testManager.test.isstart == false) {
                    DkPadApplication.mApp.testManager.test.isstart = true;
                    DkPadApplication.mApp.appSetProtectState(true);
                    mMainActivity.btnStart.setImageResource(R.drawable.stop);
                    mMainActivity.btnStart2.setImageResource(R.drawable.stop);
                    DkPadApplication.mApp.testManager.startTest();

                    DkPadApplication.mApp.sportData.addNew(DkPadApplication.mApp.testManager.test);
                    SharedPreferences.Editor editor = DkPadApplication.mApp.sharedPre.edit();
                    editor.putString(UserDefine.SETTING_LAST_TEST,DkPadApplication.mApp.testManager.test.rid);
                    editor.commit();
                } else {
                    DkPadApplication.mApp.testManager.test.isstart = false;
                    DkPadApplication.mApp.appSetProtectTime(DkPadApplication.mApp.maxProtectSecond);
                    DkPadApplication.mApp.appSetProtectState(false);
                    mMainActivity.btnStart.setImageResource(R.drawable.start);
                    mMainActivity.btnStart2.setImageResource(R.drawable.start);
                    DkPadApplication.mApp.testManager.stopTest();
                }
            }
            else
            {
                if(DkPadApplication.mApp.testManager.checkDeviceConnect() == true)
                {
                    if (DkPadApplication.mApp.testManager.test.isstart == false) {
                        DkPadApplication.mApp.testManager.test.isstart = true;
                        DkPadApplication.mApp.appSetProtectState(true);
                        mMainActivity.btnStart.setImageResource(R.drawable.stop);
                        mMainActivity.btnStart2.setImageResource(R.drawable.stop);
                        DkPadApplication.mApp.testManager.startTest();

                        DkPadApplication.mApp.sportData.addNew(DkPadApplication.mApp.testManager.test);
                        SharedPreferences.Editor editor = DkPadApplication.mApp.sharedPre.edit();
                        editor.putString(UserDefine.SETTING_LAST_TEST,DkPadApplication.mApp.testManager.test.rid);
                        editor.commit();
                    } else {
                        DkPadApplication.mApp.testManager.test.isstart = false;
                        DkPadApplication.mApp.appSetProtectTime(DkPadApplication.mApp.maxProtectSecond);
                        DkPadApplication.mApp.appSetProtectState(false);
                        mMainActivity.btnStart.setImageResource(R.drawable.start);
                        mMainActivity.btnStart2.setImageResource(R.drawable.start);
                        DkPadApplication.mApp.testManager.stopTest();
                    }
                }
                else
                {
                    AppUtils.showMessage(mMainActivity, mMainActivity.getString(R.string.connect_device_null));
                }



            }


            if(DkPadApplication.mApp.testManager.test.isstart)
            {
                mMainActivity.headlayer.setVisibility(View.INVISIBLE);
                mMainActivity.oplayer.setVisibility(View.INVISIBLE);
                mMainActivity.setting.setVisibility(View.INVISIBLE);
            }
            else
            {
                mMainActivity.headlayer.setVisibility(View.VISIBLE);
                mMainActivity.oplayer.setVisibility(View.VISIBLE);
                mMainActivity.setting.setVisibility(View.VISIBLE);
            }

        }
    };


    public View.OnClickListener addListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DkPadApplication.mApp.testManager.addLeave();
            setLeave(DkPadApplication.mApp.testManager.test.currentLeavel);
        }
    };


    public View.OnClickListener desListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DkPadApplication.mApp.testManager.desLeave();
            setLeave(DkPadApplication.mApp.testManager.test.currentLeavel);
        }
    };

    public View.OnClickListener deviceListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(DkPadApplication.mApp.testManager.test.isstart)
            {
                AppUtils.showMessage(mMainActivity,"请先暂停");
            }
            else
            {
                if(DkPadApplication.mApp.testManager.checkDeviceConnect() == true)
                {
                    if(DkPadApplication.mApp.testManager.type == 1)
                    {
                        DkPadApplication.mApp.testManager.stopTest();
                    }
                    DkPadApplication.mApp.testManager.stopConnect();
                }
            }
        }
    };

    public View.OnClickListener headListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mMainActivity, UserActivity.class);
            intent.putExtra("user",DkPadApplication.mApp.selectUser);
            mMainActivity.startActivity(intent);
        }
    };

    public View.OnClickListener optationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mMainActivity, OptationActivity.class);
            intent.putExtra("optation",DkPadApplication.mApp.selectOptation);
            mMainActivity.startActivity(intent);
        }
    };

    public View.OnClickListener showHeadListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (showhead) {
                showhead = false;
                mMainActivity.headarray.setImageResource(R.drawable.downarray);
                mMainActivity.userList.setVisibility(View.GONE);
            } else {
                showhead = true;
                mMainActivity.headarray.setImageResource(R.drawable.uparray);
                mMainActivity.userList.setVisibility(View.VISIBLE);
                mMainActivity.userList.setAdapter(userAdapter);
                mMainActivity.userList.setOnItemClickListener(userItemListener);
            }

        }
    };

    public View.OnClickListener showOptationListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (showoptation) {
                showoptation = false;
                mMainActivity.optationArray.setImageResource(R.drawable.downarray);
                mMainActivity.optationList.setVisibility(View.GONE);
            } else {
                showoptation = true;
                mMainActivity.optationArray.setImageResource(R.drawable.uparray);
                mMainActivity.optationList.setVisibility(View.VISIBLE);
                mMainActivity.optationList.setAdapter(optationAdapter);
                mMainActivity.optationList.setOnItemClickListener(optationItemListener);
            }
        }
    };

    public AdapterView.OnItemClickListener optationItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            OptationAdapter optationAdapter = (OptationAdapter) adapterView.getAdapter();
            Optation optation = optationAdapter.getItem(i);
            if (optation.name.length() == 0) {
                Optation optation1 = new Optation();
                Intent intent = new Intent(mMainActivity, OptationActivity.class);
                intent.putExtra("optation",optation1);
                mMainActivity.startActivity(intent);
            } else {
                if(DkPadApplication.mApp.testManager.test.finish == false && DkPadApplication.mApp.testManager.test.current > 0)
                {
                    popupWindow = AppUtils.creatXpxDialog(mMainActivity, null
                            , mMainActivity.getString(R.string.op_delete_title),
                            mMainActivity.getString(R.string.op_delete_title_msg),mMainActivity.getString(R.string.button_word_ok)
                            ,mMainActivity.getString(R.string.button_word_cancle), forgaveListener, mMainActivity.findViewById(R.id.activity_main),optation);
                }
                else
                {
                    DkPadApplication.mApp.optations.add(0,DkPadApplication.mApp.selectOptation);
                    DkPadApplication.mApp.optations.remove(optation);
                    DkPadApplication.mApp.selectOptation = optation;
                    mMainActivity.sOptationName.setText(optation.name);
                    optationAdapter.notifyDataSetChanged();
                    DkPadApplication.mApp.setLastOptation(DkPadApplication.mApp.selectOptation);
                    DkPadApplication.mApp.testManager.setNewTest(DkPadApplication.mApp.selectOptation,DkPadApplication.mApp.selectUser);
                    mMainActivity.homeFragment.initView();
                }

            }
        }
    };

    public View.OnClickListener forgaveListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Optation optation = (Optation) view.getTag();
            DkPadApplication.mApp.optations.add(0,DkPadApplication.mApp.selectOptation);
            DkPadApplication.mApp.optations.remove(optation);
            DkPadApplication.mApp.selectOptation = optation;
            mMainActivity.sOptationName.setText(optation.name);
            optationAdapter.notifyDataSetChanged();
            DkPadApplication.mApp.setLastOptation(DkPadApplication.mApp.selectOptation);
            DkPadApplication.mApp.testManager.setNewTest(DkPadApplication.mApp.selectOptation,DkPadApplication.mApp.selectUser);
            DkPadApplication.mApp.sportData.last = DkPadApplication.mApp.testManager.test;
            SharedPreferences.Editor editor = DkPadApplication.mApp.sharedPre.edit();
            editor.putString(UserDefine.SETTING_LAST_TEST,DkPadApplication.mApp.testManager.test.rid);
            editor.commit();
            mMainActivity.homespeed.put("value", 0);
            mMainActivity.homehert.put("value", 0);
            mMainActivity.homeselect.put("value", DkPadApplication.mApp.testManager.test.currentLeavel);
            mMainActivity.speedvalue.setText("0"+"km/h");
            mMainActivity.hertvalue.setText("0"+"bpm");
            mMainActivity.selectvalue.setText(String.valueOf(DkPadApplication.mApp.testManager.test.currentLeavel)+"%");

            mMainActivity.homeFragment.initView();
            if(popupWindow != null)
                popupWindow.dismiss();
        }
    };

    public AdapterView.OnItemClickListener userItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            UserAdapter userAdapter = (UserAdapter) adapterView.getAdapter();
            User user = userAdapter.getItem(i);
            if (user.name.length() == 0) {
                User user1 = new User();
                Intent intent = new Intent(mMainActivity, UserActivity.class);
                intent.putExtra("user",user1);
                mMainActivity.startActivity(intent);
            } else {

                if(DkPadApplication.mApp.testManager.test.finish == false && DkPadApplication.mApp.testManager.test.current > 0)
                {
                    AppUtils.creatXpxDialog(mMainActivity, null
                            , mMainActivity.getString(R.string.op_delete_title),
                            mMainActivity.getString(R.string.op_delete_title_msg),mMainActivity.getString(R.string.button_word_ok)
                            ,mMainActivity.getString(R.string.button_word_cancle), forgaveListener2, mMainActivity.findViewById(R.id.activity_main),user);
                }
                else
                {
                    DkPadApplication.mApp.users.add(0,DkPadApplication.mApp.selectUser);
                    DkPadApplication.mApp.users.remove(user);
                    DkPadApplication.mApp.selectUser = user;
                    setUserView(user);
                    userAdapter.notifyDataSetChanged();
                    pkUserAdapter.notifyDataSetChanged();
                    DkPadApplication.mApp.setLastUser(DkPadApplication.mApp.selectUser);
                    DkPadApplication.mApp.testManager.setNewTest(DkPadApplication.mApp.selectOptation,DkPadApplication.mApp.selectUser);
                    mMainActivity.homeFragment.initView();
                }
            }
        }
    };

    public View.OnClickListener forgaveListener2 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            User user = (User) view.getTag();
            DkPadApplication.mApp.users.add(0,DkPadApplication.mApp.selectUser);
            DkPadApplication.mApp.users.remove(user);
            DkPadApplication.mApp.selectUser = user;
            setUserView(user);
            userAdapter.notifyDataSetChanged();
            pkUserAdapter.notifyDataSetChanged();
            DkPadApplication.mApp.setLastUser(DkPadApplication.mApp.selectUser);
            DkPadApplication.mApp.testManager.setNewTest(DkPadApplication.mApp.selectOptation,DkPadApplication.mApp.selectUser);
            DkPadApplication.mApp.sportData.last = DkPadApplication.mApp.testManager.test;
            SharedPreferences.Editor editor = DkPadApplication.mApp.sharedPre.edit();
            editor.putString(UserDefine.SETTING_LAST_TEST,DkPadApplication.mApp.testManager.test.rid);
            editor.commit();
            mMainActivity.homespeed.put("value", 0);
            mMainActivity.homehert.put("value", 0);
            mMainActivity.homeselect.put("value", DkPadApplication.mApp.testManager.test.currentLeavel);
            mMainActivity.speedvalue.setText("0"+"km/h");
            mMainActivity.hertvalue.setText("0"+"bpm");
            mMainActivity.selectvalue.setText(String.valueOf(DkPadApplication.mApp.testManager.test.currentLeavel)+"档");
            mMainActivity.homeFragment.initView();
            if(popupWindow != null)
                popupWindow.dismiss();

        }
    };


    public void setUserView(User user) {
        if (user != null) {
            mMainActivity.sHeadName.setText(user.name);
            if (user.sex.equals(mMainActivity.getString(R.string.male))) {
                if (user.headpath.length() > 0) {
                    RequestOptions options = new RequestOptions()
                            .placeholder(R.drawable.default_user);
                    Glide.with(mMainActivity).load(new File(user.headpath)).apply(options).into(new MySimpleTarget(mMainActivity.sHead));
                } else {
                    mMainActivity.sHead.setImageResource(R.drawable.default_user);
                }

            } else {
                if (user.headpath.length() > 0) {
                    RequestOptions options = new RequestOptions()
                            .placeholder(R.drawable.default_wuser);
                    Glide.with(mMainActivity).load(new File(user.headpath)).apply(options).into(new MySimpleTarget(mMainActivity.sHead));
                } else {
                    mMainActivity.sHead.setImageResource(R.drawable.default_wuser);
                }
            }
            if(DkPadApplication.mApp.pk != null)
            {
                if(DkPadApplication.mApp.selectUser.equals(DkPadApplication.mApp.pk.uid))
                {
                    DkPadApplication.mApp.pk.select = false;
                    DkPadApplication.mApp.pk = null;
                }
            }
            resetSport();
            mMainActivity.personFragment.updataSport();

        }
    }

    public void updataSport() {
        //DkPadApplication.mApp.sportData = DBHelper.getInstance(mMainActivity).scanRecords(DkPadApplication.mApp.selectUser.uid);
        ArrayData value = new ArrayData();
        mMainActivity.myvalue.put("value", value);
        int a = DkPadApplication.mApp.sportData.getdayTime();
        double b = DkPadApplication.mApp.sportData.getDayDistence();
        int c = DkPadApplication.mApp.sportData.getaverLeavel();
        double d = DkPadApplication.mApp.sportData.getrat();
        double e = DkPadApplication.mApp.sportData.gettopspeed();
        double f = DkPadApplication.mApp.sportData.getaspeed();

        value.add(DkPadApplication.mApp.sportData.getdayTime());
        value.add(DkPadApplication.mApp.sportData.getDayDistence());
        value.add(DkPadApplication.mApp.sportData.getaverLeavel());
        value.add(DkPadApplication.mApp.sportData.getrat());
        value.add(DkPadApplication.mApp.sportData.gettopspeed());
        value.add(DkPadApplication.mApp.sportData.getaspeed());
        mMainActivity.myvalue.put("name", DkPadApplication.mApp.selectUser.name);
        mMainActivity.personopLegend.array.clear();
        mMainActivity.personopLegend.add(DkPadApplication.mApp.selectUser.name);
        if(DkPadApplication.mApp.pk != null)
        {
            mMainActivity.personopLegend.add(DkPadApplication.mApp.pk.name);
        }
        if(DkPadApplication.mApp.pk == null)
        {
            mMainActivity.seriesItem.put("name",DkPadApplication.mApp.selectUser.name);
        }
        else{
            mMainActivity.seriesItem.put("name",DkPadApplication.mApp.selectUser.name+" VS "+DkPadApplication.mApp.pk.name);
        }
        if(mMainActivity.personFragment != null)
        {
            mMainActivity.personFragment.updataSport();
        }
    }

    public void resetSport() {
        DkPadApplication.mApp.sportData = DBHelper.getInstance(mMainActivity).scanRecords(DkPadApplication.mApp.selectUser.uid);
        ArrayData value = new ArrayData();
        mMainActivity.myvalue.put("value", value);
        int a = DkPadApplication.mApp.sportData.getdayTime();
        double b = DkPadApplication.mApp.sportData.getDayDistence();
        int c = DkPadApplication.mApp.sportData.getaverLeavel();
        double d = DkPadApplication.mApp.sportData.getrat();
        double e = DkPadApplication.mApp.sportData.gettopspeed();
        double f = DkPadApplication.mApp.sportData.getaspeed();

        value.add(DkPadApplication.mApp.sportData.getdayTime());
        value.add(DkPadApplication.mApp.sportData.getDayDistence());
        value.add(DkPadApplication.mApp.sportData.getaverLeavel());
        value.add(DkPadApplication.mApp.sportData.getrat());
        value.add(DkPadApplication.mApp.sportData.gettopspeed());
        value.add(DkPadApplication.mApp.sportData.getaspeed());
        mMainActivity.myvalue.put("name", DkPadApplication.mApp.selectUser.name);
        mMainActivity.personopLegend.array.clear();
        mMainActivity.personopLegend.add(DkPadApplication.mApp.selectUser.name);
        if(DkPadApplication.mApp.pk != null)
        {
            mMainActivity.personopLegend.add(DkPadApplication.mApp.pk.name);
        }
        if(DkPadApplication.mApp.pk == null)
        {
            mMainActivity.seriesItem.put("name",DkPadApplication.mApp.selectUser.name);
        }
        else{
            mMainActivity.seriesItem.put("name",DkPadApplication.mApp.selectUser.name+" VS "+DkPadApplication.mApp.pk.name);
        }
        if(mMainActivity.personFragment != null)
        {
            mMainActivity.personFragment.updataSport();
        }
    }

    public void updataTime() {
        mMainActivity.time.setText(TimeUtils.getDateAndTimeC(mMainActivity));
        mainHandler.sendEmptyMessageDelayed(MainHandler.UPDATA_TIME, 1000);
    }

    public TestManager.SendData sendData = new TestManager.SendData() {
        @Override
        public void sendData(String[] data) {
            Message msg = new Message();
            msg.what = MainHandler.EVENT_UPDATA;
            msg.obj = data;
            mainHandler.sendMessage(msg);
        }

        @Override
        public void sendDataFinish(String[] data) {
            Message msg = new Message();
            msg.what = MainHandler.EVENT_FINISH;
            msg.obj = data;
            mainHandler.sendMessage(msg);
        }

        @Override
        public void error(String[] data) {
            Message msg = new Message();
            msg.what = MainHandler.EVENT_ERROR;
            msg.obj = data;
            mainHandler.sendMessage(msg);
        }
    };


    public void updataOptation(Intent intent) {
        Optation optation = intent.getParcelableExtra("optation");
        if (intent.getBooleanExtra("delete", false) == true) {
            Optation temp = DkPadApplication.mApp.hashoptations.get(optation.oid);
            DkPadApplication.mApp.hashoptations.remove(optation.oid);
            if (temp != null)
                DkPadApplication.mApp.optations.remove(temp);
            DkPadApplication.mApp.selectOptation = DkPadApplication.mApp.optations.get(0);
            mMainActivity.sOptationName.setText(DkPadApplication.mApp.selectOptation.name);
            DkPadApplication.mApp.optations.remove(DkPadApplication.mApp.selectOptation);
            optationAdapter.notifyDataSetChanged();
            DkPadApplication.mApp.testManager.setNewTest(DkPadApplication.mApp.selectOptation,DkPadApplication.mApp.selectUser);
        } else {
            if (DkPadApplication.mApp.hashoptations.containsKey(optation.oid)) {
                Optation temp = DkPadApplication.mApp.hashoptations.get(optation.oid);
                temp.name = optation.name;
                temp.data = optation.data;
                optationAdapter.notifyDataSetChanged();
            } else {
                DkPadApplication.mApp.hashoptations.put(optation.oid, optation);
                DkPadApplication.mApp.optations.add(0,optation);
                optationAdapter.notifyDataSetChanged();
            }
            if(DkPadApplication.mApp.selectOptation.equals(optation.oid))
            {
                if(DkPadApplication.mApp.testManager.test.finish == false && DkPadApplication.mApp.testManager.test.current > 0)
                {


                }
                DkPadApplication.mApp.optations.add(0,DkPadApplication.mApp.selectOptation);
                DkPadApplication.mApp.optations.remove(optation);
                DkPadApplication.mApp.selectOptation = optation;
                mMainActivity.sOptationName.setText(optation.name);
                optationAdapter.notifyDataSetChanged();
                DkPadApplication.mApp.setLastOptation(DkPadApplication.mApp.selectOptation);
                DkPadApplication.mApp.testManager.setNewTest(DkPadApplication.mApp.selectOptation,DkPadApplication.mApp.selectUser);
                DkPadApplication.mApp.sportData.last = DkPadApplication.mApp.testManager.test;
                SharedPreferences.Editor editor = DkPadApplication.mApp.sharedPre.edit();
                editor.putString(UserDefine.SETTING_LAST_TEST,DkPadApplication.mApp.testManager.test.rid);
                editor.commit();
                mMainActivity.homespeed.put("value", 0);
                mMainActivity.homehert.put("value", 0);
                mMainActivity.homeselect.put("value", DkPadApplication.mApp.testManager.test.currentLeavel);
                mMainActivity.speedvalue.setText("0"+"km/h");
                mMainActivity.hertvalue.setText("0"+"bpm");
                mMainActivity.selectvalue.setText(String.valueOf(DkPadApplication.mApp.testManager.test.currentLeavel)+"%");
                popupWindow = AppUtils.creatXpxDialog(mMainActivity, null
                        , mMainActivity.getString(R.string.op_delete_title),
                        mMainActivity.getString(R.string.op_delete_title_msg),mMainActivity.getString(R.string.button_word_ok)
                        ,mMainActivity.getString(R.string.button_word_cancle), forgaveListener, mMainActivity.findViewById(R.id.activity_main),optation);
            }
        }

    }

    public void updataHead(Intent intent) {
        User user = intent.getParcelableExtra("user");
        if (intent.getBooleanExtra("delete", false) == true) {
            User temp = DkPadApplication.mApp.hashMap.get(user.uid);
            DkPadApplication.mApp.hashMap.remove(user.uid);
            if (temp != null)
                DkPadApplication.mApp.users.remove(temp);

            DkPadApplication.mApp.selectUser = DkPadApplication.mApp.users.get(0);
            setUserView(DkPadApplication.mApp.selectUser);
            DkPadApplication.mApp.users.remove(DkPadApplication.mApp.selectUser);
            userAdapter.notifyDataSetChanged();
            pkUserAdapter.notifyDataSetChanged();
            DkPadApplication.mApp.testManager.setNewTest(DkPadApplication.mApp.selectOptation,DkPadApplication.mApp.selectUser);
        } else {
            if (DkPadApplication.mApp.hashMap.containsKey(user.uid)) {
                User temp = DkPadApplication.mApp.hashMap.get(user.uid);
                temp.name = user.name;
                temp.age = user.age;
                temp.toll = user.toll;
                temp.sex = user.sex;
                temp.headpath = user.headpath;
                userAdapter.notifyDataSetChanged();
                pkUserAdapter.notifyDataSetChanged();
            } else {
                DkPadApplication.mApp.hashMap.put(user.uid, user);
                DkPadApplication.mApp.users.add(0,user);
                userAdapter.notifyDataSetChanged();
                pkUserAdapter.notifyDataSetChanged();
            }
        }

    }

    public void initOptionSport() {
        mMainActivity.sportoption = new ObjectData();
        ObjectData grid = new ObjectData();
        mMainActivity.sportoption.put("backgroundColor", "rgba(0, 0, 0, 0)");
        mMainActivity.sportoption.put("grid", grid);
        grid.put("left", "18%");
        grid.put("right", "5%");
        grid.put("containLabel", true);
        ArrayData datazoom = new ArrayData();
        mMainActivity.sportoption.put("dataZoom", datazoom);
        ObjectData zoom1 = new ObjectData();
        datazoom.add(zoom1);
        zoom1.put("type", "slider");
        zoom1.put("start", 10);
        zoom1.put("end", 60);
        ObjectData zoom2 = new ObjectData();
        datazoom.add(zoom2);
        zoom2.put("type", "inside");
        zoom2.put("start", 10);
        zoom2.put("end", 60);

        ObjectData title = new ObjectData();
        mMainActivity.sportoption.put("title", title);
        title.put("text", "运动状态图");
        ArrayData color = new ArrayData();
        mMainActivity.sportoption.put("color", color);
        color.add(MainActivity.SPEET_COLOR);
        color.add(MainActivity.HERT_COLOR);
        color.add(MainActivity.SELECT_COLOR);

        ObjectData legend = new ObjectData();
        mMainActivity.sportoption.put("legend", legend);
        mMainActivity.sportLegend = new ArrayData();
        legend.put("data", mMainActivity.sportLegend);
        mMainActivity.sportLegend.add("速度");
        mMainActivity.sportLegend.add("心率");
        mMainActivity.sportLegend.add("%位");

        ObjectData xAxis = new ObjectData();
        mMainActivity.sportoption.put("xAxis", xAxis);
        xAxis.put("type", "category");
        mMainActivity.sportx = new ArrayData();
        xAxis.put("data", mMainActivity.sportx);

        ArrayData yAxiss = new ArrayData();
        mMainActivity.sportoption.put("yAxis", yAxiss);
        ObjectData yAxis = new ObjectData();
        yAxis.put("type", "value");
        yAxis.put("position", "left");
        yAxis.put("name", "速度(km/h)");
        yAxis.put("max", "40");
        ObjectData axisLine = new ObjectData();
        yAxis.put("axisLine", axisLine);
        ObjectData lineStyle = new ObjectData();
        axisLine.put("lineStyle", lineStyle);
        lineStyle.put("color", MainActivity.SPEET_COLOR);
        yAxiss.add(yAxis);


        yAxis = new ObjectData();
        yAxis.put("type", "value");
        yAxis.put("position", "left");
        yAxis.put("offset", "65");
        yAxis.put("name", "心率(bpm)");
        yAxis.put("max", "220");
        axisLine = new ObjectData();
        yAxis.put("axisLine", axisLine);
        lineStyle = new ObjectData();
        axisLine.put("lineStyle", lineStyle);
        lineStyle.put("color", MainActivity.HERT_COLOR);
        yAxiss.add(yAxis);


        yAxis = new ObjectData();
        yAxis.put("type", "value");
        yAxis.put("position", "left");
        yAxis.put("offset", "110");
        yAxis.put("name", "阻力(%)");
        yAxis.put("max", "100");
        axisLine = new ObjectData();
        yAxis.put("axisLine", axisLine);
        lineStyle = new ObjectData();
        axisLine.put("lineStyle", lineStyle);
        lineStyle.put("color", MainActivity.SELECT_COLOR);
        yAxiss.add(yAxis);

        ArrayData series = new ArrayData();
        mMainActivity.sportoption.put("series", series);
        ObjectData speed = new ObjectData();
        series.add(speed);
        speed.put("name", "速度");
        speed.put("type", "line");
        speed.put("yAxisIndex", 0);
        speed.put("symbol","none");
        speed.put("smooth",true);
        ObjectData areaStyle = new ObjectData();
        speed.put("areaStyle",areaStyle);
        areaStyle.put("color",MainActivity.SPEET_COLOR);
        ObjectData itemStyle = new ObjectData();
        speed.put("itemStyle", itemStyle);
        ObjectData normal = new ObjectData();
        speed.put("normal", normal);
        normal.put("color", MainActivity.SPEET_COLOR);
        lineStyle = new ObjectData();
        normal.put("lineStyle", lineStyle);
        lineStyle.put("color", MainActivity.SPEET_COLOR);
        mMainActivity.sportspeed = new ArrayData();
        speed.put("data", mMainActivity.sportspeed);

        ObjectData hert = new ObjectData();
        series.add(hert);
        hert.put("name", "心率");
        hert.put("type", "line");
        hert.put("symbol","none");
        hert.put("smooth",true);
        hert.put("yAxisIndex", 1);
        areaStyle = new ObjectData();
        hert.put("areaStyle",areaStyle);
        areaStyle.put("color",MainActivity.HERT_COLOR);
        itemStyle = new ObjectData();
        hert.put("itemStyle", itemStyle);
        normal = new ObjectData();
        hert.put("normal", normal);
        normal.put("color", MainActivity.HERT_COLOR);
        lineStyle = new ObjectData();
        normal.put("lineStyle", lineStyle);
        lineStyle.put("color", MainActivity.HERT_COLOR);
        mMainActivity.sporthert = new ArrayData();
        hert.put("data", mMainActivity.sporthert);

        ObjectData select = new ObjectData();
        series.add(select);
        select.put("name", "%位");
        select.put("type", "line");
        select.put("yAxisIndex", 2);
        select.put("symbol","none");
        select.put("smooth",true);
        areaStyle = new ObjectData();
        select.put("areaStyle",areaStyle);
        areaStyle.put("color",MainActivity.SELECT_COLOR);
        itemStyle = new ObjectData();
        select.put("itemStyle", itemStyle);
        normal = new ObjectData();
        select.put("normal", normal);
        normal.put("color", MainActivity.SELECT_COLOR);
        lineStyle = new ObjectData();
        normal.put("lineStyle", lineStyle);
        lineStyle.put("color", MainActivity.SELECT_COLOR);
        mMainActivity.sportselect = new ArrayData();
        select.put("data", mMainActivity.sportselect);
    }


    public void initOptionHome() {
        mMainActivity.homeoption = new ObjectData();

        ObjectData tooltip = new ObjectData();
        mMainActivity.homeoption.put("tooltip", tooltip);
        tooltip.put("formatter", "{a} <br/>{c} {b}");
        mMainActivity.homeoption.put("backgroundColor", "rgba(0, 0, 0, 0)");
        ObjectData toolbox = new ObjectData();
        mMainActivity.homeoption.put("toolbox", toolbox);
        toolbox.put("show", true);
        ObjectData feature = new ObjectData();
        toolbox.put("feature", feature);
        ObjectData restore = new ObjectData();
        feature.put("restore", restore);
        restore.put("show", true);
        ObjectData saveAsImage = new ObjectData();
        feature.put("saveAsImage", saveAsImage);
        saveAsImage.put("show", true);

        ArrayData series = new ArrayData();
        mMainActivity.homeoption.put("series", series);

        ObjectData speed = new ObjectData();
        speed.put("name", "速度");
        speed.put("type", "gauge");
        speed.put("z", 3);
        speed.put("min", 0);
        speed.put("max", 40);
        speed.put("splitNumber", 10);
        speed.put("radius", "60%");

        ObjectData axisLines = new ObjectData();
        speed.put("axisLine", axisLines);
        ObjectData lineStyleset = new ObjectData();
        axisLines.put("lineStyle", lineStyleset);
        lineStyleset.put("width", 10);
        ArrayData color = new ArrayData();
        lineStyleset.put("color",color);
        ArrayData color1 = new ArrayData();
        color1.add(0.2);
        color1.add("#108135");
        ArrayData color2 = new ArrayData();
        color2.add(0.8);
        color2.add("#f08519");
        ArrayData color3 = new ArrayData();
        color3.add(1);
        color3.add("#fc210a");
        color.add(color1);
        color.add(color2);
        color.add(color3);

        ObjectData axisTick = new ObjectData();
        speed.put("axisTick", axisTick);
        axisTick.put("length", 15);
        ObjectData lineStyle2 = new ObjectData();
        axisTick.put("lineStyle", lineStyle2);
        lineStyle2.put("color", "auto");

        ObjectData splitLine = new ObjectData();
        speed.put("splitLine", splitLine);
        axisTick.put("length", 20);
        ObjectData lineStyle3 = new ObjectData();
        splitLine.put("lineStyle", lineStyle3);
        lineStyle3.put("color", "auto");

        ObjectData axisLabel = new ObjectData();
        speed.put("axisLabel", axisLabel);
        axisLabel.put("fontSize", 25);
        axisLabel.put("backgroundColor", "auto");
        axisLabel.put("borderRadius", 2);
        axisLabel.put("color", "#eee");
        axisLabel.put("padding", 3);
        axisLabel.put("textShadowBlur", 2);
        axisLabel.put("textShadowOffsetX", 1);
        axisLabel.put("textShadowOffsetY", 1);
        axisLabel.put("textShadowColor", "#222");

        ObjectData title = new ObjectData();
        speed.put("title", title);
        title.put("fontWeight", "bolder");
        title.put("fontSize", 40);
        title.put("fontStyle", "italic");

        ObjectData detail = new ObjectData();
        speed.put("detail", detail);
        detail.put("fontWeight", "bolder");
        detail.put("fontSize", 30);
        detail.put("borderRadius", 3);
        detail.put("backgroundColor", "#444");
        detail.put("borderColor", "#aaa");
        detail.put("shadowBlur", 5);
        detail.put("shadowColor", "#333");
        detail.put("shadowOffsetX", 0);
        detail.put("shadowOffsetY", 3);
        detail.put("borderWidth", 2);
        detail.put("textBorderColor", "#000");
        detail.put("textBorderWidth", 2);
        detail.put("textShadowBlur", 2);
        detail.put("textShadowColor", "#fff");
        detail.put("textShadowOffsetX", 0);
        detail.put("textShadowOffsetY", 0);
        detail.put("fontFamily", "Arial");
        detail.put("width", 80);
        detail.put("color", "#eee");
        detail.put("rich", new ObjectData());

        ArrayData data = new ArrayData();
        speed.put("data", data);
        mMainActivity.homespeed = new ObjectData();
        data.add(mMainActivity.homespeed);
        mMainActivity.homespeed.put("value", 0);
        mMainActivity.homespeed.put("name", "km/h");

        ObjectData hert = new ObjectData();
        hert.put("name", "心率");
        hert.put("type", "gauge");
        ArrayData center = new ArrayData();
        hert.put("center", center);
        center.add("22%");
        center.add("55%");
        hert.put("radius", "40%");
        hert.put("min", 0);
        hert.put("max", 220);
        hert.put("endAngle", 45);
        hert.put("splitNumber", 11);

        ObjectData axisLine1 = new ObjectData();
        hert.put("axisLine", axisLine1);
        lineStyleset = new ObjectData();
        axisLine1.put("lineStyle", lineStyleset);
        lineStyleset.put("width", 8);
        lineStyleset.put("color",color);

        ObjectData axisTick1 = new ObjectData();
        hert.put("axisTick", axisTick1);
        axisTick1.put("length", 12);
        ObjectData lineStyle4 = new ObjectData();
        axisTick1.put("lineStyle", lineStyle4);
        lineStyle4.put("color", "auto");

        ObjectData splitLine1 = new ObjectData();
        hert.put("splitLine", splitLine1);
        splitLine1.put("length", 20);
        ObjectData lineStyle5 = new ObjectData();
        splitLine1.put("lineStyle", lineStyle5);
        lineStyle5.put("color", "auto");

        ObjectData pointer = new ObjectData();
        hert.put("pointer", pointer);
        pointer.put("width", 5);

        ObjectData axisLabel1 = new ObjectData();
        hert.put("axisLabel", axisLabel1);
        axisLabel1.put("fontSize", 15);

        ObjectData title2 = new ObjectData();
        hert.put("title", title2);
        ArrayData offsetCenter = new ArrayData();
        title2.put("fontSize", 30);
        title2.put("offsetCenter", offsetCenter);
        offsetCenter.add(0);
        offsetCenter.add("-30%");

        ObjectData detail1 = new ObjectData();
        hert.put("detail", detail1);
        detail1.put("fontWeight", "bolder");

        ArrayData data1 = new ArrayData();
        hert.put("data", data1);
        mMainActivity.homehert = new ObjectData();
        data1.add(mMainActivity.homehert);
        mMainActivity.homehert.put("value", 0);
        mMainActivity.homehert.put("name", "bpm");


        ObjectData starng = new ObjectData();
        starng.put("name", "阻力");
        starng.put("type", "gauge");
        ArrayData center1 = new ArrayData();
        starng.put("center", center1);
        center1.add("78%");
        center1.add("55%");
        starng.put("radius", "40%");
        starng.put("min", 0);
        starng.put("max", String.valueOf(DkPadApplication.mApp.maxSelect));
        starng.put("endAngle", -45);
        starng.put("startAngle", 135);
        starng.put("splitNumber", 8);

        ObjectData axisLine2 = new ObjectData();
        starng.put("axisLine", axisLine2);
        lineStyleset = new ObjectData();
        axisLine2.put("lineStyle", lineStyleset);
        lineStyleset.put("width", 8);
        lineStyleset.put("color",color);

        ObjectData axisTick2 = new ObjectData();
        starng.put("axisTick", axisTick2);
        axisTick2.put("length", 12);
        ObjectData lineStyle7 = new ObjectData();
        axisTick2.put("lineStyle", lineStyle7);
        lineStyle7.put("color", "auto");

        ObjectData splitLine2 = new ObjectData();
        starng.put("splitLine", splitLine2);
        splitLine2.put("length", 20);
        ObjectData lineStyle8 = new ObjectData();
        splitLine2.put("lineStyle", lineStyle8);
        lineStyle8.put("color", "auto");

        ObjectData pointer1 = new ObjectData();
        starng.put("pointer", pointer1);
        pointer1.put("width", 5);

        ObjectData axisLabel2 = new ObjectData();
        starng.put("axisLabel", axisLabel2);
        axisLabel2.put("fontSize", 15);


        ObjectData title3 = new ObjectData();
        starng.put("title", title3);
        title3.put("fontSize", 30);
        ArrayData offsetCenter1 = new ArrayData();
        title3.put("offsetCenter", offsetCenter1);
        offsetCenter1.add(0);
        offsetCenter1.add("-30%");

        ObjectData detail2 = new ObjectData();
        starng.put("detail", detail2);
        detail2.put("fontWeight", "bolder");

        ArrayData data2 = new ArrayData();
        starng.put("data", data2);
        mMainActivity.homeselect = new ObjectData();
        data2.add(mMainActivity.homeselect);
        mMainActivity.homeselect.put("value", DkPadApplication.mApp.testManager.test.currentLeavel);
        mMainActivity.homeselect.put("name", "档");

        mMainActivity.speedvalue.setText("0"+"km/h");
        mMainActivity.hertvalue.setText("0"+"bpm");
        mMainActivity.selectvalue.setText(String.valueOf(DkPadApplication.mApp.testManager.test.currentLeavel)+"档");

        series.add(speed);
        series.add(hert);
        series.add(starng);
    }

    public void updata(String[] data) {

        mMainActivity.homespeed.put("value", data[0]);
        mMainActivity.homehert.put("value", data[1]);
        mMainActivity.homeselect.put("value", data[2]);
        mMainActivity.speedvalue.setText(data[0]+"km/h");
        mMainActivity.hertvalue.setText(data[1]+"bpm");
        mMainActivity.selectvalue.setText(data[2]+"%");

        mMainActivity.sportspeed.add(Double.valueOf(data[0]));
        mMainActivity.sporthert.add(Double.valueOf(data[1]));
        mMainActivity.sportselect.add(Double.valueOf(data[2]));
        mMainActivity.sportx.add(DkPadApplication.mApp.testManager.test.praseCurrentTime());


        DkPadApplication.mApp.sportData.updataSecond(data);
        if(mMainActivity.detialSportFragment != null)
        mMainActivity.detialSportFragment.initView();
        if(mMainActivity.homeFragment != null)
            mMainActivity.homeFragment.initView();
        updataSport();
        if(mMainActivity.personFragment != null)
            mMainActivity.personFragment.initView();
    }

    public void setLeave(int leave)
    {
        mMainActivity.homeselect.put("value", Double.valueOf(leave));
        mMainActivity.selectvalue.setText(String.valueOf(leave)+"%");
        mMainActivity.sportselect.add(Double.valueOf(leave));
        if(mMainActivity.homeFragment != null)
            mMainActivity.homeFragment.initView();
        if(mMainActivity.detialSportFragment != null)
            mMainActivity.detialSportFragment.initView();
    }

    public void doFinish(String[] data)
    {
        mMainActivity.btnStart.setImageResource(R.drawable.start);
        mMainActivity.btnStart2.setImageResource(R.drawable.start);
        DkPadApplication.mApp.testManager.test.isstart = false;
        DkPadApplication.mApp.testManager.stopTest();
        if(DkPadApplication.mApp.testManager.test.isstart)
        {
            mMainActivity.headlayer.setVisibility(View.INVISIBLE);
            mMainActivity.oplayer.setVisibility(View.INVISIBLE);
            mMainActivity.setting.setVisibility(View.INVISIBLE);
        }
        else
        {
            mMainActivity.headlayer.setVisibility(View.VISIBLE);
            mMainActivity.oplayer.setVisibility(View.VISIBLE);
            mMainActivity.setting.setVisibility(View.VISIBLE);
        }

        mMainActivity.homespeed.put("value", "0");
        mMainActivity.homehert.put("value", data[1]);
        mMainActivity.homeselect.put("value", data[2]);
        mMainActivity.speedvalue.setText(data[0]+"km/h");
        mMainActivity.hertvalue.setText(data[1]+"bpm");
        mMainActivity.selectvalue.setText(data[2]+"%");

        mMainActivity.sportspeed.add(0.0);
        mMainActivity.sporthert.add(Double.valueOf(data[1]));
        mMainActivity.sportselect.add(Double.valueOf(data[2]));
        mMainActivity.sportx.add(DkPadApplication.mApp.testManager.test.praseCurrentTime());

        DkPadApplication.mApp.sportData.setfinish(data);
        if(mMainActivity.detialSportFragment != null)
            mMainActivity.detialSportFragment.initView();
        if(mMainActivity.homeFragment != null)
            mMainActivity.homeFragment.initView();
        updataSport();
        if(mMainActivity.personFragment != null)
            mMainActivity.personFragment.initView();
        DkPadApplication.mApp.testManager.setNewTest(DkPadApplication.mApp.selectOptation,DkPadApplication.mApp.selectUser);

    }

    public void doError(String[] data)
    {
        DkPadApplication.mApp.testManager.test.isstart = false;
        mMainActivity.btnStart.setImageResource(R.drawable.start);
        mMainActivity.btnStart2.setImageResource(R.drawable.start);
        DkPadApplication.mApp.testManager.stopTest();
        if(DkPadApplication.mApp.testManager.test.isstart)
        {
            mMainActivity.headlayer.setVisibility(View.INVISIBLE);
            mMainActivity.oplayer.setVisibility(View.INVISIBLE);
            mMainActivity.setting.setVisibility(View.INVISIBLE);
        }
        else
        {
            mMainActivity.headlayer.setVisibility(View.VISIBLE);
            mMainActivity.oplayer.setVisibility(View.VISIBLE);
            mMainActivity.setting.setVisibility(View.VISIBLE);
        }

        mMainActivity.homespeed.put("value", "0");
        mMainActivity.homehert.put("value", data[1]);
        mMainActivity.homeselect.put("value", data[2]);
        mMainActivity.speedvalue.setText(data[0]+"km/h");
        mMainActivity.hertvalue.setText(data[1]+"bpm");
        mMainActivity.selectvalue.setText(data[2]+"%");

        mMainActivity.sportspeed.add(0.0);
        mMainActivity.sporthert.add(Double.valueOf(data[1]));
        mMainActivity.sportselect.add(Double.valueOf(data[2]));
        mMainActivity.sportx.add(DkPadApplication.mApp.testManager.test.praseCurrentTime());

        DkPadApplication.mApp.sportData.updataSecond(data);
        if(mMainActivity.detialSportFragment != null)
            mMainActivity.detialSportFragment.initView();
        if(mMainActivity.homeFragment != null)
            mMainActivity.homeFragment.initView();
        updataSport();
        if(mMainActivity.personFragment != null)
            mMainActivity.personFragment.initView();

        AppUtils.showMessage(mMainActivity,mMainActivity.getString(R.string.setting_deive_error));
        //DkPadApplication.mApp.testManager.setNewTest(DkPadApplication.mApp.selectOptation,DkPadApplication.mApp.selectUser);

    }

    public void initOptionPerson() {
        mMainActivity.personoption = new ObjectData();
        mMainActivity.personTitle = new ObjectData();
        mMainActivity.personTitle.put("text", mMainActivity.getString(R.string.chart_person_title));
        mMainActivity.personoption.put("title", mMainActivity.personTitle);

        ObjectData tooltip = new ObjectData();
        mMainActivity.personoption.put("tooltip", tooltip);
        tooltip.put("formatter", "{a} <br/>{c} {b}");
        mMainActivity.personoption.put("backgroundColor", "rgba(0, 0, 0, 0)");
        ObjectData radar = new ObjectData();
        mMainActivity.personoption.put("radar", radar);
        ObjectData name = new ObjectData();
        radar.put("name", name);
        ObjectData textStyle = new ObjectData();
        name.put("textStyle", textStyle);
        textStyle.put("color", "#fff");
        textStyle.put("backgroundColor", "#999");
        textStyle.put("borderRadius", "3");
        ArrayData padding = new ArrayData();
        padding.add(3);
        padding.add(5);
        textStyle.put("padding", padding);

        ObjectData legend = new ObjectData();
        mMainActivity.personoption.put("legend", legend);
        mMainActivity.personopLegend = new ArrayData();
        legend.put("data", mMainActivity.personopLegend);
        if (DkPadApplication.mApp.selectUser != null) {
            mMainActivity.personopLegend.add(DkPadApplication.mApp.selectUser.name);
        }

        mMainActivity.indicator = new ArrayData();
        radar.put("indicator", mMainActivity.indicator);
        ObjectData jo = new ObjectData();
        mMainActivity.indicator.add(jo);
        jo.put("name", mMainActivity.getString(R.string.chart_sport_time_day));
        jo.put("max", 120);
        ObjectData jo1 = new ObjectData();
        mMainActivity.indicator.add(jo1);
        jo1.put("name", mMainActivity.getString(R.string.chart_sport_distence));
        jo1.put("max", 50);
        ObjectData jo2 = new ObjectData();
        mMainActivity.indicator.add(jo2);
        jo2.put("name", mMainActivity.getString(R.string.chart_avarage_set));
        jo2.put("max", "100");
        ObjectData jo3 = new ObjectData();
        mMainActivity.indicator.add(jo3);
        jo3.put("name", mMainActivity.getString(R.string.chart_finish_rate));
        jo3.put("max", 100);
        ObjectData jo4 = new ObjectData();
        mMainActivity.indicator.add(jo4);
        jo4.put("name", mMainActivity.getString(R.string.chart_top_speed));
        jo4.put("max", 50);
        ObjectData jo5 = new ObjectData();
        mMainActivity.indicator.add(jo5);
        jo5.put("name", mMainActivity.getString(R.string.chart_average_speed));
        jo5.put("max", 50);

        mMainActivity.series = new ArrayData();
        mMainActivity.personoption.put("series", mMainActivity.series);
        mMainActivity.seriesItem = new ObjectData();
        mMainActivity.series.add(mMainActivity.seriesItem);
        mMainActivity.seriesItem.put("name", DkPadApplication.mApp.selectUser.name);
        mMainActivity.seriesItem.put("type", "radar");
        mMainActivity.seriesData = new ArrayData();
        mMainActivity.seriesItem.put("data", mMainActivity.seriesData);
        mMainActivity.myvalue = new ObjectData();
        mMainActivity.seriesData.add(mMainActivity.myvalue);
        ArrayData value = new ArrayData();
        mMainActivity.myvalue.put("value", value);

        int a = DkPadApplication.mApp.sportData.getdayTime();
        double b = DkPadApplication.mApp.sportData.getDayDistence();
        int c = DkPadApplication.mApp.sportData.getaverLeavel();
        double d = DkPadApplication.mApp.sportData.getrat();
        double e = DkPadApplication.mApp.sportData.gettopspeed();
        double f = DkPadApplication.mApp.sportData.getaspeed();

        value.add(DkPadApplication.mApp.sportData.getdayTime());
        value.add(DkPadApplication.mApp.sportData.getDayDistence());
        value.add(DkPadApplication.mApp.sportData.getaverLeavel());
        value.add(DkPadApplication.mApp.sportData.getrat());
        value.add(DkPadApplication.mApp.sportData.gettopspeed());
        value.add(DkPadApplication.mApp.sportData.getaspeed());
        mMainActivity.myvalue.put("name", DkPadApplication.mApp.selectUser.name);
    }


    public void hidOper() {
        mMainActivity.btnStart.setVisibility(View.INVISIBLE);
        mMainActivity.btnAdd.setVisibility(View.INVISIBLE);
        mMainActivity.btnDes.setVisibility(View.INVISIBLE);
        mMainActivity.btnMap.setVisibility(View.INVISIBLE);
        mMainActivity.btnWatch.setVisibility(View.INVISIBLE);
        mMainActivity.btntv.setVisibility(View.INVISIBLE);
        mMainActivity.headlayer.setVisibility(View.INVISIBLE);
        mMainActivity.oplayer.setVisibility(View.INVISIBLE);
        if(DkPadApplication.mApp.testManager.test.isstart)
        {
            mMainActivity.setting.setVisibility(View.INVISIBLE);
        }
        else
        {
            mMainActivity.setting.setVisibility(View.VISIBLE);
        }

    }

    public void showOper() {
        mMainActivity.btnStart.setVisibility(View.VISIBLE);
        mMainActivity.btnAdd.setVisibility(View.VISIBLE);
        mMainActivity.btnDes.setVisibility(View.VISIBLE);
        mMainActivity.btnMap.setVisibility(View.VISIBLE);
        mMainActivity.btnWatch.setVisibility(View.VISIBLE);
        mMainActivity.btntv.setVisibility(View.VISIBLE);
        if(DkPadApplication.mApp.testManager.test.isstart)
        {
            mMainActivity.headlayer.setVisibility(View.INVISIBLE);
            mMainActivity.oplayer.setVisibility(View.INVISIBLE);
            mMainActivity.setting.setVisibility(View.INVISIBLE);
        }
        else
        {
            mMainActivity.headlayer.setVisibility(View.VISIBLE);
            mMainActivity.oplayer.setVisibility(View.VISIBLE);
            mMainActivity.setting.setVisibility(View.VISIBLE);
        }
    }

    public ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if(position == 0)
            {
                hidOper();
            }
            else
            {
                showOper();
            }
            if(position == 0)
            {
                mMainActivity.btnleft.setVisibility(View.INVISIBLE);
                mMainActivity.btnright.setVisibility(View.VISIBLE);
            }
            else if(position == 1)
            {
                mMainActivity.btnleft.setVisibility(View.VISIBLE);
                mMainActivity.btnright.setVisibility(View.VISIBLE);
            }
            else
            {
                mMainActivity.btnleft.setVisibility(View.VISIBLE);
                mMainActivity.btnright.setVisibility(View.INVISIBLE);

            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };


    public View.OnClickListener leftListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mMainActivity.viewPager.setCurrentItem(mMainActivity.viewPager.getCurrentItem()-1);
        }
    };


    public View.OnClickListener rightListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mMainActivity.viewPager.setCurrentItem(mMainActivity.viewPager.getCurrentItem()+1);
        }
    };


}
