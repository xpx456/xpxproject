package intersky.echartoption;

import java.util.HashMap;
import java.util.Map;

public class ObjectData {
//instanceof
    public HashMap<String,Object> object = new HashMap<String,Object>();

    @Override
    public String toString() {
        String result = "";
        for(Map.Entry<String,Object> temp:object.entrySet())
        {
            if(result.length() == 0)
            {
                result+="{";
            }
            else
            {
                result +=",";
            }

            result+=temp.getKey()+":"+getValue(temp.getValue());
        }
        if(result.length() == 0)
            result += "{";
        result+="}";
        return result;
    }

    private String getValue(Object value) {
        if(value instanceof String)
        {
            return "'"+(String)value+"'";
        }
        else if(value instanceof FunctionData)
        {
            return value.toString();
        }
        else {
            return String.valueOf(value);
        }
    }

    public void put(String key,String value) {
        object.put(key,value);
    }

    public void put(String key,boolean value) {
        object.put(key,value);
    }

    public void put(String key,double value) {
        object.put(key,value);
    }

    public void put(String key,int value) {
        object.put(key,value);
    }

    public void put(String key,ObjectData value) {
        object.put(key,value);
    }

    public void put(String key,ArrayData value) {
        object.put(key,value);
    }

    public void put(String key,FunctionData value) {
        object.put(key,value);
    }
}
