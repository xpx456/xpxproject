package intersky.document.view.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import intersky.document.presenter.DocumentPresenter;
import intersky.appbase.BaseActivity;
import intersky.appbase.FragmentTabAdapter;

/**
 * Created by xpx on 2017/8/18.
 */

public class DocumentActivity extends BaseActivity {

    public LinearLayout buttomLayer;
    public LinearLayout buttomLayern;
    public LinearLayout buttomLayerd;
    public ImageView mImgYunpan;
    public ImageView mImgDetial;
    public TextView mTxtYunpan;
    public TextView mTxtDetial;
    public RelativeLayout mBtnYunpan;
    public RelativeLayout mBtnDetial;
    public LinearLayout mlayoutHead;
    public RelativeLayout mPicture;
    public RelativeLayout mVideo;
    public RelativeLayout mAllFile;
    public RelativeLayout mBtnDown;
    public RelativeLayout mBtnDelete;
    public RelativeLayout mBtnCancle;
    public RelativeLayout mBtnMore;
    public RelativeLayout mBtnDownDelete;
    public RelativeLayout mBtnDownCancle;
    public RelativeLayout mShade;
    public RelativeLayout mOther;
    public Animation showActionUp;
    public Animation hideActionUp;
    public Animation showActionDown;
    public Animation hideActionDown;
    public RelativeLayout mmenu;
    public RelativeLayout mReneme;
    public ImageView mrenemeicon;
    public TextView renametext;
    public List<Fragment> mFragments = new ArrayList<Fragment>();
    public int tebId = 0;
    public FragmentTabAdapter tabAdapter;

    public DocumentPresenter mDocumentPresenter = new DocumentPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDocumentPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mDocumentPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener mBackListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            mDocumentPresenter.doBack();
        }
    };

    public View.OnClickListener mSelectAllListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mDocumentPresenter.doSelectAll();
        }
    };

    public View.OnClickListener mSelectUpLoadListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mDocumentPresenter.showUpload();
        }
    };

    public View.OnClickListener mCancleUpLoadListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mDocumentPresenter.hideUpload();
        }
    };

    public View.OnClickListener mYunpanListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub

            mDocumentPresenter.showYunpan();
        }
    };


    public View.OnClickListener mDetialListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub

            mDocumentPresenter.showDetial();

        }
    };

    public View.OnClickListener mAddDowanloadListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mDocumentPresenter.addDownload();
            mDocumentPresenter.hidEdit();
        }
    };

    public View.OnClickListener mDeleteDocumentListener = new View.OnClickListener() {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mDocumentPresenter.deleteDocument();
        }
    };

    public View.OnClickListener hidDocumentEditListener = new View.OnClickListener() {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mDocumentPresenter.hidEdit();
        }
    };

    public View.OnClickListener showMoreListener = new View.OnClickListener() {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mDocumentPresenter.showMenu();
        }
    };

    public View.OnClickListener renameListener = new View.OnClickListener() {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mDocumentPresenter.doRename();
        }
    };

    public View.OnClickListener mDeleteDownloadListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
           mDocumentPresenter.deleteDownloads();

        }
    };

    public View.OnClickListener mUpPictureListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mDocumentPresenter.startPhoto();
        }
    };
    public View.OnClickListener mUpVideoListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mDocumentPresenter.startVideo();

        }
    };
    public View.OnClickListener mUpAllfileListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            mDocumentPresenter.startAllFile();
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mDocumentPresenter.onKeyDown(keyCode, event)) {
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }
}
