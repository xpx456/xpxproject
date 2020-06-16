package intersky.function.presenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import intersky.appbase.Actions;
import intersky.appbase.PermissionResult;
import intersky.appbase.Presenter;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Attachment;
import intersky.appbase.entity.Conversation;
import intersky.apputils.AppUtils;
import intersky.apputils.EditDialogListener;
import intersky.function.FunctionUtils;
import intersky.function.R;
import intersky.function.asks.WorkFlowAsks;
import intersky.function.handler.WebMessageHandler;
import intersky.function.receiver.WebMessageReceiver;
import intersky.function.receiver.entity.WebBtn;
import intersky.function.view.activity.GridDetialActivity;
import intersky.function.view.activity.WebMessageActivity;
import intersky.mywidget.Utils;
import intersky.scan.ScanUtils;
import intersky.xpxnet.net.NetUtils;
import xpx.com.toolbar.utils.ToolBarHelper;

public class WebMessagePresenter implements Presenter {

    public WebMessageActivity mWebMessageActivity;
    public WebMessageHandler mWebMessageHandler;
    public WebMessagePresenter(WebMessageActivity mWebMessageActivity) {
        this.mWebMessageActivity = mWebMessageActivity;
        this.mWebMessageHandler = new WebMessageHandler(mWebMessageActivity);
        mWebMessageActivity.setBaseReceiver(new WebMessageReceiver(mWebMessageHandler));
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub

        mWebMessageActivity.setContentView(R.layout.activity_webmessage);
        ToolBarHelper.setLeftBtnText22((TextView) mWebMessageActivity.mToolBarHelper.getToolbarRoot().findViewById(xpx.com.toolbar.R.id.backText22),mWebMessageActivity.mCloseListenter,mWebMessageActivity.getString(R.string.button_close));
        mWebMessageActivity.mWebView = (WebView) mWebMessageActivity.findViewById(R.id.webview);
        initWebViewSettings();
        mWebMessageActivity.mWebView.addJavascriptInterface(mWebMessageActivity, "android");
        mWebMessageActivity.mWebView.setVisibility(View.VISIBLE);
        if(mWebMessageActivity.getIntent().getBooleanExtra("isurl",false) == true)
        {
            String url = mWebMessageActivity.getIntent().getStringExtra("url");
            if(url.contains("https://") || url.contains("http://"))
            {

            }
            else {
                if(FunctionUtils.getInstance().service.https)
                url = "https://"+url;
                else
                    url = "http://"+url;
            }
            mWebMessageActivity.mWebView.loadUrl(url);
        }
        else {
            String data = Utils.unescapeHtml4(mWebMessageActivity.getIntent().getStringExtra("data"));
            if(FunctionUtils.getInstance().service.https)
            mWebMessageActivity.mWebView.loadDataWithBaseURL(
                    "https://" + FunctionUtils.getInstance().service.sAddress + ":"
                            + FunctionUtils.getInstance().service.sPort,
                    data, "text/html", "utf-8", null);
            else
                mWebMessageActivity.mWebView.loadDataWithBaseURL(
                        "http://" + FunctionUtils.getInstance().service.sAddress + ":"
                                + FunctionUtils.getInstance().service.sPort,
                        data, "text/html", "utf-8", null);
        }
        if(mWebMessageActivity.getIntent().getBooleanExtra("showaction",false))
        {
            initActions();
        }
        ToolBarHelper.setTitle(mWebMessageActivity.mActionBar, "");
        ToolBarHelper.setBackListenr(mWebMessageActivity.mActionBar,mWebMessageActivity.mBackListener);
        mWebMessageActivity.mRelativeLayout = (RelativeLayout) mWebMessageActivity.findViewById(R.id.shade);
    }

    private void initActions() {

        LinearLayout actions = (LinearLayout) mWebMessageActivity.findViewById(R.id.actions);
        actions.setVisibility(View.VISIBLE);
        // 暂时没用
        mWebMessageActivity.back = (Button) actions.findViewById(R.id.back);
        mWebMessageActivity.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // 同意
        mWebMessageActivity.summit = (Button) actions.findViewById(R.id.summit);

        mWebMessageActivity.accept = (Button) actions.findViewById(R.id.accept);
        mWebMessageActivity.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            AppUtils.creatDialogTowButtonEdit(mWebMessageActivity,mWebMessageActivity.getString(R.string.approvemsg),""
                    ,mWebMessageActivity.getString(R.string.button_word_cancle),mWebMessageActivity.getString(R.string.button_word_ok),null,new EditDialogListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            WorkFlowAsks.doAccept(mWebMessageActivity,mWebMessageHandler,mWebMessageActivity.getIntent().getStringExtra("taskid"),editText.getText().toString());
                        }
                    },mWebMessageActivity.getString(R.string.approvemsg_hit));

            }
        });

        mWebMessageActivity.veto = (Button) actions.findViewById(R.id.veto);
        mWebMessageActivity.veto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            AppUtils.creatDialogTowButtonEdit(mWebMessageActivity,mWebMessageActivity.getString(R.string.approvedonot),""
                    ,mWebMessageActivity.getString(R.string.button_word_cancle),mWebMessageActivity.getString(R.string.button_word_ok),null,new EditDialogListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            WorkFlowAsks.doVeto(mWebMessageActivity,mWebMessageHandler,mWebMessageActivity.getIntent().getStringExtra("taskid"),editText.getText().toString());
                        }
                    },mWebMessageActivity.getString(R.string.approvemsg_hit));
            }
        });

        mWebMessageActivity.veto.setTextColor(Color.rgb(231, 231, 231));
        mWebMessageActivity.veto.setClickable(false);
        mWebMessageActivity.accept.setTextColor(Color.rgb(231, 231, 231));
        mWebMessageActivity.accept.setClickable(false);
        mWebMessageActivity.summit.setClickable(false);
        mWebMessageActivity.summit.setTextColor(Color.rgb(231, 231, 231));
        mWebMessageActivity.back.setClickable(false);
        mWebMessageActivity.back.setTextColor(Color.rgb(231, 231, 231));
        mWebMessageActivity.waitDialog.show();
        WorkFlowAsks.getWorkFlowTask(mWebMessageActivity,mWebMessageHandler,mWebMessageActivity.getIntent().getStringExtra("taskid"));
    }
    public void parseWarnDetailInfo(String jString) {

        try {
            JSONObject jsonObject = new JSONObject(jString);
            JSONObject jo = jsonObject.getJSONObject("task");

            boolean backEnabled = jo.getBoolean(WebMessageActivity.BACK_ENABLED);

            if (!backEnabled) {
                mWebMessageActivity.back.setClickable(false);
                mWebMessageActivity.back.setTextColor(Color.rgb(231, 231, 231));
            }
            else
            {
                mWebMessageActivity.back.setClickable(true);
                mWebMessageActivity.back.setTextColor(Color.rgb(140, 140, 140));
            }

            boolean transmitEnabled = jo.getBoolean(WebMessageActivity.TRANSMIT_ENABLED);

            if (!transmitEnabled) {
                mWebMessageActivity.summit.setClickable(false);
                mWebMessageActivity.summit.setTextColor(Color.rgb(231, 231, 231));
            }
            else
            {
                mWebMessageActivity.summit.setClickable(true);
                mWebMessageActivity.summit.setTextColor(Color.rgb(140, 140, 140));
            }

            boolean approvalEnabled = jo.getBoolean(WebMessageActivity.APPROVAL_ENABLED);
            if (!approvalEnabled) {
                mWebMessageActivity.accept.setTextColor(Color.rgb(231, 231, 231));
                mWebMessageActivity.accept.setClickable(false);

                mWebMessageActivity.veto.setTextColor(Color.rgb(231, 231, 231));
                mWebMessageActivity.veto.setClickable(false);
            }
            else
            {
                mWebMessageActivity.accept.setClickable(true);
                mWebMessageActivity.accept.setTextColor(Color.rgb(140, 140, 140));
                mWebMessageActivity.veto.setClickable(true);
                mWebMessageActivity.veto.setTextColor(Color.rgb(140, 140, 140));
            }

        } catch (JSONException e) {
            return;
        }
        // detailList.setAdapter(adapter);
    }


    public void initWebViewSettings() {

        WebSettings settings = mWebMessageActivity.mWebView.getSettings();
        mWebMessageActivity.initclint();
//        settings.setDefaultFontSize(50);
//        settings.setDefaultFixedFontSize(30);
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setDomStorageEnabled(true);
//        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(true);

        // WebView inside Browser doesn't want initial focus to be set.
//        settings.setNeedInitialFocus(false);
        // Browser supports multiple windows
//        settings.setSupportMultipleWindows(true);
        // enable smooth transition for better performance during panning or
        mWebMessageActivity.mWebView.setWebViewClient(new WebViewClient() {
            // 新开页面时用自己定义的webview来显示，不用系统自带的浏览器来显示
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                // 当有新连接时使用当前的webview进行显示
//                view.loadUrl(url);
//                return super.shouldOverrideUrlLoading(view, url);
                return false;
            }

            // 开始加载网页时要做的工作
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                view.setVisibility(View.INVISIBLE);
                mWebMessageActivity.waitDialog.show();
                Log.e("initcxApistart",url);
                super.onPageStarted(view, url, favicon);
            }

            // 加载完成时要做的工作
            public void onPageFinished(WebView view, String url) {
                view.setVisibility(View.VISIBLE);
                mWebMessageHandler.sendEmptyMessageDelayed(WebMessageHandler.EVENT_INPUT,500);
//                mWebMessageActivity.mWebView.loadUrl("javascript:valueToInput()");
                super.onPageFinished(view, url);


                mWebMessageActivity.waitDialog.hide();
                Log.e("initcxApifinish",url);
                Log.e("initcxApi","initcxApi success");


            }

            // 加载错误时要做的工作
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            }
        });
        mWebMessageActivity.mWebView.setWebChromeClient(new WebChromeClient(){
            public void onProgressChanged(WebView view, int progress) {
//                if(progress == 100)
//                {
//                    mWebMessageActivity.mWebView.loadUrl("javascript:valueToInput()");
//                    mWebMessageActivity.ongoback = false;
//                }
            }
        });
    }
    //{btns:[{caption:"",event:""}],show:true,barcolor:"",title:"",backurl:""}
    public void getloginfo()
    {
        Log.e("getloginfohandle","getloginfohandle success");
        try {

            JSONObject jo = new JSONObject(mWebMessageActivity.btnjson);
            JSONArray ja = jo.getJSONArray("btns");
            mWebMessageActivity.mWebBtns.clear();
            if(jo.getBoolean("show") == false)
            {
                mWebMessageActivity.mToolBarHelper.hidToolbar();

            }
            else
            {
                if(jo.has("barcolor"))
                {
                    if(jo.getString("barcolor").equals("white"))
                    {
                        //ViewUtils.setSutColor(mWebMessageActivity,Color.rgb(255,255,255));
                    }
                }
                if(jo.has("barcolor"))
                {
                    if(jo.getString("barcolor").equals("black"))
                    {
                        ToolBarHelper.setSutColor(mWebMessageActivity, Color.rgb(0,0,0));
//                        ViewUtils.setAllTextColor(mWebMessageActivity,mWebMessageActivity.mActionBar, Color.rgb(255,255,255));
                    }
                }
                if(jo.has("title"))
                {
                    if(jo.getString("title").length() > 0)
                    {
                        ToolBarHelper.setTitle(mWebMessageActivity.mActionBar,jo.getString("title"));
                    }
                }
                mWebMessageActivity.backurl = "";
                if(jo.has("backurl"))
                mWebMessageActivity.backurl = jo.getString("backurl");
            }

            for(int i = 0 ; i < ja.length() ; i++)
            {
                JSONObject jsonObject1 = ja.getJSONObject(i);
                WebBtn mWebBtnModel = new WebBtn();
                mWebBtnModel.name = jsonObject1.getString("caption");
                mWebBtnModel.method = jsonObject1.getString("event");
                mWebMessageActivity.mWebBtns.add(mWebBtnModel);
            }
            if(mWebMessageActivity.mWebBtns.size() > 0)
            {
                ToolBarHelper.setRightBtnText(mWebMessageActivity.mActionBar, mWebMessageActivity.mMoreListenter, " · · ·", true);
            }

        } catch (JSONException e) {
            e.printStackTrace();

        }
    }


    public void onSearch() {
        String url = mWebMessageActivity.mSearchViewLayout.getText();
        if (url.length() > 0)
            mWebMessageActivity.mWebView.loadUrl(url);
        else
            dotest();
    }


    public void dotest() {

        //initWebViewSettings();
//        mWebMessageActivity.mWebView.getSettings().setJavaScriptEnabled(true);
//        mWebMessageActivity.mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        mWebMessageActivity.mWebView.getSettings().setSupportMultipleWindows(true);
        initWebViewSettings();
        mWebMessageActivity.mWebView.addJavascriptInterface(mWebMessageActivity, "android");
        mWebMessageActivity.mWebView.setVisibility(View.VISIBLE);
//        mWebMessageActivity.mWebView.loadUrl("http://mail.qq.com");
        //mWebMessageActivity.mWebView.loadUrl("file:///android_asset/web.html");
    }



    //	public void getApp()
//	{
//		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//		nvps.add(new BasicNameValuePair("start", "1"));
//		nvps.add(new BasicNameValuePair("limit", "100"));
//		String urlString = NetUtils.getInstance().createURLString(MainActivity.BUSSINESS_WARN_PATH,
//				URLEncodedUtils.format(nvps, HTTP.UTF_8));
//		NetTaskManager.getInstance().addNetTask(new NetTask(urlString, mMainHandler,
//				MainActivity.EVENT_UPDATE_HINT_WARM, 0, mMainActivity));
//	}

    public void startScan()
    {
        mWebMessageActivity.permissionRepuest = ScanUtils.getInstance().checkStartScan(mWebMessageActivity, "");
    }


    public void showCode(String code) {
        mWebMessageActivity.mWebView.loadUrl("javascript:javacalljswith(" + code + ")");
        //AppUtils.showMessage(mWebMessageActivity, "android 扫到条形码：" + code);
    }

    public void showCode2(String code) {

        try {
            JSONObject jsonObject = new JSONObject(mWebMessageActivity.json);
            String method = jsonObject.getString("method");
            JSONObject arg = new JSONObject();
            arg.put("result",code);
            arg.put("params",jsonObject.getJSONObject("params"));
            mWebMessageActivity.mWebView.loadUrl("javascript:"+method+"(" + arg.toString() + ")");

        } catch (JSONException e) {
            e.printStackTrace();
            //AppUtils.showMessage(mWebMessageActivity,"josn格式错误");
        }


       // ViewUtils.showMessage(mWebMessageActivity, "android 扫到条形码：" + code + mWebMessageActivity.json);
    }

    @Override
    public void Create() {
        // TODO Auto-generated method stub
        initView();
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
        mWebMessageActivity.mWebView.destroy();
    }

    public boolean goBack()
    {
        if (mWebMessageActivity.mWebView.canGoBack())
        {
            mWebMessageActivity.ongoback = true;
            mWebMessageActivity.mWebView.goBack();

        }
        else
        {
            mWebMessageActivity.finish();
        }
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return goBack();
        } else {
            return false;
        }
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() - e2.getX() > mWebMessageActivity.mBasePresenter.mScreenDefine.verticalMinDistance && Math.abs(velocityX) > 0) {
            return false;
        } else if (e2.getX() - e1.getX() > mWebMessageActivity.mBasePresenter.mScreenDefine.verticalMinDistance && Math.abs(velocityX) > 0) {

            if (e2.getX() - e1.getX() > e2.getY() - e1.getY() && e2.getX() - e1.getX() > e1.getY() - e2.getY() && mWebMessageActivity.flagFillBack == true) {
                return false;
            } else {
                return false;
            }
        }
        return false;


    }

    public void dodismiss1() {
        mWebMessageActivity.popupWindow1.dismiss();
    }

    public void dodismiss2()
    {
        mWebMessageActivity.popupWindow1.dismiss();
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
        {
            mWebMessageActivity.uploadMessage.onReceiveValue(null);
        }
        else
        {
            mWebMessageActivity.uploadMessageAboveL.onReceiveValue(null);
        }

    }

    public void showMore() {

        View popupWindowView = LayoutInflater.from(mWebMessageActivity).inflate(R.layout.webheadbtns, null);
        RelativeLayout lsyer = (RelativeLayout) popupWindowView.findViewById(R.id.layer);
        lsyer.setFocusable(true);
        lsyer.setFocusableInTouchMode(true);
        mWebMessageActivity.popupWindow1 = new PopupWindow(popupWindowView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
        popupWindowView.setFocusable(true);
        popupWindowView.setFocusableInTouchMode(true);
        mWebMessageActivity.popupWindow1.setAnimationStyle(R.style.PopupAnimation);
        lsyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebMessageActivity.popupWindow1.dismiss();

            }
        });
        ColorDrawable dw = new ColorDrawable(0x00ffffff);
        mWebMessageActivity.popupWindow1.setBackgroundDrawable(dw);
        LinearLayout linelayer = (LinearLayout) popupWindowView.findViewById(R.id.pop_layout);
        if(mWebMessageActivity.mWebBtns.size() == 1)
        {
            WebBtn mWebBtnModel = mWebMessageActivity.mWebBtns.get(0);
            View view = LayoutInflater.from(mWebMessageActivity).inflate(R.layout.btn2,null);
            Button btn = (Button) view.findViewById(R.id.btn);
            btn.setText(mWebBtnModel.name);
            btn.setTag(mWebBtnModel.method);
            btn.setOnClickListener(mWebMessageActivity.clictBtn);
            linelayer.addView(view);
            view = LayoutInflater.from(mWebMessageActivity).inflate(R.layout.emptylayer,null);
            linelayer.addView(view);
            view = LayoutInflater.from(mWebMessageActivity).inflate(R.layout.btn2,null);
            btn = (Button) view.findViewById(R.id.btn);
            btn.setText(mWebMessageActivity.getString(R.string.button_word_cancle));
            btn.setOnClickListener(mWebMessageActivity.mCancleListenter);
            linelayer.addView(view);

        }
        else if(mWebMessageActivity.mWebBtns.size() == 2)
        {
            WebBtn mWebBtnModel = mWebMessageActivity.mWebBtns.get(0);
            WebBtn mWebBtnModel2 = mWebMessageActivity.mWebBtns.get(1);
            View view = LayoutInflater.from(mWebMessageActivity).inflate(R.layout.btn1,null);
            Button btn = (Button) view.findViewById(R.id.btn);
            btn.setText(mWebBtnModel.name);
            btn.setTag(mWebBtnModel.method);
            btn.setOnClickListener(mWebMessageActivity.clictBtn);
            linelayer.addView(view);
            view = LayoutInflater.from(mWebMessageActivity).inflate(R.layout.webline,null);
            linelayer.addView(view);
             view = LayoutInflater.from(mWebMessageActivity).inflate(R.layout.btn3,null);
             btn = (Button) view.findViewById(R.id.btn);
            btn.setText(mWebBtnModel2.name);
            btn.setTag(mWebBtnModel2.method);
            btn.setOnClickListener(mWebMessageActivity.clictBtn);
            linelayer.addView(view);
            view = LayoutInflater.from(mWebMessageActivity).inflate(R.layout.emptylayer,null);
            linelayer.addView(view);
            view = LayoutInflater.from(mWebMessageActivity).inflate(R.layout.btn2,null);
            btn = (Button) view.findViewById(R.id.btn);
            btn.setText(mWebMessageActivity.getString(R.string.button_word_cancle));
            btn.setOnClickListener(mWebMessageActivity.mCancleListenter);
            linelayer.addView(view);
;
        }
        else
        {
            for(int i = 0 ; i < mWebMessageActivity.mWebBtns.size() ; i++)
            {
                WebBtn mWebBtnModel = mWebMessageActivity.mWebBtns.get(i);
                if(i == 0)
                {
                    View view = LayoutInflater.from(mWebMessageActivity).inflate(R.layout.btn1,null);
                    Button btn = (Button) view.findViewById(R.id.btn);
                    btn.setText(mWebBtnModel.name);
                    btn.setTag(mWebBtnModel.method);
                    btn.setOnClickListener(mWebMessageActivity.clictBtn);
                    linelayer.addView(view);
                    view = LayoutInflater.from(mWebMessageActivity).inflate(R.layout.webline,null);
                    linelayer.addView(view);
                }
                else if(i == mWebMessageActivity.mWebBtns.size()-1)
                {
                    View view = LayoutInflater.from(mWebMessageActivity).inflate(R.layout.btn3,null);
                    Button btn = (Button) view.findViewById(R.id.btn);
                    btn.setText(mWebBtnModel.name);
                    btn.setTag(mWebBtnModel.method);
                    btn.setOnClickListener(mWebMessageActivity.clictBtn);
                    linelayer.addView(view);
                }
                else
                {
                    View view = LayoutInflater.from(mWebMessageActivity).inflate(R.layout.btn4,null);
                    Button btn = (Button) view.findViewById(R.id.btn);
                    btn.setText(mWebBtnModel.name);
                    btn.setTag(mWebBtnModel.method);
                    btn.setOnClickListener(mWebMessageActivity.clictBtn);
                    linelayer.addView(view);
                    view = LayoutInflater.from(mWebMessageActivity).inflate(R.layout.webline,null);
                    linelayer.addView(view);
                }

            }
            View view = LayoutInflater.from(mWebMessageActivity).inflate(R.layout.emptylayer,null);
            linelayer.addView(view);
            view = LayoutInflater.from(mWebMessageActivity).inflate(R.layout.btn2,null);
            Button btn = (Button) view.findViewById(R.id.btn);
            btn.setText(mWebMessageActivity.getString(R.string.button_word_cancle));
            btn.setOnClickListener(mWebMessageActivity.mCancleListenter);
            linelayer.addView(view);
        }
        mWebMessageActivity.mRelativeLayout.setVisibility(View.VISIBLE);
        mWebMessageActivity.popupWindow1.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mWebMessageActivity.mRelativeLayout.setVisibility(View.INVISIBLE);
            }
        });
        mWebMessageActivity.popupWindow1.showAtLocation(mWebMessageActivity.findViewById(R.id.activity_about),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    public void clickBtn(String method)
    {
        mWebMessageActivity.mWebView.loadUrl("javascript:"+method+"()");
        mWebMessageActivity.popupWindow1.dismiss();
    }

    public void showAdd() {

        View popupWindowView = LayoutInflater.from(mWebMessageActivity).inflate(R.layout.picpwindowmenu, null);
        RelativeLayout lsyer = (RelativeLayout) popupWindowView.findViewById(R.id.layer);
        lsyer.setFocusable(true);
        lsyer.setFocusableInTouchMode(true);
        mWebMessageActivity.popupWindow1 = new PopupWindow(popupWindowView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
        popupWindowView.setFocusable(true);
        popupWindowView.setFocusableInTouchMode(true);
        mWebMessageActivity.popupWindow1.setAnimationStyle(R.style.PopupAnimation);
        lsyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebMessageActivity.popupWindow1.dismiss();

            }
        });
        ColorDrawable dw = new ColorDrawable(0x00ffffff);
        mWebMessageActivity.popupWindow1.setBackgroundDrawable(dw);
        Button textview_edit = (Button) popupWindowView.findViewById(R.id.btn_pick_photo3);
        textview_edit.setOnClickListener(mWebMessageActivity.mTakePhotoListenter);
        Button textview_delete = (Button) popupWindowView.findViewById(R.id.btn_take_photo);
        textview_delete.setOnClickListener(mWebMessageActivity.mAddPicListener);
        Button textview_new = (Button) popupWindowView.findViewById(R.id.btn_cancel);
        textview_new.setOnClickListener(mWebMessageActivity.mCancleListenter2);
        mWebMessageActivity.mRelativeLayout.setVisibility(View.VISIBLE);
        mWebMessageActivity.popupWindow1.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mWebMessageActivity.mRelativeLayout.setVisibility(View.INVISIBLE);
            }
        });
        mWebMessageActivity.popupWindow1.showAtLocation(mWebMessageActivity.findViewById(R.id.activity_about),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    public void addPic() {
//        Intent mainIntent = new Intent();
//        mainIntent.putExtra("from", AlbumActivity.FORM_WebMessage);
//        mainIntent.setClass(mWebMessageActivity, PhotoDetialActivity.class);
//        mWebMessageActivity.startActivity(mainIntent);
//        mWebMessageActivity.popupWindow1.dismiss();

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType( "video/*;image/*;audio/*");
        mWebMessageActivity.startActivityForResult(intent, Actions.CHOSE_PICTURE);
        mWebMessageActivity.popupWindow1.dismiss();
    }


    public void takePhoto() {
        mWebMessageActivity.popupWindow1.dismiss();
        mWebMessageActivity.permissionRepuest = (PermissionResult) Bus.callData(mWebMessageActivity,"filetools/checkPermissionTakePhoto"
               ,mWebMessageActivity ,Bus.callData(mWebMessageActivity,"filetools/getfilePath","/webmessage/photo"));
    }

    private void clearUploadCallBack(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {//5.0以下
            if (mWebMessageActivity.uploadMessage != null){
                mWebMessageActivity.uploadMessage.onReceiveValue(null);
                mWebMessageActivity.uploadMessage = null;
            }

        }else {
            if(mWebMessageActivity.uploadMessageAboveL != null){
                mWebMessageActivity.uploadMessageAboveL.onReceiveValue(null);
                mWebMessageActivity.uploadMessageAboveL = null;
            }
        }
    }

    public void takePhotoResult(int requestCode, int resultCode, Intent data) {



        switch (requestCode) {
            case Actions.TAKE_PHOTO:
                File mFile = new File((String) Bus.callData(mWebMessageActivity,"filetools/takePhotoUri",""));
                if (mFile.exists()) {
                    mWebMessageActivity.mfilepath = mFile.getPath();
                    doupload(mFile.getPath());
                }
                else
                {
                    clearUploadCallBack();
                }
                break;
            case Actions.CHOSE_PICTURE:
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
                {
                    if (requestCode == Actions.CHOSE_PICTURE) {
                        if (null == mWebMessageActivity.uploadMessage && null == mWebMessageActivity.uploadMessageAboveL) return;
                        Uri result = data == null || resultCode != Activity.RESULT_OK ? null : data.getData();
                        if (mWebMessageActivity.uploadMessageAboveL != null) {
                            if (requestCode != Actions.CHOSE_PICTURE || mWebMessageActivity.uploadMessageAboveL == null)
                                return;
                            Uri[] results = null;
                            if (resultCode == Activity.RESULT_OK) {
                                if (data != null) {
                                    String dataString = data.getDataString();
                                    ClipData clipData = data.getClipData();
                                    if (clipData != null) {
                                        results = new Uri[clipData.getItemCount()];
                                        for (int i = 0; i < clipData.getItemCount(); i++) {
                                            ClipData.Item item = clipData.getItemAt(i);
                                            results[i] = item.getUri();
                                        }
                                    }
                                    if (dataString != null)
                                        results = new Uri[]{Uri.parse(dataString)};
                                }
                                mWebMessageActivity.uploadMessageAboveL.onReceiveValue(results);
                                mWebMessageActivity.uploadMessageAboveL = null;
                            }
                            else
                            {
                                mWebMessageActivity.uploadMessageAboveL.onReceiveValue(null);
                                mWebMessageActivity.uploadMessageAboveL = null;
                            }

                        } else if (mWebMessageActivity.uploadMessage != null) {
                            mWebMessageActivity.uploadMessage = null;
                        }
                    }
                }
                else
                {
                    if (requestCode != Actions.CHOSE_PICTURE || mWebMessageActivity.uploadMessageAboveL == null)
                        return;
                    Uri[] results = null;
                    if (resultCode == Activity.RESULT_OK) {
                        if (data != null) {
                            String dataString = data.getDataString();
                            ClipData clipData = data.getClipData();
                            if (clipData != null) {
                                results = new Uri[clipData.getItemCount()];
                                for (int i = 0; i < clipData.getItemCount(); i++) {
                                    ClipData.Item item = clipData.getItemAt(i);
                                    results[i] = item.getUri();
                                }
                            }
                            if (dataString != null)
                                results = new Uri[]{Uri.parse(dataString)};
                        }
                        mWebMessageActivity.uploadMessageAboveL.onReceiveValue(results);
                        mWebMessageActivity.uploadMessageAboveL = null;
                    }
                    else
                    {
                        mWebMessageActivity.uploadMessageAboveL.onReceiveValue(null);
                        mWebMessageActivity.uploadMessageAboveL = null;
                    }
                }
                break;
        }
    }

    public void doupload(String path)
    {
        File cameraFile = new File(path);
        Uri[] uris = new Uri[1];
        if(cameraFile.exists())
        {
            uris[0] = Uri.fromFile(cameraFile);
            if(mWebMessageActivity.uploadMessageAboveL != null)
            mWebMessageActivity.uploadMessageAboveL.onReceiveValue(uris);
        }
        else
        {
            mWebMessageActivity.uploadMessage.onReceiveValue(null);
        }

    }



    public void startdownload(Attachment mAttachmentModel)
    {
        Bus.callData(mWebMessageActivity,"filetools/startAttachment",mAttachmentModel);
    }

    public void openFileAction(String url,String size) {
        String name = url.substring(url.lastIndexOf("/")+1,url.length());
        String html = "";
        long usize = Long.valueOf(size);
        if(FunctionUtils.getInstance().service.https == false)
            html =url.replaceAll("/img","http://" + FunctionUtils.getInstance().service.sAddress+":"+FunctionUtils.getInstance().service.sPort+"/image/"+FunctionUtils.getInstance().account.mCompanyId);
        else
            html = url.replaceAll("/img","https://" + FunctionUtils.getInstance().service.sAddress+":"+FunctionUtils.getInstance().service.sPort+"/image/"+FunctionUtils.getInstance().account.mCompanyId);
        Attachment mAttachmentModel = new Attachment("", "",Bus.callData(mWebMessageActivity,"filetools/getfilePath","/smart")+"/" +name,html,usize,0,"");
        startdownload(mAttachmentModel);
    }
}
