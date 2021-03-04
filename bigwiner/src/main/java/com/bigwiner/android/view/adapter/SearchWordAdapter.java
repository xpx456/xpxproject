package com.bigwiner.android.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigwiner.R;
import com.bigwiner.android.entity.SearchWord;

import java.util.ArrayList;

import intersky.appbase.entity.Conversation;
import intersky.apputils.TimeUtils;


/**
 * Created by xpx on 2016/10/12.
 */

public class SearchWordAdapter extends BaseAdapter {

    private ArrayList<SearchWord> mConversations;
    private Context mContext;
    private LayoutInflater mInflater;
    private boolean type = false;

    public SearchWordAdapter(ArrayList<SearchWord> mConversations, Context mContext, boolean type) {
        this.mContext = mContext;
        this.mConversations = mConversations;
        this.type = type;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public SearchWordAdapter(ArrayList<SearchWord> mConversations, Context mContext) {
        this.mContext = mContext;
        this.mConversations = mConversations;
        this.type = type;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mConversations.size();
    }

    @Override
    public SearchWord getItem(int position) {
        return mConversations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        SearchWord mConversation = getItem(position);
        ViewHolder viewHolder;
        if(convertView != null)
        {
            convertView = mInflater.inflate(R.layout.search_word,null);
            viewHolder = new ViewHolder();
            viewHolder.textView = convertView.findViewById(R.id.conversation_title);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.textView.setText(mConversation.title);
        return convertView;
    }


    public class ViewHolder{
        TextView textView;
    }
}
