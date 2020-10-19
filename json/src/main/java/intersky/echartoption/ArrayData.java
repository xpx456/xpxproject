package intersky.echartoption;

import java.util.ArrayList;
import java.util.Map;

public class ArrayData {

    public ArrayList<Object> array = new ArrayList<Object>();

    @Override
    public String toString() {
        String result = "";
        for(Object temp : array)
        {
            if(result.length() == 0 )
            {
                result += "[";
            }
            else
            {
                result += ",";
            }
            result += getValue(temp);
        }
        if(result.length() == 0)
            result += "[";
        result+="]";
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

    public void add(String value) {
        array.add(value);
    }

    public void add(boolean value) {
        array.add(value);
    }

    public void add(double value) {
        array.add(value);
    }

    public void add(int value) {
        array.add(value);
    }

    public void add(ObjectData value) {
        array.add(value);
    }

    public void add(ArrayData value) {
        array.add(value);
    }

    public void add(FunctionData value) {
        array.add(value);
    }
}
