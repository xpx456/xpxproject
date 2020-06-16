package intersky.xpxnet.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetUtils {
	public static final int TimeOut = 35000;
	public static final int TimeOut2 = 100000;
	public static final int NO_NET_WORK = 1020002;
	public static final int NO_INTERFACE = 1020001;
	public static final int TOKEN_ERROR = 1020003;
	public static final MediaType MEDIA_TYPE_MARKDOWN  = MediaType.parse("text/x-markdown; charset=utf-8");
	private volatile static NetUtils mNetUtils;
	public static OkHttpClient mOkHttpClient;
	public static OkHttpClient mDownloadOkHttpClient;
//	public static String CLOUND_HOST = "";
	public static Context mContext;
	private static NetTaskManagerThread mNetTaskManagerThread;
	private static HashMap<String,MyNetTaskManagerThread> hashNetTaskManagerThread = new HashMap<String,MyNetTaskManagerThread>();
	public String token = "";
	public String utoken = "";
	public String data = "";
	public boolean tokenneetupdata = false;
	public static Call mDocumentUpdataCall;
//	public Service service = null;
	public NetUtils() {
		mOkHttpClient = new OkHttpClient.Builder().cookieJar(new CookieJarImpl(new PersistentCookieStore(mContext)))
				.connectTimeout(TimeOut, TimeUnit.MILLISECONDS).readTimeout(TimeOut2, TimeUnit.MILLISECONDS).writeTimeout(TimeOut2, TimeUnit.MILLISECONDS).build();
	}

	public void cleanTasks() {
		for (Map.Entry<String, MyNetTaskManagerThread> entry : hashNetTaskManagerThread.entrySet()) {
			MyNetTaskManagerThread taskManagerThread = entry.getValue();
			taskManagerThread.mNetTaskManager.clean();
		}
		mNetTaskManagerThread.mNetTaskManager.clean();
	}

	public static NetUtils init(Context context) {
		if (mNetUtils == null) {
			synchronized (NetUtils.class) {
				if (mNetUtils == null) {
					mContext = context;
					mNetUtils = new NetUtils();
					if(mNetTaskManagerThread == null)
						mNetTaskManagerThread = new NetTaskManagerThread();
					mNetTaskManagerThread.setStop(false);
					new Thread(mNetTaskManagerThread).start();
				}
				else
				{
					mContext = context;
					mOkHttpClient = new OkHttpClient.Builder().cookieJar(new CookieJarImpl(new PersistentCookieStore(mContext)))
							.connectTimeout(TimeOut, TimeUnit.MILLISECONDS).readTimeout(TimeOut2, TimeUnit.MILLISECONDS).writeTimeout(TimeOut2, TimeUnit.MILLISECONDS).build();
					if(mNetTaskManagerThread == null)
						mNetTaskManagerThread = new NetTaskManagerThread();
					mNetTaskManagerThread.setStop(false);
					new Thread(mNetTaskManagerThread).start();
				}
			}
		}
		return mNetUtils;
	}

	public void addaskManagerThread(String name,int time,int size) {
		if(!hashNetTaskManagerThread.containsKey(name))
		{
			MyNetTaskManagerThread taskManagerThread = new MyNetTaskManagerThread(size,time,name);
			hashNetTaskManagerThread.put(name,taskManagerThread);
			taskManagerThread.setStop(false);
			new Thread(taskManagerThread).start();
		}

	}

//	public NetUtils initService(Service service) {
//		this.service = service;
//		return mNetUtils;
//	}

//	public void initOaHost(String host) {
//		CLOUND_HOST = host;
//	}



	public void close() {
		if(mNetTaskManagerThread != null)
		mNetTaskManagerThread.setStop(true);
	}


	/**
	 * 包装OkHttpClient，用于下载文件的回调
	 */
	public static OkHttpClient getOkClient(final ProgressResponseListener listener) {
		//克隆
		OkHttpClient.Builder builder=  mOkHttpClient.newBuilder();
		builder.networkInterceptors().clear();
		Interceptor interceptor = new Interceptor() {
			@Override
			public Response intercept(Chain chain) throws IOException {
				Response response = chain.proceed(chain.request());

				return response.newBuilder().body(new ProgressResponseBody(listener, response.body())).build();
			}
		};

		builder.networkInterceptors().add(interceptor);
		return builder.build();
	}

	public int doDownload(String url,ProgressResponseListener listener,RandomAccessFile randomAccessFile,Contral mContral) throws IOException {

		if(url.length() < 6)
		{
			return 0;
		}

		if(!(url.contains("http://")||url.contains("https://")))
		{
			return 0;
		}

		mDownloadOkHttpClient = NetUtils.getOkClient(listener);
		Request request = new Request.Builder().url(url).build();
		Call mCall = mDownloadOkHttpClient.newCall(request);
		Response response = mCall.execute();
		if (response.isSuccessful()) {
			InputStream is = response.body().byteStream();
			if(mContral.stop == true)
			{
				is.close();
				mCall.cancel();
				return 0;
			}
			int len = -1;
			byte[] buffer = new byte[1024];
			while ((len = is.read(buffer)) != -1) {
				randomAccessFile.write(buffer, 0, len);
			}
			is.close();
			return 1;
		}
		else
		{
			return 0;
		}
	}


	public int doDownload2(String url,RandomAccessFile randomAccessFile,Contral mContral) throws IOException {

		if(url.length() < 6)
		{
			return 0;
		}

		if(!(url.contains("http://")||url.contains("https://")))
		{
			return 0;
		}

		Request request = new Request.Builder().url(url).build();
		Call mCall = mOkHttpClient.newCall(request);
		Response response = mCall.execute();
		if (response.isSuccessful()) {
			InputStream is = response.body().byteStream();
			if(mContral.stop == true)
			{
				is.close();
				mCall.cancel();
				return 0;
			}
			int len = -1;
			byte[] buffer = new byte[1024];
			while ((len = is.read(buffer)) != -1) {
				randomAccessFile.write(buffer, 0, len);
			}
			is.close();
			return 1;
		}
		else
		{
			return 0;
		}
	}

	public int doDownload3(String url,RandomAccessFile randomAccessFile,Contral mContral) throws IOException {

		if(url.length() < 6)
		{
			return 0;
		}

		if(!(url.contains("http://")||url.contains("https://")))
		{
			return 0;
		}

		Request request = new Request.Builder().url(url).build();
		Call mCall = mOkHttpClient.newCall(request);
		Response response = mCall.execute();
		if (response.isSuccessful()) {
			InputStream is = response.body().byteStream();
			if(mContral.stop == true)
			{
				is.close();
				mCall.cancel();
				return 0;
			}
			int len = -1;
			byte[] buffer = new byte[1024];
			while ((len = is.read(buffer)) != -1) {
				randomAccessFile.write(buffer, 0, len);
			}
			is.close();
			return 1;
		}
		else
		{
			return 0;
		}
	}

	public static synchronized NetUtils getInstance() {
		if (mNetUtils == null) {
			mNetUtils = new NetUtils();
		}
		return mNetUtils;
	}

	public static String praseUrl(Service service,String iurl,HashMap<String,String> values) {
		String url = "";

		String path = iurl;
		if(path.startsWith("\\"))
		{
			path = path.substring(1,path.length());
		}
		path = path.replaceAll("\\\\","/");

		if(service.https)
		{
			url += "https://"+service.sAddress+":"+service.sPort+"/";
		}
		else
		{
			url += "http://"+service.sAddress+":"+service.sPort+"/";
		}
		url += path;
		if(values != null)
		{
			int i = 0;
			for (Map.Entry<String, String> entry : values.entrySet()) {
				if(i == 0)
					url+="?";
				url+= (entry.getKey()+"="+entry.getValue());
				if(i != 0)
					url+="&";
			}
		}
		return url;
	}

	public static String praseUrl(Service service,String iurl) {
		String url = "";

		String path = iurl;
		if(path.startsWith("\\"))
		{
			path = path.substring(1,path.length());
		}
		path = path.replaceAll("\\\\","/");


		if(service.https)
		{
			url += "https://"+service.sAddress+":"+service.sPort+"/";
		}
		else
		{
			url += "http://"+service.sAddress+":"+service.sPort+"/";
		}
		url += path;
		return url;
	}



	public final String praseUrl(Service service,String iurl,String params) {
		String url = "";

		String path = iurl;
		if(path.startsWith("\\"))
		{
			path = path.substring(1,path.length());
		}
		path = path.replaceAll("\\\\","/");

		if(service.https == true)
		{
			return ("https://" + service.sAddress // HOST
					+ ":" + service.sPort // HOST
					// PORT
					+ "/"+path+"?"+params); // PAGE
		}
		else
		{
			return ("http://" + service.sAddress // HOST
					+ ":" + service.sPort // HOST
					// PORT
					+ "/"+ path+"?"+params); // PAGE
		}

	}
//
//	public final String createURLStringex(String path,Service mService) {
//		if(mService.https == true)
//		{
//			return ("https://" + mService.sAddress // HOST
//					+ ":" + mService.sPort // HOST
//					// PORT
//					+ "/" + path); // PAGE
//		}
//		else
//		{
//			return ("http://" + mService.sAddress // HOST
//					+ ":" + mService.sPort // HOST
//					// PORT
//					+ "/" + path); // PAGE
//		}
//
//	}
//
//	public final String createURLStringex(Service service,String ipath) {
//		String path = ipath;
//		if(path.startsWith("\\"))
//		{
//			path = path.substring(1,path.length());
//		}
//		path = path.replaceAll("\\\\","/");
//		if(service.https == true)
//		{
//			return ("https://" + service.sAddress // HOST
//					+ ":" + service.sPort // HOST
//					// PORT
//					+ "/" + path); // PAGE
//		}
//		else
//		{
//			return ("http://" + service.sAddress // HOST
//					+ ":" + service.sPort // HOST
//					// PORT
//					+ "/" + path); // PAGE
//		}
//
//	}
//
//
//	public final String createURLString(Service service,String path) {
//		if(service.https == true)
//		{
//			return ("https://" + service.sAddress // HOST
//					+ ":" + service.sPort // HOST
//					// PORT
//					+  path); // PAGE
//		}
//		else
//		{
//			return ("http://" + service.sAddress // HOST
//					+ ":" + service.sPort // HOST
//					// PORT
//					+  path); // PAGE
//		}
//
//	}
//
//


	public ResposeResult post(String url , RequestBody formBody) {

		try {
			if(url.length() < 6)
			{
				return null;
			}
			if(!(url.contains("http://")||url.contains("https://")))
			{
				return null;
			}
			if(isNotUrl(url))
			{
				return null;
			}
			Request request = new Request.Builder()
					.url(url.replaceAll(" ", ""))
					.post(formBody)
					.build();
			Response response;
			 response = mOkHttpClient.newCall(request).execute();
			 ResposeResult resposeResult = new ResposeResult(response.isSuccessful(),response.body().string());
			return resposeResult;
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}


	public ResposeResult postJson(String url , String formBody) {


		try {
			if(url == null)
			{
				return null;
			}
			if(url.length() < 6)
			{
				return null;
			}
			if(!(url.contains("http://")||url.contains("https://")))
			{
				return null;
			}
			if(isNotUrl(url))
			{
				return null;
			}
			String urla = url.replaceAll(" ","");
			if(urla.length() == 0)
			{
				return null;
			}
			Request request = new Request.Builder()
					.url(urla)
					.post(RequestBody.create(MEDIA_TYPE_MARKDOWN, formBody))
					.build();
			Response response;
			response = mOkHttpClient.newCall(request).execute();
			String head = response.headers().toString();
			String head2 = response.request().headers().toString();
			String url1 = response.request().url().toString();
			String res = response.request().toString();
			ResposeResult resposeResult = new ResposeResult(response.isSuccessful(),response.body().string());
			return resposeResult;
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}




	
	public Call get(String url) {

		if(url.length() < 6)
		{
			return null;
		}
		if(!(url.contains("http://")||url.contains("https://")))
		{
			return null;
		}
		if(isNotUrl(url))
		{
			return null;
		}
		Request request = new Request.Builder().url(url.replaceAll(" ","")).build();
		return mOkHttpClient.newCall(request);
	}
	
	public ResposeResult getUrl(Call mCall)
	{
		Response response;
		try {
			response = mCall.execute();
			ResposeResult resposeResult = new ResposeResult(response.isSuccessful(),response.body().string());
			return resposeResult;
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}



	
	public InputStream getStresm(String url)
	{


		try {
			if(url.length() < 6)
			{
				return null;
			}
			if(!(url.contains("http://")||url.contains("https://")))
			{
				return null;
			}
			if(isNotUrl(url))
			{
				return null;
			}

			Request request = new Request.Builder().url(url.replaceAll(" ","")).build();
			Response response;
			response = mOkHttpClient.newCall(request).execute();
			return response.body().byteStream();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public long getfile(String iurl)
	{
		if(iurl.length() < 6)
		{
			return 0;
		}
		if(!(iurl.contains("http://")||iurl.contains("https://")))
		{
			return 0;
		}
		URL url = null;
		String fileUrl = iurl;
		try {
			url = new URL(fileUrl.replaceAll(" ",""));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		HttpURLConnection urlcon = null;
		try {
			urlcon = (HttpURLConnection) url.openConnection();
			long fileLength = urlcon.getContentLength();
			return fileLength;
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	}


	public static Boolean checkNetWorkState(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getApplicationContext().getSystemService(
						Context.CONNECTIVITY_SERVICE);

		if (manager == null) {
			return false;
		}
		NetworkInfo networkinfo = manager.getActiveNetworkInfo();
		if (networkinfo == null || !networkinfo.isAvailable()) {
			return false;
		}

		return true;
	}

	public static int getNetType(Context context)
	{
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);//获取系统的连接服务
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();//获取网络的连接情况
		if(activeNetInfo.getType()==ConnectivityManager.TYPE_WIFI){
			//判断WIFI网
		}else if(activeNetInfo.getType()==ConnectivityManager.TYPE_MOBILE) {
			//判断3G网
		}
		return activeNetInfo.getType();
	}

	public static RequestBody initRepuestBody(ArrayList<NameValuePair> nameValuePairs) {
		MultipartBody.Builder builder = new MultipartBody.Builder();
		builder.setType(MultipartBody.FORM);
		for(int i = 0 ; i < nameValuePairs.size() ; i++) {
			NameValuePair mNameValuePair = nameValuePairs.get(i);
			if(mNameValuePair.isFile)
			{
				File file = new File(mNameValuePair.path);
				RequestBody fileBody = RequestBody.create(NetUtils.MEDIA_TYPE_MARKDOWN, file);
				builder.addFormDataPart(mNameValuePair.key, mNameValuePair.value, fileBody);
			}
			else
			{
				builder.addFormDataPart(mNameValuePair.key, mNameValuePair.value);
			}

		}
		return builder.build();
	}

	public static boolean initRepuestBodyUpload(ArrayList<NameValuePair> nameValuePairs,String url,ProgressRequestListener progressRequestListener) {

		try {
			MultipartBody.Builder builder = new MultipartBody.Builder();
			builder.setType(MediaType.parse("multipart/form-data"));
			for(int i = 0 ; i < nameValuePairs.size() ; i++) {
				NameValuePair mNameValuePair = nameValuePairs.get(i);
				if(mNameValuePair.isFile)
				{
					File file = new File(mNameValuePair.path);
					RequestBody fileBody = RequestBody.create(NetUtils.MEDIA_TYPE_MARKDOWN, file);
					builder.addFormDataPart(mNameValuePair.key, mNameValuePair.value, fileBody);
				}
				else
				{
					builder.addFormDataPart(mNameValuePair.key, mNameValuePair.value);
				}

			}

			RequestBody formBody =  builder.build();
			Request request = new Request.Builder()
					.url(url)
					.post(addProgressRequestListener(formBody, progressRequestListener)).build();

			Response response;
			mDocumentUpdataCall = NetUtils.mOkHttpClient.newCall(request);
			response = mDocumentUpdataCall.execute();
			if(response.isSuccessful())
			{
				return  true;
			}
			else
			{
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void stopDocumentUpCall() {
		if(mDocumentUpdataCall != null)
		{
			mDocumentUpdataCall.cancel();
			mDocumentUpdataCall = null;
		}

	}

	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements(); ) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			return "";
		}


		return "";
	}

	public static ProgressRequestBody addProgressRequestListener(RequestBody requestBody,ProgressRequestListener progressRequestListener){
		//包装请求体
		return new ProgressRequestBody(requestBody,progressRequestListener);
	}


	public static boolean isNotUrl(String str) {
		Pattern pattern = Pattern.compile("(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]");
		Matcher isUrl = pattern.matcher(str);
		if(!isUrl.matches() ){
			return true;
		}else{
			return false;
		}
	}

}
