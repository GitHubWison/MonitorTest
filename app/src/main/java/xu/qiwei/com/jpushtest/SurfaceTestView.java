package xu.qiwei.com.jpushtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;

import xu.qiwei.com.jpushtest.beans.WaveFormBean;
import xu.qiwei.com.jpushtest.beans.WaveFormBeanShell;
import xu.qiwei.com.jpushtest.interfaces.WaveDrawFinishCallBack;

/**
 * Created by xuqiwei on 17-12-4.
 */

public class SurfaceTestView extends SurfaceView implements SurfaceHolder.Callback {
    // SurfaceHolder
    private SurfaceHolder mSurfaceHolder;
    // 画布
    private Canvas mCanvas;
    // 子线程标志位
    private boolean isDrawing;
    //    private int drawCount = 0;
    private Rect refreshRect = new Rect(0, 0, 0, 0);
    private Paint paint;
    private SurfaceTestHandler surfaceTestHandler;
    private Path mPath = new Path();
    private float x;
    private float y;
//    private float BASELINE = -1;
//    private float MULTIPLE_TIMES = -1;
    private static final int REFRESH_HEADER_WIDTH = 15;
    private RectF befRect = new RectF(0, 0, 0, 0);
    private RectF aftRect = new RectF(0, 0, 0, 0);
    private static final int FLAT_WAVE = 50;
    private WaveDrawFinishCallBack waveDrawFinishCallBack;
//    这个控件容纳正常波形的高度
    private float viewWaveHeight;
//    倍率
    private float yRate;
//偏移量
    private float yOffset;
//    容错率
    private static final float FAULT_TOLERANT=1f/4f;

//    波形正常最大值
    private int maxY = 0;
//    波形正常最小值
    private int minY = 0;

    public int getMaxY() {
        return maxY;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    public int getMinY() {
        return minY;
    }

    public void setMinY(int minY) {
        this.minY = minY;
    }
    //    目前放置了多少个波形

    int currentWaveCount = 0;
    //    在这个控件中一共需要放置的波形数量
    private int totalWaveCount = 3;

    public void setTotalWaveCount(int totalWaveCount) {
        this.totalWaveCount = totalWaveCount;
    }

//    public void setMULTIPLE_TIMES(float MULTIPLE_TIMES) {
//        this.MULTIPLE_TIMES = MULTIPLE_TIMES;
//    }
//
//    public void setBASELINE(float BASELINE) {
//        this.BASELINE = BASELINE;
//    }

    private Bitmap bitmap;

    public SurfaceTestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public void setWaveDrawFinishCallBack(WaveDrawFinishCallBack waveDrawFinishCallBack) {
        this.waveDrawFinishCallBack = waveDrawFinishCallBack;
    }

    private void init() {
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);
        paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        HandlerThread handlerThread = new HandlerThread("uppp-ui");
        handlerThread.start();
        surfaceTestHandler = new SurfaceTestHandler(handlerThread.getLooper());
//        bitmap = Bitmap.createBitmap(REFRESH_HEADER_WIDTH,getHeight(), Bitmap.Config.ALPHA_8);


    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isDrawing = true;
        if (maxY==0) {
            maxY = 200;
        }
        float  h =(float) getHeight();
        yOffset = (h)/6f;
        yRate = (h*(1f-FAULT_TOLERANT*2))/(maxY - minY);

//        new Thread(this).start();
        //        波形y值在200到(200-FLAT_WAVE)之间
//        if (MULTIPLE_TIMES < 0) {
//            MULTIPLE_TIMES = (getHeight()) / (2 * (Math.abs(200 - FLAT_WAVE))) + 1;
//        }
//        if (BASELINE < 0) {
//            BASELINE = getHeight() / 2 + FLAT_WAVE * MULTIPLE_TIMES;
//        }

//
//        MULTIPLE_TIMES =1;
//        BASELINE = 1000;
        mPath.reset();
        mPath.moveTo(0, caculatedY(125));

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isDrawing = false;
    }

    public void refreshWave(WaveFormBeanShell waveFormBeanShell) {
        Message message = new Message();
        message.obj = waveFormBeanShell;
        surfaceTestHandler.sendMessage(message);
    }

    private void drawing() {
        try {
            mCanvas = mSurfaceHolder.lockCanvas();
            Paint befPaint = new Paint();
            befPaint.setColor(Color.BLACK);
            Paint aftPaint = new Paint();
            aftPaint.setColor(Color.BLACK);
            befRect.set(0, 0, x, getHeight());
            if (mCanvas == null) {
                return;
            }
            mCanvas.drawRect(befRect, befPaint);

            mCanvas.drawPath(mPath, paint);
            aftRect.set(x + REFRESH_HEADER_WIDTH, 0, x + 2 * REFRESH_HEADER_WIDTH, getHeight());
            mCanvas.drawRect(aftRect, aftPaint);
        } finally {
            if (mCanvas != null) {
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }


    private class SurfaceTestHandler extends Handler {
        public SurfaceTestHandler(Looper looper) {
            super(looper);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            WaveFormBeanShell waveFormBeanShell = (WaveFormBeanShell) msg.obj;
            List<WaveFormBean> list = waveFormBeanShell.getWaveFormBeanList();
            int count = 0;
            float viewWidth = getWidth();
            float widthSpace = viewWidth / (list.size() * totalWaveCount);
//            只有当计算出单个ｘ大于０时才能开始绘制和计算信息
            if ((widthSpace > 0) && (list.size() > 0)) {
                while (count <= (list.size() - 1)) {
                    WaveFormBean temp = list.get(count);
                    x = count * widthSpace + currentWaveCount * list.size() * widthSpace;
                    y = caculatedY(getWaveY(temp.getWy()));
                    if (count == 0 && currentWaveCount == 0) {
//                    初始值
                        mPath.moveTo(x, y);
                    }
                    mPath.lineTo(x, y);
                    drawing();
                    count++;
                }
                currentWaveCount++;
                if (currentWaveCount == totalWaveCount) {
//                    画笔回到原来的状态
                    x = 0;
                    y = 0;
                    befRect.set(0, 0, 0, 0);
                    aftRect.set(0, 0, 0, 0);
                    mPath.reset();
//                    mPath.moveTo(0, caculatedY(125));
                    currentWaveCount = 0;
                }
            } else {
//防止循环过于频繁暂停１秒
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (waveDrawFinishCallBack != null) {
                waveDrawFinishCallBack.onDrawWaveFinish();
            }
        }
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
