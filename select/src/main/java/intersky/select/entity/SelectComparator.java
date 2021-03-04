package intersky.select.entity;

import java.util.Arrays;
import java.util.Comparator;

import intersky.appbase.entity.Contacts;

public class SelectComparator implements Comparator {

    @Override
    public int compare(Object mMailPersonItem1, Object mMailPersonItem2) {
        // TODO Auto-generated method stub
        String[] array = new String[2];

        array[0] = ((Select) mMailPersonItem1).mPingyin;
        array[1] = ((Select) mMailPersonItem2).mPingyin;
        if (array[0].equals(array[1])) {
            return 0;
        }
        Arrays.sort(array);
        if (array[0].equals(((Select) mMailPersonItem1).mPingyin)) {
            return -1;
        } else if (array[0].equals(((Select) mMailPersonItem2).mPingyin)) {
            return 1;
        }
        return 0;
    }
}
