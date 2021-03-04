package com.bigwiner.android.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigwiner.R;
import com.bigwiner.android.entity.Source;

import java.util.ArrayList;
import java.util.HashMap;

import intersky.appbase.entity.Conversation;
import intersky.apputils.TimeUtils;
import intersky.select.entity.Select;


/**
 * Created by xpx on 2016/10/12.
 */

public class SourceAdapter extends BaseAdapter {

    public ArrayList<Select> source;
    private Context mContext;
    private LayoutInflater mInflater;
    private boolean type = false;
    public HashMap<String ,Select> hashMap;
    public SourceAdapter(ArrayList<Select> source, Context mContext) {
        this.mContext = mContext;
        this.source = source;
        this.type = type;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public SourceAdapter(ArrayList<Select> source, Context mContext,boolean type ,HashMap<String ,Select> hashMap) {
        this.mContext = mContext;
        this.source = source;
        this.type = type;
        this.hashMap = hashMap;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return source.size();
    }

    @Override
    public Select getItem(int position) {
        return source.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        Select mConversation = getItem(position);
        if(convertView == null )
        {
            convertView = mInflater.inflate(R.layout.sourcedata,null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.title = convertView.findViewById(R.id.conversation_title);
            viewHolder.imageView = convertView.findViewById(R.id.select);
            convertView.setTag(viewHolder);
        }
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.title.setText(mConversation.mName);
        if(type == false)
        {
            if(mConversation.iselect)
            {
                viewHolder.imageView.setVisibility(View.VISIBLE);
            }
            else
            {
                viewHolder.imageView.setVisibility(View.INVISIBLE);
            }
        }
        else
        {
            if(hashMap != null)
            {
                if(hashMap.containsKey(mConversation.mId))
                {
                    viewHolder.imageView.setVisibility(View.VISIBLE);
                }
                else
                {
                    viewHolder.imageView.setVisibility(View.INVISIBLE);
                }
            }
            else
            {
                viewHolder.imageView.setVisibility(View.VISIBLE);
            }
        }
        return convertView;
    }


    class ViewHolder{
        TextView title;
        ImageView imageView;
    }
}
