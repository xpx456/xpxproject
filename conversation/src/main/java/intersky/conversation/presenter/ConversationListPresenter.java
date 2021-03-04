package intersky.conversation.presenter;

import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.conversation.ConversationManager;
import intersky.conversation.R;
import intersky.appbase.entity.Conversation;
import intersky.conversation.entity.Channel;
import intersky.conversation.handler.ConversationListHandler;
import intersky.conversation.receiver.ConversationReceiver;
import intersky.conversation.view.activity.ConversationListActivity;
import intersky.conversation.view.adapter.ConversationAdapter;
import intersky.conversation.view.adapter.ConversationAdapter2;
import intersky.conversation.view.adapter.ConversationPageAdapter;
import intersky.mywidget.NoScrollViewPager;
import xpx.com.toolbar.utils.ToolBarHelper;

/**
 * Created by xpx on 2017/8/18.
 */

public class  ConversationListPresenter implements Presenter {

    public ConversationListHandler mConversationListHandler;
    public ConversationListActivity mConversationListActivity;
    public ConversationListPresenter(ConversationListActivity mConversationListActivity)
    {
        this.mConversationListActivity = mConversationListActivity;
        this.mConversationListHandler = new ConversationListHandler(mConversationListActivity);
        mConversationListActivity.setBaseReceiver(new ConversationReceiver(mConversationListHandler));
    }

    @Override
    public void Create() {
        initView();

    }

    @Override
    public void initView() {
        mConversationListActivity.flagFillBack = false;
        mConversationListActivity.setContentView(R.layout.activity_conversation_list);
        ImageView back = mConversationListActivity.findViewById(R.id.back);
        back.setOnClickListener(mConversationListActivity.mBackListener);
        mConversationListActivity.mConversation = mConversationListActivity.getIntent().getParcelableExtra("conversation");
        Channel channel = ConversationManager.getInstance().getChannel(mConversationListActivity.mConversation.mType);
        TextView title = mConversationListActivity.findViewById(R.id.title);
        if(channel != null)
            title.setText(channel.name);
        View mView1 = null;
        View mView2 = null;
        String[] names = new String[2];
        mView1 = mConversationListActivity.getLayoutInflater().inflate(R.layout.conversation_pager, null);
        names[0] = "未读";
        names[1] = "已读";
        mView2 = mConversationListActivity.getLayoutInflater().inflate(R.layout.conversation_pager, null);
        mConversationListActivity.mTabHeadView = mConversationListActivity.findViewById(R.id.head);
        mConversationListActivity.mViewPager = (NoScrollViewPager) mConversationListActivity.findViewById(R.id.load_pager);
        mConversationListActivity.mViewPager.setNoScroll(true);
        mConversationListActivity.mViews.add(mView1);
        mConversationListActivity.mViews.add(mView2);
        mConversationListActivity.mReadList = mView2.findViewById(R.id.busines_List);
        mConversationListActivity.mReadList.setLayoutManager(new LinearLayoutManager(mConversationListActivity));
        mConversationListActivity.mUnReadList = mView1.findViewById(R.id.busines_List);
        mConversationListActivity.mUnReadList.setLayoutManager(new LinearLayoutManager(mConversationListActivity));
        mConversationListActivity.mLoderPageAdapter = new ConversationPageAdapter(mConversationListActivity.mViews,names);
        intConversations();
        mConversationListActivity.mViewPager.setAdapter(mConversationListActivity.mLoderPageAdapter);
        mConversationListActivity.mTabHeadView.setViewPager(mConversationListActivity.mViewPager);
    }

    @Override
    public void Start() {

    }

    @Override
    public void Resume() {

    }

    @Override
    public void Pause() {

    }

    @Override
    public void Destroy() {

    }

    public void intConversations()
    {
        mConversationListActivity.mConversation = mConversationListActivity.getIntent().getParcelableExtra("conversation");
        mConversationListActivity.register = ConversationManager.getInstance().conversationAll.getRegister(mConversationListActivity.mConversation.mType);
        mConversationListActivity.mReadConversationAdapter = new ConversationAdapter2(ConversationManager.getInstance().getDetialList(mConversationListActivity.mConversation,true), mConversationListActivity,swapFunction);
        mConversationListActivity.mUnReadConversationAdapter = new ConversationAdapter2(ConversationManager.getInstance().getDetialList(mConversationListActivity.mConversation,false), mConversationListActivity,swapFunction);
        mConversationListActivity.mReadList.setAdapter(mConversationListActivity.mReadConversationAdapter);
        mConversationListActivity.mUnReadList.setAdapter(mConversationListActivity.mUnReadConversationAdapter);
        mConversationListActivity.mReadConversationAdapter.setOnItemClickListener(mConversationListActivity.clickListener);
        mConversationListActivity.mUnReadConversationAdapter.setOnItemClickListener(mConversationListActivity.clickListener);
    }

    public void onItemClick(Conversation conversation) {
        if(mConversationListActivity.register.conversationFunctions != null)
        {

            mConversationListActivity.register.conversationFunctions.Open(conversation);
        }
    }

    public ConversationAdapter2.SwapFunction swapFunction = new ConversationAdapter2.SwapFunction() {
        @Override
        public void read(Conversation conversation) {

        }

        @Override
        public void delete(Conversation conversation) {
            onItemLongClick(conversation);
        }
    };

    public void onItemLongClick(Conversation conversation) {
        AppUtils.creatDialogTowButton(mConversationListActivity,mConversationListActivity.getString(R.string.delete_conversation),
                mConversationListActivity.getString(R.string.button_delete),
                mConversationListActivity.getString(R.string.button_no),mConversationListActivity.getString(R.string.button_yes),
                null,new  DeleteListener(conversation));
    }

    public void doUpdate() {
        mConversationListActivity.mReadConversationAdapter.notifyDataSetChanged();
        mConversationListActivity.mUnReadConversationAdapter.notifyDataSetChanged();
    }

    class DeleteListener implements DialogInterface.OnClickListener
    {
        public Conversation conversation;

        public DeleteListener(Conversation conversation)
        {
            this.conversation = conversation;
        }
        @Override
        public void onClick(DialogInterface dialog, int which) {

            ConversationManager.getInstance().doDelete(conversation);
        }
    }
}
