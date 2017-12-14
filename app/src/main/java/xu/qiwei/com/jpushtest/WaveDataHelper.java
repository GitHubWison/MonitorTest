package xu.qiwei.com.jpushtest;

import xu.qiwei.com.jpushtest.beans.MonitorBean;
import xu.qiwei.com.jpushtest.interfaces.WaveDataSource;
import xu.qiwei.com.jpushtest.utils.SharedPrefsUtils;

/**
 * Created by xuqiwei on 17-12-12.
 */

public class WaveDataHelper implements WaveDataSource {
    private static final String ORIGINAL_WAVE_KEY="ORIGINAL_WAVE_KEY";
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
    public void saveOrUpdateWaveInfo(byte[] waveBytes) {
//        1.取出原始数据
//        2.判断原始数据，如果为空则新加，否则修改后增加
        MonitorBean monitorBean = getWaveInfo();
        if (monitorBean==null) {
            monitorBean = new MonitorBean(waveBytes);
        }else
        {
            MonitorBean temp = new MonitorBean(waveBytes);
//        根据数据的类型(cateGory)更新数据到数据库中
            switch (monitorBean.getCateGory()){
                case 101:
                case 102:
                case 103:
                case 104:
                case 105:
                case 106:
                case 107:
                case 108:
                case 109:
                case 110:
                case 111:
                case 112:
//                   更新hr/pvc/波形图
                    monitorBean.setHr(temp.getHr());
                    monitorBean.setPvc(temp.getPvc());
                    monitorBean.setWave_15(temp.getWave_15());
                    break;
                case 113:
                    //                   更新hr/pvc/3个波形图
                    monitorBean.setHr(temp.getHr());
                    monitorBean.setPvc(temp.getPvc());
                    monitorBean.setWave_54(temp.getWave_54());
                    monitorBean.setWave_55(temp.getWave_55());
                    monitorBean.setWave_80(temp.getWave_80());
                case 118:
//                   更新resp/心电图
                    monitorBean.setResp(temp.getResp());
                    monitorBean.setWave_01(temp.getWave_01());

                    break;
                case 119:
//                   更新spo2/pr/波形图
                    monitorBean.setPr(temp.getPr());
                    monitorBean.setSpo2(temp.getSpo2());
                    monitorBean.setWave_02(temp.getWave_02());
                    break;
                case 120:
                    monitorBean.setNibp(temp.getNibp());
                    break;
                default:
                    break;

            }
        }

//       刷新数据信息
        SharedPrefsUtils.setObjectPreference(CommonApplication.context,ORIGINAL_WAVE_KEY,monitorBean);
    }

    @Override
    public MonitorBean getWaveInfo() {
        MonitorBean monitorBean = (MonitorBean)SharedPrefsUtils.getObjectPreference(CommonApplication.context,ORIGINAL_WAVE_KEY,MonitorBean.class);
        return monitorBean;
    }
}
