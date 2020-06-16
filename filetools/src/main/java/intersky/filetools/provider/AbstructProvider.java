package intersky.filetools.provider;

import java.util.ArrayList;

import intersky.filetools.entity.Video;


public interface AbstructProvider {
 
    public int getList(ArrayList<Video> listVideos, String selectpath);
    public Video getVideoItem(String selectpath);
    
}

