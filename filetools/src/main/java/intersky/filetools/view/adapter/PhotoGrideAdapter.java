package intersky.filetools.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;

import intersky.apputils.AppUtils;
import intersky.apputils.BitmapCache;
import intersky.apputils.BitmapSize;
import intersky.filetools.FileUtils;
import intersky.filetools.R;
import intersky.filetools.entity.AlbumItem;
import intersky.filetools.entity.ImageItem;
import intersky.filetools.handler.PhotoSelectHandler;

public class PhotoGrideAdapter extends BaseAdapter {

    private ArrayList<ImageItem> mImageItems;
    private Context mContext;
    private LayoutInflater mInflater;
    private boolean isSignal = false;
    public Handler mHandler;

    public PhotoGrideAdapter(Context context, ArrayList<ImageItem> mImageItems, Handler mHandler) {
        this.mContext = context;
        this.mImageItems = mImageItems;
        this.isSignal = isSignal;
        this.mHandler = mHandler;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mImageItems.size();
    }

    @Override
    public ImageItem getItem(int position) {
        // TODO Auto-generated method stub
        return mImageItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ImageItem mImageItem = getItem(position);


        convertView = mInflater.inflate(R.layout.photo_gride_item, null);
        ImageView mIcon = (ImageView) convertView.findViewById(R.id.image_view);
        ImageView mSelectIcon = (ImageView) convertView.findViewById(R.id.image_select);
        mSelectIcon.setTag(mImageItem);
        mSelectIcon.setOnClickListener(selectlistener);
        if (mImageItem.isSelected == false) {
            mSelectIcon.setImageResource(R.drawable.checkbox_unchecked);
        } else {
            mSelectIcon.setImageResource(R.drawable.checkbox_checked);
        }
        if (mImageItem.imagePath != null) {
            File mfile = new File(mImageItem.imagePath);
            BitmapSize bitmapSize = BitmapCache.measureBitmap((int) (80* FileUtils.mFileUtils.mScreenDefine.density)
                    ,(int) (80*FileUtils.mFileUtils.mScreenDefine.density),mfile);
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.plugin_camera_no_pictures).override(bitmapSize.width,bitmapSize.height);
            Glide.with(mContext).load(mfile).apply(options).into(mIcon);
        }
        return convertView;
    }


    public View.OnClickListener selectlistener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            select((ImageItem) v.getTag());
        }
    };

    public void select(ImageItem item) {
        if(AlbumItem.getInstance().max > 1)
        {
            if (item.isSelected == true) {
                item.isSelected = false;
                AlbumItem.getInstance().adds.remove(item);
            } else {
                if (AlbumItem.getInstance().max > AlbumItem.getInstance().adds.size()) {
                    item.isSelected = true;
                    AlbumItem.getInstance().adds.add(item);
                } else {

                    AppUtils.showMessage(mContext,mContext.getString(R.string.album_photoaddmax1) + String.valueOf(AlbumItem.getInstance().max) + mContext.getString(R.string.album_photoaddmax2));
                }
            }
        }
        else
        {
            if (item.isSelected == true) {
                item.isSelected = false;
                AlbumItem.getInstance().adds.remove(item);
            } else {
                if (AlbumItem.getInstance().max > AlbumItem.getInstance().adds.size()) {
                    item.isSelected = true;
                    AlbumItem.getInstance().adds.add(item);
                } else {
                    AlbumItem.getInstance().adds.get(0).isSelected = false;
                    AlbumItem.getInstance().adds.clear();
                    item.isSelected = true;
                    AlbumItem.getInstance().adds.add(item);
                }
            }
        }
        if (mHandler != null)
            mHandler.sendEmptyMessage(PhotoSelectHandler.UPDATA_PHOTO_VIEW);
    }

}
