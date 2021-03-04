package intersky.filetools.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import intersky.apputils.AppUtils;
import intersky.apputils.BitmapCache;
import intersky.apputils.Res;
import intersky.filetools.R;
import intersky.filetools.entity.ImageBucket;
import intersky.filetools.entity.ImageItem;

@SuppressLint("InflateParams")
public class AlbumItemAdapter extends BaseAdapter
{
	private List<ImageBucket> mAlbums;
	private Context mContext;
	private LayoutInflater mInflater;
	BitmapCache cache;
	final String TAG = getClass().getSimpleName();
	
	
	public AlbumItemAdapter(Context context, List<ImageBucket> mAlbums){
		this.mContext = context;
		this.mAlbums = mAlbums;
		cache = new BitmapCache();
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
	}
	
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return mAlbums.size();
	}

	@Override
	public ImageBucket getItem(int position)
	{
		// TODO Auto-generated method stub
		return mAlbums.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		ViewHolder holder;
		ImageBucket mImageBucket = getItem(position);
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.album_item, null);
			holder.mName = (TextView) convertView.findViewById(R.id.item_name);
			holder.mCount = (TextView) convertView.findViewById(R.id.item_count);
			holder.mIcon = (ImageView) convertView.findViewById(R.id.item_icon_back);

			convertView.setTag(holder);
		}
		else
		{
			holder=(ViewHolder)convertView.getTag();
		}
		holder.mName.setText(mImageBucket.bucketName);
		holder.mCount.setText("("+mImageBucket.count+")");
		String path;
		if (mImageBucket.imageList != null)
		{
			path = mImageBucket.imageList.get(0).imagePath;
			
		}
		else
		{
			path = "android_hybrid_camera_default";
		}
		if (path.contains("android_hybrid_camera_default"))
		{
			holder.mIcon.setImageResource(Res.getDrawableID("plugin_camera_no_pictures"));
		}			
		else 
		{
			final ImageItem item = mImageBucket.imageList.get(0);
			holder.mIcon.setTag(item.imagePath);
			cache.displayBmp(holder.mIcon, item.imagePath, item.imagePath,
					callback);

//			File mFile = new File(path);
//			if(mFile.exists())
//			{
//				Bitmap bm = BitmapFactory.decodeFile(mFile.getPath());
//				if(bm != null)
//				{
//					if(bm.getWidth() != bm.getHeight())
//						bm = AppUtils.centerSquareScaleBitmap(bm ,400);
//					holder.mIcon.setImageBitmap(bm);
//				}
//				else
//				{
//					holder.mIcon.setImageResource(Res.getDrawableID("plugin_camera_no_pictures"));
//				}
//			}
//			else
//			{
//				holder.mIcon.setImageResource(Res.getDrawableID("plugin_camera_no_pictures"));
//			}
		}
		return convertView;
	}

	private static class ViewHolder {
		private TextView mName;
		private TextView mCount;
		private ImageView mIcon;
//		private ImageView mIconBack;
	}
	
	BitmapCache.ImageCallback callback = new BitmapCache.ImageCallback() {
		@Override
		public void imageLoad(ImageView imageView, Bitmap bitmap,
                              Object... params) {
			if (imageView != null && bitmap != null) {
				String url = (String) params[0];
				if (url != null && url.equals((String) imageView.getTag())) {
					if(bitmap.getWidth() != bitmap.getHeight())
					{
						Bitmap bm = AppUtils.centerSquareScaleBitmap(bitmap ,400);
						((ImageView) imageView).setImageBitmap(bm);
					}

				} else {
					Log.e(TAG, "callback, bmp not match");
				}
			} else {
				Log.e(TAG, "callback, bmp null");
			}
		}
	};
}
