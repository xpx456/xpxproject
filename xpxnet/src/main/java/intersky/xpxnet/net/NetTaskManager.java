package intersky.xpxnet.net;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import intersky.xpxnet.net.nettask.NetTask;

public class NetTaskManager {
	// UI请求队列
	private LinkedList<NetTask> mNetTasks;
	// 任务不能重复
	private Set<String> taskIdSet;
	private static NetTaskManager mNetTaskMananger;
	private HashMap<String,LinkedList<NetTask>> mHashNetTasks;
	private HashMap<String,Set<String>> mHasTaskIdSet;
	public HashMap<String,NetTask> threadHash = new HashMap<String,NetTask>();
	public HashMap<String,HashMap<String,NetTask>> threadHashHash = new HashMap<String,HashMap<String,NetTask>>();
	private NetTaskManager() {

		mNetTasks = new LinkedList<NetTask>();
		mHashNetTasks = new HashMap<String,LinkedList<NetTask>>();
		taskIdSet = new HashSet<String>();
		mHasTaskIdSet = new HashMap<String,Set<String>>();
	}

	public static synchronized NetTaskManager getInstance() {
		if (mNetTaskMananger == null) {
			mNetTaskMananger = new NetTaskManager();
		}
		return mNetTaskMananger;
	}

	public void clean() {
		taskIdSet.clear();
		mNetTasks.clear();
		mHashNetTasks.clear();
		mHasTaskIdSet.clear();
		threadHash.clear();
		threadHashHash.clear();
	}

	//1.先执行
	public void addNetTask(NetTask mNetTask) {
		synchronized (mNetTasks) {
			if (!isTaskRepeat(mNetTask.mRecordId) ) {
				if(!threadHash.containsKey(mNetTask.mRecordId))
				mNetTasks.addLast(mNetTask);
			}
		}
	}

	public void addNetTask(NetTask mNetTask,String name) {
		synchronized (mNetTasks) {

			if(mHashNetTasks.containsKey(name))
			{
				LinkedList<NetTask> mNetTasks = mHashNetTasks.get(name);
				HashMap<String, NetTask> hash = threadHashHash.get(name);
				if(hash == null)
				{
					hash = new HashMap<String, NetTask>();
					threadHashHash.put(name,hash);

				}
				if(!isTaskRepeat(mNetTask.mRecordId,name))
				{
					if(!hash.containsKey(mNetTask.mRecordId))
					mNetTasks.add(mNetTask);
				}
			}
			else
			{
				LinkedList<NetTask> mNetTasks = new LinkedList<NetTask>();
				mHashNetTasks.put(name,mNetTasks);
				HashMap<String, NetTask> hash = threadHashHash.get(name);
				if(hash == null)
				{
					hash = new HashMap<String, NetTask>();
					threadHashHash.put(name,hash);
				}
				if(!isTaskRepeat(mNetTask.mRecordId,name))
				{
					if(!hash.containsKey(mNetTask.mRecordId))
						mNetTasks.add(mNetTask);
				}

			}
		}
	}

	public boolean isTaskRepeat(String mRecordId) {
		synchronized (taskIdSet) {
			if (taskIdSet.contains(mRecordId)) {
				return true;
			} else {
				taskIdSet.add(mRecordId);
				return false;
			}
		}
	}

	public boolean isTaskRepeat(String mRecordId,String name) {
		synchronized (mHasTaskIdSet) {
			Set<String> ttaskIdSet = mHasTaskIdSet.get(name);
			if(ttaskIdSet == null)
			{
				ttaskIdSet = new HashSet<String>();
				mHasTaskIdSet.put(name,ttaskIdSet);
			}
			if (ttaskIdSet.contains(mRecordId)) {
				return true;
			} else {
				ttaskIdSet.add(mRecordId);
				return false;
			}
		}
	}
	
	public NetTask getNetTask() {
		synchronized (mNetTasks) {
			if (mNetTasks.size() > 0) {
				NetTask mNetTask = mNetTasks.removeFirst();
				return mNetTask;
			}
		}
		return null;
	}

	public NetTask getNetTask(String name) {
		synchronized (mHashNetTasks) {
			LinkedList<NetTask> tasks = mHashNetTasks.get(name);
			if(tasks == null)
			{
				tasks = new LinkedList<NetTask>();
				mHashNetTasks.put(name,tasks);
			}
			if (tasks.size() > 0) {
				NetTask mNetTask = tasks.removeFirst();
				return mNetTask;
			}
		}
		return null;
	}
}
