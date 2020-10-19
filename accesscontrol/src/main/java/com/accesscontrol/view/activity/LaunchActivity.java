package com.accesscontrol.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class LaunchActivity extends PadBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this,MainActivity.class);
        this.startActivity(intent);
        finish();
    }
}
