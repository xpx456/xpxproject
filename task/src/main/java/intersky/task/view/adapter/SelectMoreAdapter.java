package intersky.task.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.select.entity.Select;
import intersky.task.R;

@SuppressLint("InflateParams")
public class SelectMoreAdapter extends BaseAdapter
{

	private ArrayList<Select> mSelectMores;
	private Context mContext;
	private LayoutInflater mInflater;
	private boolean isone = false;
	public SelectMoreAdapter(Context context, ArrayList<Select> mSelectMores, boolean isone)
	{
		this.mContext = context;
		this.mSelectMores = mSelectMores;
		this.isone = isone;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
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
		ViewHolder holder;
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.select_more_item, null);
			holder.mMail = (TextView) convertView.findViewById(R.id.item_mial_address);
			holder.micon = (ImageView) convertView.findViewById(R.id.item_mial_select_icon);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mMail.setText(mSelectMoreModel.mName);
		if(isone == false)
		{
			if(mSelectMoreModel.iselect == true)
			{
				holder.mMail.setTextColor(Color.rgb(145,221,242));
				holder.micon.setVisibility(View.VISIBLE);
			}
			else
			{
				holder.mMail.setTextColor(Color.rgb(36,36,36));
				holder.micon.setVisibility(View.INVISIBLE);
			}
		}
		else
		{
			holder.mMail.setTextColor(Color.rgb(0,0,0));
			holder.micon.setVisibility(View.INVISIBLE);
		}

		return convertView;
	}

	private static class ViewHolder {
		private TextView mMail;
		private ImageView micon;
	} 
}
