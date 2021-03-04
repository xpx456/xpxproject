package intersky.filetools.bus;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.util.ArrayList;

import intersky.appbase.bus.BusObject;
import intersky.appbase.entity.Attachment;
import intersky.appbase.entity.CheckPath;
import intersky.filetools.FileUtils;
import intersky.filetools.thread.DownloadThread;
import intersky.mywidget.MyLinearLayout;

/**
 * @author Zhenhua on 2017/12/8.
 * @email zhshan@ctrip.com ^.^
 */

public class FileBusObject extends BusObject {
    public FileBusObject(String host) {
        super(host);
    }

    @Override
    public Object doDataJob(Context context, String bizName, Object... var3) {
        if (TextUtils.equals(bizName, "filetools/getfilePath")) {
            return FileUtils.mFileUtils.pathUtils.getfilePath((String) var3[0]);
        }
        else if(TextUtils.equals(bizName, "filetools/collectFile")) {
            FileUtils.mFileUtils.collectFile((File) var3[0],(File) var3[1],context);
            return null;
        }
        else if(TextUtils.equals(bizName, "filetools/getPhotos")) {
            FileUtils.mFileUtils.getPhotos(context,(boolean)var3[0],(int)var3[1],(String)var3[2],(String)var3[3]);
            return null;
        }
        else if(TextUtils.equals(bizName, "filetools/getVideos")) {
            FileUtils.mFileUtils.getVideos(context,(boolean)var3[0],(String)var3[2],(String)var3[3]);
            return null;
        }
        else if(TextUtils.equals(bizName, "filetools/getPhotosUpload")) {
            FileUtils.mFileUtils.getPhotos(context,(boolean)var3[0],(int)var3[1],(String)var3[2],(String)var3[3],(View.OnClickListener)var3[4],(String)var3[5],(CheckPath)var3[6]);
            return null;
        }
        else if(TextUtils.equals(bizName, "filetools/getVideosUpload")) {
            FileUtils.mFileUtils.getVideos(context,(boolean)var3[0],(String)var3[1],(String)var3[2],(View.OnClickListener)var3[3],(String)var3[4],(CheckPath)var3[5]);
            return null;
        }
        else if(TextUtils.equals(bizName, "filetools/getFilesUpload")) {
            FileUtils.mFileUtils.getFiles(context,(String)var3[0],(String)var3[1],(View.OnClickListener)var3[2],(String)var3[3],(CheckPath)var3[4]);
            return null;
        }
        else if(TextUtils.equals(bizName, "filetools/checkPermissionTakePhoto")) {
            return FileUtils.mFileUtils.checkPermissionTakePhoto((Activity) var3[0],(String) var3[1]);
        }
        else if(TextUtils.equals(bizName, "filetools/takePhotoUri")) {
            File file = new File(FileUtils.mFileUtils.takePhotoUri.getPath());
            if(file.exists())
            {
                return file.getPath();
            }
            else
            {
                return FileUtils.mFileUtils.takePhotoPath;
            }
        }
        else if(TextUtils.equals(bizName, "filetools/openfile")) {
            return FileUtils.mFileUtils.openfile((File) var3[0]);
        }
        else if(TextUtils.equals(bizName, "filetools/getImageFileIntent")) {
            return FileUtils.mFileUtils.getImageFileIntent((File) var3[0]);
        }
        else if(TextUtils.equals(bizName, "filetools/downloadthread")) {
            return new DownloadThread((Attachment)var3[0],(String)var3[1],(String)var3[2],(String)var3[3],(Parcelable)var3[4]);
        }
        else if(TextUtils.equals(bizName, "filetools/freeSpaceOnSd")) {
            return FileUtils.mFileUtils.freeSpaceOnSd();
        }
        else if(TextUtils.equals(bizName, "filetools/hasThread")) {
            return FileUtils.mFileUtils.hasThread((String) var3[0]);
        }
        else if(TextUtils.equals(bizName, "filetools/addDownloadTask")) {
            FileUtils.mFileUtils.addDownloadTask((String)var3[0],(DownloadThread)var3[1]);
            return null;
        }
        else if(TextUtils.equals(bizName, "filetools/showBigImage")) {
            FileUtils.mFileUtils.showBigImage((File)var3[0]);
            return null;
        }
        else if(TextUtils.equals(bizName, "filetools/getFileType")) {
            return FileUtils.mFileUtils.getFileType((String) var3[0]);
        }
        else if(TextUtils.equals(bizName, "filetools/startAttachment")) {
            FileUtils.mFileUtils.startAttachment(context,(Attachment) var3[0]);
            return null;
        }
        else if(TextUtils.equals(bizName, "filetools/startAttachmentDoc")) {
            FileUtils.mFileUtils.startAttachmentDoc(context,(Attachment) var3[0]);
            return null;
        }
        else if(TextUtils.equals(bizName, "filetools/addPicView")) {
            FileUtils.mFileUtils.addPicView((Attachment) var3[0],context,(String)var3[1],(LinearLayout)var3[2]);
            return null;
        }
        else if(TextUtils.equals(bizName, "filetools/addPicViewListener")) {
            FileUtils.mFileUtils.addPicView((Attachment) var3[0],context,(String)var3[1],(LinearLayout)var3[2],(View.OnClickListener)var3[3]);
            return null;
        }
        else if(TextUtils.equals(bizName, "filetools/addPicView2")) {
            FileUtils.mFileUtils.addPicView2((Attachment) var3[0],context,(String)var3[1],(MyLinearLayout)var3[2],(View.OnClickListener) var3[3]);
            return null;
        }
        else if(TextUtils.equals(bizName, "filetools/addPicView3")) {
            FileUtils.mFileUtils.addPicView3((Attachment) var3[0],context,(String)var3[1],(MyLinearLayout)var3[2],(View.OnClickListener) var3[3]);
            return null;
        }
        else if(TextUtils.equals(bizName, "filetools/getUploadFiles")) {

            return FileUtils.mFileUtils.getUploadFiles((ArrayList<Attachment>) var3[0]);
        }
        else if(TextUtils.equals(bizName, "filetools/DeletePicListener")) {

            return new FileUtils.DeletePicListener((Attachment)var3[0],(ArrayList<Attachment>) var3[1],(LinearLayout)var3[2]);
        }
        else if(TextUtils.equals(bizName, "filetools/showSeriasPic")) {
            FileUtils.mFileUtils.showSeriasPic(context,(ArrayList<Attachment>) var3[0],(Attachment)var3[1],(boolean)var3[2],(boolean)var3[3],(String)var3[4]);
            return null;
        }
        else if(TextUtils.equals(bizName, "filetools/setfileimg")) {
            FileUtils.mFileUtils.setfileimg((ImageView)var3[0],(String)var3[1]);
            return null;
        }
        else if(TextUtils.equals(bizName, "filetools/setfileimgfinish")) {
            FileUtils.mFileUtils.setfileimgfinish((ImageView)var3[0],(String)var3[1]);
            return null;
        }
        else if(TextUtils.equals(bizName, "filetools/savelocal")) {
            FileUtils.mFileUtils.saveLocal(context,(File) var3[0]);
            return null;
        }
        else if(TextUtils.equals(bizName, "filetools/savenet")) {
            FileUtils.mFileUtils.saveNet(context,(File) var3[0],(String)var3[1]);
            return null;
        }
        return null;
    }
}
