package intersky.conversation;

import java.util.Arrays;
import java.util.Comparator;

import intersky.appbase.entity.Conversation;

public class RegisterOrder implements Comparator {

        @Override
        public int compare(Object mMailPersonItem1, Object mMailPersonItem2) {
            // TODO Auto-generated method stub
            Conversation tem1 = (Conversation) mMailPersonItem1;
            Conversation tem2 = (Conversation) mMailPersonItem2;
            String[] array = new String[2];
            int cr = 0;
            int a = tem2.mPriority - tem1.mPriority;
            if (a != 0) {
                cr = (a > 0) ? 2 : -1;
            } else {
                //按薪水降序排列
                array[0] = ((Conversation) mMailPersonItem1).mTime;
                array[1] = ((Conversation) mMailPersonItem2).mTime;
                if (array[0].equals(array[1])) {
                    cr = 0;
                }
                Arrays.sort(array);
                if (array[0].equals(((Conversation) mMailPersonItem1).mTime)) {
                    cr =  -2;
                } else if (array[0].equals(((Conversation) mMailPersonItem2).mTime)) {
                     cr = 1;
                }
            }
            return cr;
        }
    }