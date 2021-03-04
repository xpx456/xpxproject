package intersky.attendance.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.ArrayList;

import intersky.attendance.R;
import intersky.attendance.entity.AttdanceSet;

@SuppressLint("InflateParams")
public class AttdanceSetAdapter extends BaseAdapter
{

	private ArrayList<AttdanceSet> mAttdanceSets;
	private Context mContext;
	private LayoutInflater mInflater;
	public AttdanceSetAdapter(Context context, ArrayList<AttdanceSet> mAttdanceSets)
	{
		this.mContext = context;
		this.mAttdanceSets = mAttdanceSets;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
	} 
	
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return mAttdanceSets.size();
	}

	@Override
	public AttdanceSet getItem(int position)
	{
		// TODO Auto-generated method stub
		return mAttdanceSets.get(position);
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
		AttdanceSet mAttdanceSetModel = mAttdanceSets.get(position);
		ViewHolder holder;
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.attdance_set_item, null);
			holder.mStart = (TextView) convertView.findViewById(R.id.atteance_start_value);
			holder.mTitle = (TextView) convertView.findViewById(R.id.name_title);
			holder.mEnd = (TextView) convertView.findViewById(R.id.atteance_end_value);
			holder.mDay = (TextView) convertView.findViewById(R.id.atteance_day_value);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mTitle.setText(mAttdanceSetModel.name);
		holder.mEnd.setText(mAttdanceSetModel.end.substring(0,5));
		holder.mStart.setText(mAttdanceSetModel.start.substring(0,5));
		holder.mDay.setText(mAttdanceSetModel.day);

		return convertView;
	}

	private static class ViewHolder {
		private TextView mStart;
		private TextView mEnd;
		private TextView mTitle;
		private TextView mDay;
	} 
}
