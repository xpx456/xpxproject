package intersky.apputils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.ref.SoftReference;
import java.nio.ByteBuffer;
import java.util.HashMap;


public class BitmapCache extends Activity {

	public Handler h = new Handler();
	public final String TAG = getClass().getSimpleName();
	private HashMap<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();
	
	
	public void put(String path, Bitmap bmp) {
		if (!TextUtils.isEmpty(path) && bmp != null) {
			imageCache.put(path, new SoftReference<Bitmap>(bmp));
		}
	}


	public void displayBmp(final ImageView iv, final String thumbPath,
                           final String sourcePath, final ImageCallback callback) {
		if (TextUtils.isEmpty(thumbPath) && TextUtils.isEmpty(sourcePath)) {
			Log.e(TAG, "no paths pass in");
			return;
		}

		final String path;
		final boolean isThumbPath;
		if (!TextUtils.isEmpty(thumbPath)) {
			path = thumbPath;
			isThumbPath = true;
		} else if (!TextUtils.isEmpty(sourcePath)) {
			path = sourcePath;
			isThumbPath = false;
		} else {
			// iv.setImageBitmap(null);
			return;
		}

		if (imageCache.containsKey(path)) {
			SoftReference<Bitmap> reference = imageCache.get(path);
			Bitmap bmp = reference.get();
			if (bmp != null) {
				if (callback != null) {
					callback.imageLoad(iv, bmp, sourcePath);
				}
				iv.setImageBitmap(bmp);
				Log.d(TAG, "hit cache");
				return;
			}
		}
		iv.setImageBitmap(null);

		new Thread() {
			Bitmap thumb;

			public void run() {

				try {
					if (isThumbPath) {

//						thumb = BitmapFactory.decodeFile(thumbPath);
						thumb = AppUtils.decodeBitmap(thumbPath);
						if (thumb == null) {
							thumb = revitionImageSize(sourcePath);						
						}						
					} else {
						thumb = revitionImageSize(sourcePath);											
					}
				} catch (Exception e) {
					
				}
				if (thumb == null) {
					//thumb = PhotoActivity.bimap;
				}
				Log.e(TAG, "-------thumb------"+thumb);
				put(path, thumb);

				if (callback != null) {
					h.post(new Runnable() {
						@Override
						public void run() {
							callback.imageLoad(iv, thumb, sourcePath);
						}
					});
				}
			}
		}.start();

	}

	public Bitmap revitionImageSize(String path) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);
		in.close();
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			if ((options.outWidth >> i <= 256)
					&& (options.outHeight >> i <= 256)) {
				in = new BufferedInputStream(
						new FileInputStream(new File(path)));
				options.inSampleSize = (int) Math.pow(2.0D, i);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(in, null, options);
				break;
			}
			i += 1;
		}
		return bitmap;
	}

	public interface ImageCallback {
		public void imageLoad(ImageView imageView, Bitmap bitmap,
                              Object... params);
	}

	public static Bitmap getCircleBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap( bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas( output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect( 0, 0, bitmap.getWidth(), bitmap.getHeight());

		paint.setAntiAlias( true);
		paint.setFilterBitmap( true);
		paint.setDither( true);
		canvas.drawARGB( 0, 0, 0, 0);
		paint.setColor( color);
		//在画布上绘制一个圆
		canvas.drawCircle( bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
		paint.setXfermode( new PorterDuffXfermode( PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap( bitmap, rect, rect, paint);
		return output;
	}

	public static Bitmap bitmapMeasureSize(File file,int h,int w)
	{
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(file.getPath(), options);
		int sacle = calculateInSampleSize(options, w, h);
		options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;//这块要取实际的Bitmap了，用false
		options.inSampleSize = sacle;//设置缩放比例
		return BitmapFactory.decodeFile(file.getPath(), options);//返回缩放后的图片
	}

	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
	{
		// reqWidth、reqHeight是想要显示图片的大小
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		int rw = 1;
		int rh = 1;
		int r = 1;
		if(reqWidth != width)
		{
			 rw = width/reqWidth +1 ;
		}
		if(reqHeight != height)
		{
			rh = height/reqHeight +1 ;
		}
		if(rw > rh)
		{
			r = rh;
		}
		else
		{
			r = rw;
		}
		inSampleSize = r;
		return inSampleSize;
	}


	public static Bitmap cropBitmapCenter(Bitmap bitmap,int width,int height) {
		int w = bitmap.getWidth(); // 得到图片的宽，高
		int h = bitmap.getHeight();
		return Bitmap.createBitmap(bitmap, (w -width)/2, (h-height)/2, width, height, null, false);
	}

	public static boolean saveBitmap(Bitmap bm,String path) {
		File f = new File(path);
		if (f.exists()) {
			f.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public static BitmapSize measureBitmap(int w,int h,File file) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(file.getPath(), options);
		BitmapSize bitmapSize = new BitmapSize();
		if(options.outWidth < options.outHeight )
		{
			if(options.outHeight < h)
			{
				bitmapSize.height = options.outHeight;
			}
			else
			{
				bitmapSize.height = h;
			}
			bitmapSize.width = options.outWidth*bitmapSize.height/options.outHeight;
		}
		else
		{
			if(options.outWidth < w)
			{
				bitmapSize.width = options.outWidth;
			}
			else
			{
				bitmapSize.width = w;
			}
			bitmapSize.height = options.outHeight*bitmapSize.width/options.outWidth;
		}
		return bitmapSize;
	}


	public static void saveBitmap(Bitmap bitmap,String path,String name) {
		try {
			File dirFile = new File(path);
			if (!dirFile.exists()) {              //如果不存在，那就建立这个文件夹
				dirFile.mkdirs();
			}
			File file = new File(path, name + ".jpg");
			FileOutputStream fos = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static byte[] Bitmap2Bytes(Bitmap bm) {



		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	public static Bitmap Bytes2Bimap(byte[] buffer,int width,int height) {


		if (buffer.length != 0) {
			Bitmap stitchBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ALPHA_8);

			stitchBmp.copyPixelsFromBuffer(ByteBuffer.wrap(buffer));
			return stitchBmp;
		} else {
			return null;
		}

	}

	public static Bitmap createBitmapGray(byte[] values, int picW, int picH) {
		if(values == null || picW <= 0 || picH <= 0)
			return null;
		//使用8位来保存图片
		Bitmap bitmap = Bitmap
				.createBitmap(picW, picH, Bitmap.Config.RGB_565);
		int pixels[] = new int[picW * picH];
		for (int i = 0; i < pixels.length; ++i) {
			//关键代码，生产灰度图
			pixels[i] = values[i] * 256 * 256 + values[i] * 256 + values[i] + 0xFF000000;
		}
		bitmap.setPixels(pixels, 0, picW, 0, 0, picW, picH);
		values = null;
		pixels = null;
		return bitmap;
	}


	public static byte[] readFile(File file) {
		RandomAccessFile rf = null;
		byte[] data = null;
		try {
			rf = new RandomAccessFile(file, "r");
			data = new byte[(int) rf.length()];
			rf.readFully(data);
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			closeQuietly(rf);
		}
		return data;
	}

	//关闭读取file
	public static void closeQuietly(Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public static Bitmap createBitmap(byte[] values, int picW, int picH) {
		if(values == null || picW <= 0 || picH <= 0)
			return null;
		//使用8位来保存图片
		Bitmap bitmap = Bitmap
				.createBitmap(picW, picH, Bitmap.Config.ARGB_8888);
		int pixels[] = new int[picW * picH];
		for (int i = 0; i < pixels.length; ++i) {
			//关键代码，生产灰度图
			pixels[i] = values[i] * 256 * 256 + values[i] * 256 + values[i] + 0xFF000000;
		}
		bitmap.setPixels(pixels, 0, picW, 0, 0, picW, picH);
		values = null;
		pixels = null;
		return bitmap;
	}


}
