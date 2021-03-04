package intersky.filetools.presenter;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import intersky.appbase.Presenter;
import intersky.appbase.entity.Attachment;
import intersky.apputils.AppUtils;
import intersky.filetools.FileUtils;
import intersky.filetools.R;
import intersky.filetools.entity.LocalDocument;
import intersky.filetools.thread.DownloadThread;
import intersky.filetools.view.activity.SaveAttachmentActivity;
import intersky.filetools.view.adapter.DocumentAdapter;
import xpx.com.toolbar.utils.ToolBarHelper;

public class SaveAttachmentPresenter implements Presenter {

    public static final String PATH_INFO = "path_info";
    public SaveAttachmentActivity mSaveAttachmentActivity;

    public SaveAttachmentPresenter(SaveAttachmentActivity mSaveAttachmentActivity) {
        this.mSaveAttachmentActivity = mSaveAttachmentActivity;
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        mSaveAttachmentActivity.setContentView(R.layout.activity_mail_fujian_save);
        RelativeLayout mLinearLayout = (RelativeLayout) mSaveAttachmentActivity.findViewById(R.id.activity_mail_fujian_save);
        ImageView back = mSaveAttachmentActivity.findViewById(R.id.back);
        back.setOnClickListener(mSaveAttachmentActivity.mBackListener);
        TextView save = mSaveAttachmentActivity.findViewById(R.id.save);
        save.setOnClickListener(mSaveAttachmentActivity.mSaveListener);

        mSaveAttachmentActivity.path = mSaveAttachmentActivity.getIntent().getStringExtra("path");
        mSaveAttachmentActivity.mListView = (ListView) mSaveAttachmentActivity.findViewById(R.id.file_List);
        mSaveAttachmentActivity.mListView.setOnItemClickListener(mSaveAttachmentActivity.mOnItemClickListener);
    }

    private void initData() {

        mSaveAttachmentActivity.mDocumentItems.clear();
        SharedPreferences sharedPre = mSaveAttachmentActivity.getSharedPreferences(PATH_INFO, 0);
        mSaveAttachmentActivity.nowPath = sharedPre.getString("path", Environment.getExternalStorageDirectory().getPath());
        //nowPath = getSDPath();
        File mfile = new File(mSaveAttachmentActivity.nowPath);
        if (mSaveAttachmentActivity.nowPath.equals(Environment.getExternalStorageDirectory().getPath()) == false) {
            mSaveAttachmentActivity.mDocumentItems.add(new LocalDocument(0,
                    mSaveAttachmentActivity.nowPath, mfile.getName(), mfile.getParent()));
        }
        getFileList(mfile, mSaveAttachmentActivity.mDocumentItems);

        mSaveAttachmentActivity.mDocumentAdapter = new DocumentAdapter(mSaveAttachmentActivity, mSaveAttachmentActivity.mDocumentItems);
        mSaveAttachmentActivity.mListView.setAdapter(mSaveAttachmentActivity.mDocumentAdapter);
    }

    @Override
    public void Create() {
        // TODO Auto-generated method stub
        initView();
        initData();
    }

    @Override
    public void Start() {
        // TODO Auto-generated method stub

    }

    @Override
    public void Resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void Pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void Destroy() {
        // TODO Auto-generated method stub

    }

    private void getFileList(File path, ArrayList<LocalDocument> mDocumentItems) {

        // 如果是文件夹的话
        if (path.isDirectory()) {
            // 返回文件夹中有的数据
            File[] files = path.listFiles();
            // 先判断下有没有权限，如果没有权限的话，就不执行了
            if (null == files)
                return;

            for (int i = 0; i < files.length; i++) {
                // getFileList(files[i], fileList);
                if (files[i].isDirectory()) {
                    mDocumentItems.add(new LocalDocument(FileUtils.FAIL_TYPE_DOCUMEN, files[i].getPath(),
                            files[i].getName(), files[i].getParent()));
                }
            }
        }
        if(mDocumentItems.size() > 0)
        Collections.sort(mDocumentItems, new SortComparator2());
    }
    public class SortComparator2 implements Comparator {

        @Override
        public int compare(Object mMailPersonItem1, Object mMailPersonItem2) {
            // TODO Auto-generated method stub
            String[] array = new String[2];

            array[0] = ((LocalDocument) mMailPersonItem1).mName + " " + ((LocalDocument) mMailPersonItem1).mName;
            array[1] = ((LocalDocument) mMailPersonItem2).mName + " " + ((LocalDocument) mMailPersonItem2).mName;
            if (array[0].equals(array[1])) {
                return 0;
            }
            Arrays.sort(array);
            if (array[0].equals(((LocalDocument) mMailPersonItem1).mName + " " + ((LocalDocument) mMailPersonItem1).mName)) {
                return -1;
            } else if (array[0].equals(((LocalDocument) mMailPersonItem2).mName + " " + ((LocalDocument) mMailPersonItem2).mName)) {
                return 1;
            }
            return 0;
        }
    }

    public void dosave() {
        File oldPath = new File(mSaveAttachmentActivity.path);
        File newPath = new File(mSaveAttachmentActivity.nowPath + "/" + oldPath.getName());
        int bytesum = 0;
        int byteread = 0;

        if (newPath.exists()) {
            newPath.delete();
        }

        if (oldPath.exists()) {

            try {
                InputStream inStream;
                inStream = new FileInputStream(oldPath);
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[4096];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; // 字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } // 读入原文件
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            mSaveAttachmentActivity.finish();
            AppUtils.showMessage(mSaveAttachmentActivity, mSaveAttachmentActivity.getString(R.string.files_saveto) + mSaveAttachmentActivity.nowPath);
        } else {
            mSaveAttachmentActivity.finish();
        }
    }

    public void doDownload() {
        if (FileUtils.freeSpaceOnSd() < 100) {
            AppUtils.showMessage(mSaveAttachmentActivity, mSaveAttachmentActivity.getString(R.string.attachment_outcoche));
        }
        else
        {
            File oldPath = new File(mSaveAttachmentActivity.path);
            File newPath = new File(mSaveAttachmentActivity.nowPath + "/" + oldPath.getName());
            DownloadThread mDownloadTask = FileUtils.mFileUtils.downloadAttachments.get(newPath.getPath());
            if(mDownloadTask == null)
            {
                File file = new File(mSaveAttachmentActivity.getIntent().getStringExtra("path"));
                file.delete();
                Attachment attachment = new Attachment();
                attachment.mUrl = mSaveAttachmentActivity.getIntent().getStringExtra("url");
                attachment.mPath = newPath.getPath();
                attachment.mName = file.getName();
                mDownloadTask = new DownloadThread(
                        attachment,FileUtils.ATTACHMENT_SAVE_NET_FILE_FAIL,"0",FileUtils.ATTACHMENT_SAVE_NET_FILE_SUCCESS,null);
                FileUtils.mFileUtils.downloadAttachments.put(attachment.mPath,mDownloadTask);
                mDownloadTask.start();
                AppUtils.showMessage(mSaveAttachmentActivity,mSaveAttachmentActivity.getString(R.string.downloadstart));
                mSaveAttachmentActivity.finish();
            }
            else
            {
                AppUtils.showMessage(mSaveAttachmentActivity,mSaveAttachmentActivity.getString(R.string.file_exist));
            }

        }
    }

    private void savePath(File path) {
        SharedPreferences sharedPre = mSaveAttachmentActivity.getSharedPreferences(PATH_INFO, 0);
        Editor editor = sharedPre.edit();
        editor.putString("path", path.getPath());
        editor.commit();

    }

    public void clickItem(int position) {
        LocalDocument mDocumentItem = mSaveAttachmentActivity.mDocumentItems.get(position);
        File mFile = new File(mDocumentItem.mPath);
        File mfila1;
        switch (mDocumentItem.mType) {
            case 0:
                mSaveAttachmentActivity.mDocumentItems.clear();
                mfila1 = new File(mDocumentItem.mParentPath);
                if (mfila1.getPath().equals(Environment.getExternalStorageDirectory().getPath()) == false) {
                    mSaveAttachmentActivity.mDocumentItems.add(new LocalDocument(0,
                            mfila1.getPath(), mfila1.getName(), mfila1.getParent()));
                }

                getFileList(mfila1, mSaveAttachmentActivity.mDocumentItems);
                savePath(mfila1);
                mSaveAttachmentActivity.nowPath = mfila1.getPath();
                mSaveAttachmentActivity.mDocumentAdapter.notifyDataSetChanged();
                break;
            case FileUtils.FAIL_TYPE_DOCUMEN:
                mSaveAttachmentActivity.mDocumentItems.clear();
                mfila1 = new File(mDocumentItem.mPath);
                mSaveAttachmentActivity.mDocumentItems.add(new LocalDocument(0,
                        mSaveAttachmentActivity.nowPath, mfila1.getName(), mfila1.getParent()));
                mSaveAttachmentActivity.nowPath = mfila1.getPath();
                getFileList(mFile, mSaveAttachmentActivity.mDocumentItems);
                savePath(new File(mDocumentItem.mPath));
                mSaveAttachmentActivity.mDocumentAdapter.notifyDataSetChanged();
                break;
        }
    }

}
