package intersky.json;

import java.util.ArrayList;
import java.util.Map;

public class EchartArray {

    public ArrayList<ArrayItem> items = new ArrayList<ArrayItem>();


    public void put(EchartObject echartObject) {
        items.add(new ArrayItem(0,echartObject));
    }

    public void put(EchartArray echartArray) {
        items.add(new ArrayItem(1,echartArray));
    }

    public void put(String string) {
        items.add(new ArrayItem(2,string));
    }

    public void put(Double data) {
        items.add(new ArrayItem(3,data));
    }

    public String toString() {
        String json = "";
        String result = "";
        for(int i = 0 ; i < items.size() ; i++) {
            if(json.length() == 0)
            {
                json += items.get(i).getdata();
            }
            else
            {
                json += ","+items.get(i).getdata();
            }
        }
        result = "["+json+"]";
        return result;
    }

    public class ArrayItem {

        public int type = 0;

        public EchartObject objects;
        public EchartArray arrays;
        public String strings;
        public Double doubles;

        public ArrayItem(int type,Object obj) {
            this.type = type;
            switch (type)
            {
                case 0:
                    objects = (EchartObject) obj;
                break;
                case 1:
                    arrays = (EchartArray) obj;
                    break;
                case 2:
                    strings = (String) obj;
                    break;
                case 3:
                    doubles = (Double) obj;
                    break;
            }

        }

        public String getdata() {
            if(type == 0 )
            {
                return objects.toString();
            }
            else if(type == 1)
            {
                return arrays.toString();
            }
            else if(type == 2)
            {
                return "'"+strings.toString()+"'";
            }
            else
            {
                return doubles.toString();
            }
        }

    }
}
