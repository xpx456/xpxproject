package intersky.document.prase;

import android.content.Context;

import org.json.JSONException;

import java.util.ArrayList;

import intersky.appbase.bus.Bus;
import intersky.document.DocumentManager;
import intersky.document.R;
import intersky.document.asks.DocumentAsks;
import intersky.document.entity.DocumentNet;
import intersky.apputils.AppUtils;
import intersky.json.XpxJSONArray;
import intersky.json.XpxJSONObject;
import intersky.xpxnet.net.NetUtils;

public class DocumentPrase {


    public static void praseDocumentList(Context context,String json, DocumentNet parent) {
        try {
            DocumentManager.getInstance().fileList.clear();
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
                return;
            XpxJSONObject jObject = new XpxJSONObject(json);
            if (jObject.has("Data")) {
                XpxJSONArray data = jObject.getJSONArray("Data");
                if (data != null) {
                    for (int i = 0; i < data.length(); i++) {
                        XpxJSONObject mdocument = data.getJSONObject(i);
                        DocumentNet documentNet = new DocumentNet();
                        documentNet.mOwnerType = mdocument.getString("OwnerType");
                        if(documentNet.mOwnerType.length() == 0)
                        {
                            documentNet.mOwnerType = parent.mOwnerType;
                        }
                        documentNet.mOwnerID = mdocument.getString("OwnerID");
                        documentNet.mRecordID = mdocument.getString("RecordID");
                        documentNet.mName = mdocument.getString("Name");
                        documentNet.mName = documentNet.mName.replace("?", "");
                        if (mdocument.has("Shared")) {
                            documentNet.mShared = mdocument.getBoolean("Shared",false);
                        }
                        else {
                            documentNet.mShared = parent.mShared;
                        }
                        documentNet.mDate = mdocument.getString("Created");
                        documentNet.parentid = mdocument.getString("ParentID");
                        documentNet.mType = DocumentManager.TYPE_DOCUMENT;

                        if (documentNet.mOwnerType.equals("(Company)")
                                && documentNet.mName.length() == 0) {
                            documentNet.mName = DocumentManager.getInstance().context.getString(R.string.document_company);
                        }
                        else if (documentNet.mOwnerType.equals("(User)")
                                && documentNet.mName.length() == 0 && documentNet.mShared == false) {
                            documentNet.mName = DocumentManager.getInstance().context.getString(R.string.document_my);
                        }
                        else if (documentNet.mOwnerType.equals("(User)")
                                && documentNet.mName.length() == 0 && documentNet.mShared == true) {
                            documentNet.mName = DocumentManager.getInstance().context.getString(R.string.document_share);
                        }
                        DocumentManager.getInstance().fileList.add(documentNet);
                    }
                }
            }
            if (jObject.has("Files")) {
                XpxJSONArray file = jObject.getJSONArray("Files");
                if (file != null) {
                    for (int i = 0; i < file.length(); i++) {
                        XpxJSONObject mfile = file.getJSONObject(i);
                        DocumentNet documentNet = new DocumentNet();
                        documentNet.mOwnerID = mfile.getString("OwnerID");
                        documentNet.mOwnerType = parent.mOwnerType;
                        documentNet.mRecordID = mfile.getString("RecordID");
                        documentNet.mName = mfile.getString("FileName");
                        documentNet.mName = documentNet.mName.replace("?", "");
                        documentNet.mSize = mfile.getLong("Size",0);
                        documentNet.mShared = parent.mShared;
                        documentNet.mUrl = DocumentAsks.praseUrl(documentNet);
                        documentNet.mDate = mfile.getString("LastWrite");
                        documentNet.mPath = Bus.callData(context,"filetools/getfilePath", "/intersky"  + "/document"+"/download")+"/"+documentNet.mName;
                        documentNet.mType = DocumentManager.TYPE_DOWNLOAD_NOMAL;
                        DocumentManager.getInstance().fileList.add(documentNet);
                    }
                }
            }
        }
        catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void praseSearchDocumentList(String json, DocumentNet parent) {
        try {

            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
                return;

            DocumentManager.getInstance().sfileList.clear();
            XpxJSONObject jObject = new XpxJSONObject(json);
            if (jObject.has("Files")) {
                XpxJSONArray file = jObject.getJSONArray("Files");
                if (file != null) {
                    for (int i = 0; i < file.length(); i++) {
                        XpxJSONObject mfile = file.getJSONObject(i);
                        DocumentNet documentNet = new DocumentNet();
                        documentNet.mOwnerID = mfile.getString("OwnerID");
                        documentNet.mOwnerType = parent.mOwnerType;
                        documentNet.mRecordID = mfile.getString("RecordID");
                        documentNet.mName = mfile.getString("FileName");
                        documentNet.mName = documentNet.mName.replace("?", "");
                        documentNet.mSize = mfile.getLong("Size",0);
                        documentNet.mShared = parent.mShared;
                        documentNet.mDate = mfile.getString("LastWrite");
                        documentNet.mType = DocumentManager.TYPE_DOWNLOAD_NOMAL;
                        DocumentManager.getInstance().sfileList.add(documentNet);
                    }
                }
            }
        }
        catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void praseDocumentOnly(String json, DocumentNet parent, ArrayList<DocumentNet> documentNets) {
        try {
            documentNets.clear();
            if(AppUtils.measureToken(json) != null) {
                NetUtils.getInstance().token = AppUtils.measureToken(json);
            }
            if(AppUtils.success(json) == false)
                return;
            XpxJSONObject jObject = new XpxJSONObject(json);
            if (jObject.has("Data")) {
                XpxJSONArray data = jObject.getJSONArray("Data");
                if (data != null) {
                    for (int i = 0; i < data.length(); i++) {
                        XpxJSONObject mdocument = data.getJSONObject(i);
                        DocumentNet documentNet = new DocumentNet();
                        documentNet.mOwnerType = mdocument.getString("OwnerType");
                        if(documentNet.mOwnerType.length() == 0)
                        {
                            documentNet.mOwnerType = parent.mOwnerType;
                        }
                        documentNet.mOwnerID = mdocument.getString("OwnerID");
                        documentNet.mRecordID = mdocument.getString("RecordID");
                        documentNet.mName = mdocument.getString("Name");
                        documentNet.mName = documentNet.mName.replace("?", "");
                        if (mdocument.has("Shared")) {
                            documentNet.mShared = mdocument.getBoolean("Shared",false);
                        }
                        else {
                            documentNet.mShared = parent.mShared;
                        }
                        documentNet.mDate = mdocument.getString("Created");
                        documentNet.parentid = mdocument.getString("ParentID");
                        documentNet.mType = DocumentManager.TYPE_DOCUMENT;

                        if (documentNet.mOwnerType.equals("(Company)")
                                && documentNet.mName.length() == 0) {
                            documentNet.mName = DocumentManager.getInstance().context.getString(R.string.document_company);
                        }
                        else if (documentNet.mOwnerType.equals("(User)")
                                && documentNet.mName.length() == 0 && documentNet.mShared == false) {
                            documentNet.mName = DocumentManager.getInstance().context.getString(R.string.document_my);
                        }
                        else if (documentNet.mOwnerType.equals("(User)")
                                && documentNet.mName.length() == 0 && documentNet.mShared == true) {
                            documentNet.mName = DocumentManager.getInstance().context.getString(R.string.document_share);
                        }
                        documentNets.add(documentNet);
                    }
                }
            }
        }
        catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
