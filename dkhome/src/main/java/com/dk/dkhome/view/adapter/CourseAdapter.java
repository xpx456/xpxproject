package com.dk.dkhome.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dk.dkhome.R;
import com.dk.dkhome.entity.Course;

import java.util.ArrayList;

import intersky.apputils.TimeUtils;
import intersky.mywidget.RoundProgressBar;


public class CourseAdapter extends RecyclerView.Adapter
{
	private ArrayList<Course> mCourses;
	private Context mContext;
	private LayoutInflater mInflater;
	public CourseAdapter(ArrayList<Course> mCourses,Context context)
	{
		this.mContext = context;
		this.mCourses = mCourses;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public Course getItem(int position)
	{
		// TODO Auto-generated method stub
		return mCourses.get(position);
	}

	@NonNull
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View convertView = mInflater.inflate(R.layout.plan_item, null);
		return new ViewHolder(convertView);
	}

	public interface OnItemClickListener{
		void onItemClick(Course contacts, int position, View view);
	}

	private OnItemClickListener mListener;

	public void setOnItemClickListener(OnItemClickListener mListener) {
		this.mListener = mListener;
	}


	public CourseFunction CourseionFunction;
	public void setCourseFunction(CourseFunction CourseionFunction) {
		this.CourseionFunction = CourseionFunction;
	}

	public interface CourseFunction {
		void delete(Course mCourse);
	}

	@Override
	public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder iholder, final int position) {
		final Course course = mCourses.get(position);
		ViewHolder holder = (ViewHolder) iholder;
		RelativeLayout relativeLayout = holder.itemView.findViewById(R.id.main);
		relativeLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mListener != null)
					mListener.onItemClick(course,position,iholder.itemView);
			}
		});
		holder.delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(CourseionFunction != null)
					CourseionFunction.delete(course);
			}
		});
		holder.roundProgressBar.setMax(course.during);
		holder.roundProgressBar.setProgress(course.current);
		holder.persent.setText(String.format("%d",course.current*100/course.during)+"%");
		if(course.videoname.length() > 0)
		{
			holder.videoname.setVisibility(View.VISIBLE);
			holder.imageView.setVisibility(View.VISIBLE);
			holder.videoname.setText(course.name);
		}
		else
		{
			holder.videoname.setVisibility(View.INVISIBLE);
			holder.imageView.setVisibility(View.INVISIBLE);
		}
		holder.title.setText(course.name);
		holder.time.setText(TimeUtils.measureDeteForm2(mContext, course.creat));
		holder.during.setText(String.valueOf(course.during/60)+" min");
		if(position == mCourses.size()-1)
			holder.mRelativeLayout.setVisibility(View.INVISIBLE);
		else
			holder.mRelativeLayout.setVisibility(View.VISIBLE);
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getItemCount() {
		return mCourses.size();
	}

	class ViewHolder extends RecyclerView.ViewHolder {
		TextView title;
		TextView time;
		TextView during;
		TextView videoname;
		TextView persent;
		ImageView imageView;
		RelativeLayout main;
		RelativeLayout delete;
		RelativeLayout mRelativeLayout;
		RoundProgressBar roundProgressBar;
		public ViewHolder(@NonNull View convertView) {
			super(convertView);
			time = convertView.findViewById(R.id.name_title);
			title = convertView.findViewById(R.id.time_title);
			during = convertView.findViewById(R.id.during_value);
			videoname = convertView.findViewById(R.id.video_name);
			imageView = convertView.findViewById(R.id.video);
			persent = convertView.findViewById(R.id.persent);
			main = convertView.findViewById(R.id.main);
			delete = convertView.findViewById(R.id.btndelete);
			mRelativeLayout = convertView.findViewById(R.id.line);
			roundProgressBar = convertView.findViewById(R.id.roundProgressBar);
		}
	}



}
