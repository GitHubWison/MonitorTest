package xu.qiwei.com.jpushtest.interfaces;

import java.util.List;

import xu.qiwei.com.jpushtest.beans.WaveFormBean;

/**
 * Created by xuqiwei on 17-11-24.
 */

public interface WaveFormInterface {
//    通知视图刷新
    public void refreshData(List<WaveFormBean> waveFormBeanList);
}
