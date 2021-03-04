package intersky.mail.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import intersky.mail.R;
import intersky.appbase.Actions;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Attachment;

@SuppressLint("InflateParams")
public class AttachmentAdapter extends BaseAdapter
{
	private ArrayList<Attachment> mFuJianItems;
	private Context mContext;
	private LayoutInflater mInflater;
	
	public AttachmentAdapter(Context context, ArrayList<Attachment> mFuJianItems){
		this.mContext = context;
		this.mFuJianItems = mFuJianItems;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return mFuJianItems.size();
	}

	@Override
	public Attachment getItem(int position)
	{
		// TODO Auto-generated method stub
		return mFuJianItems.get(position);
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
		Attachment mFuJianItem = getItem(position);
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_fujian, null);
			holder.micon = (ImageView) convertView.findViewById(R.id.fujian_img_big);
			holder.name = (TextView) convertView.findViewById(R.id.fujian_title);
			holder.up = (TextView) convertView.findViewById(R.id.fujian_title_up);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		if(mFuJianItem.stata == Attachment.STATA_NONE ||mFuJianItem.stata == Attachment.STATA_DOWNLOADFINISH)
		{
			holder.name.setVisibility(View.VISIBLE);
			holder.up.setVisibility(View.INVISIBLE);
		}
		else
		{
			holder.name.setVisibility(View.INVISIBLE);
			holder.up.setVisibility(View.VISIBLE);
			if(mFuJianItem.stata == Attachment.STATA_NOSTART)
			{
				holder.up.setText(mContext.getString(R.string.wait_upload));
			}
			else if(mFuJianItem.stata == Attachment.STATA_DWONLODING)
			{
				holder.up.setText(mContext.getString(R.string.uploading));
			}
			else if(mFuJianItem.stata == Attachment.STATA_DOWNLOADFINISH)
			{
				holder.up.setText(mContext.getString(R.string.upload_fail));
			}
		}

		holder.name.setText(mFuJianItem.mName);

		if(	(int)Bus.callData(mContext,"filetools/getFileType",mFuJianItem.mName) != Actions.FILE_TYPE_PICTURE)
		{
			Bus.callData(mContext,"filetools/setfileimg",holder.micon,mFuJianItem.mName);
		}
		else
		{
			File mfile = new File(mFuJianItem.mPath);
			if(mfile.exists())
			{
//				RequestOptions options = new RequestOptions()
//						.placeholder(R.drawable.plugin_camera_no_pictures).transform(new CropSquareTransformation((BitmapPool) mContext));
				Glide.with(mContext).load(mfile).into(holder.micon);
			}
			else
			{
				Bus.callData(mContext,"filetools/setfileimg",holder.micon,mFuJianItem.mName);
			}
		}
		return convertView;
	}

	private static class ViewHolder {
		private ImageView micon;
		private TextView name;
		private TextView up;
	}
}
