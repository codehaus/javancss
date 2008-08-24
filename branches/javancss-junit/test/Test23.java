import java.util.Vector;
import FloatPoint;

public interface Net {

    protected Vector _getNeurons() {
    }

    protected void _setInputNeurons(Vector v) {
    }

    protected void _setUnivers(FloatPoint fp) {
    }

    public void setNeurons(Vector v) {
    }

    public void setStartPosition(int modus) {
    }

    public void nextStep () {
    }

    /**schaufelt alle neuen Gewichte zu den alten um*/
    public void move () {
    }
}

