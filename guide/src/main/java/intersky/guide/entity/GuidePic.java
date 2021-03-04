package intersky.guide.entity;

import intersky.guide.GuideUtils;

public class GuidePic {

    public int resid = 1;
    public String path = "";
    public String keyword = "";
    public GuidePic(int id)
    {
        resid = id;
    }

    public GuidePic(int id,String word)
    {
        resid = id;
        keyword = word;
    }

    public GuidePic(String path)
    {
        this.path = path;
    }
}
