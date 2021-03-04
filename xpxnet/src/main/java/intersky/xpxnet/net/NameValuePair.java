package intersky.xpxnet.net;

/**
 * Created by xpx on 16-7-5.
 */
public class NameValuePair {

    public String key = "";
    public String value = "";
    public boolean isFile = false;
    public String path  = "";

    public NameValuePair(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
