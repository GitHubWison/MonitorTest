package xu.qiwei.com.jpushtest.beans;

import java.util.List;

/**
 * Created by xuqiwei on 17-12-14.
 */

public class WaveFormBeanShell {
    private int length ;
    private String waveProperty;
    private List<WaveFormBean> waveFormBeanList;


    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getWaveProperty() {
        return waveProperty;
    }

    public void setWaveProperty(String waveProperty) {
        this.waveProperty = waveProperty;
    }

    public List<WaveFormBean> getWaveFormBeanList() {
        return waveFormBeanList;
    }

    public void setWaveFormBeanList(List<WaveFormBean> waveFormBeanList) {
        this.waveFormBeanList = waveFormBeanList;
    }
}
