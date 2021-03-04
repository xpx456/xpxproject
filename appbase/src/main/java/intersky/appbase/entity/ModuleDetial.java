package intersky.appbase.entity;

public class ModuleDetial {
    public int currentpage = 1;
    public int totlepage = 1;
    public int pagesize = 30;
    public int totleszie = 0;
    public int currentszie = 0;
    public int allpagesize = 0;
    public int allpagesizemax = 2;
    public Conversation today = null;
    public Conversation yestday = null;
    public Conversation before = null;
    public int todaycount = 0;
    public int yestdaycount = 0;
    public int beforecount = 0;

    public void reset() {
        currentpage = 1;
        totlepage = 1;
        pagesize = 30;
        totleszie = 0;
        currentszie = 0;
        allpagesize = 0;
        allpagesizemax = 2;
        today = null;
        yestday = null;
        before = null;
        todaycount = 0;
        yestdaycount = 0;
        beforecount = 0;
    }
}