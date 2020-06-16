package intersky.scan.view.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;

import com.google.zxing.BarcodeFormat;

import java.util.Vector;

import intersky.appbase.PadBaseActivity;
import intersky.scan.presenter.MipcaCapturePadPresenter;
import mining.app.zxing.decoding.InactivityTimer;
import mining.app.zxing.view.ViewfinderView;


/**
 * Initial the camera
 * @author Ryan.Tang
 */
public class MipcaCapturePadActivity extends PadBaseActivity implements Callback {

	public static final long VIBRATE_DURATION = 200L;
	public ViewfinderView viewfinderView;
	public boolean hasSurface;
	public Vector<BarcodeFormat> decodeFormats;
	public String characterSet;
	public InactivityTimer inactivityTimer;
	public MediaPlayer mediaPlayer;
	public boolean playBeep;
	public static final float BEEP_VOLUME = 0.10f;
	public boolean vibrate;
	public MipcaCapturePadPresenter mMipcaCapturePadPresenter = new MipcaCapturePadPresenter(this);
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMipcaCapturePadPresenter.Create();
	}

	@Override
	protected void onResume() {
		mMipcaCapturePadPresenter.Resume();
		super.onResume();

	}

	@Override
	protected void onPause() {
		mMipcaCapturePadPresenter.Pause();
		super.onPause();
	}
	


	@Override
	protected void onDestroy() {
		mMipcaCapturePadPresenter.Destroy();
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
			mMipcaCapturePadPresenter.initCamera(holder);
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
		return mMipcaCapturePadPresenter.handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}


	

}