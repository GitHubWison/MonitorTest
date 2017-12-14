package xu.qiwei.com.jpushtest;

import android.app.Application;
import android.content.Context;

/**
 * Created by xuqiwei on 17-12-14.
 */

public class CommonApplication extends Application{
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
}
