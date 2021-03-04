package intersky.mywidget;

import java.util.ArrayList;
import java.util.HashMap;

public class ClendarDay {
    public ArrayList<ClendarEvent> clendarEvents = new ArrayList<ClendarEvent>();
    public HashMap<String,ClendarEvent> hashMap = new HashMap<String,ClendarEvent>();

    public void add(ClendarEvent clendarEvent) {
        if(!hashMap.containsKey(clendarEvent.eventId))
        {
            hashMap.put(clendarEvent.eventId,clendarEvent);
            clendarEvents.add(clendarEvent);
        }
    }

    public void remove(String id) {
        ClendarEvent clendarEvent = hashMap.get(id);
        if(clendarEvent != null)
        {
            clendarEvents.remove(clendarEvent);
            hashMap.remove(id);
        }
    }
}
