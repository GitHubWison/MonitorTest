package xu.qiwei.com.jpushtest;

import android.os.Bundle;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Random;

import xu.qiwei.com.jpushtest.handlers.PatientListHandler;
import xu.qiwei.com.jpushtest.handlers.ReceiveWaveHandler;
import xu.qiwei.com.jpushtest.handlers.SandWaveHandler;
import xu.qiwei.com.jpushtest.handlers.SendPatientListHandler;

public class HandlerTestActivity extends AppCompatActivity {
    private Button message_get_button;
    private Button patient_list_get_button;
    private Button sand_wave_button;
    private Button receive_wave_button;

    private PatientListHandler handler;
    private SendPatientListHandler sendPatientListHandler;
    private SandWaveHandler sandWaveHandler;
    private ReceiveWaveHandler receiveWaveHandler;
    private DatagramSocket listSocket;
    private DatagramSocket waveSocket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler_test);
        initDatas();
        initViews();
        initEvents();
        initThreads();

    }

    private void initThreads() {
        try {
            listSocket = new DatagramSocket(3243);
            waveSocket = new DatagramSocket(7192);

            HandlerThread myHandlerThread = new HandlerThread("handler_patient_list_thread");
            myHandlerThread.start();
            handler = new PatientListHandler(myHandlerThread.getLooper(), listSocket);

            HandlerThread patientListHandlerThread = new HandlerThread("handler_patient_list_thread_2");
            patientListHandlerThread.start();
            sendPatientListHandler = new SendPatientListHandler(patientListHandlerThread.getLooper(), listSocket);

            HandlerThread sendWaveHandlerThread = new HandlerThread("sendWaveHandlerThread");
            sendWaveHandlerThread.start();
            sandWaveHandler = new SandWaveHandler(sendWaveHandlerThread.getLooper(),waveSocket);

            HandlerThread receiveWaveHandlerThread = new HandlerThread("receiveWaveHandlerThread");
            receiveWaveHandlerThread.start();
            receiveWaveHandler = new ReceiveWaveHandler(receiveWaveHandlerThread.getLooper(),waveSocket);

        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private void initDatas() {
    }

    private void initViews() {
        message_get_button = (Button) findViewById(R.id.message_get_button);
        patient_list_get_button = (Button) findViewById(R.id.patient_list_get_button);
        sand_wave_button = (Button)findViewById(R.id.sand_wave_button);
        receive_wave_button = (Button)findViewById(R.id.receive_wave_button);
    }

    private void initEvents() {
        message_get_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMonitor();
            }
        });
        patient_list_get_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getPatientList();
            }
        });
        sand_wave_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendWave();
            }
        });
        receive_wave_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receiveWave();
            }
        });
    }

    private void receiveWave() {
        receiveWaveHandler.sendEmptyMessage(new Random().nextInt(10));
    }

    private void sendWave() {
        sandWaveHandler.sendEmptyMessage(new Random().nextInt(10));

    }

    private void getPatientList() {
        sendPatientListHandler.sendEmptyMessage(new Random().nextInt(20));
    }

    private void getMonitor() {
        handler.sendEmptyMessage(new Random().nextInt(10));
    }

}
