package com.iccard.thread;

import android.util.Log;

import com.iccard.IcCardManager;

import java.io.IOException;

public class ReadThread extends Thread {

    public IcCardManager icCardManager;

    public ReadThread(IcCardManager icCardManager) {
        this.icCardManager = icCardManager;
    }

    @Override
    public void run() {
        super.run();
        Log.i("chuan2", "----string");

        while(!isInterrupted()) {
            int size,size_tmp;
            try {
                //byte[] HEAD = new byte[1];
                byte[] buffer_tmp = new byte[64];
                byte[] buffer = new byte[64];
                if (icCardManager.mInputStream == null) return;

//					String string = IOUtils.toString(mInputStream);
                /*
                 * 卡号读取时，一般都是两个包发送过来，第二个包才是卡号
                 * */
                size = icCardManager.mInputStream.read(buffer);

                Log.i("hgt", "read buffer:"+icCardManager. BytetoHex(buffer));
                icCardManager.parase_pkg(buffer);
                    /*if(size > 0 && buffer[0] == 0x68) {
                        sleep(50);
                        size_tmp = mInputStream.read(buffer_tmp);
                        //Log.i("hgt", " :"+ buffer_tmp.toString());
                        //if(size > 0) {
                            //buffer[0] = 0x68;
                        Log.i("hgt", "read buffer_tmp:"+ BytetoHex(buffer) + " buffer:" + BytetoHex(buffer));
                            System.arraycopy(buffer_tmp, 0, buffer, size, size_tmp);
                            Log.i("hgt", "read buffer:"+ BytetoHex(buffer));
                            parase_pkg(buffer);
                            if((size+size_tmp) > buffer[1]) {
                                System.arraycopy(buffer, buffer[1], buffer_tmp, 0, size-buffer[1]);
                                parase_pkg(buffer_tmp);
                            }
                        //}
                    }*/
                // Thread.sleep(50);

            } catch (IOException e) {
                Log.i("chuan2", e.toString()+"");
                e.printStackTrace();
                return;
            }/* catch (InterruptedException e) {
                    Log.i("chuan2", e.toString()+"");
                    e.printStackTrace();
                    return;
                }*/
        }
    }
}
