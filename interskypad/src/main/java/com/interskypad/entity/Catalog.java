package com.interskypad.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Catalog implements Parcelable {

    public String mSerialID = "";
    public String mItemNo = "";
    public String mBarcode = "";
    public String mCatalogueID = "";
    public String mENGItemName = "";
    public String mENGSpecification = "";
    public String mOuterVolume = "";
    public String mOuterCapacity = "";
    public String mPacking = "";
    public String mUnit = "";
    public String mENGMemo = "";
    public String mLastModified = "";
    public String mPhoto = "";
    public String mSupplierShortName = "";
    public String mSupplierItemNo = "";
    public String mCanBill = "";
    public String mPurchasePrice = "";
    public String mMinimumQty = "";
    public String mOuterGrossWeight = "";
    public String mOuterNetWeight = "";
    public String mSalesPrice = "";
    public String mRebate = "";

    public Catalog() {

    }

    public Catalog(String mSerialID, String mItemNo, String mBarcode
            , String mCatalogueID, String mENGItemName, String mENGSpecification, String mOuterVolume
            , String mOuterCapacity, String mPacking, String mUnit, String mENGMemo, String mLastModified
            , String mPhoto, String mSupplierShortName, String mSupplierItemNo, String mCanBill, String mPurchasePrice
            , String mMinimumQty, String mOuterGrossWeight, String mOuterNetWeight, String mSalesPrice
            , String mRebate) {

        this.mSerialID = mSerialID;
        this.mItemNo = mItemNo;
        this.mBarcode = mBarcode;
        this.mCatalogueID = mCatalogueID;
        this.mENGItemName = mENGItemName;
        this.mENGSpecification = mENGSpecification;
        this.mOuterVolume = mOuterVolume;
        this.mOuterCapacity = mOuterCapacity;
        this.mPacking = mPacking;
        this.mUnit = mUnit;
        this.mENGMemo = mENGMemo;
        this.mLastModified = mLastModified;
        this.mPhoto = mPhoto;
        this.mSupplierShortName = mSupplierShortName;
        this.mSupplierItemNo = mSupplierItemNo;
        this.mCanBill = mCanBill;
        this.mPurchasePrice = mPurchasePrice;
        this.mMinimumQty = mMinimumQty;
        this.mOuterGrossWeight = mOuterGrossWeight;
        this.mOuterNetWeight = mOuterNetWeight;
        this.mSalesPrice = mSalesPrice;
        this.mRebate = mRebate;
    }

    public Catalog(String mSerialID,String mCatalogueID, String mENGItemName) {
        this.mSerialID = mSerialID;
        this.mCatalogueID = mCatalogueID;
        this.mENGItemName = mENGItemName;
    }

    protected Catalog(Parcel in) {
        mSerialID = in.readString();
        mItemNo = in.readString();
        mBarcode = in.readString();
        mCatalogueID = in.readString();
        mENGItemName = in.readString();
        mENGSpecification = in.readString();
        mOuterVolume = in.readString();
        mOuterCapacity = in.readString();
        mPacking = in.readString();
        mUnit = in.readString();
        mENGMemo = in.readString();
        mLastModified = in.readString();
        mPhoto = in.readString();
        mSupplierShortName = in.readString();
        mSupplierItemNo = in.readString();
        mCanBill = in.readString();
        mPurchasePrice = in.readString();
        mMinimumQty = in.readString();
        mOuterGrossWeight = in.readString();
        mOuterNetWeight = in.readString();
        mSalesPrice = in.readString();
        mRebate = in.readString();
    }

    public static final Creator<Catalog> CREATOR = new Creator<Catalog>() {
        @Override
        public Catalog createFromParcel(Parcel in) {
            return new Catalog(in);
        }

        @Override
        public Catalog[] newArray(int size) {
            return new Catalog[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mSerialID);
        dest.writeString(mItemNo);
        dest.writeString(mBarcode);
        dest.writeString(mCatalogueID);
        dest.writeString(mENGItemName);
        dest.writeString(mENGSpecification);
        dest.writeString(mOuterVolume);

        dest.writeString(mOuterCapacity);
        dest.writeString(mPacking);
        dest.writeString(mUnit);
        dest.writeString(mENGMemo);
        dest.writeString(mLastModified);
        dest.writeString(mPhoto);
        dest.writeString(mSupplierShortName);

        dest.writeString(mSupplierItemNo);
        dest.writeString(mCanBill);
        dest.writeString(mPurchasePrice);
        dest.writeString(mMinimumQty);
        dest.writeString(mOuterGrossWeight);
        dest.writeString(mOuterNetWeight);
        dest.writeString(mSalesPrice);

        dest.writeString(mRebate);
    }
}
