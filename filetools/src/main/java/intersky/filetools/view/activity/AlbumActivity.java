package intersky.filetools.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import intersky.filetools.presenter.AlbumPresenter;
import intersky.filetools.view.adapter.AlbumItemAdapter;

/**
 * Created by xpx on 2017/8/18.
 */

public class AlbumActivity extends BaseActivity {

    public ListView mAlbumList;
    public AlbumItemAdapter mAlbumItemAdapter;
    public AlbumPresenter mAlbumPresenter = new AlbumPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAlbumPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mAlbumPresenter.Destroy();
        super.onDestroy();
    }

    public AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id)
        {
            // TODO Auto-generated method stub
            mAlbumPresenter.clickItem(position);

        }

    };
}
