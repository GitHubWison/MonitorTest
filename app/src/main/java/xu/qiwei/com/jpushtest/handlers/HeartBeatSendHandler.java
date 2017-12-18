package xu.qiwei.com.jpushtest.handlers;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import xu.qiwei.com.jpushtest.Utils;

/**
 * Created by xuqiwei on 17-12-18.
 * 心跳检测
 */

public class HeartBeatSendHandler extends Handler {
    private DatagramSocket heartBeatSocket;

    public HeartBeatSendHandler(Looper looper, DatagramSocket heartBeatSocket) {
        super(looper);
        this.heartBeatSocket = heartBeatSocket;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        byte[] arrayOfByte = {11, -47, 28, 13};
        try {
            if (heartBeatSocket == null)
                heartBeatSocket = new DatagramSocket(3242);
            DatagramPacket localDatagramPacket = new DatagramPacket(arrayOfByte, arrayOfByte.length, InetAddress.getByName(Utils.ServerIP), 3242);
            heartBeatSocket.send(localDatagramPacket);
            Thread.sleep(3000L);
//            }
        } catch (InterruptedException | IOException localUnknownHostException) {
          localUnknownHostException.printStackTrace();
        }
    }
}



