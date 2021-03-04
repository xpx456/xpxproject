package com.restaurant.model.bean;

import android.text.TextUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Data;

@Data
public class VeinImageInfo {
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");

    private File imgFile;
    private File featFile;

    private String deviceName = "";
    private String deviceNo = "";
    private String createTime = "";
    private String name = "";
    private String fingerName = "";
    private String base64 = "";
    private String photoFilename = "";
    private String base64Filename = "";

    private String commonFileName = "";


    public void genFileNames(Date date, String rid) {
        commonFileName = dateFormat.format(date) +
                "_" + (name != null ? name : "null") + "_" +
                (TextUtils.isEmpty(rid) ? 0 : rid) + "_" +
                (TextUtils.isEmpty(fingerName) ? "null" : fingerName) + "_" + deviceNo;
        photoFilename = commonFileName + ".png";
        base64Filename = commonFileName + ".txt";
    }


    public static SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public File getImgFile() {
        return imgFile;
    }

    public void setImgFile(File imgFile) {
        this.imgFile = imgFile;
    }

    public File getFeatFile() {
        return featFile;
    }

    public void setFeatFile(File featFile) {
        this.featFile = featFile;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFingerName() {
        return fingerName;
    }

    public void setFingerName(String fingerName) {
        this.fingerName = fingerName;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public String getPhotoFilename() {
        return photoFilename;
    }

    public void setPhotoFilename(String photoFilename) {
        this.photoFilename = photoFilename;
    }

    public String getBase64Filename() {
        return base64Filename;
    }

    public void setBase64Filename(String base64Filename) {
        this.base64Filename = base64Filename;
    }

    public String getCommonFileName() {
        return commonFileName;
    }

    public void setCommonFileName(String commonFileName) {
        this.commonFileName = commonFileName;
    }
}
