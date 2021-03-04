package com.dk.dkhome.presenter;


import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.gifdecoder.GifHeader;
import com.dk.dkhome.R;
import com.dk.dkhome.handler.MainHandler;
import com.dk.dkhome.receiver.MainReceiver;
import com.dk.dkhome.view.CourseView;
import com.dk.dkhome.view.DairyView;
import com.dk.dkhome.view.DkhomeApplication;
import com.dk.dkhome.view.SettingView;
import com.dk.dkhome.view.activity.ChartActivity;
import com.dk.dkhome.view.activity.DeviceActivity;
import com.dk.dkhome.view.activity.FoodActivity;
import com.dk.dkhome.view.activity.GoalActivity;
import com.dk.dkhome.view.activity.HealthActivity;
import com.dk.dkhome.view.activity.MainActivity;
import com.dk.dkhome.view.activity.RegisterActivity;
import com.dk.dkhome.view.activity.ScheduleActivity;
import com.dk.dkhome.view.adapter.SportPageAdapter;

import java.io.File;
import java.util.ArrayList;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.apputils.MenuItem;
import intersky.mywidget.CircleImageView;
import intersky.mywidget.NoScrollViewPager;


public class MainPresenter implements Presenter {

    public MainActivity mMainActivity;
    public NoScrollViewPager viewPager;
    public SportPageAdapter mianAdapter;
    public RelativeLayout mShade;
    public PopupWindow popupWindow;
    public DairyView dairyView;
    public CourseView courseView;
    public SettingView settingView;
    public ImageView tabimgdairy;
    public ImageView tabimgcourse;
    public ImageView tabimgsetting;
    public TextView tabtxtdairy;
    public TextView tabtxtcourse;
    public TextView tabtxtsetting;
    public RelativeLayout btndairy;
    public RelativeLayout btncourse;
    public RelativeLayout btnsetting;
    public TextView title;
    public View leftView;
    public MainHandler mMainHandler;
    public ImageView menu;
    public CircleImageView lefthead;
    public TextView leftName;
    public TextView leftDes;
    public RelativeLayout btnSchdule;
    public RelativeLayout btnEat;
    public RelativeLayout btnData;
    public RelativeLayout btnUser;
    public RelativeLayout btnHelth;
    public RelativeLayout btnGoal;
    public RelativeLayout btnDevice;
    public PopupWindow popupWindow1;
    public MainPresenter(MainActivity mMainActivity) {
        this.mMainActivity = mMainActivity;
        this.mMainHandler = new MainHandler(mMainActivity);
        this.mMainActivity.setBaseReceiver(new MainReceiver(mMainHandler));
    }

    @Override
    public void initView() {
        // TODO Autonerated method stub
        mMainActivity.flagFillBack = false;
        mMainActivity.setContentView(R.layout.activity_main);
        DkhomeApplication.mApp.testManager.bluetoothSetManager.scanLeDevice();
        menu = mMainActivity.findViewById(R.id.menu);
        title = mMainActivity.findViewById(R.id.maintitle);
        viewPager = mMainActivity.findViewById(R.id.page);
        viewPager.setNoScroll(true);
        mShade = mMainActivity.findViewById(R.id.shade);
        dairyView = new DairyView(this);
        courseView = new CourseView(this);
        settingView = new SettingView(this);
        btndairy = mMainActivity.findViewById(R.id.btndairy);
        btncourse = mMainActivity.findViewById(R.id.btntcourse);
        btnsetting = mMainActivity.findViewById(R.id.btnset);
        tabtxtdairy = mMainActivity.findViewById(R.id.dairytext);
        tabtxtcourse = mMainActivity.findViewById(R.id.coursetext);
        tabtxtsetting = mMainActivity.findViewById(R.id.settext);
        tabimgdairy = mMainActivity.findViewById(R.id.dairytimg);
        tabimgcourse = mMainActivity.findViewById(R.id.coursetimg);
        tabimgsetting = mMainActivity.findViewById(R.id.settimg);
        btndairy.setOnClickListener(showdairyListener);
        btncourse.setOnClickListener(showcourseListener);
        btnsetting.setOnClickListener(showsettingListener);
        mianAdapter = new SportPageAdapter();
        mianAdapter.mViews.add(dairyView.view);
        mianAdapter.mViews.add(courseView.view);
        mianAdapter.mViews.add(settingView.view);
        viewPager.setAdapter(mianAdapter);
        tabimgdairy.setImageResource(R.drawable.dairys);
        tabimgcourse.setImageResource(R.drawable.course);
        tabimgsetting.setImageResource(R.drawable.set);
        tabtxtdairy.setTextColor(Color.parseColor("#ff5e3a"));
        tabtxtcourse.setTextColor(Color.parseColor("#333333"));
        tabtxtsetting.setTextColor(Color.parseColor("#333333"));
        viewPager.setCurrentItem(0);
        title.setText(mMainActivity.getString(R.string.main_tab_dairy));
        menu.setOnClickListener(showLeftListener);
        initLeft();
        dairyView.updataData();
    }


    @Override
    public void Start() {
        // TODO Auto-generated method stub

    }


    @Override
    public void Resume() {
        // TODO Auto-generated method stub
    }


    @Override
    public void Pause() {
        // TODO Auto-generated method stub
    }

    @Override
    public void Destroy() {
        // TODO Auto-generated method stub
    }

    @Override
    public void Create() {
        // TODO Auto-generated method stub
        initView();
    }

    private void initLeft() {
        LayoutInflater inflater = LayoutInflater.from(mMainActivity);
        leftView = inflater.inflate(R.layout.fragment_left, null);
        lefthead = leftView.findViewById(R.id.icon);
        leftName = leftView.findViewById(R.id.leftname);
        leftDes = leftView.findViewById(R.id.leftcreat);
        btnSchdule = leftView.findViewById(R.id.schedule);
        btnEat = leftView.findViewById(R.id.eat);
        btnData = leftView.findViewById(R.id.data);
        btnUser = leftView.findViewById(R.id.user);
        btnHelth = leftView.findViewById(R.id.health);
        btnGoal = leftView.findViewById(R.id.goal);
        btnDevice = leftView.findViewById(R.id.device);
        if(DkhomeApplication.mApp.mAccount.headpath.length() > 0)
        {
            File mFile = new File(DkhomeApplication.mApp.mAccount.headpath);
            if(mFile.exists())
            {
                Glide.with(mMainActivity).load(mFile).into(lefthead);
            }
        }
        if(DkhomeApplication.mApp.mAccount.sex == 0)
        {
            if(DkhomeApplication.mApp.mAccount.headpath.length() <= 0)
            {
                lefthead.setImageResource(R.drawable.default_user);
            }
        }
        else
        {
            if(DkhomeApplication.mApp.mAccount.headpath.length() <= 0)
            {
                lefthead.setImageResource(R.drawable.default_wuser);
            }
        }
        lefthead.setOnClickListener(headListener);
        leftName.setText(DkhomeApplication.mApp.mAccount.name);
        leftDes.setText(mMainActivity.findViewById(R.id.leftcreat)+DkhomeApplication.mApp.mAccount.creat);
        btnSchdule.setOnClickListener(startScheduleListener);
        btnEat.setOnClickListener(startEatListener);
        btnData.setOnClickListener(startSportdataListener);
        btnUser.setOnClickListener(startUserListener);
        btnHelth.setOnClickListener(startHealListener);
        btnGoal.setOnClickListener(startGoalListener);
        btnDevice.setOnClickListener(startDeviceListener);

    }

    private View.OnClickListener showdairyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            tabimgdairy.setImageResource(R.drawable.dairys);
            tabimgcourse.setImageResource(R.drawable.course);
            tabimgsetting.setImageResource(R.drawable.set);
            tabtxtdairy.setTextColor(Color.parseColor("#ff5e3a"));
            tabtxtcourse.setTextColor(Color.parseColor("#333333"));
            tabtxtsetting.setTextColor(Color.parseColor("#333333"));
            viewPager.setCurrentItem(0);
            title.setText(mMainActivity.getString(R.string.main_tab_dairy));
        }
    };

    private View.OnClickListener showcourseListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            tabimgdairy.setImageResource(R.drawable.dairy);
            tabimgcourse.setImageResource(R.drawable.courses);
            tabimgsetting.setImageResource(R.drawable.set);
            tabtxtdairy.setTextColor(Color.parseColor("#333333"));
            tabtxtcourse.setTextColor(Color.parseColor("#ff5e3a"));
            tabtxtsetting.setTextColor(Color.parseColor("#333333"));
            viewPager.setCurrentItem(1);
            title.setText(mMainActivity.getString(R.string.main_tab_course));
        }
    };

    private View.OnClickListener showsettingListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            tabimgdairy.setImageResource(R.drawable.dairy);
            tabimgcourse.setImageResource(R.drawable.course);
            tabimgsetting.setImageResource(R.drawable.sets);
            tabtxtdairy.setTextColor(Color.parseColor("#333333"));
            tabtxtcourse.setTextColor(Color.parseColor("#333333"));
            tabtxtsetting.setTextColor(Color.parseColor("#ff5e3a"));
            viewPager.setCurrentItem(2);
            title.setText(mMainActivity.getString(R.string.main_tab_set));
        }
    };

    private View.OnClickListener showLeftListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(popupWindow1 != null)
            {
                popupWindow1.dismiss();
            }
            popupWindow1 = AppUtils.creatLeftView(mMainActivity,mShade,mMainActivity.findViewById(R.id.activity_main),leftView);
        }
    };

    private View.OnClickListener headListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ArrayList<MenuItem> items = new ArrayList<MenuItem>();
            MenuItem menuItem = new MenuItem();
            menuItem.btnName = mMainActivity.getString(R.string.button_word_takephoto);
            menuItem.mListener = mTakePhotoListenter;
            items.add(menuItem);
            menuItem = new MenuItem();
            menuItem.btnName = mMainActivity.getString(R.string.button_word_album);
            menuItem.mListener = mAddPicListener;
            items.add(menuItem);
            popupWindow = AppUtils.creatButtomMenu(mMainActivity,mShade,items,mMainActivity.findViewById(R.id.activity_root));
        }
    };

    private View.OnClickListener mAddPicListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            DkhomeApplication.mApp.mFileUtils.selectPhotos(mMainActivity, RegisterActivity.CHOSE_PICTURE_HEAD);
            if(popupWindow != null)
            popupWindow.dismiss();
        }
    };

    private View.OnClickListener mTakePhotoListenter = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            DkhomeApplication.mApp.mFileUtils.checkPermissionTakePhoto(mMainActivity, DkhomeApplication.mApp.mFileUtils.pathUtils.getfilePath("head/photo"),RegisterActivity.TAKE_PHOTO_HEAD);
            if(popupWindow != null)
            popupWindow.dismiss();
        }

    };

    private View.OnClickListener startScheduleListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mMainActivity, ScheduleActivity.class);
            mMainActivity.startActivity(intent);
            if(popupWindow1 != null)
            {
                popupWindow1.dismiss();
            }
        }
    };

    public View.OnClickListener startEatListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mMainActivity, FoodActivity.class);
            mMainActivity.startActivity(intent);
            if(popupWindow1 != null)
            {
                popupWindow1.dismiss();
            }
        }
    };

    public View.OnClickListener startSportdataListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mMainActivity, ChartActivity.class);
            mMainActivity.startActivity(intent);
            if(popupWindow1 != null)
            {
                popupWindow1.dismiss();
            }
        }
    };

    private View.OnClickListener startUserListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mMainActivity, RegisterActivity.class);
            mMainActivity.startActivity(intent);
            if(popupWindow1 != null)
            {
                popupWindow1.dismiss();
            }
        }
    };

    public View.OnClickListener startHealListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mMainActivity, HealthActivity.class);
            mMainActivity.startActivity(intent);
            if(popupWindow1 != null)
            {
                popupWindow1.dismiss();
            }
        }
    };

    public View.OnClickListener startGoalListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mMainActivity, GoalActivity.class);
            mMainActivity.startActivity(intent);
            if(popupWindow1 != null)
            {
                popupWindow1.dismiss();
            }
        }
    };

    private View.OnClickListener startDeviceListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mMainActivity, DeviceActivity.class);
            mMainActivity.startActivity(intent);
            if(popupWindow1 != null)
            {
                popupWindow1.dismiss();
            }
        }
    };
}
