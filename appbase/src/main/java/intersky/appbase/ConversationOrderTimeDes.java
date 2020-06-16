package intersky.appbase;

import java.util.Arrays;
import java.util.Comparator;

import intersky.appbase.entity.Conversation;

public class ConversationOrderTimeDes implements Comparator {

        @Override
        public int compare(Object mMailPersonItem1, Object mMailPersonItem2) {
            // TODO Auto-generated method stub
            String[] array = new String[2];

            array[0] = ((Conversation) mMailPersonItem1).mTime;
            array[1] = ((Conversation) mMailPersonItem2).mTime;
            if (array[0].equals(array[1])) {
                return 0;
            }
            Arrays.sort(array);
            if (array[0].equals(((Conversation) mMailPersonItem1).mTime)) {
                return 1;
            } else if (array[0].equals(((Conversation) mMailPersonItem2).mTime)) {
                return -1;
            }
            return 0;
        }
    }