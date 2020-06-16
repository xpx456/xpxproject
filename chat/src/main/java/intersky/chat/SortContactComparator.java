package intersky.chat;

import java.util.Arrays;
import java.util.Comparator;

import intersky.appbase.entity.Contacts;

public class SortContactComparator implements Comparator {

    @Override
    public int compare(Object mMailPersonItem1, Object mMailPersonItem2) {
        // TODO Auto-generated method stub
        String[] array = new String[2];

        array[0] = ((Contacts) mMailPersonItem1).getmPingyin();
        array[1] = ((Contacts) mMailPersonItem2).getmPingyin();
        if (array[0].equals(array[1])) {
            return 0;
        }
        Arrays.sort(array);
        if (array[0].equals(((Contacts) mMailPersonItem1).getmPingyin())) {
            return -1;
        } else if (array[0].equals(((Contacts) mMailPersonItem2).getmPingyin())) {
            return 1;
        }
        return 0;
    }
}
