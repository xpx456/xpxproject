package intersky.mail;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html.ImageGetter;
import android.util.Log;
import android.widget.EditText;

import java.io.InputStream;

import intersky.xpxnet.net.NetUtils;

public class URLImageParser implements ImageGetter
{
	Context c;
	EditText container;

	/***
	 * 构建URLImageParser将执行AsyncTask,刷新容器
	 * 
	 * @param t
	 * @param c
	 */
	public URLImageParser(EditText t, Context c)
	{
		this.c = c;
		this.container = t;
	}

	public Drawable getDrawable(String source)
	{
		URLDrawable urlDrawable = new URLDrawable();

		// 获得实际的源
		ImageGetterAsyncTask asyncTask = new ImageGetterAsyncTask(urlDrawable);

		asyncTask.execute(source);

		// 返回引用URLDrawable我将改变从src与实际图像标记
		return urlDrawable;
	}

	public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable>
	{
		URLDrawable urlDrawable;

		public ImageGetterAsyncTask(URLDrawable d)
		{
			this.urlDrawable = d;
		}

		@Override
		protected Drawable doInBackground(String... params)
		{
			String source = params[0];
			return fetchDrawable(source);
		}

		@Override
		protected void onPostExecute(Drawable result)
		{
			// 设置正确的绑定根据HTTP调用的结果
			if (result != null)
			{
				Log.d("height", "" + result.getIntrinsicHeight());
				Log.d("width", "" + result.getIntrinsicWidth());
				urlDrawable.setBounds(0, 0, 0 + result.getIntrinsicWidth(), 0 + result.getIntrinsicHeight());

				// 改变当前可提取的参考结果从HTTP调用
				urlDrawable.drawable = result;

				// 绘制图像容器
				URLImageParser.this.container.invalidate();

				// For ICS
				URLImageParser.this.container.setHeight((URLImageParser.this.container.getHeight() + result.getIntrinsicHeight()));

				// Pre ICS
				URLImageParser.this.container.setEllipsize(null);
			}

		}

		/***
		 * 得到Drawable的URL
		 * 
		 * @param urlString
		 * @return
		 */
		public Drawable fetchDrawable(String urlString)
		{
			try
			{
				InputStream is = fetch(urlString);
				Drawable drawable = Drawable.createFromStream(is, "src");
				drawable.setBounds(0, 0, 0 + drawable.getIntrinsicWidth(), 0 + drawable.getIntrinsicHeight());
				return drawable;
			}
			catch (Exception e)
			{
				return null;
			}
		}

		private InputStream fetch(String urlString)
		{
			String String;
			if(urlString.contains("/Mail/Sources/"))
			{
				String = NetUtils.getInstance().praseUrl(MailManager.getInstance().service,urlString);
				String = urlString;
			}
			else
			{
				String = urlString;
			}
			return NetUtils.getInstance().getStresm(String);
			
		}
	}

	
}
