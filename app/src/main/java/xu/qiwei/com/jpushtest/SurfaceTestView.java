package xu.qiwei.com.jpushtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;

import xu.qiwei.com.jpushtest.beans.WaveFormBean;

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
    private int x;
    private int y ;
    private final int BASELINE = 2000;
    private final int MULTIPLE_TIMES = 8;
    private static final int REFRESH_HEADER_WIDTH = 30;
    private Rect befRect = new Rect(0,0,0,0);
    private Rect aftRect = new Rect(0,0,0,0);


    private Bitmap bitmap;

    public SurfaceTestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

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
            befRect.set(0,0,x,getWidth());
            mCanvas.drawRect(befRect,befPaint);

            mCanvas.drawPath(mPath,paint);
            aftRect.set(x+REFRESH_HEADER_WIDTH,0,x+2*REFRESH_HEADER_WIDTH,getWidth());

            mCanvas.drawRect(aftRect,aftPaint);


        }finally {
            if (mCanvas != null){
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }


//    public void createLayer() {
//        Rect rect = new Rect(0,0,getWidth(),getHeight());
//        Paint paint = new Paint();
//        paint.setColor(Color.WHITE);
//        mCanvas = mSurfaceHolder.lockCanvas();
//        mCanvas.saveLayerAlpha(0,0,getWidth(),getHeight(),127);
//        paint.setColor(Color.YELLOW);
//        mCanvas.drawLine(0,0,300,300,paint);
//        mCanvas.restore();
//        mSurfaceHolder.unlockCanvasAndPost(mCanvas);
//    }

    private class  SurfaceTestHandler extends Handler{
        public SurfaceTestHandler(Looper looper) {
            super(looper);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            List<WaveFormBean> list = (List<WaveFormBean>)msg.obj;
            int count =0;
            int widthSpace = getWidth()/list.size();
            while (count<(list.size()-1))
            {
                WaveFormBean temp = list.get(count);
                drawing();
                x=count*widthSpace;
                y = caculatedY(getWaveY(temp.getWy()));
                mPath.lineTo(x,y);
                count++;
            }
            x=0;
            y=0;
            befRect.set(0,0,0,0);
            aftRect.set(0,0,0,0);
            mPath.moveTo(0,0);
            mPath.reset();
//            mCanvas.
//            drawCount++;
        }
    }

    private int caculatedY(int wavey){
        int result = BASELINE - wavey* MULTIPLE_TIMES;
        return result;
    }
    private int getWaveY(byte originaly) {

        return Utils.bytesToInt(new byte[]{ originaly, 0, 0, 0}, 0);
    }


}
