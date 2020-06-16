package intersky.filetools.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.filetools.R;
import intersky.filetools.entity.LocalDocument;

@SuppressLint("InflateParams")
public class LocalPathAdapter extends BaseAdapter
{
	private ArrayList<LocalDocument> mPathItems;
	private Context mContext;
	private LayoutInflater mInflater;
	
	
	public LocalPathAdapter(Context context, ArrayList<LocalDocument> mPathItems){
		this.mContext = context;
		this.mPathItems = mPathItems;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
	}
	
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return mPathItems.size();
	}

	@Override
	public LocalDocument getItem(int position)
	{
		// TODO Auto-generated method stub
		return mPathItems.get(position);
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
		ViewHolder holder;
		LocalDocument mDocumentItem = getItem(position);
		if(convertView == null)
		{
			holder = new ViewHolder();
			if(position == mPathItems.size()-1)
			{
				convertView = mInflater.inflate(R.layout.path_item_end, null);
			}
			else
			{
				convertView = mInflater.inflate(R.layout.path_item, null);
			}
			holder.mName = (TextView) convertView.findViewById(R.id.path_title);
			convertView.setTag(holder);
		}
		else
		{
			holder=(ViewHolder)convertView.getTag();
		}
		holder.mName.setText(mDocumentItem.mName);
		return convertView;
	}




	private static class ViewHolder {
		private TextView mName;
		private RelativeLayout mTrangle;
	}
	
	
}
