package intersky.chat.entity;

import java.util.ArrayList;

import intersky.appbase.entity.Conversation;

public class ChatPager {

    public int showindex = 0;
    public int addsize = 10;
    public int showcount = 0;
    public boolean needprase = false;
    public int praseStart = 0;
    public int praseCount = 0;
    public int totalcount;
    public ChatPager(int totalcount) {
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
        showcount+=praseCount;
        this.totalcount+=praseCount;
    }

    public boolean checkShowmore()
    {
        if(showcount < totalcount)
        {
            if(showcount+addsize > totalcount)
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
}
