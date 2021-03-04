package com.bigwiner.android.view.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.bigwiner.R;
import com.bigwiner.android.asks.DetialAsks;
import com.bigwiner.android.presenter.MainPresenter;
import com.bigwiner.android.view.BigwinerApplication;
import com.bigwiner.android.view.activity.MainActivity;

import java.io.File;
import java.util.ArrayList;

import intersky.appbase.Actions;
import intersky.appbase.BaseActivity;
import intersky.appbase.BaseFragment;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Attachment;
import intersky.apputils.AppUtils;
import intersky.apputils.MenuItem;
import intersky.mywidget.MyGridView;

public class ToolsFragment extends BaseFragment {

//    public MainPresenter mMainPresenter;
    public ArrayList<MyGridView> mGridViews = new ArrayList<MyGridView>();
    public UploadHandler mUploadHandler;
    public WebView mWebView;
    public PopupWindow popupWindow1;
    public String mfilepath;
    public ValueCallback<Uri> uploadMessage;
    public ValueCallback<Uri[]> uploadMessageAboveL;
    public MainActivity mMainActivity;


    public ToolsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_tools2, container, false);
        mMainActivity = (MainActivity) getActivity();
        measureStatubar(mMainActivity, (RelativeLayout) mView.findViewById(R.id.stutebar));
        mWebView = (WebView) mView.findViewById(R.id.webview);
        String urlString = BigwinerApplication.BASE_NET_PATH+ DetialAsks.TOOL_PATH;
        initWebViewSettings();
        mWebView.loadUrl(urlString);

        return mView;
    }

    public void initWebViewSettings() {

        WebSettings settings = mWebView.getSettings();
        initclint();
//        settings.setDefaultFontSize(50);
//        settings.setDefaultFixedFontSize(30);
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setDomStorageEnabled(true);
//        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(true);
        mWebView.addJavascriptInterface(new JavascriptInterface(getActivity()), "imagelistener");
        // WebView inside Browser doesn't want initial focus to be set.
//        settings.setNeedInitialFocus(false);
        // Browser supports multiple windows
//        settings.setSupportMultipleWindows(true);
        // enable smooth transition for better performance during panning or
        mWebView.setWebViewClient(new WebViewClient() {
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
                mMainActivity.waitDialog.show();
                Log.e("initcxApistart",url);
                super.onPageStarted(view, url, favicon);
            }

            // 加载完成时要做的工作
            public void onPageFinished(WebView view, String url) {
                view.setVisibility(View.VISIBLE);
                super.onPageFinished(view, url);
                mMainActivity.waitDialog.hide();
                Log.e("initcxApifinish",url);
                Log.e("initcxApi","initcxApi success");
//                mWebView.loadUrl("javascript:initcxApi()");
                addImageClickListener(view);
            }

            // 加载错误时要做的工作
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            }
        });
        mWebView.addJavascriptInterface(mMainActivity, "android");
    }

    private class JavascriptInterface {

        private Context context;

        public JavascriptInterface(Context context) {
            this.context = context;
        }

        @android.webkit.JavascriptInterface
        public void openImage(String img) {
            if(!img.contains("earth.png")) {
                AppUtils.showMessage(mMainActivity,"该功能开发中,尽情期待");
            }

        }

    }

    public void initclint()
    {
        mWebView.setWebChromeClient(new MyChromeViewClient());
        mWebView.setWebViewClient(new MyWebViewClinet());
        mWebView.setDownloadListener(new MyWebViewDownLoadListener());
//		mWebView.setWebViewClient(new WebViewClient());
    }

    public class MyChromeViewClient extends WebChromeClient {


        @Override
        public void onCloseWindow(WebView window) {
            super.onCloseWindow(window);
        }

        public void onProgressChanged(WebView view, final int progress) {

        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {

            new AlertDialog.Builder(mMainActivity).setTitle("提示信息").setMessage(message)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm();
                        }
                    }).setCancelable(false).create().show();
            return true;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {

            new AlertDialog.Builder(mMainActivity).setTitle("提示信息").setMessage(message)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm();
                        }
                    }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    result.cancel();
                }
            }).setCancelable(false).create().show();
            return true;

        }

        // Android 2.x
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            openFileChooser(uploadMsg, "");
        }

        // Android 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            openFileChooser(uploadMsg, "", "filesystem");
        }

        // Android 4.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {

            uploadMessage = uploadMsg;
            mUploadHandler.openFileChooser(uploadMsg, acceptType, capture);
        }

        @Override
        public boolean onShowFileChooser(WebView webView,
                                         ValueCallback<Uri[]> filePathCallback,
                                         FileChooserParams fileChooserParams) {
            uploadMessageAboveL = filePathCallback;
            showAdd();
//			return super.onShowFileChooser(webView, filePathCallback,
//					fileChooserParams);
            return true;
        }
    }

    public void showAdd() {

        ArrayList<MenuItem> items = new ArrayList<MenuItem>();
        MenuItem item1 = new MenuItem();
        item1.btnName = mMainActivity.getString(R.string.my_take_photo);
        item1.mListener = mTakePhotoListenter;
        items.add(item1);
        item1 = new MenuItem();
        item1.btnName = mMainActivity.getString(R.string.my_photo_select);
        item1.mListener = mAddPicListener;
        items.add(item1);
        mMainActivity.popupWindow = AppUtils.creatButtomMenu(mMainActivity, mMainActivity.shade, items, mMainActivity.findViewById(R.id.headlayer));
    }

    public View.OnClickListener mTakePhotoListenter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean isbg = (boolean) v.getTag();
            if(isbg)
                BigwinerApplication.mApp.mFileUtils.checkPermissionTakePhoto(mMainActivity, BigwinerApplication.mApp.mFileUtils.pathUtils.getfilePath("bg/photo"), MainActivity.TAKE_PHOTO_BG);
            else
                BigwinerApplication.mApp.mFileUtils.checkPermissionTakePhoto(mMainActivity, BigwinerApplication.mApp.mFileUtils.pathUtils.getfilePath("head/photo"),MainActivity.TAKE_PHOTO_HEAD);
            mMainActivity.popupWindow.dismiss();
        }
    };

    public View.OnClickListener mAddPicListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean isbg = (boolean) v.getTag();
            if(isbg)
                BigwinerApplication.mApp.mFileUtils.selectPhotos(mMainActivity,MainActivity.CHOSE_PICTURE_BG);
            else
                BigwinerApplication.mApp.mFileUtils.selectPhotos(mMainActivity,MainActivity.CHOSE_PICTURE_HEAD);
            mMainActivity.popupWindow.dismiss();
        }
    };

    public class MyWebViewClinet extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

    }

    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override

        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,

                                    long contentLength) {

            if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){

                return;

            }
            String name;
            if(contentDisposition.contains("filename="))
            {
                name = contentDisposition.substring(contentDisposition.indexOf("=")+1,contentDisposition.length());
            }
            else
            {
                name = "temp.dat";
            }
            String file = name;
            Attachment mAttachmentModel = new Attachment("",name,
                    Bus.callData(mMainActivity,"filetools/getfilePath","webmessage/photo")+"/"+name,url,0,Long.valueOf(contentLength),"");
            startdownload(mAttachmentModel);
            //attachment;filename=采购合同批量.xls
//			DownloaderTask task=new DownloaderTask();
//
//			task.execute(url);

        }

    }

    public class UploadHandler {
        /*
         * The Object used to inform the WebView of the file to upload.
         */
        private ValueCallback<Uri> mUploadMessage;
        private String mCameraFilePath;
        private boolean mHandled;
        private boolean mCaughtActivityNotFoundException;
        private Controller mController;

        public UploadHandler(Controller controller) {
            mController = controller;
        }

        public String getFilePath() {
            return mCameraFilePath;
        }

        boolean handled() {
            return mHandled;
        }

        public void onResult(int resultCode, Intent intent) {
            if (resultCode == Activity.RESULT_CANCELED && mCaughtActivityNotFoundException) {
                // Couldn't resolve an activity, we are going to try again so skip
                // this result.
                mCaughtActivityNotFoundException = false;
                return;
            }
            Uri result = (intent == null || resultCode != Activity.RESULT_OK) ? null : intent.getData();

            // As we ask the camera to save the result of the user taking
            // a picture, the camera application does not return anything other
            // than RESULT_OK. So we need to check whether the file we expected
            // was written to disk in the in the case that we
            // did not get an intent returned but did get a RESULT_OK. If it was,
            // we assume that this result has came back from the camera.
            if (result == null && intent == null && resultCode == Activity.RESULT_OK) {
                File cameraFile = new File(mCameraFilePath);
                if (cameraFile.exists()) {
                    result = Uri.fromFile(cameraFile);
                    // Broadcast to the media scanner that we have a new photo
                    // so it will be added into the gallery for the user.
                    Intent intent1 = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, result);
                    intent1.setPackage(BigwinerApplication.mApp.getPackageName());
                    mController.getActivity().sendBroadcast(intent1);
                }
            }
            mUploadMessage.onReceiveValue(result);
            mHandled = true;
            mCaughtActivityNotFoundException = false;
        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            final String imageMimeType = "image/*";
            final String videoMimeType = "video/*";
            final String audioMimeType = "audio/*";
            final String mediaSourceKey = "capture";
            final String mediaSourceValueCamera = "camera";
            final String mediaSourceValueFileSystem = "filesystem";
            final String mediaSourceValueCamcorder = "camcorder";
            final String mediaSourceValueMicrophone = "microphone";
            // According to the spec, media source can be 'filesystem' or 'camera' or 'camcorder'
            // or 'microphone' and the default value should be 'filesystem'.
            String mediaSource = mediaSourceValueFileSystem;
            if (mUploadMessage != null) {
                // Already a file picker operation in progress.
                return;
            }
            mUploadMessage = uploadMsg;
            // Parse the accept type.
            String params[] = acceptType.split(";");
            String mimeType = params[0];
            if (capture.length() > 0) {
                mediaSource = capture;
            }
            if (capture.equals(mediaSourceValueFileSystem)) {
                // To maintain backwards compatibility with the previous implementation
                // of the media capture API, if the value of the 'capture' attribute is
                // "filesystem", we should examine the accept-type for a MIME type that
                // may specify a different capture value.
                for (String p : params) {
                    String[] keyValue = p.split("=");
                    if (keyValue.length == 2) {
                        // Process key=value parameters.
                        if (mediaSourceKey.equals(keyValue[0])) {
                            mediaSource = keyValue[1];
                        }
                    }
                }
            }
            //Ensure it is not still set from a previous upload.
            mCameraFilePath = null;
            if (mimeType.equals(imageMimeType)) {
                if (mediaSource.equals(mediaSourceValueCamera)) {
                    // Specified 'image/*' and requested the camera, so go ahead and launch the
                    // camera directly.
                    startActivity(createCameraIntent());
                    return;
                } else {
                    // Specified just 'image/*', capture=filesystem, or an invalid capture parameter.
                    // In all these cases we show a traditional picker filetered on accept type
                    // so launch an intent for both the Camera and image/* OPENABLE.
                    Intent chooser = createChooserIntent(createCameraIntent());
                    chooser.putExtra(Intent.EXTRA_INTENT, createOpenableIntent(imageMimeType));
                    startActivity(chooser);
                    return;
                }
            } else if (mimeType.equals(videoMimeType)) {
                if (mediaSource.equals(mediaSourceValueCamcorder)) {
                    // Specified 'video/*' and requested the camcorder, so go ahead and launch the
                    // camcorder directly.
                    startActivity(createCamcorderIntent());
                    return;
                } else {
                    // Specified just 'video/*', capture=filesystem or an invalid capture parameter.
                    // In all these cases we show an intent for the traditional file picker, filtered
                    // on accept type so launch an intent for both camcorder and video/* OPENABLE.
                    Intent chooser = createChooserIntent(createCamcorderIntent());
                    chooser.putExtra(Intent.EXTRA_INTENT, createOpenableIntent(videoMimeType));
                    startActivity(chooser);
                    return;
                }
            } else if (mimeType.equals(audioMimeType)) {
                if (mediaSource.equals(mediaSourceValueMicrophone)) {
                    // Specified 'audio/*' and requested microphone, so go ahead and launch the sound
                    // recorder.
                    startActivity(createSoundRecorderIntent());
                    return;
                } else {
                    // Specified just 'audio/*',  capture=filesystem of an invalid capture parameter.
                    // In all these cases so go ahead and launch an intent for both the sound
                    // recorder and audio/* OPENABLE.
                    Intent chooser = createChooserIntent(createSoundRecorderIntent());
                    chooser.putExtra(Intent.EXTRA_INTENT, createOpenableIntent(audioMimeType));
                    startActivity(chooser);
                    return;
                }
            }
            // No special handling based on the accept type was necessary, so trigger the default
            // file upload chooser.
            startActivity(createDefaultOpenableIntent());
        }

        private void startActivity(Intent intent) {
            try {
                mController.getActivity().startActivityForResult(intent, Controller.FILE_SELECTED);
            } catch (ActivityNotFoundException e) {
                // No installed app was able to handle the intent that
                // we sent, so fallback to the default file upload control.
                try {
                    mCaughtActivityNotFoundException = true;
                    mController.getActivity().startActivityForResult(createDefaultOpenableIntent(),
                            Controller.FILE_SELECTED);
                } catch (ActivityNotFoundException e2) {
                    // Nothing can return us a file, so file upload is effectively disabled.
                    //Toast.makeText(mController.getActivity(), "File uploads are disabled.", Toast.LENGTH_LONG).show();
                }
            }
        }

        private Intent createDefaultOpenableIntent() {
            // Create and return a chooser with the default OPENABLE
            // actions including the camera, camcorder and sound
            // recorder where available.
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            Intent chooser = createChooserIntent(createCameraIntent(), createCamcorderIntent(),
                    createSoundRecorderIntent());
            chooser.putExtra(Intent.EXTRA_INTENT, i);
            return chooser;
        }

        private Intent createChooserIntent(Intent... intents) {
            Intent chooser = new Intent(Intent.ACTION_CHOOSER);
            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents);
            chooser.putExtra(Intent.EXTRA_TITLE, "Choose file for upload");
            return chooser;
        }

        private Intent createOpenableIntent(String type) {
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType(type);
            return i;
        }

        private Intent createCameraIntent() {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File externalDataDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            File cameraDataDir = new File(externalDataDir.getAbsolutePath() + File.separator + "browser-photos");
            cameraDataDir.mkdirs();
            mCameraFilePath = cameraDataDir.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg";
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mCameraFilePath)));
            return cameraIntent;
        }

        private Intent createCamcorderIntent() {
            return new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        }

        private Intent createSoundRecorderIntent() {
            return new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        }
    }

    class Controller {

        final static int FILE_SELECTED = 4;

        Activity getActivity() {
            return mMainActivity;
        }
    }

    public void startdownload(Attachment mAttachmentModel)
    {
        Bus.callData(mMainActivity,"filetools/startAttachment",mAttachmentModel);
    }

    public void doupload(String path)
    {
        File cameraFile = new File(path);
        Uri[] uris = new Uri[1];
        if(cameraFile.exists())
        {
            uris[0] = Uri.fromFile(cameraFile);
            if(uploadMessageAboveL != null)
                uploadMessageAboveL.onReceiveValue(uris);
        }
        else
        {
            uploadMessage.onReceiveValue(null);
        }

    }

    public void showCode(String code) {
        mWebView.loadUrl("javascript:javacalljswith(" + code + ")");
        AppUtils.showMessage(mMainActivity, "android 扫到条形码：" + code);
    }

    public void showCode2(String code) {

    }

    public void takePhotoResult(int requestCode, int resultCode, Intent data) {



        switch (requestCode) {
            case Actions.TAKE_PHOTO:
                File mFile = new File((String) Bus.callData(mMainActivity,"filetools/takePhotoUri",""));
                if (mFile.exists()) {
                    mfilepath = mFile.getPath();
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
                        if (null == uploadMessage && null == uploadMessageAboveL) return;
                        Uri result = data == null || resultCode != Activity.RESULT_OK ? null : data.getData();
                        if (uploadMessageAboveL != null) {
                            if (requestCode != Actions.CHOSE_PICTURE || uploadMessageAboveL == null)
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
                                uploadMessageAboveL.onReceiveValue(results);
                                uploadMessageAboveL = null;
                            }
                            else
                            {
                                uploadMessageAboveL.onReceiveValue(null);
                                uploadMessageAboveL = null;
                            }

                        } else if (uploadMessage != null) {
                            uploadMessage = null;
                        }
                    }
                }
                else
                {
                    if (requestCode != Actions.CHOSE_PICTURE || uploadMessageAboveL == null)
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
                        uploadMessageAboveL.onReceiveValue(results);
                        uploadMessageAboveL = null;
                    }
                    else
                    {
                        uploadMessageAboveL.onReceiveValue(null);
                        uploadMessageAboveL = null;
                    }
                }
                break;
        }
    }

    private void clearUploadCallBack(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {//5.0以下
            if (uploadMessage != null){
                uploadMessage.onReceiveValue(null);
                uploadMessage = null;
            }

        }else {
            if(uploadMessageAboveL != null){
                uploadMessageAboveL.onReceiveValue(null);
                uploadMessageAboveL = null;
            }
        }
    }

    private void addImageClickListener(WebView webView) {
        webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.imagelistener.openImage(this.src);  " +//通过js代码找到标签为img的代码块，设置点击的监听方法与本地的openImage方法进行连接
                "    }  " +
                "}" +
                "})()");
    }
}
