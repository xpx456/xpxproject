package intersky.task.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.task.R;
import intersky.task.entity.Stage;

@SuppressLint("InflateParams")
public class StageAdapter extends BaseAdapter
{

	private ArrayList<Stage> mStages;
	private Context mContext;
	private LayoutInflater mInflater;
	public StageAdapter(Context context, ArrayList<Stage> mStages)
	{
		this.mContext = context;
		this.mStages = mStages;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
	} 
	
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return mStages.size();
	}

	@Override
	public Stage getItem(int position)
	{
		// TODO Auto-generated method stub
		return mStages.get(position);
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
		Stage mStage = mStages.get(position);
		ViewHolder holder;
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.stage_item, null);
			holder.mTitle = (TextView) convertView.findViewById(R.id.item_title);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mTitle.setText(mStage.name);
		return convertView;
	}

	private static class ViewHolder {
		private TextView mTitle;
	} 
}
