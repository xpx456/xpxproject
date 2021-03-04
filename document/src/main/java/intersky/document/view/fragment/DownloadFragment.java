package intersky.document.view.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import intersky.document.DocumentManager;
import intersky.document.R;
import intersky.document.entity.DocumentNet;
import intersky.document.presenter.DocumentPresenter;
import intersky.document.view.activity.DocumentActivity;
import intersky.document.view.adapter.LoadItemAdapter;
import intersky.document.view.adapter.LoderPageAdapter;
import intersky.appbase.BaseFragment;
import xpx.com.toolbar.utils.ToolBarHelper;


@SuppressLint("ValidFragment")
public class DownloadFragment extends BaseFragment {

    public ViewPager mViewPager;
    private RelativeLayout mRightTeb;
    private RelativeLayout mLefttTeb;
    public TextView mRightImg;
    public TextView mLefttImg;
    private ArrayList<View> mViews = new ArrayList<View>();
    private LoderPageAdapter mLoderPageAdapter;
    private ListView mDownloadList;
    private ListView mUploadList;
    public LoadItemAdapter mDownLoadItemAdapter;
    public LoadItemAdapter mUpLoadItemAdapter;
    public DocumentActivity mDocumentActivity;
    public DownloadFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mDocumentActivity = (DocumentActivity) getActivity();
        View mView = inflater.inflate(R.layout.fragment_download, container, false);
        View mView1 = inflater.inflate(R.layout.loadpager, null);
        View mView2 = inflater.inflate(R.layout.loadpager, null);
        mViews.add(mView1);
        mViews.add(mView2);
        mDownloadList = (ListView) mViews.get(0).findViewById(R.id.loder_List);
        mUploadList = (ListView) mViews.get(1).findViewById(R.id.loder_List);
        mViewPager = (ViewPager) mView.findViewById(R.id.load_pager);
        mLefttTeb = (RelativeLayout) mView.findViewById(R.id.downloadpage);
        mRightTeb = (RelativeLayout) mView.findViewById(R.id.uploadpage);
        mRightImg = (TextView) mView.findViewById(R.id.btnupload);
        mLefttImg = (TextView) mView.findViewById(R.id.btndownload);
        mLefttTeb.setOnClickListener(mTabLeftListener);
        mRightTeb.setOnClickListener(mTabRightListener);
        mLoderPageAdapter = new LoderPageAdapter(mViews);
        mDownLoadItemAdapter = new LoadItemAdapter(getActivity(), DocumentManager.getInstance().downloads,mDocumentActivity.mDocumentPresenter.mDocumentHandler,0);
        mUpLoadItemAdapter = new LoadItemAdapter(getActivity(), DocumentManager.getInstance().uploads,mDocumentActivity.mDocumentPresenter.mDocumentHandler,1);
        mViewPager.setAdapter(mLoderPageAdapter);
        mDownloadList.setAdapter(mDownLoadItemAdapter);
        mUploadList.setAdapter(mUpLoadItemAdapter);
        mDownloadList.setOnItemClickListener(mOnDownItemClickListener);
        mUploadList.setOnItemClickListener(mOnUpItemClickListener);
        if(mDocumentActivity.mDocumentPresenter.mDocumentActivity.getIntent().getBooleanExtra("showup",false) == true)
        {
            showUppage();
        }
        else
        {
            showDownpage();
        }

        return mView;
    }

    public void showDownpage() {

        mDocumentActivity.title.setText(mDocumentActivity.getString(R.string.document_xiazai));
        mViewPager.setCurrentItem(0);
        mRightImg.setBackgroundResource(R.drawable.shape_bg_readbgun);
        mRightImg.setTextColor(Color.parseColor("#8F9093"));
        mLefttImg.setBackgroundResource(R.drawable.shape_bg_readbg);
        mLefttImg.setTextColor(Color.parseColor("#ffffff"));
        if (DocumentManager.getInstance().upSelectCount > 0)
            mDocumentActivity.mDocumentPresenter.hideLoadEdit();

    }

    public void showUppage() {
        mDocumentActivity.title.setText(mDocumentActivity.getString(R.string.document_shangchuan));
        mViewPager.setCurrentItem(1);
        mRightImg.setBackgroundResource(R.drawable.shape_bg_readbg);
        mRightImg.setTextColor(Color.parseColor("#ffffff"));
        mLefttImg.setBackgroundResource(R.drawable.shape_bg_readbgun);
        mLefttImg.setTextColor(Color.parseColor("#8F9093"));
        if (DocumentManager.getInstance().downSelectCount > 0)
            mDocumentActivity.mDocumentPresenter.hideLoadEdit();
    }

    private View.OnClickListener mTabLeftListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            showDownpage();
        }
    };

    private View.OnClickListener mTabRightListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            showUppage();

        }
    };

    private AdapterView.OnItemClickListener mOnDownItemClickListener = new AdapterView.OnItemClickListener()
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id)
        {
            // TODO Auto-generated method stub
            mDocumentActivity.mDocumentPresenter.doDowloadClick((DocumentNet) parent.getAdapter().getItem(position));

        }
    };

    private AdapterView.OnItemClickListener mOnUpItemClickListener = new AdapterView.OnItemClickListener()
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id)
        {
            // TODO Auto-generated method stub
            mDocumentActivity.mDocumentPresenter.doDowloadClick((DocumentNet) parent.getAdapter().getItem(position));

        }
    };
}
