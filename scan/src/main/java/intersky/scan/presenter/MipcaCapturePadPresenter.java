package intersky.scan.presenter;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.Result;

import java.io.IOException;

import intersky.appbase.Presenter;
import intersky.scan.R;
import intersky.scan.ScanUtils;
import intersky.scan.view.activity.MipcaCapturePadActivity;
import mining.app.zxing.camera.CameraManager;
import mining.app.zxing.decoding.CaptureActivityPadHandler;
import mining.app.zxing.decoding.InactivityTimer;
import mining.app.zxing.view.ViewfinderView;

public class MipcaCapturePadPresenter implements Presenter {
	

	public MipcaCapturePadActivity mMipcaCapturePadActivity;
	public CaptureActivityPadHandler handler;

	public MipcaCapturePadPresenter(MipcaCapturePadActivity mMipcaCapturePadActivity)
	{
		this.mMipcaCapturePadActivity = mMipcaCapturePadActivity;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		mMipcaCapturePadActivity.setContentView(R.layout.activity_capture_pad);
		CameraManager.init(ScanUtils.getInstance().context);
		mMipcaCapturePadActivity.viewfinderView = (ViewfinderView) mMipcaCapturePadActivity.findViewById(R.id.viewfinder_view);
		mMipcaCapturePadActivity.hasSurface = false;
		mMipcaCapturePadActivity.inactivityTimer = new InactivityTimer(mMipcaCapturePadActivity);
        ImageView back = mMipcaCapturePadActivity.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                mMipcaCapturePadActivity.finish();
            }
        });
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
		SurfaceView surfaceView = (SurfaceView) mMipcaCapturePadActivity.findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (mMipcaCapturePadActivity.hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(mMipcaCapturePadActivity);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//			surfaceHolder.setFormat(SurfaceHolder.);
		}
		mMipcaCapturePadActivity.decodeFormats = null;
		mMipcaCapturePadActivity.characterSet = null;

		mMipcaCapturePadActivity.playBeep = true;
		AudioManager audioService = (AudioManager) mMipcaCapturePadActivity.getSystemService(mMipcaCapturePadActivity.AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			mMipcaCapturePadActivity.playBeep = false;
		}
		initBeepSound();
		mMipcaCapturePadActivity.vibrate = true;
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
		mMipcaCapturePadActivity.inactivityTimer.shutdown();
	}

	public void handleDecode(Result result, Bitmap barcode) {
		mMipcaCapturePadActivity.inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		String resultString = result.getText();
		if (resultString.equals("")) {
			Toast.makeText(mMipcaCapturePadActivity, "Scan failed!", Toast.LENGTH_SHORT).show();
		}else {
			Intent intent = new Intent();
			intent.setAction(mMipcaCapturePadActivity.getIntent().getAction());
			intent.putExtra("result",resultString);
			mMipcaCapturePadActivity.sendBroadcast(intent);
		}
		mMipcaCapturePadActivity.finish();

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
			handler = new CaptureActivityPadHandler(mMipcaCapturePadActivity, mMipcaCapturePadActivity.decodeFormats,
					mMipcaCapturePadActivity.characterSet);
		}
	}
	
	private void initBeepSound() {
		if (mMipcaCapturePadActivity.playBeep && mMipcaCapturePadActivity.mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			mMipcaCapturePadActivity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mMipcaCapturePadActivity.mediaPlayer = new MediaPlayer();
			mMipcaCapturePadActivity.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mMipcaCapturePadActivity.mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = mMipcaCapturePadActivity.getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mMipcaCapturePadActivity.mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mMipcaCapturePadActivity.mediaPlayer.setVolume(mMipcaCapturePadActivity.BEEP_VOLUME, mMipcaCapturePadActivity.BEEP_VOLUME);
				mMipcaCapturePadActivity.mediaPlayer.prepare();
			} catch (IOException e) {
				mMipcaCapturePadActivity.mediaPlayer = null;
			}
		}
	}
	
	private void playBeepSoundAndVibrate() {
		if (mMipcaCapturePadActivity.playBeep && mMipcaCapturePadActivity.mediaPlayer != null) {
			mMipcaCapturePadActivity.mediaPlayer.start();
		}
		if (mMipcaCapturePadActivity.vibrate) {
			Vibrator vibrator = (Vibrator) mMipcaCapturePadActivity.getSystemService(mMipcaCapturePadActivity.VIBRATOR_SERVICE);
			vibrator.vibrate(MipcaCapturePadActivity.VIBRATE_DURATION);
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
