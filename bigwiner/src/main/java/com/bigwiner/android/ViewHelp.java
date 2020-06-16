package com.bigwiner.android;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigwiner.R;

import java.util.ArrayList;

import intersky.mywidget.MyLinearLayout;

public class ViewHelp {


    public static void measureConversationLable(MyLinearLayout mMyLinearLayout, String labe, int id, LayoutInflater mInflater) {
        View view = mInflater.inflate(R.layout.conversation_labe_item,null);
        TextView textView = view.findViewById(R.id.conversation_lable);
        textView.setBackgroundResource(id);
        textView.setText(labe);
        mMyLinearLayout.addView(view);
    }

    public static void praseLeaves(ArrayList<ImageView> imageViews,int leaves) {
        for(int i = 0 ; i < imageViews.size() ; i++)
        {
            if(i<leaves)
            {
                imageViews.get(i).setImageResource(R.drawable.stars);
            }
            else
            {
                imageViews.get(i).setImageResource(R.drawable.star);
            }
        }
    }

    public static View measureConversationLable(MyLinearLayout mMyLinearLayout, String labe,int imageid ,int id, LayoutInflater mInflater,int textcolor) {
        View view = mInflater.inflate(R.layout.conversation_labe_img_item,null);
        TextView textView = view.findViewById(R.id.cinfirmsubject);
        ImageView imageView = view.findViewById(R.id.confirmimg);
        RelativeLayout bg = view.findViewById(R.id.confirm);
        bg.setBackgroundResource(id);
        textView.setText(labe);
        textView.setTextColor(textcolor);
        imageView.setImageResource(imageid);
        mMyLinearLayout.addView(view);
        return view;
    }

    public static void measureConversationLable(MyLinearLayout mMyLinearLayout, String labe,int imageid ,int id, LayoutInflater mInflater,int textcolor1,int textcolor2,String count) {
        View view = mInflater.inflate(R.layout.conversation_labe_img_count_item,null);
        TextView textView = view.findViewById(R.id.cinfirmsubject);
        TextView textView2 = view.findViewById(R.id.count);
        ImageView imageView = view.findViewById(R.id.confirmimg);
        RelativeLayout bg = view.findViewById(R.id.confirm);
        bg.setBackgroundResource(id);
        textView.setText(labe);
        textView.setTextColor(textcolor1);
        textView2.setText(count);
        textView2.setTextColor(textcolor2);
        imageView.setImageResource(imageid);
        mMyLinearLayout.addView(view);
    }
}
