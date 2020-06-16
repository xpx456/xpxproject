package intersky.mywidget;


public class TableItem {

    public String mTitle;
    public int mWidth = 80;

    public boolean isHead = false;
    public String filde =  "";

    public TableItem(String fild,String mTitle, boolean isHead, int mWidth,int density)
    {
        this.mTitle = mTitle;
        this.isHead = isHead;
        this.filde = fild;
        this.mWidth = (int) (mWidth* density);
    }

}
