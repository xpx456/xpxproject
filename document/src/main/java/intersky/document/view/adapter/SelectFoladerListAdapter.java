package intersky.document.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.document.R;
import intersky.document.entity.DocumentNet;

@SuppressLint("InflateParams")
public class SelectFoladerListAdapter extends BaseAdapter
{
	private ArrayList<DocumentNet> mPathItems;
	private Context mContext;
	private LayoutInflater mInflater;
	
	
	public SelectFoladerListAdapter(Context context, ArrayList<DocumentNet> mPathItems){
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
	public DocumentNet getItem(int position)
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
		DocumentNet mDocumentItem = getItem(position);
		if(convertView == null)
		{
			holder = new ViewHolder();
			if(position == getCount()-1)
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
	}
	
	
}
