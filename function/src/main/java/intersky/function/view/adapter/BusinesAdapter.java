package intersky.function.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

import intersky.function.R;
import intersky.function.entity.BusinessCardModel;

@SuppressLint("InflateParams")
public class BusinesAdapter extends BaseAdapter
{
	private ArrayList<BusinessCardModel> mBusinesItems;
	private Context mContext;
	private LayoutInflater mInflater;
	
	public BusinesAdapter(Context context, ArrayList<BusinessCardModel> mBusinesItems)
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
	public BusinessCardModel getItem(int position)
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
		BusinessCardModel mBusinesItem = mBusinesItems.get(position);
		if(mBusinesItem.type == 0)
		{
			convertView = mInflater.inflate(R.layout.busines_item, null);
			TextView mName = (TextView) convertView.findViewById(R.id.item_busines_name);
			TextView mCompany = (TextView) convertView.findViewById(R.id.item_busines_mobil);
			ImageView icon = (ImageView) convertView.findViewById(R.id.item_busines_icon);
			mName.setText(mBusinesItem.name);
			if(mBusinesItem.mobile1 != null)
			{
				mCompany.setText(mBusinesItem.mobile1);
			}
			else
			{
				mCompany.setText(mBusinesItem.tel1);
			}
			if(mBusinesItem.isselect == false)
			{
				icon.setImageResource(R.drawable.bunselect);
			}
			else
			{
				icon.setImageResource(R.drawable.bselect);
			}
		}
		else
		{
			convertView = mInflater.inflate(R.layout.busines_letter_item, null);
			TextView mName = (TextView) convertView.findViewById(R.id.item_letter_name);
			mName.setText(mBusinesItem.name);
		}
		return convertView;
	}
	
}
