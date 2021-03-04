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

import com.bumptech.glide.Glide;
import com.dk.dkhome.R;
import com.dk.dkhome.entity.Course;

import java.util.ArrayList;

import intersky.appbase.MySimpleTarget;
import intersky.apputils.GlideApp;
import intersky.apputils.TimeUtils;
import intersky.mywidget.RoundProgressBar;


public class NetCourseAdapter extends RecyclerView.Adapter
{
	private ArrayList<Course> mCourses;
	private Context mContext;
	private LayoutInflater mInflater;
	public NetCourseAdapter(ArrayList<Course> mCourses, Context context)
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
		View convertView = mInflater.inflate(R.layout.course_item, null);
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
		GlideApp.with(mContext).load(course.img).into(holder.imageView);
		holder.title.setText(course.name);
		holder.during.setText(course.time);
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
		TextView during;
		ImageView imageView;
		public ViewHolder(@NonNull View convertView) {
			super(convertView);
			title = convertView.findViewById(R.id.name_title);
			during = convertView.findViewById(R.id.during_value);
			imageView = convertView.findViewById(R.id.video);
		}
	}



}
