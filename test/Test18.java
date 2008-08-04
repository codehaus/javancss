public class Test18 {
    public static Vector map(Vector pVector_,                                     Transformer pTransformer_)    {
        Vector vRetVal = new Vector();
        for(Enumeration e = pVector_.elements(); e.hasMoreElements(); ) {
            Object pObject = e.nextElement();
            vRetVal.addElement(pTransformer_.transform(pObject));        }
        return vRetVal;    }
    public static boolean contains(Vector pVector_,                                             final String sFind_)    {
        panicIf(sFind_ == null);
        Predicate pFilter = new Predicate() {
            public boolean test(Object pObject_) {
                return(sFind_.equals((String)pObject_));            }        };
        return contains(pVector_, pFilter);    }
    public static boolean contains(Vector pVector_,                                             Predicate pFilter_)    {
        Vector vRetVal = new Vector();
        for(Enumeration e = pVector_.elements(); e.hasMoreElements(); ) {
            Object pObject = e.nextElement();
            if (pFilter_.test(pObject)) {
                return true;            }        }
        return false;    }
    public static Vector sort(final Vector pVector_) {
        ObjectComparator classcmp = new ObjectComparator();
        return sort(pVector_, classcmp);    }
    public static Vector sort(final Vector vInput_, Comparator pComparator_) {
        panicIf(vInput_ == null);
        Vector vRetVal = (Vector)vInput_.clone();
        if (vInput_.size() > 0) {
            quickSort(vRetVal, 0, vRetVal.size() - 1, pComparator_);        }
        return vRetVal;    }
    public static Vector concat(Vector vFirst_, Vector vSecond_) {
        Vector vRetVal = (Vector)vFirst_.clone();
        for(Enumeration e = vSecond_.elements(); e.hasMoreElements(); ) {
            vRetVal.addElement(e.nextElement());        }
        return vRetVal;    }
    public static Vector subtract(Vector vSource_, Vector vToDelete_) {
        Vector vRetVal = (Vector)vSource_.clone();
        for(Enumeration e = vToDelete_.elements(); e.hasMoreElements(); ) {
            vRetVal.removeElement(e.nextElement());        }
        return vRetVal;    }}
