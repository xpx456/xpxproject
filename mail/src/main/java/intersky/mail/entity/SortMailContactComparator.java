package intersky.mail.entity;

import java.util.Arrays;
import java.util.Comparator;

public class SortMailContactComparator implements Comparator {

    @Override
    public int compare(Object mMailPersonItem1, Object mMailPersonItem2) {
        // TODO Auto-generated method stub
        String[] array = new String[2];

        array[0] = ((MailContact) mMailPersonItem1).pingyin;
        array[1] = ((MailContact) mMailPersonItem2).pingyin;
        if (array[0].equals(array[1])) {
            return 0;
        }
        Arrays.sort(array);
        if (array[0].equals(((MailContact) mMailPersonItem1).pingyin)) {
            return -1;
        } else if (array[0].equals(((MailContact) mMailPersonItem2).pingyin)) {
            return 1;
        }
        return 0;
    }
}
