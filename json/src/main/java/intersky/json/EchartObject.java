package intersky.json;

import java.util.HashMap;
import java.util.Map;

public class EchartObject {

    public HashMap<String,EchartObject> objects = new HashMap<String,EchartObject>();
    public HashMap<String,EchartArray> arrays = new HashMap<String,EchartArray>();
    public HashMap<String,String> strings = new HashMap<String,String>();
    public HashMap<String,Double> doubles = new HashMap<String,Double>();
    public void put(String name,EchartObject echartObject) {
        objects.put(name,echartObject);
    }

    public void put(String name,EchartArray echartArray) {
        arrays.put(name,echartArray);
    }

    public void put(String name,String string) {
        strings.put(name,string);
    }

    public void put(String name,Double data) {
        doubles.put(name,data);
    }

    public String toString() {
        String json = "";
        String result = "";
        for (Map.Entry<String, String> entry : strings.entrySet())
        {
            String name = entry.getKey();
            String obj = entry.getValue();
            if(json.length() == 0)
            {
                json += "'"+name+"'"+":'"+obj+"'";
            }
            else
            {
                json += ",'"+name+"'"+":'"+obj+"'";
            }
        }

        for (Map.Entry<String, Double> entry : doubles.entrySet())
        {
            String name = entry.getKey();
            Double obj = entry.getValue();
            if(json.length() == 0)
            {
                json += "'"+name+"'"+":"+obj;
            }
            else
            {
                json += ",'"+name+"'"+":"+obj;
            }
        }

        for (Map.Entry<String, EchartObject> entry : objects.entrySet())
        {
            String name = entry.getKey();
            EchartObject obj = entry.getValue();
            if(json.length() == 0)
            {
                json += "'"+name+"'"+":"+obj.toString();
            }
            else
            {
                json += ",'"+name+"'"+":"+obj.toString();
            }
        }
        for (Map.Entry<String, EchartArray> entry : arrays.entrySet())
        {
            String name = entry.getKey();
            EchartArray obj = entry.getValue();
            if(json.length() == 0)
            {
                json += "'"+name+"'"+":"+obj.toString();
            }
            else
            {
                json += ",'"+name+"'"+":"+obj.toString();
            }
        }
        result = "{"+json+"}";
        return result;
    }
}
