package intersky.json;

import org.json.JSONException;

public class XpxJSONObject {

    public org.json.JSONObject jsonObject;

    public XpxJSONObject(String json) throws JSONException {
        jsonObject = new org.json.JSONObject(json);
    }

    public XpxJSONObject(org.json.JSONObject jsonObject){
        this.jsonObject = jsonObject;
    }

    public XpxJSONObject()
    {
        jsonObject = new org.json.JSONObject();
    }

    public String getString(String id)
    {
        if(jsonObject.has(id))
        {
            try {
                if(!jsonObject.getString(id).equals("null"))
                return jsonObject.getString(id);
                else
                    return "";
            } catch (JSONException e) {
                e.printStackTrace();
                return "";
            }
        }
        return "";
    }

    public String getString(String id,String defult)
    {
        if(jsonObject.has(id))
        {
            try {
                if(!jsonObject.getString(id).equals("null") && jsonObject.getString(id).length() != 0)
                    return jsonObject.getString(id);
                else
                    return defult;
            } catch (JSONException e) {
                e.printStackTrace();
                return defult;
            }
        }
        return defult;
    }

    public int getInt(String id, int defult)
    {
        if(jsonObject.has(id))
        {
            try {
                return jsonObject.getInt(id);
            } catch (JSONException e) {
                e.printStackTrace();
                return defult;
            }
        }
        return defult;
    }

    public long getLong(String id, long defult)
    {
        if(jsonObject.has(id))
        {
            try {
                return jsonObject.getLong(id);
            } catch (JSONException e) {
                e.printStackTrace();
                return defult;
            }
        }
        return defult;
    }

    public boolean getBoolean(String id, boolean defult)
    {
        if(jsonObject.has(id))
        {
            try {
                return jsonObject.getBoolean(id);
            } catch (JSONException e) {
                e.printStackTrace();
                return defult;
            }
        }
        return defult;
    }

    public double getDouble(String id, double defult)
    {
        if(jsonObject.has(id))
        {
            try {
                return jsonObject.getDouble(id);
            } catch (JSONException e) {
                e.printStackTrace();
                return defult;
            }
        }
        return defult;
    }

    public boolean has(String key)
    {
        return jsonObject.has(key);
    }

    public int length() {
        return jsonObject.length();
    }

    public void put(String key, String obj) throws JSONException {
        jsonObject.put(key,obj);
    }

    public void put(String key, int obj) throws JSONException {
        jsonObject.put(key,obj);
    }

    public void put(String key, long obj) throws JSONException {
        jsonObject.put(key,obj);
    }

    public void put(String key, double obj) throws JSONException {
        jsonObject.put(key,obj);
    }

    public void put(String key, boolean obj) throws JSONException {
        jsonObject.put(key,obj);
    }

    public void put(String key, XpxJSONObject obj) throws JSONException {
        jsonObject.put(key,obj.jsonObject);
    }

    public void put(String key, XpxJSONArray obj) throws JSONException {
        jsonObject.put(key,obj.jsonArray);
    }

    public XpxJSONObject getJSONObject(String key) throws JSONException {
        return new XpxJSONObject(jsonObject.getJSONObject(key));
    };

    public XpxJSONArray getJSONArray(String key) throws JSONException {
        return new XpxJSONArray(jsonObject.getJSONArray(key));
    }

    public Object get(String name)throws JSONException
    {
        return jsonObject.get(name);
    }

    @Override
    public String toString() {
        if(jsonObject != null)
        return jsonObject.toString();
        else
            return "";
    }
}
