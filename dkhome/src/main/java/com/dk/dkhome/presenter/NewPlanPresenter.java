package com.dk.dkhome.presenter;


import android.content.Intent;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dk.dkhome.R;
import com.dk.dkhome.database.DBHelper;
import com.dk.dkhome.entity.Course;
import com.dk.dkhome.handler.NewPlanHandler;
import com.dk.dkhome.receiver.NewPlanReceiver;
import com.dk.dkhome.view.DkhomeApplication;
import com.dk.dkhome.view.InputView;
import com.dk.dkhome.view.activity.NewPlanActivity;

import java.io.File;

import intersky.appbase.Presenter;
import intersky.appbase.entity.Attachment;
import intersky.apputils.TimeUtils;


public class NewPlanPresenter implements Presenter {

    public static final String ACTION_SET_VIDEO = "ACTION_SET_VIDEO";
    public static final int COURSE_TINME_HIT = 20*60;
    public NewPlanActivity mNewPlanActivity;
    public NewPlanReceiver newPlanReceiver;
    public NewPlanHandler newPlanHandler;
    public Attachment video;
    public Course course;
    public InputView timeInput;
    public EditText name;
    public RelativeLayout btnTime;
    public RelativeLayout btnVideo;
    public RelativeLayout shade;
    public RelativeLayout videoView;
    public ImageView btnDeleteVideo;
    public ImageView btnReplaceVideo;
    public TextView videoName;
    public TextView time;
    public TextView btnSave;

    public NewPlanPresenter(NewPlanActivity mNewPlanActivity) {
        this.mNewPlanActivity = mNewPlanActivity;
        this.newPlanHandler = new NewPlanHandler(mNewPlanActivity);
        this.newPlanReceiver = new NewPlanReceiver(newPlanHandler);
        mNewPlanActivity.setBaseReceiver(this.newPlanReceiver);
    }

    @Override
    public void initView() {
        // TODO Autonerated method stub
        mNewPlanActivity.setContentView(R.layout.activity_newplan);
        ImageView back = mNewPlanActivity.findViewById(R.id.back);
        back.setOnClickListener(mNewPlanActivity.mBackListener);
        course = mNewPlanActivity.getIntent().getParcelableExtra("plan");
        name = mNewPlanActivity.findViewById(R.id.name_value);
        shade = mNewPlanActivity.findViewById(R.id.shade);
        btnTime = mNewPlanActivity.findViewById(R.id.btn_time);
        time = mNewPlanActivity.findViewById(R.id.timevalue);
        btnVideo = mNewPlanActivity.findViewById(R.id.btnvideo);
        videoView = mNewPlanActivity.findViewById(R.id.videoview);
        btnDeleteVideo = mNewPlanActivity.findViewById(R.id.deletevideo);
        btnReplaceVideo = mNewPlanActivity.findViewById(R.id.replacevideo);
        btnSave = mNewPlanActivity.findViewById(R.id.btn_finish);
        videoName = mNewPlanActivity.findViewById(R.id.videotext);
        btnTime.setOnClickListener(setTimeListener);
        btnVideo.setOnClickListener(setVideoListener);
        btnDeleteVideo.setOnClickListener(deleteVideoListener);
        btnReplaceVideo.setOnClickListener(replaceVideoListener);
        btnSave.setOnClickListener(saveListener);
        name.setText(course.name);
        time.setText(String.valueOf(course.during/60));
        timeInput = new InputView(mNewPlanActivity,doOkListener, InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL,"20");
        initData();
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


    public void setVideo(Intent intent) {
        video = intent.getParcelableExtra("attachment");
        if(btnVideo.getVisibility() == View.VISIBLE)
        {
            btnVideo.setVisibility(View.INVISIBLE);
        }
        videoView.setVisibility(View.VISIBLE);
        videoName.setText(video.mName);
    }

    public void initData() {
        if(course.path.length() > 0)
        {
            File file = new File(course.path);
            if(file.exists())
            {
                btnVideo.setVisibility(View.INVISIBLE);
                videoView.setVisibility(View.VISIBLE);
                videoName.setText(course.videoname);
                video = new Attachment();
                video.mName = course.videoname;
                video.mSize = file.length();
                video.mPath = course.path;
            }
        }
        if(course.name.length() > 0)
        name.setText(course.name);

        if(course.during > 0)
        time.setText(String.valueOf(course.during/60));
    }

    private View.OnClickListener setTimeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            timeInput.creat(mNewPlanActivity,shade,mNewPlanActivity.findViewById(R.id.activity_root),time.getText().toString());
        }
    };

    private InputView.DoOkListener doOkListener = new InputView.DoOkListener() {
        @Override
        public void OkListener() {
            time.setText(timeInput.getString());
        }
    };

    private View.OnClickListener setVideoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DkhomeApplication.mApp.mFileUtils.getSignalVideo(mNewPlanActivity,false,"com.dk.dkhome.view.activity.NewPlanActivity",ACTION_SET_VIDEO);
        }
    };

    private View.OnClickListener deleteVideoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            video = null;
            if(videoView.getVisibility() == View.VISIBLE)
            {
                videoView.setVisibility(View.INVISIBLE);
            }
            btnVideo.setVisibility(View.VISIBLE);
        }
    };

    private View.OnClickListener replaceVideoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DkhomeApplication.mApp.mFileUtils.getSignalVideo(mNewPlanActivity,false,"com.dk.dkhome.view.activity.NewPlanActivity",ACTION_SET_VIDEO);
        }
    };

    private View.OnClickListener saveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean add = false;
            if(course.name.length() == 0)
            {
                add = true;
            }
            course.name = name.getText().toString();
            if(course.name.length() == 0)
            {
                course.name = name.getHint().toString();
            }
            course.during = Integer.valueOf(time.getText().toString())*60;
            if(video != null)
            {
                course.videoname = video.mName;
                course.path = video.mPath;
            }
            course.userid = DkhomeApplication.mApp.mAccount.uid;
            if(add)
            DkhomeApplication.mApp.addPlan(course);
            else
                DkhomeApplication.mApp.updataPlan(course);

            mNewPlanActivity.finish();
        }
    };
}
