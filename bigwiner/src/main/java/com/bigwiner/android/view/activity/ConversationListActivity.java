package com.bigwiner.android.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.recyclerview.widget.RecyclerView;

import com.bigwiner.android.presenter.ConversationListPresenter;
import com.bigwiner.android.view.adapter.ConversationAdapter;

import intersky.appbase.BaseActivity;
import intersky.appbase.entity.Conversation;
import intersky.mywidget.SwipeAdapter;
import intersky.mywidget.SwipeListView;

/**
 * Created by xpx on 2017/8/18.
 */

public class ConversationListActivity extends BaseActivity {

    public ConversationListPresenter mConversationListPresenter = new ConversationListPresenter(this);
    public RecyclerView listView;
    public ImageView back;
    public ConversationAdapter mConversationAdapter;
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

    public AdapterView.OnItemClickListener startChatListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mConversationListPresenter.startChat((Conversation) parent.getAdapter().getItem(position));
        }
    };

    public ConversationAdapter.OnItemClickListener startClickListener = new ConversationAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(Conversation conversation, int position, View view) {
            mConversationListPresenter.onItemClick(conversation);
        }
    };

    public SwipeAdapter.OnItemClickListener startClickListener2 = new SwipeAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position, View view,Object item) {
            mConversationListPresenter.onItemClick((Conversation) item);
        }

    };

    public SwipeAdapter.OnItemMenuClickListener menuItemlistener = new SwipeAdapter.OnItemMenuClickListener() {
        @Override
        public void onItemMenuClick(int position, View menu,Object item) {
            mConversationListPresenter.menuclick((Conversation) item,menu.getId());
        }

    };

    public View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
}
