package intersky.conversation.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import intersky.appbase.BaseActivity;
import intersky.appbase.entity.Conversation;
import intersky.appbase.entity.Register;
import intersky.conversation.presenter.ConversationListPresenter;
import intersky.conversation.view.adapter.ConversationAdapter;
import intersky.conversation.view.adapter.ConversationPageAdapter;
import intersky.mywidget.NoScrollViewPager;
import intersky.mywidget.TabHeadView;

/**
 * Created by xpx on 2017/8/18.
 */

public class ConversationListActivity extends BaseActivity {

    public static final String ACTION_UPDATE_CONVERSATION_LIST = "ACTION_UPDATE_CONVERSATION_LIST";
    public Conversation mConversation;
    public Register register;
    public ConversationAdapter mReadConversationAdapter;
    public ConversationAdapter mUnReadConversationAdapter;
    public NoScrollViewPager mViewPager;
    public ArrayList<View> mViews = new ArrayList<View>();
    public ListView mReadList;
    public ListView mUnReadList;
    public ConversationPageAdapter mLoderPageAdapter;
    public TabHeadView mTabHeadView;
    public ConversationListPresenter mConversationListPresenter = new ConversationListPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConversationListPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mConversationListPresenter.Destroy();
        super.onDestroy();
    }

    public AdapterView.OnItemLongClickListener longClickListener = new AdapterView.OnItemLongClickListener()
    {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            mConversationListPresenter.onItemLongClick((Conversation) parent.getAdapter().getItem(position));
            return true;
        }
    };

    public AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener()
    {


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mConversationListPresenter.onItemClick((Conversation) parent.getAdapter().getItem(position));

        }
    };

}
