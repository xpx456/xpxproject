package com.dk.dkhome.presenter;


import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.dk.dkhome.R;
import com.dk.dkhome.entity.EquipData;
import com.dk.dkhome.entity.Goal;
import com.dk.dkhome.entity.User;
import com.dk.dkhome.view.DkhomeApplication;
import com.dk.dkhome.view.activity.GoalActivity;
import com.dk.dkhome.view.activity.MainActivity;
import com.dk.dkhome.view.activity.RegisterActivity;
import com.dk.dkhome.view.adapter.GoalPageAdapter;
import com.dk.dkhome.view.adapter.SportPageAdapter;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.mywidget.NoScrollViewPager;


public class GoalPresenter implements Presenter {

    public GoalActivity mGoalActivity;
    public GoalPageAdapter sportPageAdapter;
    public NoScrollViewPager viewPager;
    public TextView btnFinish;
    public TextView titleDay;
    public TextView titleWeight;
    public EditText dayValue;
    public EditText weightValue;
    public TextView carlValue;
    public TextView btnhealth;
    public TextView btnlose;
    public TextView btngain;
    public View goal1;
    public View goal2;
    public Goal goal = new Goal();
    public User account = new User();
    public GoalPresenter(GoalActivity mGoalActivity) {
        this.mGoalActivity = mGoalActivity;
    }

    @Override
    public void initView() {
        // TODO Autonerated method stub
        mGoalActivity.setContentView(R.layout.activity_goal);
        TextView title = mGoalActivity.findViewById(R.id.title);
        if(mGoalActivity.getIntent().getBooleanExtra(RegisterActivity.IS_REGISTER,false) == true)
        {
            account = mGoalActivity.getIntent().getParcelableExtra("user");
            title.setText(mGoalActivity.getString(R.string.register_title));
        }
        else
        {
            title.setText(mGoalActivity.getString(R.string.main_left_goal));
        }
        ImageView back = mGoalActivity.findViewById(R.id.back);
        back.setOnClickListener(backListener);
        LayoutInflater inflater = LayoutInflater.from(mGoalActivity);
        goal1 = inflater.inflate(R.layout.fragment_goal1, null);
        goal2 = inflater.inflate(R.layout.fragment_goal2, null);
        viewPager = mGoalActivity.findViewById(R.id.goalpager);
        sportPageAdapter = new GoalPageAdapter();
        sportPageAdapter.mViews.add(goal1);
        sportPageAdapter.mViews.add(goal2);

        viewPager.setNoScroll(true);
        viewPager.setAdapter(sportPageAdapter);
        viewPager.setCurrentItem(0);
        btnhealth = goal1.findViewById(R.id.goal1);
        btnlose = goal1.findViewById(R.id.goal2);
        btngain = goal1.findViewById(R.id.goal3);

        btnFinish = goal2.findViewById(R.id.btn_finish);
        titleDay = goal2.findViewById(R.id.day_title);
        dayValue = goal2.findViewById(R.id.day_value);
        titleWeight = goal2.findViewById(R.id.goal_title);
        weightValue = goal2.findViewById(R.id.goal_value);
        carlValue = goal2.findViewById(R.id.carl_value);
        btnhealth.setOnClickListener(healthListener);
        btnlose.setOnClickListener(loseListener);
        btngain.setOnClickListener(gainListener);
        btnFinish.setOnClickListener(finishListener);
        dayValue.addTextChangedListener(dayWatcher);
        weightValue.addTextChangedListener(weightWatcher);
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


    private View.OnClickListener finishListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            goal.goalweight = Integer.valueOf(weightValue.getText().toString());
            goal.daycarl = Integer.valueOf(carlValue.getText().toString());
            goal.week = Integer.valueOf(dayValue.getText().toString());
            if(goal.goalweight == 0 || goal.week == 0)
            {
                AppUtils.showMessage(mGoalActivity,mGoalActivity.getString(R.string.goal_fail));
                return;
            }
            DkhomeApplication.mApp.goal.goalweight = goal.goalweight;
            DkhomeApplication.mApp.goal.daycarl = goal.daycarl;
            DkhomeApplication.mApp.goal.week = goal.week;
            DkhomeApplication.mApp.setGoal();
            if(mGoalActivity.getIntent().getBooleanExtra(RegisterActivity.IS_REGISTER,false) == true)
            {
                DkhomeApplication.mApp.mAccount.name = account.name;
                DkhomeApplication.mApp.mAccount.age = account.age;
                DkhomeApplication.mApp.mAccount.sex = account.sex;
                DkhomeApplication.mApp.mAccount.tall = account.tall;
                DkhomeApplication.mApp.mAccount.lastweight = account.lastweight;
                DkhomeApplication.mApp.mAccount.headpath = account.headpath;
                DkhomeApplication.mApp.setAccount();
                Intent intent = new Intent(mGoalActivity, MainActivity.class);
                mGoalActivity.startActivity(intent);
            }
            else
            {
                Intent intent = new Intent(MainActivity.ACTION_UPDATA_DAIRY);
                mGoalActivity.sendBroadcast(intent);
                mGoalActivity.finish();
            }
        }
    };


    private TextWatcher weightWatcher = new TextWatcher()
    {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(weightValue.getText().toString().startsWith("0"))
            {
                weightValue.setText(weightValue.getText().toString().substring(1,weightValue.getText().toString().length()));
            }
            if(dayValue.getText().length() > 0 && weightValue.getText().length() > 0)
                carlValue.setText(String.valueOf(EquipData.goalCarl(Integer.valueOf(weightValue.getText().toString()),
                        Integer.valueOf(dayValue.getText().toString())*7)));
        }

    };


    private TextWatcher dayWatcher = new TextWatcher()
    {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            if(dayValue.getText().toString().startsWith("0"))
            {
                dayValue.setText(dayValue.getText().toString().substring(1,dayValue.getText().toString().length()));
            }
            if(dayValue.getText().length() > 0 && weightValue.getText().length() > 0)
                carlValue.setText(String.valueOf(EquipData.goalCarl(Integer.valueOf(weightValue.getText().toString()),
                        Integer.valueOf(dayValue.getText().toString())*7)));
        }

    };


    private View.OnClickListener healthListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DkhomeApplication.mApp.goal.type = Goal.TYPE_HEALTH;
            DkhomeApplication.mApp.goal.goalweight = 0;
            DkhomeApplication.mApp.goal.daycarl = 0;
            DkhomeApplication.mApp.goal.week = 0;
            DkhomeApplication.mApp.setGoal();
            if(mGoalActivity.getIntent().getBooleanExtra(RegisterActivity.IS_REGISTER,true) == true)
            {
                DkhomeApplication.mApp.setAccount();
                Intent intent = new Intent(mGoalActivity, MainActivity.class);
                mGoalActivity.startActivity(intent);
            }
            else
            {
                Intent intent = new Intent(MainActivity.ACTION_UPDATA_DAIRY);
                mGoalActivity.sendBroadcast(intent);
                mGoalActivity.finish();
            }
        }
    };

    private View.OnClickListener loseListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DkhomeApplication.mApp.goal.type = Goal.TYPE_LOSE_WEIGHT;
            titleDay.setText(mGoalActivity.getString(R.string.goal_lday));
            titleWeight.setText(mGoalActivity.getString(R.string.goal_lweight));
            viewPager.setCurrentItem(1);

        }
    };

    private View.OnClickListener gainListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DkhomeApplication.mApp.goal.type = Goal.TYPE_GAIN_WEIGHT;
            titleDay.setText(mGoalActivity.getString(R.string.goal_gday));
            titleWeight.setText(mGoalActivity.getString(R.string.goal_gweight));
            viewPager.setCurrentItem(1);
        }
    };

    private View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(viewPager.getCurrentItem() == 0)
            {
                mGoalActivity.finish();
            }
            else
            {
                viewPager.setCurrentItem(0);
            }
        }
    };
}
