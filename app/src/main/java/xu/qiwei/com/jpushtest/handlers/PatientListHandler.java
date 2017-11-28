package xu.qiwei.com.jpushtest.handlers;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by xuqiwei on 17-11-23.
 */

public class PatientListHandler extends Handler {
    private DatagramSocket listSocket;

    public PatientListHandler(Looper looper,DatagramSocket listSocket) {
        super(looper);
        this.listSocket = listSocket;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        Log.e("threadName==",Thread.currentThread().getName());
        Log.e("msgwhat==",msg.what+"");
        getPatientList();

    }
    private void testHandler(){
        Log.e("socket_msg==","test_start");
    }
    private void getPatientList(){
        try {
//            DatagramSocket listSocket = new DatagramSocket(3243);

            byte[] arrayOfByte = new byte[1000];
            DatagramPacket localDatagramPacket = new DatagramPacket(arrayOfByte, arrayOfByte.length);
            listSocket.receive(localDatagramPacket);
            String str = new String(arrayOfByte, "US-ASCII");
            System.out.println("获取数据=="+str+"");
            String resultstr = new String(localDatagramPacket.getData() , localDatagramPacket.getOffset() , localDatagramPacket.getLength());
            System.out.println("获取数据2=="+resultstr+"");
            if ((arrayOfByte[0] == 11) && (arrayOfByte[1] == -61))
            {
                String[] arrayOfString = str.split("ID");
                Log.e("","");
//                WaveViewActivity.this.patientnum = arrayOfString.length;
//                WaveViewActivity.this.patients = ((String[][]) Array.newInstance(String.class, new int[] { WaveViewActivity.this.patientnum, 12 }));
//                for (int j = 1; j < arrayOfString.length; j++)
//                    WaveViewActivity.this.patients[j] = arrayOfString[j].split("\\|");
            }

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
