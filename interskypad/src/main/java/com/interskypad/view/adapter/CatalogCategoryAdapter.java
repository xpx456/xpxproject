package com.interskypad.view.adapter;


import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.interskypad.R;
import com.interskypad.entity.Category;

import java.util.ArrayList;

import intersky.appbase.AppActivityManager;

/**
 * TreeViewAdapter
 * @author carrey
 *
 */
public class CatalogCategoryAdapter extends BaseAdapter {

	public static final int UPDATA_LIST = 1000;
	public ArrayList<Category> mItems;
	public LayoutInflater inflater;
	public int indentionBase;
	public Context mContext;
	public Handler handler;
	
	public CatalogCategoryAdapter(Context context, ArrayList<Category> mItems, Handler handler) {
		this.mItems = mItems;
		mContext = context;
		this.inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		indentionBase = (int) (AppActivityManager.getAppActivityManager(mContext).mScreenDefine.density*10);
		this.handler = handler;
	}
	
	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		Category element = mItems.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.catalog_category_item, null);
			holder.contentText = (TextView) convertView.findViewById(R.id.contenttext);
			holder.expend = (TextView) convertView.findViewById(R.id.expend);
			holder.mLayer = (LinearLayout) convertView.findViewById(R.id.content);
			holder.expend.setOnClickListener(expendClicklistener);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(element.childs.size() == 0)
		{
			holder.expend.setVisibility(View.INVISIBLE);
		}
		else
		{
			holder.expend.setVisibility(View.VISIBLE);
			if(element.isExpanded)
			{
				holder.expend.setText("-");
			}
			else
			{
				holder.expend.setText("+");
			}
		}
		holder.expend.setTag(element);
		holder.mLayer.setPadding(
				indentionBase * (element.level),
				holder.contentText.getPaddingTop(), 
				holder.contentText.getPaddingRight(), 
				holder.contentText.getPaddingBottom());
		holder.contentText.setText(element.contentText);
		if(element.isSelected == false)
		{
			holder.mLayer.setBackgroundColor(Color.parseColor("#00000000"));
		}
		else
		{
			holder.mLayer.setBackgroundColor(Color.parseColor("#ff6191c1"));
		}
		return convertView;
	}
	
	/**
	 * 优化Holder
	 * @author carrey
	 *
	 */
	static class ViewHolder{
		TextView contentText;
		TextView expend;
		LinearLayout mLayer;
	}

	public View.OnClickListener expendClicklistener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Category item = (Category) v.getTag();
			if(item.isExpanded) {
				item.isExpanded = false;
				doRemove(item);
			}
			else
			{
				item.isExpanded = true;
				doAdd(item);
			}
			handler.sendEmptyMessage(UPDATA_LIST);
		}
	};

	public void doRemove(Category item) {
		for(int i = 0 ; i < item.childs.size();i++)
		{
			if(item.childs.get(i).isExpanded == true)
			doRemove(item.childs.get(i));
		}
		mItems.removeAll(item.childs);
	}

	public void doAdd(Category item) {
		int inedx = mItems.indexOf(item);
		mItems.addAll(inedx+1,item.childs);
		for(int i = 0 ; i < item.childs.size();i++)
		{
			if(item.childs.get(i).isExpanded)
			doAdd(item.childs.get(i));
		}
	}
}



