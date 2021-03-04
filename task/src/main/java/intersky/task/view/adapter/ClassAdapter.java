package intersky.task.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.task.R;
import intersky.task.entity.CourseClass;

@SuppressLint("InflateParams")
public class ClassAdapter extends BaseAdapter
{

	private ArrayList<CourseClass> mCourseClasses;
	private Context mContext;
	private LayoutInflater mInflater;
	public ClassAdapter(Context context, ArrayList<CourseClass> mCourseClasses)
	{
		this.mContext = context;
		this.mCourseClasses = mCourseClasses;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
	} 
	
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return mCourseClasses.size();
	}

	@Override
	public CourseClass getItem(int position)
	{
		// TODO Auto-generated method stub
		return mCourseClasses.get(position);
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
		CourseClass mCourseClass = mCourseClasses.get(position);
		ViewHolder holder;
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_course_class, null);
			holder.value = (TextView) convertView.findViewById(R.id.name_title);
			holder.line = (RelativeLayout) convertView.findViewById(R.id.line);
			holder.bg = (RelativeLayout) convertView.findViewById(R.id.list_item);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.value.setText(mCourseClass.mCatName);
		if(position == mCourseClasses.size()-1)
		{
			holder.line.setVisibility(View.INVISIBLE);
		}
		else
		{
			holder.line.setVisibility(View.VISIBLE);
		}
		if(mCourseClass.isSelect)
		{
			holder.bg.setBackgroundColor(Color.rgb(255,255,255));
            holder.value.setTextColor(Color.rgb(247,114,51));
		}
		else
		{
			holder.bg.setBackgroundColor(Color.rgb(242,242,242));
            holder.value.setTextColor(Color.rgb(98,98,98));
		}
		return convertView;
	}

	private static class ViewHolder {
		private RelativeLayout line;
		private TextView value;
		private RelativeLayout bg;
	} 
}
