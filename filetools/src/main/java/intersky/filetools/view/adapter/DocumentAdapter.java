package intersky.filetools.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;

import intersky.filetools.FileUtils;
import intersky.filetools.R;
import intersky.filetools.entity.LocalDocument;

@SuppressLint("InflateParams")
public class DocumentAdapter extends BaseAdapter {
    private ArrayList<LocalDocument> mDocumentItems;
    private Context mContext;
    private LayoutInflater mInflater;
    private Handler mHandler;

    public DocumentAdapter(Context context, ArrayList<LocalDocument> mDocumentItems) {
        this.mContext = context;
        this.mDocumentItems = mDocumentItems;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);// LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mDocumentItems.size();
    }

    @Override
    public LocalDocument getItem(int position) {
        // TODO Auto-generated method stub
        return mDocumentItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LocalDocument mDocumentItem = getItem(position);
        convertView = mInflater.inflate(R.layout.item_document, null);
        TextView mName = (TextView) convertView.findViewById(R.id.item_name);
        TextView mDate = (TextView) convertView.findViewById(R.id.item_date);
        ImageView mIcon = (ImageView) convertView.findViewById(R.id.item_icon);
        ImageView mSelectIcon = (ImageView) convertView.findViewById(R.id.item_selecticon);
        RelativeLayout mSelectlayer = (RelativeLayout) convertView.findViewById(R.id.item_selecticon_layer);
        TextView size = (TextView) convertView.findViewById(R.id.item_size);
        Button mButton = (Button) convertView.findViewById(R.id.item_selecticon_buttom);
        size.setText("");
        if (mDocumentItem.mType == FileUtils.FAIL_TYPE_DOCUMEN) {
            mIcon.setImageResource(R.drawable.folder_icon);
        }
        else {
            mIcon.setImageResource(R.drawable.backbefore);
        }

        mName.setText(mDocumentItem.mName);
        mDate.setText(mDocumentItem.mDate);
        if(mDocumentItem.mType == FileUtils.FAIL_TYPE_DOCUMEN)
        {
            mSelectlayer.setVisibility(View.INVISIBLE);
            mButton.setVisibility(View.INVISIBLE);
        }
        else
        {
            if (mDocumentItem.isSelect) {
                mSelectIcon.setImageResource(R.drawable.bselect);
            } else {
                mSelectIcon.setImageResource(R.drawable.bunselect);
            }
            mSelectlayer.setVisibility(View.INVISIBLE);
            mButton.setVisibility(View.INVISIBLE);
        }


        return convertView;
    }

}
