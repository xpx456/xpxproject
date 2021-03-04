package com.dk.dkphone.presenter;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dk.dkphone.R;
import com.dk.dkphone.database.DBHelper;
import com.dk.dkphone.entity.DecimalInputTextWatcher;
import com.dk.dkphone.entity.UserWeight;
import com.dk.dkphone.handler.UserHandler;
import com.dk.dkphone.receiver.MainReceiver;
import com.dk.dkphone.view.DkPhoneApplication;
import com.dk.dkphone.view.activity.MainActivity;
import com.dk.dkphone.view.activity.UserActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import intersky.appbase.MySimpleTarget;
import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.apputils.GlideApp;
import intersky.apputils.TimeUtils;
import intersky.echartoption.ArrayData;
import intersky.echartoption.ObjectData;

public class UserPresenter implements Presenter {

    public UserActivity mUserActivity;
    public UserHandler userHandler;
    public ArrayList<UserWeight> userWeights = new ArrayList<UserWeight>();
    public HashMap<String,UserWeight> hashMap = new HashMap<String,UserWeight>();
    public boolean ismale = true;
    public UserPresenter(UserActivity UserActivity) {
        mUserActivity = UserActivity;
        userHandler = new UserHandler(mUserActivity);
    }

    @Override
    public void initView() {

        mUserActivity.flagFillBack = false;
        mUserActivity.setContentView(R.layout.activity_user);
        mUserActivity.user = mUserActivity.getIntent().getParcelableExtra("user");
        mUserActivity.title = mUserActivity.findViewById(R.id.title);
        mUserActivity.close = mUserActivity.findViewById(R.id.close);
        mUserActivity.agevalue = mUserActivity.findViewById(R.id.agevalue);
        mUserActivity.namevalue = mUserActivity.findViewById(R.id.namevalue);
        mUserActivity.tollvalue = mUserActivity.findViewById(R.id.tollvalue);
        mUserActivity.weightvalue = mUserActivity.findViewById(R.id.weightvalue);
        mUserActivity.btnfemale = mUserActivity.findViewById(R.id.femalebtn);
        mUserActivity.female = mUserActivity.findViewById(R.id.female_img);
        mUserActivity.btnmale = mUserActivity.findViewById(R.id.malebtn);
        mUserActivity.male = mUserActivity.findViewById(R.id.male_img);
        mUserActivity.male.setImageResource(R.drawable.bselect);
        mUserActivity.headicon = mUserActivity.findViewById(R.id.headimg);
        mUserActivity.btnsave = mUserActivity.findViewById(R.id.btnsave);
        mUserActivity.btndelete= mUserActivity.findViewById(R.id.btndelete);
        mUserActivity.chartShow = mUserActivity.findViewById(R.id.chart);
        mUserActivity.chartShow.getSettings().setAllowFileAccess(true);
        mUserActivity.chartShow.getSettings().setJavaScriptEnabled(true);
        mUserActivity.chartShow.setWebViewClient(mWebViewClient);
        mUserActivity.chartShow.setVisibility(View.VISIBLE);
        mUserActivity.chartShow.loadUrl("file:///android_asset/echart/myechart.html");
        mUserActivity.weightvalue.addTextChangedListener(new DecimalInputTextWatcher(mUserActivity.weightvalue,1));
        mUserActivity.headicon.setOnClickListener(setHeadListener);
        mUserActivity.btnsave.setOnClickListener(saveListener);
        mUserActivity.btndelete.setOnClickListener(deleteListener);
        mUserActivity.root = mUserActivity.findViewById(R.id.root);
        mUserActivity.root.setFocusable(true);
        mUserActivity.root.setFocusableInTouchMode(true);
        mUserActivity.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mUserActivity.finish();
            }
        });
        mUserActivity.all = mUserActivity.findViewById(R.id.allcontent);
        mUserActivity.all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        mUserActivity.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserActivity.finish();
            }
        });
        initData();
        mUserActivity.btnmale.setOnClickListener(maleLisetner);
        mUserActivity.btnfemale.setOnClickListener(femaleLisetner);
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
        mUserActivity.chartShow.destroy();
    }

    public void initData()
    {
        iniop();
        if(mUserActivity.user.name.length() == 0)
        {
            mUserActivity.title.setText(mUserActivity.getString(R.string.new_user));
            mUserActivity.btndelete.setVisibility(View.GONE);
        }
        else
        {
            DBHelper.getInstance(mUserActivity).scanUserWeight(mUserActivity.user.uid,userWeights,hashMap);
            mUserActivity.title.setText(mUserActivity.user.name);
            mUserActivity.namevalue.setText(mUserActivity.user.name);
            mUserActivity.tollvalue.setText(mUserActivity.user.toll);
            mUserActivity.agevalue.setText(mUserActivity.user.age);
            if(userWeights.size() > 0)
            mUserActivity.weightvalue.setText(userWeights.get(0).weight);
            if(mUserActivity.user.sex.equals(mUserActivity.getString(R.string.male)))
            {
                ismale = true;
                mUserActivity.female.setImageResource(R.drawable.bunselect);
                mUserActivity.male.setImageResource(R.drawable.bselect);
            }
            else
            {
                ismale = false;
                mUserActivity.male.setImageResource(R.drawable.bunselect);
                mUserActivity.female.setImageResource(R.drawable.bselect);
            }
            if(mUserActivity.user.headpath.length() > 0)
            {
                File file = new File(mUserActivity.user.headpath);
                if(file.exists())
                {
                    RequestOptions options = new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL).override(80,80);
                    GlideApp.with(mUserActivity).load(file.getPath()).apply(options).
                            into(new MySimpleTarget(mUserActivity.headicon));
                }
            }
        }
        initWeightData();
    }

    public void iniop(){
        mUserActivity.option = new ObjectData();
        ObjectData grid = new ObjectData();
        mUserActivity.option.put("grid",grid);
        grid.put("left","1%");
        grid.put("right","1%");
        grid.put("containLabel",true);
        ArrayData datazoom = new ArrayData();
        mUserActivity.option.put("dataZoom",datazoom);
        ObjectData zoom1 = new ObjectData();
        datazoom.add(zoom1);
        zoom1.put("type","slider");
        zoom1.put("start",10);
        zoom1.put("end",60);
        ObjectData zoom2 = new ObjectData();
        datazoom.add(zoom2);
        zoom2.put("type","inside");
        zoom2.put("start",10);
        zoom2.put("end",60);

        ObjectData title = new ObjectData();
        mUserActivity.option.put("title",title);
        title.put("text",mUserActivity.getString(R.string.weight_chart_title));

        ObjectData xAxis = new ObjectData();
        mUserActivity.option.put("xAxis",xAxis);
        xAxis.put("type","category");
        mUserActivity.x = new ArrayData();
        xAxis.put("data",mUserActivity.x);
        ObjectData yAxis = new ObjectData();
        mUserActivity.option.put("yAxis",yAxis);
        yAxis.put("type","value");
        ObjectData al = new ObjectData();
        al.put("formatter","{value} KG");
        yAxis.put("axisLabel",al);
        ArrayData series = new ArrayData();
        mUserActivity.option.put("series",series);
        ObjectData objectData1 = new ObjectData();
        series.add(objectData1);
        mUserActivity.y = new ArrayData();
        objectData1.put("data",mUserActivity.y);
        objectData1.put("type","line");
        objectData1.put("smooth",true);
    }

    public void initWeightData() {
        for(int i = userWeights.size()-1 ; i >= 0 ; i--)
        {
            UserWeight weight = userWeights.get(i);
            mUserActivity.x.add(weight.date);
            mUserActivity.y.add(Double.valueOf(Double.valueOf(weight.weight)));
        }
        userHandler.sendEmptyMessageDelayed(UserHandler.UPDATA_WEIGHT_CHART,300);
    }

    public void updataChart()
    {
        String url = "javascript:createChart('orther'," + mUserActivity.option.toString()+");";
        mUserActivity.chartShow.loadUrl(url);
    }

    public View.OnClickListener maleLisetner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(ismale == false)
            {
                ismale = true;
                mUserActivity.female.setImageResource(R.drawable.bunselect);
                mUserActivity.male.setImageResource(R.drawable.bselect);
            }
        }
    };

    public View.OnClickListener femaleLisetner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(ismale == true)
            {
                ismale = false;
                mUserActivity.male.setImageResource(R.drawable.bunselect);
                mUserActivity.female.setImageResource(R.drawable.bselect);
            }
        }
    };


    public View.OnClickListener setHeadListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            DkPhoneApplication.mApp.fileUtils.checkPermissionTakePhoto(mUserActivity,
                    DkPhoneApplication.mApp.fileUtils.pathUtils.getfilePath("head/photo"),
                    MainActivity.TAKE_PHOTO_HEAD);
        }
    };

    public View.OnClickListener saveListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(mUserActivity.namevalue.getText().toString().length() == 0)
            {
                AppUtils.showMessage(mUserActivity,mUserActivity.getString(R.string.user_name_empty));
                return;
            }
            mUserActivity.user.name = mUserActivity.namevalue.getText().toString();
            mUserActivity.user.toll = mUserActivity.tollvalue.getText().toString();
            mUserActivity.user.age = mUserActivity.namevalue.getText().toString();
            if(ismale)
            mUserActivity.user.sex = mUserActivity.getString(R.string.male);
            else
                mUserActivity.user.sex = mUserActivity.getString(R.string.female);
            if(mUserActivity.head != null)
            {
                if(mUserActivity.head.exists())
                {
                    if(mUserActivity.user.headpath.length() > 0)
                    {
                        File file = new File(mUserActivity.user.headpath);
                        if(file.exists())
                        {
                            file.delete();
                        }
                    }
                    mUserActivity.user.headpath = mUserActivity.head.getPath();
                }
            }

            if(mUserActivity.weightvalue.getText().toString().length() > 0)
            {
                UserWeight userWeight = hashMap.get(TimeUtils.getDate());
                if(userWeight == null)
                 userWeight = new UserWeight();
                userWeight.weight = mUserActivity.weightvalue.getText().toString();
                userWeight.uid = mUserActivity.user.uid;
                userWeight.date = TimeUtils.getDate();
                DBHelper.getInstance(mUserActivity).addUserWeight(userWeight);
            }
            DBHelper.getInstance(mUserActivity).addUser(mUserActivity.user);
            Intent intent = new Intent(MainReceiver.ACTION_UPDTATA_HEAD);
            intent.putExtra("user",mUserActivity.user);
            intent.putExtra("delete",false);
            mUserActivity.sendBroadcast(intent);
            mUserActivity.finish();
        }
    };


    public View.OnClickListener deleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DBHelper.getInstance(mUserActivity).deleteUser(mUserActivity.user);
            DBHelper.getInstance(mUserActivity).deleteUserWeight(mUserActivity.user);
            Intent intent = new Intent(MainReceiver.ACTION_UPDTATA_HEAD);
            intent.putExtra("user",mUserActivity.user);
            intent.putExtra("delete",true);
            mUserActivity.sendBroadcast(intent);
            mUserActivity.finish();
        }
    };

    public WebViewClient mWebViewClient = new WebViewClient() {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            // 当有新连接时使用当前的webview进行显示
            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }

        // 开始加载网页时要做的工作
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            view.setVisibility(View.INVISIBLE);
            mUserActivity.waitDialog.show();
            super.onPageStarted(view, url, favicon);
        }

        // 加载完成时要做的工作
        public void onPageFinished(WebView view, String url) {
            view.setVisibility(View.VISIBLE);
            mUserActivity.waitDialog.hide();
//            if (mUserActivity.isFirst == false) {
//                mUserActivity.isFirst = true;
//                //mChartActivity.waitDialog.show();
//            }
            //ChartUtils.loadBarChart(mChartActivity.chartData,mChartActivity.chartShow);
            super.onPageFinished(view, url);

        }

        // 加载错误时要做的工作
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            mUserActivity.waitDialog.hide();
        }
    };

    public void takePhotoResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MainActivity.TAKE_PHOTO_HEAD:
            case MainActivity.TAKE_PHOTO_BG:
                if (resultCode == Activity.RESULT_OK) {
                    File file = new File(DkPhoneApplication.mApp.fileUtils.takePhotoPath);
                    String name = file.getName().substring(0, file.getName().lastIndexOf("."));
                    if (file.exists()) {
                        if (requestCode == MainActivity.TAKE_PHOTO_HEAD) {
                            // 开启裁剪,设置requestCode为CROP_PHOTO
                            DkPhoneApplication.mApp.fileUtils.cropPhoto(mUserActivity, 1, 1, file.getPath(),
                                    DkPhoneApplication.mApp.fileUtils.getOutputHeadMediaFileUri(
                                            DkPhoneApplication.mApp.fileUtils.pathUtils.getfilePath("head/photo"), name).getPath()
                                    , (int) (80 * mUserActivity.mBasePresenter.mScreenDefine.density),
                                    (int) (80 * mUserActivity.mBasePresenter.mScreenDefine.density), MainActivity.CROP_HEAD);
                        } else {
                            // 开启裁剪,设置requestCode为CROP_PHOTO
                            DkPhoneApplication.mApp.fileUtils.cropPhoto(mUserActivity, 9, 5, file.getPath(), DkPhoneApplication.mApp.fileUtils.getOutputBgMediaFileUri(DkPhoneApplication.mApp.fileUtils.pathUtils.getfilePath("bg/photo"), name).getPath()
                                    , mUserActivity.mBasePresenter.mScreenDefine.ScreenWidth, (int) (200 * mUserActivity.mBasePresenter.mScreenDefine.density), MainActivity.CROP_BG);
                        }
                    }
                }
                break;
            case MainActivity.CHOSE_PICTURE_HEAD:
            case MainActivity.CHOSE_PICTURE_BG:
                if (resultCode == Activity.RESULT_OK) {
                    String path = AppUtils.getFileAbsolutePath(mUserActivity, data.getData());
                    File file = new File(path);
                    String name = file.getName().substring(0, file.getName().lastIndexOf("."));
                    if (file.exists()) {
                        // 设置可缩放
                        if (requestCode == MainActivity.CHOSE_PICTURE_HEAD) {
                            // 开启裁剪,设置requestCode为CROP_PHOTO
                            DkPhoneApplication.mApp.fileUtils.cropPhoto(mUserActivity, 1, 1, path, DkPhoneApplication.mApp.fileUtils.getOutputHeadMediaFileUri(DkPhoneApplication.mApp.fileUtils.pathUtils.getfilePath("head/photo"), name).getPath()
                                    , (int) (60 * mUserActivity.mBasePresenter.mScreenDefine.density), (int) (60 * mUserActivity.mBasePresenter.mScreenDefine.density), MainActivity.CROP_HEAD);
                        } else {
                            // 开启裁剪,设置requestCode为CROP_PHOTO
                            DkPhoneApplication.mApp.fileUtils.cropPhoto(mUserActivity, 9, 5, path, DkPhoneApplication.mApp.fileUtils.getOutputBgMediaFileUri(DkPhoneApplication.mApp.fileUtils.pathUtils.getfilePath("bg/photo"), name).getPath()
                                    , mUserActivity.mBasePresenter.mScreenDefine.ScreenWidth, (int) (200 * mUserActivity.mBasePresenter.mScreenDefine.density), MainActivity.CROP_BG);

                        }
                    }
                }
                break;
            case MainActivity.CROP_HEAD:
                if (resultCode == Activity.RESULT_OK) {
                    ArrayList<String> paths = DkPhoneApplication.mApp.fileUtils.getImage(data);
                    File mFile = new File(paths.get(0));
//                    LoginAsks.setUploadHead(mUserActivity,mUserInfoHandler,mFile);
                    if(mUserActivity.head != null)
                    {
                        if(mUserActivity.head.exists())
                        {
                            mUserActivity.head.delete();
                        }
                    }
                    mUserActivity.head = mFile;
                    if(mUserActivity.head.exists())
                    {
                        RequestOptions options = new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.ALL).override(80,80);
                        GlideApp.with(mUserActivity).load(mUserActivity.head.getPath()).apply(options).
                                into(new MySimpleTarget(mUserActivity.headicon));
                    }
                }
                break;

        }
    }
}
