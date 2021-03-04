package intersky.document.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import intersky.document.DocumentManager;
import intersky.document.R;
import intersky.document.entity.DocumentNet;
import intersky.document.presenter.DocumentPresenter;
import intersky.document.view.activity.DocumentActivity;
import intersky.document.view.adapter.DocumentAdapter;
import intersky.appbase.BaseFragment;
import intersky.document.view.adapter.DocumentGAdapter;
import intersky.mywidget.PullToRefreshView;
import intersky.mywidget.SearchViewLayout;

@SuppressLint("ValidFragment")
public class DocumentFragment extends BaseFragment implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {

    public DocumentAdapter mDocumentAdapter;
    public DocumentAdapter mDocumentSearchAdapter;
    public DocumentGAdapter mDocumentGAdapter;
    public ListView documentList;
    public GridView documentGList;
    public ImageView mNew;
    public SearchViewLayout msearchLayer;
    public PullToRefreshView mPullToRefreshView;
    public PullToRefreshView mPullToRefreshViewG;
    public DocumentActivity mDocumentActivity;
    public boolean showgrid = true;
    public DocumentFragment()
    {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mDocumentActivity = (DocumentActivity) getActivity();
        View mView = inflater.inflate(R.layout.fragment_document, container, false);
        documentList = mView.findViewById(R.id.document_List);
        documentGList = mView.findViewById(R.id.document_List_g);
        mNew = (ImageView) mView.findViewById(R.id.item_new);
        msearchLayer = (SearchViewLayout) mView.findViewById(R.id.search);
        mPullToRefreshViewG = (PullToRefreshView) mView.findViewById(R.id.mail_pull_refresh_viewg);
        mPullToRefreshView = (PullToRefreshView) mView.findViewById(R.id.mail_pull_refresh_view);
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mDocumentAdapter = new DocumentAdapter(mDocumentActivity, DocumentManager.getInstance().fileList, mDocumentActivity.mDocumentPresenter.mDocumentHandler);
        mDocumentGAdapter = new DocumentGAdapter(mDocumentActivity, DocumentManager.getInstance().fileList, mDocumentActivity.mDocumentPresenter.mDocumentHandler);
        mDocumentSearchAdapter = new DocumentAdapter(mDocumentActivity, DocumentManager.getInstance().sfileList, mDocumentActivity.mDocumentPresenter.mDocumentHandler);
        documentList.setAdapter(mDocumentAdapter);
        documentGList.setAdapter(mDocumentGAdapter);
        documentList.setOnItemClickListener(documentClcikListener);
        documentGList.setOnItemClickListener(documentClcikListener);
        mDocumentActivity.mDocumentPresenter.doUpdate();
        msearchLayer.btnCancle.setOnEditorActionListener(mOnEditorActionListener);
        showgrid();

        return mView;
    }

    public void showgrid()
    {
        if(showgrid == true)
        {
            if(DocumentManager.getInstance().pathList.size() > 1)
            {
                showgrid = false;
                mPullToRefreshViewG.setVisibility(View.INVISIBLE);
                mPullToRefreshView.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            if(DocumentManager.getInstance().pathList.size() == 1)
            {
                showgrid = true;
                mPullToRefreshViewG.setVisibility(View.VISIBLE);
                mPullToRefreshView.setVisibility(View.INVISIBLE);
            }
        }
    }

    public TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener()
    {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
        {
            // TODO Auto-generated method stub
            if (actionId == EditorInfo.IME_ACTION_NEXT)
            {
                if (msearchLayer.getText().length() > 0)
                {
                    mPullToRefreshViewG.setVisibility(View.INVISIBLE);
                    mPullToRefreshView.setVisibility(View.VISIBLE);

                    mDocumentActivity.mDocumentPresenter.showSearch();
                    mDocumentActivity.mDocumentPresenter.doSearch(msearchLayer.getText().toString());
                }
                else
                {
                    mDocumentActivity.mDocumentPresenter.hidSearch();
                    if(DocumentManager.getInstance().pathList.size() == 1)
                    {
                        mPullToRefreshViewG.setVisibility(View.VISIBLE);
                        mPullToRefreshView.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        mPullToRefreshViewG.setVisibility(View.INVISIBLE);
                        mPullToRefreshView.setVisibility(View.VISIBLE);
                    }
                }

            }

            return true;
        }
    };


    public AdapterView.OnItemClickListener documentClcikListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mDocumentActivity.mDocumentPresenter.doDocumentClcick((DocumentNet) parent.getAdapter().getItem(position));
        }
    };

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        mPullToRefreshView.onFooterRefreshComplete();
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        mDocumentActivity.mDocumentPresenter.doUpdate();
        mPullToRefreshView.onHeaderRefreshComplete();
    }
}
