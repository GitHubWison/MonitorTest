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
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;

import xu.qiwei.com.jpushtest.beans.WaveFormBean;
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
    private int drawCount = 0;
    private Rect refreshRect = new Rect(0,0,0,0);
    private Paint paint;
    private SurfaceTestHandler surfaceTestHandler;
    private Path mPath = new Path();
    private float x;
    private float y ;
    private  float BASELINE = 2000;
    private  float MULTIPLE_TIMES = 8;
    private static final int REFRESH_HEADER_WIDTH = 15;
    private RectF befRect = new RectF(0,0,0,0);
    private RectF aftRect = new RectF(0,0,0,0);
    private static final int FLAT_WAVE = 125;
    private WaveDrawFinishCallBack waveDrawFinishCallBack;
//    目前放置了多少个波形

    int currentWaveCount =0;
//    在这个控件中一共需要放置的波形数量
    private int totalWaveCount =3;

    public void setTotalWaveCount(int totalWaveCount) {
        this.totalWaveCount = totalWaveCount;
    }

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
        HandlerThread handlerThread = new HandlerThread("uppp-ui");
        handlerThread.start();
        surfaceTestHandler = new SurfaceTestHandler(handlerThread.getLooper());
//        bitmap = Bitmap.createBitmap(REFRESH_HEADER_WIDTH,getHeight(), Bitmap.Config.ALPHA_8);


    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isDrawing = true;
//        new Thread(this).start();
        //        波形y值在200到(200-FLAT_WAVE)之间
        MULTIPLE_TIMES = (getHeight())/(2*(Math.abs(200-FLAT_WAVE))) +1;
        BASELINE = getHeight()/2+FLAT_WAVE*MULTIPLE_TIMES;
//
//        MULTIPLE_TIMES =1;
//        BASELINE = 1000;
        mPath.reset();
        mPath.moveTo(0,caculatedY(125));

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isDrawing = false;
    }
    public void refreshWave(List<WaveFormBean> list){
        Message message = new Message();
        message.obj = list;
        surfaceTestHandler.sendMessage(message);
    }

    private void drawing() {
        try {
            mCanvas = mSurfaceHolder.lockCanvas();
            Paint befPaint = new Paint();
            befPaint.setColor(drawCount==0?Color.BLACK:Color.RED);
            Paint aftPaint = new Paint();
            aftPaint.setColor(Color.BLACK);
            befRect.set(0,0,x,getHeight());
            if (mCanvas==null) {
                return;
            }
            mCanvas.drawRect(befRect,befPaint);

            mCanvas.drawPath(mPath,paint);
            aftRect.set(x+REFRESH_HEADER_WIDTH,0,x+2*REFRESH_HEADER_WIDTH,getHeight());
            mCanvas.drawRect(aftRect,aftPaint);
        }finally {
            if (mCanvas != null){
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }



    private class  SurfaceTestHandler extends Handler{
        public SurfaceTestHandler(Looper looper) {
            super(looper);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            List<WaveFormBean> list = (List<WaveFormBean>)msg.obj;
            Log.e("nulll",list.size()+"");
            if (list.size()==0) {
                return;
            }
            int count =0;
            float viewWidth = getWidth();


            float widthSpace = viewWidth/(list.size()*totalWaveCount);
            while (count<=(list.size()-1))
            {
                WaveFormBean temp = list.get(count);
                drawing();
                x=count*widthSpace+currentWaveCount*list.size()*widthSpace;
//                x=x+widthSpace;
                y = caculatedY(getWaveY(temp.getWy()));
                mPath.lineTo(x,y);
                count++;
            }
            if (waveDrawFinishCallBack!=null) {
                waveDrawFinishCallBack.onDrawWaveFinish();
            }
            currentWaveCount++;
            if (currentWaveCount==totalWaveCount) {
                x=0;
                y=0;
                befRect.set(0,0,0,0);
                aftRect.set(0,0,0,0);
                mPath.reset();
                mPath.moveTo(0,caculatedY(125));
                currentWaveCount=0;
            }
        }
    }

    private int caculatedY(int wavey){
        int result = (int)(BASELINE - wavey* MULTIPLE_TIMES);
        return result;
    }
    private int getWaveY(byte originaly) {

        return Utils.bytesToInt(new byte[]{ originaly, 0, 0, 0}, 0);
    }


}
