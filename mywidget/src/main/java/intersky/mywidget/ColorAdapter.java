package intersky.mywidget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

@SuppressLint("InflateParams")
public class ColorAdapter extends BaseAdapter
{

	private ArrayList<ColorModel> mColors;
	private Context mContext;
	private LayoutInflater mInflater;
	public ColorAdapter(Context context, ArrayList<ColorModel> mColors)
	{
		this.mContext = context;
		this.mColors = mColors;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
	} 
	
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return mColors.size();
	}

	@Override
	public ColorModel getItem(int position)
	{
		// TODO Auto-generated method stub
		return mColors.get(position);
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
		ColorModel color = mColors.get(position);
		ViewHolder holder;
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.color_item, null);
			holder.mColor = (TextView) convertView.findViewById(R.id.color);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mColor.setBackgroundResource(color.colorId);
		return convertView;
	}

	private static class ViewHolder {
		private TextView mColor;
	} 
}
