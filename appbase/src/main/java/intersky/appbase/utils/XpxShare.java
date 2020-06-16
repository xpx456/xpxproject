package intersky.appbase.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import java.io.File;

import intersky.appbase.BaseActivity;
import intersky.appbase.entity.ShareItem;

public interface XpxShare {

    void doShare(BaseActivity context,String des, String keyword);
    void doShare(BaseActivity context,String des,String keyword,String url);
    void doShare(BaseActivity context,String des, String keyword, File file);
    void doShare(BaseActivity context,String des, String keyword, Bitmap bitmap);
    void doShare(BaseActivity context,String des, String keyword, int rid);
    void doShare(BaseActivity context,String des, String keyword, byte[] bytes);
    void onActivityResult(Context context,int requestCode, int resultCode, Intent data);
}
