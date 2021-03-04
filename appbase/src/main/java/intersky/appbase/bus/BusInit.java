package intersky.appbase.bus;

import android.text.TextUtils;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import intersky.appbase.bundle.BundleConfigFactory;
import intersky.appbase.bundle.BundleConfigModel;
import intersky.appbase.utils.LogUtil;

/**
 * @author Zhenhua on 2017/7/11.
 * @email zhshan@ctrip.com ^.^
 */
public class BusInit implements BusObjectProvider {

    private HashMap<String, BusObject> hashMap = new HashMap<>();

    @Override
    public boolean register(BusObject busObject) {
        if (busObject == null) {
            return false;
        }

        String host = busObject.getHost().toLowerCase();
        if (hashMap.containsKey(host)) {
        }
        hashMap.put(host, busObject);
        return true;
    }

    public static BusObject registerBusObjectWithHost(String host) {
        if (TextUtils.isEmpty(host)) {
            return null;
        }
        BundleConfigModel bundleConfigModel = BundleConfigFactory.getBundleConfigModelByModuleName(host);
        if (null == bundleConfigModel) {
            return null;
        }
        BusObject retObj = null;
        try {
            String className = bundleConfigModel.busObjectName;
            Class<BusObject> clazz = (Class<BusObject>) Class.forName(className);
            Constructor<BusObject> constructor = clazz.getConstructor(String.class);
            BusObject tmpRetObj = constructor.newInstance(host);
            if (Bus.register(tmpRetObj)) {
                retObj = tmpRetObj;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retObj;
    }

    @Override
    public BusObject findBusObject(String host) {
        if (TextUtils.isEmpty(host)) {
            return null;
        }

        BusObject object = hashMap.get(host.toLowerCase());
        if (object == null) {
            object = registerBusObjectWithHost(host);
        }
        return object;
    }
}
