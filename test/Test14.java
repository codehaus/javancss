public class Test14 {
    public static String firstCharToLowerCase(String pString_) {
        String sRetVal = new String();
        if (pString_ == null || pString_.length() == 0) {
            return(sRetVal);        }
        sRetVal = pString_.substring(0, 1).toLowerCase() +                    pString_.substring(1, pString_.length());
        return(sRetVal);    }
    public static String replace(String pString_,                                          String sOld_, String sNew_)    {
        panicIf(sNew_ == null || sOld_ == null);
        if (pString_ == null) {
            return null;        }
        String sRetVal = new String(pString_);
        int indexNew = sNew_.length();
        int index = sRetVal.indexOf(sOld_);
        while(index > -1) {
            sRetVal = sRetVal.substring(0, index) +                     sNew_ + sRetVal.substring(index + sOld_.length(),                                                        sRetVal.length());
            index += indexNew;
            if (index >= sRetVal.length()) {
                break;            }
            index = sRetVal.indexOf(sOld_, index + indexNew);        }
        return sRetVal;    }
    public static boolean isSpaceLine(String sLine_) {
        if (sLine_ == null || sLine_.length() == 0) {
            return true;        }
        for(int index = 0; index < sLine_.length(); index++) {
            char c = sLine_.charAt(index);
            if (c != ' ' && c != '\t' && c != '\n') {
                return false;            }        }
        return true;    }
    public static String getHeuteSortable() {
        return getTodaySortable();    }
    public static String getTodaySortable() {
        String sDatum = null;
        Date date = new Date();
          Calendar calendar = new GregorianCalendar();
          calendar.setTime(date);
        sDatum = itoa(calendar.get(Calendar.YEAR));
        if (calendar.get(Calendar.MONTH) < 9) {
            sDatum += "0";        }
        sDatum += itoa(calendar.get(Calendar.MONTH) + 1);
        if (calendar.get(Calendar.DATE) < 10) {
            sDatum += "0";        }
        sDatum += itoa(calendar.get(Calendar.DATE));
        return sDatum;    }
    public static String concatPath(String sPath_, String sFile_) {
        return FileUtil.concatPath(sPath_, sFile_);    }
    public static DataInputStream openFile(String sFile) {
        FileInputStream fis;
        try {            fis = new FileInputStream(sFile);
            if (fis != null) {
                DataInputStream dis = new DataInputStream(fis);
                return(dis);            }
        } catch (Exception e) {        }
        return(null);    }
    public static DataOutputStream openOutputFile(String sFile) {
        FileOutputStream fos;
        try {            fos = new FileOutputStream(sFile);
            if (fos != null) {
                DataOutputStream dos = new DataOutputStream(fos);
                return(dos);            }
        } catch (Exception e) {        }
        return(null);    }
   public static String readFile(String FileName)             throws IOException,                      FileNotFoundException   {
        StringBuffer sb = new StringBuffer();
      FileInputStream fis;
      fis = new FileInputStream(FileName);
      int oneChar;
      while ((oneChar=fis.read()) != -1) {
            if (oneChar != 13) {
                sb.append((char)oneChar);            }        }
        fis.close();
      return sb.toString();   }}
