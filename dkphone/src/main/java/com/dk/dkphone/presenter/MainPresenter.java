package com.dk.dkphone.presenter;


import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.dk.dkphone.R;
import com.dk.dkphone.TestManager;
import com.dk.dkphone.database.DBHelper;
import com.dk.dkphone.entity.EquipData;
import com.dk.dkphone.entity.User;
import com.dk.dkphone.entity.UserDefine;
import com.dk.dkphone.handler.MainHandler;
import com.dk.dkphone.receiver.MainReceiver;
import com.dk.dkphone.view.DkPhoneApplication;
import com.dk.dkphone.view.QueryView;
import com.dk.dkphone.view.QueryView2;
import com.dk.dkphone.view.activity.MainActivity;
import com.dk.dkphone.view.activity.SettingActivity;
import com.dk.dkphone.view.activity.UserActivity;
import com.dk.dkphone.view.adapter.PagerMainAdapter;
import com.dk.dkphone.view.adapter.PkUserAdapter;
import com.dk.dkphone.view.adapter.UserAdapter;
import com.dk.dkphone.view.fragment.DetialSportFragment;
import com.dk.dkphone.view.fragment.HomeFragment;
import com.dk.dkphone.view.fragment.PersonFragment;

import org.json.JSONException;
import org.json.JSONObject;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.apputils.TimeUtils;
import intersky.echartoption.ArrayData;
import intersky.echartoption.ObjectData;

public class MainPresenter implements Presenter {


    public MainActivity mMainActivity;
    public MainHandler mainHandler;
    public UserAdapter userAdapter;
    public PkUserAdapter pkUserAdapter;
//    public OptationAdapter optationAdapter;
    public boolean showhead = false;
    public boolean showoptation = false;
    public boolean tvshow = false;
    public QueryView queryView;
    public QueryView2 queryView2;
    private AudioManager mAudioManager;
    public PopupWindow popupWindow;
    public int currentVolume;
    public int nat;

    public MainPresenter(MainActivity mMainActivity) {
        this.mMainActivity = mMainActivity;
        mainHandler = new MainHandler(mMainActivity);
        mMainActivity.setBaseReceiver(new MainReceiver(mainHandler));
    }

    @Override
    public void initView() {
        mMainActivity.flagFillBack = false;
        mMainActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        mMainActivity.setContentView(R.layout.activity_main);
        if (DkPhoneApplication.mApp.mUpDataManager != null)
            DkPhoneApplication.mApp.mUpDataManager.checkVersin();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mAudioManager = (AudioManager) mMainActivity.getSystemService(Context.AUDIO_SERVICE);
        }
        currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mMainActivity.power = mMainActivity.findViewById(R.id.power);
        mMainActivity.power.setMax(100);
        TextView mode = mMainActivity.findViewById(R.id.mode);
        mode.setText(String.valueOf(DkPhoneApplication.mApp.testManager.xpxUsbManager.SPEED_MODE));
        TextView dataform = mMainActivity.findViewById(R.id.dataform);
        queryView2 = new QueryView2(mMainActivity);
        dataform.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                queryView2.creatView(mMainActivity.findViewById(R.id.activity_main));
            }
        });

        mMainActivity.power.setOnSeekBarChangeListener(onSeekBarChangeListener);
        mMainActivity.timevalue1 = mMainActivity.findViewById(R.id.timevalue1);
        mMainActivity.timevalue2 = mMainActivity.findViewById(R.id.timevalue2);
        mMainActivity.timevalue2.setVisibility(View.INVISIBLE);
        mMainActivity.speedvalue = mMainActivity.findViewById(R.id.speedvalue);
        mMainActivity.hertvalue = mMainActivity.findViewById(R.id.hertvalue);
        mMainActivity.selectvalue = mMainActivity.findViewById(R.id.selectvalue);
        mMainActivity.btnStart2 = mMainActivity.findViewById(R.id.start2);
        mMainActivity.btnAdd2 = mMainActivity.findViewById(R.id.add2);
        mMainActivity.btnDes2 = mMainActivity.findViewById(R.id.des2);
        mMainActivity.btntv2 = mMainActivity.findViewById(R.id.tv2);
        mMainActivity.btnlanya = mMainActivity.findViewById(R.id.bluetooth);
        mMainActivity.showlayer = mMainActivity.findViewById(R.id.seriesdata);
        mMainActivity.stateTitle = mMainActivity.findViewById(R.id.deviecstatetiel);
        mMainActivity.state = mMainActivity.findViewById(R.id.deviecstatevalue);
        mMainActivity.sp = mMainActivity.findViewById(R.id.devicesp);
        mMainActivity.rpmTitle = mMainActivity.findViewById(R.id.devicerpmtitle);
        mMainActivity.rpm = mMainActivity.findViewById(R.id.devicerpm);
        mMainActivity.workTitle = mMainActivity.findViewById(R.id.deviceworktitle);
        mMainActivity.work = mMainActivity.findViewById(R.id.devicework);
        mMainActivity.showlayer.setVisibility(View.INVISIBLE);
        initOptionSport();
        initOptionHome();
        initOptionPerson();
        queryView = new QueryView(mMainActivity, onItemClickListener);
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
        mMainActivity.viewPager.setNoScroll(true);
        mMainActivity.btnleft = mMainActivity.findViewById(R.id.left);
        mMainActivity.btnright = mMainActivity.findViewById(R.id.right);
        mMainActivity.personFragment = new PersonFragment();
        mMainActivity.homeFragment = new HomeFragment();
        mMainActivity.detialSportFragment = new DetialSportFragment();
//        mMainActivity.headlayer = mMainActivity.findViewById(R.id.head);
        mMainActivity.oplayer = mMainActivity.findViewById(R.id.optation);
//        mMainActivity.head = mMainActivity.findViewById(R.id.headselect);
//        mMainActivity.head.setOnClickListener(headListener);
        mMainActivity.optation = mMainActivity.findViewById(R.id.optationselect);
//        mMainActivity.optation.setOnClickListener(optationListener);
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
//        mMainActivity.sHeadName = mMainActivity.findViewById(R.id.headname);
        mMainActivity.sOptation = mMainActivity.findViewById(R.id.optationicon);
        mMainActivity.sOptationName = mMainActivity.findViewById(R.id.optationname);
//        mMainActivity.userList = mMainActivity.findViewById(R.id.headlist);
//        mMainActivity.headarray = mMainActivity.findViewById(R.id.headarray);
        mMainActivity.optationArray = mMainActivity.findViewById(R.id.optationarray);
        mMainActivity.time = mMainActivity.findViewById(R.id.time);
//        mMainActivity.btnShowHead = mMainActivity.findViewById(R.id.btnheadshow);
        mMainActivity.btnShowOptation = mMainActivity.findViewById(R.id.btnoptationshow);
        mMainActivity.btnStart.setOnClickListener(startListener);
        mMainActivity.btnAdd.setOnClickListener(addListener);
        mMainActivity.btnDes.setOnClickListener(desListener);
        mMainActivity.btnStart2.setOnClickListener(startListener);
        mMainActivity.btnAdd2.setOnClickListener(addListener);
        mMainActivity.btnDes2.setOnClickListener(desListener);
        //mMainActivity.btnWatch.setOnClickListener(watchListener);
//        mMainActivity.btnShowHead.setOnClickListener(showHeadListener);
        mMainActivity.sHead.setOnClickListener(headListener);
//        mMainActivity.btnShowOptation.setOnClickListener(showOptationListener);
        mMainActivity.btnleft.setOnClickListener(leftListener);
        mMainActivity.btnright.setOnClickListener(rightListener);
        mMainActivity.btntv.setOnClickListener(showTvListener);
        mMainActivity.btntv2.setOnClickListener(showTvListener);
        mMainActivity.btnlanya.setOnClickListener(deviceListener);
        userAdapter = new UserAdapter(mMainActivity, DkPhoneApplication.mApp.users);
        pkUserAdapter = new PkUserAdapter(mMainActivity, DkPhoneApplication.mApp.users);
//        optationAdapter = new OptationAdapter(mMainActivity, DkPadApplication.mApp.optations);
        DkPhoneApplication.mApp.testManager.sendData.add(sendData);
        updataTime();
        permission();
        mMainActivity.viewPager.setAdapter(mMainActivity.pagerMainAdapter);
        mMainActivity.viewPager.setCurrentItem(1);
        mMainActivity.viewPager.addOnPageChangeListener(onPageChangeListener);
        setUserView(DkPhoneApplication.mApp.selectUser);
//        mMainActivity.sOptationName.setText(DkPadApplication.mApp.selectOptation.name);
        if (TestManager.testManager.checkDeviceConnect() == true) {
            mMainActivity.btnlanya.setImageResource(R.drawable.lanyas);
        } else {
            mMainActivity.btnlanya.setImageResource(R.drawable.lanya);
        }
        if(TestManager.TEST_MODE == false)
        mainHandler.sendEmptyMessageDelayed(MainHandler.AUTO_CONNECT, 500);
    }

    public void checkBlue() {

        if (TestManager.testManager.checkDeviceConnect() == false) {
            if (DkPhoneApplication.mApp.testManager.bluetoothSetManager.getLast().length() > 0) {
                DkPhoneApplication.mApp.testManager.connectDevice(mMainActivity, mMainActivity.findViewById(R.id.activity_main), DkPhoneApplication.mApp.testManager.deviceMac);
            } else {
                DkPhoneApplication.mApp.testManager.bluetoothSetManager.scanLeDevice();
            }
        } else {
            DkPhoneApplication.mApp.testManager.bluetoothSetManager.scanLeDevice();
        }
    }

    public SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
                DkPhoneApplication.mApp.testManager.test.currentLeavel = seekBar.getProgress();
                DkPhoneApplication.mApp.testManager.setLeavestete(DkPhoneApplication.mApp.testManager.test.currentLeavel);
                setLeave(DkPhoneApplication.mApp.testManager.test.currentLeavel);
        }
    };

    public void updateBluestate(Intent intent) {
        if(TestManager.TEST_MODE == false)
        {
            if (intent.getBooleanExtra("blue", false) == true) {
                mMainActivity.btnlanya.setImageResource(R.drawable.lanyas);
                mMainActivity.homeFragment.web.setVisibility(View.VISIBLE);
                mMainActivity.homeFragment.scan.setVisibility(View.INVISIBLE);


            } else {
                mMainActivity.btnlanya.setImageResource(R.drawable.lanya);
                mMainActivity.homeFragment.web.setVisibility(View.INVISIBLE);
                mMainActivity.homeFragment.scan.setVisibility(View.VISIBLE);
            }
        }
    }

    public void scanFinish(Intent intent) {
        Bundle bundle = intent.getExtras();
        String json = bundle.getString("result");
        try {
            JSONObject jsonObject = new JSONObject(json);
            String address = jsonObject.getString("deviceAddress");
            DkPhoneApplication.mApp.testManager.connectDevice(
                    mMainActivity, mMainActivity.findViewById(R.id.activity_main), address);


        } catch (JSONException e) {
            e.printStackTrace();
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
        if (mMainActivity.tv != null)
            mMainActivity.tv.destroy();
    }


    public AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            BluetoothDevice bluetoothDevice = DkPhoneApplication.mApp.testManager.bluetoothSetManager.
                    deviceslist.get(position);
            if (DkPhoneApplication.mApp.testManager.checkDeviceConnect() == false) {
                DkPhoneApplication.mApp.testManager.connectDevice(mMainActivity,
                        mMainActivity.findViewById(R.id.activity_main), bluetoothDevice.getAddress());
            } else {
                DkPhoneApplication.mApp.testManager.bluetoothSetManager.connectStop(bluetoothDevice.getAddress());
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
            if (nat == 0) {
                mMainActivity.first = true;
                mMainActivity.tv.loadUrl("https://search.youku.com/search_video?keyword=%E5%81%A5%E8%BA%AB");

            } else if (nat == 1) {
                mMainActivity.first = true;
                mMainActivity.tv.loadUrl("https://m.v.qq.com/search.html?act=102&keyWord=%E5%81%A5%E8%BA%AB");
            } else {
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
                if (!Settings.canDrawOverlays(mMainActivity)) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + mMainActivity.getPackageName()));
                    intent.putExtra("extra_prefs_show_button_bar", true);//是否显示button bar
                    intent.putExtra("extra_prefs_set_next_text", "完成");
                    intent.putExtra("extra_prefs_set_back_text", "返回");
                    mMainActivity.startActivity(intent);

                } else if (!Settings.System.canWrite(mMainActivity)) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                    intent.putExtra("extra_prefs_show_button_bar", true);//是否显示button bar
                    intent.putExtra("extra_prefs_set_next_text", "完成");
                    intent.putExtra("extra_prefs_set_back_text", "返回");
                    mMainActivity.startActivity(intent);
                } else {
                    Intent intent = new Intent(mMainActivity, SettingActivity.class);
                    mMainActivity.startActivity(intent);
                }
            } else {
                Intent intent = new Intent(mMainActivity, SettingActivity.class);
                mMainActivity.startActivity(intent);
            }
        }
    };


    public View.OnClickListener startListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(TestManager.TEST_MODE == false)
            {
                if (DkPhoneApplication.mApp.testManager.xpxUsbManager.deviceerror == true) {
                    AppUtils.showMessage(mMainActivity, mMainActivity.getString(R.string.setting_deive_error));
                    return;
                }
                if (DkPhoneApplication.mApp.testManager.xpxUsbManager.connect) {
                    if (DkPhoneApplication.mApp.testManager.test.isstart == false) {
                        DkPhoneApplication.mApp.testManager.test.isstart = true;
                        DkPhoneApplication.mApp.appSetProtectState(true);
                        mMainActivity.btnStart.setImageResource(R.drawable.stop);
                        mMainActivity.btnStart2.setImageResource(R.drawable.stop);
                        DkPhoneApplication.mApp.testManager.startTest();

                        DkPhoneApplication.mApp.sportData.addNew(DkPhoneApplication.mApp.testManager.test);
                        SharedPreferences.Editor editor = DkPhoneApplication.mApp.sharedPre.edit();
                        editor.putString(UserDefine.SETTING_LAST_TEST, DkPhoneApplication.mApp.testManager.test.rid);
                        editor.commit();
                    } else {
                        DkPhoneApplication.mApp.testManager.test.isstart = false;
                        DkPhoneApplication.mApp.appSetProtectTime(DkPhoneApplication.mApp.maxProtectSecond);
                        DkPhoneApplication.mApp.appSetProtectState(false);
                        mMainActivity.btnStart.setImageResource(R.drawable.start);
                        mMainActivity.btnStart2.setImageResource(R.drawable.start);
                        DkPhoneApplication.mApp.testManager.stopTest();
                    }
                } else {
                    if (DkPhoneApplication.mApp.testManager.checkDeviceConnect()) {
                        if (DkPhoneApplication.mApp.testManager.test.isstart == false) {
                            DkPhoneApplication.mApp.testManager.test.isstart = true;
                            DkPhoneApplication.mApp.appSetProtectState(true);
                            mMainActivity.btnStart.setImageResource(R.drawable.stop);
                            mMainActivity.btnStart2.setImageResource(R.drawable.stop);
                            DkPhoneApplication.mApp.testManager.startTest();

                            DkPhoneApplication.mApp.sportData.addNew(DkPhoneApplication.mApp.testManager.test);
                            SharedPreferences.Editor editor = DkPhoneApplication.mApp.sharedPre.edit();
                            editor.putString(UserDefine.SETTING_LAST_TEST, DkPhoneApplication.mApp.testManager.test.rid);
                            editor.commit();
                        } else {
                            DkPhoneApplication.mApp.testManager.test.isstart = false;
                            DkPhoneApplication.mApp.appSetProtectTime(DkPhoneApplication.mApp.maxProtectSecond);
                            DkPhoneApplication.mApp.appSetProtectState(false);
                            mMainActivity.btnStart.setImageResource(R.drawable.start);
                            mMainActivity.btnStart2.setImageResource(R.drawable.start);
                            DkPhoneApplication.mApp.testManager.stopTest();
                        }
                    } else {
                        AppUtils.showMessage(mMainActivity, mMainActivity.getString(R.string.connect_device_null));
                    }
                }
            }
            else
            {
                if (DkPhoneApplication.mApp.testManager.test.isstart == false) {
                    DkPhoneApplication.mApp.testManager.test.isstart = true;
                    DkPhoneApplication.mApp.appSetProtectState(true);
                    mMainActivity.btnStart.setImageResource(R.drawable.stop);
                    mMainActivity.btnStart2.setImageResource(R.drawable.stop);
                    DkPhoneApplication.mApp.testManager.startTest();

                    DkPhoneApplication.mApp.sportData.addNew(DkPhoneApplication.mApp.testManager.test);
                    SharedPreferences.Editor editor = DkPhoneApplication.mApp.sharedPre.edit();
                    editor.putString(UserDefine.SETTING_LAST_TEST, DkPhoneApplication.mApp.testManager.test.rid);
                    editor.commit();
                } else {
                    DkPhoneApplication.mApp.testManager.test.isstart = false;
                    DkPhoneApplication.mApp.appSetProtectTime(DkPhoneApplication.mApp.maxProtectSecond);
                    DkPhoneApplication.mApp.appSetProtectState(false);
                    mMainActivity.btnStart.setImageResource(R.drawable.start);
                    mMainActivity.btnStart2.setImageResource(R.drawable.start);
                    DkPhoneApplication.mApp.testManager.stopTest();
                }
            }


            if (DkPhoneApplication.mApp.testManager.test.isstart) {
//                mMainActivity.headlayer.setVisibility(View.INVISIBLE);
                //mMainActivity.oplayer.setVisibility(View.INVISIBLE);
                mMainActivity.setting.setVisibility(View.INVISIBLE);
            } else {
//                mMainActivity.headlayer.setVisibility(View.VISIBLE);
                //mMainActivity.oplayer.setVisibility(View.VISIBLE);
                mMainActivity.setting.setVisibility(View.VISIBLE);
            }

        }
    };


    public View.OnClickListener addListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                DkPhoneApplication.mApp.testManager.addLeave();
                setLeave(DkPhoneApplication.mApp.testManager.test.currentLeavel);
        }
    };


    public View.OnClickListener desListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                DkPhoneApplication.mApp.testManager.desLeave();
                setLeave(DkPhoneApplication.mApp.testManager.test.currentLeavel);
        }
    };

    public View.OnClickListener deviceListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (DkPhoneApplication.mApp.testManager.test.isstart) {
                AppUtils.showMessage(mMainActivity, "请先暂停");
            } else {

                if (DkPhoneApplication.mApp.testManager.checkDeviceConnect()) {
                    if (DkPhoneApplication.mApp.testManager.type == 1) {
                        DkPhoneApplication.mApp.testManager.stopTest();
                    }
                    DkPhoneApplication.mApp.testManager.stopConnect();
                    DkPhoneApplication.mApp.testManager.bluetoothSetManager.cleanLast();
                } else {
                    queryView.creatView(mMainActivity.findViewById(R.id.activity_main));
                }
            }
        }
    };

    public View.OnClickListener headListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mMainActivity, UserActivity.class);
            intent.putExtra("user", DkPhoneApplication.mApp.selectUser);
            mMainActivity.startActivity(intent);
        }
    };

//    public View.OnClickListener optationListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Intent intent = new Intent(mMainActivity, OptationActivity.class);
//            intent.putExtra("optation", DkPadApplication.mApp.selectOptation);
//            mMainActivity.startActivity(intent);
//        }
//    };

    public View.OnClickListener showUserListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    public View.OnClickListener showHeadListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            if (showhead) {
//                showhead = false;
//                mMainActivity.headarray.setImageResource(R.drawable.downarray);
//                mMainActivity.userList.setVisibility(View.GONE);
//            } else {
//                showhead = true;
//                mMainActivity.headarray.setImageResource(R.drawable.uparray);
//                mMainActivity.userList.setVisibility(View.VISIBLE);
//                mMainActivity.userList.setAdapter(userAdapter);
//                mMainActivity.userList.setOnItemClickListener(userItemListener);
//            }

        }
    };

//    public View.OnClickListener showOptationListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            if (showoptation) {
//                showoptation = false;
//                mMainActivity.optationArray.setImageResource(R.drawable.downarray);
//                mMainActivity.optationList.setVisibility(View.GONE);
//            } else {
//                showoptation = true;
//                mMainActivity.optationArray.setImageResource(R.drawable.uparray);
//                mMainActivity.optationList.setVisibility(View.VISIBLE);
//                mMainActivity.optationList.setAdapter(optationAdapter);
//                mMainActivity.optationList.setOnItemClickListener(optationItemListener);
//            }
//        }
//    };

//    public AdapterView.OnItemClickListener optationItemListener = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//            OptationAdapter optationAdapter = (OptationAdapter) adapterView.getAdapter();
//            Optation optation = optationAdapter.getItem(i);
//            if (optation.name.length() == 0) {
//                Optation optation1 = new Optation();
//                Intent intent = new Intent(mMainActivity, OptationActivity.class);
//                intent.putExtra("optation", optation1);
//                mMainActivity.startActivity(intent);
//            } else {
//                if (DkPadApplication.mApp.testManager.test.finish == false && DkPadApplication.mApp.testManager.test.current > 0) {
//                    popupWindow = AppUtils.creatXpxDialog(mMainActivity, null
//                            , mMainActivity.getString(R.string.op_delete_title),
//                            mMainActivity.getString(R.string.op_delete_title_msg), mMainActivity.getString(R.string.button_word_ok)
//                            , mMainActivity.getString(R.string.button_word_cancle), forgaveListener, mMainActivity.findViewById(R.id.activity_main), optation);
//                } else {
//                    DkPadApplication.mApp.optations.add(0, DkPadApplication.mApp.selectOptation);
//                    DkPadApplication.mApp.optations.remove(optation);
//                    DkPadApplication.mApp.selectOptation = optation;
//                    mMainActivity.sOptationName.setText(optation.name);
//                    optationAdapter.notifyDataSetChanged();
//                    DkPadApplication.mApp.setLastOptation(DkPadApplication.mApp.selectOptation);
//                    DkPadApplication.mApp.testManager.setNewTest(DkPadApplication.mApp.selectOptation, DkPadApplication.mApp.selectUser);
//                    mMainActivity.speedvalue.setText("0" + "km/h");
//                    mMainActivity.hertvalue.setText("0" + "bpm");
//                    mMainActivity.homeFragment.initView();
//                }
//
//            }
//        }
//    };

/*    public View.OnClickListener forgaveListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Optation optation = (Optation) view.getTag();
            DkPadApplication.mApp.optations.add(0, DkPadApplication.mApp.selectOptation);
            DkPadApplication.mApp.optations.remove(optation);
            DkPadApplication.mApp.selectOptation = optation;
            mMainActivity.sOptationName.setText(optation.name);
            optationAdapter.notifyDataSetChanged();
            DkPadApplication.mApp.setLastOptation(DkPadApplication.mApp.selectOptation);
            DkPadApplication.mApp.testManager.setNewTest(DkPadApplication.mApp.selectOptation, DkPadApplication.mApp.selectUser);
            DkPadApplication.mApp.sportData.last = DkPadApplication.mApp.testManager.test;
            SharedPreferences.Editor editor = DkPadApplication.mApp.sharedPre.edit();
            editor.putString(UserDefine.SETTING_LAST_TEST, DkPadApplication.mApp.testManager.test.rid);
            editor.commit();
            mMainActivity.homespeed.put("value", 0);
            mMainActivity.homehert.put("value", 0);
            mMainActivity.homeselect.put("value", DkPadApplication.mApp.testManager.test.currentLeavel);
            mMainActivity.power.setProgress(DkPadApplication.mApp.testManager.test.currentLeavel);
            mMainActivity.speedvalue.setText("0" + "km/h");
            mMainActivity.hertvalue.setText("0" + "bpm");
            mMainActivity.selectvalue.setText(String.valueOf(DkPadApplication.mApp.testManager.test.currentLeavel) + "%");

            mMainActivity.homeFragment.initView();
            if (popupWindow != null)
                popupWindow.dismiss();
        }
    };*/

//    public AdapterView.OnItemClickListener userItemListener = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//            UserAdapter userAdapter = (UserAdapter) adapterView.getAdapter();
//            User user = userAdapter.getItem(i);
//            if (user.name.length() == 0) {
//                User user1 = new User();
//                Intent intent = new Intent(mMainActivity, UserActivity.class);
//                intent.putExtra("user", user1);
//                mMainActivity.startActivity(intent);
//            } else {
//
//                if (DkPadApplication.mApp.testManager.test.finish == false && DkPadApplication.mApp.testManager.test.current > 0) {
//                    AppUtils.creatXpxDialog(mMainActivity, null
//                            , mMainActivity.getString(R.string.op_delete_title),
//                            mMainActivity.getString(R.string.op_delete_title_msg), mMainActivity.getString(R.string.button_word_ok)
//                            , mMainActivity.getString(R.string.button_word_cancle), forgaveListener2, mMainActivity.findViewById(R.id.activity_main), user);
//                } else {
//                    DkPadApplication.mApp.users.add(0, DkPadApplication.mApp.selectUser);
//                    DkPadApplication.mApp.users.remove(user);
//                    DkPadApplication.mApp.selectUser = user;
//                    setUserView(user);
//                    userAdapter.notifyDataSetChanged();
//                    pkUserAdapter.notifyDataSetChanged();
//                    DkPadApplication.mApp.setLastUser(DkPadApplication.mApp.selectUser);
//                    DkPadApplication.mApp.testManager.setNewTest(DkPadApplication.mApp.selectOptation, DkPadApplication.mApp.selectUser);
//                    mMainActivity.homeFragment.initView();
//                }
//            }
//        }
//    };

//    public View.OnClickListener forgaveListener2 = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            User user = (User) view.getTag();
//            DkPadApplication.mApp.users.add(0, DkPadApplication.mApp.selectUser);
//            DkPadApplication.mApp.users.remove(user);
//            DkPadApplication.mApp.selectUser = user;
//            setUserView(user);
//            userAdapter.notifyDataSetChanged();
//            pkUserAdapter.notifyDataSetChanged();
//            DkPadApplication.mApp.setLastUser(DkPadApplication.mApp.selectUser);
//            DkPadApplication.mApp.testManager.setNewTest(DkPadApplication.mApp.selectOptation, DkPadApplication.mApp.selectUser);
//            DkPadApplication.mApp.sportData.last = DkPadApplication.mApp.testManager.test;
//            SharedPreferences.Editor editor = DkPadApplication.mApp.sharedPre.edit();
//            editor.putString(UserDefine.SETTING_LAST_TEST, DkPadApplication.mApp.testManager.test.rid);
//            editor.commit();
//            mMainActivity.homespeed.put("value", 0);
//            mMainActivity.homehert.put("value", 0);
//            mMainActivity.homeselect.put("value", DkPadApplication.mApp.testManager.test.currentLeavel);
//            mMainActivity.power.setProgress(DkPadApplication.mApp.testManager.test.currentLeavel);
//            mMainActivity.speedvalue.setText("0" + "km/h");
//            mMainActivity.hertvalue.setText("0" + "bpm");
//            mMainActivity.selectvalue.setText(String.valueOf(DkPadApplication.mApp.testManager.test.currentLeavel) + "%");
//            mMainActivity.homeFragment.initView();
//            if (popupWindow != null)
//                popupWindow.dismiss();
//
//        }
//    };


    public void setUserView(User user) {
//        if (user != null) {
//            mMainActivity.sHeadName.setText(user.name);
//            if (user.sex.equals(mMainActivity.getString(R.string.male))) {
//                if (user.headpath.length() > 0) {
//                    RequestOptions options = new RequestOptions()
//                            .placeholder(R.drawable.default_user);
//                    Glide.with(mMainActivity).load(new File(user.headpath)).apply(options).into(new MySimpleTarget(mMainActivity.sHead));
//                } else {
//                    mMainActivity.sHead.setImageResource(R.drawable.default_user);
//                }
//
//            } else {
//                if (user.headpath.length() > 0) {
//                    RequestOptions options = new RequestOptions()
//                            .placeholder(R.drawable.default_wuser);
//                    Glide.with(mMainActivity).load(new File(user.headpath)).apply(options).into(new MySimpleTarget(mMainActivity.sHead));
//                } else {
//                    mMainActivity.sHead.setImageResource(R.drawable.default_wuser);
//                }
//            }
//            if(DkPadApplication.mApp.pk != null)
//            {
//                if(DkPadApplication.mApp.selectUser.equals(DkPadApplication.mApp.pk.uid))
//                {
//                    DkPadApplication.mApp.pk.select = false;
//                    DkPadApplication.mApp.pk = null;
//                }
//            }
//            resetSport();
//            mMainActivity.personFragment.updataSport();
//
//        }
    }

    public void updataSport() {
        //DkPadApplication.mApp.sportData = DBHelper.getInstance(mMainActivity).scanRecords(DkPadApplication.mApp.selectUser.uid);
        ArrayData value = new ArrayData();
        mMainActivity.myvalue.put("value", value);
        int a = DkPhoneApplication.mApp.sportData.getdayTime();
        double b = DkPhoneApplication.mApp.sportData.getDayDistence();
        int c = DkPhoneApplication.mApp.sportData.getaverLeavel();
        double d = DkPhoneApplication.mApp.sportData.getDaycarl();
        double e = DkPhoneApplication.mApp.sportData.gettopspeed();
        double f = DkPhoneApplication.mApp.sportData.getaspeed();

        value.add(DkPhoneApplication.mApp.sportData.getdayTime());
        value.add(DkPhoneApplication.mApp.sportData.getDayDistence());
        value.add(DkPhoneApplication.mApp.sportData.getaverLeavel());
        value.add(DkPhoneApplication.mApp.sportData.getDaycarl());
        value.add(DkPhoneApplication.mApp.sportData.gettopspeed());
        value.add(DkPhoneApplication.mApp.sportData.getaspeed());
        mMainActivity.myvalue.put("name", DkPhoneApplication.mApp.selectUser.name);
        mMainActivity.personopLegend.array.clear();
        mMainActivity.personopLegend.add(DkPhoneApplication.mApp.selectUser.name);
        if (DkPhoneApplication.mApp.pk != null) {
            mMainActivity.personopLegend.add(DkPhoneApplication.mApp.pk.name);
        }
        if (DkPhoneApplication.mApp.pk == null) {
            mMainActivity.seriesItem.put("name", DkPhoneApplication.mApp.selectUser.name);
        } else {
            mMainActivity.seriesItem.put("name", DkPhoneApplication.mApp.selectUser.name + " VS " + DkPhoneApplication.mApp.pk.name);
        }
        if (mMainActivity.personFragment != null) {
            mMainActivity.personFragment.updataSport();
        }
    }

    public void resetSport() {
        DkPhoneApplication.mApp.sportData = DBHelper.getInstance(mMainActivity).scanRecords(DkPhoneApplication.mApp.selectUser.uid);
        ArrayData value = new ArrayData();
        mMainActivity.myvalue.put("value", value);
        int a = DkPhoneApplication.mApp.sportData.getdayTime();
        double b = DkPhoneApplication.mApp.sportData.getDayDistence();
        int c = DkPhoneApplication.mApp.sportData.getaverLeavel();
        double d = DkPhoneApplication.mApp.sportData.getDaycarl();
        double e = DkPhoneApplication.mApp.sportData.gettopspeed();
        double f = DkPhoneApplication.mApp.sportData.getaspeed();

        value.add(DkPhoneApplication.mApp.sportData.getdayTime());
        value.add(DkPhoneApplication.mApp.sportData.getDayDistence());
        value.add(DkPhoneApplication.mApp.sportData.getaverLeavel());
        value.add(DkPhoneApplication.mApp.sportData.getDaycarl());
        value.add(DkPhoneApplication.mApp.sportData.gettopspeed());
        value.add(DkPhoneApplication.mApp.sportData.getaspeed());
        mMainActivity.myvalue.put("name", DkPhoneApplication.mApp.selectUser.name);
        mMainActivity.personopLegend.array.clear();
        mMainActivity.personopLegend.add(DkPhoneApplication.mApp.selectUser.name);
        if (DkPhoneApplication.mApp.pk != null) {
            mMainActivity.personopLegend.add(DkPhoneApplication.mApp.pk.name);
        }
        if (DkPhoneApplication.mApp.pk == null) {
            mMainActivity.seriesItem.put("name", DkPhoneApplication.mApp.selectUser.name);
        } else {
            mMainActivity.seriesItem.put("name", DkPhoneApplication.mApp.selectUser.name + " VS " + DkPhoneApplication.mApp.pk.name);
        }
        if (mMainActivity.personFragment != null) {
            mMainActivity.personFragment.updataSport();
        }
    }

    public void updataTime() {
        mMainActivity.time.setText(TimeUtils.getDateAndTimeC(mMainActivity));
        mainHandler.sendEmptyMessageDelayed(MainHandler.UPDATA_TIME, 1000);
    }

    public TestManager.SendData sendData = new TestManager.SendData() {
        @Override
        public void sendDeviceConnect(boolean state) {
            Message msg = new Message();
            msg.what = MainHandler.EVENT_DEVICE_SEATE;
            msg.obj = state;
            mainHandler.sendMessage(msg);
        }

        @Override
        public void sendDeviceData(String[] data) {
            Message msg = new Message();
            msg.what = MainHandler.EVENT_DEVICE_UPDATA;
            msg.obj = data;
            mainHandler.sendMessage(msg);
        }

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


//    public void updataOptation(Intent intent) {
//        Optation optation = intent.getParcelableExtra("optation");
//        if (intent.getBooleanExtra("delete", false) == true) {
//            Optation temp = DkPadApplication.mApp.hashoptations.get(optation.oid);
//            DkPadApplication.mApp.hashoptations.remove(optation.oid);
//            if (temp != null)
//                DkPadApplication.mApp.optations.remove(temp);
//            DkPadApplication.mApp.selectOptation = DkPadApplication.mApp.optations.get(0);
//            mMainActivity.sOptationName.setText(DkPadApplication.mApp.selectOptation.name);
//            DkPadApplication.mApp.optations.remove(DkPadApplication.mApp.selectOptation);
//            optationAdapter.notifyDataSetChanged();
//            DkPadApplication.mApp.testManager.setNewTest(DkPadApplication.mApp.selectOptation, DkPadApplication.mApp.selectUser);
//        } else {
//            if (DkPadApplication.mApp.hashoptations.containsKey(optation.oid)) {
//                Optation temp = DkPadApplication.mApp.hashoptations.get(optation.oid);
//                temp.name = optation.name;
//                temp.data = optation.data;
//                optationAdapter.notifyDataSetChanged();
//            } else {
//                DkPadApplication.mApp.hashoptations.put(optation.oid, optation);
//                DkPadApplication.mApp.optations.add(0, optation);
//                optationAdapter.notifyDataSetChanged();
//            }
//            if(DkPadApplication.mApp.selectOptation.oid.equals(optation.oid))
//            {
//                DkPadApplication.mApp.optations.add(0, DkPadApplication.mApp.selectOptation);
//                DkPadApplication.mApp.optations.remove(optation);
//                DkPadApplication.mApp.selectOptation = optation;
//                mMainActivity.sOptationName.setText(optation.name);
//                optationAdapter.notifyDataSetChanged();
//                DkPadApplication.mApp.setLastOptation(DkPadApplication.mApp.selectOptation);
//                DkPadApplication.mApp.testManager.setNewTest(DkPadApplication.mApp.selectOptation, DkPadApplication.mApp.selectUser);
//                mMainActivity.homeFragment.initView();
//            }
//        }
//
//    }

    public void updataHead(Intent intent) {
        User user = intent.getParcelableExtra("user");
        if (intent.getBooleanExtra("delete", false) == true) {
            User temp = DkPhoneApplication.mApp.hashMap.get(user.uid);
            DkPhoneApplication.mApp.hashMap.remove(user.uid);
            if (temp != null)
                DkPhoneApplication.mApp.users.remove(temp);

            DkPhoneApplication.mApp.selectUser = DkPhoneApplication.mApp.users.get(0);
            setUserView(DkPhoneApplication.mApp.selectUser);
            DkPhoneApplication.mApp.users.remove(DkPhoneApplication.mApp.selectUser);
            userAdapter.notifyDataSetChanged();
            pkUserAdapter.notifyDataSetChanged();
            //DkPadApplication.mApp.testManager.setNewTest(DkPadApplication.mApp.selectOptation, DkPadApplication.mApp.selectUser);
        } else {
            if (DkPhoneApplication.mApp.hashMap.containsKey(user.uid)) {
                User temp = DkPhoneApplication.mApp.hashMap.get(user.uid);
                temp.name = user.name;
                temp.age = user.age;
                temp.toll = user.toll;
                temp.sex = user.sex;
                temp.headpath = user.headpath;
                userAdapter.notifyDataSetChanged();
                pkUserAdapter.notifyDataSetChanged();
            } else {
                DkPhoneApplication.mApp.hashMap.put(user.uid, user);
                DkPhoneApplication.mApp.users.add(0, user);
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
        grid.put("left", "16%");
        grid.put("right", "5%");
        grid.put("containLabel", true);
        ArrayData datazoom = new ArrayData();
        mMainActivity.sportoption.put("dataZoom", datazoom);
        ObjectData zoom1 = new ObjectData();
        datazoom.add(zoom1);
        zoom1.put("type", "slider");
        zoom1.put("start", 60);
        zoom1.put("end", 100);
        ObjectData zoom2 = new ObjectData();
        datazoom.add(zoom2);
        zoom2.put("type", "inside");
        zoom2.put("start", 60);
        zoom2.put("end", 100);

        ObjectData title = new ObjectData();
        mMainActivity.sportoption.put("title", title);
        title.put("text", "运动状态图");
        ArrayData color = new ArrayData();
        mMainActivity.sportoption.put("color", color);
        color.add(MainActivity.SPEET_COLOR);
        color.add(MainActivity.HERT_COLOR);
        color.add(MainActivity.SELECT_COLOR);
        color.add(MainActivity.CRL_COLOR);

        ObjectData legend = new ObjectData();
        mMainActivity.sportoption.put("legend", legend);
        mMainActivity.sportLegend = new ArrayData();
        legend.put("data", mMainActivity.sportLegend);
        mMainActivity.sportLegend.add("速度");
        mMainActivity.sportLegend.add("心率");
        mMainActivity.sportLegend.add("阻力");
        mMainActivity.sportLegend.add("卡路里");

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
        yAxis.put("name", "km/h");
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
        yAxis.put("offset", "30");
        yAxis.put("name", "bpm");
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
        yAxis.put("offset", "60");
        yAxis.put("name", "%");
        yAxis.put("max", "100");
        axisLine = new ObjectData();
        yAxis.put("axisLine", axisLine);
        lineStyle = new ObjectData();
        axisLine.put("lineStyle", lineStyle);
        lineStyle.put("color", MainActivity.SELECT_COLOR);
        yAxiss.add(yAxis);


        yAxis = new ObjectData();
        yAxis.put("type", "value");
        yAxis.put("position", "left");
        yAxis.put("offset", "90");
        yAxis.put("name", "cal");
        yAxis.put("max", "2000");
        axisLine = new ObjectData();
        yAxis.put("axisLine", axisLine);
        lineStyle = new ObjectData();
        axisLine.put("lineStyle", lineStyle);
        lineStyle.put("color", MainActivity.CRL_COLOR);
        yAxiss.add(yAxis);

        ArrayData series = new ArrayData();
        mMainActivity.sportoption.put("series", series);
        ObjectData speed = new ObjectData();
        series.add(speed);
        speed.put("name", "速度");
        speed.put("type", "line");
        speed.put("yAxisIndex", 0);
        speed.put("symbol", "none");
        speed.put("smooth", true);
        ObjectData areaStyle = new ObjectData();
        speed.put("areaStyle", areaStyle);
        areaStyle.put("color", MainActivity.SPEET_COLOR);
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
        hert.put("symbol", "none");
        hert.put("smooth", true);
        hert.put("yAxisIndex", 1);
        areaStyle = new ObjectData();
        hert.put("areaStyle", areaStyle);
        areaStyle.put("color", MainActivity.HERT_COLOR);
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
        select.put("name", "阻力");
        select.put("type", "line");
        select.put("yAxisIndex", 2);
        select.put("symbol", "none");
        select.put("smooth", true);
        areaStyle = new ObjectData();
        select.put("areaStyle", areaStyle);
        areaStyle.put("color", MainActivity.SELECT_COLOR);
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


        ObjectData crl = new ObjectData();
        series.add(crl);
        crl.put("name", "卡路里");
        crl.put("type", "line");
        crl.put("yAxisIndex", 3);
        crl.put("symbol", "none");
        crl.put("smooth", true);
        areaStyle = new ObjectData();
        crl.put("areaStyle", areaStyle);
        areaStyle.put("color", MainActivity.CRL_COLOR);
        itemStyle = new ObjectData();
        crl.put("itemStyle", itemStyle);
        normal = new ObjectData();
        crl.put("normal", normal);
        normal.put("color", MainActivity.CRL_COLOR);
        lineStyle = new ObjectData();
        normal.put("lineStyle", lineStyle);
        lineStyle.put("color", MainActivity.CRL_COLOR);
        mMainActivity.sportCalorie = new ArrayData();
        crl.put("data", mMainActivity.sportCalorie);
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
        lineStyleset.put("color", color);
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
        axisLabel.put("fontSize", 12);
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
        title.put("fontSize", 20);
        title.put("fontStyle", "italic");

        ObjectData detail = new ObjectData();
        speed.put("detail", detail);
        detail.put("fontWeight", "bolder");
        detail.put("fontSize", 15);
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
        detail.put("width", 38);
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
        center.add("28%");
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
        lineStyleset.put("color", color);

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
        axisLabel1.put("fontSize", 7);

        ObjectData title2 = new ObjectData();
        hert.put("title", title2);
        ArrayData offsetCenter = new ArrayData();
        title2.put("fontSize", 15);
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
        center1.add("72%");
        center1.add("55%");
        starng.put("radius", "40%");
        starng.put("min", 0);
        starng.put("max", 100);
        starng.put("endAngle", -45);
        starng.put("startAngle", 135);
        starng.put("splitNumber", 10);

        ObjectData axisLine2 = new ObjectData();
        starng.put("axisLine", axisLine2);
        lineStyleset = new ObjectData();
        axisLine2.put("lineStyle", lineStyleset);
        lineStyleset.put("width", 8);
        lineStyleset.put("color", color);

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
        axisLabel2.put("fontSize", 7);


        ObjectData title3 = new ObjectData();
        starng.put("title", title3);
        title3.put("fontSize", 15);
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
        mMainActivity.homeselect.put("value", DkPhoneApplication.mApp.testManager.test.currentLeavel);
        mMainActivity.homeselect.put("name", "%");
        mMainActivity.power.setProgress(DkPhoneApplication.mApp.testManager.test.currentLeavel);

        mMainActivity.speedvalue.setText("0" + "km/h");
        mMainActivity.hertvalue.setText("0" + "bpm");
        mMainActivity.selectvalue.setText(String.valueOf(DkPhoneApplication.mApp.testManager.test.currentLeavel) + "%");

        series.add(speed);
        series.add(hert);
        series.add(starng);
    }

    public Double getCar(int leavel, int rpm) {
        int a = 100 / DkPhoneApplication.mApp.maxSelect;
        int b = leavel / a;
        if (b > DkPhoneApplication.mApp.maxSelect) {
            b = DkPhoneApplication.mApp.maxSelect;
        }
        b = b - 1;
        if (b < 0) {
            b = 0;
        }

        int c = rpm / 10 - 1;
        if (c < 0) {
            c = 0;
        } else if (c > 11) {
            c = 11;
        }
        double k = EquipData.data[c][b] * 2.5;
        return k;
    }

    public Double getWart(int leavel, int rpm) {
        int a = 100 / DkPhoneApplication.mApp.maxSelect;
        int b = leavel / a;
        if (b > DkPhoneApplication.mApp.maxSelect) {
            b = DkPhoneApplication.mApp.maxSelect;
        }
        b = b - 1;
        if (b < 0) {
            b = 0;
        }

        int c = rpm / 10 - 1;
        if (c < 0) {
            c = 0;
        } else if (c > 11) {
            c = 11;
        }
        double w = EquipData.data[c][b];
        return w;
    }

    public void updata(String[] data) {

        mMainActivity.homespeed.put("value", data[0]);
        mMainActivity.homehert.put("value", data[1]);
        mMainActivity.homeselect.put("value", data[2]);

        setLeave(DkPhoneApplication.mApp.testManager.test.currentLeavel);

        mMainActivity.speedvalue.setText(data[0] + "km/h");
        mMainActivity.hertvalue.setText(data[1] + "bpm");
        mMainActivity.selectvalue.setText(data[2] + "%");

        mMainActivity.sportspeed.add(Double.valueOf(data[0]));
        mMainActivity.sporthert.add(Double.valueOf(data[1]));
        mMainActivity.sportselect.add(Double.valueOf(data[2]));
        mMainActivity.sportCalorie.add(getCar(Integer.valueOf(data[2]), Integer.valueOf(data[3])));

        mMainActivity.rpm.setText(data[3]+" rpm");
        if(Integer.valueOf(data[3]) > 0)
        {
            mMainActivity.state.setText("转动中");
        }
        else
        {
            mMainActivity.state.setText("已停止");
        }
        mMainActivity.sp.setText(String.valueOf(data[4]));
        mMainActivity.work.setText(String.valueOf(getWart(
                Integer.valueOf(data[2]), Integer.valueOf(data[3])))+" W");
        mMainActivity.sp.setText(data[2]);
        mMainActivity.sportx.add(DkPhoneApplication.mApp.testManager.test.praseCurrentTime());


        DkPhoneApplication.mApp.sportData.updataSecond(data);
        if (mMainActivity.detialSportFragment != null)
            mMainActivity.detialSportFragment.initView();
        if (mMainActivity.homeFragment != null)
            mMainActivity.homeFragment.initView();
        updataSport();
        if (mMainActivity.personFragment != null)
            mMainActivity.personFragment.initView();
        queryView2.queryListAdapter.notifyDataSetChanged();
        queryView2.queryListAdapter2.notifyDataSetChanged();
    }

    public void updataDevice(String[] data) {


        mMainActivity.rpm.setText(data[3]+" rpm");
        if(Integer.valueOf(data[3]) > 0)
        {
            mMainActivity.state.setText("转动中");
        }
        else
        {
            mMainActivity.state.setText("已停止");
        }
        mMainActivity.sp.setText(String.valueOf(data[4]));
        mMainActivity.sp.setText(data[2]);
        mMainActivity.work.setText(String.valueOf(getWart(
                Integer.valueOf(data[2]), Integer.valueOf(data[3])))+" W");

        mMainActivity.homespeed.put("value", data[0]);
        mMainActivity.homehert.put("value", data[1]);
        mMainActivity.homeselect.put("value", data[2]);

        setLeave(DkPhoneApplication.mApp.testManager.test.currentLeavel);

        mMainActivity.speedvalue.setText(data[0] + "km/h");
        mMainActivity.hertvalue.setText(data[1] + "bpm");
        mMainActivity.selectvalue.setText(data[2] + "%");

        mMainActivity.sportspeed.add(Double.valueOf(data[0]));
        mMainActivity.sporthert.add(Double.valueOf(data[1]));
        mMainActivity.sportselect.add(Double.valueOf(data[2]));
        mMainActivity.sportCalorie.add(getCar(Integer.valueOf(data[2]), Integer.valueOf(data[3])));

        mMainActivity.rpm.setText(data[3]+" rpm");
        if(Integer.valueOf(data[3]) > 0)
        {
            mMainActivity.state.setText("转动中");
        }
        else
        {
            mMainActivity.state.setText("已停止");
        }
        mMainActivity.sp.setText(String.valueOf(data[4]));
        mMainActivity.work.setText(String.valueOf(getWart(
                Integer.valueOf(data[2]), Integer.valueOf(data[3])))+" W");
        mMainActivity.sp.setText(data[2]);
        mMainActivity.sportx.add(DkPhoneApplication.mApp.testManager.test.praseCurrentTime());


        //DkPhoneApplication.mApp.sportData.updataSecond(data);
        if (mMainActivity.detialSportFragment != null)
            mMainActivity.detialSportFragment.initView();
        if (mMainActivity.homeFragment != null)
            mMainActivity.homeFragment.initView();
        //updataSport();
//        if (mMainActivity.personFragment != null)
//            mMainActivity.personFragment.initView();
        queryView2.queryListAdapter.notifyDataSetChanged();
        queryView2.queryListAdapter2.notifyDataSetChanged();
    }

    public void updataDeviceState(boolean data) {



        if(data == false)
        {
            mMainActivity.rpm.setText("0 rpm");
            mMainActivity.state.setText("已断开");
            mMainActivity.work.setText("0 W");
            mMainActivity.sp.setText("0");
        }
        else{
            if(mMainActivity.state.getText().equals("已断开"))
            mMainActivity.state.setText("已停止");
        }


    }

    public void setLeave(int leave) {

        mMainActivity.homeselect.put("value", Double.valueOf(leave));
        mMainActivity.power.setProgress(DkPhoneApplication.mApp.testManager.test.currentLeavel);
        mMainActivity.selectvalue.setText(String.valueOf(leave) + "%");
        mMainActivity.sportselect.add(Double.valueOf(leave));
        if (mMainActivity.homeFragment != null)
            mMainActivity.homeFragment.initView();
        if (mMainActivity.detialSportFragment != null)
            mMainActivity.detialSportFragment.initView();
    }

    public void doFinish(String[] data) {
        mMainActivity.btnStart.setImageResource(R.drawable.start);
        mMainActivity.btnStart2.setImageResource(R.drawable.start);
        DkPhoneApplication.mApp.testManager.test.isstart = false;
        DkPhoneApplication.mApp.testManager.stopTest();
        if (DkPhoneApplication.mApp.testManager.test.isstart) {
//            mMainActivity.headlayer.setVisibility(View.INVISIBLE);
            //mMainActivity.oplayer.setVisibility(View.INVISIBLE);
            mMainActivity.setting.setVisibility(View.INVISIBLE);
        } else {
//            mMainActivity.headlayer.setVisibility(View.VISIBLE);
            //mMainActivity.oplayer.setVisibility(View.VISIBLE);
            mMainActivity.setting.setVisibility(View.VISIBLE);
        }


        mMainActivity.homespeed.put("value", "0");
        mMainActivity.homehert.put("value", data[1]);
        mMainActivity.homeselect.put("value", data[2]);
        setLeave(DkPhoneApplication.mApp.testManager.test.currentLeavel);
        mMainActivity.speedvalue.setText(data[0] + "km/h");
        mMainActivity.hertvalue.setText(data[1] + "bpm");
        mMainActivity.selectvalue.setText(data[2] + "%");

        mMainActivity.rpm.setText(data[3]+" rpm");
        if(Integer.valueOf(data[3]) > 0)
        {
            mMainActivity.state.setText("转动中");
        }
        else
        {
            mMainActivity.state.setText("已停止");
        }
        mMainActivity.sp.setText(String.valueOf(data[4]));
        mMainActivity.sp.setText(data[2]);
        mMainActivity.work.setText(String.valueOf(getWart(
                Integer.valueOf(data[2]), Integer.valueOf(data[3])))+" W");


        mMainActivity.sportspeed.add(0.0);
        mMainActivity.sporthert.add(Double.valueOf(data[1]));
        mMainActivity.sportselect.add(Double.valueOf(data[2]));
        mMainActivity.sportCalorie.add(getCar(Integer.valueOf(data[2]), Integer.valueOf(data[3])));
        mMainActivity.sportx.add(DkPhoneApplication.mApp.testManager.test.praseCurrentTime());

//        DkPadApplication.mApp.sportData.setfinish(data);
        if (mMainActivity.detialSportFragment != null)
            mMainActivity.detialSportFragment.initView();
        if (mMainActivity.homeFragment != null)
            mMainActivity.homeFragment.initView();
        updataSport();
        if (mMainActivity.personFragment != null)
            mMainActivity.personFragment.initView();
        //DkPadApplication.mApp.testManager.setNewTest(DkPadApplication.mApp.selectOptation, DkPadApplication.mApp.selectUser);

    }

    public void doError(String[] data) {
//        DkPhoneApplication.mApp.testManager.test.isstart = false;
//        mMainActivity.btnStart.setImageResource(R.drawable.start);
//        mMainActivity.btnStart2.setImageResource(R.drawable.start);
//        DkPhoneApplication.mApp.testManager.stopTest();
//        if (DkPhoneApplication.mApp.testManager.test.isstart) {
////            mMainActivity.headlayer.setVisibility(View.INVISIBLE);
//            //mMainActivity.oplayer.setVisibility(View.INVISIBLE);
//            mMainActivity.setting.setVisibility(View.INVISIBLE);
//        } else {
////            mMainActivity.headlayer.setVisibility(View.VISIBLE);
//            //mMainActivity.oplayer.setVisibility(View.VISIBLE);
//            mMainActivity.setting.setVisibility(View.VISIBLE);
//        }


        mMainActivity.homespeed.put("value", "0");
        mMainActivity.homehert.put("value", data[1]);
        mMainActivity.homeselect.put("value", data[2]);
        setLeave(DkPhoneApplication.mApp.testManager.test.currentLeavel);
        mMainActivity.speedvalue.setText(data[0] + "km/h");
        mMainActivity.hertvalue.setText(data[1] + "bpm");
        mMainActivity.selectvalue.setText(data[2] + "%");
        mMainActivity.rpm.setText(data[3]+" rpm");
        if(Integer.valueOf(data[3]) > 0)
        {
            mMainActivity.state.setText("转动中");
        }
        else
        {
            mMainActivity.state.setText("已停止");
        }
        mMainActivity.sp.setText(String.valueOf(data[4]));
        mMainActivity.sp.setText(data[2]);
        mMainActivity.work.setText(String.valueOf(getWart(
                Integer.valueOf(data[2]), Integer.valueOf(data[3])))+" W");

        mMainActivity.sportspeed.add(0.0);
        mMainActivity.sporthert.add(Double.valueOf(data[1]));
        mMainActivity.sportselect.add(Double.valueOf(data[2]));
        mMainActivity.sportCalorie.add(getCar(Integer.valueOf(data[2]), Integer.valueOf(data[3])));
        mMainActivity.sportx.add(DkPhoneApplication.mApp.testManager.test.praseCurrentTime());

        DkPhoneApplication.mApp.sportData.updataSecond(data);
        if (mMainActivity.detialSportFragment != null)
            mMainActivity.detialSportFragment.initView();
        if (mMainActivity.homeFragment != null)
            mMainActivity.homeFragment.initView();
        updataSport();
        if (mMainActivity.personFragment != null)
            mMainActivity.personFragment.initView();

        AppUtils.showMessage(mMainActivity, mMainActivity.getString(R.string.setting_deive_error));
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
        radar.put("radius", "60%");
        radar.put("splitNumber", "5");
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
        if (DkPhoneApplication.mApp.selectUser != null) {
            mMainActivity.personopLegend.add(DkPhoneApplication.mApp.selectUser.name);
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
        jo3.put("name", mMainActivity.getString(R.string.chart_car));
        jo3.put("max", 2500);
        ObjectData jo4 = new ObjectData();
        mMainActivity.indicator.add(jo4);
        jo4.put("name", mMainActivity.getString(R.string.chart_top_speed));
        jo4.put("max",  50);
        ObjectData jo5 = new ObjectData();
        mMainActivity.indicator.add(jo5);
        jo5.put("name", mMainActivity.getString(R.string.chart_average_speed));
        jo5.put("max", 50);

        mMainActivity.series = new ArrayData();
        mMainActivity.personoption.put("series", mMainActivity.series);
        mMainActivity.seriesItem = new ObjectData();
        mMainActivity.series.add(mMainActivity.seriesItem);
        mMainActivity.seriesItem.put("name", DkPhoneApplication.mApp.selectUser.name);
        mMainActivity.seriesItem.put("type", "radar");
        mMainActivity.seriesData = new ArrayData();
        mMainActivity.seriesItem.put("data", mMainActivity.seriesData);
        mMainActivity.myvalue = new ObjectData();
        mMainActivity.seriesData.add(mMainActivity.myvalue);
        ArrayData value = new ArrayData();
        mMainActivity.myvalue.put("value", value);

        int a = DkPhoneApplication.mApp.sportData.getdayTime();
        double b = DkPhoneApplication.mApp.sportData.getDayDistence();
        int c = DkPhoneApplication.mApp.sportData.getaverLeavel();
        double d = DkPhoneApplication.mApp.sportData.getDaycarl();
        double e = DkPhoneApplication.mApp.sportData.gettopspeed();
        double f = DkPhoneApplication.mApp.sportData.getaspeed();

        value.add(DkPhoneApplication.mApp.sportData.getdayTime());
        value.add(DkPhoneApplication.mApp.sportData.getDayDistence());
        value.add(DkPhoneApplication.mApp.sportData.getaverLeavel());
        value.add(DkPhoneApplication.mApp.sportData.getDaycarl());
        value.add(DkPhoneApplication.mApp.sportData.gettopspeed());
        value.add(DkPhoneApplication.mApp.sportData.getaspeed());
        mMainActivity.myvalue.put("name", DkPhoneApplication.mApp.selectUser.name);
    }


    public void hidOper() {
        mMainActivity.power.setVisibility(View.INVISIBLE);
        mMainActivity.btnStart.setVisibility(View.INVISIBLE);
        mMainActivity.btnAdd.setVisibility(View.INVISIBLE);
        mMainActivity.btnDes.setVisibility(View.INVISIBLE);
        mMainActivity.btnMap.setVisibility(View.INVISIBLE);
        mMainActivity.btnWatch.setVisibility(View.INVISIBLE);
        mMainActivity.btntv.setVisibility(View.INVISIBLE);
//        mMainActivity.headlayer.setVisibility(View.INVISIBLE);
        //mMainActivity.oplayer.setVisibility(View.INVISIBLE);
        if (DkPhoneApplication.mApp.testManager.test.isstart) {
            mMainActivity.setting.setVisibility(View.INVISIBLE);
        } else {
            mMainActivity.setting.setVisibility(View.VISIBLE);
        }

    }

    public void showOper() {
        mMainActivity.power.setVisibility(View.VISIBLE);
        mMainActivity.btnStart.setVisibility(View.VISIBLE);
        mMainActivity.btnAdd.setVisibility(View.VISIBLE);
        mMainActivity.btnDes.setVisibility(View.VISIBLE);
        mMainActivity.btnMap.setVisibility(View.VISIBLE);
        mMainActivity.btnWatch.setVisibility(View.VISIBLE);
        mMainActivity.btntv.setVisibility(View.VISIBLE);
        if (DkPhoneApplication.mApp.testManager.test.isstart) {
//            mMainActivity.headlayer.setVisibility(View.INVISIBLE);
           // mMainActivity.oplayer.setVisibility(View.INVISIBLE);
            mMainActivity.setting.setVisibility(View.INVISIBLE);
        } else {
//            mMainActivity.headlayer.setVisibility(View.VISIBLE);
            //mMainActivity.oplayer.setVisibility(View.VISIBLE);
            mMainActivity.setting.setVisibility(View.VISIBLE);
        }
    }

    public ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                //hidOper();
            } else {
                //showOper();
            }
            if (position == 0) {
                mMainActivity.btnleft.setVisibility(View.INVISIBLE);
                mMainActivity.btnright.setVisibility(View.VISIBLE);
                mMainActivity.state.setVisibility(View.INVISIBLE);
                mMainActivity.stateTitle.setVisibility(View.INVISIBLE);
                mMainActivity.rpm.setVisibility(View.INVISIBLE);
                mMainActivity.rpmTitle.setVisibility(View.INVISIBLE);
                mMainActivity.work.setVisibility(View.INVISIBLE);
                mMainActivity.workTitle.setVisibility(View.INVISIBLE);
            } else if (position == 1) {
                mMainActivity.btnleft.setVisibility(View.VISIBLE);
                mMainActivity.btnright.setVisibility(View.VISIBLE);
                mMainActivity.state.setVisibility(View.VISIBLE);
                mMainActivity.stateTitle.setVisibility(View.VISIBLE);
                mMainActivity.rpm.setVisibility(View.VISIBLE);
                mMainActivity.rpmTitle.setVisibility(View.VISIBLE);
                mMainActivity.work.setVisibility(View.VISIBLE);
                mMainActivity.workTitle.setVisibility(View.VISIBLE);
            } else {
                mMainActivity.btnleft.setVisibility(View.VISIBLE);
                mMainActivity.btnright.setVisibility(View.INVISIBLE);
                mMainActivity.state.setVisibility(View.INVISIBLE);
                mMainActivity.stateTitle.setVisibility(View.INVISIBLE);
                mMainActivity.rpm.setVisibility(View.INVISIBLE);
                mMainActivity.rpmTitle.setVisibility(View.INVISIBLE);
                mMainActivity.work.setVisibility(View.INVISIBLE);
                mMainActivity.workTitle.setVisibility(View.INVISIBLE);

            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };


    public View.OnClickListener leftListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mMainActivity.viewPager.setCurrentItem(mMainActivity.viewPager.getCurrentItem() - 1);
        }
    };


    public View.OnClickListener rightListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mMainActivity.viewPager.setCurrentItem(mMainActivity.viewPager.getCurrentItem() + 1);
        }
    };


}
