package intersky.function.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.apputils.TimeUtils;
import intersky.function.R;
import intersky.function.receiver.entity.WorkFlowItem;

public class WorkFlowAdapter extends BaseAdapter
{

	private static final String TAG = "WarnListAdapter";

	private LayoutInflater mInflater;
	private Context mContext;

	private ArrayList<WorkFlowItem> mData;

	public WorkFlowAdapter(Context context, ArrayList<WorkFlowItem> data)
	{
		this.mContext = context;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mData = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		WorkFlowItem item = (WorkFlowItem) this.getItem(position);
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
		mViewHolder.detail.setText( mContext.getString(R.string.workflow_sendname)+":" + item.userName );
		mViewHolder.time.setText(TimeUtils.measureDeteForm(item.receiveTime));
		mViewHolder.caption.setText(item.module);

		return convertView;
	}

	@Override
	public int getCount()
	{
		return mData.size();
	}

	@Override
	public WorkFlowItem getItem( int position )
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
