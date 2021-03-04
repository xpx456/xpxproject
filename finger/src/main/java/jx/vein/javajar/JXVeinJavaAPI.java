package jx.vein.javajar;


import jx.vein.javajar.vein.GetUSBPermission;

public class JXVeinJavaAPI {
	public static final int veinFeatSize = 512;
	public static final int width = 230;
	public static final int height = 100;
	public static final int imgSize = width*height;
	
	static {
		System.loadLibrary("usb1.0");
		System.loadLibrary("JXVeinJavaAPI");
	}

	private static JXVeinJavaAPI jxVeinJavaAPI = new JXVeinJavaAPI();

	public static JXVeinJavaAPI getInstance() {
		return jxVeinJavaAPI;
	}

	private native long InitVeinSDK();
	private native void ReleaseVeinSDK(long handle);
	private native int IsVeinDevConnected(long handle);
	private native int GetVeinDevSerial(long handle, byte[] buf, int len);
	private native int IsFingerTouched(long handle);
	private native int CapVeinFeat(long handle, byte[] feat_buf, int buf_len);
	private native int ReadLastVeinImg(long handle, byte[] img_buf, int buf_len);
	private native int SetRecgLevel(long handle, int level);
	private native int GetRecgLevel(long handle);
	private native int MatchVeinFeat(long handle, byte[] feat1, byte[] feat2, int feat_len);
	// 大于 0.29 判断不通过， 小于等于 0.29 通过， 0.20 - 0.26 执行更新操作
	private native float MatchVeinFeatEx(long handle, byte[] feat1, byte[] feat2, int feat_len);
	private native int SetRecgResult(long handle, int state);

	public static long devHandle;

	public long jxInitForThread() {
		return InitVeinSDK();
	}

	public void jxReleaseForThread(long devHandle) {
		ReleaseVeinSDK(devHandle);
	}

	public long jxInitVeinSDK()
	{
		new GetUSBPermission();
		devHandle = InitVeinSDK();
		return devHandle;
	}
	
	public void jxReleaseVeinSDK()
	{
		ReleaseVeinSDK(devHandle);
	}
	
	public int jxIsVeinDevConnected()
	{
		return IsVeinDevConnected(devHandle);
	}
	
	public int jxGetVeinDevSerial(byte[] buf) throws Exception
	{
		if(buf.length < 16)
			throw new Exception("Invalid buf length");
		return GetVeinDevSerial(devHandle, buf, 16);
	}
	
	public int jxIsFingerTouched()
	{
		return IsFingerTouched(devHandle);
	}
	
	public int jxCapVeinFeat(byte[] feat_buf) throws Exception
	{
		if(feat_buf.length < veinFeatSize)
			throw new Exception("Invalid buf length");
		return CapVeinFeat(devHandle, feat_buf, veinFeatSize);
	}
	
	public int jxSetRecgLevel(int level)
	{
		return SetRecgLevel(devHandle, level);
	}
	
	public int jxGetRecgLevel()
	{
		return GetRecgLevel(devHandle);
	}
	
	public int jxMatchVeinFeat(byte[] feat1, byte[] feat2) throws Exception
	{
		if(feat1.length < veinFeatSize || feat2.length < veinFeatSize)
			throw new Exception("Invalid buf length");
		return MatchVeinFeat(devHandle, feat1, feat2, veinFeatSize);
	}



	public float jxMatchVeinFeatEx(long devHandle, byte[] feat1, byte[] feat2)
	{
		return MatchVeinFeatEx(devHandle, feat1, feat2, veinFeatSize);
	}

	public int jxMatchVeinFeat(long devHandle, byte[] feat1, byte[] feat2) throws Exception
	{
		if(feat1.length < veinFeatSize || feat2.length < veinFeatSize)
			throw new Exception("Invalid buf length");
		return MatchVeinFeat(devHandle, feat1, feat2, veinFeatSize);
	}

	public int jxReadLastVeinImg(byte[] img_buf) throws Exception
	{
		if(img_buf.length < imgSize)
			throw new Exception("Invalid img buf length");
		return ReadLastVeinImg(devHandle, img_buf, imgSize);

	}

	public int jxSetRecgResult(int state)
	{
		 return SetRecgResult(devHandle, state);
	}
}