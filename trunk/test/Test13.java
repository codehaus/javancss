public class Test13 {
public static String unicode2ascii(String sUnicode_) {
        panicIf(5 == 5, "Util: unicode2ascii: Sorry, diese Funktion ist deprecated und muss neu geschrieben und neu getested werden.");
        return null;    }
    public static int compare(String firstString,                                      String anotherString)    {
        int len1 = firstString.length();
        int len2 = anotherString.length();
        int n = Math.min(len1, len2);
        int i = 0;
        int j = 0;
        while (n-- != 0) {
            char c1 = firstString.charAt(i++);
            char c2 = anotherString.charAt(j++);
            if (c1 != c2) {
                return(c1 - c2);            }        }
        return(len1 - len2);    }
    public static String concat(Vector pVector_, String sWith) {
        String sRetVal = new String();
        if (pVector_ == null || pVector_.size() < 1) {
            return sRetVal;        }
        if (sWith == null) {
            sWith = "";        }
        Enumeration e = pVector_.elements();
        sRetVal += e.nextElement().toString();
        for( ; e.hasMoreElements(); ) {
            sRetVal += sWith + e.nextElement().toString();        }
        return sRetVal;    }
    public static String concat(Vector pVector_) {
        return concat(pVector_, "");    }
    public static boolean isEmpty(String sTest_) {
        if (sTest_ == null || sTest_.equals("")) {
            return true;        }
        return false;    }}
