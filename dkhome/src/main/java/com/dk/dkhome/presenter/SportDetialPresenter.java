package com.dk.dkhome.presenter;


import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.dk.dkhome.database.DBHelper;
import com.dk.dkhome.entity.Course;
import com.dk.dkhome.entity.Device;
import com.dk.dkhome.entity.EquipData;
import com.dk.dkhome.handler.SportDetialHandler;
import com.dk.dkhome.receiver.SportDetialReceiver;
import com.dk.dkhome.utils.TestManager;
import com.dk.dkhome.view.ChartView;
import com.dk.dkhome.view.DeviceView;
import com.dk.dkhome.view.DkhomeApplication;
import com.dk.dkhome.view.ProgressView;
import com.dk.dkhome.view.SportView;
import com.dk.dkhome.view.activity.MainActivity;
import com.dk.dkhome.view.activity.SportDetialActivity;
import com.dk.dkhome.R;
import com.dk.dkhome.view.adapter.SportPageAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import intersky.appbase.Presenter;
import intersky.json.XpxJSONObject;
import intersky.mywidget.NoScrollViewPager;
import intersky.mywidget.conturypick.DbHelper;
import intersky.select.SelectManager;
import intersky.select.view.adapter.CustomSelectAdapter;

public class SportDetialPresenter implements Presenter {

    public static final int HID_BAR_TIME = 6000;
    public SportDetialActivity mSportDetialActivity;
    public SportDetialHandler mSportDetialHandler;
    public SurfaceView surfaceView;
    public SurfaceHolder surfaceHolder;
    public MediaPlayer mediaPlayer;
    public Course course;
    public NoScrollViewPager viewPager;
    public ImageView tabimgchart;
    public ImageView tabimgsport;
    public ImageView tabimgdevice;
    public TextView tabtxtchart;
    public TextView tabtxtsport;
    public TextView tabtxtdevice;
    public RelativeLayout btnChart;
    public RelativeLayout btnSport;
    public RelativeLayout btnDevice;
    public ChartView chartView;
    public SportView sportView;
    public DeviceView deviceView;
    public SportPageAdapter sportPageAdapter;
    public RelativeLayout tochLauer;
    public RelativeLayout bar1;
    public RelativeLayout bar2;
    public RelativeLayout top1;
    public RelativeLayout top2;
    public ImageView btnplay1;
    public ImageView btnplay2;
    public TextView time1;
    public TextView time2;
    public ImageView fullScreen;
    public ImageView smallScreen;
    public TextView title1;
    public TextView title2;
    public ImageView back1;
    public ImageView back2;
    public SeekBar seekBar1;
    public SeekBar seekBar2;
    public View waitDialog;
    public ProgressView progressView;
    public RelativeLayout shade;
    public RelativeLayout operbar;
    public TextView fspeed;
    public TextView fcarl;
    public TextView fselect;
    public TextView fdis;
    public TextView ftime;
    public ImageView fadd;
    public ImageView fdes;
    public CustomSelectAdapter customSelectAdapter;
    public SportDetialPresenter(SportDetialActivity SportDetialActivity) {
        mSportDetialActivity = SportDetialActivity;
        mSportDetialHandler = new SportDetialHandler(SportDetialActivity);
        mSportDetialActivity.setBaseReceiver(new SportDetialReceiver(mSportDetialHandler));
    }

    @Override
    public void initView() {
        mSportDetialActivity.flagFillBack = false;
        mSportDetialActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mSportDetialActivity.setContentView(R.layout.activity_sport_detial);
        operbar = mSportDetialActivity.findViewById(R.id.operbar);
        fspeed = mSportDetialActivity.findViewById(R.id.fspeedvalue);
        fselect = mSportDetialActivity.findViewById(R.id.fselect);
        fcarl = mSportDetialActivity.findViewById(R.id.fcarlvalue);
        fdis = mSportDetialActivity.findViewById(R.id.fdisvalue);
        ftime = mSportDetialActivity.findViewById(R.id.ftimevalue);
        fadd = mSportDetialActivity.findViewById(R.id.fadd);
        fdes = mSportDetialActivity.findViewById(R.id.fdes);
        customSelectAdapter = new CustomSelectAdapter(mSportDetialActivity,EquipData.equipData.names);
        course = mSportDetialActivity.getIntent().getParcelableExtra("plan");
        shade = mSportDetialActivity.findViewById(R.id.shade);
        DkhomeApplication.mApp.testManager.sendData.add(sendData);
        fadd.setOnClickListener(addListener);
        fdes.setOnClickListener(desListener);
        initVideo();
        initTab();
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
        if(mediaPlayer!=null){
            if(mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
        DkhomeApplication.mApp.testManager.sendData.remove(sendData);
        DkhomeApplication.mApp.updataPlan(course);
    }

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
            super.onPageStarted(view, url, favicon);
        }

        // 加载完成时要做的工作
        public void onPageFinished(WebView view, String url) {
            view.setVisibility(View.VISIBLE);
            super.onPageFinished(view, url);

        }

        // 加载错误时要做的工作
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

        }
    };

    public void scanFinish(Intent intent) {
        Bundle bundle = intent.getExtras();
        String json = bundle.getString("result");
        try {
            XpxJSONObject jsonObject = new XpxJSONObject(json);
            String address = jsonObject.getString("deviceAddress");
            String name = jsonObject.getString("deviceName");
            deviceView.device.deviceName = name;
            deviceView.device.deviceMac = address;
            SelectManager.getInstance().startCustomSelectView(mSportDetialActivity,
                    customSelectAdapter,null,"" ,
                    DeviceView.ACTION_UPDTAT_DEVICE_TYPE,false,false );


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void updataDeviceState()
    {
        deviceView.updataView();
        sportView.updataView();
    }

    public void hidNavBar() {
        Configuration mConfiguration = mSportDetialActivity.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向
        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {
            if (top2.getVisibility() == View.VISIBLE) {
                bar2.setVisibility(View.INVISIBLE);
                top2.setVisibility(View.INVISIBLE);
            }
        } else {
            if (top1.getVisibility() == View.VISIBLE) {
                bar1.setVisibility(View.INVISIBLE);
                top1.setVisibility(View.INVISIBLE);
            }

        }
    }

    public void updataImf() {
        time1.setText(praseTime());
        time2.setText(praseTime());
        if(mediaPlayer != null)
        {
            seekBar1.setProgress(mediaPlayer.getCurrentPosition()/1000);
            seekBar2.setProgress(mediaPlayer.getCurrentPosition()/1000);
        }
        mSportDetialHandler.sendEmptyMessageDelayed(SportDetialHandler.UPDTATA_IMF,1000);
    }

    public void upDataCourse(String[] data) {
        deviceView.device.updataData(data);
        course.leavels.put(deviceView.device.nowleavel);
        course.speeds.put(data[0]);
        course.herts.put(0);
        course.rpm.put(data[3]);
        course.current++;
        course.carl.put(String.valueOf(EquipData.getCar(Integer.valueOf(data[2]),
                Integer.valueOf(data[3]),deviceView.device)));
        Double sp = Double.valueOf(data[0]);
        course.nowspeed = sp;
        if(sp >course.topSpeed)
        {
            course.topSpeed = sp;
        }
        course.dis += sp/3600;
        course.totalCarl += EquipData.getCar(Integer.valueOf(data[2]),
                Integer.valueOf(data[3]),deviceView.device);
        deviceView.device.nowrpm = Integer.valueOf(data[3]);

        if(data[5].equals("1"))
        {
            deviceView.device.statetype = Device.DEVICE_ERROR;
        }
        else
        {
            if(data[7].startsWith("1"))
            {
                deviceView.device.statetype = Device.DEVICE_LEAVECHANGE;
            }
            else
            {
                deviceView.device.statetype = Device.DEVICE_NOMAL;
            }
        }
        DBHelper.getInstance(mSportDetialActivity).upDataOptation(course);
        DkhomeApplication.mApp.sportDataManager.updataData(data,course,deviceView.device);
        int h = course.current/60/60;
        int m = (course.current/60)%60;
        int s = course.current%60;
        ftime.setText(String.format("%02d:%02d:%02d",h,m,s));
        fspeed.setText(String.valueOf(data[0])+"km/h");
        fdis.setText(String.valueOf(course.dis)+"km");
        fcarl.setText(String.valueOf(course.totalCarl)+"kcal");
        fselect.setText(String.valueOf(deviceView.device.nowleavel)+"%");

        sportView.doUpdata();
        deviceView.doUpdata();
        chartView.doUpdata();
    }

    private void initVideo() {
        surfaceView = mSportDetialActivity.findViewById(R.id.videoview);
        mSportDetialActivity.waitDialog.show();
        LayoutInflater mInflater = (LayoutInflater) mSportDetialActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        waitDialog = mInflater.inflate(R.layout.loading_dialog_blue2, null);// 得到加载view
        surfaceHolder = surfaceView.getHolder();//获取surfaceHolder
        mediaPlayer = new MediaPlayer();
        surfaceHolder.addCallback(callback);
        mediaPlayer.setOnPreparedListener(preparedListener);
        mediaPlayer.setOnInfoListener(onInfoListener);
        mediaPlayer.setOnCompletionListener(onCompletionListener);
        bar1 = mSportDetialActivity.findViewById(R.id.bar1);
        btnplay1 = mSportDetialActivity.findViewById(R.id.play);
        time1 = mSportDetialActivity.findViewById(R.id.time);
        fullScreen = mSportDetialActivity.findViewById(R.id.full);
        bar2 = mSportDetialActivity.findViewById(R.id.bar2);
        btnplay2 = mSportDetialActivity.findViewById(R.id.play2);
        time2 = mSportDetialActivity.findViewById(R.id.time2);
        smallScreen = mSportDetialActivity.findViewById(R.id.full2);
        top1 = mSportDetialActivity.findViewById(R.id.top1);
        top2 = mSportDetialActivity.findViewById(R.id.top2);
        tochLauer = mSportDetialActivity.findViewById(R.id.tochlayer);
        fullScreen = mSportDetialActivity.findViewById(R.id.full);
        smallScreen = mSportDetialActivity.findViewById(R.id.full2);
        seekBar1 = mSportDetialActivity.findViewById(R.id.seekbar);
        seekBar2 = mSportDetialActivity.findViewById(R.id.seekbar2);
        back1 = mSportDetialActivity.findViewById(R.id.back);
        back2 = mSportDetialActivity.findViewById(R.id.back2);
        title1 = mSportDetialActivity.findViewById(R.id.title);
        title2 = mSportDetialActivity.findViewById(R.id.title2);
        back1.setOnClickListener(mSportDetialActivity.mBackListener);
        back2.setOnClickListener(mSportDetialActivity.mBackListener);
        title1.setText(course.videoname);
        title2.setText(course.videoname);
        fullScreen.setOnClickListener(setScreenListener);
        smallScreen.setOnClickListener(setScreenListener);
        tochLauer.setOnClickListener(doSurfaceListener);
        seekBar1.setOnSeekBarChangeListener(onSeekBarChangeListener);
        seekBar2.setOnSeekBarChangeListener(onSeekBarChangeListener);
        //mSportDetialHandler.sendEmptyMessageDelayed(SportDetialHandler.HID_VIDEO_BAR, HID_BAR_TIME);
    }


    private void initTab() {
        chartView = new ChartView(this);
        sportView = new SportView(this);
        deviceView = new DeviceView(this);
        progressView = new ProgressView(mSportDetialActivity,null);
        viewPager = mSportDetialActivity.findViewById(R.id.page);
        viewPager.setNoScroll(true);
        btnChart = mSportDetialActivity.findViewById(R.id.btnchart);
        btnSport = mSportDetialActivity.findViewById(R.id.btntsport);
        btnDevice = mSportDetialActivity.findViewById(R.id.btndevice);
        tabtxtchart = mSportDetialActivity.findViewById(R.id.charttext);
        tabtxtsport = mSportDetialActivity.findViewById(R.id.sporttext);
        tabtxtdevice = mSportDetialActivity.findViewById(R.id.devicetext);
        tabimgchart = mSportDetialActivity.findViewById(R.id.charttimg);
        tabimgsport = mSportDetialActivity.findViewById(R.id.sporttimg);
        tabimgdevice = mSportDetialActivity.findViewById(R.id.devicetimg);
        btnChart.setOnClickListener(showChartListener);
        btnSport.setOnClickListener(showSportListener);
        btnDevice.setOnClickListener(showDeviceListener);
        sportPageAdapter = new SportPageAdapter();
        sportPageAdapter.mViews.add(chartView.view);
        sportPageAdapter.mViews.add(sportView.view);
        sportPageAdapter.mViews.add(deviceView.view);
        viewPager.setAdapter(sportPageAdapter);
        viewPager.setCurrentItem(1);
        sportView.doUpdata();
    }

    private MediaPlayer.OnInfoListener onInfoListener = new MediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            if(what == MediaPlayer.MEDIA_INFO_BUFFERING_START)
            {
                mSportDetialActivity.waitDialog.show();

            }
            else if(what == MediaPlayer.MEDIA_INFO_BUFFERING_END)
            {
                mSportDetialActivity.waitDialog.hide();
            }
            return false;
        }
    };

    private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(@NonNull SurfaceHolder holder) {
            mediaPlayer.setDisplay(holder);
            if(course.url.length() > 0)
            {
                try {
                    mediaPlayer.setDataSource(course.url);
                    mediaPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if (course.path.length() > 0) {
                File file = new File(course.path);
                if (file.exists()) {
                    try {
                        mediaPlayer.setDataSource(file.getPath());
                        mediaPlayer.prepareAsync();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
//            if (mediaPlayer != null) {
//                mediaPlayer.stop();
//                mediaPlayer.release();
//            }
        }
    };

    private MediaPlayer.OnPreparedListener preparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            mSportDetialActivity.waitDialog.hide();
            seekBar1.setMax(mp.getDuration()/1000);
            seekBar2.setMax(mp.getDuration()/1000);
            updataImf();
            doPlayPause();
        }
    };

    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            mediaPlayer.pause();
            btnplay1.setImageResource(R.drawable.play);
            btnplay2.setImageResource(R.drawable.play);
            mp.seekTo(0);
            seekBar1.setMax(mp.getCurrentPosition());
            seekBar2.setMax(mp.getCurrentPosition());
        }
    };

    private View.OnClickListener doSurfaceListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Configuration mConfiguration = mSportDetialActivity.getResources().getConfiguration(); //获取设置的配置信息
            int ori = mConfiguration.orientation; //获取屏幕方向
            if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {
                bar2.setVisibility(View.VISIBLE);
                top2.setVisibility(View.VISIBLE);
                mSportDetialHandler.removeMessages(SportDetialHandler.HID_VIDEO_BAR);
                mSportDetialHandler.sendEmptyMessageDelayed(SportDetialHandler.HID_VIDEO_BAR, HID_BAR_TIME);

            } else {
                bar1.setVisibility(View.VISIBLE);
                top1.setVisibility(View.VISIBLE);
                mSportDetialHandler.removeMessages(SportDetialHandler.HID_VIDEO_BAR);
                mSportDetialHandler.sendEmptyMessageDelayed(SportDetialHandler.HID_VIDEO_BAR, HID_BAR_TIME);
            }
        }
    };

    private View.OnClickListener setScreenListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Configuration mConfiguration = mSportDetialActivity.getResources().getConfiguration(); //获取设置的配置信息
            int ori = mConfiguration.orientation; //获取屏幕方向
            if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {
                hidFullScreen();
                bar1.setVisibility(View.VISIBLE);
                bar2.setVisibility(View.INVISIBLE);
                top1.setVisibility(View.VISIBLE);
                top2.setVisibility(View.INVISIBLE);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) surfaceView.getLayoutParams();
                layoutParams.height = (int) (mSportDetialActivity.mBasePresenter.mScreenDefine.density * 220);
                surfaceView.setLayoutParams(layoutParams);
                RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) tochLauer.getLayoutParams();
                layoutParams2.height = (int) (mSportDetialActivity.mBasePresenter.mScreenDefine.density * 220);
                tochLauer.setLayoutParams(layoutParams2);
                operbar.setVisibility(View.INVISIBLE);


            } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {

                dofullScreen();
                bar1.setVisibility(View.INVISIBLE);
                bar2.setVisibility(View.VISIBLE);
                top1.setVisibility(View.INVISIBLE);
                top2.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) surfaceView.getLayoutParams();
                layoutParams.height = (int) (mSportDetialActivity.mBasePresenter.mScreenDefine.ScreenHeight);
                surfaceView.setLayoutParams(layoutParams);
                RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) tochLauer.getLayoutParams();
                layoutParams2.height = (int) (mSportDetialActivity.mBasePresenter.mScreenDefine.ScreenHeight);
                tochLauer.setLayoutParams(layoutParams2);
                operbar.setVisibility(View.VISIBLE);
            }
        }
    };

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if(seekBar1.getProgress() != seekBar.getProgress())
            {
                seekBar1.setProgress(seekBar.getProgress());
            }
            if(seekBar2.getProgress() != seekBar.getProgress())
            {
                seekBar2.setProgress(seekBar.getProgress());
            }
            if(mediaPlayer != null)
            {
                mediaPlayer.seekTo(seekBar.getProgress()*1000);
            }
        }
    };



    private void hidFullScreen() {
        WindowInsetsControllerCompat windowInsetsController = ViewCompat.getWindowInsetsController(mSportDetialActivity.findViewById(R.id.activity_splash));
        mSportDetialActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        RelativeLayout stute = mSportDetialActivity.findViewById(R.id.stutebar);
        stute.setVisibility(View.VISIBLE);
        if(windowInsetsController != null)
        {
            windowInsetsController.show(WindowInsetsCompat.Type.systemBars());
            windowInsetsController.show(WindowInsetsCompat.Type.statusBars());
            //if(mSportDetialActivity.shownave)
            windowInsetsController.show(WindowInsetsCompat.Type.navigationBars());
        }

    }

    private void doPlayPause() {
        if(mediaPlayer != null)
        {
            if(mediaPlayer.isPlaying())
            {
                mediaPlayer.pause();
                btnplay1.setImageResource(R.drawable.play);
                btnplay2.setImageResource(R.drawable.play);
            }
            else
            {
                mediaPlayer.start();
                btnplay1.setImageResource(R.drawable.pause);
                btnplay2.setImageResource(R.drawable.pause);
            }
        }

    }

    private void dofullScreen() {
        WindowInsetsControllerCompat windowInsetsController = ViewCompat.getWindowInsetsController(mSportDetialActivity.findViewById(R.id.activity_splash));
        mSportDetialActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        RelativeLayout stute = mSportDetialActivity.findViewById(R.id.stutebar);
        stute.setVisibility(View.GONE);
        // 设置为竖屏模式
        if(windowInsetsController != null)
        {
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
            windowInsetsController.hide(WindowInsetsCompat.Type.statusBars());
            windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars());
        }

    }

    private String praseTime() {
        String time = "00:00/00:00";
        if(mediaPlayer != null)
        {
            int max = mediaPlayer.getDuration()/1000;
            int now = mediaPlayer.getCurrentPosition()/1000;
            if(max/60/60 > 0)
            {
                time = String.format("%d:%02d:%02d/%d:%02d:%02d",
                        now/60/60,(now/60)%60,now%60,max/60/60,(max/60)%60,max%60);
            }
            else if(max / 60 > 0)
            {
                time = String.format("%02d:%02d/%02d:%02d",
                        (now/60)%60,now%60,(max/60)%60,max%60);
            }
            else
            {
                time = String.format("%02d/%02d",
                        now%60,max%60);
            }
        }
        return time;
    }

    private View.OnClickListener showChartListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            tabimgchart.setImageResource(R.drawable.charts);
            tabimgsport.setImageResource(R.drawable.sport);
            tabimgdevice.setImageResource(R.drawable.device);
            tabtxtchart.setTextColor(Color.parseColor("#ff5e3a"));
            tabtxtsport.setTextColor(Color.parseColor("#333333"));
            tabtxtdevice.setTextColor(Color.parseColor("#333333"));
            viewPager.setCurrentItem(0);
        }
    };

    private View.OnClickListener showSportListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            tabimgchart.setImageResource(R.drawable.chart);
            tabimgsport.setImageResource(R.drawable.sports);
            tabimgdevice.setImageResource(R.drawable.device);
            tabtxtchart.setTextColor(Color.parseColor("#333333"));
            tabtxtsport.setTextColor(Color.parseColor("#ff5e3a"));
            tabtxtdevice.setTextColor(Color.parseColor("#333333"));
            viewPager.setCurrentItem(1);
        }
    };

    private View.OnClickListener showDeviceListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            tabimgchart.setImageResource(R.drawable.chart);
            tabimgsport.setImageResource(R.drawable.sport);
            tabimgdevice.setImageResource(R.drawable.devices);
            tabtxtchart.setTextColor(Color.parseColor("#333333"));
            tabtxtsport.setTextColor(Color.parseColor("#333333"));
            tabtxtdevice.setTextColor(Color.parseColor("#ff5e3a"));
            viewPager.setCurrentItem(2);
        }
    };


    private TestManager.SendData sendData = new TestManager.SendData() {
        @Override
        public void sendData(String[] data) {
            Message msg = new Message();
            msg.what = SportDetialHandler.UPDTATA_DATA;
            msg.obj = data;
            if(mSportDetialHandler != null)
            mSportDetialHandler.sendMessage(msg);
        }
    };

    private View.OnClickListener desListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(deviceView.device.nowleavel > 0)
            {
                deviceView.device.nowleavel--;
            }
            sportView.persent.setText(String.valueOf(deviceView.device.nowleavel)+"%");
            fselect.setText(String.valueOf(deviceView.device.nowleavel)+"%");
            TestManager.testManager.setLeavel(deviceView.device.nowleavel,deviceView.device);
        }
    };

    private View.OnClickListener addListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(deviceView.device.nowleavel < 100)
            {
                deviceView.device.nowleavel++;
            }
            sportView.persent.setText(String.valueOf(deviceView.device.nowleavel)+"%");
            fselect.setText(String.valueOf(deviceView.device.nowleavel)+"%");
            TestManager.testManager.setLeavel(deviceView.device.nowleavel,deviceView.device);
        }
    };



}
