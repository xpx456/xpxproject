package intersky.filetools.view.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import intersky.appbase.entity.Attachment;
import intersky.apputils.BitmapCache;
import intersky.apputils.BitmapSize;
import intersky.apputils.GlideConfiguration;
import intersky.filetools.FileUtils;
import intersky.filetools.R;
import intersky.filetools.entity.ImageItem;

public class PhotoSelectPageAdapter extends PagerAdapter
{
	private ArrayList<View> mViews;
	private Context mContext;
	private int mode = 0;

	public PhotoSelectPageAdapter(ArrayList<View> mViews, int mode, Context mContext)
	{
		super();
		this.mViews = mViews;
		this.mode = mode;
		this.mContext = mContext;
	}

	@Override
	public int getCount()
	{

		return mViews.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1)
	{
		return arg0 == arg1;
	}

	@Override
	public int getItemPosition(Object object)
	{
		// TODO Auto-generated method stub
		// return super.getItemPosition(object);
		return POSITION_NONE;
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2)
	{
		// TODO Auto-generated method stub
		((ViewPager) arg0).removeView(mViews.get(arg1));
	}


	@Override
	public Object instantiateItem(View arg0, int arg1)
	{
		// TODO Auto-generated method stub
		((ViewPager) arg0).addView(mViews.get(arg1));
//		ImageView image = (ImageView) mViews.get(arg1).findViewById(R.id.imgage);
		ImageView image = mViews.get(arg1).findViewById(R.id.imgage);
		if(mode == 0)
		{
			ImageItem mAttachmentModel = (ImageItem) mViews.get(arg1).getTag();
			File mfile = new File(mAttachmentModel.imagePath);
			BitmapSize bitmapSize = BitmapCache.measureBitmap((int) (FileUtils.mFileUtils.mScreenDefine.ScreenWidth)
					,(int) (FileUtils.mFileUtils.mScreenDefine.ScreenHeight),mfile);
			RequestOptions options = new RequestOptions()
					.placeholder(R.drawable.plugin_camera_no_pictures).override(bitmapSize.width,bitmapSize.height);
			Glide.with(mContext).load(mfile).apply(options).into(image);
		}
		else if(mode == 1)
		{
			Attachment mAttachmentModel = (Attachment) mViews.get(arg1).getTag();
			File mfile = new File(mAttachmentModel.mPath);
			BitmapSize bitmapSize = BitmapCache.measureBitmap((int) (FileUtils.mFileUtils.mScreenDefine.ScreenWidth)
					,(int) (FileUtils.mFileUtils.mScreenDefine.ScreenHeight),mfile);
			RequestOptions options = new RequestOptions()
					.placeholder(R.drawable.plugin_camera_no_pictures).override(bitmapSize.width,bitmapSize.height);
			Glide.with(mContext).load(mfile).apply(options).into(image);
		}
		else
		{
			Attachment mAttachmentModel = (Attachment) mViews.get(arg1).getTag();

			if(mAttachmentModel.mUrltemp.length() == 0)
            {
                RequestOptions options = new RequestOptions()
                        .placeholder(R.drawable.plugin_camera_no_pictures);
                Glide.with(mContext).load(mAttachmentModel.mUrl).apply(options).into(image);
            }
            else
            {
                File mFile = GlideConfiguration.getCachedFile( mAttachmentModel.mUrltemp,mContext);
				BitmapSize bitmapSize = BitmapCache.measureBitmap((int) (FileUtils.mFileUtils.mScreenDefine.ScreenWidth)
						,(int) (FileUtils.mFileUtils.mScreenDefine.ScreenHeight),mFile);
                RequestOptions options = new RequestOptions().
                        placeholder(new  BitmapDrawable(mContext.getResources(),mFile.getPath())).override(bitmapSize.width,bitmapSize.height);;
                Glide.with(mContext).load(mAttachmentModel.mUrl).apply(options).into(image);
            }

		}

		return mViews.get(arg1);

	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public Parcelable saveState()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startUpdate(View arg0)
	{
		// TODO Auto-generated method stub
		// ((ViewPager) arg0).removeAllViews();
	}

	@Override
	public void finishUpdate(View arg0)
	{
		// TODO Auto-generated method stub

	}

}