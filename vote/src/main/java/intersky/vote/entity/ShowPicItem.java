package intersky.vote.entity;

import java.util.ArrayList;

import intersky.appbase.entity.Attachment;

/**
 * Created by xpx on 2017/6/20.
 */

public class ShowPicItem {

    public Attachment mAttachment;
    public ArrayList<Attachment> mAttachments;

    public ShowPicItem(Attachment mAttachment, ArrayList<Attachment> mAttachments)
    {
        this.mAttachment =mAttachment;
        this.mAttachments =mAttachments;
    }
}
