package intersky.guide.entity;

import intersky.guide.GuideUtils;

public class GuidePic {

    public int resid = 1;
    public String path = "";

    public GuidePic(int id)
    {
        resid = id;
    }

    public GuidePic(String path)
    {
        this.path = path;
    }
}
