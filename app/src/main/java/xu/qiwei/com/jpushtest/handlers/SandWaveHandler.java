package xu.qiwei.com.jpushtest.handlers;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import xu.qiwei.com.jpushtest.Utils;

/**
 * Created by xuqiwei on 17-11-23.
 */

public class SandWaveHandler extends Handler {
    private DatagramSocket waveSocket;
    private int waveQrd=0;
    public SandWaveHandler(Looper looper,DatagramSocket waveSocket) {
        super(looper);
        this.waveSocket = waveSocket;

    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        byte[] arrayOfByte1 = new byte[50];
        arrayOfByte1[0] = 11;
        arrayOfByte1[1] = -60;
        arrayOfByte1[2] = 81;
        arrayOfByte1[3] = 82;
        arrayOfByte1[4] = 68;
        try
        {
            if (waveSocket == null)
                waveSocket = new DatagramSocket(7192);
//            while (WaveViewActivity.this.isRun)
//            {
                arrayOfByte1[5] = 124;
                int i = waveQrd / 1000;
                int j = (waveQrd - i * 1000) / 100;
                int k = (waveQrd - i * 1000 - j * 100) / 10;
                int m = waveQrd - i * 1000 - j * 100 - k * 10;
                arrayOfByte1[6] = ((byte)(i + 48));
                arrayOfByte1[7] = ((byte)(j + 48));
                arrayOfByte1[8] = ((byte)(k + 48));
                arrayOfByte1[9] = ((byte)(m + 48));
                arrayOfByte1[10] = 124;
                arrayOfByte1[11] = 54;
                arrayOfByte1[12] = 124;
                byte[] arrayOfByte2 = Utils.ID.getBytes();
                for (int n = 0; n < arrayOfByte2.length; n++)
                    arrayOfByte1[(n + 13)] = arrayOfByte2[n];
                arrayOfByte1[(13 + arrayOfByte2.length)] = 124;
                arrayOfByte1[(14 + arrayOfByte2.length)] = 0;
                arrayOfByte1[(15 + arrayOfByte2.length)] = 124;
                arrayOfByte1[(16 + arrayOfByte2.length)] = 124;
                arrayOfByte1[(17 + arrayOfByte2.length)] = 124;
                arrayOfByte1[(18 + arrayOfByte2.length)] = 28;
                arrayOfByte1[(19 + arrayOfByte2.length)] = 13;
                DatagramPacket localDatagramPacket = new DatagramPacket(arrayOfByte1, 20 + arrayOfByte2.length, InetAddress.getByName(Utils.ServerIP), 3243);
            waveSocket.send(localDatagramPacket);
            waveQrd = (1 + waveQrd);
                if (waveQrd == 10000)
                    waveQrd = 0;
//                Thread.sleep(5000L);
//            }
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
