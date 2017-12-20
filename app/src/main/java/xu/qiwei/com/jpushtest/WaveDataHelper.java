package xu.qiwei.com.jpushtest;

import xu.qiwei.com.jpushtest.beans.MonitorBean;
import xu.qiwei.com.jpushtest.beans.WaveFormBeanShell;
import xu.qiwei.com.jpushtest.interfaces.WaveDataSource;
import xu.qiwei.com.jpushtest.utils.SharedPrefsUtils;

/**
 * Created by xuqiwei on 17-12-12.
 */

public class WaveDataHelper implements WaveDataSource {
    private static final String ORIGINAL_WAVE_KEY = "ORIGINAL_WAVE_KEY";

    private static final String TIME_KEY = "TIME_KEY";
    private static final String HR_KEY = "HR_KEY";
    private static final String PVC_KEY = "PVC_KEY";

    private static final String SPO2_KEY = "SPO2_KEY";
    private static final String PR_KEY = "PR_KEY";
    private static final String NIPB_KEY = "NIPB_KEY";

    private static final String RESP_KEY = "RESP_KEY";
    private static final String WAVE_15_KEY = "WAVE_15_KEY";
    private static final String WAVE_54_KEY = "WAVE_54_KEY";

    private static final String WAVE_55_KEY = "WAVE_55_KEY";
    private static final String WAVE_80_KEY = "WAVE_80_KEY";
    private static final String WAVE_01_KEY = "WAVE_01_KEY";

    private static final String WAVE_02_KEY = "WAVE_02_KEY";

    private static WaveDataHelper waveDataHelper;

    public WaveDataHelper() {
    }

    public static WaveDataHelper getInstance() {
        if (waveDataHelper == null) {
            synchronized (WaveDataHelper.class) {
                if (waveDataHelper == null) {
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
        MonitorBean temp = new MonitorBean(waveBytes);
        saveTime(temp.getTime());
//        根据数据的类型(cateGory)更新数据到数据库中
        switch (temp.getCateGory()) {
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
                saveHR(temp.getHr());
                savePvc(temp.getPvc());
                saveWave_15(temp.getWave_15());
                break;
            case 113:
                //                   更新hr/pvc/3个波形图

                saveHR(temp.getHr());
                savePvc(temp.getPvc());
                saveWave_54(temp.getWave_54());
                saveWave_55(temp.getWave_55());
                saveWave_80(temp.getWave_80());
                break;
            case 118:
//                   更新resp/心电图
                saveResp(temp.getResp());
                saveWave_01(temp.getWave_01());

                break;
            case 119:
//                   更新spo2/pr/波形图
                savePR(temp.getPr());
                saveSpo2(temp.getSpo2());
                saveWave_02(temp.getWave_02());
                break;
            case 120:
                saveNibp(temp.getNibp());
                break;
            default:
                break;

        }
    }

    @Override
    public void saveTime(String time) {
//        TIME_KEY
        SharedPrefsUtils.setStringPreference(CommonApplication.context, TIME_KEY, time);
    }


    @Override
    public void saveHR(String hr) {
        SharedPrefsUtils.setStringPreference(CommonApplication.context, HR_KEY, hr);
    }

    @Override
    public void savePvc(String pvc) {
        SharedPrefsUtils.setStringPreference(CommonApplication.context, PVC_KEY, pvc);
    }

    @Override
    public void saveSpo2(String spo2) {
        SharedPrefsUtils.setStringPreference(CommonApplication.context, SPO2_KEY, spo2);
    }

    @Override
    public void savePR(String pr) {
        SharedPrefsUtils.setStringPreference(CommonApplication.context, PR_KEY, pr);
    }

    @Override
    public void saveNibp(String nipb) {
        SharedPrefsUtils.setStringPreference(CommonApplication.context, NIPB_KEY, nipb);
    }

    @Override
    public void saveResp(String resp) {
        SharedPrefsUtils.setStringPreference(CommonApplication.context, RESP_KEY, resp);
    }

    @Override
    public void saveWave_15(WaveFormBeanShell waveFormBeanShell) {
        String id = spellWaveKey(waveFormBeanShell.getWaveID());
        SharedPrefsUtils.setObjectPreference(CommonApplication.context, id, waveFormBeanShell);
    }

    @Override
    public void saveWave_54(WaveFormBeanShell waveFormBeanShell) {
        SharedPrefsUtils.setObjectPreference(CommonApplication.context, WAVE_54_KEY, waveFormBeanShell);

    }

    @Override
    public void saveWave_55(WaveFormBeanShell waveFormBeanShell) {
        SharedPrefsUtils.setObjectPreference(CommonApplication.context, WAVE_55_KEY, waveFormBeanShell);

    }

    @Override
    public void saveWave_80(WaveFormBeanShell waveFormBeanShell) {
        SharedPrefsUtils.setObjectPreference(CommonApplication.context, WAVE_80_KEY, waveFormBeanShell);

    }

    @Override
    public void saveWave_01(WaveFormBeanShell waveFormBeanShell) {
        SharedPrefsUtils.setObjectPreference(CommonApplication.context, WAVE_01_KEY, waveFormBeanShell);

    }

    @Override
    public void saveWave_02(WaveFormBeanShell waveFormBeanShell) {
        SharedPrefsUtils.setObjectPreference(CommonApplication.context, WAVE_02_KEY, waveFormBeanShell);

    }

//    @Override
//    public void saveWave_102(WaveFormBeanShell waveFormBeanShell) {
////        WAVE_102_KEY
//        SharedPrefsUtils.setObjectPreference(CommonApplication.context, WAVE_102_KEY, waveFormBeanShell);
//    }

    @Override
    public String getStoredHR() {
        String temp = SharedPrefsUtils.getStringPreference(CommonApplication.context, HR_KEY);
        if (temp == null || "null".equals(temp) || "".equals(temp)) {
            temp = "--";
        }
        return temp;
    }

    @Override
    public String getStoredPvc() {
        String temp = SharedPrefsUtils.getStringPreference(CommonApplication.context, PVC_KEY);
        if (temp == null || "null".equals(temp) || "".equals(temp)) {
            temp = "--";
        }
        return temp;
    }

    @Override
    public String getStoredSpo2() {
        String temp = SharedPrefsUtils.getStringPreference(CommonApplication.context, SPO2_KEY);
        if (temp == null || "null".equals(temp) || "".equals(temp)) {
            temp = "--";
        }
        return temp;
    }

    @Override
    public String getStoredPR() {
        String temp = SharedPrefsUtils.getStringPreference(CommonApplication.context, PR_KEY);
        if (temp == null || "null".equals(temp) || "".equals(temp)) {
            temp = "--";
        }
        return temp;
    }

    @Override
    public String getStoredNibp() {
        String temp = SharedPrefsUtils.getStringPreference(CommonApplication.context, NIPB_KEY);
        if (temp == null || "null".equals(temp) || "".equals(temp)) {
            temp = "--/--";
        }
        return temp;
    }

    @Override
    public String getStoredResp() {
        String temp = SharedPrefsUtils.getStringPreference(CommonApplication.context, RESP_KEY);
        if (temp == null || "null".equals(temp) || "".equals(temp)) {
            temp = "--";
        }
        return temp;
    }

    @Override
    public String getTime() {

        String temp = SharedPrefsUtils.getStringPreference(CommonApplication.context, TIME_KEY);
        if (temp == null || "null".equals(temp) || "".equals(temp)) {
            temp = "--";
        }
/*        else
        {
//            将时间格式化
            if (temp.contains(" ")) {
                temp = temp.replace(" ","\n");
            }
        }*/
        return temp;
    }

    @Override
    public WaveFormBeanShell getStoredWave_54() {
        WaveFormBeanShell waveFormBeanShell = (WaveFormBeanShell) SharedPrefsUtils.getObjectPreference(CommonApplication.context,
                WAVE_54_KEY, WaveFormBeanShell.class);
        if (waveFormBeanShell == null) {
            waveFormBeanShell = new WaveFormBeanShell();
        }
        return waveFormBeanShell;
    }

    @Override
    public WaveFormBeanShell getStoredWave_55() {
        WaveFormBeanShell waveFormBeanShell = (WaveFormBeanShell) SharedPrefsUtils.getObjectPreference(CommonApplication.context,
                WAVE_55_KEY, WaveFormBeanShell.class);
        if (waveFormBeanShell == null) {
            waveFormBeanShell = new WaveFormBeanShell();
        }
        return waveFormBeanShell;
    }

    @Override
    public WaveFormBeanShell getStoredWave_80() {
        WaveFormBeanShell waveFormBeanShell = (WaveFormBeanShell) SharedPrefsUtils.getObjectPreference(CommonApplication.context,
                WAVE_80_KEY, WaveFormBeanShell.class);
        if (waveFormBeanShell == null) {
            waveFormBeanShell = new WaveFormBeanShell();
        }
        return waveFormBeanShell;
    }

    @Override
    public WaveFormBeanShell getStoredWave_01() {
        WaveFormBeanShell waveFormBeanShell = (WaveFormBeanShell) SharedPrefsUtils.getObjectPreference(CommonApplication.context,
                WAVE_01_KEY, WaveFormBeanShell.class);
        if (waveFormBeanShell == null) {
            waveFormBeanShell = new WaveFormBeanShell();
        }
        return waveFormBeanShell;
    }

    @Override
    public WaveFormBeanShell getStoredWave_02() {
        WaveFormBeanShell waveFormBeanShell = (WaveFormBeanShell) SharedPrefsUtils.getObjectPreference(CommonApplication.context,
                WAVE_02_KEY, WaveFormBeanShell.class);
        if (waveFormBeanShell == null) {
            waveFormBeanShell = new WaveFormBeanShell();
        }
        return waveFormBeanShell;
    }

    @Override
    public WaveFormBeanShell getStoredWave_15(String waveID) {
        String id = spellWaveKey(waveID);
        WaveFormBeanShell waveFormBeanShell = (WaveFormBeanShell) SharedPrefsUtils.getObjectPreference(CommonApplication.context,
                id  , WaveFormBeanShell.class);
        if (waveFormBeanShell == null) {
            waveFormBeanShell = new WaveFormBeanShell();
        }
        return waveFormBeanShell;
    }

//    @Override
//    public WaveFormBeanShell getStoredWave_102() {
//        WaveFormBeanShell waveFormBeanShell = (WaveFormBeanShell) SharedPrefsUtils.getObjectPreference(CommonApplication.context,
//                WAVE_102_KEY, WaveFormBeanShell.class);
//        if (waveFormBeanShell == null) {
//            waveFormBeanShell = new WaveFormBeanShell();
//        }
//        return waveFormBeanShell;
//    }
    private String spellWaveKey(String waveID){
        if (waveID==null||"null".equals(waveID)) {
            waveID = "";
        }
        StringBuffer stringBuffer  = new StringBuffer();
        stringBuffer.append(WAVE_15_KEY).append("_").append(waveID);
        return stringBuffer.toString();
    }

}
