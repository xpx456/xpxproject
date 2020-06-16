package intersky.vote.presenter;

import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import intersky.appbase.Presenter;
import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Contacts;
import intersky.vote.R;
import intersky.vote.entity.Reocrd;
import intersky.vote.view.activity.VoteRecordActivity;
import intersky.vote.view.adapter.RecordAdapter;
import xpx.com.toolbar.utils.ToolBarHelper;

public class VoteRecordPresenter implements Presenter {

    public VoteRecordActivity mVoteRecordActivity;


    public VoteRecordPresenter(VoteRecordActivity mVoteRecordActivity) {
        this.mVoteRecordActivity = mVoteRecordActivity;
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        mVoteRecordActivity.setContentView(R.layout.activity_vote_record);
        ToolBarHelper.setTitle(mVoteRecordActivity.mActionBar, mVoteRecordActivity.getString(R.string.xml_vote_record_title));
        mVoteRecordActivity.mListView = (ListView) mVoteRecordActivity.findViewById(R.id.record_List);
        mVoteRecordActivity.vote = mVoteRecordActivity.getIntent().getParcelableExtra("vote");
        initRecords();
        mVoteRecordActivity.mRecordAdapter = new RecordAdapter(mVoteRecordActivity, mVoteRecordActivity.mReocrds);
        mVoteRecordActivity.mListView.setAdapter(mVoteRecordActivity.mRecordAdapter);

    }

    @Override
    public void Create() {
        // TODO Auto-generated method stub
        initView();
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

    public void initRecords() {

        try {
            JSONObject jsonObject = new JSONObject(mVoteRecordActivity.vote.selectJson);
            JSONObject data = jsonObject.getJSONObject("data");

            if (data.has("has_many_tiems")) {
                JSONArray ja = data.getJSONArray("has_many_tiems");
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    Reocrd mReocrdModel = new Reocrd(jo.getString("item_name"), jo.getString("vote_item_id"));
                    mVoteRecordActivity.mReocrds.add(mReocrdModel);
                }
                Reocrd nuvote = new Reocrd("未投用户", "-1");
                mVoteRecordActivity.mReocrds.add(nuvote);
                if (data.has("has_many_notice")) {
                    JSONArray ja2 = data.getJSONArray("has_many_notice");
                    for (int i = 0; i < ja2.length(); i++) {
                        JSONObject jo = ja2.getJSONObject(i);
                        String str[] = jo.getString("vote_item_id").split(",");
                        if(jo.getString("vote_item_id").equals("0"))
                        {
                            nuvote.mContacts.add((Contacts) Bus.callData(mVoteRecordActivity,"chat/getContactItem",jo.getString("user_id")));
                        }
                        else
                        {
                            for (int k = 0; k < str.length; k++) {
                                for (int j = 0; j < mVoteRecordActivity.mReocrds.size(); j++) {
                                    Reocrd mReocrdModel = mVoteRecordActivity.mReocrds.get(j);

                                    if(str[k].equals(mReocrdModel.id))
                                    {
                                        mReocrdModel.mContacts.add((Contacts) Bus.callData(mVoteRecordActivity,"chat/getContactItem",jo.getString("user_id")));
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
