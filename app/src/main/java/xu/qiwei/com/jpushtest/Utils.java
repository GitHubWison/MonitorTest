package xu.qiwei.com.jpushtest;

/**
 * Created by xuqiwei on 17-11-21.
 */

public class Utils {
    public static final String ServerIP = "192.168.19.5";
    public static final String ID ="101";
    public static int bytesToInt(byte[] paramArrayOfByte, int paramInt)
    {
        return 0xFF & paramArrayOfByte[paramInt] | (0xFF & paramArrayOfByte[(paramInt + 1)]) << 8 | (0xFF & paramArrayOfByte[(paramInt + 2)]) << 16 | (0xFF & paramArrayOfByte[(paramInt + 3)]) << 24;
    }

}
