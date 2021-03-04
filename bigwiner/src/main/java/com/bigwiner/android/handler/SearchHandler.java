package com.bigwiner.android.handler;

import android.os.Handler;
import android.os.Message;

import com.bigwiner.android.view.activity.SearchActivity;

//05

public class SearchHandler extends Handler {

    public SearchActivity theActivity;
    public SearchHandler(SearchActivity mSearchActivity) {
        theActivity = mSearchActivity;
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
        }

    }
}
