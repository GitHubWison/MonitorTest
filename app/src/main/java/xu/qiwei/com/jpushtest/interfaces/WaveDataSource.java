package xu.qiwei.com.jpushtest.interfaces;

import xu.qiwei.com.jpushtest.beans.WaveFormBeanShell;

/**
 * Created by xuqiwei on 17-12-12.
 */

public interface WaveDataSource {
//    波形数据统一在此处获取
     public byte[] getWaveBytes();
//    获得bytes后将数据转化为波形，时间等值保存到数据库中
    public void saveOrUpdateWaveInfo(byte[] waveBytes);
////    获取最近一笔的wave
//    public MonitorBean getWaveInfo();
//    获取
    public void saveTime(String time);
    public void saveHR(String hr);
    public void savePvc(String pvc);
    public void saveSpo2(String spo2);
    public void savePR(String pr);
    public void saveNibp(String nipb);
    public void saveResp(String resp);
    public void saveWave_15(WaveFormBeanShell waveFormBeanShell);
    public void saveWave_54(WaveFormBeanShell waveFormBeanShell);
    public void saveWave_55(WaveFormBeanShell waveFormBeanShell);
    public void saveWave_80(WaveFormBeanShell waveFormBeanShell);
    public void saveWave_01(WaveFormBeanShell waveFormBeanShell);
    public void saveWave_02(WaveFormBeanShell waveFormBeanShell);

    public String getStoredHR();
    public String getStoredPvc();
    public String getStoredSpo2();
    public String getStoredPR();
    public String getStoredNibp();
    public String getStoredResp();
    public String getTime();

    public WaveFormBeanShell getStoredWave_54();
    public WaveFormBeanShell getStoredWave_55();
    public WaveFormBeanShell getStoredWave_80();
    public WaveFormBeanShell getStoredWave_01();
    public WaveFormBeanShell getStoredWave_02();
    public WaveFormBeanShell getStoredWave_15();



}
