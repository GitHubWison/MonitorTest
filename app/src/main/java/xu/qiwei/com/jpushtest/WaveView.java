package xu.qiwei.com.jpushtest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import xu.qiwei.com.jpushtest.beans.WaveFormBean;
import xu.qiwei.com.jpushtest.interfaces.WaveFormInterface;

/**
 * Created by xuqiwei on 17-11-24.
 */

public class WaveView extends View implements WaveFormInterface {
//    private Canvas canVas;
    private Paint paint;
    private int listCount = 0;
    private static int BASE_LINE = 1000;
    private static final int TIMS = 4;
    //    待更新的位置
    private int count = 0;
    private List<WaveFormBean> datas = new ArrayList<>();
    private List<WaveFormBean> newDatas = new ArrayList<>();
//    private int refreshstartx = 0;
//    private int refreshstarty = 0;
//    private int refreshendx = 0;
//    private int refreshendy = 0;

    private int strokeOne = 100;
    private int strokeTwo = 150;
    private List<WaveFormBean> spaceWave = new ArrayList<>();
    private Rect refreshRect = new Rect(0,0,0,0);
    private static final int REFRESH_WIDTH = 20;
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
//        drawTest(canvas);
    }

    private void drawTest(Canvas canvas) {
        paint = new Paint();
        paint.setColor(Color.BLUE);

        canvas.drawRect(refreshRect,paint);
    }

    public void refreshStroke(){
        strokeOne +=10;
        strokeTwo+=10;
        WaveView.this.post(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        });
    }

    private void drawWave(Canvas canvas) {

        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        paint = new Paint();
        paint.setStrokeWidth(5);
        if (datas.size() != 0) {
            for (int listIndex = 0; listIndex < datas.size(); listIndex++) {
                //////////////v2////////////////////
//                目前待更新的数据处于count节点
                int indexTemp_0 = listIndex - 2;//第一个点
                int indexTemp_1 = listIndex - 1;//第二个点
                int indexTemp_2 = listIndex;//第三个点
//                    表示已经获得了三个以下的点
                    if (indexTemp_1 > 0) {

//                        canvas.drawLine(refreshstartx,refreshstarty,refreshendx,refreshendy,paint);
//                        表示已经获得了两个点
                        WaveFormBean temp_1 = datas.get(indexTemp_1);
                        WaveFormBean temp_2 = datas.get(indexTemp_2);
//                        连接第二第三个点
                        paint.setColor(Color.YELLOW);
                        canvas.drawLine(temp_1.getX(), temp_1.getY() , temp_2.getX(),temp_2.getY(), paint);

                        paint.setColor(Color.GRAY);
                        canvas.drawRect(refreshRect,paint);

                    }else
                    {
//                        只有一个点
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
//            datas.clear();
            final List<WaveFormBean> list = (List<WaveFormBean>) msg.obj;
            count = 0;
            int singleWidth = getWidth() / list.size();
            while (count < list.size()) {
                WaveFormBean waveFormBean = list.get(count);
                waveFormBean.setX(count * singleWidth);
                waveFormBean.setY(BASE_LINE - getWaveY(waveFormBean.getY())* TIMS);

                if (count-1>=0) {
                    WaveFormBean temp_1 = list.get(count-1);
                    WaveFormBean temp_2 = list.get(count);
                    refreshRect.set(temp_1.getX(),0,temp_1.getX()+REFRESH_WIDTH, getHeight());
                }

                if (datas.size() - 1 < count) {
//                    代表当前没有绘制心电图,需要添加心电数据
                    datas.add(waveFormBean);
                } else {
//                    代表之前已经绘制过了心电图,需要更新心电数据
                    datas.set(count, waveFormBean);
                }

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

        }


    }




}
