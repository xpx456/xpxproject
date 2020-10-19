package com.bigwiner.android.presenter;

import android.annotation.SuppressLint;
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
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigwiner.R;
import com.bigwiner.android.asks.ConversationAsks;
import com.bigwiner.android.handler.WebMessageHandler;
import com.bigwiner.android.prase.ConversationPrase;
import com.bigwiner.android.receiver.WebMessageReceiver;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.MainActivity;
import com.bigwiner.android.view.activity.WebMessageActivity;
import com.umeng.commonsdk.debug.I;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import intersky.appbase.Actions;
import intersky.appbase.PermissionResult;
import intersky.appbase.Presenter;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Attachment;
import intersky.appbase.entity.ShareItem;
import intersky.apputils.AppUtils;
import intersky.function.receiver.entity.WebBtn;
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
//        ToolBarHelper.setSutColor(mWebMessageActivity, Color.argb(0, 255, 255, 255));
        mWebMessageActivity.setContentView(R.layout.activity_webdetial);
//        mWebMessageActivity.mToolBarHelper.hidToolbar(mWebMessageActivity, (RelativeLayout) mWebMessageActivity.findViewById(R.id.buttomaciton));
//        mWebMessageActivity.measureStatubar(mWebMessageActivity, (RelativeLayout) mWebMessageActivity.findViewById(R.id.stutebar));
        mWebMessageActivity.mToolBarHelper.hidToolbar2(mWebMessageActivity);
        ToolBarHelper.setBgColor(mWebMessageActivity, mWebMessageActivity.mActionBar, Color.rgb(255, 255, 255));
        mWebMessageActivity.btnBach = mWebMessageActivity.findViewById(R.id.back);
        mWebMessageActivity.title = mWebMessageActivity.findViewById(R.id.title);
        mWebMessageActivity.shareBtn = mWebMessageActivity.findViewById(R.id.share);
        if(mWebMessageActivity.getIntent().getBooleanExtra("showshare",false) == false)
        {
            mWebMessageActivity.shareBtn.setVisibility(View.INVISIBLE);
        }
        else
        {
            ShareItem shareItem = new ShareItem();
            shareItem.picurl = mWebMessageActivity.getIntent().getStringExtra("img");
            shareItem.title = mWebMessageActivity.getIntent().getStringExtra("des");
            shareItem.des = " ";
            shareItem.weburl = ConversationPrase.prseShareUrl(ConversationAsks.praseShareType(mWebMessageActivity.getIntent().getStringExtra("type"))
                    ,mWebMessageActivity.getIntent().getStringExtra("detialid"));
            mWebMessageActivity.shareBtn.setOnClickListener(new BigwinerApplication.DoshareListener(mWebMessageActivity,shareItem));
        }
        mWebMessageActivity.btnBach.setOnClickListener(mWebMessageActivity.backListener);

        mWebMessageActivity.mWebView = (WebView) mWebMessageActivity.findViewById(R.id.webview);
        BigwinerApplication.mApp.hisupdata = true;
        initWebViewSettings();
        String url = mWebMessageActivity.getIntent().getStringExtra("url");
        mWebMessageActivity.mWebView.loadUrl(mWebMessageActivity.getIntent().getStringExtra("url"));
        mWebMessageActivity.mRelativeLayout = (RelativeLayout) mWebMessageActivity.findViewById(R.id.shade);

        if(mWebMessageActivity.getIntent().hasExtra("detialid"))
        {
            Intent intent = new Intent(MainActivity.ACTION_SET_READ_CONVERSATION);
            intent.putExtra("id",mWebMessageActivity.getIntent().getStringExtra("detialid"));
            intent.setPackage(BigwinerApplication.mApp.getPackageName());
            mWebMessageActivity.sendBroadcast(intent);
        }

    }


    public void initWebViewSettings() {

        WebSettings settings = mWebMessageActivity.mWebView.getSettings();
        mWebMessageActivity.initclint();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setDomStorageEnabled(true);
        settings.setBlockNetworkImage(true);
        if(NetUtils.checkNetWorkState(mWebMessageActivity))
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        else
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
        mWebMessageActivity.mWebView.setWebViewClient(new WebViewClient() {
            // 新开页面时用自己定义的webview来显示，不用系统自带的浏览器来显示
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                // 当有新连接时使用当前的webview进行显示
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
                settings.setBlockNetworkImage(false);
                super.onPageFinished(view, url);
                mWebMessageActivity.waitDialog.hide();

            }

            // 加载错误时要做的工作
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            }
        });
//        mWebMessageActivity.mWebView.addJavascriptInterface(mWebMessageActivity, "android");
        mWebMessageActivity.mWebView.setWebChromeClient(new WebChromeClient() {//允许有alert弹出框
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if(mWebMessageActivity.alttitle.contains("没有问卷调查"))
                mWebMessageActivity.finish();
            }

            /**
             * 处理alert弹出框
             */
            @Override
            public boolean onJsAlert(WebView view, String url,
                                     String message, JsResult result) {
                mWebMessageActivity.alttitle = message;
                return super.onJsAlert(view,url,message, result);
//
            }

            /**
             * 处理confirm弹出框
             */
            @Override
            public boolean onJsConfirm(WebView view, String url, String message,
                                       JsResult result) {
                //对confirm的简单封装
                return super.onJsConfirm(view, url, message, result);
                //如果采用下面的代码会另外再弹出个消息框，目前不知道原理
                //
            }

            /**
             * 处理prompt弹出框
             */
            @Override
            public boolean onJsPrompt(WebView view, String url, String message,
                                      String defaultValue, JsPromptResult result) {
                result.confirm();
                return super.onJsPrompt(view, url, message, message, result);
            }

        });
    }



//    public void onSearch() {
//        String url = mWebMessageActivity.mSearchViewLayout.getText();
//        if (url.length() > 0)
//            mWebMessageActivity.mWebView.loadUrl(url);
//    }
//
//
//    public void startScan()
//    {
//        mWebMessageActivity.permissionRepuest = ScanUtils.getInstance().checkStartScan(mWebMessageActivity, "");
//    }
//
//
//    public void showCode(String code) {
//        mWebMessageActivity.mWebView.loadUrl("javascript:javacalljswith(" + code + ")");
//        AppUtils.showMessage(mWebMessageActivity, "android 扫到条形码：" + code);
//    }
//
//    public void showCode2(String code) {
//
//        try {
//            JSONObject jsonObject = new JSONObject(mWebMessageActivity.json);
//            String method = jsonObject.getString("method");
//            JSONObject arg = new JSONObject();
//            arg.put("result",code);
//            arg.put("params",jsonObject.getJSONObject("params"));
//            mWebMessageActivity.mWebView.loadUrl("javascript:"+method+"(" + arg.toString() + ")");
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//            AppUtils.showMessage(mWebMessageActivity,"josn格式错误");
//        }
//
//
//    }

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


//    public void dodismiss1() {
//        mWebMessageActivity.popupWindow1.dismiss();
//    }
//
//    public void dodismiss2()
//    {
//        mWebMessageActivity.popupWindow1.dismiss();
//        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
//        {
//            mWebMessageActivity.uploadMessage.onReceiveValue(null);
//        }
//        else
//        {
//            mWebMessageActivity.uploadMessageAboveL.onReceiveValue(null);
//        }
//
//    }

//    public void showMore() {
//
//        View popupWindowView = LayoutInflater.from(mWebMessageActivity).inflate(R.layout.webheadbtns, null);
//        RelativeLayout lsyer = (RelativeLayout) popupWindowView.findViewById(R.id.layer);
//        lsyer.setFocusable(true);
//        lsyer.setFocusableInTouchMode(true);
//        mWebMessageActivity.popupWindow1 = new PopupWindow(popupWindowView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
//        popupWindowView.setFocusable(true);
//        popupWindowView.setFocusableInTouchMode(true);
//        mWebMessageActivity.popupWindow1.setAnimationStyle(R.style.PopupAnimation);
//        lsyer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mWebMessageActivity.popupWindow1.dismiss();
//
//            }
//        });
//        ColorDrawable dw = new ColorDrawable(0x00ffffff);
//        mWebMessageActivity.popupWindow1.setBackgroundDrawable(dw);
//        LinearLayout linelayer = (LinearLayout) popupWindowView.findViewById(R.id.pop_layout);
//        if(mWebMessageActivity.mWebBtns.size() == 1)
//        {
//            WebBtn mWebBtnModel = mWebMessageActivity.mWebBtns.get(0);
//            View view = LayoutInflater.from(mWebMessageActivity).inflate(R.layout.btn2,null);
//            Button btn = (Button) view.findViewById(R.id.btn);
//            btn.setText(mWebBtnModel.name);
//            btn.setTag(mWebBtnModel.method);
//            btn.setOnClickListener(mWebMessageActivity.clictBtn);
//            linelayer.addView(view);
//            view = LayoutInflater.from(mWebMessageActivity).inflate(R.layout.emptylayer,null);
//            linelayer.addView(view);
//            view = LayoutInflater.from(mWebMessageActivity).inflate(R.layout.btn2,null);
//            btn = (Button) view.findViewById(R.id.btn);
//            btn.setText(mWebMessageActivity.getString(R.string.button_word_cancle));
//            btn.setOnClickListener(mWebMessageActivity.mCancleListenter);
//            linelayer.addView(view);
//
//        }
//        else if(mWebMessageActivity.mWebBtns.size() == 2)
//        {
//            WebBtn mWebBtnModel = mWebMessageActivity.mWebBtns.get(0);
//            WebBtn mWebBtnModel2 = mWebMessageActivity.mWebBtns.get(1);
//            View view = LayoutInflater.from(mWebMessageActivity).inflate(R.layout.btn1,null);
//            Button btn = (Button) view.findViewById(R.id.btn);
//            btn.setText(mWebBtnModel.name);
//            btn.setTag(mWebBtnModel.method);
//            btn.setOnClickListener(mWebMessageActivity.clictBtn);
//            linelayer.addView(view);
//            view = LayoutInflater.from(mWebMessageActivity).inflate(R.layout.webline,null);
//            linelayer.addView(view);
//             view = LayoutInflater.from(mWebMessageActivity).inflate(R.layout.btn3,null);
//             btn = (Button) view.findViewById(R.id.btn);
//            btn.setText(mWebBtnModel2.name);
//            btn.setTag(mWebBtnModel2.method);
//            btn.setOnClickListener(mWebMessageActivity.clictBtn);
//            linelayer.addView(view);
//            view = LayoutInflater.from(mWebMessageActivity).inflate(R.layout.emptylayer,null);
//            linelayer.addView(view);
//            view = LayoutInflater.from(mWebMessageActivity).inflate(R.layout.btn2,null);
//            btn = (Button) view.findViewById(R.id.btn);
//            btn.setText(mWebMessageActivity.getString(R.string.button_word_cancle));
//            btn.setOnClickListener(mWebMessageActivity.mCancleListenter);
//            linelayer.addView(view);
//;
//        }
//        else
//        {
//            for(int i = 0 ; i < mWebMessageActivity.mWebBtns.size() ; i++)
//            {
//                WebBtn mWebBtnModel = mWebMessageActivity.mWebBtns.get(i);
//                if(i == 0)
//                {
//                    View view = LayoutInflater.from(mWebMessageActivity).inflate(R.layout.btn1,null);
//                    Button btn = (Button) view.findViewById(R.id.btn);
//                    btn.setText(mWebBtnModel.name);
//                    btn.setTag(mWebBtnModel.method);
//                    btn.setOnClickListener(mWebMessageActivity.clictBtn);
//                    linelayer.addView(view);
//                    view = LayoutInflater.from(mWebMessageActivity).inflate(R.layout.webline,null);
//                    linelayer.addView(view);
//                }
//                else if(i == mWebMessageActivity.mWebBtns.size()-1)
//                {
//                    View view = LayoutInflater.from(mWebMessageActivity).inflate(R.layout.btn3,null);
//                    Button btn = (Button) view.findViewById(R.id.btn);
//                    btn.setText(mWebBtnModel.name);
//                    btn.setTag(mWebBtnModel.method);
//                    btn.setOnClickListener(mWebMessageActivity.clictBtn);
//                    linelayer.addView(view);
//                }
//                else
//                {
//                    View view = LayoutInflater.from(mWebMessageActivity).inflate(R.layout.btn4,null);
//                    Button btn = (Button) view.findViewById(R.id.btn);
//                    btn.setText(mWebBtnModel.name);
//                    btn.setTag(mWebBtnModel.method);
//                    btn.setOnClickListener(mWebMessageActivity.clictBtn);
//                    linelayer.addView(view);
//                    view = LayoutInflater.from(mWebMessageActivity).inflate(R.layout.webline,null);
//                    linelayer.addView(view);
//                }
//
//            }
//            View view = LayoutInflater.from(mWebMessageActivity).inflate(R.layout.emptylayer,null);
//            linelayer.addView(view);
//            view = LayoutInflater.from(mWebMessageActivity).inflate(R.layout.btn2,null);
//            Button btn = (Button) view.findViewById(R.id.btn);
//            btn.setText(mWebMessageActivity.getString(R.string.button_word_cancle));
//            btn.setOnClickListener(mWebMessageActivity.mCancleListenter);
//            linelayer.addView(view);
//        }
//        mWebMessageActivity.mRelativeLayout.setVisibility(View.VISIBLE);
//        mWebMessageActivity.popupWindow1.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                mWebMessageActivity.mRelativeLayout.setVisibility(View.INVISIBLE);
//            }
//        });
//        mWebMessageActivity.popupWindow1.showAtLocation(mWebMessageActivity.findViewById(R.id.activity_video),
//                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//    }

//    public void clickBtn(String method)
//    {
//        mWebMessageActivity.mWebView.loadUrl("javascript:"+method+"()");
//        mWebMessageActivity.popupWindow1.dismiss();
//    }

//    public void showAdd() {
//
//        View popupWindowView = LayoutInflater.from(mWebMessageActivity).inflate(R.layout.picpwindowmenu, null);
//        RelativeLayout lsyer = (RelativeLayout) popupWindowView.findViewById(R.id.layer);
//        lsyer.setFocusable(true);
//        lsyer.setFocusableInTouchMode(true);
//        mWebMessageActivity.popupWindow1 = new PopupWindow(popupWindowView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
//        popupWindowView.setFocusable(true);
//        popupWindowView.setFocusableInTouchMode(true);
//        mWebMessageActivity.popupWindow1.setAnimationStyle(R.style.PopupAnimation);
//        lsyer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mWebMessageActivity.popupWindow1.dismiss();
//
//            }
//        });
//        ColorDrawable dw = new ColorDrawable(0x00ffffff);
//        mWebMessageActivity.popupWindow1.setBackgroundDrawable(dw);
//        Button textview_edit = (Button) popupWindowView.findViewById(R.id.btn_pick_photo3);
//        textview_edit.setOnClickListener(mWebMessageActivity.mTakePhotoListenter);
//        Button textview_delete = (Button) popupWindowView.findViewById(R.id.btn_take_photo);
//        textview_delete.setOnClickListener(mWebMessageActivity.mAddPicListener);
//        Button textview_new = (Button) popupWindowView.findViewById(R.id.btn_cancel);
//        textview_new.setOnClickListener(mWebMessageActivity.mCancleListenter2);
//        mWebMessageActivity.mRelativeLayout.setVisibility(View.VISIBLE);
//        mWebMessageActivity.popupWindow1.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                mWebMessageActivity.mRelativeLayout.setVisibility(View.INVISIBLE);
//            }
//        });
//        mWebMessageActivity.popupWindow1.showAtLocation(mWebMessageActivity.findViewById(R.id.activity_video),
//                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//    }

//    public void addPic() {
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setType( "video/*;image/*;audio/*");
//        mWebMessageActivity.startActivityForResult(intent, Actions.CHOSE_PICTURE);
//        mWebMessageActivity.popupWindow1.dismiss();
//    }


//    public void takePhoto() {
//        mWebMessageActivity.popupWindow1.dismiss();
//        mWebMessageActivity.permissionRepuest = (PermissionResult) Bus.callData(mWebMessageActivity,"filetools/checkPermissionTakePhoto"
//                ,mWebMessageActivity,Bus.callData(mWebMessageActivity,"filetools/getfilePath","webmessage/photo"));
//    }

//    private void clearUploadCallBack(){
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {//5.0以下
//            if (mWebMessageActivity.uploadMessage != null){
//                mWebMessageActivity.uploadMessage.onReceiveValue(null);
//                mWebMessageActivity.uploadMessage = null;
//            }
//
//        }else {
//            if(mWebMessageActivity.uploadMessageAboveL != null){
//                mWebMessageActivity.uploadMessageAboveL.onReceiveValue(null);
//                mWebMessageActivity.uploadMessageAboveL = null;
//            }
//        }
//    }

//    public void takePhotoResult(int requestCode, int resultCode, Intent data) {
//
//
//
//        switch (requestCode) {
//            case Actions.TAKE_PHOTO:
//                File mFile = new File((String) Bus.callData(mWebMessageActivity,"filetools/takePhotoUri",""));
//                if (mFile.exists()) {
//                    mWebMessageActivity.mfilepath = mFile.getPath();
//                    doupload(mFile.getPath());
//                }
//                else
//                {
//                    clearUploadCallBack();
//                }
//                break;
//            case Actions.CHOSE_PICTURE:
//                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
//                {
//                    if (requestCode == Actions.CHOSE_PICTURE) {
//                        if (null == mWebMessageActivity.uploadMessage && null == mWebMessageActivity.uploadMessageAboveL) return;
//                        Uri result = data == null || resultCode != Activity.RESULT_OK ? null : data.getData();
//                        if (mWebMessageActivity.uploadMessageAboveL != null) {
//                            if (requestCode != Actions.CHOSE_PICTURE || mWebMessageActivity.uploadMessageAboveL == null)
//                                return;
//                            Uri[] results = null;
//                            if (resultCode == Activity.RESULT_OK) {
//                                if (data != null) {
//                                    String dataString = data.getDataString();
//                                    ClipData clipData = data.getClipData();
//                                    if (clipData != null) {
//                                        results = new Uri[clipData.getItemCount()];
//                                        for (int i = 0; i < clipData.getItemCount(); i++) {
//                                            ClipData.Item item = clipData.getItemAt(i);
//                                            results[i] = item.getUri();
//                                        }
//                                    }
//                                    if (dataString != null)
//                                        results = new Uri[]{Uri.parse(dataString)};
//                                }
//                                mWebMessageActivity.uploadMessageAboveL.onReceiveValue(results);
//                                mWebMessageActivity.uploadMessageAboveL = null;
//                            }
//                            else
//                            {
//                                mWebMessageActivity.uploadMessageAboveL.onReceiveValue(null);
//                                mWebMessageActivity.uploadMessageAboveL = null;
//                            }
//
//                        } else if (mWebMessageActivity.uploadMessage != null) {
//                            mWebMessageActivity.uploadMessage = null;
//                        }
//                    }
//                }
//                else
//                {
//                    if (requestCode != Actions.CHOSE_PICTURE || mWebMessageActivity.uploadMessageAboveL == null)
//                        return;
//                    Uri[] results = null;
//                    if (resultCode == Activity.RESULT_OK) {
//                        if (data != null) {
//                            String dataString = data.getDataString();
//                            ClipData clipData = data.getClipData();
//                            if (clipData != null) {
//                                results = new Uri[clipData.getItemCount()];
//                                for (int i = 0; i < clipData.getItemCount(); i++) {
//                                    ClipData.Item item = clipData.getItemAt(i);
//                                    results[i] = item.getUri();
//                                }
//                            }
//                            if (dataString != null)
//                                results = new Uri[]{Uri.parse(dataString)};
//                        }
//                        mWebMessageActivity.uploadMessageAboveL.onReceiveValue(results);
//                        mWebMessageActivity.uploadMessageAboveL = null;
//                    }
//                    else
//                    {
//                        mWebMessageActivity.uploadMessageAboveL.onReceiveValue(null);
//                        mWebMessageActivity.uploadMessageAboveL = null;
//                    }
//                }
//                break;
//        }
//    }

//    public void doupload(String path)
//    {
//        File cameraFile = new File(path);
//        Uri[] uris = new Uri[1];
//        if(cameraFile.exists())
//        {
//            uris[0] = Uri.fromFile(cameraFile);
//            if(mWebMessageActivity.uploadMessageAboveL != null)
//            mWebMessageActivity.uploadMessageAboveL.onReceiveValue(uris);
//        }
//        else
//        {
//            mWebMessageActivity.uploadMessage.onReceiveValue(null);
//        }
//
//    }


//    public void startdownload(Attachment mAttachmentModel)
//    {
//        Bus.callData(mWebMessageActivity,"filetools/startAttachment",mAttachmentModel);
//    }
}
