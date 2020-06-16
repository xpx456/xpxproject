package intersky.appbase.entity;


/**
 * Created by xpx on 2017/3/31.
 */

public class Reply {

    public String mReplyId;
    public String mUserId;
    public String mConetent;
    public String mReportId;
    public String mCreatTime;

    public Reply(String mReplyId, String mUserId, String mConetent, String mReportId, String mCreatTime)
    {
        this.mReplyId = mReplyId;
        this.mUserId = mUserId;
        this.mConetent = mConetent;
        this.mReportId = mReportId;
        this.mCreatTime = mCreatTime;
    }
}
