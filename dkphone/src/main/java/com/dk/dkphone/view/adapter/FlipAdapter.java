package com.dk.dkphone.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dk.dkphone.R;
import com.dk.dkphone.entity.Page;

import java.io.File;
import java.util.ArrayList;

public class FlipAdapter extends BaseAdapter{

	private LayoutInflater inflater;
	public Context context;
	public ArrayList<Page> items = new ArrayList<Page>();
	
	public FlipAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		this.context = context;
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
		return 0;
	}

	
	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		Page item = items.get(position);
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.gally_image, parent, false);

			holder.img = (ImageView) convertView.findViewById(R.id.photo);

			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}

		//TODO set a text with the id as well
		if(item.filepath.length() > 0)
		{
			RequestOptions options = new RequestOptions()
					.placeholder(R.drawable.padbg);
			Glide.with(context).load(new File(item.filepath)).apply(options).into(holder.img);
		}
		else if(item.sourceid != -1)
		{
			holder.img.setImageResource(item.sourceid);
		}
		else
		{

		}

		return convertView;
	}

	static class ViewHolder{
		ImageView img;
	}


}
