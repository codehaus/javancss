public class Test17 {
    public static void quickSort(Object s[], int lo, int hi, Comparator cmp) {
        if (lo >= hi)
            return;
        int mid = (lo + hi) / 2;
        if (cmp.compare(s[lo], s[mid]) > 0) {
            Object tmp = s[lo];
            s[lo] = s[mid];
            s[mid] = tmp;         }
        if (cmp.compare(s[mid], s[hi]) > 0) {
            Object tmp = s[mid];
            s[mid] = s[hi];
            s[hi] = tmp; // swap
            if (cmp.compare(s[lo], s[mid]) > 0) {
                Object tmp2 = s[lo];
                s[lo] = s[mid];
                s[mid] = tmp2;             }        }
        int left = lo+1;           // start one past lo since already handled lo
        int right = hi-1;          // similarly
        if (left >= right)
            return; // if three or fewer we are done
        Object partition = s[mid];
        for (;;) {
            while (cmp.compare(s[right], partition) > 0)
                --right;
            while (left < right && cmp.compare(s[left], partition) <= 0)
                ++left;
            if (left < right) {
                Object tmp = s[left];
                s[left] = s[right];
                s[right] = tmp; // swap
                --right;            }
            else
                break;        }
        quickSort(s, lo, left, cmp);
        quickSort(s, left+1, hi, cmp);    }
    public static void quickSort(Vector s, int lo, int hi, Comparator cmp) {
        panicIf (s == null);
        if (lo >= hi)
            return;
        int mid = (lo + hi) / 2;
        if (cmp.compare(s.elementAt(lo), s.elementAt(mid)) > 0) {
            Object tmp = s.elementAt(lo);
            s.setElementAt(s.elementAt(mid), lo);
            s.setElementAt(tmp, mid);        }
        if (cmp.compare(s.elementAt(mid), s.elementAt(hi)) > 0) {
            Object tmp = s.elementAt(mid);
            s.setElementAt(s.elementAt(hi), mid);
            s.setElementAt(tmp, hi);
            if (cmp.compare(s.elementAt(lo), s.elementAt(mid)) > 0) {
                Object tmp2 = s.elementAt(lo);
                s.setElementAt(s.elementAt(mid), lo);
                s.setElementAt(tmp2, mid);            }        }
        int left = lo+1;           // start one past lo since already handled lo
        int right = hi-1;          // similarly
        if (left >= right)
            return; // if three or fewer we are done
        Object partition = s.elementAt(mid);
        for (;;) {
            while (cmp.compare(s.elementAt(right), partition) > 0)
                --right;
            while (left < right && cmp.compare(s.elementAt(left), partition) <= 0)
                ++left;
            if (left < right) {
                Object tmp = s.elementAt(left);
                s.setElementAt(s.elementAt(right), left);
                s.setElementAt(tmp, right);
                --right;            }
            else
                break;        }
        quickSort(s, lo, left, cmp);
        quickSort(s, left+1, hi, cmp);    }}
