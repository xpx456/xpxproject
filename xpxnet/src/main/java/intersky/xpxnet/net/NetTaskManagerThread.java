package intersky.xpxnet.net;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import intersky.xpxnet.net.nettask.NetTask;


public class NetTaskManagerThread implements Runnable {

	public NetTaskManager mNetTaskManager;

	// 创建一个可重用固定线程数的线程池
	private ExecutorService mPool;
	// 线程池大小
	private final int POOL_SIZE = 4;
	// 轮询时间
	private final int SLEEP_TIME = 1000;
	// 是否停止
	private boolean isStop = false;



	public NetTaskManagerThread() {
		mNetTaskManager = NetTaskManager.getInstance();
		mPool = Executors.newFixedThreadPool(POOL_SIZE);

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (!isStop) {
			NetTask mNetTask = mNetTaskManager.getNetTask();
			if (mNetTask != null) {
				if(!mNetTaskManager.threadHash.containsKey(mNetTask.mRecordId))
				{
					mNetTask.endCallback = endCallback;
					mNetTaskManager.threadHash.put(mNetTask.mRecordId,mNetTask);
					mPool.execute(mNetTask);
				}
				else
				{
					continue;
				}

			} else {  //如果当前未有downloadTask在任务队列中
				try {
					// 查询任务完成失败的,重新加载任务队列
					// 轮询,
					Thread.sleep(SLEEP_TIME);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		if (isStop) {
			mPool.shutdown();
		}

	}

	/**
	 * @param isStop
	 *            the isStop to set
	 */
	public void setStop(boolean isStop) {
		this.isStop = isStop;
	}

	public EndCallback endCallback = new EndCallback() {
		@Override
		public void doremove(String mRecordId) {
			if(mRecordId != null)
			{
				if(mNetTaskManager.threadHash.containsKey(mRecordId))
					mNetTaskManager.threadHash.remove(mRecordId);
			}
		}
	};


}
