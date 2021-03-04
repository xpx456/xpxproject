package com.dk.dkhome.utils;

import com.dk.dkhome.R;
import com.dk.dkhome.view.DkhomeApplication;

public class Utils {

    public static void getSex(int id) {
        if(id == 0)
        {
            DkhomeApplication.mApp.getString(R.string.register_male);
        }
        else
        {
            DkhomeApplication.mApp.getString(R.string.register_female);
        }
    }

}
