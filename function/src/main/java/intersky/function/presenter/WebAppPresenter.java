package intersky.function.presenter;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import intersky.appbase.Actions;
import intersky.appbase.PermissionResult;
import intersky.appbase.Presenter;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Attachment;
import intersky.apputils.AppUtils;
import intersky.function.FunctionUtils;
import intersky.function.R;
import intersky.function.entity.WebBtn;
import intersky.function.handler.WebAppHandler;
import intersky.function.receiver.WebAppReceiver;
import intersky.function.receiver.WebMessageReceiver;
import intersky.function.view.activity.WebAppActivity;
import intersky.mywidget.SearchViewLayout;
import intersky.scan.ScanUtils;
import intersky.xpxnet.net.NameValuePair;
import intersky.xpxnet.net.NetUtils;
import xpx.com.toolbar.utils.ToolBarHelper;


public class WebAppPresenter implements Presenter {


    public ImageView back;
    public TextView more;
    public TextView title;
    public WebAppActivity mWebAppActivity;
    public WebAppHandler mWebAppHandler;
    public WebAppPresenter(WebAppActivity mWebAppActivity) {
        this.mWebAppActivity = mWebAppActivity;
        this.mWebAppHandler = new WebAppHandler(mWebAppActivity);
        mWebAppActivity.setBaseReceiver(new WebAppReceiver(mWebAppHandler));
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        mWebAppActivity.flagFillBack = false;
        if (mWebAppActivity.getIntent().getBooleanExtra("test", false) == true) {
            mWebAppActivity.setContentView(R.layout.activity_webapp_test);
            mWebAppActivity.mWebView = (WebView) mWebAppActivity.findViewById(R.id.webview);
            mWebAppActivity.mSearchViewLayout = (SearchViewLayout) mWebAppActivity.findViewById(R.id.top_layer);
//            mWebAppActivity.mSearchViewLayout.setOnclickListener(mWebAppActivity.mOnEditorActionListener);
//            mWebAppActivity.mSearchViewLayout.setaddTextChange(mWebAppActivity.mTextchange);
//            UpLoadModel mUpLoadModel = new UpLoadModel("(User)", "", "", "", "/storage/emulated/0/Intersky/webapp/20170714_102232.jpg",AppUtils.getDateAndTime(), "20170714_102232.jpg",
//                    "", 2330940, false);
//            mUpLoadModel.setmRecordId(DocumentManager.getInstance().getUpLoadGuid(mUpLoadModel));
//            UploadTask mUploadTask = new UploadTask(
//                    NetUtils.getInstance().createURLString(DocumentManager.HTTP_UPLOAD_PATH),
//                    mUpLoadModel);
//            UploadTaskManager.getInstance().addUploadTask(mUploadTask);
            dotest();
        } else {
            mWebAppActivity.setContentView(R.layout.activity_webapp);

            title = mWebAppActivity.findViewById(R.id.title);
            more = mWebAppActivity.findViewById(R.id.more);
            more.setOnClickListener(mWebAppActivity.mMoreListenter);
            back = mWebAppActivity.findViewById(R.id.back1);
            back.setOnClickListener(mWebAppActivity.mBackListener);
            TextView close = mWebAppActivity.findViewById(R.id.cancle);
            close.setOnClickListener(mWebAppActivity.mCloseListenter);
            mWebAppActivity.fInfo = mWebAppActivity.getIntent().getParcelableExtra("function");
            title.setText(mWebAppActivity.fInfo.mCaption);
            mWebAppActivity.mWebView = (WebView) mWebAppActivity.findViewById(R.id.webview);
            getapp();
        }
        mWebAppActivity.mRelativeLayout = (RelativeLayout) mWebAppActivity.findViewById(R.id.shade);
    }

    public void initWebViewSettings() {

        WebSettings settings = mWebAppActivity.mWebView.getSettings();
        mWebAppActivity.initclint();
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
        mWebAppActivity.mWebView.setWebViewClient(new WebViewClient() {
            // 新开页面时用自己定义的webview来显示，不用系统自带的浏览器来显示
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                // 当有新连接时使用当前的webview进行显示
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            // 开始加载网页时要做的工作
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                view.setVisibility(View.INVISIBLE);
                mWebAppActivity.waitDialog.show();
                Log.e("initcxApistart",url);
                //AppUtils.showMessage(mWebAppActivity,url+"-----------success");
                super.onPageStarted(view, url, favicon);
            }

            // 加载完成时要做的工作
            public void onPageFinished(WebView view, String url) {
                view.setVisibility(View.VISIBLE);
                super.onPageFinished(view, url);
                mWebAppActivity.waitDialog.hide();
                Log.e("initcxApifinish",url);
                Log.e("initcxApi","initcxApi success");
//                AppUtils.showMessage(mWebAppActivity,url);
                mWebAppActivity.mWebView.loadUrl("javascript:initcxApi()");
//                if(url.equals("javascript:initcxApi()"))
//                {
//                    AppUtils.showMessage(mWebAppActivity,"javascript:initcxApi() success");
//                }
//                AppUtils.showMessage(mWebAppActivity,"调用initcxApi："+NetUtils.getInstance().token);

            }

            // 加载错误时要做的工作
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    //AppUtils.showMessage(mWebAppActivity,"error"+failingUrl+"/"+String.valueOf(errorCode)+"/"+description);
            }
        });
    }

    public void getloginfo()
    {
        Log.e("getloginfohandle","getloginfohandle success");
        try {

            JSONObject jo = new JSONObject(mWebAppActivity.btnjson);
            JSONArray ja = jo.getJSONArray("btns");
            mWebAppActivity.mWebBtns.clear();
            if(jo.getBoolean("show") == false)
            {
                RelativeLayout head = mWebAppActivity.findViewById(R.id.head);
                head.setVisibility(View.GONE);

            }
            else
            {
                if(jo.has("barcolor"))
                {
                    if(jo.getString("barcolor").equals("white"))
                    {
                        //ViewUtils.setSutColor(mWebAppActivity,Color.rgb(255,255,255));
                    }
                }
                if(jo.has("barcolor"))
                {
                    if(jo.getString("barcolor").equals("black"))
                    {
                        ToolBarHelper.setSutColor(mWebAppActivity, Color.rgb(0,0,0));
//                        ViewUtils.setAllTextColor(mWebAppActivity,mWebAppActivity.mActionBar, Color.rgb(255,255,255));
                    }
                }
                if(jo.has("title"))
                {
                    if(jo.getString("title").length() > 0)
                    {
                        title.setText(jo.getString("title"));
                    }
                }
                mWebAppActivity.backurl = "";
                if(jo.has("backurl"))
                mWebAppActivity.backurl = jo.getString("backurl");
            }

            for(int i = 0 ; i < ja.length() ; i++)
            {
                JSONObject jsonObject1 = ja.getJSONObject(i);
                WebBtn mWebBtnModel = new WebBtn();
                mWebBtnModel.name = jsonObject1.getString("caption");
                mWebBtnModel.method = jsonObject1.getString("event");
                mWebAppActivity.mWebBtns.add(mWebBtnModel);
            }
            if(mWebAppActivity.mWebBtns.size() > 0)
            {
                more.setVisibility(View.VISIBLE);
            }

        } catch (JSONException e) {
            e.printStackTrace();

        }
    }


    public void onSearch() {
        String url = mWebAppActivity.mSearchViewLayout.getText();
        if (url.length() > 0)
            mWebAppActivity.mWebView.loadUrl(url);
        else
            dotest();
    }

    public void getapp() {
//        mWebAppActivity.mWebView.setWebViewClient(new WebViewClient());
//        mWebAppActivity.mWebView.getSettings().setJavaScriptEnabled(true);
//        mWebAppActivity.mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        mWebAppActivity.mWebView.getSettings().setSupportMultipleWindows(true);
//        mWebAppActivity.mWebView.setWebViewClient(new WebViewClient());
//        mWebAppActivity.mWebView.setWebChromeClient(new WebChromeClient());
        initWebViewSettings();
        mWebAppActivity.mWebView.addJavascriptInterface(mWebAppActivity, "android");
        String url = getWebAppUrl(mWebAppActivity.fInfo.mName);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        mWebAppActivity.mWebView.loadUrl(url);
//        mWebAppHandler.sendEmptyMessageDelayed(WebAppActivity.EVENT_GET_INFO,2000);
        mWebAppActivity.mWebView.setVisibility(View.VISIBLE);
    }

    public void dotest() {

        //initWebViewSettings();
//        mWebAppActivity.mWebView.getSettings().setJavaScriptEnabled(true);
//        mWebAppActivity.mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        mWebAppActivity.mWebView.getSettings().setSupportMultipleWindows(true);
        initWebViewSettings();
        mWebAppActivity.mWebView.addJavascriptInterface(mWebAppActivity, "android");
        mWebAppActivity.mWebView.setVisibility(View.VISIBLE);
//        mWebAppActivity.mWebView.loadUrl("http://mail.qq.com");
        //mWebAppActivity.mWebView.loadUrl("file:///android_asset/web.html");
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
        mWebAppActivity.permissionRepuest = ScanUtils.getInstance().checkStartScan(mWebAppActivity, "");
    }


    public void showCode(String code) {
        mWebAppActivity.mWebView.loadUrl("javascript:javacalljswith(" + code + ")");
//        AppUtils.showMessage(mWebAppActivity, "android 扫到条形码：" + code);
    }

    public void showCode2(String code) {

        try {
            JSONObject jsonObject = new JSONObject(mWebAppActivity.json);
            String method = jsonObject.getString("method");
            JSONObject arg = new JSONObject();
            arg.put("result",code);
            arg.put("params",jsonObject.getJSONObject("params"));
            mWebAppActivity.mWebView.loadUrl("javascript:"+method+"(" + arg.toString() + ")");

        } catch (JSONException e) {
            e.printStackTrace();
            //AppUtils.showMessage(mWebAppActivity,"josn格式错误");
        }


       // ViewUtils.showMessage(mWebAppActivity, "android 扫到条形码：" + code + mWebAppActivity.json);
    }

    @Override
    public void Create() {
        // TODO Auto-generated method stub
        initView();
        IntentFilter filter = new IntentFilter();
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
        mWebAppActivity.mWebView.destroy();
    }

    public boolean goBack()
    {
        if(mWebAppActivity.backurl.length() == 0)
        {
//            if (mWebAppActivity.mWebView.canGoBack())
//            {
//                mWebAppActivity.mWebView.goBack();
//            }
//            else
            {
                mWebAppActivity.finish();
            }

        }
        else
        {
            mWebAppActivity.mWebView.loadUrl("javascript:"+mWebAppActivity.backurl+"()");
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
        if (e1.getX() - e2.getX() > mWebAppActivity.mBasePresenter.mScreenDefine.verticalMinDistance && Math.abs(velocityX) > 0) {
            return false;
        } else if (e2.getX() - e1.getX() > mWebAppActivity.mBasePresenter.mScreenDefine.verticalMinDistance && Math.abs(velocityX) > 0) {

            if (e2.getX() - e1.getX() > e2.getY() - e1.getY() && e2.getX() - e1.getX() > e1.getY() - e2.getY() && mWebAppActivity.flagFillBack == true) {
                return false;
            } else {
                return false;
            }
        }
        return false;


    }

    public void dodismiss1() {
        mWebAppActivity.popupWindow1.dismiss();
    }

    public void dodismiss2()
    {
        mWebAppActivity.popupWindow1.dismiss();
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
        {
            mWebAppActivity.uploadMessage.onReceiveValue(null);
        }
        else
        {
            mWebAppActivity.uploadMessageAboveL.onReceiveValue(null);
        }

    }

    public void showMore() {

        View popupWindowView = LayoutInflater.from(mWebAppActivity).inflate(R.layout.webheadbtns, null);
        RelativeLayout lsyer = (RelativeLayout) popupWindowView.findViewById(R.id.layer);
        lsyer.setFocusable(true);
        lsyer.setFocusableInTouchMode(true);
        mWebAppActivity.popupWindow1 = new PopupWindow(popupWindowView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
        popupWindowView.setFocusable(true);
        popupWindowView.setFocusableInTouchMode(true);
        mWebAppActivity.popupWindow1.setAnimationStyle(R.style.PopupAnimation);
        lsyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebAppActivity.popupWindow1.dismiss();

            }
        });
        ColorDrawable dw = new ColorDrawable(0x00ffffff);
        mWebAppActivity.popupWindow1.setBackgroundDrawable(dw);
        LinearLayout linelayer = (LinearLayout) popupWindowView.findViewById(R.id.pop_layout);
        if(mWebAppActivity.mWebBtns.size() == 1)
        {
            WebBtn mWebBtnModel = mWebAppActivity.mWebBtns.get(0);
            View view = LayoutInflater.from(mWebAppActivity).inflate(R.layout.btn2,null);
            Button btn = (Button) view.findViewById(R.id.btn);
            btn.setText(mWebBtnModel.name);
            btn.setTag(mWebBtnModel.method);
            btn.setOnClickListener(mWebAppActivity.clictBtn);
            linelayer.addView(view);
            view = LayoutInflater.from(mWebAppActivity).inflate(R.layout.emptylayer,null);
            linelayer.addView(view);
            view = LayoutInflater.from(mWebAppActivity).inflate(R.layout.btn2,null);
            btn = (Button) view.findViewById(R.id.btn);
            btn.setText(mWebAppActivity.getString(R.string.button_word_cancle));
            btn.setOnClickListener(mWebAppActivity.mCancleListenter);
            linelayer.addView(view);

        }
        else if(mWebAppActivity.mWebBtns.size() == 2)
        {
            WebBtn mWebBtnModel = mWebAppActivity.mWebBtns.get(0);
            WebBtn mWebBtnModel2 = mWebAppActivity.mWebBtns.get(1);
            View view = LayoutInflater.from(mWebAppActivity).inflate(R.layout.btn1,null);
            Button btn = (Button) view.findViewById(R.id.btn);
            btn.setText(mWebBtnModel.name);
            btn.setTag(mWebBtnModel.method);
            btn.setOnClickListener(mWebAppActivity.clictBtn);
            linelayer.addView(view);
            view = LayoutInflater.from(mWebAppActivity).inflate(R.layout.webline,null);
            linelayer.addView(view);
             view = LayoutInflater.from(mWebAppActivity).inflate(R.layout.btn3,null);
             btn = (Button) view.findViewById(R.id.btn);
            btn.setText(mWebBtnModel2.name);
            btn.setTag(mWebBtnModel2.method);
            btn.setOnClickListener(mWebAppActivity.clictBtn);
            linelayer.addView(view);
            view = LayoutInflater.from(mWebAppActivity).inflate(R.layout.emptylayer,null);
            linelayer.addView(view);
            view = LayoutInflater.from(mWebAppActivity).inflate(R.layout.btn2,null);
            btn = (Button) view.findViewById(R.id.btn);
            btn.setText(mWebAppActivity.getString(R.string.button_word_cancle));
            btn.setOnClickListener(mWebAppActivity.mCancleListenter);
            linelayer.addView(view);
;
        }
        else
        {
            for(int i = 0 ; i < mWebAppActivity.mWebBtns.size() ; i++)
            {
                WebBtn mWebBtnModel = mWebAppActivity.mWebBtns.get(i);
                if(i == 0)
                {
                    View view = LayoutInflater.from(mWebAppActivity).inflate(R.layout.btn1,null);
                    Button btn = (Button) view.findViewById(R.id.btn);
                    btn.setText(mWebBtnModel.name);
                    btn.setTag(mWebBtnModel.method);
                    btn.setOnClickListener(mWebAppActivity.clictBtn);
                    linelayer.addView(view);
                    view = LayoutInflater.from(mWebAppActivity).inflate(R.layout.webline,null);
                    linelayer.addView(view);
                }
                else if(i == mWebAppActivity.mWebBtns.size()-1)
                {
                    View view = LayoutInflater.from(mWebAppActivity).inflate(R.layout.btn3,null);
                    Button btn = (Button) view.findViewById(R.id.btn);
                    btn.setText(mWebBtnModel.name);
                    btn.setTag(mWebBtnModel.method);
                    btn.setOnClickListener(mWebAppActivity.clictBtn);
                    linelayer.addView(view);
                }
                else
                {
                    View view = LayoutInflater.from(mWebAppActivity).inflate(R.layout.btn4,null);
                    Button btn = (Button) view.findViewById(R.id.btn);
                    btn.setText(mWebBtnModel.name);
                    btn.setTag(mWebBtnModel.method);
                    btn.setOnClickListener(mWebAppActivity.clictBtn);
                    linelayer.addView(view);
                    view = LayoutInflater.from(mWebAppActivity).inflate(R.layout.webline,null);
                    linelayer.addView(view);
                }

            }
            View view = LayoutInflater.from(mWebAppActivity).inflate(R.layout.emptylayer,null);
            linelayer.addView(view);
            view = LayoutInflater.from(mWebAppActivity).inflate(R.layout.btn2,null);
            Button btn = (Button) view.findViewById(R.id.btn);
            btn.setText(mWebAppActivity.getString(R.string.button_word_cancle));
            btn.setOnClickListener(mWebAppActivity.mCancleListenter);
            linelayer.addView(view);
        }
        mWebAppActivity.mRelativeLayout.setVisibility(View.VISIBLE);
        mWebAppActivity.popupWindow1.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mWebAppActivity.mRelativeLayout.setVisibility(View.INVISIBLE);
            }
        });
        mWebAppActivity.popupWindow1.showAtLocation(mWebAppActivity.findViewById(R.id.activity_about),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    public void clickBtn(String method)
    {
        mWebAppActivity.mWebView.loadUrl("javascript:"+method+"()");
        mWebAppActivity.popupWindow1.dismiss();
//        AppUtils.showMessage(mWebAppActivity,"应用内token："+NetUtils.getInstance().token);
    }

    public void showAdd() {

        View popupWindowView = LayoutInflater.from(mWebAppActivity).inflate(R.layout.picpwindowmenu, null);
        RelativeLayout lsyer = (RelativeLayout) popupWindowView.findViewById(R.id.layer);
        lsyer.setFocusable(true);
        lsyer.setFocusableInTouchMode(true);
        mWebAppActivity.popupWindow1 = new PopupWindow(popupWindowView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
        popupWindowView.setFocusable(true);
        popupWindowView.setFocusableInTouchMode(true);
        mWebAppActivity.popupWindow1.setAnimationStyle(R.style.PopupAnimation);
        lsyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebAppActivity.popupWindow1.dismiss();

            }
        });
        ColorDrawable dw = new ColorDrawable(0x00ffffff);
        mWebAppActivity.popupWindow1.setBackgroundDrawable(dw);
        Button textview_edit = (Button) popupWindowView.findViewById(R.id.btn_pick_photo3);
        textview_edit.setOnClickListener(mWebAppActivity.mTakePhotoListenter);
        Button textview_delete = (Button) popupWindowView.findViewById(R.id.btn_take_photo);
        textview_delete.setOnClickListener(mWebAppActivity.mAddPicListener);
        Button textview_new = (Button) popupWindowView.findViewById(R.id.btn_cancel);
        textview_new.setOnClickListener(mWebAppActivity.mCancleListenter2);
        mWebAppActivity.mRelativeLayout.setVisibility(View.VISIBLE);
        mWebAppActivity.popupWindow1.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mWebAppActivity.mRelativeLayout.setVisibility(View.INVISIBLE);
            }
        });
        mWebAppActivity.popupWindow1.showAtLocation(mWebAppActivity.findViewById(R.id.activity_about),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    public void addPic() {
//        Intent mainIntent = new Intent();
//        mainIntent.putExtra("from", AlbumActivity.FORM_WEBAPP);
//        mainIntent.setClass(mWebAppActivity, PhotoDetialActivity.class);
//        mWebAppActivity.startActivity(mainIntent);
//        mWebAppActivity.popupWindow1.dismiss()
        Bus.callData(mWebAppActivity,"filetools/getPhotos",false,9,"intersky.function.view.activity.WebAppActivity",WebAppActivity.ACTION_WEBAPP_ADDPICTORE);
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setType( "video/*;image/*;audio/*");
//        mWebAppActivity.startActivityForResult(intent, Actions.CHOSE_PICTURE);
        mWebAppActivity.popupWindow1.dismiss();
    }


    public void takePhoto() {
        mWebAppActivity.popupWindow1.dismiss();
        mWebAppActivity.popupWindow1.dismiss();
        mWebAppActivity.permissionRepuest = (PermissionResult) Bus.callData(mWebAppActivity,"filetools/checkPermissionTakePhoto"
                ,mWebAppActivity,Bus.callData(mWebAppActivity,"filetools/getfilePath","/webapp/photo"));

    }

    private void clearUploadCallBack(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {//5.0以下
            if (mWebAppActivity.uploadMessage != null){
                mWebAppActivity.uploadMessage.onReceiveValue(null);
                mWebAppActivity.uploadMessage = null;
            }

        }else {
            if(mWebAppActivity.uploadMessageAboveL != null){
                mWebAppActivity.uploadMessageAboveL.onReceiveValue(null);
                mWebAppActivity.uploadMessageAboveL = null;
            }
        }
    }

    public void takePhotoResult(int requestCode, int resultCode, Intent data) {



        switch (requestCode) {
            case Actions.TAKE_PHOTO:
                File mFile = new File((String) Bus.callData(mWebAppActivity,"filetools/takePhotoUri",""));
                if (mFile.exists()) {
                    mWebAppActivity.mfilepath = mFile.getPath();
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
                        if (null == mWebAppActivity.uploadMessage && null == mWebAppActivity.uploadMessageAboveL) return;
                        Uri result = data == null || resultCode != Activity.RESULT_OK ? null : data.getData();
                        if (mWebAppActivity.uploadMessageAboveL != null) {
                            if (requestCode != Actions.CHOSE_PICTURE || mWebAppActivity.uploadMessageAboveL == null)
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
                                mWebAppActivity.uploadMessageAboveL.onReceiveValue(results);
                                mWebAppActivity.uploadMessageAboveL = null;
                            }
                            else
                            {
                                mWebAppActivity.uploadMessageAboveL.onReceiveValue(null);
                                mWebAppActivity.uploadMessageAboveL = null;
                            }

                        } else if (mWebAppActivity.uploadMessage != null) {
                            mWebAppActivity.uploadMessage = null;
                        }
                    }
                }
                else
                {
                    if (requestCode != Actions.CHOSE_PICTURE || mWebAppActivity.uploadMessageAboveL == null)
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
                        mWebAppActivity.uploadMessageAboveL.onReceiveValue(results);
                        mWebAppActivity.uploadMessageAboveL = null;
                    }
                    else
                    {
                        mWebAppActivity.uploadMessageAboveL.onReceiveValue(null);
                        mWebAppActivity.uploadMessageAboveL = null;
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
            if(mWebAppActivity.uploadMessageAboveL != null)
            mWebAppActivity.uploadMessageAboveL.onReceiveValue(uris);
        }
        else
        {
            mWebAppActivity.uploadMessage.onReceiveValue(null);
        }

    }



    public void startdownload(Attachment mAttachmentModel)
    {
        Bus.callData(mWebAppActivity,"filetools/startAttachment",mAttachmentModel);
    }

    public String getWebAppUrl(String path) {
        if (path.toLowerCase().startsWith("http") || path.toLowerCase().startsWith("https")) {
            return path;
        } else {
            if(FunctionUtils.getInstance().service.https == true)
            return "https://" + FunctionUtils.getInstance().service.sAddress.replaceAll(" ","") // HOST
                    + ":" + FunctionUtils.getInstance().service.sPort + path + "?" + "token=" + NetUtils.getInstance().token+"&cid="+ FunctionUtils.getInstance().account.mUCid;
            else
                return "http://" + FunctionUtils.getInstance().service.sAddress.replaceAll(" ","") // HOST
                        + ":" + FunctionUtils.getInstance().service.sPort + path + "?" + "token=" + NetUtils.getInstance().token+"&cid="+ FunctionUtils.getInstance().account.mUCid;
        }

    }

    public void openFileAction(String url,String size) {
        String name = url.substring(url.lastIndexOf("/")+1,url.length());
        String html = NetUtils.getInstance().praseUrl(FunctionUtils.getInstance().service,url);
        long usize = Long.valueOf(size);
        Attachment mAttachmentModel = new Attachment("", "",Bus.callData(mWebAppActivity,"filetools/getfilePath","/wbapp")+"/" +name,html,usize,0,"");
        startdownload(mAttachmentModel);
    }

    public void setpic(Intent intent) {
        ArrayList<Attachment> attachments = intent.getParcelableArrayListExtra("attachments");
        Uri[] uris = new Uri[attachments.size()];
        for(int i = 0 ; i < attachments.size() ; i++)
        {
            File cameraFile = new File(attachments.get(i).mPath);

            if(cameraFile.exists())
            {
                uris[i] = Uri.fromFile(cameraFile);

            }

        }
        if(mWebAppActivity.uploadMessageAboveL != null)
            mWebAppActivity.uploadMessageAboveL.onReceiveValue(uris);
        else
        {
            mWebAppActivity.uploadMessage.onReceiveValue(null);
        }
    }
}
