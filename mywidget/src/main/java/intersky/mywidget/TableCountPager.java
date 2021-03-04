package intersky.mywidget;

public class TableCountPager {

    public int showindex = 0;
    public int addsize = 15;
    public int showcount = 0;
    public boolean needprase = false;
    public int praseStart = 0;
    public int praseCount = 0;
    public int totalcount = 0;
    public TableCountPager(int totalcount) {
        showindex = 0;
        this.totalcount = totalcount;
        if(addsize < totalcount)
        {
            showcount = addsize;
        }
        else
        {
            showcount = totalcount;
        }
    }

    public void addConversation(int praseCount) {
        praseStart = 0;
        showindex+=praseCount;
        this.totalcount+=praseCount;
    }

    public boolean checkShowmore()
    {
        if(showindex < totalcount)
        {
            if(showindex+addsize > totalcount)
            {
                showcount = totalcount;
            }
            else
            {
                showcount+=addsize;
            }
            return true;
        }
        else
        {
            return false;
        }
    }


    public void updataTotal(int total) {
        totalcount = total;
    }
}
