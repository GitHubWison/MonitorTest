package xu.qiwei.com.jpushtest.handlers;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by xuqiwei on 17-12-18.
 */

public class HeartBeatReciveHandler extends Handler {
    private DatagramSocket heartBeatSocket;

    public HeartBeatReciveHandler(Looper looper, DatagramSocket heartBeatSocket) {
        super(looper);
        this.heartBeatSocket = heartBeatSocket;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        try {
            if (heartBeatSocket == null)
                heartBeatSocket = new DatagramSocket(3242);
//            while (true)
//            {
            byte[] arrayOfByte = new byte[4];
            DatagramPacket localDatagramPacket = new DatagramPacket(arrayOfByte, arrayOfByte.length);
//                WaveViewActivity.this.receiveTime.setToNow();
            heartBeatSocket.receive(localDatagramPacket);
//                System.out.println("已经接受在线包");
            if ((arrayOfByte[0] == 11) && (arrayOfByte[1] == -46) && (arrayOfByte[2] == 28) && (arrayOfByte[3] == 13)) {
//                    成功接收在线包

            }
//            }
        } catch (UnknownHostException | SocketException localUnknownHostException) {
            localUnknownHostException.printStackTrace();
            return;
        } catch (IOException localIOException) {
            localIOException.printStackTrace();
        }
    }


}

