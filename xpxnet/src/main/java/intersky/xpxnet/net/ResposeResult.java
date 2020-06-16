package intersky.xpxnet.net;

public class ResposeResult {

    public String result;
    public boolean isSuccess;

    public ResposeResult(boolean isSuccess,String result) {
        this.result = result;
        this.isSuccess = isSuccess;
    }

}
