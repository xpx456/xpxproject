package intersky.appbase.bundle;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import intersky.appbase.bus.BusInit;
import intersky.appbase.bus.BusManager;

/**
 * @author Zhenhua on 2017/7/12.
 * @email zhshan@ctrip.com ^.^
 */
public class BaseLibsInit {

    public static void init(Application application) {
        initBusAndBundle(application);
    }

    private static void initBusAndBundle(Application application) {
        BusInit busInit = new BusInit();

        //Bus manager init
        BusManager.init(busInit);

        //all bundles setted
        configBundles(application, busInit);
    }

    private static void configBundles(Context context, BusInit busInit) {
        List<BundleConfigModel> bundleConfigModels = new ArrayList<>();

        //conversation
        bundleConfigModels.add(new BundleConfigModel(
                "conversation",
                "intersky.conversation",
                "intersky.conversation.bus.ConversationBusObject",
                BundleConfigModel.BundleLoadType.AutoLoad
        ));
        //filetools
        bundleConfigModels.add(new BundleConfigModel(
                "filetools",
                "intersky.filetools",
                "intersky.filetools.bus.FileBusObject",
                BundleConfigModel.BundleLoadType.AutoLoad
        ));
        bundleConfigModels.add(new BundleConfigModel(
                "chat",
                "intersky.chat",
                "intersky.chat.bus.ChatBusObject",
                BundleConfigModel.BundleLoadType.AutoLoad
        ));
        bundleConfigModels.add(new BundleConfigModel(
                "document",
                "intersky.document",
                "intersky.document.bus.DocumentBusObject",
                BundleConfigModel.BundleLoadType.AutoLoad
        ));
        bundleConfigModels.add(new BundleConfigModel(
                "mail",
                "intersky.mail",
                "intersky.mail.bus.MailBusObject",
                BundleConfigModel.BundleLoadType.AutoLoad
        ));
        bundleConfigModels.add(new BundleConfigModel(
                "leave",
                "intersky.leave",
                "intersky.leave.bus.LeaveBusObject",
                BundleConfigModel.BundleLoadType.AutoLoad
        ));
        bundleConfigModels.add(new BundleConfigModel(
                "task",
                "intersky.task",
                "intersky.task.bus.TaskBusObject",
                BundleConfigModel.BundleLoadType.AutoLoad
        ));
        bundleConfigModels.add(new BundleConfigModel(
                "notice",
                "intersky.notice",
                "intersky.notice.bus.NoticeBusObject",
                BundleConfigModel.BundleLoadType.AutoLoad
        ));
        bundleConfigModels.add(new BundleConfigModel(
                "workreport",
                "intersky.workreport",
                "intersky.workreport.bus.WorkreportBusObject",
                BundleConfigModel.BundleLoadType.AutoLoad
        ));
        bundleConfigModels.add(new BundleConfigModel(
                "vote",
                "intersky.vote",
                "intersky.vote.bus.VoteBusObject",
                BundleConfigModel.BundleLoadType.AutoLoad
        ));
        bundleConfigModels.add(new BundleConfigModel(
                "function",
                "intersky.function",
                "intersky.function.bus.FunctionBusObject",
                BundleConfigModel.BundleLoadType.AutoLoad
        ));
        bundleConfigModels.add(new BundleConfigModel(
                "riche",
                "intersky.riche",
                "intersky.riche.bus.RicheBusObject",
                BundleConfigModel.BundleLoadType.AutoLoad
        ));
        bundleConfigModels.add(new BundleConfigModel(
                "app",
                "com.intersky",
                "com.intersky.android.bus.AppBusObject",
                BundleConfigModel.BundleLoadType.AutoLoad
        ));
        bundleConfigModels.add(new BundleConfigModel(
                "schedule",
                "intersky.schedule",
                "intersky.schedule.bus.ScheduleBusObject",
                BundleConfigModel.BundleLoadType.AutoLoad
        ));
        bundleConfigModels.add(new BundleConfigModel(
                "sign",
                "intersky.sign",
                "intersky.sign.bus.SignBusObject",
                BundleConfigModel.BundleLoadType.AutoLoad
        ));
        bundleConfigModels.add(new BundleConfigModel(
                "attendance",
                "intersky.attendance",
                "intersky.attendance.bus.AttendanceBusObject",
                BundleConfigModel.BundleLoadType.AutoLoad
        ));
        BundleConfigFactory.configBundles(bundleConfigModels);

    }
}
