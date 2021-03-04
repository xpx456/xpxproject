package intersky.sign;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;

import java.util.ArrayList;

import intersky.appbase.entity.Account;
import intersky.appbase.entity.Attachment;
import intersky.appbase.entity.Contacts;
import intersky.apputils.Onlocation;
import intersky.oa.OaUtils;
import intersky.sign.asks.SignAsks;
import intersky.sign.handler.SignManagerHandler;
import xpx.map.MapManager;

public class SignManager {
	public static final String ACTION_AMPA_SET_ADDTESS = "ACTION_AMPA_SET_ADDTESS";
	public static final String ACTION_ADD_PICTURE = "add_picture";
	public static final String ACTION_DELETE_PICTURE = "delete_picture";
	public static final String ACTION_SIGN_SET_USAER = "attdence_set_user";
	public static final String ACTION_UPDATE_SIGN_COUNT = "ACTION_UPDATE_SIGN_COUNT";
	public static final int MAX_PIC_COUNT = 9;
	public LatLonPoint mLatLonPoint = new LatLonPoint(0,0);
	public ArrayList<PoiItem> mPoiItems = new ArrayList<PoiItem>();
	public String addressname = "";
	public String addressdetial = "";
	public int signHit = 0;
	public Contacts setContact;
	public volatile static SignManager mSignModul;
	public OaUtils oaUtils;
	public MapManager mapManager;
	public static SignManager init(OaUtils oaUtils,MapManager mapManager) {

		if (mSignModul == null) {
			synchronized (SignManager.class) {
				if (mSignModul == null) {
					mSignModul = new SignManager(oaUtils,mapManager);
				}
				else
				{
					mSignModul.mapManager = mapManager;
					mSignModul.oaUtils = oaUtils;
				}
			}
		}
		return mSignModul;
	}

	public static SignManager getInstance() {
		return mSignModul;
	}

	public SignManager(OaUtils oaUtils,MapManager mapManager) {
		this.mapManager = mapManager;
		this.oaUtils = oaUtils;
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

	public void getSignHit(Context context) {
		SignAsks.getSignHit(new SignManagerHandler(context), context, getSetUserid());
	}

}
