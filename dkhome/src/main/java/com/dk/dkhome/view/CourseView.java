package com.dk.dkhome.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dk.dkhome.R;
import com.dk.dkhome.asks.CourseAsks;
import com.dk.dkhome.entity.Course;
import com.dk.dkhome.presenter.MainPresenter;
import com.dk.dkhome.presenter.MainPresenter;
import com.dk.dkhome.view.activity.SportDetialActivity;
import com.dk.dkhome.view.adapter.CourseAdapter;
import com.dk.dkhome.view.adapter.NetCourseAdapter;
import com.google.gson.JsonObject;

import org.json.JSONException;

import java.util.ArrayList;

import intersky.apputils.AppUtils;
import intersky.apputils.TimeUtils;
import intersky.json.XpxJSONArray;
import intersky.json.XpxJSONObject;
import intersky.xpxnet.net.NetObject;

public class CourseView {

    public MainPresenter mMainPresenter;
    public View view;
    public RecyclerView recyclerView;
    public NetCourseAdapter courseAdapter;
    public ArrayList<Course> courses = new ArrayList<Course>();
    public CourseView(MainPresenter mMainPresenter) {
        this.mMainPresenter = mMainPresenter;
        init();
    }

    private void init() {
        LayoutInflater mInflater = (LayoutInflater) mMainPresenter.mMainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = mInflater.inflate(R.layout.fragment_course,null);
        recyclerView = view.findViewById(R.id.courselist);
        recyclerView.setLayoutManager(new LinearLayoutManager(mMainPresenter.mMainActivity));
        courseAdapter = new NetCourseAdapter(courses,mMainPresenter.mMainActivity);
        recyclerView.setAdapter(courseAdapter);
        courseAdapter.setOnItemClickListener(onItemClickListener);
        CourseAsks.getCourse(mMainPresenter.mMainActivity,mMainPresenter.mMainHandler);

    }

    private NetCourseAdapter.OnItemClickListener onItemClickListener = new NetCourseAdapter.OnItemClickListener(){

        @Override
        public void onItemClick(Course course, int position, View view) {
            Course course1 = new Course();
            course1.setPlan(course);
            course1.oid = AppUtils.getguid();
            course1.during = TimeUtils.praseTimeToSecond(course.time);
            course1.creat = TimeUtils.getDate();
            course1.userid = DkhomeApplication.mApp.mAccount.uid;
            Course same = DkhomeApplication.mApp.sportDataManager.getSameCourse(course1);
            if(same != null)
            {
                AppUtils.creatDialogTowButton(mMainPresenter.mMainActivity,mMainPresenter.mMainActivity.getString(R.string.course_same_message),""
                ,mMainPresenter.mMainActivity.getString(R.string.button_word_continue),mMainPresenter.mMainActivity.getString(R.string.button_new),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(mMainPresenter.mMainActivity, SportDetialActivity.class);
                                intent.putExtra("plan",same);
                                mMainPresenter.mMainActivity.startActivity(intent);
                            }
                        },new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DkhomeApplication.mApp.addPlan(course1);
                                Intent intent = new Intent(mMainPresenter.mMainActivity, SportDetialActivity.class);
                                intent.putExtra("plan",course1);
                                mMainPresenter.mMainActivity.startActivity(intent);
                            }
                        });
            }
            else
            {
                DkhomeApplication.mApp.addPlan(course1);
                Intent intent = new Intent(mMainPresenter.mMainActivity, SportDetialActivity.class);
                intent.putExtra("plan",course1);
                mMainPresenter.mMainActivity.startActivity(intent);
            }
        }
    };


    public void praseCourse(NetObject net) {
        String json = net.result;
        try {
            XpxJSONObject jsonObject= new XpxJSONObject(json);
            XpxJSONArray ja = jsonObject.getJSONArray("data");
            for(int i = 0 ; i < ja.length() ; i++)
            {
                XpxJSONObject jo = ja.getJSONObject(i);
                Course course = new Course();
                course.cid = jo.getString("cid");
                course.name = jo.getString("cname");
                course.img = jo.getString("img");
                course.url = jo.getString("video");
                course.time = jo.getString("time");
                //course.during = Integer.valueOf(jo.getString("during"));
                course.creat = TimeUtils.getDate();
                courses.add(course);
            }
            if(courseAdapter != null)
            courseAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void prseTimeToDuring(String time) {

    }
}
