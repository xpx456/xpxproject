package intersky.function.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.function.R;
import intersky.function.entity.LableDataItem;

public class LabelListAdapter extends BaseAdapter
{
	
	private ArrayList<LableDataItem> mData;
	
	private LayoutInflater mInflater;
	
	public LabelListAdapter(Context context, ArrayList<LableDataItem> data )
	{
		mInflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		this.mData = data;
	}
	
	@Override
	public int getCount()
	{
		return mData.size();
	}

	@Override
	public LableDataItem getItem( int position )
	{
		return mData.get( position );
	}

	@Override
	public long getItemId( int position )
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent )
	{
		LableDataItem mLableDataItem = mData.get(position);
		ViewHolder mViewHolder;
		if( null == convertView )
		{
			convertView =  mInflater.inflate( R.layout.label_item, null );
			mViewHolder = new ViewHolder();
			mViewHolder.name = (TextView) convertView.findViewById(R.id.name);
			mViewHolder.value = (TextView) convertView.findViewById( R.id.value );
			convertView.setTag(mViewHolder);
		}
		mViewHolder = (ViewHolder) convertView.getTag();
		mViewHolder.name.setText( mLableDataItem.value1 );
		mViewHolder.value.setText( mLableDataItem.value2 );
		
		return convertView;
	}

	private static class ViewHolder {
		private TextView name;
		private TextView value;
	}
}

