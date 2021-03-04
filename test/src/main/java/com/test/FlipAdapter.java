package com.test;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class FlipAdapter extends BaseAdapter implements OnClickListener {
	
	public interface Callback{
		public void onPageRequested(int page);
	}
	
	static class Item {



		static long id = 0;
		public int sorce = 0;
		long mId;
		public Item(int sourid) {
			this.sorce = sourid;
		}
		public Item() {
			mId = id++;
		}
		
		long getId(){
			return mId;
		}
	}
	
	private LayoutInflater inflater;
	private Callback callback;
	private List<Item> items = new ArrayList<Item>();
	
	public FlipAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		for(int i = 0 ; i<10 ; i++){
			if(i/3 == 0)
			{
				items.add(new Item(R.drawable.a));
			}
			else if(i/3 == 1)
			{
				items.add(new Item(R.drawable.b));
			}
			else if(i/3 == 2)
			{
				items.add(new Item(R.drawable.c));
			}
		}
	}

	public void setCallback(Callback callback) {
		this.callback = callback;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return items.get(position).getId();
	}
	
	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		Item item = items.get(position);
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.page, parent, false);
			
			holder.text = (TextView) convertView.findViewById(R.id.text);
			holder.img = convertView.findViewById(R.id.img);
//			holder.firstPage.setOnClickListener(this);
//			holder.lastPage.setOnClickListener(this);

			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		//TODO set a text with the id as well
		holder.text.setText(items.get(position).getId()+":"+position);
		holder.img.setImageResource(item.sorce);
		return convertView;
	}

	static class ViewHolder{
		TextView text;
		Button firstPage;
		Button lastPage;
		ImageView img;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.first_page:
			if(callback != null){
				callback.onPageRequested(0);
			}
			break;
		case R.id.last_page:
			if(callback != null){
				callback.onPageRequested(getCount()-1);
			}
			break;
		}
	}

	public void addItems(int amount) {
		for(int i = 0 ; i<amount ; i++){


		}
		notifyDataSetChanged();
	}

	public void addItemsBefore(int amount) {
		for(int i = 0 ; i<amount ; i++){
			items.add(0, new Item());
		}
		notifyDataSetChanged();
	}

}
