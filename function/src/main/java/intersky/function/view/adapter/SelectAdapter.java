package intersky.function.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.function.R;

@SuppressLint("InflateParams")
public class SelectAdapter extends BaseAdapter
{

	private ArrayList<String> mStrings;
	private Context mContext;
	private LayoutInflater mInflater;
	public TextView mTextView;
	public SelectAdapter(Context context, ArrayList<String> mStrings, TextView mTextView)
	{
		this.mContext = context;
		this.mStrings = mStrings;
		this.mTextView = mTextView;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
	} 
	
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return mStrings.size();
	}

	@Override
	public String getItem(int position)
	{
		// TODO Auto-generated method stub
		return mStrings.get(position);
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
		String mString = mStrings.get(position);
		ViewHolder holder;
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.xpx_select_item, null);
			holder.mTitle = (TextView) convertView.findViewById(R.id.item_title);
			holder.mButtonlayer = (RelativeLayout) convertView.findViewById(R.id.line);
			holder.mButtonlayer2 = (RelativeLayout) convertView.findViewById(R.id.line2);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		if(position == mStrings.size()-1)
		{

			holder.mButtonlayer.setVisibility(View.INVISIBLE);
			holder.mButtonlayer2.setVisibility(View.VISIBLE);
		}
		else
		{

			holder.mButtonlayer.setVisibility(View.VISIBLE);
			holder.mButtonlayer2.setVisibility(View.INVISIBLE);
		}

		holder.mTitle.setText(mString);
		return convertView;
	}

	private static class ViewHolder {
		private TextView mTitle;
		private RelativeLayout mButtonlayer;
		private RelativeLayout mButtonlayer2;
	} 
}
