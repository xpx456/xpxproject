package intersky.filetools;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.durban.Controller;
import com.yanzhenjie.durban.Durban;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import intersky.appbase.BaseActivity;
import intersky.appbase.GetProvideGetPath;
import intersky.appbase.MySimpleTarget;
import intersky.appbase.PermissionCode;
import intersky.appbase.PermissionResult;
import intersky.appbase.ScreenDefine;
import intersky.appbase.entity.Attachment;
import intersky.appbase.entity.CheckPath;
import intersky.appbase.utils.XpxShare;
import intersky.apputils.AppUtils;
import intersky.filetools.entity.AlbumItem;
import intersky.filetools.entity.ImageItem;
import intersky.filetools.entity.LocalDocument;
import intersky.filetools.handler.DownloadThreadHandler;
import intersky.filetools.handler.PermissionHandler;
import intersky.filetools.thread.DownloadThread;
import intersky.filetools.view.activity.AllFileActivity;
import intersky.filetools.view.activity.AttachmentActivity;
import intersky.filetools.view.activity.BigImageViewActivity;
import intersky.filetools.view.activity.PhotoSelectActivity;
import intersky.filetools.view.activity.SaveAttachmentActivity;
import intersky.filetools.view.activity.VideoActivity;
import intersky.mywidget.MyLinearLayout;
import intersky.xpxnet.net.NetObject;

public class FileUtils {
    public static final String ATTACHMENT_DOWNLOAD_FINISH = "ATTACHMENT_DOWNLOAD_FINISH";
    public static final String ATTACHMENT_DOWNLOAD_UPDATA = "ATTACHMENT_DOWNLOAD_UPDATA";
    public static final String ATTACHMENT_DOWNLOAD_FAIL = "ATTACHMENT_DOWNLOAD_FAIL";
    public static final String ATTACHMENT_SET_POSITION = "ATTACHMENT_SET_POSITION";
    public static final String ATTACHMENT_SAVE_NET_FILE_SUCCESS = "ATTACHMENT_SAVE_NET_FILE_SUCCESS";
    public static final String ATTACHMENT_SAVE_NET_FILE_FAIL = "ATTACHMENT_SAVE_NET_FILE_FAIL";
    public static final int TAKE_PHOTO = 0x1;
    public static final int TAKE_VIDEO = 0x2;
    public static final int CHOSE_PICTURE = 0x3;
    public static final int SELECT_TYPE_NOMAL = 7000;
    public static final int SELECT_TYPE_DOCUMENTMANAGER = 7001;
    public static final int FAIL_TYPE_DOCUMEN = 200;
    public static final int FAIL_TYPE_ENDBACK = 201;
    public static final int FILE_TYPE_UNKNOW = 300;
    public static final int FILE_TYPE_PICTURE = 301;
    public static final int FILE_TYPE_TXT = 302;
    public static final int FILE_TYPE_VIDEO = 303;
    public static final int FILE_TYPE_PPT = 304;
    public static final int FILE_TYPE_WORD = 305;
    public static final int FILE_TYPE_EXL = 306;
    public static final int FILE_TYPE_PDF = 307;
    public static final int FILE_TYPE_MUSIC = 308;
    public static final int FILE_TYPE_HTML = 309;
    public static final int FILE_TYPE_APK = 310;
    public static DisplayMetrics metric;
    public static ArrayList<LocalDocument> localPaths = new ArrayList<LocalDocument>();
    public volatile static FileUtils mFileUtils;
    public static Context context;
    public static View.OnClickListener setPositionListener = null;
    public static CheckPath mCheckPath = null;
    public static Uri takePhotoUri;
    public static Uri takeVideoUri;
    public static String takePhotoPath;
    public static String takeVideoPath;
    public static PathUtils pathUtils;
    public static HashMap<String ,DownloadThread>  downloadAttachments = new HashMap<String ,DownloadThread>();
    public static HashMap<String ,DownloadThread>  saveAttachments = new HashMap<String ,DownloadThread>();
    public static DownloadThread mDownloadThread;
    public static DownloadThreadHandler mDownloadThreadHandler;
    public static FileOperation operation;
    public static XpxShare xpxShare;
    public static ScreenDefine mScreenDefine;
    public static GetProvideGetPath metProvideGetPath;


    public static FileUtils init(Context context, GetProvideGetPath metProvideGetPath, FileOperation operation, XpxShare xpxShare) {

        if (mFileUtils == null) {
            synchronized (FileUtils.class) {
                if (mFileUtils == null) {
                    mFileUtils = new FileUtils(context,metProvideGetPath,operation,xpxShare);
                }
                else
                {
                    mFileUtils.metProvideGetPath = metProvideGetPath;
                    mFileUtils.context = context;
                    mFileUtils.operation = operation;
                    mFileUtils.xpxShare = xpxShare;
                    mFileUtils.mScreenDefine = new ScreenDefine(context);
                    metric = AppUtils.getWindowInfo(context);
                    pathUtils = PathUtils.init();
                    localPaths.clear();
                    if(localPaths.size() == 0) {
                        if (pathUtils.getBasePath() != null) {
                            addtag(pathUtils.getBasePath());
                        } else {
                            localPaths.add(new LocalDocument(FileUtils.FAIL_TYPE_DOCUMEN,"/storage", "storage" , ""));
                        }
                    }
                }
            }
        }
        return mFileUtils;
    }

    public FileUtils(Context context,GetProvideGetPath metProvideGetPath,FileOperation operation,XpxShare xpxShare) {
        this.metProvideGetPath = metProvideGetPath;
        this.context = context;
        this.operation = operation;
        this.xpxShare = xpxShare;
        this.mScreenDefine = new ScreenDefine(context);
        metric = AppUtils.getWindowInfo(context);
        pathUtils = PathUtils.init();
        if(localPaths.size() == 0) {
            if (pathUtils.getBasePath() != null) {
                addtag(pathUtils.getBasePath());
            } else {
                localPaths.add(new LocalDocument(FileUtils.FAIL_TYPE_DOCUMEN,"/storage", "storage" , ""));
            }
        }
    }

    private static void addtag(String path) {
        if (path != null) {
            File mfile = new File(path);
            localPaths.add(new LocalDocument(FileUtils.FAIL_TYPE_DOCUMEN,"sdcard",
                    mfile.getPath(), mfile.getParent()));
            while (mfile.getParent() != null) {
                mfile = new File(mfile.getParent());
                if (mfile.getName().length() != 0) {
                    localPaths.add(0, new LocalDocument(FileUtils.FAIL_TYPE_DOCUMEN,mfile.getName(),
                            mfile.getPath(), mfile.getParent()));
                } else {
                    continue;
                }

            }
        }

    }

    public int getFileType(String aname) {
        String name = "";
        if (aname != null) {
            name = aname.toLowerCase();
        }

        if (name.endsWith(".rmvb") || name.endsWith(".mp4") || name.endsWith(".wmv") || name.endsWith(".avi")
                || name.endsWith(".flv")) {
            return FILE_TYPE_VIDEO;
        } else if (name.endsWith(".mp3") || name.endsWith(".wav")) {
            return FILE_TYPE_MUSIC;
        } else if (name.endsWith(".txt")) {
            return FILE_TYPE_TXT;
        } else if (name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".gif")
                || name.endsWith(".jpeg") || name.endsWith(".bmp") || name.endsWith(".pcx")
                || name.endsWith(".dxf") || name.endsWith(".wmf") || name.endsWith(".tga")) {
            return FILE_TYPE_PICTURE;
        } else if (name.endsWith(".doc") || name.endsWith(".docx")) {
            return FILE_TYPE_WORD;
        } else if (name.endsWith(".xls") || name.endsWith(".xlsx")) {
            return FILE_TYPE_EXL;
        } else if (name.endsWith(".ppt") || name.endsWith(".pptx")) {
            return FILE_TYPE_PPT;
        } else if (name.endsWith(".pdf")) {
            return FILE_TYPE_PDF;
        } else if (name.endsWith(".html")) {
            return FILE_TYPE_HTML;
        } else if (name.endsWith(".apk")) {
            return FILE_TYPE_APK;
        } else {
            return FILE_TYPE_UNKNOW;
        }

    }

    public Intent openfile(File mLoadItem) {
        Intent mopenten = null;
        switch (getFileType(mLoadItem.getName().toLowerCase())) {
            case FileUtils.FILE_TYPE_HTML:
                mopenten = FileUtils.getHtmlFileIntent(mLoadItem);
                break;
            case FileUtils.FILE_TYPE_PICTURE:
                mopenten = FileUtils.getImageFileIntent(mLoadItem);
                break;
            case FileUtils.FILE_TYPE_PDF:
                mopenten = FileUtils.getPdfFileIntent(mLoadItem);
                break;
            case FileUtils.FILE_TYPE_TXT:
                mopenten = FileUtils.getTextFileIntent(mLoadItem);
                break;
            case FileUtils.FILE_TYPE_MUSIC:
                mopenten = FileUtils.getAudioFileIntent(mLoadItem);
                break;
            case FileUtils.FILE_TYPE_VIDEO:
                mopenten = FileUtils.getVideoFileIntent(mLoadItem);
                break;
            case FileUtils.FILE_TYPE_WORD:
                mopenten = FileUtils.getWordFileIntent(mLoadItem);
                break;
            case FileUtils.FILE_TYPE_EXL:
                mopenten = FileUtils.getExcelFileIntent(mLoadItem);
                break;
            case FileUtils.FILE_TYPE_PPT:
                mopenten = FileUtils.getPPTFileIntent(mLoadItem);
                break;
            case FileUtils.FILE_TYPE_APK:
                mopenten = FileUtils.getApkFileIntent(mLoadItem);
                break;
        }

        return mopenten;
    }

    public void setfileimgfinish(ImageView mViwe, String name) {
        if (name != null) {
            switch (getFileType(name)) {
                case FileUtils.FILE_TYPE_HTML:
                    mViwe.setImageResource(R.drawable.icon_list_html2);
                    break;
                case FileUtils.FILE_TYPE_PICTURE:
                    mViwe.setImageResource(R.drawable.icon_list_image2);
                    break;
                case FileUtils.FILE_TYPE_PDF:
                    mViwe.setImageResource(R.drawable.icon_list_pdf2);
                    break;
                case FileUtils.FILE_TYPE_TXT:
                    mViwe.setImageResource(R.drawable.icon_list_txtfile2);
                    break;
                case FileUtils.FILE_TYPE_MUSIC:
                    mViwe.setImageResource(R.drawable.icon_list_audiofile2);
                    break;
                case FileUtils.FILE_TYPE_VIDEO:
                    mViwe.setImageResource(R.drawable.icon_list_videofile2);
                    break;
                case FileUtils.FILE_TYPE_WORD:
                    mViwe.setImageResource(R.drawable.icon_list_doc2);
                    break;
                case FileUtils.FILE_TYPE_EXL:
                    mViwe.setImageResource(R.drawable.icon_list_excel2);
                    break;
                case FileUtils.FILE_TYPE_PPT:
                    mViwe.setImageResource(R.drawable.icon_list_ppt2);
                    break;
                case FileUtils.FILE_TYPE_APK:
                    mViwe.setImageResource(R.drawable.icon_list_apk2);
                    break;
                default:
                    mViwe.setImageResource(R.drawable.icon_list_unknown2);
                    break;
            }
        }

    }

    public void setfileimg(ImageView mViwe, String name) {
        if (name != null) {
            switch (getFileType(name)) {
                case FileUtils.FILE_TYPE_HTML:
                    mViwe.setImageResource(R.drawable.icon_list_html);
                    break;
                case FileUtils.FILE_TYPE_PICTURE:
                    mViwe.setImageResource(R.drawable.icon_list_image);
                    break;
                case FileUtils.FILE_TYPE_PDF:
                    mViwe.setImageResource(R.drawable.icon_list_pdf);
                    break;
                case FileUtils.FILE_TYPE_TXT:
                    mViwe.setImageResource(R.drawable.icon_list_txtfile);
                    break;
                case FileUtils.FILE_TYPE_MUSIC:
                    mViwe.setImageResource(R.drawable.icon_list_audiofile);
                    break;
                case FileUtils.FILE_TYPE_VIDEO:
                    mViwe.setImageResource(R.drawable.icon_list_videofile);
                    break;
                case FileUtils.FILE_TYPE_WORD:
                    mViwe.setImageResource(R.drawable.icon_list_doc);
                    break;
                case FileUtils.FILE_TYPE_EXL:
                    mViwe.setImageResource(R.drawable.icon_list_excel);
                    break;
                case FileUtils.FILE_TYPE_PPT:
                    mViwe.setImageResource(R.drawable.icon_list_ppt);
                    break;
                case FileUtils.FILE_TYPE_APK:
                    mViwe.setImageResource(R.drawable.icon_list_apk);
                    break;
                default:
                    mViwe.setImageResource(R.drawable.icon_list_unknown);
                    break;
            }
        }

    }

    public static Intent shareHtmlFileIntent(File file) {

        Intent intent = new Intent("android.intent.action.VIEW");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = metProvideGetPath.getProvideGetPath(file);
            intent.setAction(Intent.ACTION_SEND);
            intent.setDataAndType(contentUri, "text/html");
        } else {
            Uri uri = Uri.parse(file.toString()).buildUpon().encodedAuthority("com.android.htmlfileprovider")
                    .scheme("content").encodedPath(file.toString()).build();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setDataAndType(uri, "text/html");
        }

        return intent;
    }

    // android获取一个用于打开HTML文件的intent
    public static Intent getHtmlFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = metProvideGetPath.getProvideGetPath(file);
            intent.setAction(Intent.ACTION_SEND);
            intent.setDataAndType(contentUri, "text/html");
        } else {
            Uri uri = Uri.parse(file.toString()).buildUpon().encodedAuthority("com.android.htmlfileprovider")
                    .scheme("content").encodedPath(file.toString()).build();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setDataAndType(uri, "text/html");
        }
        return intent;
    }

    // android获取一个用于打开图片文件的intent
    public static Intent getImageFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = metProvideGetPath.getProvideGetPath(file);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(contentUri, "image/*");
        } else {
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "image/*");
        }

        return intent;
    }

    // android获取一个用于打开PDF文件的intent
    public static Intent getPdfFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = metProvideGetPath.getProvideGetPath(file);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(contentUri, "application/pdf");
        } else {
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/pdf");
        }

        return intent;
    }

    public static Intent sharePdfFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = metProvideGetPath.getProvideGetPath(file);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_SEND);
            intent.setDataAndType(contentUri, "application/pdf");
        } else {
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(file);
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setDataAndType(uri, "application/pdf");
        }

        return intent;
    }

    // android获取一个用于打开文本文件的intent
    public static Intent getTextFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = metProvideGetPath.getProvideGetPath(file);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(contentUri, "text/plain");
        } else {
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri2 = Uri.fromFile(file);
            intent.setDataAndType(uri2, "text/plain");
        }

        return intent;
    }

    public static Intent shareTextFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = metProvideGetPath.getProvideGetPath(file);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_SEND);
            intent.setDataAndType(contentUri, "text/plain");
        } else {
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_SEND);
            Uri uri = Uri.fromFile(file);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setDataAndType(uri, "text/plain");
        }

        return intent;
    }

    // android获取一个用于打开音频文件的intent
    public static Intent getAudioFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = metProvideGetPath.getProvideGetPath(file);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("oneshot", 0);
            intent.putExtra("configchange", 0);
            intent.setDataAndType(contentUri, "audio/*");
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("oneshot", 0);
            intent.putExtra("configchange", 0);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "audio/*");
        }

        return intent;
    }

    public static Intent shareAudioFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = metProvideGetPath.getProvideGetPath(file);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("oneshot", 0);
            intent.putExtra("configchange", 0);
            intent.setAction(Intent.ACTION_SEND);
            intent.setDataAndType(contentUri, "audio/*");
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("oneshot", 0);
            intent.putExtra("configchange", 0);
            Uri uri = Uri.fromFile(file);
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setDataAndType(uri, "audio/*");
        }

        return intent;
    }

    // android获取一个用于打开视频文件的intent
    public static Intent getVideoFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = metProvideGetPath.getProvideGetPath(file);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("oneshot", 0);
            intent.putExtra("configchange", 0);
            intent.setDataAndType(contentUri, "video/*");
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("oneshot", 0);
            intent.putExtra("configchange", 0);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "video/*");
        }

        return intent;
    }

    public static Intent shareVideoFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = metProvideGetPath.getProvideGetPath(file);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("oneshot", 0);
            intent.putExtra("configchange", 0);
            intent.setAction(Intent.ACTION_SEND);
            intent.setDataAndType(contentUri, "video/*");
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("oneshot", 0);
            intent.putExtra("configchange", 0);
            Uri uri = Uri.fromFile(file);
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setDataAndType(uri, "video/*");
        }

        return intent;
    }

    // android获取一个用于打开CHM文件的intent
    public static Intent getChmFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = metProvideGetPath.getProvideGetPath(file);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(contentUri, "application/x-chm");
        } else {
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/x-chm");
        }

        return intent;
    }

    // android获取一个用于打开CHM文件的intent
    public static Intent shareChmFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = metProvideGetPath.getProvideGetPath(file);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_SEND);
            intent.setDataAndType(contentUri, "application/x-chm");
        } else {
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(file);
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setDataAndType(uri, "application/x-chm");
        }

        return intent;
    }

    // android获取一个用于打开Word文件的intent
    public static Intent getWordFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = metProvideGetPath.getProvideGetPath(file);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(contentUri, "application/msword");
        } else {
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/msword");
        }

        return intent;
    }

    // android获取一个用于打开Word文件的intent
    public static Intent shareWordFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = metProvideGetPath.getProvideGetPath(file);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_SEND);
            intent.setDataAndType(contentUri, "application/msword");
        } else {
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/msword");
        }

        return intent;
    }

    // android获取一个用于打开Excel文件的intent
    public static Intent getExcelFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = metProvideGetPath.getProvideGetPath(file);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(contentUri, "application/vnd.ms-excel");
        } else {
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        }
        return intent;
    }

    public static Intent shareExcelFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = metProvideGetPath.getProvideGetPath(file);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_SEND);
            intent.setDataAndType(contentUri, "application/vnd.ms-excel");
        } else {
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        }


        return intent;
    }

    // android获取一个用于打开PPT文件的intent
    public static Intent getPPTFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = metProvideGetPath.getProvideGetPath(file);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(contentUri, "application/vnd.ms-powerpoint");
        } else {
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        }
        return intent;
    }

    public static Intent sharePPTFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = metProvideGetPath.getProvideGetPath(file);
            intent.setDataAndType(contentUri, "application/vnd.ms-powerpoint");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_SEND);
        } else {
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        }
        return intent;
    }

    // android获取一个用于打开apk文件的intent
    public static Intent getApkFileIntent(File file) {

        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri contentUri = metProvideGetPath.getProvideGetPath(file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            intent.setAction(Intent.ACTION_VIEW);
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
        }
        return intent;
    }

    public static Intent shareApkFileIntent(File file) {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = metProvideGetPath.getProvideGetPath(file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_SEND);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        return intent;
    }

    public Uri getOutputMediaFileUri(String ipath,boolean isvideo) {
//        File picDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String path = ipath + "/" + timeStamp + ".jpg";
        File videoFile = new File(path);
        if(isvideo)
        {
            takeVideoPath = path;
        }
        else
        {
            takePhotoPath = path;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            return metProvideGetPath.getProvideGetPath(videoFile);
        }
        else
        {
            return Uri.fromFile(videoFile);
        }

    }

    public Uri getOutputHeadMediaFileUri(String ipath,String name) {
        String path = ipath + "/" + name+"_h" + ".jpg";
        File videoFile = new File(path);
        return Uri.fromFile(videoFile);

    }

    public Uri getOutputBgMediaFileUri(String ipath,String name) {
        String path = ipath + "/" + name+"_b" + ".jpg";
        File videoFile = new File(path);
        return Uri.fromFile(videoFile);
    }

    public void getPhotos(Context mContext,boolean cantakephoto,int maxcount,String classname,String action) {
        Intent intent = new Intent(mContext, PhotoSelectActivity.class);
        intent.setAction(action);
        if(cantakephoto)
        intent.putExtra("takephoto",true);
        setPositionListener = null;
        if(maxcount <= 9)
        intent.putExtra("max",maxcount);
        else
            intent.putExtra("max",9);
        intent.putExtra("class",classname);
        mContext.startActivity(intent);
    }

    public void getPhotos(Context mContext,boolean cantakephoto,int maxcount,String classname,String action,View.OnClickListener listener,String position,CheckPath checkPath) {
        Intent intent = new Intent(mContext, PhotoSelectActivity.class);
        intent.setAction(action);
        if(cantakephoto)
            intent.putExtra("takephoto",true);
        setPositionListener = listener;
        mCheckPath = checkPath;
        intent.putExtra("type",FileUtils.SELECT_TYPE_DOCUMENTMANAGER);
        intent.putExtra("class",classname);
        intent.putExtra("position",position);
        mContext.startActivity(intent);
    }

    public void selectPhotos(Activity mContext) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        mContext.startActivityForResult(intent, CHOSE_PICTURE);
    }

    public void selectPhotos(Activity mContext,int id) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        mContext.startActivityForResult(intent, id);
    }

    public void getFiles(Context mContext,String classname,String action) {
        Intent intent = new Intent(mContext, AllFileActivity.class);
        intent.setAction(action);
        setPositionListener = null;
        intent.putExtra("class",classname);
        mContext.startActivity(intent);
    }

    public void getFiles(Context mContext,String classname,String action,View.OnClickListener listener,String position,CheckPath checkPath) {
        Intent intent = new Intent(mContext, AllFileActivity.class);
        intent.setAction(action);
        setPositionListener = listener;
        intent.putExtra("type",FileUtils.SELECT_TYPE_DOCUMENTMANAGER);
        intent.putExtra("class",classname);
        intent.putExtra("position",position);
        mCheckPath = checkPath;
        mContext.startActivity(intent);
    }

    public void getVideos(Context mContext,boolean cantakephoto,String classname,String action)
    {
        Intent intent = new Intent(mContext, VideoActivity.class);
        intent.setAction(action);
        if(cantakephoto)
            intent.putExtra("takephoto",true);
        setPositionListener = null;
        intent.putExtra("class",classname);
        mContext.startActivity(intent);
    }

    public void getVideos(Context mContext,boolean cantakephoto,String classname,String action,View.OnClickListener listener,String position,CheckPath checkPath) {
        Intent intent = new Intent(mContext, VideoActivity.class);
        intent.setAction(action);
        if(cantakephoto)
            intent.putExtra("takephoto",true);
        setPositionListener = listener;
        intent.putExtra("type",FileUtils.SELECT_TYPE_DOCUMENTMANAGER);
        intent.putExtra("class",classname);
        intent.putExtra("position",position);
        mCheckPath = checkPath;
        mContext.startActivity(intent);
    }

    public void takePhoto(Activity mContext,  String path)
    {
        takePhotoUri = null;
        takePhotoUri = getOutputMediaFileUri(path,false);
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, takePhotoUri);
        mContext.startActivityForResult(openCameraIntent, TAKE_PHOTO);
    }

    public void takePhoto(Activity mContext,  String path,int id)
    {
        takePhotoUri = null;
        takePhotoUri = getOutputMediaFileUri(path,false);
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, takePhotoUri);
        mContext.startActivityForResult(openCameraIntent, id);
    }

    public void takeVideo(Activity mContext, String path) {
        takeVideoUri = null;
        takeVideoUri = getOutputMediaFileUri(path,false);
        Intent openCameraIntent = new Intent(
                MediaStore.ACTION_VIDEO_CAPTURE);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, takeVideoUri);
        openCameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        mContext.startActivityForResult(openCameraIntent, TAKE_VIDEO);
    }

    public void takeVideo(Activity mContext, String path,int id) {
        takeVideoUri = null;
        takeVideoUri = getOutputMediaFileUri(path,false);
        Intent openCameraIntent = new Intent(
                MediaStore.ACTION_VIDEO_CAPTURE);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, takeVideoUri);
        openCameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        mContext.startActivityForResult(openCameraIntent, id);
    }

    public void saveLocal(Context context,File file) {
        Intent intent = new Intent(context, SaveAttachmentActivity.class);
        intent.putExtra("path",file.getPath());
        context.startActivity(intent);
    }

    public void saveNet(Context context,File file,String url) {
        Intent intent = new Intent(context, SaveAttachmentActivity.class);
        intent.putExtra("path",file.getPath());
        intent.putExtra("url",url);
        context.startActivity(intent);
    }

    public PermissionResult checkPermissionTakePhoto(Activity context, String path) {
        PermissionResult mPermissionRepuest = new TakePhotoPremissionResult(context,path);
        Handler handler = new PermissionHandler(context,path);
        AppUtils.getPermission(Manifest.permission.CAMERA,context, PermissionCode.PERMISSION_REQUEST_CAMERA_PHOTO,handler);
        return mPermissionRepuest;
    }

    public PermissionResult checkPermissionTakePhoto(Activity context, String path,int id) {
        PermissionResult mPermissionRepuest = new TakePhotoPremissionResult(context,path);
        Handler handler = new PermissionHandler(context,path,id);
        AppUtils.getPermission(Manifest.permission.CAMERA,context, PermissionCode.PERMISSION_REQUEST_CAMERA_PHOTO,handler);
        return mPermissionRepuest;
    }

    public PermissionResult checkPermissionTakeVideo(Activity context,String path) {
        PermissionResult mPermissionRepuest = new TakeVideoPremissionResult(context,path);
        Handler handler = new PermissionHandler(context,path);
        AppUtils.getPermission(Manifest.permission.CAMERA,context, PermissionCode.PERMISSION_REQUEST_CAMERA_VIDEO,handler);
        return mPermissionRepuest;
    }

    public PermissionResult checkPermissionTakeVideo(Activity context,String path,int id) {
        PermissionResult mPermissionRepuest = new TakeVideoPremissionResult(context,path);
        Handler handler = new PermissionHandler(context,path,id);
        AppUtils.getPermission(Manifest.permission.CAMERA,context, PermissionCode.PERMISSION_REQUEST_CAMERA_VIDEO,handler);
        return mPermissionRepuest;
    }

    public ArrayList<Attachment> makePhotoAttachmentList() {
        ArrayList<Attachment> attachments = new ArrayList<Attachment>();
        for(int i = 0; i < AlbumItem.getInstance().adds.size() ; i++) {
            ImageItem imageItem = AlbumItem.getInstance().adds.get(i);
            imageItem.isSelected = false;
            File file = new File(imageItem.imagePath);
            Attachment attachment = new Attachment();
            attachment.mName = file.getName();
            attachment.mPath = file.getPath();
            attachment.mSize = file.length();
//            attachment.mRecordid = AppUtils.getguid();
            attachments.add(attachment);
        }
        return attachments;
    }

    public void doSelectFinish(Context mContext,String classname,String action, ArrayList<Attachment> attachments,boolean upload) {
        AlbumItem.getInstance().adds.clear();
        if(upload)
        {
            if(FileUtils.mFileUtils.mCheckPath.checkPath())
            {
                Intent mIntent = new Intent();
                mIntent.putParcelableArrayListExtra("attachments",attachments);
                mIntent.setAction(action);
                mContext.sendBroadcast(mIntent);
                Intent mIntent1 = null;
                try {
                    mIntent1 = new Intent(mContext, Class.forName(classname));
                    mIntent1.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mContext.startActivity(mIntent1);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                AppUtils.showMessage(mContext,mContext.getString(R.string.file_upload_position_error));
            }
        }
        else
        {
            Intent mIntent = new Intent();
            mIntent.putParcelableArrayListExtra("attachments",attachments);
            mIntent.setAction(action);
            mContext.sendBroadcast(mIntent);
            Intent mIntent1 = null;
            try {
                mIntent1 = new Intent(mContext, Class.forName(classname));
                mIntent1.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(mIntent1);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    public void collectFile(File file1,File file2,Context context) {
        File mFile2 = file2;
        mFile2 = checkExist(file1,mFile2,0,context);
        if(mFile2 == null)
        {
            AppUtils.showMessage(context,context.getString(R.string.file_save_exitst));
        }
        else
        {
            FileUtils.copyFile(file1.getPath(),mFile2.getPath());
            AppUtils.showMessage(context,context.getString(R.string.file_save_success));
        }
    }

    public File checkExist(File mFile, File mFile2, int count,Context context) {
        if(mFile2.exists())
        {
            if(mFile2.length() == mFile.length())
            {
                AppUtils.showMessage(context,context.getString(R.string.file_save_exitst));
                return null;
            }
            else
            {
                String name = mFile.getName().substring(0,mFile.getName().lastIndexOf("."));
                name+="("+count+")"+mFile.getName().substring(mFile.getName().lastIndexOf("."),mFile.getName().length());
                count++;
                File mFile3 = new File(pathUtils.getfilePath("/chat"+name));
                return checkExist(mFile,mFile3,count,context);
            }
        }
        else
        {
            return mFile2;
        }
    }

    public static void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }

    public static int freeSpaceOnSd() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
                .getPath());
        double sdFreeMB = ((double) stat.getAvailableBlocksLong() * (double) stat
                .getBlockSizeLong()) / 1024 / 1024;
        return (int) sdFreeMB;
    }

    public boolean hasThread(String key) {
        if(downloadAttachments.containsKey(key))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void addDownloadTask(String key,DownloadThread downloadThread) {
        downloadAttachments.put(key,downloadThread);
        Iterator map1it= FileUtils.mFileUtils.downloadAttachments.entrySet().iterator();
        if(map1it.hasNext())
        {
            mDownloadThreadHandler.removeMessages(DownloadThreadHandler.EVENT_START_DOWNLOAD_THREAD);
            mDownloadThreadHandler.sendEmptyMessageDelayed(DownloadThreadHandler.EVENT_START_DOWNLOAD_THREAD,300);
        }
    }



    public void startAttachment(Context context,Attachment attachment) {
        Intent intent = new Intent(context,AttachmentActivity.class);
        intent.putExtra("attachment",attachment);
        context.startActivity(intent);
    }

    public void startAttachmentDoc(Context context,Attachment attachment) {
        Intent intent = new Intent(context,AttachmentActivity.class);
        intent.putExtra("attachment",attachment);
        intent.putExtra("isdoc",true);
        context.startActivity(intent);
    }

    public NetObject getUploadFiles(ArrayList<Attachment> attachments)
    {
        NetObject netObject = new NetObject();
        ArrayList<File> files = new ArrayList<File>();
        String path = "";
        for(int i = 0 ; i < attachments.size() ; i++) {
            if(attachments.get(i).mRecordid.length() == 0)
            {
                files.add(new File(attachments.get(i).mPath));
            }
            else
            {
                if(path.length() > 0)
                {
                    path+=","+attachments.get(i).mRecordid;
                }
                else
                {
                    path+=attachments.get(i).mRecordid;
                }
            }
        }
        netObject.result = path;
        netObject.item = files;
        return netObject;
    }

    public void addPicView(Attachment mAttachmentModel, final Context context, String url, LinearLayout mImageLayer) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = mInflater.inflate(R.layout.fujian_long_item2, null);
        mView.setTag(mAttachmentModel);
        ImageView mImageView = (ImageView) mView.findViewById(R.id.fujian_img_big);
        TextView textView = (TextView) mView.findViewById(R.id.fujian_title);
        textView.setText(mAttachmentModel.mName);
        mView.setTag(mAttachmentModel);
        mImageView.setTag(mAttachmentModel);
        FileUtils.mFileUtils.setfileimg(mImageView, mAttachmentModel.mName);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUtils.mFileUtils.startAttachment(context, (Attachment) v.getTag());
            }
        });
        if (FileUtils.mFileUtils.getFileType(mAttachmentModel.mName) == FileUtils.FILE_TYPE_PICTURE) {
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.plugin_camera_no_pictures);
            Glide.with(context).load(url).apply(options).into(new MySimpleTarget(mImageView));
        }
        mImageLayer.addView(mView);
    }

    public void addPicView(final Attachment mAttachmentModel, final Context context, String url, LinearLayout mImageLayer, View.OnClickListener mDeletePicListener) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = mInflater.inflate(R.layout.fujian_long_item, null);
        ImageView mImageView = (ImageView) mView.findViewById(R.id.fujian_img_big);
        TextView textView = (TextView) mView.findViewById(R.id.fujian_title);
        textView.setText(mAttachmentModel.mName);
        Button close = (Button) mView.findViewById(R.id.close_image_b);
        close.setTag(mAttachmentModel);
        close.setOnClickListener(mDeletePicListener);
        mView.setTag(mAttachmentModel);
        FileUtils.mFileUtils.setfileimg(mImageView,mAttachmentModel.mName);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUtils.mFileUtils.startAttachment(context, (Attachment) v.getTag());
            }
        });
        if(FileUtils.mFileUtils.getFileType(mAttachmentModel.mName) == FileUtils.FILE_TYPE_PICTURE)
        {
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.plugin_camera_no_pictures);
            if(url.length() > 0)
                Glide.with(context).load(url).apply(options).into(new MySimpleTarget(mImageView));
            else
                Glide.with(context).load(new File(mAttachmentModel.mPath)).apply(options).into(new MySimpleTarget(mImageView));
        }
        mImageLayer.addView(mView);

    }

    public void addPicView2(final Attachment mAttachmentModel, final Context context, String url, MyLinearLayout mImageLayer, View.OnClickListener closeListener) {

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = null;
        File mfile = new File(mAttachmentModel.mPath);
        mView = mInflater.inflate(R.layout.fujian_image2, null);
        mView.setTag(mAttachmentModel);
        ImageView mImageView = (ImageView) mView.findViewById(R.id.fujian_img_big);
        ImageView closebtn = (ImageView) mView.findViewById(R.id.close);
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.plugin_camera_no_pictures);
//        options.override((int) (metric.density));
        Glide.with(context).load(mfile).apply(options).into(new MySimpleTarget(mImageView));
        if(closeListener != null)
        {
            closebtn.setVisibility(View.VISIBLE);
            closebtn.setOnClickListener(closeListener);
            closebtn.setTag(mView);
        }
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUtils.mFileUtils.startAttachment(context, (Attachment) v.getTag());
            }
        });
        mImageLayer.addView(mView, mImageLayer.getChildCount() - 1);
    }

    public void addPicView2(final Attachment mAttachmentModel, final Context context, String url, MyLinearLayout mImageLayer, ArrayList<Attachment> attachments,View.OnClickListener clickListener) {

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = null;
        File mfile = new File(mAttachmentModel.mPath);
        mView = mInflater.inflate(R.layout.fujian_image, null);
        mView.setTag(mAttachmentModel);
        ImageView mImageView = (ImageView) mView.findViewById(R.id.fujian_img_big);
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.plugin_camera_no_pictures);
//        options.override((int) (metric.density));
        Glide.with(context).load(mfile).apply(options).into(new MySimpleTarget(mImageView));
        mView.setOnClickListener(clickListener);
        mImageLayer.addView(mView, mImageLayer.getChildCount() - 1);
    }

    public static class DeletePicListener implements DialogInterface.OnClickListener {

        public Attachment attachment;
        public ArrayList<Attachment> attachments;
        public LinearLayout mImageLayer;
        public DeletePicListener(Attachment attachment,ArrayList<Attachment> attachments,LinearLayout mImageLayer) {
            this.attachment = attachment;
            this.attachments = attachments;
            this.mImageLayer = mImageLayer;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            int pos = attachments.indexOf(attachment);
            attachments.remove(attachment);
            mImageLayer.removeViewAt(pos);
        }
    }

    public void showSeriasPic(Context context ,ArrayList<Attachment> attachments , Attachment attachment,boolean delete,boolean local,String action) {
        int index = attachments.indexOf(attachment);
        Intent intent = new Intent(context, BigImageViewActivity.class);
        intent.setAction(action);
        intent.putExtra("delete",delete);
        intent.putExtra("islocal",local);
        intent.putExtra("index",index);
        intent.putParcelableArrayListExtra("attachments",attachments);
        context.startActivity(intent);
    }

    public void showBigImage(File file) {
        Attachment attachment = new Attachment("",file.getName(),file.getPath(),"",file.length(),file.length(),"");
        Intent intent = new Intent(context, BigImageViewActivity.class);
        ArrayList<Attachment> attachments = new ArrayList<Attachment>();
        attachments.add(attachment);
        intent.putExtra("islocal",false);
        intent.putExtra("delete",false);
        intent.putExtra("mode",1);
        intent.putParcelableArrayListExtra("attachments",attachments);
        context.startActivity(intent);
    }


    public void selectPic(Context context, boolean showcamera, int select,Action<ArrayList<AlbumFile>> result)
    {
        Album.image(context).multipleChoice().camera(showcamera).columnCount(4).selectCount(select).onResult(result).start();
    }

    public void cropPhoto(Activity context,float ratx,float raty,String inputpath,String outputPath,int cropwidth,int cropHeight,int requestcode)
    {
        Durban.with(context)
                .statusBarColor(Color.rgb(0,0,0))
                .toolBarColor(Color.rgb(0,0,0))
                .navigationBarColor(Color.rgb(0,0,0))
                // Image path list/array.
                .inputImagePaths(inputpath)
                // Image output directory.
                .outputDirectory(outputPath)
                // Image size limit.
                .maxWidthHeight(cropwidth, cropHeight)
                // Aspect ratio.
                .aspectRatio(ratx, raty)
                // Output format: JPEG, PNG.
                .compressFormat(Durban.COMPRESS_JPEG)
                // Compress quality, see Bitmap#compress(Bitmap.CompressFormat, int, OutputStream)
                .compressQuality(90)
                // Gesture: ROTATE, SCALE, ALL, NONE.
                .gesture(Durban.GESTURE_ALL)
                .controller(Controller.newBuilder()
                        .enable(false)
                        .rotation(true)
                        .rotationTitle(true)
                        .scale(true)
                        .scaleTitle(true)
                        .build())
                .requestCode(requestcode)
                .start();
    }

    public ArrayList<String> getImage(Intent data)
    {
        ArrayList<String> images = Durban.parseResult(data);
        return images;
    }

    public static void delFile(String path) {
        File file = new File(path);
        if(file.exists())
        {
            File[] files = file.listFiles();
            if(files != null)
            {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isFile() == false){
                        delFile(files[i].getPath());
                    }
                }
            }
            file.delete();
        }
    }

    public static void delFile2(String path) {
        File file = new File(path);
        if(file.exists())
        {
            File[] files = file.listFiles();
            if(files != null)
            {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isFile()){
                        files[i].delete();
                    }
                    else
                    {
                        delFile(files[i].getPath());
                    }
                }
            }
        }
    }

    public static String getFileContent(File file) {
        String content = "";
        if (!file.isDirectory()) {  //检查此路径名的文件是否是一个目录(文件夹)
            if (file.getName().endsWith("txt")) {//文件格式为""文件
                try {
                    InputStream instream = new FileInputStream(file);
                    if (instream != null) {
                        InputStreamReader inputreader
                                = new InputStreamReader(instream, "UTF-8");
                        BufferedReader buffreader = new BufferedReader(inputreader);
                        String line = "";
                        //分行读取
                        while ((line = buffreader.readLine()) != null) {
                            content += line + "\n";
                        }
                        instream.close();//关闭输入流
                    }
                } catch (java.io.FileNotFoundException e) {
                    Log.d("TestFile", "The File doesn't not exist.");
                } catch (IOException e) {
                    Log.d("TestFile", e.getMessage());
                }
            }
        }
        return content;
    }


    public void writeTxtToFile(String strcontent, String filePath, String fileName) {
        //生成文件夹之后，再生成文件，不然会出错
        makeFilePath(filePath, fileName);

        String strFilePath = filePath + fileName;
        // 每次写入时，都换行写
        String strContent = strcontent + "\r\n";
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }
    private File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

//生成文件夹

    private static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            Log.i("error:", e + "");
        }
    }

    public interface FileOperation{

        public void share(BaseActivity context, String title, String path);
        public void sendDocument(Context context,String path);
        public void sendChat(Context context,String path);
        public void sendMail(Context context,String path);
    }

}