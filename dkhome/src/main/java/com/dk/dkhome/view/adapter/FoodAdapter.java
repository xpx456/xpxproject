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
import com.dk.dkhome.entity.Food;
import com.dk.dkhome.utils.FoodManager;

import java.util.ArrayList;

import intersky.apputils.GlideApp;


public class FoodAdapter extends RecyclerView.Adapter
{
	public ArrayList<Food> mFoods = new ArrayList<Food>();
	private Context mContext;
	private LayoutInflater mInflater;
	public FoodAdapter(Context context)
	{
		this.mContext = context;
		this.mFoods = mFoods;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public Food getItem(int position)
	{
		// TODO Auto-generated method stub
		return mFoods.get(position);
	}

	@NonNull
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View convertView = mInflater.inflate(R.layout.item_food, null);
		return new ViewHolder(convertView);
	}

	public interface OnItemClickListener{
		void onItemClick(Food food, int position, View view);
	}

	private OnItemClickListener mListener;

	public void setOnItemClickListener(OnItemClickListener mListener) {
		this.mListener = mListener;
	}


	public FoodFunction FoodionFunction;
	public void setFoodFunction(FoodFunction FoodionFunction) {
		this.FoodionFunction = FoodionFunction;
	}

	public interface FoodFunction {
		void delete(Food mFood);
	}

	@Override
	public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder iholder, final int position) {
		final Food food = mFoods.get(position);
		ViewHolder holder = (ViewHolder) iholder;
		holder.car.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mListener != null)
					mListener.onItemClick(food,position,iholder.itemView);
			}
		});
		holder.imageView.setImageResource(FoodManager.foodManager.getFoodImgSource(food.typeid));
		holder.title.setText(food.name);
		holder.carl.setText(String.format("%d Kcal / %d g",food.carl,food.weight ));
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getItemCount() {
		return mFoods.size();
	}

	class ViewHolder extends RecyclerView.ViewHolder {
		TextView title;
		TextView carl;
		ImageView imageView;
		ImageView car;
		public ViewHolder(@NonNull View convertView) {
			super(convertView);
			title = convertView.findViewById(R.id.food_name);
			carl = convertView.findViewById(R.id.food_carl);
			imageView = convertView.findViewById(R.id.food);
			car = convertView.findViewById(R.id.addcar);
		}
	}



}
