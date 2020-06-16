package intersky.guide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import intersky.appbase.MySimpleTarget;
import intersky.apputils.GlideUtils;
import intersky.guide.entity.GuidePic;
import intersky.guide.view.activity.GuideActivity;

public class GuideUtils {

    public String PACKNAME = "";
    public static GuideUtils mGuideUtils;
    public Context context;
    public Intent nextIntent = null;
    public String nextActivity = "";
    public ArrayList<GuidePic> pics = new ArrayList<GuidePic>();
    public static void init(Context context) {
        mGuideUtils = new GuideUtils(context);
    }

    public GuideUtils(Context context) {
        this.context = context;
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
            this.PACKNAME = context.getPackageName()+info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static synchronized GuideUtils getInstance() {
        return mGuideUtils;
    }

    public boolean checkGuide() {
        SharedPreferences sharedPre = context.getSharedPreferences(context.getPackageName(), 0);
        boolean show = true;
        if(sharedPre.contains(this.PACKNAME))
        {
            show = sharedPre.getBoolean(this.PACKNAME,false);
        }
        return show;
    }

    public void setGuide() {
        SharedPreferences sharedPre = context.getSharedPreferences(context.getPackageName(), 0);
        SharedPreferences.Editor e = sharedPre.edit();
        e.putBoolean(PACKNAME, false);
        e.commit();
    }

    public void startGuide(ArrayList<GuidePic> pics,Context context,String activity,Intent intent) {
        nextIntent = intent;
        nextActivity = activity;
        this.pics.addAll(pics);
        if(checkGuide()) {
            Intent intent1 = new Intent(context, GuideActivity.class);
            context.startActivity(intent1);
        }
        else
        {
            try {
                intent.setClass(context,Class.forName(activity));
                context.startActivity(intent);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void startNext(Context context) {
        setGuide();
        try {
            nextIntent.setClass(context,Class.forName(nextActivity));
            context.startActivity(nextIntent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setImage(ImageView image,GuidePic pic,Context context) {
        if(pic.resid > 0)
        {
            image.setImageResource(pic.resid);
        }
        else if(pic.path.length() > 0)
        {

            Glide.with(context).load(new File(pic.path)).into(image);
        }
        else
        {
            //image.setImageResource(R.drawable.guide);
        }
    }


}
