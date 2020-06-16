package intersky.function.view.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import intersky.function.FunctionUtils;
import intersky.function.presenter.WebMessagePresenter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import intersky.appbase.Actions;
import intersky.appbase.BaseActivity;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Attachment;
import intersky.function.receiver.entity.Function;
import intersky.function.receiver.entity.WebBtn;
import intersky.mywidget.SearchViewLayout;
import intersky.xpxnet.net.NetUtils;
import xpx.com.toolbar.utils.ToolBarHelper;


@SuppressLint("ClickableViewAccessibility")
public class WebMessageActivity extends BaseActivity
{
	public static final int EVENT_GET_INFO = 4034000;
	public static final int SCANNIN_GREQUEST_CODE = 1000;
	public static final int SCANNIN_GREQUEST_CODE_WITH_JSON = 1001;
	public static final int SCANNIN_EVENT = 1002;
	public static final int SCANNIN_EVENT_WITH_JSON = 1003;
	public static final int GET_PIC_PATH = 1004;
	public static final String BACK_ENABLED = "BackEnabled";
	public static final String TRANSMIT_ENABLED = "TransmitEnabled";
	public static final String APPROVAL_ENABLED = "ApprovalEnabled";

	public PopupWindow popupWindow1;
	public ValueCallback<Uri> uploadMessage;
	public ValueCallback<Uri[]> uploadMessageAboveL;
	public ArrayList<WebBtn> mWebBtns = new ArrayList<WebBtn>();
	public WebMessagePresenter mWebMessagePresenter = new WebMessagePresenter(this);
	public SearchViewLayout mSearchViewLayout;
	private UploadHandler mUploadHandler;
	public WebView mWebView;
	public String json;
	public Function fInfo;
	public boolean ongoback = false;
	public RelativeLayout mRelativeLayout;
	public Uri fileUri;
	public String mfilepath;
	public String btnjson = "";
    public String backurl = "";
	public Button back;
	public Button summit;
	public Button accept;
	public Button veto;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mWebMessagePresenter.Create();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mWebMessagePresenter.Destroy();
	}
	@Override
	protected void onStart()
	{
		super.onStart();
		mWebMessagePresenter.Start();
	}

	@Override
	protected void onStop()
	{
		super.onStop();

	}

	@Override
	protected void onPause()
	{
		// TODO Auto-generated method stub
		super.onPause();
		mWebMessagePresenter.Pause();

	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		mWebMessagePresenter.Resume();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(mWebMessagePresenter.onKeyDown(keyCode, event))
		{
			return true;
		}
		else
		{
			return super.onKeyDown(keyCode, event);
		}

	}
	@Override
	public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
		return mWebMessagePresenter.onFling(motionEvent, motionEvent1, v, v1);
	}




	@JavascriptInterface
	public void scanQRCode(){
		Message msg = new Message();
		msg.what = SCANNIN_EVENT;
		mWebMessagePresenter.mWebMessageHandler.sendMessage(msg);

	}

	@JavascriptInterface
	public void scanQRCode(String json){
		Message msg = new Message();
		msg.what = SCANNIN_EVENT_WITH_JSON;
		msg.obj = json;
		mWebMessagePresenter.mWebMessageHandler.sendMessage(msg);
	}

	@JavascriptInterface
	public String getToken()
	{
		return NetUtils.getInstance().token;
	}

	@JavascriptInterface
	public void OpenFileAction(String url,String size)
	{
		mWebMessagePresenter.openFileAction(url,size);
	}

	@JavascriptInterface
	public String getLoginInfo(String data)
	{
		String json = "";
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("token", NetUtils.getInstance().token);
			jsonObject.put("project",FunctionUtils.getInstance().account.project);
			json =  jsonObject.toString();
			//mWebMessagePresenter.getloginfo(data);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		btnjson = data;
		//Log.e("getloginfoJavascriptInterface","getloginfohandle success");
		mWebMessagePresenter.mWebMessageHandler.sendEmptyMessageDelayed(WebMessageActivity.EVENT_GET_INFO,10);
//		mWebMessagePresenter.getloginfo(data);
		return json;
	}

	@JavascriptInterface
	public void setTitleAction(String title) {
		ToolBarHelper.setTitle(mActionBar,title);
	}

	@JavascriptInterface
	public void valueToInput() {
	}

	public View.OnClickListener mBackListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mWebMessagePresenter.goBack();
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == Controller.FILE_SELECTED) {
			// Chose a file from the file picker.
			if (mUploadHandler != null) {
				mUploadHandler.onResult(resultCode, data);
			}
		}
		else if(requestCode == Actions.TAKE_PHOTO)
		{
			mWebMessagePresenter.takePhotoResult(requestCode, resultCode, data);
		}
		else if(requestCode == Actions.CHOSE_PICTURE)
		{
			mWebMessagePresenter.takePhotoResult(requestCode, resultCode, data);
		}
		else
		{
			switch (requestCode) {
				case SCANNIN_GREQUEST_CODE:
					if (resultCode == RESULT_OK) {
						Bundle bundle = data.getExtras();
						mWebMessagePresenter.showCode(bundle.getString("result"));

					}
					break;
				case SCANNIN_GREQUEST_CODE_WITH_JSON:
					if (resultCode == RESULT_OK) {
						Bundle bundle = data.getExtras();
						mWebMessagePresenter.showCode2(bundle.getString("result"));

					}
					else
					{
						mWebMessagePresenter.showCode2("");
					}
					break;

			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener()
	{

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
		{
			// TODO Auto-generated method stub
			if (actionId == EditorInfo.IME_ACTION_SEARCH)
			{
				mWebMessagePresenter.onSearch();

			}
			return true;
		}
	};

	public TextWatcher mTextchange = new TextWatcher()
	{

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			if(mSearchViewLayout.getText().length()> 0)
			{
				mSearchViewLayout.showEdit();
			}
			else
			{
				mSearchViewLayout.hidEdit();
				//mAboutPresenter.onSearch();
			}


		}
	};

	public View.OnClickListener mAddPicListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mWebMessagePresenter.addPic();
		}
	};

	public View.OnClickListener mCancleListenter = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mWebMessagePresenter.dodismiss1();
		}

	};

	public View.OnClickListener mCancleListenter2 = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mWebMessagePresenter.dodismiss2();
		}

	};

	public View.OnClickListener mTakePhotoListenter = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mWebMessagePresenter.takePhoto();
		}

	};


	public void initclint()
	{
		mWebView.setWebChromeClient(new WebMessageActivity.MyChromeViewClient());
		mWebView.setWebViewClient(new WebMessageActivity.MyWebViewClinet());
		mWebView.setDownloadListener(new MyWebViewDownLoadListener());
//		mWebView.setWebViewClient(new WebViewClient());
	}

	class  MyDownloadListener implements DownloadListener {


		@Override
		public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
									long contentLength) {
			// TODO Auto-generated method stub

		}

	}

	public class MyChromeViewClient extends WebChromeClient {


		@Override
		public void onCloseWindow(WebView window) {
			WebMessageActivity.this.finish();
			super.onCloseWindow(window);
		}

		public void onProgressChanged(WebView view, final int progress) {

		}

		@Override
		public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {

			new AlertDialog.Builder(WebMessageActivity.this).setTitle("提示信息").setMessage(message)
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

			new AlertDialog.Builder(WebMessageActivity.this).setTitle("提示信息").setMessage(message)
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
			mWebMessagePresenter.showAdd();
//			return super.onShowFileChooser(webView, filePathCallback,
//					fileChooserParams);
			return true;
		}
	}


	public class MyWebViewClinet extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

	}

	class UploadHandler {
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
					mController.getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, result));
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
			return WebMessageActivity.this;
		}
	}

	public View.OnClickListener clictBtn = new View.OnClickListener()
	{

		@Override
		public void onClick(View v) {
			mWebMessagePresenter.clickBtn((String) v.getTag());
		}
	};

	public View.OnClickListener mMoreListenter = new View.OnClickListener()
	{

		@Override
		public void onClick(View v) {
			mWebMessagePresenter.showMore();
		}
	};

    public View.OnClickListener mCloseListenter = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            finish();
        }
    };

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
					Bus.callData(mWebMessagePresenter.mWebMessageActivity,"filetools/getfilePath","/webmessage/photo")+"/"+name,url,0,Long.valueOf(contentLength),"");
			mWebMessagePresenter.startdownload(mAttachmentModel);
			//attachment;filename=采购合同批量.xls
//			DownloaderTask task=new DownloaderTask();
//
//			task.execute(url);

		}

	}
}
