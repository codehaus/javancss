public class Test15 {
    public static Vector stringToLines(int lines_,                                                  String pString_, char cCutter_)    {
        int maxLines = Integer.MAX_VALUE;
        if (lines_ > 0) {
            maxLines = lines_;        }
        Vector vRetVal = new Vector();
        if (pString_ == null) {
            return vRetVal;        }
        int startIndex = 0;
        for( ; maxLines > 0; maxLines-- ) {
            int endIndex = pString_.indexOf(cCutter_, startIndex);
            if (endIndex == -1) {
                if (startIndex < pString_.length()) {
                    endIndex = pString_.length();
                } else {
                    break;                }            }
            String sLine = pString_.substring(startIndex, endIndex);
            vRetVal.addElement((Object)sLine);
            startIndex = endIndex + 1;        }
        return vRetVal;    }
    public static Vector stringToLines(String pString_, char cCutter_) {
        return stringToLines(0, pString_, cCutter_);    }
    public static Vector stringToLines(String pString_) {
        return stringToLines(pString_, '\n');    }
    public static Vector stringToLines(int lines_, String pString_) {
        return stringToLines(lines_, pString_, '\n');    }
    public static boolean equalsCaseless(String sA_, String sB_) {
        String sFirst = sA_.toUpperCase();
        String sSecond = sB_.toUpperCase();
        return sFirst.equals(sSecond);    }
    public static String firstCharToUpperCase(String pString_) {
        String sRetVal = new String();
        if (pString_ == null || pString_.length() == 0) {
            return(sRetVal);        }
        sRetVal = pString_.substring(0, 1).toUpperCase() +                    pString_.substring(1, pString_.length());
        return(sRetVal);    }}
