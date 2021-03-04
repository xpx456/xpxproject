package intersky.filetools.entity;

public class LocalDocument {

    public int mType;
    public String mName;
    public String mDate;
    public String mPath;
    public boolean isSelect;
    public String mParentPath;

    public LocalDocument() {

    }

    public LocalDocument(int mType,String mPath,String mName,String mParentPath)
    {
        this.mType = mType;
        this.mName = mName;
        this.isSelect = isSelect;
        this.mPath = mPath;
        this.mParentPath = mParentPath;
    }
}
