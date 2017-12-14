package xu.qiwei.com.jpushtest.interfaces;

import xu.qiwei.com.jpushtest.beans.MonitorBean;

/**
 * Created by xuqiwei on 17-12-12.
 */

public interface WaveDataSource {
//    波形数据统一在此处获取
     public byte[] getWaveBytes();
//    获得bytes后将数据转化为波形，时间等值保存到数据库中
    public void saveOrUpdateWaveInfo(byte[] waveBytes);
//    获取最近一笔的wave
    public MonitorBean getWaveInfo();
//    获取

}
