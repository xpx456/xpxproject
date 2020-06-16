package intersky.filetools.presenter;

import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import intersky.appbase.Presenter;
import intersky.appbase.entity.Attachment;
import intersky.filetools.FileUtils;
import intersky.filetools.R;
import intersky.filetools.entity.LocalDocument;
import intersky.filetools.view.activity.AllFileActivity;
import intersky.filetools.view.adapter.DocumentAdapter;
import intersky.filetools.view.adapter.LocalPathAdapter;
import intersky.mywidget.HorizontalListView;
import xpx.com.toolbar.utils.ToolBarHelper;

public class AllFilePresenter implements Presenter {

    private AllFileActivity mAllFileActivity;

    public AllFilePresenter(AllFileActivity mAllFileActivity) {
        this.mAllFileActivity = mAllFileActivity;
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        mAllFileActivity.setContentView(R.layout.activity_all_file);
        ToolBarHelper.setTitle(mAllFileActivity.mActionBar, FileUtils.mFileUtils.localPaths.get(FileUtils.mFileUtils.localPaths.size()-1).mName);
        ToolBarHelper.setBackListenr(mAllFileActivity.mActionBar, mAllFileActivity.mBackListener);
        ToolBarHelper.setRightBtnText(mAllFileActivity.mActionBar, mAllFileActivity.mSelectAllListener, mAllFileActivity.getString(R.string.button_word_selectall));
        mAllFileActivity.mPathList = (HorizontalListView) mAllFileActivity.findViewById(R.id.horizon_listview);
        mAllFileActivity.mListView = (ListView) mAllFileActivity.findViewById(R.id.file_List);
        mAllFileActivity.mFilePath = (TextView) mAllFileActivity.findViewById(R.id.path_title);
        mAllFileActivity.listlayer = (RelativeLayout) mAllFileActivity.findViewById(R.id.listlayer);
        mAllFileActivity.layer = (RelativeLayout) mAllFileActivity.findViewById(R.id.allfilebuttomimf);
        mAllFileActivity.mbtnright2 = (TextView) mAllFileActivity.findViewById(R.id.save2);
        mAllFileActivity.mBtnRight = (TextView) mAllFileActivity.findViewById(R.id.btnright);
        mAllFileActivity.layer2 = (RelativeLayout) mAllFileActivity.findViewById(R.id.btnsave2);
        mAllFileActivity.mBtnleft = (TextView) mAllFileActivity.findViewById(R.id.btnleft);
        mAllFileActivity.mbtnright2.setOnClickListener(mAllFileActivity.mOkListener);
        mAllFileActivity.mbtnright2.setOnClickListener(mAllFileActivity.mOkListener);

        if(mAllFileActivity.getIntent().getIntExtra("type",FileUtils.SELECT_TYPE_NOMAL) == FileUtils.SELECT_TYPE_DOCUMENTMANAGER)
        {
            mAllFileActivity.layer2.setVisibility(View.INVISIBLE);
            mAllFileActivity.layer.setVisibility(View.VISIBLE);
            mAllFileActivity.mBtnleft.setOnClickListener(FileUtils.mFileUtils.setPositionListener);
        }
        else
        {
            mAllFileActivity.layer2.setVisibility(View.VISIBLE);
            mAllFileActivity.layer.setVisibility(View.INVISIBLE);
        }
        mAllFileActivity.mListView.setOnItemClickListener(mAllFileActivity.mOnItemClickListener);
        mAllFileActivity.mPathList.setOnItemClickListener(mAllFileActivity.mPathItemClick);
        mAllFileActivity.mBtnRight.setOnClickListener(mAllFileActivity.mOkListener);
    }


    private void initData() {
        mAllFileActivity.mDocumentItems.clear();
        getFileList(new File(FileUtils.mFileUtils.localPaths.get(FileUtils.mFileUtils.localPaths.size()-1).mPath), mAllFileActivity.mDocumentItems);
        mAllFileActivity.mDocumentAdapter = new DocumentAdapter(mAllFileActivity, mAllFileActivity.mDocumentItems);
        mAllFileActivity.mSelectFoladerListAdapter = new LocalPathAdapter(mAllFileActivity,FileUtils.mFileUtils.localPaths);
        mAllFileActivity.mListView.setAdapter(mAllFileActivity.mDocumentAdapter);
        mAllFileActivity.mPathList.setAdapter(mAllFileActivity.mSelectFoladerListAdapter);
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
                } else {
                    mDocumentItems.add(new LocalDocument(FileUtils.mFileUtils.getFileType(files[i].getName()),
                            files[i].getPath(), files[i].getName(), files[i].getParent()));
                }
            }
        }
    }

    public void dofinish() {
        mAllFileActivity.finish();
    }

    public void doBack() {
        if (FileUtils.mFileUtils.localPaths.size() == 1) {
            dofinish();
        } else {
            mAllFileActivity.mDocumentItems.clear();
            FileUtils.mFileUtils.localPaths.remove(FileUtils.mFileUtils.localPaths.size()-1);
            getFileList(new File( FileUtils.mFileUtils.localPaths.get( FileUtils.mFileUtils.localPaths.size()-1).mPath), mAllFileActivity.mDocumentItems);
            mAllFileActivity.mDocumentAdapter.notifyDataSetChanged();
        }
    }

    public void selectAll() {
        for (int i = 0; i < mAllFileActivity.mDocumentItems.size(); i++) {
            if(mAllFileActivity.mDocumentItems.get(i).mType != FileUtils.FAIL_TYPE_DOCUMEN)
            {
                if (mAllFileActivity.mDocumentItems.get(i).isSelect == false) {
                    mAllFileActivity.mDocumentItems.get(i).isSelect = true;
                }
            }
        }
        mAllFileActivity.mDocumentAdapter.notifyDataSetChanged();
    }



    public void doItemClick(LocalDocument mDocumentItem) {
        if (mDocumentItem.mType == FileUtils.FAIL_TYPE_DOCUMEN) {
            mAllFileActivity.mDocumentItems.clear();
            getFileList(new File(mDocumentItem.mPath), mAllFileActivity.mDocumentItems);
            FileUtils.mFileUtils.localPaths.add(mDocumentItem);
            mAllFileActivity.mSelectFoladerListAdapter.notifyDataSetChanged();
            mAllFileActivity.mDocumentAdapter.notifyDataSetChanged();
        } else {
            if (mDocumentItem.isSelect == false) {

                mDocumentItem.isSelect = true;
            } else {
                mDocumentItem.isSelect = false;
            }
            mAllFileActivity.mDocumentAdapter.notifyDataSetChanged();
        }
    }

    public void doPathItemClick(LocalDocument mPathItem) {
        mAllFileActivity.mDocumentItems.clear();
        getFileList(new File(mPathItem.mPath), mAllFileActivity.mDocumentItems);
        int index = FileUtils.mFileUtils.localPaths.indexOf(mPathItem);
        for(int i = index+1 ; i <  FileUtils.mFileUtils.localPaths.size() ; i++) {
            FileUtils.mFileUtils.localPaths.remove(i);
            i--;
        }
        mAllFileActivity.mSelectFoladerListAdapter.notifyDataSetChanged();
        mAllFileActivity.mDocumentAdapter.notifyDataSetChanged();
    }

    public void addMailAttachmenFinish() {
        ArrayList<Attachment> attachments = new ArrayList<Attachment>();
        for (int i = 0; i < mAllFileActivity.mDocumentItems.size(); i++) {
            if (mAllFileActivity.mDocumentItems.get(i).isSelect) {
                if(mAllFileActivity.mDocumentItems.get(i).mType != FileUtils.FAIL_TYPE_DOCUMEN)
                {
                    File mFile = new File( mAllFileActivity.mDocumentItems.get(i).mPath);
                    attachments.add(new Attachment("", mFile.getName(), mAllFileActivity.mDocumentItems.get(i).mPath, "", mFile.length(), mFile.length(), ""));
                }
            }
        }
        if(mAllFileActivity.layer.getVisibility() == View.VISIBLE)
        {
            FileUtils.mFileUtils.doSelectFinish(mAllFileActivity, mAllFileActivity.getIntent().getStringExtra("class"), mAllFileActivity.getIntent().getAction(), attachments,true);
        }
        else
        {
            FileUtils.mFileUtils.doSelectFinish(mAllFileActivity, mAllFileActivity.getIntent().getStringExtra("class"), mAllFileActivity.getIntent().getAction(), attachments,false);
        }

    }


}
