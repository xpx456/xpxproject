package intersky.mywidget;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import intersky.apputils.AppUtils;


/**
 * 自定义的View，实现ListView A~Z快速索引效果
 * 
 * @author Folyd
 * 
 */
public class MySlideBar extends View {
	private Paint paint = new Paint();
	private OnTouchLetterChangeListenner listenner;
	// 是否画出背景
	private boolean showBg = false;
	// 选中的项
	private int choose = -1;
	// 准备好的A~Z的字母数组
	public static String[] letters = { "A", "B", "C", "D", "E", "F", "G",
			"H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
			"U", "V", "W", "X", "Y", "Z" };
//	public  ArrayList<ContactModel> addletters = new ArrayList<ContactModel>();

	public static String[] addletters = new String[0];

	public DisplayMetrics metric;
	// 构造方法
	public MySlideBar(Context context) {
		super(context);
		metric = AppUtils.getWindowInfo(context);
	}

	public MySlideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		metric = AppUtils.getWindowInfo(context);
	}

	public TextView mletterView;
	public RelativeLayout mRelativeLayout;
	public int startHeight;
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 获取宽和高
		int width = getWidth();
		int height = getHeight() - 30;
		// 每个字母的高度
		int singleHeight = height / letters.length;
		startHeight =( height-singleHeight*addletters.length)/2;
//		int tempHeight = ( height-singleHeight*addletters.size())/(addletters.size()-1);
		if (showBg) {
			// 画出背景
			//canvas.drawColor(Color.parseColor("#55000000"));
		}
		// 画字母
		for (int i = 0; i < addletters.length; i++) {
			paint.setColor(Color.argb(255, 91, 174, 226));
			// 设置字体格式
			paint.setTypeface(Typeface.MONOSPACE);
			paint.setAntiAlias(true);
			paint.setTextSize(12* metric.density);
			// 如果这一项被选中，则换一种颜色画
			if (i == choose) {
				paint.setColor(Color.parseColor("#3399ff"));
				paint.setFakeBoldText(true);
			}
			// 要画的字母的x,y坐标
			float posX = width / 2 - paint.measureText(addletters[i]) / 2;
			float posY = startHeight+i * singleHeight + singleHeight;
//			float posY = tempHeight*i+i * singleHeight + singleHeight;
			// 画出字母
			canvas.drawText(addletters[i], posX, posY, paint);
			// 重新设置画笔
			paint.reset();
		}
	}


//	public ArrayList<ContactModel> getAddletters() {
//		return addletters;
//	}


		public void setAddletters(String[] addletters) {
		this.addletters = addletters;
	}
	/**
	 * 处理SlideBar的状态
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final float y = event.getY();
		// 算出点击的字母的索引
		final int index = (int) ((y - startHeight)/ (getHeight() - startHeight*2)* this.addletters.length);
		// 保存上次点击的字母的索引到oldChoose
		if(index < 0 || index > addletters.length-1)
		{
			return true;
		}
		final int oldChoose = choose;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			showBg = true;
			if (oldChoose != index && listenner != null && index >= 0
					&& index < this.addletters.length) {
				choose = index;
				listenner.onTouchLetterChange(event, index);
				invalidate();
			}
			if(mRelativeLayout != null )
			{
				mletterView.setText(this.addletters[index]);
				mRelativeLayout.setVisibility(View.VISIBLE);
			}
			break;

		case MotionEvent.ACTION_MOVE:
//			if (oldChoose != index && listenner != null && index >= 0
//					&& index < addletters.size()) {
//				choose = index;
//				listenner.onTouchLetterChange(event, letters[index]);
//				invalidate();
//			}
			break;
		case MotionEvent.ACTION_UP:
		default:
			showBg = false;
			choose = -1;
			if (listenner != null && index >= 0 && index < letters.length)
				listenner.onTouchLetterChange(event, index);
			invalidate();

			if(mRelativeLayout != null )
			{
				mRelativeLayout.setVisibility(View.INVISIBLE);
			}
			break;
		}
		return true;
	}

	/**
	 * 回调方法，注册监听器
	 * 
	 * @param listenner
	 */
	public void setOnTouchLetterChangeListenner(
			OnTouchLetterChangeListenner listenner) {
		this.listenner = listenner;
	}

	/**
	 * SlideBar 的监听器接口
	 * 
	 * @author Folyd
	 * 
	 */
	public interface OnTouchLetterChangeListenner {

		void onTouchLetterChange(MotionEvent event, int position);
	}

	public void setMletterView(TextView mletterView) {
		this.mletterView = mletterView;
	}

	public void setmRelativeLayout(RelativeLayout mRelativeLayout) {
		this.mRelativeLayout = mRelativeLayout;
	}
}
