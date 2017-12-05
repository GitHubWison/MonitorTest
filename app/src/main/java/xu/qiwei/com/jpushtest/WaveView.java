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
//    private int listCount = 0;
    private int BASE_LINE = 1000;
    private static final int FLAT_WAVE = 125;
    private int TIMS = 8;
    private int waveUnit = 0;
    private static final int maxPointCount = 200;
    //    待更新的位置
    private int count = 0;
    private List<WaveFormBean> datas = new ArrayList<>();
    private Rect refreshRect = new Rect(0, 0, 0, 0);
    private static final int REFRESH_WIDTH = 10;
    private boolean isNeedRefresh = false;
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
        initDatas();
        drawWave(canvas);
//        drawTest(canvas);
    }

    private void initDatas() {
//        波形y值在250到(250-FLAT_WAVE)之间
        TIMS = (getHeight())/(2*(Math.abs(250-FLAT_WAVE))) +1;
        BASE_LINE = getHeight()/2+FLAT_WAVE*TIMS;


    }

    private void drawTest(Canvas canvas) {
        paint = new Paint();
        paint.setColor(Color.BLUE);

        canvas.drawRect(refreshRect, paint);
    }


    private void drawWave(Canvas canvas) {

        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        paint = new Paint();
        paint.setStrokeWidth(5);
        if (datas.size() != 0) {
            for (int listIndex = 0; listIndex < datas.size(); listIndex++) {

//                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//                目前待更新的数据处于count节点
//                int indexTemp_0 = listIndex - 2;//第一个点
                int indexTemp_1 = listIndex - 1;//第二个点
                int indexTemp_2 = listIndex;//第三个点
//                    表示已经获得了三个以下的点
                if (indexTemp_1 > 0) {
//                    if (isNeedRefresh) {
                        //                        表示已经获得了两个点
                        WaveFormBean temp_1 = datas.get(indexTemp_1);
                        WaveFormBean temp_2 = datas.get(indexTemp_2);
//                        连接第二第三个点
                        paint.setColor(Color.YELLOW);
                        canvas.drawLine(temp_1.getX(), temp_1.getY(), temp_2.getX(), temp_2.getY(), paint);
//                    }


                    paint.setColor(Color.BLUE);
                    canvas.drawRect(refreshRect, paint);

                } else {
//                        只有一个点
                }

            }
        }
    }

    private int getWaveY(byte originaly) {

        return Utils.bytesToInt(new byte[]{ originaly, 0, 0, 0}, 0);
    }


    @Override
    public void refreshData(List<WaveFormBean> waveFormBeanList) {
//        listCount = waveFormBeanList.size();
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
                waveFormBean.setY(caculatedY(getWaveY(waveFormBean.getWy())));
                if (count - 1 >= 0) {
                    WaveFormBean temp_1 = list.get(count - 1);
                    refreshRect.set(temp_1.getX(), 0, temp_1.getX() + REFRESH_WIDTH, getHeight());
                }

                if (datas.size() - 1 < count) {
//                    代表当前没有绘制心电图,需要添加心电数据
                    datas.add(waveFormBean);
                    isNeedRefresh = true;
                } else {
                    isNeedRefresh = datas.get(count).getY() != waveFormBean.getY();
                    datas.set(count, waveFormBean);

                }

                WaveView.this.post(new Runnable() {
                    @Override
                    public void run() {
                            invalidate();
                    }
                });

                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count++;
            }

        }


    }
    private int caculatedY(int wavey){
        int result = BASE_LINE - wavey* TIMS;
        return result;
    }


}
