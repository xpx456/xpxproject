package intersky.json;

import org.json.JSONException;

public class XpxJSONArray {

    public org.json.JSONArray jsonArray;

    public XpxJSONArray(String json) throws JSONException {
        jsonArray = new org.json.JSONArray(json);
    }

    public XpxJSONArray(org.json.JSONArray jsonArray){
        this.jsonArray = jsonArray;
    }

    public XpxJSONObject getJSONObject(int pos) throws JSONException {
        return new XpxJSONObject(jsonArray.getJSONObject(pos));
    }

    public String getString(int pos) throws JSONException {
        return new String(jsonArray.getString(pos));
    }

    public int length()
    {
        return jsonArray.length();
    }

    public boolean isNull(int pos)
    {
        return jsonArray.isNull(pos);
    }

    @Override
    public String toString() {
        if(jsonArray != null)
        return jsonArray.toString();
        else
            return "";
    }
}
