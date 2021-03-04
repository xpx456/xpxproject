package intersky.filetools.entity;

import java.io.Serializable;


public class Video implements Serializable {
    /**
  *
  */
 private static final long serialVersionUID = -7920222595800367956L;
 private int id;
    public String title;
    public String album;
    public String artist;
    public String displayName;
    public String mimeType;
    public String path;
    public long size;
    public long duration;
    public boolean isselect = false;
    /**
    *
    */
    public Video() {
        super();
    }

    /**
    * @param id
    * @param title
    * @param album
    * @param artist
    * @param displayName
    * @param mimeType
    * @param size
    * @param duration
    */
    public Video(int id, String title, String album, String artist,
                 String displayName, String mimeType, String path, long size,
                 long duration) {
        super();
        this.id = id;
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.displayName = displayName;
        this.mimeType = mimeType;
        this.path = path;
        this.size = size;
        this.duration = duration;
    }


}

