package intersky.document.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;

import intersky.appbase.Actions;
import intersky.appbase.bus.Bus;
import intersky.apputils.AppUtils;
import intersky.apputils.MyGlideUrl;
import intersky.document.DocumentManager;
import intersky.document.R;
import intersky.document.asks.DocumentAsks;
import intersky.document.entity.DocumentNet;
import intersky.document.handler.DocumentHandler;

@SuppressLint("InflateParams")
public class DocumentPostationAdapter extends BaseAdapter {
    private ArrayList<DocumentNet> mDocumentItems;
    private Context mContext;
    private LayoutInflater mInflater;
    private Handler mHandler;

    public DocumentPostationAdapter(Context context, ArrayList<DocumentNet> mDocumentItems, Handler mHandler) {
        this.mContext = context;
        this.mDocumentItems = mDocumentItems;
        this.mHandler = mHandler;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);// LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mDocumentItems.size();
    }

    @Override
    public DocumentNet getItem(int position) {
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
        DocumentNet mDocumentItem = getItem(position);
        convertView = mInflater.inflate(R.layout.item_document_postation, null);
        TextView mName = (TextView) convertView.findViewById(R.id.item_name);
        TextView mDate = (TextView) convertView.findViewById(R.id.item_date);
        ImageView mIcon = (ImageView) convertView.findViewById(R.id.item_icon);

        TextView size = (TextView) convertView.findViewById(R.id.item_size);
        Button mButton = (Button) convertView.findViewById(R.id.item_selecticon_buttom);
        size.setText("");
        if (mDocumentItem.mType == Actions.FAIL_TYPE_DOCUMEN || mDocumentItem.mType == DocumentManager.TYPE_DOCUMENT) {
            mIcon.setImageResource(R.drawable.folder_icon);
        } else {
            if (mDocumentItem.mState == DocumentManager.STATE_FINISH) {
                Bus.callData(mContext,"filetools/setfileimgfinish",mIcon, mDocumentItem.mName);
                if ((int)Bus.callData(mContext,"filetools/getFileType",mDocumentItem.mName) == Actions.FILE_TYPE_PICTURE) {
                    RequestOptions options = new RequestOptions()
                            .placeholder(R.drawable.plugin_camera_no_pictures);
                    File file = new File(mDocumentItem.mPath);
                    if(file.exists())
                    {
                        Glide.with(mContext).load(file).apply(options).into(mIcon);
                    }
                    else
                    {
                        Glide.with(mContext).load(mDocumentItem.mUrl).apply(options).into(mIcon);
                    }
                }


            } else {
                Bus.callData(mContext,"filetools/setfileimg",mIcon, mDocumentItem.mName);
                if ((int)Bus.callData(mContext,"filetools/getFileType",mDocumentItem.mName) == Actions.FILE_TYPE_PICTURE) {
                    RequestOptions options = new RequestOptions()
                            .placeholder(R.drawable.plugin_camera_no_pictures).diskCacheStrategy(DiskCacheStrategy.ALL);;
                    Glide.with(mContext).load(new MyGlideUrl(DocumentAsks.praseUrl(mDocumentItem))).apply(options).into(mIcon);
                }
            }
            size.setText(AppUtils.getSizeText(mDocumentItem.mSize));
        }

        mName.setText(mDocumentItem.mName);
        mDate.setText(mDocumentItem.mDate);
        return convertView;
    }

}
