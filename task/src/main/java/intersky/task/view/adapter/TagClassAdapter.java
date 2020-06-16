package intersky.task.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.task.R;
import intersky.task.entity.CourseClass;

@SuppressLint("InflateParams")
public class TagClassAdapter extends BaseAdapter
{

	private ArrayList<CourseClass> mCourseClasses;
	private Context mContext;
	private LayoutInflater mInflater;
	public TagClassAdapter(Context context, ArrayList<CourseClass> mCourseClasses)
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
			convertView = mInflater.inflate(R.layout.item_tag_course_class, null);
			holder.value = (TextView) convertView.findViewById(R.id.name_title);
			holder.tag = (TextView) convertView.findViewById(R.id.tag_1);
			holder.line = (RelativeLayout) convertView.findViewById(R.id.line);
			holder.bg = (RelativeLayout) convertView.findViewById(R.id.list_item);
			holder.select = (ImageView) convertView.findViewById(R.id.item_mial_select_icon);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		if(mCourseClass.mClassId.equals("0"))
		holder.tag.setVisibility(View.INVISIBLE);
		if(mCourseClass.mClassId.equals("1"))
		{
			holder.tag.setVisibility(View.VISIBLE);
			holder.tag.setBackgroundResource(R.drawable.shape_bg_tag_cycle1);
		}
		else if(mCourseClass.mClassId.equals("2"))
		{
			holder.tag.setVisibility(View.VISIBLE);
			holder.tag.setBackgroundResource(R.drawable.shape_bg_tag_cycle2);
		}
		else if(mCourseClass.mClassId.equals("3"))
		{
			holder.tag.setVisibility(View.VISIBLE);
			holder.tag.setBackgroundResource(R.drawable.shape_bg_tag_cycle3);
		}
		else if(mCourseClass.mClassId.equals("4"))
		{
			holder.tag.setVisibility(View.VISIBLE);
			holder.tag.setBackgroundResource(R.drawable.shape_bg_tag_cycle4);
		}
		else if(mCourseClass.mClassId.equals("5"))
		{
			holder.tag.setVisibility(View.VISIBLE);
			holder.tag.setBackgroundResource(R.drawable.shape_bg_tag_cycle5);
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
			holder.select.setVisibility(View.VISIBLE);
			holder.bg.setBackgroundColor(Color.rgb(255,255,255));
            holder.value.setTextColor(Color.rgb(247,114,51));
		}
		else
		{
			holder.select.setVisibility(View.INVISIBLE);
			holder.bg.setBackgroundColor(Color.rgb(242,242,242));
            holder.value.setTextColor(Color.rgb(98,98,98));
		}
		return convertView;
	}

	private static class ViewHolder {
		private RelativeLayout line;
		private TextView value;
		private TextView tag;
		private ImageView select;
		private RelativeLayout bg;
	} 
}
