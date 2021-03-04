package intersky.appbase.entity;

import intersky.json.XpxJSONObject;

public class Account {

    public String mAccountId = "000000000";
    public String mPassword = "";
    public String mRecordId = "";
    public String mUserName = "";
    public String mUCid = "";
    public String mRealName = "";
    public String mRoleId = "";
    public String mOrgId = "";
    public String mOrgName = "";
    public String mSex = "";
    public String mPhone = "";
    public String mMobile = "";
    public String mFax = "";
    public String mEmail = "";
    public String mCompanyId = "";
    public String mCompanyName = "";
    public String mManagerId = "";
    public String mCloundAdminId = "";
    public String mPosition = "";
    public String mAddress = "";
    public String complaint = "";
    public String icon = "";
    public String cover = "";
    public int sex = 0;
    public String city = "";
    public String province = "";
    public String confrim = "";
    public Boolean issail = false;
    public int leavel = 2;
    public String typeBusiness = "";
    public String typeArea = "";
    public String des = "";
    public boolean isAdmin = false;
    public String bgpath = "";
    public String headpath = "";
    public String vip = "";
    public int mHCurrent = 0;
    public String serviceid = "";
    public String cloudServer = "";
    public String modify = "";
    public boolean islogin = false;
    public boolean isouter = false;
    public boolean iscloud = false;
    public XpxJSONObject project;
    public String logininfo;

    public void copy(Account account) {
        this.mAccountId = account.mAccountId;
        this.mPassword = account.mPassword;
        this.mRecordId = account.mRecordId;
        this.mUserName = account.mUserName;
        this.mUCid = account.mUCid;
        this.mRealName = account.mRealName;
        this.mRoleId = account.mRoleId;
        this.mOrgId = account.mOrgId;
        this.mOrgName = account.mOrgName;
        this.mSex = account.mSex;
        this.mPhone = account.mPhone;
        this.mMobile = account.mMobile;
        this.mFax = account.mFax;
        this.mEmail = account.mEmail;
        this.mCompanyId = account.mCompanyId;
        this.mCompanyName = account.mCompanyName;
        this.mManagerId = account.mManagerId;
        this.mCloundAdminId = account.mCloundAdminId;
        this.mPosition = account.mPosition;
        this.mAddress = account.mAddress;
        this.icon = account.icon;
        this.cover = account.cover;
        this.sex = account.sex;
        this.city = account.city;
        this.province = account.province;
        this.confrim = account.confrim;
        this.leavel = account.leavel;
        this.typeBusiness = account.typeBusiness;
        this.typeArea = account.typeArea;
        this.des = account.des;
        this.isAdmin = account.isAdmin;
        this.bgpath = account.bgpath;
        this.headpath = account.headpath;
        this.mHCurrent = account.mHCurrent;
        this.islogin = account.islogin;
        this.vip = account.vip;
        this.mHCurrent = account.mHCurrent;
        this.serviceid = account.serviceid;
        this.cloudServer = account.cloudServer;
        this.isouter = account.isouter;
        this.iscloud = account.iscloud;
        this.project = account.project;
        this.logininfo = account.logininfo;
        this.modify = account.modify;
    }

    public String getName()
    {
        if(mRealName.length() != 0)
        {
            return mRealName;
        }
        else
        {
            return mUserName;
        }
    }
}


