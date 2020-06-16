package intersky.filetools.handler;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.util.Iterator;
import java.util.Map;

import intersky.filetools.FileUtils;
import intersky.filetools.thread.DownloadThread;

//02
public class DownloadThreadHandler extends Handler {

    public static final int EVENT_START_DOWNLOAD_THREAD = 3030200;

    public FileUtils fileUtils;

    public DownloadThreadHandler(FileUtils fileUtils) {
        this.fileUtils = fileUtils;
    }

    @Override
    public void handleMessage(Message msg) {
        Intent intent = new Intent();
        switch (msg.what) {
            case EVENT_START_DOWNLOAD_THREAD:
                Iterator map1it= FileUtils.mFileUtils.downloadAttachments.entrySet().iterator();
                if(FileUtils.mFileUtils.mDownloadThread == null) {
                    if(map1it.hasNext())
                    {
                        Map.Entry<String, DownloadThread> entry=(Map.Entry<String, DownloadThread>) map1it.next();
                        FileUtils.mFileUtils.mDownloadThread = entry.getValue();
                        FileUtils.mFileUtils.mDownloadThread.start();
                        fileUtils.mDownloadThreadHandler.removeMessages(DownloadThreadHandler.EVENT_START_DOWNLOAD_THREAD);
                        fileUtils.mDownloadThreadHandler.sendEmptyMessageAtTime(DownloadThreadHandler.EVENT_START_DOWNLOAD_THREAD,300);
                        break;
                    }
                }
                else {
                    if(FileUtils.mFileUtils.mDownloadThread.state == DownloadThread.STATE_NOMAL)
                    {
                        fileUtils.mDownloadThreadHandler.removeMessages(DownloadThreadHandler.EVENT_START_DOWNLOAD_THREAD);
                        fileUtils.mDownloadThreadHandler.sendEmptyMessageAtTime(DownloadThreadHandler.EVENT_START_DOWNLOAD_THREAD,300);
                        break;
                    }
                    else if(FileUtils.mFileUtils.mDownloadThread.state == DownloadThread.STATE_END)
                    {
                        FileUtils.mFileUtils.downloadAttachments.remove(FileUtils.mFileUtils.mDownloadThread.mAttachment.mUrl);
                        FileUtils.mFileUtils.mDownloadThread = null;
                        fileUtils.mDownloadThreadHandler.removeMessages(DownloadThreadHandler.EVENT_START_DOWNLOAD_THREAD);
                        fileUtils.mDownloadThreadHandler.sendEmptyMessageAtTime(DownloadThreadHandler.EVENT_START_DOWNLOAD_THREAD,300);
                        break;
                    }
                    else
                    {
//                        if(map1it.hasNext())
//                        {
//                            Map.Entry<String, DownloadThread> entry=(Map.Entry<String, DownloadThread>) map1it.next();
//                            FileUtils.mFileUtils.downloadAttachments.remove(FileUtils.mFileUtils.mDownloadThread);
//                            FileUtils.mFileUtils.mDownloadThread = entry.getValue();
//                            FileUtils.mFileUtils.mDownloadThread.start();
//                            fileUtils.mDownloadThreadHandler.removeMessages(DownloadThreadHandler.EVENT_START_DOWNLOAD_THREAD);
//                            fileUtils.mDownloadThreadHandler.sendEmptyMessageAtTime(DownloadThreadHandler.EVENT_START_DOWNLOAD_THREAD,300);
//                            break;
//                        }
                        fileUtils.mDownloadThreadHandler.removeMessages(DownloadThreadHandler.EVENT_START_DOWNLOAD_THREAD);
                        fileUtils.mDownloadThreadHandler.sendEmptyMessageAtTime(DownloadThreadHandler.EVENT_START_DOWNLOAD_THREAD,300);
                        break;
                    }

                }
                break;
        }

    }

}
