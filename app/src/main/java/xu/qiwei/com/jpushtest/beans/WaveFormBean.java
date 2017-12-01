package xu.qiwei.com.jpushtest.beans;

/**
 * Created by xuqiwei on 17-11-24.
 */

public class WaveFormBean {
    private int x;
    private int y;
    private byte wy;

    public WaveFormBean(int x, int y,byte wy) {
        this.x = x;
        this.y = y;
        this.wy = wy;
    }

    public byte getWy() {
        return wy;
    }

    public void setWy(byte wy) {
        this.wy = wy;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
