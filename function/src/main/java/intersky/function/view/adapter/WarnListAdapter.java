package intersky.function.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import intersky.apputils.TimeUtils;
import intersky.function.R;
import intersky.function.receiver.entity.BussinessWarnItem;

public class WarnListAdapter extends BaseAdapter
{
	
	private static final String TAG = "WarnListAdapter";
	
	private LayoutInflater mInflater;
	private Context mContext;
	
	private List<BussinessWarnItem> mData;
	
	public WarnListAdapter(Context context, List<BussinessWarnItem> data)
	{
		this.mContext = context;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mData = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		BussinessWarnItem item = (BussinessWarnItem) this.getItem(position);
		ViewHolder mViewHolder;
		if( null == convertView )
		{
			convertView =  mInflater.inflate( R.layout.workflow_item, null );
			mViewHolder = new ViewHolder();
			mViewHolder.caption = (TextView) convertView.findViewById(R.id.caption);
			mViewHolder.title = (TextView) convertView.findViewById( R.id.title );
			mViewHolder.detail = (TextView) convertView.findViewById( R.id.detail );
			mViewHolder.time = (TextView) convertView.findViewById( R.id.date );
			convertView.setTag(mViewHolder);
		}
		mViewHolder = (ViewHolder) convertView.getTag();
		mViewHolder.title.setText( item.subject );
		mViewHolder.detail.setText( item.memo );
		mViewHolder.time.setText(TimeUtils.measureDeteForm(item.startTime));
		mViewHolder.caption.setText(item.caption);

		return convertView;
	}

	@Override
	public int getCount()
	{
		return mData.size();
	}

	@Override
	public BussinessWarnItem getItem( int position )
	{
		return mData.get( position );
	}

	@Override
	public long getItemId( int position )
	{
		return position;
	}

	private static class ViewHolder {
		private TextView time;
		private TextView detail;
		private TextView title;
		private TextView caption;
	}
}
