package xu.qiwei.com.jpushtest.beans;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import xu.qiwei.com.jpushtest.Utils;

import static java.lang.Integer.parseInt;

/**
 * Created by xuqiwei on 17-11-24.
 */

public class MonitorBean {
    private List<WaveFormBean> waveFormBeanList;
//    返回数据的类型
    private int cateGory;
//    时间
    private String time;

    private String nibp;
    private String spo2;
    private String pr;
    private String resp;



    public MonitorBean(byte[] bytes) {
        cateGory = bytes[15];
        this.time = setTime(bytes);
        waveFormBeanList = convertToWaveData(bytes);

    }

    public List<WaveFormBean> getWaveFormBeanList() {
        return waveFormBeanList;
    }

    public void setWaveFormBeanList(List<WaveFormBean> waveFormBeanList) {
        this.waveFormBeanList = waveFormBeanList;
    }

    public int getCateGory() {
        return cateGory;
    }

    public void setCateGory(int cateGory) {
        this.cateGory = cateGory;
    }
//    private String getNibp(byte[] bytes){
//
//    }
    private List<WaveFormBean> convertToWaveData(byte[] bytes) {
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
                return singleECGParser(bytes);
            case 118:
                return respParser(bytes);
            default:


                break;

        }
        return null;
    }

    public String setTime(byte[] bytes) {
        String retTime = "";
        byte[] time = new byte[4];
        System.arraycopy(bytes,8,time,0,4);
        int timeInt = Utils.bytesToInt(time,0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            long cDate = simpleDateFormat.parse("2005-03-24 00:00:00").getTime();
            long glspacetime = (long)timeInt*1000;
            long dDate = glspacetime+cDate;
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
        int resp =Utils.bytesToInt(new byte[]{bytes[18],bytes[17],0,0},0) ;
//        int length =Math.abs(bytes[26]);
        int length =Utils.bytesToInt(new byte[]{bytes[26],bytes[25],0,0},0);
        Log.e("resp==length",resp+"=="+length);
        byte[] temp = new byte[length];
        System.arraycopy(bytes,27,temp,0,length);
        List<WaveFormBean> result = new ArrayList<>();
        for (int i = 0; i < temp.length; i++) {
            result.add(new WaveFormBean(0,0, temp[i]));
        }
        Log.e("","");

        byte[] time = new byte[4];
        System.arraycopy(bytes,8,time,0,4);
        int timeInt = Utils.bytesToInt(time,0);
        Log.e("timeInt=",timeInt+"");
//            399832272
//        1111593600000
//        1511425870000
//        2005-03-24 00:00:00
//        Date date = new Date()
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            long cDate = simpleDateFormat.parse("2005-03-24 00:00:00").getTime();
            long glspacetime = (long)timeInt*1000;
            long dDate = glspacetime+cDate;
            Date fiDate = new Date(dDate);
            Log.e("finaldate",simpleDateFormat.format(fiDate)+"");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<WaveFormBean> singleECGParser(byte[] bytes) {

        int length = parseInt(new StringBuffer().append(bytes[40] + "").append(bytes[41] + "").append("0").toString(), 16) - 1;
        Log.e("length", length + "");
        byte[] temp = new byte[length];
        System.arraycopy(bytes, 43, temp, 0, length);
        List<WaveFormBean> result = new ArrayList<>();
        for (int i = 0; i < temp.length; i++) {
            result.add(new WaveFormBean(0,0, temp[i]));
        }
        return result;
    }
//    获得bytes中[a,b)的值
    private byte[] rangeByte(int a,int b,byte[] bytes)
    {
        int length = b-a;
        byte[] temp = new byte[length];
        System.arraycopy(bytes,a,temp,0,length);
        return temp;

    }
}
