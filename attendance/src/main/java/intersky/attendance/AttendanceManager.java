package intersky.attendance;

import android.content.Context;


import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;

import java.util.ArrayList;

import intersky.appbase.entity.Account;
import intersky.appbase.entity.Contacts;
import intersky.apputils.Onlocation;
import intersky.oa.OaUtils;
import xpx.map.MapManager;

public class AttendanceManager {

	public static final String  ACTION_UPDATE_ATTANDENCE_SET_LIST = "ACTION_UPDATE_ATTANDENCE_SET_LIST";
	public Contacts setContact;
	public static Context context;
	public volatile static AttendanceManager mAttendance;
	public OaUtils oaUtils;
	public MapManager mapManager;
	public static synchronized AttendanceManager init(OaUtils oaUtils,Context context, MapManager mapManager) {

		if (mAttendance == null) {
			synchronized (OaUtils.class) {
				if (mAttendance == null) {
					mAttendance = new AttendanceManager(oaUtils,context, mapManager);
				}
				else
				{
					mAttendance.context = context;
					mAttendance.mapManager = mapManager;
					mAttendance.oaUtils = oaUtils;
				}
			}
		}
		return mAttendance;
	}

	public static  AttendanceManager getInstance() {
		return mAttendance;
	}

	public AttendanceManager(OaUtils oaUtils, Context context, MapManager mapManager) {
		this.oaUtils = oaUtils;
		this.context = context;
		this.mapManager = mapManager;
	}
	
	//29.883859,121.571892
	//浙江省宁波市江东区规划二路靠近浅绿小院
	//浅绿小院
	public String getSetUserid() {
		if(setContact == null) {
			return oaUtils.mAccount.mRecordId;
		}
		else {
			return setContact.mRecordid;
		}
	}

	public static String initDayString(String dayid) {
		String a = "";
		if (dayid.length() != 0) {
			if (dayid.equals("0,1,2,3,4,5,6") || dayid.equals("1,2,3,4,5,6,0")) {
				a = context.getString(R.string.every_day);
			} else if (dayid.equals("1,2,3,4,5")) {
				a = context.getString(R.string.work_day);;
			}
		}

		return a;
	}

	public static String getweek(int id) {
		String toast = "";
		switch (id) {
			case 0:
				toast = context.getString(R.string.keyword_sun_e);
				break;
			case 1:
				toast = context.getString(R.string.keyword_mon_e);
				break;
			case 2:
				toast = context.getString(R.string.keyword_tue_e);
				break;
			case 3:
				toast = context.getString(R.string.keyword_wen_e);
				break;
			case 4:
				toast = context.getString(R.string.keyword_ths_e);
				break;
			case 5:
				toast = context.getString(R.string.keyword_fri_e);
				break;
			case 6:
				toast = context.getString(R.string.keyword_sat_e);
				break;
		}
		return toast;
	}

//	public interface AttendanceFunctions{
//		void setUnderlineContacts(Context context,String text,String action);
//		String getMemberIds(ArrayList<Contacts> mCopyers);
//		void selectListContact(Context context,ArrayList<Contacts> selects,String action,String title,boolean all);
//		void getContacts(String records, ArrayList<Contacts> mContactModels);
//		AMapLocation getLastLocation();
//	}
}
