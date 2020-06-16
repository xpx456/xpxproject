package intersky.select.entity;

public class CustomSelect {

    public String mId;
    public String mName;
    public Object object;
    public boolean iselect = false;

    public CustomSelect(String mId, String mName,Object object)
    {
        this.mId = mId;
        this.mName = mName;
        this.object = object;
    }
}
