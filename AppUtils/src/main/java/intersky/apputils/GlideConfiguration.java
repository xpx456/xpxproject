package intersky.apputils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.disklrucache.DiskLruCache;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.module.LibraryGlideModule;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.signature.EmptySignature;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.bumptech.glide.util.Util;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import okhttp3.OkHttpClient;

import static android.content.ContentValues.TAG;
import static com.bumptech.glide.load.Key.STRING_CHARSET_NAME;

@GlideModule
public class GlideConfiguration extends AppGlideModule {

    public static final String DISK_CACHE_SIZE = "2147483648";//最多可以缓存多少字节的数据
    public static final String DISK_CACHE_NAME = "xpx_glide";
    public static File fileCache = null;
    public volatile static GlideConfiguration mGlideConfiguration;
    public static long memorySize = 0;


    public static GlideConfiguration init(File file)
    {

        if (mGlideConfiguration == null) {
            synchronized (GlideConfiguration.class) {
                if (mGlideConfiguration == null) {
                    fileCache = file;
                    mGlideConfiguration = new GlideConfiguration();
                }
            }
        }
        return mGlideConfiguration;
    }

    public GlideConfiguration() {

    }


    @Override
    public void applyOptions(Context context, GlideBuilder builder) {

        //1.设置Glide内存缓存大小
        int maxMemory = (int) Runtime.getRuntime().maxMemory();//获取系统分配给应用的总内存大小
        memorySize = maxMemory;
        int memoryCacheSize = maxMemory / 4;//设置图片内存缓存占用八分之一
        //设置内存缓存大小
        builder.setMemoryCache(new LruResourceCache(memoryCacheSize));
        // 2.设置Glide磁盘缓存大小
//        File cacheDir = context.getExternalCacheDir();//指定的是数据的缓存地址
        //设置磁盘缓存大小
        if(fileCache != null){
            builder.setDiskCache(new DiskLruCacheFactory(fileCache.getPath(), DISK_CACHE_NAME, Long.valueOf(DISK_CACHE_SIZE)));
        }
        //builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, DISK_CACHE_NAME, diskSize));

        //3.设置图片解码格式
        // builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
//        builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565);

        //4.设置BitmapPool缓存内存大小
        builder.setBitmapPool(new LruBitmapPool(memoryCacheSize));
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide,
                                   @NonNull Registry registry) {
        // Default empty impl.
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new ProgressInterceptor());
        OkHttpClient okHttpClient = builder.build();
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(okHttpClient));
    }

    public static File getCachedFile(String url,Context context) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        String name1 = getGlide4_SafeKey(url);
        File realfail = new File(fileCache.getPath()+"/"+DISK_CACHE_NAME+"/"+name1);
        String path = realfail.getPath();
        return realfail;
    }

    public static File deleteCachedFile(String url,Context context) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        String name1 = getGlide4_SafeKey(url);
        File realfail = new File(fileCache.getPath()+"/"+DISK_CACHE_NAME+"/"+name1);
        String path = realfail.getPath();
        if(realfail.exists())
        {
            realfail.delete();
        }
        return realfail;
    }

    /**
     * Glide缓存存储路径：/data/data/your_packagexxxxxxx/cache/image_manager_disk_cache
     * Glide文件名生成规则函数 : 3.0+ 版本
     *
     * @param url    传入您的图片地址url
     * @param width  设备屏幕分辨率的宽度 eg : 1080
     * @param height 设备屏幕分辨率的高度 eg : 1920
     * @return
     * @autor 胖虎 https://blog.csdn.net/ljphhj
     */
    private String getGlide3_SafeKey(String url, int width, int height) {
        byte[] dimensions = ByteBuffer.allocate(8)
                .putInt(width)
                .putInt(height)
                .array();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            EmptySignature signature = EmptySignature.obtain();
            signature.updateDiskCacheKey(messageDigest);
            messageDigest.update(url.getBytes(STRING_CHARSET_NAME));
            messageDigest.update(dimensions);
            messageDigest.update("".getBytes(STRING_CHARSET_NAME));
            messageDigest.update("ImageVideoBitmapDecoder.com.bumptech.glide.load.resource.bitmap".getBytes(STRING_CHARSET_NAME));
            messageDigest.update("FitCenter.com.bumptech.glide.load.resource.bitmap".getBytes(STRING_CHARSET_NAME));
            messageDigest.update("BitmapEncoder.com.bumptech.glide.load.resource.bitmap".getBytes(STRING_CHARSET_NAME));
            messageDigest.update("".getBytes(STRING_CHARSET_NAME));
            String safeKey = Util.sha256BytesToHex(messageDigest.digest());
            return safeKey + ".0";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Glide缓存存储路径：/data/data/your_packagexxxxxxx/cache/image_manager_disk_cache
     * Glide文件名生成规则函数 : 4.0+ 版本
     *
     * @param url 图片地址url
     * @return 返回图片在磁盘缓存的key值
     */
    private static String getGlide4_SafeKey(String url) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            EmptySignature signature = EmptySignature.obtain();
            signature.updateDiskCacheKey(messageDigest);
            new GlideUrl(url).updateDiskCacheKey(messageDigest);
            String safeKey = Util.sha256BytesToHex(messageDigest.digest());
            return safeKey + ".0";
        } catch (Exception e) {
        }
        return null;
    }

    static class OriginalKey implements Key {
        private final String id;
        private final Key signature;
        private OriginalKey(String id, Key signature) {
            this.id = id;
            this.signature = signature;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            OriginalKey that = (OriginalKey) o;
            return id.equals(that.id) && signature.equals(that.signature);
        }

        @Override
        public int hashCode() {
            int result = id.hashCode();
            result = 31 * result + signature.hashCode();
            return result;
        }

        @Override
        public void updateDiskCacheKey(MessageDigest messageDigest) {
            try {
                messageDigest.update(id.getBytes(STRING_CHARSET_NAME));
                signature.updateDiskCacheKey(messageDigest);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
    }

    public void clearImageAllCache(Context context) {
        clearImageDiskCache(context);
        clearImageMemoryCache(context);
        String ImageExternalCatchDir=context.getExternalCacheDir()+ ExternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR;
        deleteFolderFile(ImageExternalCatchDir, true);
    }

    /**
     * 清除图片磁盘缓存
     */
    public void clearImageDiskCache(final Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context).clearDiskCache();
// BusUtil.getBus().post(new GlideCacheClearSuccessEvent());
                    }
                }).start();
            } else {
                Glide.get(context).clearDiskCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除图片内存缓存
     */
    public void clearImageMemoryCache(Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) { //只能在主线程执行
                Glide.get(context).clearMemory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除指定目录下的文件，这里用于缓存的删除
     *
     * @param filePath filePath
     * @param deleteThisPath deleteThisPath
     */
    private void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {
                    File files[] = file.listFiles();
                    for (File file1 : files) {
                        deleteFolderFile(file1.getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {
                        file.delete();
                    } else {
                        if (file.listFiles().length == 0) {
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
