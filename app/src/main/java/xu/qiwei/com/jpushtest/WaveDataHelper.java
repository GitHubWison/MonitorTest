package xu.qiwei.com.jpushtest;

import xu.qiwei.com.jpushtest.beans.MonitorBean;
import xu.qiwei.com.jpushtest.interfaces.WaveDataSource;

/**
 * Created by xuqiwei on 17-12-12.
 */

public class WaveDataHelper implements WaveDataSource {
    private static WaveDataHelper waveDataHelper;

    public WaveDataHelper() {
    }

    public static WaveDataHelper getInstance() {
        if (waveDataHelper==null) {
            synchronized (WaveDataHelper.class){
                if (waveDataHelper==null) {
                    waveDataHelper = new WaveDataHelper();

                }
            }
        }
        return waveDataHelper;
    }
    @Override
    public byte[] getWaveBytes() {

        return new byte[0];
    }

    @Override
    public void saveWaveInfo() {
//        1.拉取监护仪返回的数据
//        2.解析监护仪的数据
//        3.将数据存入数据库中


    }

    @Override
    public MonitorBean getWaveInfo() {
        return null;
    }
}
