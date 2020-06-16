package intersky.vote.entity;

import android.view.View;

import java.util.ArrayList;

import intersky.appbase.entity.Attachment;

/**
 * Created by xpx on 2017/6/21.
 */

public class VoteSelect {

    public ArrayList<Attachment> mPics = new ArrayList<Attachment>();
    public String selectid = "";
    public String name;
    public View mView;
    public String hash = "";
    public int count = 0;
    public boolean iselect = false;
    public VoteSelect(String name,View mView)
    {
        this.name = name;
        this.mView = mView;
    }
}
