package intersky.appbase.entity;

import java.util.HashMap;

public class NameMap {

    HashMap<String ,String > mNameMap = new HashMap<String ,String >();

    public void put(String key,String value) {
        mNameMap.put(key,value);
    }

    public String get(String key) {
        if(mNameMap.containsKey(key))
        {
            return mNameMap.get(key);
        }
        else
        {
            return "unknow";
        }
    }
}
