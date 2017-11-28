package xu.qiwei.com.jpushtest.handlers;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xuqiwei on 17-11-23.
 */

public class ReceiveWaveHandler extends Handler {
    private DatagramSocket waveSocket;

    public ReceiveWaveHandler(Looper looper, DatagramSocket waveSocket) {
        super(looper);
        this.waveSocket = waveSocket;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        try {
            byte[] bytes = new byte[]{12, 12};
            byte[] arrayOfByte = new byte[1000];
            DatagramPacket localDatagramPacket = new DatagramPacket(arrayOfByte, arrayOfByte.length);
            waveSocket.receive(localDatagramPacket);
            StringBuffer arrayByteStr = new StringBuffer();
            arrayByteStr.append("new byte[]{");
            for (int i = 0; i < arrayOfByte.length; i++) {
                arrayByteStr.append(arrayOfByte[i]).append(",");
            }
            arrayByteStr.append("};");
//            把信息放到平板中
            saveMonitorInfo(arrayByteStr.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveMonitorInfo(String monitorData) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String directory = Environment.getExternalStorageDirectory().getPath()+"/monitordata/"+simpleDateFormat.format(new Date())+".txt";
        File file = new File(directory);
        File dirFile = new File(Environment.getExternalStorageDirectory().getPath()+"/monitordata");
//        if (!dirFile.exists()) {
//            dirFile.mkdirs();
//        }
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            pw.print(monitorData);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public int bytesToInt(byte[] paramArrayOfByte, int paramInt) {
        return 0xFF & paramArrayOfByte[paramInt] | (0xFF & paramArrayOfByte[(paramInt + 1)]) << 8 | (0xFF & paramArrayOfByte[(paramInt + 2)]) << 16 | (0xFF & paramArrayOfByte[(paramInt + 3)]) << 24;
    }
}
