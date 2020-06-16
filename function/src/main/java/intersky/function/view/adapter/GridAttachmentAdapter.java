package intersky.function.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Attachment;
import intersky.apputils.AppUtils;
import intersky.function.R;

/**
 * Created by xpx on 2016/10/12.
 */

public class GridAttachmentAdapter extends BaseAdapter {

    public ArrayList<Attachment> mAttachments;
    private Context mContext;
    private LayoutInflater mInflater;

    public GridAttachmentAdapter(ArrayList<Attachment> mAttachments, Context mContext)
    {
        this.mContext = mContext;
        this.mAttachments = mAttachments;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mAttachments.size();
    }

    @Override
    public Attachment getItem(int position) {
        return mAttachments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        Attachment mAttachment = getItem(position);
        ViewHolder mViewHolder;
        if(convertView == null)
        {
            convertView = mInflater.inflate(R.layout.grid_attchment_item, null);
            mViewHolder = new ViewHolder();
            mViewHolder.name = (TextView) convertView.findViewById(R.id.fujian_name);
            mViewHolder.size = (TextView) convertView.findViewById(R.id.fujian_size);
            mViewHolder.dete = (TextView) convertView.findViewById(R.id.fujian_dete);
            mViewHolder.icon = (ImageView) convertView.findViewById(R.id.fujian_img);
            convertView.setTag(mViewHolder);
        }
        else
        {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.name.setText(mAttachment.mName);
        mViewHolder.size.setText(AppUtils.getSizeText(mAttachment.mSize));
        mViewHolder.dete.setText(mAttachment.mDete);
        Bus.callData(mContext,"filetools/setfileimg",mViewHolder.icon, mAttachment.mName);
        return convertView;
    }


    private static class ViewHolder {
        private TextView name;
        private TextView size;
        private TextView dete;
        private ImageView icon;
    }
}