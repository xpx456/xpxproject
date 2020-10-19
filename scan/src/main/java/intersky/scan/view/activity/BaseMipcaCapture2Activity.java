package intersky.scan.view.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;

import com.google.zxing.BarcodeFormat;

import java.util.Vector;

import intersky.appbase.BaseActivity;
import intersky.appbase.PadBaseActivity;
import intersky.scan.presenter.BaseMipcaCapture2Presenter;
import intersky.scan.presenter.BaseMipcaCapturePresenter;
import mining.app.zxing.decoding.InactivityTimer;
import mining.app.zxing.view.ViewfinderView;


/**
 * Initial the camera
 * @author Ryan.Tang
 */
public class BaseMipcaCapture2Activity extends PadBaseActivity implements Callback {

	public static final long VIBRATE_DURATION = 200L;
	public ViewfinderView viewfinderView;
	public boolean hasSurface;
	public Vector<BarcodeFormat> decodeFormats;
	public String characterSet;
	public InactivityTimer inactivityTimer;
	public MediaPlayer mediaPlayer;
	public boolean playBeep;
	public Parcelable object = null;
	public static final float BEEP_VOLUME = 0.10f;
	public boolean vibrate;
	public BaseMipcaCapture2Presenter mMipcaCapturePresenter = new BaseMipcaCapture2Presenter(this);
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		mMipcaCapturePresenter.Create();
	}

	@Override
	protected void onResume() {
		mMipcaCapturePresenter.Resume();
		super.onResume();

	}

	@Override
	protected void onPause() {
		mMipcaCapturePresenter.Pause();
		super.onPause();
	}
	


	@Override
	protected void onDestroy() {
		mMipcaCapturePresenter.Destroy();
		super.onDestroy();
	}

	

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			mMipcaCapturePresenter.initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return mMipcaCapturePresenter.handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}


	

}