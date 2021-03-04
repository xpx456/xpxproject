package intersky.function;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import intersky.appbase.bus.Bus;
import intersky.appbase.entity.Account;
import intersky.appbase.entity.Commend;
import intersky.appbase.entity.Conversation;
import intersky.apputils.AppUtils;
import intersky.function.asks.FunAsks;
import intersky.function.entity.FunData;
import intersky.function.entity.Function;
import intersky.function.handler.FunHitHandler;
import intersky.function.prase.FunctionPrase;
import intersky.function.view.activity.BusinesCardActivity;
import intersky.function.view.activity.BusinessWarnActivity;
import intersky.function.view.activity.ChartActivity;
import intersky.function.view.activity.FunctionModuleActivity;
import intersky.function.view.activity.GridActivity;
import intersky.function.view.activity.GridDetialActivity;
import intersky.function.view.activity.LabelActivity;
import intersky.function.view.activity.WebAppActivity;
import intersky.function.view.activity.WebMessageActivity;
import intersky.function.view.activity.WorkFlowActivity;
import intersky.function.view.adapter.FunctionAdapter;
import intersky.json.XpxJSONArray;
import intersky.json.XpxJSONObject;
import intersky.oa.OaAsks;
import intersky.oa.OaUtils;
import intersky.xpxnet.net.NetUtils;
import intersky.xpxnet.net.Service;

public class FunctionUtils {

    public static final String ACTION_GET_BASE_HIT_FINISH = "ACTION_GET_BASE_HIT_FINISH";
    public static final String ACTION_GET_OA_HIT_FINISH = "ACTION_GET_OA_HIT_FINISH";
    public static final String ACTION_GET_MAIL_HIT= "ACTION_GET_MAIL_HIT";
    public static final String ACTION_GET_TASK_HIT = "ACTION_GET_TASK_HIT";
    private static FunctionUtils functionUtils;
    public FunData mFunData = new FunData();
    public FunData mFunData2 = new FunData();
    public ArrayList<Function> mFunctions = new ArrayList<Function>();
    public ArrayList<Function> myFunction = new ArrayList<>();
    public ArrayList<Function> otherFunction = new ArrayList<>();
    public HashMap<String,Function> all = new HashMap<String,Function>();
    public Map<String,ArrayList<Function>> mFunctionGrids = new HashMap<String, ArrayList<Function>>();
    public ArrayList<Function> moreFunctions = new ArrayList<Function>();
    public ArrayList<FunctionAdapter> mFunctionAdapters = new ArrayList<FunctionAdapter>();
    public FunctionAdapter allAdapter;
    public FunctionAdapter myAdapter;
    public Context context;
    public Service service;
    public Account account;
    public int workhit = 0;
    public FunHitHandler mFunHitHandler = new FunHitHandler();

    public static FunctionUtils init(Context context) {

        if (functionUtils == null) {
            synchronized (FunctionUtils.class) {
                if (functionUtils == null) {
                    functionUtils =  new FunctionUtils(context);
                }
                else
                {
                    functionUtils.context = context;
                }
            }
        }

        return functionUtils;
    }

    public static FunctionUtils getInstance() {
        return functionUtils;
    }

    public FunctionUtils(Context context) {
        this.context = context;


    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public void starFunctionData() {
        cleanAll();
        addFunctions();
        initMore();
    }

    public void cleanAll() {
        mFunData = new FunData();
        mFunData2 = new FunData();
        mFunctions.clear();
        mFunctionGrids.clear();
        moreFunctions.clear();
        mFunctionAdapters.clear();
    }

    public void cleanWorkflow() {
        if(mFunData != null)
        mFunData.clean();
        if(mFunData2 != null)
        mFunData2.clean();
    }

    public void measureWorkHit() {
        ArrayList<Function> mfuns = FunctionUtils.getInstance().mFunctionGrids.get("oa");
        workhit = FunctionUtils.getInstance().mFunctions.get(0).hintCount +FunctionUtils.getInstance().mFunctions.get(1).hintCount+mfuns.get(0).hintCount+
        mfuns.get(2).hintCount +mfuns.get(3).hintCount +mfuns.get(5).hintCount +mfuns.get(6).hintCount+ mfuns.get(7).hintCount
                +(int)Bus.callData(context,"mail/getMailHitCount","")+(int)Bus.callData(context,"task/getTaskHitCount","");

    }

    public void saveMyFunction() {
        SharedPreferences sharedPre = context.getSharedPreferences(service.sAddress, 0);
        SharedPreferences.Editor editor = sharedPre.edit();
        JSONArray ja = new JSONArray();
        for(int i = 0 ; i < myFunction.size()-1 ; i++)
        {
            ja.put(myFunction.get(i).mCaption);
        }
        editor.putString("myfunction",ja.toString());
        editor.commit();
    };

    public void initMy()
    {
        SharedPreferences sharedPre = context.getSharedPreferences(service.sAddress, 0);
        String myjson = sharedPre.getString("myfunction","");
        myFunction.clear();
        if(myjson.length() > 0)
        {
            try {
                XpxJSONArray ja = new XpxJSONArray(myjson);
                for(int i= 0 ; i < ja.length() ;i++)
                {
                    if(all.containsKey(ja.getString(i)))
                    {
                        myFunction.add(all.get(ja.getString(i)));
                        all.get(ja.getString(i)).select = true;
                        otherFunction.remove(all.get(ja.getString(i)));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        JSONArray ja2 = new JSONArray();
        for(int i = 0 ; i < myFunction.size() ; i++)
        {
            ja2.put(myFunction.get(i).mCaption);
        }
        SharedPreferences.Editor editor = sharedPre.edit();
        editor.putString("myfunction",ja2.toString());
        editor.commit();
        Function tmpB;
        tmpB = new Function();
        tmpB.mCaption = context.getString(R.string.add_comm);
        tmpB.typeName = Function.ADD;
        myFunction.add(tmpB);
        myAdapter = new FunctionAdapter(context,myFunction,false);
    }

    private void addFunctions() {
        try {

            mFunctions.clear();
            all.clear();
            otherFunction.clear();
            XpxJSONObject xpxJsonObject = new XpxJSONObject(account.logininfo);
            Function tmpB;
            if(account.isouter == false)
            {
                XpxJSONObject hits = xpxJsonObject.getJSONObject("badges");
                tmpB = new Function();
                tmpB.mCaption = context.getResources().getString(R.string.function_businessWarn);
                tmpB.typeName = Function.WARM;
                tmpB.hintCount = hits.getInt("reminder",0);
                mFunctions.add(tmpB);
                addfun(tmpB);
                all.put(tmpB.mCaption,tmpB);
                otherFunction.add(tmpB);
                tmpB = new Function();
                tmpB.mCaption = context.getResources().getString(R.string.function_taskExamine);
                tmpB.typeName = Function.EXAMINE;
                tmpB.hintCount = hits.getInt("workflow",0);
                mFunctions.add(tmpB);
                addfun(tmpB);
                all.put(tmpB.mCaption,tmpB);
                otherFunction.add(tmpB);
            }
            List<Function> mfuncs = FunctionPrase.praseFunctions(account.logininfo);
            for (int i = 0; i < mfuncs.size(); i++) {
                Function temp = mfuncs.get(i);
                addfun(temp);
                all.put(temp.mCaption,temp);
                otherFunction.add(temp);
            }
            mfuncs = FunctionPrase.praseWebapp(account.logininfo);
            for (int i = 0; i < mfuncs.size(); i++) {
                Function temp = mfuncs.get(i);
                addfun(temp);
                all.put(temp.mCaption,temp);
                otherFunction.add(temp);
                mFunctions.add(temp);
            }


            tmpB = new Function();
            tmpB.mCatalogueName = "main";
            tmpB.mCaption = context.getResources().getString(R.string.function_mail);
            tmpB.typeName = Function.MAIL;
            addfun(tmpB);
            all.put(tmpB.mCaption,tmpB);
            otherFunction.add(tmpB);
            mFunctions.add(tmpB);

            tmpB = new Function();
            tmpB.mCatalogueName = "main";
            tmpB.mCaption = context.getResources().getString(R.string.function_doucument);
            tmpB.typeName = Function.DOCUMENT;
            addfun(tmpB);
            all.put(tmpB.mCaption,tmpB);
            otherFunction.add(tmpB);
            mFunctions.add(tmpB);


            tmpB = new Function();
            tmpB.mCatalogueName = "main";
            tmpB.mCaption = context.getResources().getString(R.string.function_smart);
            tmpB.typeName = Function.SEARCH;
            addfun(tmpB);
            all.put(tmpB.mCaption,tmpB);
            otherFunction.add(tmpB);
            mFunctions.add(tmpB);

            tmpB = new Function();
            tmpB.mCatalogueName = "oa";
            tmpB.mCaption = context.getResources().getString(R.string.function_workreport);
            tmpB.typeName = Function.WORKREPORT;
            addfun(tmpB);
            all.put(tmpB.mCaption,tmpB);
            otherFunction.add(tmpB);
            mFunctions.add(tmpB);
            tmpB = new Function();
            tmpB.mCatalogueName = "oa";
            tmpB.mCaption = context.getResources().getString(R.string.function_sign);
            tmpB.typeName = Function.SIGN;
            addfun(tmpB);
            all.put(tmpB.mCaption,tmpB);
            otherFunction.add(tmpB);
            mFunctions.add(tmpB);
            tmpB = new Function();
            tmpB.mCatalogueName = "oa";
            tmpB.mCaption = context.getResources().getString(R.string.function_leave);
            tmpB.typeName = Function.LEAVE;
            addfun(tmpB);
            all.put(tmpB.mCaption,tmpB);
            otherFunction.add(tmpB);
            mFunctions.add(tmpB);
            tmpB = new Function();
            tmpB.mCatalogueName = "oa";
            tmpB.mCaption = context.getResources().getString(R.string.function_schedule);
            tmpB.typeName = Function.DATE;
            addfun(tmpB);
            all.put(tmpB.mCaption,tmpB);
            otherFunction.add(tmpB);
            mFunctions.add(tmpB);
            tmpB = new Function();
            tmpB.mCatalogueName = "oa";
            tmpB.mCaption = context.getResources().getString(R.string.function_attdence);
            tmpB.typeName = Function.WORKATTDENCE;
            addfun(tmpB);
            all.put(tmpB.mCaption,tmpB);
            otherFunction.add(tmpB);
            mFunctions.add(tmpB);
            tmpB = new Function();
            tmpB.mCatalogueName = "oa";
            tmpB.mCaption = context.getResources().getString(R.string.function_systemmesage);
            tmpB.typeName = Function.NOTICE;
            addfun(tmpB);
            all.put(tmpB.mCaption,tmpB);
            otherFunction.add(tmpB);
            mFunctions.add(tmpB);
            tmpB = new Function();
            tmpB.mCatalogueName = "oa";
            tmpB.mCaption = context.getResources().getString(R.string.function_vote);
            tmpB.typeName = Function.VOTE;
            addfun(tmpB);
            all.put(tmpB.mCaption,tmpB);
            otherFunction.add(tmpB);
            mFunctions.add(tmpB);
            tmpB = new Function();
            tmpB.mCatalogueName = "oa";
            tmpB.mCaption = context.getResources().getString(R.string.function_task);
            tmpB.typeName = Function.TASK;
            addfun(tmpB);
            all.put(tmpB.mCaption,tmpB);
            otherFunction.add(tmpB);
            mFunctions.add(tmpB);
        } catch (JSONException e) {
            e.printStackTrace();
            Function tmpB;
            tmpB = new Function();
            tmpB.mCatalogueName = "oa";
            tmpB.mCaption = context.getResources().getString(R.string.function_workreport);
            tmpB.typeName = Function.WORKREPORT;
            addfun(tmpB);
            all.put(tmpB.mCaption,tmpB);
            otherFunction.add(tmpB);
            mFunctions.add(tmpB);
            tmpB = new Function();
            tmpB.mCatalogueName = "oa";
            tmpB.mCaption = context.getResources().getString(R.string.function_sign);
            tmpB.typeName = Function.SIGN;
            addfun(tmpB);
            all.put(tmpB.mCaption,tmpB);
            otherFunction.add(tmpB);
            mFunctions.add(tmpB);
            tmpB = new Function();
            tmpB.mCatalogueName = "oa";
            tmpB.mCaption = context.getResources().getString(R.string.function_leave);
            tmpB.typeName = Function.LEAVE;
            addfun(tmpB);
            all.put(tmpB.mCaption,tmpB);
            otherFunction.add(tmpB);
            mFunctions.add(tmpB);
            tmpB = new Function();
            tmpB.mCatalogueName = "oa";
            tmpB.mCaption = context.getResources().getString(R.string.function_schedule);
            tmpB.typeName = Function.DATE;
            addfun(tmpB);
            all.put(tmpB.mCaption,tmpB);
            otherFunction.add(tmpB);
            mFunctions.add(tmpB);
            tmpB = new Function();
            tmpB.mCatalogueName = "oa";
            tmpB.mCaption = context.getResources().getString(R.string.function_attdence);
            tmpB.typeName = Function.WORKATTDENCE;
            addfun(tmpB);
            all.put(tmpB.mCaption,tmpB);
            otherFunction.add(tmpB);
            mFunctions.add(tmpB);
            tmpB = new Function();
            tmpB.mCatalogueName = "oa";
            tmpB.mCaption = context.getResources().getString(R.string.function_systemmesage);
            tmpB.typeName = Function.NOTICE;
            addfun(tmpB);
            all.put(tmpB.mCaption,tmpB);
            otherFunction.add(tmpB);
            mFunctions.add(tmpB);
            tmpB = new Function();
            tmpB.mCatalogueName = "oa";
            tmpB.mCaption = context.getResources().getString(R.string.function_vote);
            tmpB.typeName = Function.VOTE;
            addfun(tmpB);
            all.put(tmpB.mCaption,tmpB);
            otherFunction.add(tmpB);
            mFunctions.add(tmpB);
            tmpB = new Function();
            tmpB.mCatalogueName = "oa";
            tmpB.mCaption = context.getResources().getString(R.string.function_task);
            tmpB.typeName = Function.TASK;
            addfun(tmpB);
            all.put(tmpB.mCaption,tmpB);
            otherFunction.add(tmpB);
            mFunctions.add(tmpB);
        }
        allAdapter = new FunctionAdapter(context,otherFunction,false);
        initMy();

    }

    private void addfun(Function temp) {
        if (temp.mCatalogueName.length() > 0) {
            if (mFunctionGrids.get(temp.mCatalogueName) != null) {
                ArrayList<Function> list = mFunctionGrids.get(temp.mCatalogueName);
                list.add(temp);
            } else {
                ArrayList<Function> list = new ArrayList<Function>();
                list.add(temp);
                mFunctionGrids.put(temp.mCatalogueName, list);
                mFunctionAdapters.add(new FunctionAdapter(context, list, false));
            }
        } else {
            if (mFunctionGrids.get("base") != null) {
                ArrayList<Function> list = mFunctionGrids.get("base");
                list.add(temp);
            } else {
                ArrayList<Function> list = new ArrayList<Function>();
                list.add(temp);
                mFunctionGrids.put("base", list);
                mFunctionAdapters.add(new FunctionAdapter(context, list, false));
            }
        }
    }

    private void initMore() {

        if(account.isouter == false)
        {
            Function tmpB = new Function();
            tmpB.mCaption = context.getResources().getString(R.string.function_newmail);
            tmpB.typeName = Function.NEWMAIL;
            moreFunctions.add(tmpB);
            tmpB = new Function();
            tmpB.mCaption = context.getResources().getString(R.string.function_businesscard);
            tmpB.typeName = Function.CARD;
            moreFunctions.add(tmpB);

        }
        Function tmpB = new Function();
        tmpB.mCaption =context.getResources().getString(R.string.function_newtask);
        tmpB.typeName = Function.NEWTASK;
        moreFunctions.add(0, tmpB);

        tmpB = new Function();
        tmpB.mCaption = context.getResources().getString(R.string.function_newproject);
        tmpB.typeName = Function.NEWPROJECT;
        moreFunctions.add(1, tmpB);


        tmpB = new Function();
        tmpB.mCaption = context.getResources().getString(R.string.function_workreport);
        tmpB.typeName = Function.WORKREPORT_M;
        moreFunctions.add(tmpB);

        tmpB = new Function();
        tmpB.mCaption = context.getResources().getString(R.string.function_sign);
        tmpB.typeName = Function.SIGN_M;
        moreFunctions.add(tmpB);

        tmpB = new Function();
        tmpB.mCaption = context.getResources().getString(R.string.function_leave_n);
        tmpB.typeName = Function.LEAVE_M;
        moreFunctions.add(tmpB);

        tmpB = new Function();
        tmpB.mCaption = context.getResources().getString(R.string.function_date_n);
        tmpB.typeName = Function.DATE_M;
        moreFunctions.add(tmpB);


        tmpB = new Function();
        tmpB.mCaption = context.getResources().getString(R.string.function_attdence);
        tmpB.typeName = Function.WORKATTDENCE_M;
        moreFunctions.add(tmpB);

        tmpB = new Function();
        tmpB.mCaption = context.getResources().getString(R.string.function_systemmesage_n);
        tmpB.typeName = Function.NOTICE_M;
        moreFunctions.add(tmpB);

        tmpB = new Function();
        tmpB.mCaption = context.getResources().getString(R.string.function_vote_n);
        tmpB.typeName = Function.VOTE_M;
        moreFunctions.add(tmpB);
    }

    public void getBaseHit() {
        if(account.isouter == false)
        FunAsks.getFunhit(context,mFunHitHandler);
    }

    public void getOaHit() {
        if (account.mCloundAdminId.length() != 0 && account.mCompanyId.length() != 0 && account.cloudServer.length() != 0)
            OaAsks.getOaHit(mFunHitHandler,context,account.mRecordId,account.mCompanyId);
    }

    public void startFunction(Context context ,Function mFunction,Boolean iscloud) {
        Intent intent;
        if(mFunction.typeName.toLowerCase().equals(Function.WARM))
        {
            intent = new Intent(context, BusinessWarnActivity.class);
            intent.putExtra("function",mFunction);
            intent.putExtra("iscloud",iscloud);
            context.startActivity(intent);
        }
        else if(mFunction.typeName.toLowerCase().equals(Function.EXAMINE))
        {
            intent = new Intent(context, WorkFlowActivity.class);
            intent.putExtra("function",mFunction);
            intent.putExtra("iscloud",iscloud);
            context.startActivity(intent);
        }
        else if(mFunction.typeName.toLowerCase().equals(Function.GRID))
        {
            intent = new Intent(context, GridActivity.class);
            intent.putExtra("function",mFunction);
            context.startActivity(intent);
        }
        else if(mFunction.typeName.toLowerCase().equals(Function.MIX))
        {
            intent = new Intent(context, FunctionModuleActivity.class);
            intent.putExtra("function",mFunction);
            context.startActivity(intent);
        }
        else if(mFunction.typeName.toLowerCase().equals(Function.LABEL))
        {
            intent = new Intent(context, LabelActivity.class);
            intent.putExtra("function",mFunction);
            context.startActivity(intent);
        }
        else if((mFunction.typeName.equals(Function.COLUMN) || mFunction.typeName.equals(Function.COLUMNS) || mFunction.typeName.equals(Function.PIE)
                || mFunction.typeName.equals(Function.LINE) || mFunction.typeName.equals(Function.BAR)
                || mFunction.typeName.equals(Function.FUNNEL)) )
        {
            intent = new Intent(context, ChartActivity.class);
            intent.putExtra("function",mFunction);
            context.startActivity(intent);
        }
        else if(mFunction.typeName.toLowerCase().equals(Function.WEB))
        {
            intent = new Intent(context, WebAppActivity.class);
            intent.putExtra("function",mFunction);
            intent.putExtra("iscloud",iscloud);
            if(FunctionUtils.functionUtils.service != null)
            context.startActivity(intent);
        }
        else if(mFunction.typeName.toLowerCase().equals(Function.LEAVE) || mFunction.typeName.toLowerCase().equals(Function.LEAVE_M))
        {
            if (account.mCloundAdminId.length() == 0 || account.mCompanyId.length() == 0 || account.cloudServer.length() == 0) {
                AppUtils.showMessage(context, context.getString(R.string.oaunaccess));
                return;
            }
            else
            {
                if (mFunction.typeName.equals(Function.LEAVE_M)) {
                    Bus.callData(context,"leave/startNewLeave");
                } else {
                    Bus.callData(context,"leave/startLeave");
                }

            }
        }
        else if(mFunction.typeName.toLowerCase().equals(Function.NOTICE) || mFunction.typeName.toLowerCase().equals(Function.NOTICE_M))
        {
            if (account.mCloundAdminId.length() == 0 || account.mCompanyId.length() == 0 || account.cloudServer.length() == 0) {
                AppUtils.showMessage(context, context.getString(R.string.oaunaccess));
                return;
            }
            else
            {
                if (mFunction.typeName.equals(Function.NOTICE_M)) {
                    Bus.callData(context,"notice/startNewNotice");
                } else {
                    Bus.callData(context,"notice/startNotice");
                }

            }
        }
        else if(mFunction.typeName.toLowerCase().equals(Function.WORKATTDENCE) || mFunction.typeName.toLowerCase().equals(Function.WORKATTDENCE_M))
        {
            if (account.mCloundAdminId.length() == 0 || account.mCompanyId.length() == 0 || account.cloudServer.length() == 0) {
                AppUtils.showMessage(context, context.getString(R.string.oaunaccess));
                return;
            }
            else
            {
                if (mFunction.typeName.equals(Function.WORKATTDENCE_M)) {
                    Bus.callData(context,"attendance/startAttdence");
                } else {
                    Bus.callData(context,"attendance/startAttdence");
                }

            }
        }
        else if(mFunction.typeName.toLowerCase().equals(Function.SIGN) || mFunction.typeName.toLowerCase().equals(Function.SIGN_M))
        {
            if (account.mCloundAdminId.length() == 0 || account.mCompanyId.length() == 0 || account.cloudServer.length() == 0) {
                AppUtils.showMessage(context, context.getString(R.string.oaunaccess));
                return;
            }
            else
            {
                if (mFunction.typeName.equals(Function.SIGN_M)) {
                    Bus.callData(context,"sign/startSign");
                } else {
                    Bus.callData(context,"sign/startSign");
                }

            }
        }

        else if(mFunction.typeName.toLowerCase().equals(Function.VOTE) || mFunction.typeName.toLowerCase().equals(Function.VOTE_M))
        {
            if (account.mCloundAdminId.length() == 0 || account.mCompanyId.length() == 0 || account.cloudServer.length() == 0) {
                AppUtils.showMessage(context, context.getString(R.string.oaunaccess));
                return;
            }
            else
            {
                if (mFunction.typeName.equals(Function.VOTE_M)) {
                    Bus.callData(context,"vote/startVoteNew");
                } else {
                    Bus.callData(context,"vote/startVoteMain");
                }

            }
        }
        else if(mFunction.typeName.toLowerCase().equals(Function.WORKREPORT) || mFunction.typeName.toLowerCase().equals(Function.WORKREPORT_M))
        {
            if (account.mCloundAdminId.length() == 0 || account.mCompanyId.length() == 0 || account.cloudServer.length() == 0) {
                AppUtils.showMessage(context, context.getString(R.string.oaunaccess));
                return;
            }
            else
            {
                if (mFunction.typeName.equals(Function.WORKREPORT_M)) {
                    Bus.callData(context,"workreport/startReportMain");
                } else {
                    Bus.callData(context,"workreport/startReportMain");
                }

            }
        }
        else if(mFunction.typeName.toLowerCase().equals(Function.DATE) || mFunction.typeName.toLowerCase().equals(Function.DATE_M))
        {
            if (account.mCloundAdminId.length() == 0 || account.mCompanyId.length() == 0 || account.cloudServer.length() == 0) {
                AppUtils.showMessage(context, context.getString(R.string.oaunaccess));
                return;
            }
            else
            {
                if (mFunction.typeName.equals(Function.DATE_M)) {
                    Bus.callData(context,"schedule/startEvent");
                } else {
                    Bus.callData(context,"schedule/startMain");
                }

            }
        }
        else if(mFunction.typeName.toLowerCase().equals(Function.TASK) || mFunction.typeName.toLowerCase().equals(Function.NEWTASK))
        {
            if (account.mCloundAdminId.length() == 0 || account.mCompanyId.length() == 0 || account.cloudServer.length() == 0) {
                AppUtils.showMessage(context, context.getString(R.string.oaunaccess));
                return;
            }
            else
            {
                if (mFunction.typeName.equals(Function.TASK)) {
                    Bus.callData(context,"task/startManager");
                } else {
                    Bus.callData(context,"task/startTasknew");
                }

            }
        }
        else if(mFunction.typeName.toLowerCase().equals(Function.NEWPROJECT))
        {
            if (account.mCloundAdminId.length() == 0 || account.mCompanyId.length() == 0 || account.cloudServer.length() == 0) {
                AppUtils.showMessage(context, context.getString(R.string.oaunaccess));
                return;
            }
            else
            {
                Bus.callData(context,"task/startAddProject");
            }
        }
        else if(mFunction.typeName.toLowerCase().equals(Function.NEWMAIL))
        {
            if (account.mCloundAdminId.length() == 0 || account.mCompanyId.length() == 0 || account.cloudServer.length() == 0) {
                AppUtils.showMessage(context, context.getString(R.string.oaunaccess));
                return;
            }
            else
            {
                Bus.callData(context,"mail/newMail");
            }
        }
        else if(mFunction.typeName.toLowerCase().equals(Function.CARD))
        {
            if (account.mCloundAdminId.length() == 0 || account.mCompanyId.length() == 0 || account.cloudServer.length() == 0) {
                AppUtils.showMessage(context, context.getString(R.string.oaunaccess));
                return;
            }
            else
            {
                startCard(context);
            }
        }
    }

    public void showWebMessage(Context context,Intent intent1) {
        Intent intent = new Intent(context, WebMessageActivity.class);
        if(intent1.getBooleanExtra("isurl",false) == false)
        {
            intent.putExtra("isurl",false);
            intent.putExtra("data",intent1.getStringExtra("detialid"));
        }
        else
        {
            intent.putExtra("isurl",true);
            intent.putExtra("url",intent1.getStringExtra("detialid"));
        }
//        intent.putExtra("conversation", mConversationModel);
        context.startActivity(intent);
    }

    public void showWebMessage(Context context,Conversation mConversationModel)
    {
        Intent intent = new Intent(context, WebMessageActivity.class);
        if(mConversationModel.sourceId.length() == 0)
        {
            intent.putExtra("isurl",false);
            intent.putExtra("data",mConversationModel.mSubject);
        }
        else
        {
            intent.putExtra("isurl",true);
            intent.putExtra("url",mConversationModel.sourceId);
        }
        intent.putExtra("conversation", mConversationModel);
        context.startActivity(intent);
    }


    public void showWebMessageRemind(Context context,String detialId,String content,String cap,boolean iscloud)
    {
        if(iscloud == false)
        {
            if(cap.toLowerCase().contains("mail"))
            {

            }
            else
            {
                Function info = new Function();
                info.mCaption = cap;
                info.mName = content;
                info.modulflag = content;
                info.mRecordId = detialId;
                Intent intent = new Intent(context, GridDetialActivity.class);
                intent.putExtra("function",info);
                intent.putExtra("edit",false);
                context.startActivity(intent);
            }
        }
        else
        {
            Intent intent = new Intent(context,
                    WebMessageActivity.class);
            intent.putExtra("isurl", true);
            String url = "";
            if(FunctionUtils.getInstance().service.https)
            {
                url = "https://"+FunctionUtils.getInstance().service.sAddress+":"+FunctionUtils.getInstance().service.sPort+"/app/reminder/detail"+cap+"?token="+ NetUtils.getInstance().token
                        +"&rid="+detialId+"&app_languge="+ AppUtils.getLangue(context);
            }
            else
            {
                url = "http://"+FunctionUtils.getInstance().service.sAddress+":"+FunctionUtils.getInstance().service.sPort+"/app/reminder/detail/"+cap+"?token="+NetUtils.getInstance().token
                        +"&rid="+detialId+"&app_languge="+ AppUtils.getLangue(context);
            }
            intent.putExtra("url", url);
            context.startActivity(intent);
        }
    }

    public void showWebMessageRemind(Context context,Conversation mConversationModel,boolean iscloud)
    {
        if(iscloud == false)
        {
            if(mConversationModel.mTitle2.toLowerCase().contains("mail"))
            {

            }
            else
            {
                Function info = new Function();
                info.mCaption = mConversationModel.mTitle2;
                info.mName = mConversationModel.mTitle;
                info.modulflag = mConversationModel.mTitle;
                info.mRecordId = mConversationModel.detialId;
                Intent intent = new Intent(context, GridDetialActivity.class);
                intent.putExtra("function",info);
                intent.putExtra("edit",false);
                context.startActivity(intent);
            }
        }
        else
        {
            Intent intent = new Intent(context,
                    WebMessageActivity.class);
            intent.putExtra("isurl", true);
            String url = "";
            if(FunctionUtils.getInstance().service.https)
            {
                url = "https://"+FunctionUtils.getInstance().service.sAddress+":"+FunctionUtils.getInstance().service.sPort+"/app/reminder/detail"+mConversationModel.mTitle2+"?token="+ NetUtils.getInstance().token
                        +"&rid="+mConversationModel.detialId+"&app_languge="+ AppUtils.getLangue(context);
            }
            else
            {
                url = "http://"+FunctionUtils.getInstance().service.sAddress+":"+FunctionUtils.getInstance().service.sPort+"/app/reminder/detail/"+mConversationModel.mTitle2+"?token="+NetUtils.getInstance().token
                        +"&rid="+mConversationModel.detialId+"&app_languge="+ AppUtils.getLangue(context);
            }
            intent.putExtra("url", url);
            context.startActivity(intent);
        }
    }

    public void startCard(Context context)
    {
        Intent intent = new Intent(context,BusinesCardActivity.class);
        context.startActivity(intent);
    }


    public Commend.CommendFun commendCard = new Commend.CommendFun() {
        @Override
        public void doCommend(Context context) {
            startCard(context);
        }
    };

    public Commend.CommendFun commendWarn = new Commend.CommendFun() {
        @Override
        public void doCommend(Context context) {
            startFunction(context,mFunctions.get(0),account.iscloud);
        }
    };

    public Commend.CommendFun commendWorkFlow = new Commend.CommendFun() {
        @Override
        public void doCommend(Context context) {
            startFunction(context,mFunctions.get(1),account.iscloud);
        }
    };

    public Commend.CommendFun commendWorkReport = new Commend.CommendFun() {
        @Override
        public void doCommend(Context context) {
            Bus.callData(context,"workreport/startReportMain");
        }
    };

    public Commend.CommendFun commendLeave = new Commend.CommendFun() {
        @Override
        public void doCommend(Context context) {
            Bus.callData(context,"leave/startNewLeave");
        }
    };

    public Commend.CommendFun commendLeaveNew = new Commend.CommendFun() {
        @Override
        public void doCommend(Context context) {
            Bus.callData(context,"leave/startLeave");
        }
    };

    public Commend.CommendFun commendNotice = new Commend.CommendFun() {
        @Override
        public void doCommend(Context context) {
            Bus.callData(context,"notice/startNotice");
        }
    };

    public Commend.CommendFun commendNoticeNew = new Commend.CommendFun() {
        @Override
        public void doCommend(Context context) {
            Bus.callData(context,"notice/startNewNotice");
        }
    };


    public Commend.CommendFun commendDateNew = new Commend.CommendFun() {
        @Override
        public void doCommend(Context context) {
            Bus.callData(context,"schedule/startEvent");
        }
    };

    public Commend.CommendFun commendDate = new Commend.CommendFun() {
        @Override
        public void doCommend(Context context) {
            Bus.callData(context,"schedule/startMain");
        }
    };


    public Commend.CommendFun commendTaskManager = new Commend.CommendFun() {
        @Override
        public void doCommend(Context context) {
            Bus.callData(context,"task/startManager");
        }
    };

    public Commend.CommendFun commendTaskNew = new Commend.CommendFun() {
        @Override
        public void doCommend(Context context) {
            Bus.callData(context,"task/startTasknew");
        }
    };

    public Commend.CommendFun commendProjectNew = new Commend.CommendFun() {
        @Override
        public void doCommend(Context context) {
            Bus.callData(context,"task/startAddProject");
        }
    };

    public Commend.CommendFun commendSign = new Commend.CommendFun() {

        @Override
        public void doCommend(Context context) {
            Bus.callData(context,"task/startSign");
        }
    };

    public Commend.CommendFun commendVoteNew = new Commend.CommendFun() {

        @Override
        public void doCommend(Context context) {
            Bus.callData(context,"task/startVoteNew");
        }
    };

    public Commend.CommendFun commendVote = new Commend.CommendFun() {

        @Override
        public void doCommend(Context context) {
            Bus.callData(context,"task/startVoteMain");
        }
    };


    public Commend.CommendFun commendAtt = new Commend.CommendFun() {

        @Override
        public void doCommend(Context context) {
            Bus.callData(context,"task/startAttdence");
        }
    };
}
