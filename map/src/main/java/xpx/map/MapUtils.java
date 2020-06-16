package xpx.map;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.amap.api.maps2d.model.LatLng;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import intersky.apputils.AppUtils;
import intersky.apputils.MenuItem;

public class MapUtils {

    public static final String PN_GAODE_MAP = "com.autonavi.minimap";// 高德地图包名

    public static final String PN_BAIDU_MAP = "com.baidu.BaiduMap"; // 百度地图包名

    public static final String PN_TENCENT_MAP = "com.tencent.map"; // 腾讯地图包名

    /**
     * 检查地图应用是否安装
     *
     * @return
     */

    public static boolean isAvilible(Context context, String packageName) {
//获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
//获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
//用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
//从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
//判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    public static List<String> mapsList() {
        List<String> maps = new ArrayList<>();
        maps.add(PN_BAIDU_MAP);
        maps.add(PN_GAODE_MAP);
        maps.add(PN_TENCENT_MAP);
//        maps.add("com.google.android.apps.maps");
        return maps;
    }

    public static boolean isGdMapInstalled() {

        return isInstallPackage(PN_GAODE_MAP);

    }

    public static boolean isBaiduMapInstalled() {

        return isInstallPackage(PN_BAIDU_MAP);

    }

    public static boolean isTencentMapInstalled() {

        return isInstallPackage(PN_TENCENT_MAP);

    }

    private static boolean isInstallPackage(String packageName) {

        return new File("/data/data/" + packageName).exists();

    }

    /**
     * 百度转高德
     *
     * @param bd_lat
     * @param bd_lon
     * @return
     */

    public static double[] bdToGaoDe(double bd_lat, double bd_lon) {

        double[] gd_lat_lon = new double[2];

        double PI = 3.14159265358979324 * 3000.0 / 180.0;

        double x = bd_lon - 0.0065, y = bd_lat - 0.006;

        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * PI);

        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * PI);

        gd_lat_lon[0] = z * Math.cos(theta);

        gd_lat_lon[1] = z * Math.sin(theta);

        return gd_lat_lon;

    }

    /**
     * 高德、腾讯转百度
     *
     * @param gd_lon
     * @param gd_lat
     * @return
     */

    private static double[] gaoDeToBaidu(double gd_lon, double gd_lat) {

        double[] bd_lat_lon = new double[2];

        double PI = 3.14159265358979324 * 3000.0 / 180.0;

        double x = gd_lon, y = gd_lat;

        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * PI);

        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * PI);

        bd_lat_lon[0] = z * Math.cos(theta) + 0.0065;

        bd_lat_lon[1] = z * Math.sin(theta) + 0.006;

        return bd_lat_lon;

    }

    /**
     * 百度坐标系 (BD-09) 与 火星坐标系 (GCJ-02)的转换
     * <p>
     * 即 百度 转 谷歌、高德
     *
     * @param latLng
     * @returns 使用此方法需要下载导入百度地图的BaiduLBS_Android.jar包
     */

    public static LatLng BD09ToGCJ02(LatLng latLng) {

        double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

        double x = latLng.longitude - 0.0065;

        double y = latLng.latitude - 0.006;

        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);

        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);

        double gg_lat = z * Math.sin(theta);

        double gg_lng = z * Math.cos(theta);

        return new LatLng(gg_lat, gg_lng);

    }

    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换
     * <p>
     * 即谷歌、高德 转 百度
     *
     * @param latLng
     * @returns 需要百度地图的BaiduLBS_Android.jar包
     */

    public static LatLng GCJ02ToBD09(LatLng latLng) {

        double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

        double z = Math.sqrt(latLng.longitude * latLng.longitude + latLng.latitude * latLng.latitude) + 0.00002 * Math.sin(latLng.latitude * x_pi);

        double theta = Math.atan2(latLng.latitude, latLng.longitude) + 0.000003 * Math.cos(latLng.longitude * x_pi);

        double bd_lat = z * Math.sin(theta) + 0.006;

        double bd_lng = z * Math.cos(theta) + 0.0065;

        return new LatLng(bd_lat, bd_lng);

    }

    /**
     * 打开高德地图导航功能
     *
     * @param context
     * @param slat    起点纬度
     * @param slon    起点经度
     * @param sname   起点名称 可不填（0,0，null）
     * @param dlat    终点纬度
     * @param dlon    终点经度
     * @param dname   终点名称 必填
     */

    public static Intent openGaoDeNavi(Context context, double slat, double slon, String sname, double dlat, double dlon, String dname) {

        String uriString = null;

        StringBuilder builder = new StringBuilder("amapuri://route/plan?sourceApplication=maxuslife");

        if (slat != 0) {

            builder.append("&sname=").append(sname)

                    .append("&slat=").append(slat)

                    .append("&slon=").append(slon);

        }

        builder.append("&dlat=").append(dlat)

                .append("&dlon=").append(dlon)

                .append("&dname=").append(dname)

                .append("&dev=0")

                .append("&t=0");

        uriString = builder.toString();

        Intent intent = new Intent(Intent.ACTION_VIEW);

        intent.setPackage(PN_GAODE_MAP);

        intent.setData(Uri.parse(uriString));

        return intent;

    }

    /**
     * 打开腾讯地图
     * <p>
     * params 参考http://lbs.qq.com/uri_v1/guide-route.html
     *
     * @param context
     * @param slat    起点纬度
     * @param slon    起点经度
     * @param sname   起点名称 可不填（0,0，null）
     * @param dlat    终点纬度
     * @param dlon    终点经度
     * @param dname   终点名称 必填
     *                <p>
     *                驾车：type=drive，policy有以下取值
     *                <p>
     *                0：较快捷
     *                <p>
     *                1：无高速
     *                <p>
     *                2：距离
     *                <p>
     *                policy的取值缺省为0
     *                <p>
     *                &from=" + dqAddress + "&fromcoord=" + dqLatitude + "," + dqLongitude + "
     */

    public static Intent openTencentMap(Context context, double slat, double slon, String sname, double dlat, double dlon, String dname) {

        String uriString = null;

        StringBuilder builder = new StringBuilder("qqmap://map/routeplan?type=drive&policy=0&referer=zhongshuo");

        if (slat != 0) {

            builder.append("&from=").append(sname)

                    .append("&fromcoord=").append(slat)

                    .append(",")

                    .append(slon);

        }

        builder.append("&to=").append(dname)

                .append("&tocoord=").append(dlat)

                .append(",")

                .append(dlon);

        uriString = builder.toString();

        Intent intent = new Intent(Intent.ACTION_VIEW);

        intent.setPackage(PN_TENCENT_MAP);

        intent.setData(Uri.parse(uriString));

        return intent;

    }

    /**
     * 打开百度地图导航功能(默认坐标点是高德地图，需要转换)
     *
     * @param context
     * @param slat    起点纬度
     * @param slon    起点经度
     * @param sname   起点名称 可不填（0,0，null）
     * @param dlat    终点纬度
     * @param dlon    终点经度
     * @param dname   终点名称 必填
     */

    public static Intent openBaiDuNavi(Context context, double slat, double slon, String sname, double dlat, double dlon, String dname) {

        String uriString = null;

        //终点坐标转换

//    此方法需要百度地图的BaiduLBS_Android.jar包

//    LatLng destination = new LatLng(dlat,dlon);

//    LatLng destinationLatLng = GCJ02ToBD09(destination);

//    dlat = destinationLatLng.latitude;

//    dlon = destinationLatLng.longitude;

        double destination[] = gaoDeToBaidu(dlat, dlon);

        dlat = destination[0];

        dlon = destination[1];

        StringBuilder builder = new StringBuilder("baidumap://map/direction?mode=driving&");

        if (slat != 0) {

            //起点坐标转换

//      LatLng origin = new LatLng(slat,slon);

//      LatLng originLatLng = GCJ02ToBD09(origin);

//      slat = originLatLng.latitude;

//      slon = originLatLng.longitude;

            double[] origin = gaoDeToBaidu(slat, slon);

            slat = origin[0];

            slon = origin[1];

            builder.append("origin=latlng:")

                    .append(slat)

                    .append(",")

                    .append(slon)

                    .append("|name:")

                    .append(sname);

        }

        builder.append("&destination=latlng:")

                .append(dlat)

                .append(",")

                .append(dlon)

                .append("|name:")

                .append(dname);

        uriString = builder.toString();

        Intent intent = new Intent(Intent.ACTION_VIEW);

        intent.setPackage(PN_BAIDU_MAP);

        intent.setData(Uri.parse(uriString));

        return intent;

    }


    public static PopupWindow showMap(Context context, RelativeLayout shage, View view, String address, String appname) {
        ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
        List<String> maps = hasMap(context);
        PopupWindow popupWindow = null;
        for(int i = 0 ; i < maps.size() ; i++)
        {
            MenuItem menuItem = praseMenu(maps.get(i),context,address,appname,popupWindow);
            menuItems.add(menuItem);
        }
        popupWindow = AppUtils.creatButtomMenu(context,shage,menuItems,view);
        return popupWindow;
    }

    public static PopupWindow showMapLocation(Context context, RelativeLayout shage,View view,String appname,String saddress,double slan,double slon, String address,double lan,double lon) {
        ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
        List<String> maps = hasMap(context);
        PopupWindow popupWindow = null;
        for(int i = 0 ; i < maps.size() ; i++)
        {
            MenuItem menuItem = praseMenu2(maps.get(i),context,popupWindow,saddress,slan,slon,address,lan,lon);
            menuItems.add(menuItem);
        }
        popupWindow = AppUtils.creatButtomMenu(context,shage,menuItems,view);
        for(int i = 0 ; i < menuItems.size() ; i++)
        {
            MapClickListener mapClickListener = (MapClickListener) menuItems.get(i).mListener;
            mapClickListener.setPopupWindow(popupWindow);
        }
        return popupWindow;
    }
    public static List<String> hasMap(Context context) {
        List<String> mapsList = mapsList();
        List<String> backList = new ArrayList<>();
        for (int i = 0; i < mapsList.size(); i++) {
            boolean avilible = isAvilible(context, mapsList.get(i));
            if (avilible) {
                backList.add(mapsList.get(i));
            }
        }
        return backList;
    }

    public static MenuItem praseMenu(String url,Context context,String address,String appname,PopupWindow popupWindow)
    {
        MenuItem item = new MenuItem();

        if(url.equals("com.baidu.BaiduMap"))
        {
            item.btnName = context.getString(intersky.apputils.R.string.map_baidu);
            item.mListener = new MapClickListener(context);
            Intent intent = new Intent();
            intent.setData(Uri.parse("baidumap://map/geocoder?src=andr.baidu.openAPIdemo&address="+address));
            item.item = intent;

        }
        else if(url.equals("com.autonavi.minimap"))
        {
            item.btnName = context.getString(intersky.apputils.R.string.map_gaode);
            item.mListener = new MapClickListener(context);
            Intent intent = new Intent();
            intent.setType(Intent.CATEGORY_DEFAULT);
//            intent.setAction(Intent.ACTION_VIEW);
            intent.setPackage("com.autonavi.minimap");
            intent.setData(Uri.parse("androidamap://keywordNavi?sourceApplication=com.bigwiner&keyword="+address+"&style=2"));
            item.item = intent;
        }
        else if(url.equals("com.tencent.map"))
        {
            item.btnName = context.getString(intersky.apputils.R.string.map_tengxun);
            item.mListener = new MapClickListener(context);
            StringBuffer stringBuffer = new StringBuffer("qqmap://map/routeplan?type=drive")
                    .append("&tocoord=").append("&to=" + address);

            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(stringBuffer.toString()));
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage(PN_TENCENT_MAP);
            item.item = intent;
        }
        else if(url.equals("com.google.android.apps.maps"))
        {
            item.btnName = context.getString(intersky.apputils.R.string.map_google);
            item.mListener = new MapClickListener(context);
            Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://ditu.google.cn/maps?f=d&source="+address+"&hl=zh"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK & Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            item.item = intent;
        }
        return item;
    }

    public static MenuItem praseMenu2(String url,Context context,PopupWindow popupWindow,String saddress,double slan,double slon,String daddress,double dlan,double dlon)
    {
        MenuItem item = new MenuItem();

        if(url.equals("com.baidu.BaiduMap"))
        {
            item.btnName = context.getString(intersky.apputils.R.string.map_baidu);
            item.mListener = new MapClickListener(context);
            item.item = openBaiDuNavi(context,slan,slon,saddress,dlan,dlon,daddress);

        }
        else if(url.equals("com.autonavi.minimap"))
        {
            item.btnName = context.getString(intersky.apputils.R.string.map_gaode);
            item.mListener = new MapClickListener(context);
            item.item = openGaoDeNavi(context,slan,slon,saddress,dlan,dlon,daddress);
        }
        else if(url.equals("com.tencent.map"))
        {
            item.btnName = context.getString(intersky.apputils.R.string.map_tengxun);
            item.mListener = new MapClickListener(context);
            item.item = openTencentMap(context,slan,slon,saddress,dlan,dlon,daddress);
        }
        else if(url.equals("com.google.android.apps.maps"))
        {
            item.btnName = context.getString(intersky.apputils.R.string.map_google);
            item.mListener = new MapClickListener(context);
            Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://ditu.google.cn/maps?f=d&source="+daddress+"&hl=zh"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK & Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            item.item = intent;
        }
        return item;
    }

    public static class MapClickListener implements View.OnClickListener {

        public Context context;
        public PopupWindow popupWindow;
        public MapClickListener(Context context)
        {
            this.context = context;
        }

        public void setPopupWindow(PopupWindow popupWindow) {
            this.popupWindow = popupWindow;
        }

        @Override
        public void onClick(View v) {
            context.startActivity((Intent) v.getTag());
            if(popupWindow != null)
            {
                popupWindow.dismiss();
            }
        }
    }
}

