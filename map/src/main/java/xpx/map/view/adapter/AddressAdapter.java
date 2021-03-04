package xpx.map.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;

import java.util.ArrayList;

import xpx.map.R;

@SuppressLint("InflateParams")
public class AddressAdapter extends BaseAdapter
{

	private ArrayList<PoiItem> mPoiItems;
	private Context mContext;
	private LayoutInflater mInflater;
	public AddressAdapter(Context context, ArrayList<PoiItem> mPoiItems)
	{
		this.mContext = context;
		this.mPoiItems = mPoiItems;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
	} 
	
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return mPoiItems.size();
	}

	@Override
	public PoiItem getItem(int position)
	{
		// TODO Auto-generated method stub
		return mPoiItems.get(position);
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
		PoiItem mPoiItem = mPoiItems.get(position);
		ViewHolder holder;
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.address_map_item, null);
			holder.mAddress = (TextView) convertView.findViewById(R.id.item_address);
			holder.mTitle = (TextView) convertView.findViewById(R.id.item_title);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mTitle.setText(mPoiItem.getTitle());
		holder.mAddress.setText(mPoiItem.getSnippet());
		return convertView;
	}

	private static class ViewHolder {
		private TextView mAddress;
		private TextView mTitle;
	} 
}
