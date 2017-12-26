package xu.qiwei.com.jpushtest.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

import xu.qiwei.com.jpushtest.Utils;
import xu.qiwei.com.jpushtest.beans.WaveFormBean;
import xu.qiwei.com.jpushtest.beans.WaveFormBeanShell;

/**
 * Created by xuqiwei on 17-12-26.
 */

public class SurfaceWaveViewV2 extends View {
    //    public SurfaceWaveViewV2(Context context) {
//        super(context);
//    }
    private boolean readyToDrawWave = false;
    private WaveDrawHandler waveDrawHandler;
    private Path path;
    private RectF befRect;
    private RectF aftRect;
//    maxY - minY
    private float maxY;
    private float minY;
    private float x;
    private float y;
    //    倍率
    private float yRate;
    //偏移量
    private float yOffset;
    //    在这个控件中一共需要放置的波形数量
    private int totalWaveCount = 3;
    //    容错率(必须<1/2)
    private static final float FAULT_TOLERANT=1f/3f;
    //    目前放置了多少个波形
    int currentWaveCount = 0;
    private Path mPath = new Path();
    private Canvas canvas;

    public float getMaxY() {
        return maxY;
    }

    public void setMaxY(float maxY) {
        this.maxY = maxY;
    }

    public float getMinY() {
        return minY;
    }

    public void setMinY(float minY) {
        this.minY = minY;
    }

    public SurfaceWaveViewV2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initDatas();
        readyToDrawWave = true;
        drawWave(canvas);

    }

    private void drawWave(Canvas canvas) {
        this.canvas = canvas;
    }

    public void refreshWave(WaveFormBeanShell waveFormBeanShell) {
        if (!readyToDrawWave) {
            return;
        }
        Message message = new Message();
        message.obj = waveFormBeanShell;

        waveDrawHandler.handleMessage(message);
    }

    private class WaveDrawHandler extends Handler {
        public WaveDrawHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.obj instanceof WaveFormBeanShell) {
                WaveFormBeanShell waveFormBeanShell = (WaveFormBeanShell) msg.obj;
                List<WaveFormBean> waveList = waveFormBeanShell.getWaveFormBeanList();
                if (waveList == null || waveList.size() == 0) {
                    return;
                }
                int count = 0;
                float viewWidth = getWidth();
                float widthSpace = viewWidth / (waveList.size() * totalWaveCount);
                while (count <= (waveList.size() - 1)) {
                    WaveFormBean temp = waveList.get(count);
                    x = count * widthSpace + currentWaveCount * waveList.size() * widthSpace;
                    y = caculatedY(getWaveY(temp.getWy()));
                    mPath.lineTo(x,y);
                    this.post(new Runnable() {
                        @Override
                        public void run() {
                            drawing();
                        }
                    });
                    count++;
                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }
        }
    }

    private void drawing() {
        Paint wavePaint = new Paint();
        wavePaint.setColor(Color.YELLOW);
        canvas.drawPath(path,wavePaint);
    }

    private void initDatas() {
        if (maxY==0) {
            maxY = 200;
        }
        float  h =(float) getHeight();
        yOffset = (h)*FAULT_TOLERANT;
        yRate = (h*(1f-FAULT_TOLERANT*2))/(maxY - minY);


        HandlerThread handlerThread = new HandlerThread("refresh-thread");
        handlerThread.start();
        waveDrawHandler = new WaveDrawHandler(handlerThread.getLooper());
        path = new Path();
        befRect = new RectF(0, 0, 0, 0);
        aftRect = new RectF(0, 0, 0, 0);

    }
    //    wavey:１６位转１０位后的数字
    private float caculatedY(int wavey) {
//        int result = (int) (BASELINE - wavey * MULTIPLE_TIMES);
        float result = ((float)(maxY-wavey))*yRate+yOffset;
        return result;
    }

    private int getWaveY(byte originaly) {

        return Utils.bytesToInt(new byte[]{originaly, 0, 0, 0}, 0);
    }
}
