package intersky.filetools.view.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import intersky.appbase.bus.Bus;
import intersky.filetools.FileUtils;
import intersky.filetools.R;
import intersky.filetools.entity.Video;

@SuppressLint("InflateParams")
public class VideoAdapter extends BaseAdapter
{
	private ArrayList<Video> mVideoItems;
	private Context mContext;
	private LayoutInflater mInflater;

	public VideoAdapter(Context context, ArrayList<Video> mVideoItems)
	{
		this.mContext = context;
		this.mVideoItems = mVideoItems;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return mVideoItems.size();
	}

	@Override
	public Video getItem(int position)
	{
		// TODO Auto-generated method stub
		return mVideoItems.get(position);
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
		Video mVideoItem = getItem(position);
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_video, null);
			holder = new ViewHolder();
			holder.mName = (TextView) convertView.findViewById(R.id.video_name);
			holder.mSize = (TextView) convertView.findViewById(R.id.video_size);
			holder.mItemImage = (ImageView) convertView.findViewById(R.id.item_icon);
			holder.mItemIcon = (ImageView) convertView.findViewById(R.id.item_selecticon);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		{
			holder.mName.setText(mVideoItem.title);
			File mfile = new File(mVideoItem.path);
			if(mfile.exists())
			holder.mSize.setText(getSizeText(mfile.length()));
			else
				holder.mSize.setText("0B");
			holder.mItemImage.setImageResource(R.drawable.icon_list_unknown);
			FileUtils.mFileUtils.setfileimg(holder.mItemImage,mVideoItem.path);
		}
		if(mVideoItem.isselect)
		{
			holder.mItemIcon.setImageResource(R.drawable.bselect);
		}
		else
		{
			holder.mItemIcon.setImageResource(R.drawable.bunselect);
		}
		return convertView;
	}

	private static class ViewHolder
	{
		private TextView mName;
		private TextView mSize;
		private ImageView mItemImage;
		private ImageView mItemIcon;
	}
	
	private String getSizeText(long size)
	{
		String s1 = "";
		if (size / 1024 / 1024 > 0)
		{
			if (size/ 1024 % 1024 * 100 / 1024 > 9)
			{
				s1 = String
						.valueOf(size/ 1024 / 1024)
						+ "."
						+ String.valueOf(size
								/ 1024 % 1024 * 100 / 1024) + "MB";
			}
			else
			{
				s1 = String
						.valueOf(size / 1024 / 1024)
						+ "."
						+ "0"
						+ String.valueOf(size
								/ 1024 % 1024 * 100 / 1024) + "MB";
			}
		}
		else if (size / 1024 > 0)
		{
			if (size % 1024 * 100 / 1024 > 9)
			{
				s1 = String.valueOf(size / 1024)
						+ "."
						+ String.valueOf(size % 1024 * 100 / 1024)
						+ "KB";
			}
			else
			{
				s1 = String.valueOf(size / 1024)
						+ "."
						+ "0"
						+ String.valueOf(size % 1024 * 100 / 1024)
						+ "KB";
			}
		}
		else if (size > 0)
		{
			s1 = String.valueOf(size) + "B";
		}
		else
		{
			s1 = "0B";
		}
		return s1;
	}
 
}
