package intersky.filetools.entity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import intersky.filetools.FileUtils;

/**
 * Created by xpx on 2017/5/3.
 */

public class AlbumItem {

    public static AlbumItem mAlbumItem;
    public ArrayList<ImageBucket> contentList = new ArrayList<ImageBucket>();
    public HashMap<String, ImageBucket> bucketList = new HashMap<String, ImageBucket>();
    public ArrayList<ImageItem> adds = new ArrayList<ImageItem>();
    public boolean hasBuildImagesBucketList = false;
    public int max = 99999;
    private ContentResolver cr;
    public static synchronized AlbumItem getInstance() {
        if (mAlbumItem == null) {
            mAlbumItem = new AlbumItem();
        }

        return mAlbumItem;
    }

    public AlbumItem()
    {
        cr = FileUtils.mFileUtils.context.getContentResolver();
    }




    public void buildImagesBucketList() {

        bucketList.clear();
        long startTime = System.currentTimeMillis();

        String columns[] = new String[] { MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.PICASA_ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.TITLE,
                MediaStore.Images.Media.SIZE, MediaStore.Images.Media.BUCKET_DISPLAY_NAME };
        Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null,
                null);
        if (cur.moveToFirst()) {
            int photoIDIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int photoPathIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            int photoNameIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            int photoTitleIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
            int photoSizeIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);
            int bucketDisplayNameIndex = cur
                    .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int bucketIdIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID);
            int picasaIdIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.PICASA_ID);
            int totalNum = cur.getCount();

            do {
                String _id = cur.getString(photoIDIndex);
                String name = cur.getString(photoNameIndex);
                String path = cur.getString(photoPathIndex);
                String title = cur.getString(photoTitleIndex);
                String size = cur.getString(photoSizeIndex);
                String bucketName = cur.getString(bucketDisplayNameIndex);
                String bucketId = cur.getString(bucketIdIndex);
                String picasaId = cur.getString(picasaIdIndex);

                ImageBucket bucket = bucketList.get(bucketId);
                if (bucket == null) {
                    bucket = new ImageBucket();
                    bucketList.put(bucketId, bucket);
                    bucket.imageList = new ArrayList<ImageItem>();
                    bucket.bucketName = bucketName;
                }
                bucket.count++;
                ImageItem imageItem = new ImageItem();
                imageItem.imageId = _id;
                imageItem.imagePath = path;
                bucket.imageList.add(0,imageItem);

            } while (cur.moveToNext());
        }

        Iterator<Map.Entry<String, ImageBucket>> itr = bucketList.entrySet()
                .iterator();
        while (itr.hasNext()) {
            Map.Entry<String, ImageBucket> entry = (Map.Entry<String, ImageBucket>) itr
                    .next();
            ImageBucket bucket = entry.getValue();
            for (int i = 0; i < bucket.imageList.size(); ++i) {
                ImageItem image = bucket.imageList.get(i);
            }
        }
        hasBuildImagesBucketList = true;
        long endTime = System.currentTimeMillis();


        contentList = new ArrayList<ImageBucket>();
        Iterator<Map.Entry<String, ImageBucket>> itr1 = bucketList.entrySet()
                .iterator();
        while (itr1.hasNext()) {
            Map.Entry<String, ImageBucket> entry = (Map.Entry<String, ImageBucket>) itr1
                    .next();
            contentList.add(0, entry.getValue());
        }
    }

    public void cleanAdds()
    {
        for(int i = 0 ; i < adds.size() ;i++)
        {
            adds.get(i).isSelected = false;
        }
        adds.clear();
    }

}
