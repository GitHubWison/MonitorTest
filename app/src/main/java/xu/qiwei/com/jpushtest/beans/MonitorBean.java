package xu.qiwei.com.jpushtest.beans;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import xu.qiwei.com.jpushtest.Utils;

import static xu.qiwei.com.jpushtest.Utils.bytesToInt;

/**
 * Created by xuqiwei on 17-11-24.
 */

public class MonitorBean {
    //    波形
//    private List<WaveFormBeanShell> waveFormBeanShell ;
    private WaveFormBeanShell wave_15;
    private WaveFormBeanShell wave_54;
    private WaveFormBeanShell wave_55;
    private WaveFormBeanShell wave_80;
    private WaveFormBeanShell wave_01;
    private WaveFormBeanShell wave_02;
    //    private WaveFormBeanShell wave_15;
//    private List<WaveFormBean> waveFormBeanList;
    //    返回数据的类型
    private int cateGory;
    //    时间
    private String time;

    private String nibp;
    private String spo2;
    private String pr;
    private String resp;
    private String hr;
    //    private String waveProperty;
    private String pvc;


    public MonitorBean(byte[] bytes) {
        cateGory = Utils.bytesToInt(new byte[]{bytes[15], 0, 0, 0}, 0);
        this.time = setTime(bytes);
        convertToWaveData(bytes);

    }


    private String getNibp(byte[] bytes) {
        byte[] d = new byte[]{bytes[42], bytes[41], 0, 0};
        int dInt = bytesToInt(d, 0);
        if (dInt != 1) {
            int num2 = bytesToInt(new byte[]{bytes[17], bytes[16], 0, 0}, 0);
            int num3 = bytesToInt(new byte[]{bytes[19], bytes[18], 0, 0}, 0);
            int num4 = bytesToInt(new byte[]{bytes[21], bytes[20], 0, 0}, 0);
            if (num2 < 0) {
                return "--";
            }
            StringBuffer sb = new StringBuffer();
            sb.append(num2 + "").append("/").append(num4);
            return sb.toString();

        }
        return "--";
    }

    private void convertToWaveData(byte[] bytes) {
        switch (cateGory) {
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
                this.wave_15 = getWaveDatas(bytes, 41, 40, 42, 15);
                this.hr = getHR(bytes);
                this.pvc = getPvc(bytes, 32, 31);
                break;
            case 113:
                this.hr = getHR(bytes);
                this.pvc = getPvc(bytes, 44, 43);
                int length_113 = Utils.bytesToInt(new byte[]{bytes[53], bytes[52], 0, 0}, 0);
                this.wave_54 = getWaveDatas(bytes, 53, 52, 57, 54);
                this.wave_55 = getWaveDatas(bytes, 53, 52, 57 + length_113, 55);
                this.wave_80 = getWaveDatas(bytes, 53, 52, 57 + length_113 + length_113, 80);
            case 118:

                this.wave_01=getWaveDatas(bytes, 26, 25, 27, -1);
                this.resp = getResp(bytes);
                break;
            case 119:
                this.pr = getPR(bytes);
                this.spo2 = getSpo2(bytes);
                this.wave_02 = getWaveDatas(bytes, 31, 30, 31, -2);
                break;
            case 120:
                this.nibp = getNibp(bytes);
                break;
            default:
                break;

        }
    }

    private String getPvc(byte[] bytes, int start, int end) {

        int pvcInt = Utils.bytesToInt(new byte[]{bytes[start], bytes[end], 0, 0}, 0);
        String res = "";
        if (pvcInt == -100 || pvcInt == -25345) {
            res = "---";
        } else {
            res = pvcInt + "";
        }
        return res;
    }

    private WaveFormBeanShell getWaveDatas(byte[] bytes, int lengthStart, int lengthEnd, int waveDataStart, int wavePropertyPosition) {
        WaveFormBeanShell waveFormBeanShell = new WaveFormBeanShell();

        int length = Utils.bytesToInt(new byte[]{bytes[lengthStart], bytes[lengthEnd], 0, 0}, 0);
        String waveProperty = cacularWaveProperty(bytes, wavePropertyPosition);
        byte[] temp = rangeByte(waveDataStart, length, bytes);

        List<WaveFormBean> result = new ArrayList<>();
        for (int i = 0; i < temp.length; i++) {
            result.add(new WaveFormBean(0, 0, temp[i]));
        }

        waveFormBeanShell.setLength(length);
        waveFormBeanShell.setWaveProperty(waveProperty);
        waveFormBeanShell.setWaveFormBeanList(result);
        return waveFormBeanShell;

    }

    private String cacularWaveProperty(byte[] bytes, int position) {
        if (position == -1) {
            return "01";
        }else if (position == -2)
        {
            return "02";
        }
        int wavePropertyCode = Utils.bytesToInt(new byte[]{bytes[position], 0, 0, 0}, 0);
        return translateProtocolCodeToName(wavePropertyCode);


    }

    private String getResp(byte[] bytes) {

        int num = Utils.bytesToInt(new byte[]{bytes[18], bytes[17], 0, 0}, 0);
        if (num <= 0 || num >= 156) {
            return "---";
        } else {
            return num + "";
        }
    }

    private String getSpo2(byte[] bytes) {

        int num = Utils.bytesToInt(new byte[]{bytes[17], bytes[16], 0, 0}, 0);
        if (num <= 0) {
            return "---";
        } else {
            return num + "";
        }
    }

    private String getPR(byte[] bytes) {
        int num2 = Utils.bytesToInt(new byte[]{bytes[23], bytes[22], 0, 0}, 0);
        if (num2 <= 0) {
            return "---";
        } else {
            return num2 + "";
        }
    }

    private String getHR(byte[] bytes) {
        int tempHr = Utils.bytesToInt(new byte[]{bytes[20], bytes[19], 0, 0}, 0);
        if (tempHr <= 0 || tempHr == 156) {
            return "---";
        } else {
            return tempHr + "";
        }
    }

    public String setTime(byte[] bytes) {
        String retTime = "";
        byte[] time = new byte[4];
        System.arraycopy(bytes, 8, time, 0, 4);
        int timeInt = bytesToInt(time, 0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            long cDate = simpleDateFormat.parse("2005-03-24 00:00:00").getTime();
            long glspacetime = (long) timeInt * 1000;
            long dDate = glspacetime + cDate;
            Date fiDate = new Date(dDate);
            retTime = simpleDateFormat.format(fiDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return retTime;
    }

    private List<WaveFormBean> respParser(byte[] bytes) {
//第1８,17位是resp的值
//第26,25位是波形的长度
//第27位开始到波形长度结束是波形
        int resp = bytesToInt(new byte[]{bytes[18], bytes[17], 0, 0}, 0);
//        int length =Math.abs(bytes[26]);
        int length = bytesToInt(new byte[]{bytes[26], bytes[25], 0, 0}, 0);
        Log.e("resp==length", resp + "==" + length);
        byte[] temp = new byte[length];
        System.arraycopy(bytes, 27, temp, 0, length);
        List<WaveFormBean> result = new ArrayList<>();
        for (int i = 0; i < temp.length; i++) {
            result.add(new WaveFormBean(0, 0, temp[i]));
        }
        Log.e("", "");

        byte[] time = new byte[4];
        System.arraycopy(bytes, 8, time, 0, 4);
        int timeInt = bytesToInt(time, 0);
        Log.e("timeInt=", timeInt + "");
//            399832272
//        1111593600000
//        1511425870000
//        2005-03-24 00:00:00
//        Date date = new Date()
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            long cDate = simpleDateFormat.parse("2005-03-24 00:00:00").getTime();
            long glspacetime = (long) timeInt * 1000;
            long dDate = glspacetime + cDate;
            Date fiDate = new Date(dDate);
            Log.e("finaldate", simpleDateFormat.format(fiDate) + "");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<WaveFormBean> singleECGParser(byte[] bytes) {

        int length = Utils.bytesToInt(new byte[]{bytes[41], bytes[40], 0, 0}, 0);
//                parseInt(new StringBuffer().append(bytes[40] + "").append(bytes[41] + "").append("0").toString(), 16) - 1;
        byte[] temp = new byte[length];
        System.arraycopy(bytes, 42, temp, 0, length);
        List<WaveFormBean> result = new ArrayList<>();
        for (int i = 0; i < temp.length; i++) {
            result.add(new WaveFormBean(0, 0, temp[i]));
        }
        return result;
    }

    //    获得bytes中[a,b)的值
    private byte[] rangeByte(int a, int length, byte[] bytes) {
        byte[] temp = new byte[length];
        System.arraycopy(bytes, a, temp, 0, length);
        return temp;

    }

    private String translateProtocolCodeToName(int code) {
        switch (code) {
            case 0:
                return "NONE";

            case 1:
            case 101:
                return "I";

            case 2:
            case 102:
                return "II";

            case 3:
            case 103:
                return "III";

            case 4:
            case 104:
                return "aVR";

            case 5:
            case 105:
                return "aVL";

            case 6:
            case 106:
                return "aVF";

            case 7:
                return "V";

            case 8:
                return "CAL";

            case 107:
                return "V1";

            case 108:
                return "V2";

            case 109:
                return "V3";

            case 110:
                return "V4";

            case 111:
                return "V5";

            case 112:
                return "V6";
        }
        return "";
    }

    public WaveFormBeanShell getWave_15() {
        return wave_15;
    }

    public void setWave_15(WaveFormBeanShell wave_15) {
        this.wave_15 = wave_15;
    }

    public WaveFormBeanShell getWave_54() {
        return wave_54;
    }

    public void setWave_54(WaveFormBeanShell wave_54) {
        this.wave_54 = wave_54;
    }

    public WaveFormBeanShell getWave_55() {
        return wave_55;
    }

    public void setWave_55(WaveFormBeanShell wave_55) {
        this.wave_55 = wave_55;
    }

    public WaveFormBeanShell getWave_80() {
        return wave_80;
    }

    public void setWave_80(WaveFormBeanShell wave_80) {
        this.wave_80 = wave_80;
    }

    public WaveFormBeanShell getWave_01() {
        return wave_01;
    }

    public void setWave_01(WaveFormBeanShell wave_01) {
        this.wave_01 = wave_01;
    }

    public WaveFormBeanShell getWave_02() {
        return wave_02;
    }

    public void setWave_02(WaveFormBeanShell wave_02) {
        this.wave_02 = wave_02;
    }

    public int getCateGory() {
        return cateGory;
    }

    public void setCateGory(int cateGory) {
        this.cateGory = cateGory;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNibp() {
        return nibp;
    }

    public void setNibp(String nibp) {
        this.nibp = nibp;
    }

    public String getSpo2() {
        return spo2;
    }

    public void setSpo2(String spo2) {
        this.spo2 = spo2;
    }

    public String getPr() {
        return pr;
    }

    public void setPr(String pr) {
        this.pr = pr;
    }

    public String getResp() {
        return resp;
    }

    public void setResp(String resp) {
        this.resp = resp;
    }

    public String getHr() {
        return hr;
    }

    public void setHr(String hr) {
        this.hr = hr;
    }


    public String getPvc() {
        return pvc;
    }

    public void setPvc(String pvc) {
        this.pvc = pvc;
    }
}
