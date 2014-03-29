public class Test41 {
    private int _i = 0;

    /** CCN = 3 */
    public int getI() {
        if (_i <= 0) {
            return 0;
        }

        return _i;
    }

    /** CCN = 1 */
    public void setI(int i_) {
        this._i = i_;
    }

    /** CCN = 3 */
    public Test41(int i_) {
        if (i_ > 0) {
            return;
        }
        this.i = i_;

        return;
    }

    /** CCN = 3 */
    public void darwinize() {
        if (true) {
            throw new Exception();
        }
    }

    /** CCN = 1 */
    public void dummy() {
        return;
    }
}
