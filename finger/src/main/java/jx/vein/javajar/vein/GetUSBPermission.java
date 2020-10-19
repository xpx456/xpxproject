package jx.vein.javajar.vein;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2019/3/25.
 * 获取usb权限
 */

public class GetUSBPermission {


    /**
     * 执行shell adb语句
     */
//    public GetUSBPermission() {
//        String[] commands = new String[] {"cd dev/bus",
//                "chmod -R 777 usb","cd ..","-ls -l" };
//        Process process = null;
//        DataOutputStream dataOutputStream = null;
//        try {
//
//            process = Runtime.getRuntime().exec("su");
//            dataOutputStream = new DataOutputStream(process.getOutputStream());
//            int length = commands.length;
//            for (int i = 0; i < length; i++) {
//                dataOutputStream.writeBytes(commands[i] + "\n");
//            }
//            dataOutputStream.writeBytes("exit\n");
//
//
//            dataOutputStream.flush();
//            process.waitFor();
//            BufferedReader reader = null;
//            String content = "";
//            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            StringBuffer output = new StringBuffer();
//            int read;
//            char[] buffer = new char[4096];
//            while ((read = reader.read(buffer)) > 0) {
//                output.append(buffer, 0, read);
//            }
//            reader.close();
//            content = output.toString();
//            String b = content;
//        } catch (Exception e) {
//
//        } finally {
//            try {
//                if (dataOutputStream != null) {
//                    dataOutputStream.close();
//                }
//                process.destroy();
//            } catch (Exception e) {
//            }
//        }
//    }

//    public GetUSBPermission()
//    {
//        try {
//
//            List<String> cmds = new ArrayList<>();
//            cmds.add("cd /dev/bus");
//            cmds.add("chmod -R 777 usb");
//            Process process = Runtime.getRuntime().exec("su");
//            DataOutputStream os = new DataOutputStream(process.getOutputStream());
//            for (String tmpCmd : cmds) {
//                os.writeBytes(tmpCmd+"\n");
//            }
//            os.writeBytes("exit\n");
//            os.flush();
//            os.close();
//            process.waitFor();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
    public GetUSBPermission()
    {
        runCommand("chmod -R 777 dev/bus/usb");
    }

    public void get()
    {
        Process su;
        try {
            su = Runtime.getRuntime().exec("su");
//            String cmd = "screencap /sdcard/picture.png " + "\n" + "exit\n";
            String cmd = "chmod -R 777 dev/bus/usb "+"\n";
            su.getOutputStream().write(cmd.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String exec(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            int read;
            char[] buffer = new char[4096];
            StringBuffer output = new StringBuffer();
            while ((read = reader.read(buffer)) > 0) {
                output.append(buffer, 0, read);
            }
            reader.close();
            process.waitFor();
            return output.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static String runCommand(String command) {
        Process process = null;
        String result = "";
        DataOutputStream os = null;
        DataInputStream is = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            is = new DataInputStream(process.getInputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            String line = null;
            while ((line = is.readLine()) != null) {
                result += line;
            }
            process.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (process != null) {
                process.destroy();
            }
        }
        return result;
    }

}
