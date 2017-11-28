package xu.qiwei.com.jpushtest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

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

        drawWave(canvas);
    }

    private void drawWave(Canvas canvas) {
        this.canVas = canvas;
        canVas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        paint = new Paint();
        paint.setStrokeWidth(2);
        paint.setColor(Color.BLUE);
        if (datas.size() != 0) {
//            int singleWidth = getWidth() / datas.size();
//            Log.e("singleWidth==",singleWidth+"");
//            Log.e("totalwidth==",getWidth()+"");
            for (int i = 0; i < datas.size(); i++) {
                WaveFormBean temp = datas.get(i);
//                temp.setX(singleWidth);
                if ((i + 1) < (datas.size() - 1)) {
                    WaveFormBean temp2 = datas.get(i + 1);
//                    canVas.drawLine(i * singleWidth, BASE_LINE -getWaveY(temp.getY())*TIMS, (i + 1) * singleWidth,
//                            BASE_LINE-getWaveY(temp2.getY())*TIMS, paint);
                    canVas.drawLine(temp.getX(), BASE_LINE -getWaveY(temp.getY())*TIMS, temp2.getX(),
                            BASE_LINE-getWaveY(temp2.getY())*TIMS, paint);
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
//            final List<WaveFormBean> list = (List<WaveFormBean>) msg.obj;
//            WaveView.this.post(new Runnable() {
//                @Override
//                public void run() {
//                    datas.clear();
//                    datas.addAll(list);
//                    invalidateWithAni();
//
//                }
//            });

            final List<WaveFormBean> list = (List<WaveFormBean>) msg.obj;
            int count = 0;
            int singleWidth = getWidth() / list.size();
            while (count<list.size()) {
                final int finalCount = count;
                list.get(count).setX(count*singleWidth);
                WaveView.this.post(new Runnable() {
                    @Override
                    public void run() {
                        showUI(list.get(finalCount),finalCount);
                    }
                });
                count++;
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            datas=new ArrayList<>();
            Log.e("datassize==",count+"");
//            datas.removeAll();

        }

        private void showUI(WaveFormBean waveFormBean, int index) {
            datas.add(waveFormBean);
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
