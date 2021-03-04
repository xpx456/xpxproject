package intersky.appbase;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.security.Permission;
import java.util.ArrayList;

import intersky.apputils.AppUtils;

public class PermissionItem implements PermissionResult {

    public String permission = "";
    public ArrayList<String> permissions;
    public BaseActivity baseActivity;
    public boolean finish = false;
    public PermissionItem(String permission,BaseActivity baseActivity) {
        this.permission = permission;
        this.baseActivity = baseActivity;
    }

    public PermissionItem(String permission,BaseActivity baseActivity,boolean finish) {
        this.permission = permission;
        this.baseActivity = baseActivity;
        this.finish = finish;
    }

    public PermissionItem(String permission,BaseActivity baseActivity,ArrayList<String> permissions) {
        this.permission = permission;
        this.baseActivity = baseActivity;
        this.permissions = permissions;
    }

    public PermissionItem(String permission,BaseActivity baseActivity,ArrayList<String> permissions,boolean finish) {
        this.permission = permission;
        this.baseActivity = baseActivity;
        this.permissions = permissions;
        this.finish = finish;
    }

    @Override
    public void doResult(int code, int[] grantResults) {
        if(grantResults.length > 0)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(permissions != null && permission != null)
                {
                    for(int i = 0 ; i < permissions.size() ; i++)
                    {

                        if(permissions.get(i).equals(permission))
                        {
                            permissions.remove(i);
                        }
                    }
                    if(permissions.size() > 0)
                    {
                        if(baseActivity != null)
                                baseActivity.mBasePresenter.getPermission(permissions,finish);

                    }
                }

            } else {
                if(baseActivity != null)
                    baseActivity.mBasePresenter.getPermission(permissions,finish);
            }
        }
        else
        {
            if(baseActivity != null)
                    baseActivity.mBasePresenter.getPermission(permissions,false);
        }
    }
}
