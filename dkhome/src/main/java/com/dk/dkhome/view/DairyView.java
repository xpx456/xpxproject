package com.dk.dkhome.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dk.dkhome.R;
import com.dk.dkhome.database.DBHelper;
import com.dk.dkhome.entity.Course;
import com.dk.dkhome.entity.Eat;
import com.dk.dkhome.entity.EquipData;
import com.dk.dkhome.entity.Goal;
import com.dk.dkhome.entity.UserWeight;
import com.dk.dkhome.presenter.MainPresenter;
import com.dk.dkhome.presenter.NewPlanPresenter;
import com.dk.dkhome.utils.FoodManager;
import com.dk.dkhome.utils.WeightManager;
import com.dk.dkhome.view.activity.NewPlanActivity;
import com.dk.dkhome.view.activity.RegisterActivity;
import com.dk.dkhome.view.activity.SportDetialActivity;

import java.io.File;
import java.util.ArrayList;

import intersky.apputils.AppUtils;
import intersky.apputils.MenuItem;
import intersky.apputils.TimeUtils;
import intersky.filetools.PathUtils;
import intersky.mywidget.RoundProgressBar;
import intersky.mywidget.SemicircleProgressView;

public class DairyView {

    public MainPresenter mainPresenter;
    public View view;
    public LinearLayout planList;
    public SemicircleProgressView semicircleProgressView;
    public ImageView headimg;
    public ImageView btnmenu;
    public TextView hellodword;
    public TextView goalvalue;
    public TextView weighttitlevalue;
    public Button btngoal;
    public Button btnweight;
    public TextView daytitle;
    public TextView lastweightday;
    public ImageView btnleft;
    public ImageView btnright;
    public ImageView sex;
    public TextView eatvalue;
    public TextView burnedvalue;
    public String day;
    public RelativeLayout addCoures;
    public ImageView weightState;
    public DairyView(MainPresenter mainPresenter) {
        this.mainPresenter = mainPresenter;
        init();
    }

    public void takePhotoResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RegisterActivity.TAKE_PHOTO_HEAD:
                if (resultCode == Activity.RESULT_OK) {
                    File file = new File(DkhomeApplication.mApp.mFileUtils.takePhotoPath);
                    String name = file.getName().substring(0, file.getName().lastIndexOf("."));
                    if (file.exists()) {
                        if (requestCode == RegisterActivity.TAKE_PHOTO_HEAD) {
                            // 开启裁剪,设置requestCode为CROP_PHOTO
                            DkhomeApplication.mApp.mFileUtils.cropPhoto(mainPresenter.mMainActivity, 1, 1, file.getPath(),
                                    DkhomeApplication.mApp.mFileUtils.getOutputHeadMediaFileUri(DkhomeApplication.mApp.mFileUtils.pathUtils.getfilePath(DkhomeApplication.HOME_PATH), name).getPath()
                                    , (int) (105 * mainPresenter.mMainActivity.mBasePresenter.mScreenDefine.density), (int) (105 * mainPresenter.mMainActivity.mBasePresenter.mScreenDefine.density),
                                    RegisterActivity.CROP_HEAD);
                        }
                    }
                }
                break;
            case RegisterActivity.CHOSE_PICTURE_HEAD:
                if (resultCode == Activity.RESULT_OK) {
                    String path = AppUtils.getFileAbsolutePath(mainPresenter.mMainActivity, data.getData());
                    File file = new File(path);
                    String name = file.getName().substring(0, file.getName().lastIndexOf("."));
                    if (file.exists()) {
                        // 设置可缩放
                        if (requestCode == RegisterActivity.CHOSE_PICTURE_HEAD) {
                            // 开启裁剪,设置requestCode为CROP_PHOTO
                            DkhomeApplication.mApp.mFileUtils.cropPhoto(mainPresenter.mMainActivity, 1, 1, path,
                                    DkhomeApplication.mApp.mFileUtils.getOutputHeadMediaFileUri(DkhomeApplication.mApp.mFileUtils.pathUtils.getfilePath(DkhomeApplication.HOME_PATH), name).getPath()
                                    , (int) (105 * mainPresenter.mMainActivity.mBasePresenter.mScreenDefine.density), (int) (105 * mainPresenter.mMainActivity.mBasePresenter.mScreenDefine.density),
                                    RegisterActivity.CROP_HEAD);
                        }
                    }
                }
                break;
            case RegisterActivity.CROP_HEAD:
                if (resultCode == Activity.RESULT_OK) {
                    ArrayList<String> paths = DkhomeApplication.mApp.mFileUtils.getImage(data);
                    File mFile = new File(paths.get(0));
                    Glide.with(mainPresenter.mMainActivity).load(mFile).into(headimg);
                    DkhomeApplication.mApp.mAccount.headpath = mFile.getPath();
                }
                break;

        }
    }

    public void updataData()
    {
        if(DkhomeApplication.mApp.mAccount.headpath.length() > 0)
        {
            File mFile = new File(DkhomeApplication.mApp.mAccount.headpath);
            if(mFile.exists())
            {
                Glide.with(mainPresenter.mMainActivity).load(mFile).into(headimg);
            }
        }
        if(DkhomeApplication.mApp.mAccount.sex == 0)
        {
            sex.setImageResource(R.drawable.male);
            if(DkhomeApplication.mApp.mAccount.headpath.length() <= 0)
            {
                headimg.setImageResource(R.drawable.default_user);
            }
        }
        else
        {
            sex.setImageResource(R.drawable.female);
            if(DkhomeApplication.mApp.mAccount.headpath.length() <= 0)
            {
                headimg.setImageResource(R.drawable.default_wuser);
            }
        }
        SpannableString spannableString = new SpannableString(
                mainPresenter.mMainActivity.getString(R.string.main_hellow_word_1)
                + DkhomeApplication.mApp.mAccount.name+"\r\n"
                +mainPresenter.mMainActivity.getString(R.string.main_hellow_word_2));
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#ff5e3a")), mainPresenter.mMainActivity.getString(R.string.main_hellow_word_1).length()
                ,mainPresenter.mMainActivity.getString(R.string.main_hellow_word_1).length()+
                        DkhomeApplication.mApp.mAccount.name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        hellodword.setText(spannableString);
        lastweightday.setText(TimeUtils.measureDataMc(DkhomeApplication.mApp.weightManager.lastinput.date,mainPresenter.mMainActivity));

        if(DkhomeApplication.mApp.weightManager.weightstate == WeightManager.STATE_SAME) {
            weightState.setImageDrawable(null);
            goalvalue.setTextColor(Color.parseColor("#0082ff"));
            goalvalue.setText(mainPresenter.mMainActivity.getString(R.string.goal_name1));
        }
        else if(DkhomeApplication.mApp.weightManager.weightstate == WeightManager.STATE_DOWN) {

            weightState.setImageResource(R.drawable.down);
            goalvalue.setTextColor(Color.parseColor("#1afa29"));
            goalvalue.setText(mainPresenter.mMainActivity.getString(R.string.goal_name2));
        }
        else {
            weightState.setImageResource(R.drawable.up);
            goalvalue.setTextColor(Color.parseColor("#ff5e3a"));
            goalvalue.setText(mainPresenter.mMainActivity.getString(R.string.goal_name3));
        }
        weighttitlevalue.setText(String.valueOf(DkhomeApplication.mApp.mAccount.lastweight));
        daytitle.setText(TimeUtils.measureDeteForm2(mainPresenter.mMainActivity,day));
        eatvalue.setText(String.valueOf(FoodManager.foodManager.totalCarl));
        int b = DkhomeApplication.mApp.getBuredCarl(day);
        burnedvalue.setText(String.valueOf(b));
        if(DkhomeApplication.mApp.goal.type == Goal.TYPE_LOSE_WEIGHT) {
            int last = EquipData.getBaseCarl(DkhomeApplication.mApp.mAccount)+b
                    -FoodManager.foodManager.totalCarl;
            if(last > 0)
            {
                semicircleProgressView.setSubTile(mainPresenter.mMainActivity.getString(R.string.main_calor_lose_title));
                if(last > DkhomeApplication.mApp.goal.daycarl)
                {
                    semicircleProgressView.setSesameValues(last,
                            DkhomeApplication.mApp.goal.daycarl);
                    semicircleProgressView.setTitle(mainPresenter.mMainActivity.getString(R.string.mian_day_goal_finish));
                    semicircleProgressView.setSubTile("");
                }
                else
                {
                    semicircleProgressView.setSesameValues(DkhomeApplication.mApp.goal.daycarl -last,
                            DkhomeApplication.mApp.goal.daycarl);
                }

            }
            else
            {
                semicircleProgressView.setSesameValues(0,DkhomeApplication.mApp.goal.daycarl-last);
            }
        }
        else if(DkhomeApplication.mApp.goal.type == Goal.TYPE_HEALTH) {
            int totalb = EquipData.getBaseCarl(DkhomeApplication.mApp.mAccount)+b;
            int total =  FoodManager.foodManager.totalCarl;
            if(total > totalb)
            {
                semicircleProgressView.setSubTile(mainPresenter.mMainActivity.getString(R.string.main_calor_lose_title));
                semicircleProgressView.setSesameValues(total-totalb,
                        total);
            }
            else if(total < totalb)
            {
                semicircleProgressView.setSubTile(mainPresenter.mMainActivity.getString(R.string.main_calor_add_title));
                semicircleProgressView.setSesameValues(totalb-total,totalb);
            }
            else
            {
                semicircleProgressView.setSesameValues(0,total);
                semicircleProgressView.setTitle(mainPresenter.mMainActivity.getString(R.string.mian_day_goal_finish));
                semicircleProgressView.setSubTile("");
            }
        }
        else{
            int last =FoodManager.foodManager.totalCarl-
                    EquipData.getBaseCarl(DkhomeApplication.mApp.mAccount)-b;
            if(last > 0)
            {
                semicircleProgressView.setSubTile(mainPresenter.mMainActivity.getString(R.string.main_calor_add1_title));
                if(last > DkhomeApplication.mApp.goal.daycarl)
                {
                    semicircleProgressView.setSesameValues(last,
                            DkhomeApplication.mApp.goal.daycarl);
                    semicircleProgressView.setTitle(mainPresenter.mMainActivity.getString(R.string.mian_day_goal_finish));
                    semicircleProgressView.setSubTile("");

                }
                else
                {
                    semicircleProgressView.setSesameValues(last,
                            DkhomeApplication.mApp.goal.daycarl);
                }

            }
            else
            {
                semicircleProgressView.setSesameValues(0,DkhomeApplication.mApp.goal.daycarl-last);
            }
        }

        initPlans(TimeUtils.getDate());
    }

    public void addView(Course course) {
        LayoutInflater mInflater = (LayoutInflater) mainPresenter.mMainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        course.view = mInflater.inflate(R.layout.plan_item,null);
        TextView title = course.view.findViewById(R.id.name_title);
        TextView time = course.view.findViewById(R.id.time_title);
        TextView during = course.view.findViewById(R.id.during_value);
        TextView videoname = course.view.findViewById(R.id.video_name);
        ImageView imageView = course.view.findViewById(R.id.video);
        RelativeLayout main = course.view.findViewById(R.id.main);
        TextView persent = course.view.findViewById(R.id.persent);
        RelativeLayout btndelete = course.view.findViewById(R.id.btndelete);
        main.setTag(course);
        RoundProgressBar roundProgressBar = course.view.findViewById(R.id.roundProgressBar);
        roundProgressBar.setMax(course.during);
        roundProgressBar.setProgress(course.current);
        persent.setText(String.format("%d",course.current*100/course.during)+"%");
        if(course.videoname.length() > 0)
        {
            videoname.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
            videoname.setText(course.name);
        }
        else
        {
            videoname.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.INVISIBLE);
        }
        title.setText(course.name);
        time.setText(TimeUtils.measureDeteForm2(mainPresenter.mMainActivity, course.creat));
        during.setText(String.valueOf(course.during/60)+" min");
        main.setOnClickListener(planListener);
        btndelete.setTag(course);
        btndelete.setOnClickListener(deleteListener);
        planList.addView(course.view);
    }


    public void updataView(Course course1) {
        LayoutInflater mInflater = (LayoutInflater) mainPresenter.mMainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Course course = DkhomeApplication.mApp.sportDataManager.allPlans.get(course1.oid);
        boolean isnew = false;
        if(course.view == null)
        {
            isnew = true;
            course.view = mInflater.inflate(R.layout.plan_item,null);
        }

        TextView title = course.view.findViewById(R.id.name_title);
        TextView time = course.view.findViewById(R.id.time_title);
        TextView during = course.view.findViewById(R.id.during_value);
        TextView videoname = course.view.findViewById(R.id.video_name);
        TextView persent = course.view.findViewById(R.id.persent);
        ImageView imageView = course.view.findViewById(R.id.video);
        RelativeLayout main = course.view.findViewById(R.id.main);
        RelativeLayout btndelete = course.view.findViewById(R.id.btndelete);
        main.setTag(course);
        RoundProgressBar roundProgressBar = course.view.findViewById(R.id.roundProgressBar);
        roundProgressBar.setMax(course.during);
        roundProgressBar.setProgress(course.current);
        String text = String.format("%d",course.current*100/course.during)+"%";
        persent.setText(text);
        if(course.videoname.length() > 0)
        {
            videoname.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
            videoname.setText(course.name);
        }
        else
        {
            videoname.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.INVISIBLE);
        }
        title.setText(course.name);
        time.setText(TimeUtils.measureDeteForm2(mainPresenter.mMainActivity, course.creat));
        during.setText(String.valueOf(course.during/60)+" min");
        main.setOnClickListener(planListener);
        btndelete.setTag(course);
        btndelete.setOnClickListener(deleteListener);
        if(isnew)
        planList.addView(course.view);
    }

    public void deleteView(Course course1) {

        Course course = DkhomeApplication.mApp.sportDataManager.allPlans.get(course1.oid);
        planList.removeView(course.view);
    }

    private void init() {
        LayoutInflater mInflater = (LayoutInflater) mainPresenter.mMainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = mInflater.inflate(R.layout.fragment_diary,null);
        btnmenu = view.findViewById(R.id.menu);
        headimg = view.findViewById(R.id.headimg);
        hellodword = view.findViewById(R.id.hellodword);
        goalvalue = view.findViewById(R.id.goalvalue);
        sex = view.findViewById(R.id.sex);
        weighttitlevalue = view.findViewById(R.id.weightvalue);
        btngoal = view.findViewById(R.id.goalbtn);
        btnweight = view.findViewById(R.id.weightbtn);
        daytitle = view.findViewById(R.id.daytitle);
        btnleft = view.findViewById(R.id.left);
        btnright = view.findViewById(R.id.right);
        eatvalue = view.findViewById(R.id.eatvalue);
        burnedvalue = view.findViewById(R.id.burnedvalue);
        planList = view.findViewById(R.id.planlist);
        addCoures = view.findViewById(R.id.addbtn);
        lastweightday = view.findViewById(R.id.weighttitlevalue);
        weightState = view.findViewById(R.id.weightstate);
        semicircleProgressView = view.findViewById(R.id.carprogress);
        headimg.setOnClickListener(headListener);
        addCoures.setOnClickListener(newCourseListener);
        btngoal.setOnClickListener(mainPresenter.startGoalListener);
        btnweight.setOnClickListener(mainPresenter.startHealListener);
        eatvalue.setOnClickListener(mainPresenter.startEatListener);
        burnedvalue.setOnClickListener(mainPresenter.startSportdataListener);
        day = TimeUtils.getDate();
    }

    private void initPlans(String day) {
        ArrayList<Course> courses = DkhomeApplication.mApp.sportDataManager.dayPlans.get(day);
        if(courses != null)
        {
            for(int i = 0; i < courses.size() ; i++)
            {
                addView(courses.get(i));
            }
        }

    }


    private View.OnClickListener headListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ArrayList<MenuItem> items = new ArrayList<MenuItem>();
            MenuItem menuItem = new MenuItem();
            menuItem.btnName = mainPresenter.mMainActivity.getString(R.string.button_word_takephoto);
            menuItem.mListener = mTakePhotoListenter;
            items.add(menuItem);
            menuItem = new MenuItem();
            menuItem.btnName = mainPresenter.mMainActivity.getString(R.string.button_word_album);
            menuItem.mListener = mAddPicListener;
            items.add(menuItem);
            mainPresenter.popupWindow = AppUtils.creatButtomMenu(mainPresenter.mMainActivity,mainPresenter.mShade,items,mainPresenter.mMainActivity.findViewById(R.id.activity_root));
        }
    };

    private View.OnClickListener mAddPicListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            DkhomeApplication.mApp.mFileUtils.selectPhotos(mainPresenter.mMainActivity, RegisterActivity.CHOSE_PICTURE_HEAD);
            mainPresenter.popupWindow.dismiss();
        }
    };

    private View.OnClickListener mTakePhotoListenter = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            DkhomeApplication.mApp.mFileUtils.checkPermissionTakePhoto(mainPresenter.mMainActivity, DkhomeApplication.mApp.mFileUtils.pathUtils.getfilePath("head/photo"),RegisterActivity.TAKE_PHOTO_HEAD);
            mainPresenter.popupWindow.dismiss();
        }

    };

    private View.OnClickListener newCourseListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mainPresenter.mMainActivity, NewPlanActivity.class);
            Course course = new Course();
            course.during = NewPlanPresenter.COURSE_TINME_HIT;
            course.creat = TimeUtils.getDate();
            intent.putExtra("plan",course);
            mainPresenter.mMainActivity.startActivity(intent);
        }
    };

    private View.OnClickListener planListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mainPresenter.mMainActivity, SportDetialActivity.class);
            Course course = (Course) v.getTag();
            intent.putExtra("plan",course);
            mainPresenter.mMainActivity.startActivity(intent);
        }
    };

    private View.OnClickListener deleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Course course = (Course) v.getTag();
            DkhomeApplication.mApp.deletePlan(course);

        }
    };
}
