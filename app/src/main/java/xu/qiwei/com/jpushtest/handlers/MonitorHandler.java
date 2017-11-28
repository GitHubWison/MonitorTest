package xu.qiwei.com.jpushtest.handlers;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/**
 * Created by xuqiwei on 17-11-20.
 */

public class MonitorHandler extends Handler{
    public MonitorHandler(Looper arg2)
    {
        super(arg2);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        Log.e("threadName==",Thread.currentThread().getName());
        Log.e("msgwhat==",msg.what+"");
        Log.e("start","");
    }
}
