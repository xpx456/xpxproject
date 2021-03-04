package intersky.mail.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.mail.R;
import intersky.select.entity.Select;

public class MailLableAdapter extends BaseAdapter
{

	private ArrayList<Select> mBusinesItems;
	private Context mContext;
	private LayoutInflater mInflater;
	public MailLableAdapter(Context context, ArrayList<Select> mBusinesItems)
	{
		this.mContext = context;
		this.mBusinesItems = mBusinesItems;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
	} 
	
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return mBusinesItems.size();
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return mBusinesItems.get(position);
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
		Select mMailPersonItem = mBusinesItems.get(position);
		ViewHolder holder;
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.mail_lable_item, null);
			holder.mMail = (TextView) convertView.findViewById(R.id.item_mial_address);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		if(position == mBusinesItems.size()-1)
		{
			RelativeLayout layer = (RelativeLayout) convertView.findViewById(R.id.layer);
			layer.setVisibility(View.INVISIBLE);
		}
		else
		{
			RelativeLayout layer = (RelativeLayout) convertView.findViewById(R.id.layer);
			layer.setVisibility(View.VISIBLE);
		}
		holder.mMail.setText(mMailPersonItem.mName);
		return convertView;
	}

	private static class ViewHolder {
		private TextView mMail;
	} 
}
