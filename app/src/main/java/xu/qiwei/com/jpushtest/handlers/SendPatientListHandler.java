package xu.qiwei.com.jpushtest.handlers;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import xu.qiwei.com.jpushtest.Utils;

/**
 * Created by xuqiwei on 17-11-23.
 * 发送患者列表的请求
 */

public class SendPatientListHandler extends Handler {
    private DatagramSocket listSocket;
    int qrd = 0;
    public SendPatientListHandler(Looper looper,DatagramSocket listSocket) {
        super(looper);
        this.listSocket = listSocket;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        sendForList();

    }
    public void sendForList()
    {

        byte[] arrayOfByte1 = new byte[40];
        arrayOfByte1[0] = 11;
        arrayOfByte1[1] = -61;
        arrayOfByte1[2] = 81;
        arrayOfByte1[3] = 82;
        arrayOfByte1[4] = 68;
        try
        {
//            DatagramSocket listSocket = new DatagramSocket(3243);
            arrayOfByte1[5] = 124;
            int j = this.qrd / 1000;
            int k = (this.qrd - j * 1000) / 100;
            int m = (this.qrd - j * 1000 - k * 100) / 10;
            int n = this.qrd - j * 1000 - k * 100 - m * 10;
            arrayOfByte1[6] = ((byte)(j + 48));
            arrayOfByte1[7] = ((byte)(k + 48));
            arrayOfByte1[8] = ((byte)(m + 48));
            arrayOfByte1[9] = ((byte)(n + 48));
            arrayOfByte1[10] = 124;
            arrayOfByte1[11] = 52;
            arrayOfByte1[12] = 124;
            byte[] arrayOfByte2 = Utils.ID.getBytes();
            for (int i1 = 0; i1 < arrayOfByte2.length; i1++)
                arrayOfByte1[(i1 + 13)] = arrayOfByte2[i1];
            arrayOfByte1[(13 + arrayOfByte2.length)] = 124;
            arrayOfByte1[(14 + arrayOfByte2.length)] = 124;
            arrayOfByte1[(15 + arrayOfByte2.length)] = 124;
            arrayOfByte1[(16 + arrayOfByte2.length)] = 28;
            arrayOfByte1[(17 + arrayOfByte2.length)] = 13;
            byte[] arrayOfByte3 = new byte[18 + arrayOfByte2.length];
            for (int i2 = 0; i2 < 18 + arrayOfByte2.length; i2++)
                arrayOfByte3[i2] = arrayOfByte1[i2];
            DatagramPacket localDatagramPacket = new DatagramPacket(arrayOfByte3, arrayOfByte3.length, InetAddress.getByName(Utils.ServerIP), 3243);
           listSocket.send(localDatagramPacket);
//           int localPort =listSocket.getLocalPort();
//            String str = new String(arrayOfByte3, "GBK");
            String str = new String(arrayOfByte3, "US-ASCII");
            Log.e("获取患者信息数据：---" , str);




            this.qrd = (1 + this.qrd);
            if (this.qrd == 10000)
                this.qrd = 0;
            return;
        }
        catch (UnknownHostException localUnknownHostException)
        {
            localUnknownHostException.printStackTrace();
            return;
        }
        catch (SocketException localSocketException)
        {
            localSocketException.printStackTrace();
            return;
        }
        catch (IOException localIOException)
        {
            localIOException.printStackTrace();
        }
    }
}
