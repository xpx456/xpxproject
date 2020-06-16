package intersky.scan.presenter;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.google.zxing.Result;

import java.io.IOException;

import intersky.appbase.Presenter;
import intersky.scan.R;
import intersky.scan.ScanUtils;
import intersky.scan.view.activity.BaseMipcaCaptureActivity;
import intersky.scan.view.activity.MipcaCaptureActivity;
import mining.app.zxing.camera.CameraManager;
import mining.app.zxing.decoding.BaseCaptureActivityHandler;
import mining.app.zxing.decoding.CaptureActivityHandler;
import mining.app.zxing.decoding.InactivityTimer;
import mining.app.zxing.view.ViewfinderView;
import xpx.com.toolbar.utils.ToolBarHelper;

public class BaseMipcaCapturePresenter implements Presenter {


	public BaseMipcaCaptureActivity mMipcaCaptureActivity;
	public BaseCaptureActivityHandler handler;

	public BaseMipcaCapturePresenter(BaseMipcaCaptureActivity mMipcaCaptureActivity)
	{
		this.mMipcaCaptureActivity = mMipcaCaptureActivity;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
//		mMipcaCaptureActivity.setContentView(R.layout.activity_capture);
		mMipcaCaptureActivity.viewfinderView = (ViewfinderView) mMipcaCaptureActivity.findViewById(R.id.viewfinder_view);
		CameraManager.init(ScanUtils.getInstance().context);
		mMipcaCaptureActivity.hasSurface = false;
		mMipcaCaptureActivity.inactivityTimer = new InactivityTimer(mMipcaCaptureActivity);
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
		SurfaceView surfaceView = (SurfaceView) mMipcaCaptureActivity.findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (mMipcaCaptureActivity.hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(mMipcaCaptureActivity);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//			surfaceHolder.setFormat(SurfaceHolder.);
		}
		mMipcaCaptureActivity.decodeFormats = null;
		mMipcaCaptureActivity.characterSet = null;

		mMipcaCaptureActivity.playBeep = true;
		AudioManager audioService = (AudioManager) mMipcaCaptureActivity.getSystemService(mMipcaCaptureActivity.AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			mMipcaCaptureActivity.playBeep = false;
		}
		initBeepSound();
		mMipcaCaptureActivity.vibrate = true;
	}

	@Override
	public void Pause() {
		// TODO Auto-generated method stub
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	public void Destroy() {
		// TODO Auto-generated method stub
		handler = null;
		mMipcaCaptureActivity.inactivityTimer.shutdown();
	}

	public void handleDecode(Result result, Bitmap barcode) {
		mMipcaCaptureActivity.inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		String resultString = result.getText();
		if (resultString.equals("")) {
			Toast.makeText(mMipcaCaptureActivity, "Scan failed!", Toast.LENGTH_SHORT).show();
		}else {

			if(mMipcaCaptureActivity.getIntent().hasExtra("class"))
			{

				try {
					String name = mMipcaCaptureActivity.getIntent().getStringExtra("class");
					if(name.length() > 0)
					{
						Intent intent = new Intent(mMipcaCaptureActivity, Class.forName(name));
						Bundle bundle = new Bundle();
						if(mMipcaCaptureActivity.object != null)
						{
							bundle.putParcelable("object", mMipcaCaptureActivity.object);
						}
						bundle.putString("result", resultString);
						intent.putExtras(bundle);
						intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
						mMipcaCaptureActivity.startActivity(intent);
					}
					else
					{
						Intent intent = new Intent(ScanUtils.ACTION_SCAN_FINISH);
						Bundle bundle = new Bundle();
						bundle.putString("result", resultString);
						intent.putExtras(bundle);
						mMipcaCaptureActivity.sendBroadcast(intent);
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

			}
			else
			{
				Intent resultIntent = new Intent();
				Bundle bundle = new Bundle();
				if(mMipcaCaptureActivity.object != null)
				{
					bundle.putParcelable("object", mMipcaCaptureActivity.object);
				}
				bundle.putString("result", resultString);
				resultIntent.putExtras(bundle);
				mMipcaCaptureActivity.setResult(mMipcaCaptureActivity.RESULT_OK, resultIntent);
			}

		}

		if(!mMipcaCaptureActivity.getIntent().hasExtra("class"))
		{

		}
		else
		{
			mMipcaCaptureActivity.finish();
		}

	}
	
	public void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().stopPreview();
			CameraManager.get().closeDriver();
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new BaseCaptureActivityHandler(mMipcaCaptureActivity, mMipcaCaptureActivity.decodeFormats,
					mMipcaCaptureActivity.characterSet);
		}
	}
	
	private void initBeepSound() {
		if (mMipcaCaptureActivity.playBeep && mMipcaCaptureActivity.mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			mMipcaCaptureActivity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mMipcaCaptureActivity.mediaPlayer = new MediaPlayer();
			mMipcaCaptureActivity.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mMipcaCaptureActivity.mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = mMipcaCaptureActivity.getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mMipcaCaptureActivity.mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mMipcaCaptureActivity.mediaPlayer.setVolume(mMipcaCaptureActivity.BEEP_VOLUME, mMipcaCaptureActivity.BEEP_VOLUME);
				mMipcaCaptureActivity.mediaPlayer.prepare();
			} catch (IOException e) {
				mMipcaCaptureActivity.mediaPlayer = null;
			}
		}
	}
	
	private void playBeepSoundAndVibrate() {
		if (mMipcaCaptureActivity.playBeep && mMipcaCaptureActivity.mediaPlayer != null) {
			mMipcaCaptureActivity.mediaPlayer.start();
		}
		if (mMipcaCaptureActivity.vibrate) {
			Vibrator vibrator = (Vibrator) mMipcaCaptureActivity.getSystemService(mMipcaCaptureActivity.VIBRATOR_SERVICE);
			vibrator.vibrate(MipcaCaptureActivity.VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};
}
