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
import intersky.mywidget.PullToRefreshView;

@SuppressLint("ValidFragment")
public class DocumentFragment extends BaseFragment implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {

    public DocumentAdapter mDocumentAdapter;
    public DocumentAdapter mDocumentSearchAdapter;
    public ListView documentList;
    public ImageView mNew;
    public EditText mSearchEdit;
    public RelativeLayout msearchLayer;
    public PullToRefreshView mPullToRefreshView;
    public ImageView isearch;
    public DocumentActivity mDocumentActivity;
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
        isearch = (ImageView) mView.findViewById(R.id.item_search);
        mNew = (ImageView) mView.findViewById(R.id.item_new);
        mSearchEdit = (EditText) mView.findViewById(R.id.item_search_edit);
        msearchLayer = (RelativeLayout) mView.findViewById(R.id.search_layer);
        mPullToRefreshView = (PullToRefreshView) mView.findViewById(R.id.mail_pull_refresh_view);
        //mPullToRefreshView.setOnFooterRefreshListener(this);
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mDocumentAdapter = new DocumentAdapter(mDocumentActivity, DocumentManager.getInstance().fileList, mDocumentActivity.mDocumentPresenter.mDocumentHandler);
        mDocumentSearchAdapter = new DocumentAdapter(mDocumentActivity, DocumentManager.getInstance().sfileList, mDocumentActivity.mDocumentPresenter.mDocumentHandler);
        documentList.setAdapter(mDocumentAdapter);
        documentList.setOnItemClickListener(documentClcikListener);
        mDocumentActivity.mDocumentPresenter.doUpdate();
        mSearchEdit.setOnFocusChangeListener(searchFocusChangeListener);
        mSearchEdit.setOnTouchListener(searchTouchListener);
        mSearchEdit.setOnEditorActionListener(mOnEditorActionListener);
        return mView;
    }

    private View.OnFocusChangeListener searchFocusChangeListener = new View.OnFocusChangeListener()
    {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus)
            {
                isearch.setVisibility(View.VISIBLE);
                msearchLayer.setVisibility(View.INVISIBLE);
                mSearchEdit.setHint(getActivity().getString(R.string.document_searchfilename));
            }
            else
            {
                isearch.setVisibility(View.INVISIBLE);
                msearchLayer.setVisibility(View.VISIBLE);
            }
        }
    };

    private View.OnTouchListener searchTouchListener = new View.OnTouchListener()
    {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            isearch.setVisibility(View.VISIBLE);
            msearchLayer.setVisibility(View.INVISIBLE);
            mSearchEdit.setHint(getActivity().getString(R.string.document_searchfilename));
            return false;
        }
    };

    private TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener()
    {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
        {
            // TODO Auto-generated method stub
            if (actionId == EditorInfo.IME_ACTION_SEARCH)
            {

                if (mSearchEdit.getText().toString().length() == 0)
                {
                    mDocumentActivity.mDocumentPresenter.hidSearch();
                }
                else
                {
                    mDocumentActivity.mDocumentPresenter.showSearch();
                    mDocumentActivity.mDocumentPresenter.doSearch(mSearchEdit.getText().toString());
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
