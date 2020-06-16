package intersky.select.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.select.R;
import intersky.select.entity.Select;

public class SelectAdapter extends BaseAdapter
{

	private ArrayList<Select> mSelectMores;
	private Context mContext;
	private LayoutInflater mInflater;
	public int color1 = Color.rgb(36,36,36);
	public int color2 = Color.rgb(145,221,242);
	public SelectAdapter(Context context, ArrayList<Select> mSelectMores)
	{
		this.mContext = context;
		this.mSelectMores = mSelectMores;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public SelectAdapter(Context context, ArrayList<Select> mSelectMores,int color1,int color2)
	{
		this.mContext = context;
		this.mSelectMores = mSelectMores;
		this.color1 = color1;
		this.color2 = color2;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return mSelectMores.size();
	}

	@Override
	public Select getItem(int position)
	{
		// TODO Auto-generated method stub
		return mSelectMores.get(position);
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
		Select mSelectMoreModel = mSelectMores.get(position);
		ViewHoder mview = null;
		if(mSelectMoreModel.mType == 0)
		{
			convertView = mInflater.inflate(R.layout.xpx_select_item, null);
		}
		else
		{
			convertView = mInflater.inflate(R.layout.xpx_select_item_title, null);
		}
		mview = new ViewHoder();
		mview.mMail =  convertView.findViewById(R.id.item_select_name);
		if(mSelectMoreModel.mType == 0)
		{
			mview.micon = convertView.findViewById(R.id.item_select_img);
			if(mSelectMoreModel.iselect == true)
			{
				mview.mMail.setTextColor(color2);
				mview.micon.setVisibility(View.VISIBLE);
			}
			else
			{
				mview.mMail.setTextColor(color1);
				mview.micon.setVisibility(View.INVISIBLE);
			}
		}
		else
		{
			mview.mMail.setTextColor(color1);
		}
		if(mSelectMoreModel.mType == 0)
		mview.mMail.setText(mSelectMoreModel.mName);
		else
			mview.mMail.setText(mSelectMoreModel.mName.toUpperCase());
		return convertView;
	}

	public class ViewHoder {
		public TextView mMail;
		public ImageView micon;
	} 
}
