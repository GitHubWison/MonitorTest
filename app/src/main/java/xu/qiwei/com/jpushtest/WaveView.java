package xu.qiwei.com.jpushtest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import xu.qiwei.com.jpushtest.beans.WaveFormBean;
import xu.qiwei.com.jpushtest.interfaces.WaveFormInterface;

/**
 * Created by xuqiwei on 17-11-24.
 */

public class WaveView extends View implements WaveFormInterface {
    private Canvas canVas;
    private Paint paint;
    private int listCount = 0;
    private static final int BASE_LINE = 1000;
    private static final int TIMS = 4;
    private List<WaveFormBean> datas = new ArrayList<>();
//    private UpdateUIHandler updateUIHandler;


    public WaveView(Context context) {
        super(context);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canVas = canvas;
        drawWave(canvas);
//        drawPathWave();
    }
    private void drawPathWave(){
        //初始化Paint
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10f);
//初始化Path
        Path path = new Path();
//将坐标系原点从（0,0）移动到（100,100）
//        path.moveTo(100, 100);
//画从（100,100）到（400,400）之间的直线
        path.lineTo(400, 400);
//path.rMoveTo(0, 100); //暂时注释
        path.lineTo(400, 800);
        canVas.drawPath(path, paint);

    }

    private void drawWave(Canvas canvas) {

        canVas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        paint = new Paint();
        paint.setStrokeWidth(5);
        paint.setColor(Color.BLUE);
        if (datas.size() != 0) {
            for (int i = 0; i < datas.size(); i++) {
                final WaveFormBean temp = datas.get(i);
                if ((i + 1) < (datas.size() - 1)) {
                     WaveFormBean temp2 = datas.get(i + 1);
                    if (temp!=null&&temp2!=null) {
                        canVas.drawLine(temp.getX(), BASE_LINE -getWaveY(temp.getY())*TIMS, temp2.getX(),
                                BASE_LINE-getWaveY(temp2.getY())*TIMS, paint);
                        if ((i-1)>0) {
//                            Log.e("temp3","temp3");
                            paint.setColor(Color.YELLOW);
                            WaveFormBean temp3 = datas.get(i-1);
                            canVas.drawLine(temp.getX(), BASE_LINE -getWaveY(temp.getY())*TIMS, temp3.getX(),
                                    BASE_LINE-getWaveY(temp3.getY())*TIMS, paint);
                        }
                    }else
                    {
                        Toast.makeText(getContext(), "nulllllllllllllllllll", Toast.LENGTH_SHORT).show();
                    }
                    paint.setColor(Color.BLUE);
                }
            }
        }
    }

    private int getWaveY(int originaly) {

        return Utils.bytesToInt(new byte[]{(byte) originaly, 0, 0, 0}, 0);
    }


    @Override
    public void refreshData(List<WaveFormBean> waveFormBeanList) {
        listCount = waveFormBeanList.size();
        HandlerThread handlerThread = new HandlerThread("updateui");
        handlerThread.start();
        UpdateUIHandler updateUIHandler = new UpdateUIHandler(handlerThread.getLooper());
        Message message = new Message();
        message.obj = waveFormBeanList;
        updateUIHandler.sendMessage(message);

    }

    //    通知ｕｉ更新
    @SuppressWarnings("unchecked")
    private class UpdateUIHandler extends Handler {
        public UpdateUIHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            datas.clear();
            final List<WaveFormBean> list = (List<WaveFormBean>) msg.obj;
            int count = 0;
            int singleWidth = getWidth() / list.size();
            while (count<list.size()) {
                WaveFormBean waveFormBean = list.get(count);
                waveFormBean.setX(count*singleWidth);
                datas.add(waveFormBean);
                WaveView.this.post(new Runnable() {
                    @Override
                    public void run() {
                        invalidate();
                    }
                });

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count++;
            }
            datas.clear();

        }

        private void showUI(WaveFormBean waveFormBean, int index,int totalCount) {
            datas.add(waveFormBean);
            if (index>=totalCount-1) {
                datas.clear();
            }
            invalidate();
        }
    }

    private void invalidateWithAni() {
        for (WaveFormBean data : datas) {
            Log.e("datax==",data.getX()+"");
        }
        invalidate();
    }


}
