package intersky.talk;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TalkUtils {


    public static volatile TalkUtils mTalkUtils;

    public static synchronized TalkUtils init(Context context) {
        if (mTalkUtils == null) {
            synchronized (TalkUtils.class) {
                if (mTalkUtils == null) {
                    mTalkUtils = new TalkUtils();
                    FaceConversionUtil.getInstace().getFileText(context);
                    FaceConversionUtil.getInstace().getFileText_g(context);
                }
            }
        }
        return mTalkUtils;
    };

    public static TalkUtils getInstance() {
        return mTalkUtils;
    }

    public List<String> getEmojiFilePng(Context context) {
        try {
            List<String> list = new ArrayList<String>();
            InputStream in = context.getResources().getAssets().open("emoji1");// �ļ�����Ϊrose.txt
            BufferedReader br = new BufferedReader(new InputStreamReader(in,
                    "UTF-8"));
            String str = null;
            while ((str = br.readLine()) != null) {
                list.add(str);
            }

            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    public List<String> getEmojiFileGif(Context context) {
        try {
            List<String> list = new ArrayList<String>();
            InputStream in = context.getResources().getAssets().open("emoji2");// �ļ�����Ϊrose.txt
            BufferedReader br = new BufferedReader(new InputStreamReader(in,
                    "UTF-8"));
            String str = null;
            while ((str = br.readLine()) != null) {
                list.add(str);
            }

            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
