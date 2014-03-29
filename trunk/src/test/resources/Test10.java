package ccl.util;
import java.awt.*;
import java.util.*;
import java.applet.Applet;
import java.applet.AppletContext;
import java.net.URL;
import java.io.*;
import java.net.*;
import java.lang.Math;
import ccl.awt.*;
public class Test10 {
	private static Random _rnd = new Random();
	public static int atoi(String s) {
		panicIf(s == null);
		if (s.equals("")) {
			return 0;		}
		return(Integer.parseInt(s.trim()));	}
	public static String itoa(int i) {
		return(String.valueOf(i));	}
	public static long max(long a_, long b_) {
		if (a_ > b_) {
			return a_;		}
		return(b_);	}
	public static int max(int a_, int b_) {
		if (a_ > b_) {
			return a_;		}
		return(b_);	}
	public static int min(int a_, int b_) {
		if (a_ < b_) {
			return a_;		}
		return(b_);	}
	public static void print(char c) {
		System.out.print(c);
		System.out.flush();	}
	public static void print(String s) {
		System.out.print(s);
		System.out.flush();	}
	public static void println(String s) {
		System.out.println(s);	}
	public static void println(Exception e) {
		System.err.println("Exception: " + e.getMessage());
		Thread.dumpStack();
		println(Thread.currentThread().toString());	}
	public static void panicIf(boolean bPanic) {
		if (bPanic) {
			throw(new ApplicationException());		}	}
	public static void panicIf(boolean bPanic, String sMessage) {
		if (bPanic) {
			throw(new ApplicationException(sMessage));		}	}
	private static boolean _bDebug = false;
	public static void setDebug(boolean bDebug) {
		_bDebug = bDebug;	}
	public static void debug(Object obj) {
		if (_bDebug) {
			println(obj.toString());		}	}
	public static void debug(int i) {
		if (_bDebug) {
			println("Int: " + i);		}	}
	public static void showLiveSignal() {
		showLiveSignal('.');	}
	public static void showLiveSignal(char c) {
		print(c);	}
	public static boolean rnd() {
		return(rnd(1) == 0);	}
	public static int rnd(int bis) {
		return rnd(0, bis);	}
	public static int rnd(int von, int bis) {
		panicIf(bis <= von);
		float fR = _rnd.nextFloat();
		int r = (int)(fR*(bis-von+1)+von);
		return( r );	}
	public static float rnd(float f) {
		float fR = (float)_rnd.nextFloat();
		return( f*fR );	}
	public static double rnd(double df) {
		double dR = _rnd.nextDouble();
		return( df * dR );	}
	private static final char[] _acUmlaut = { 'ä', 'Ä', 'ö', 'Ö',															 'ü', 'Ü', 'ß', 'é' };
	public static boolean isAlpha(char c_) {
		if (('A' <= c_ && c_ <= 'Z') ||			 ('a' <= c_ && c_ <= 'z'))		{
			return true;		}
		for(int i = 0; i < _acUmlaut.length; i++) {
			if (c_ == _acUmlaut[i]) {
				return true;			}		}
		return false;	}
	public static long timeToSeconds(String sTime_) {
		return ((long)MultiDate.getSecondsFromTime(sTime_));	}
	public static int getOccurances(String source, int zeichen) {
		int anzahl = -1;
		int index = 0;
		do {			index = source.indexOf(zeichen, index) + 1;
			anzahl++;
		} while (index != 0);
		return(anzahl);	}
	public static String multiplyChar(char c, int anzahl) {
		String s = "";
		while (anzahl > 0) {
			s += c;
			anzahl--;		}
		return(s);	}
	public static String multiplyChar(String sFill, int anzahl) {
		String sRet = "";
		while (anzahl > 0) {
			sRet += sFill;
			anzahl--;		}
		return(sRet);	}
	public static String paddWith(int number_, int stellen_,											char cPadd_)	{
		String sRetVal = itoa(number_);
		if (sRetVal.length() >= stellen_) {
			return(sRetVal);		}
		String sPadding = multiplyChar(cPadd_,												 stellen_ - sRetVal.length());
		sRetVal = sPadding + sRetVal;
		return(sRetVal);	}
	public static String paddWithSpace(int number, int stellen) {
		return paddWith(number, stellen, ' ');	}
	public static String paddWithZero(int number, int stellen) {
		return paddWith(number, stellen, '0');	}
	public static String rtrim(String s) {
		int index = s.length()-1;
		while (index >= 0 && s.charAt(index) == ' ') {
			index--;		}
		return(s.substring(0, index+1));	}
	public static String ltrim(String s) {
		int index = 0;  //s.length()-1;
		while (index < s.length() && s.charAt(index) == ' ') {
			index++;		}
		return(s.substring(index, s.length()));	}
	public static String unifySpaces(String s) {
		String sRetVal = new String();
		String sRest = s.trim();
		int index = 0;//s.length()-1;
		while (sRest != null && sRest.length() > 0) {
			index = sRest.indexOf(' ');
			if (index < 0) {
				sRetVal += sRest;
				sRest = null;
			} else {
				sRetVal += sRest.substring(0, index+1);
				sRest = sRest.substring(index+1, sRest.length());
				sRest = ltrim(sRest);			}		}
		return(sRetVal);	}
	public static String unicode2ascii(String sUnicode_) {
		panicIf(5 == 5, "Util: unicode2ascii: Sorry, diese Funktion ist deprecated und muss neu geschrieben und neu getested werden.");
		return null;	}
	public static int compare(String firstString,									  String anotherString)	{
		int len1 = firstString.length();
    	int len2 = anotherString.length();
		int n = Math.min(len1, len2);
    	int i = 0;
		int j = 0;
    	while (n-- != 0) {
			char c1 = firstString.charAt(i++);
			char c2 = anotherString.charAt(j++);
			if (c1 != c2) {
        		return(c1 - c2);			}    	}
		return(len1 - len2);	}
	public static String concat(Vector pVector_, String sWith) {
		String sRetVal = new String();
		if (pVector_ == null || pVector_.size() < 1) {
			return sRetVal;		}
		if (sWith == null) {
			sWith = "";		}
		Enumeration e = pVector_.elements();
		sRetVal += e.nextElement().toString();
		for( ; e.hasMoreElements(); ) {
			sRetVal += sWith + e.nextElement().toString();		}
		return sRetVal;	}
	public static String concat(Vector pVector_) {
		return concat(pVector_, "");	}
	public static boolean isEmpty(String sTest_) {
		if (sTest_ == null || sTest_.equals("")) {
			return true;		}
		return false;	}
	public static Vector stringToLines(int lines_,												  String pString_, char cCutter_)	{
		int maxLines = Integer.MAX_VALUE;
		if (lines_ > 0) {
			maxLines = lines_;		}
		Vector vRetVal = new Vector();
		if (pString_ == null) {
			return vRetVal;		}
		int startIndex = 0;
		for( ; maxLines > 0; maxLines-- ) {
			int endIndex = pString_.indexOf(cCutter_, startIndex);
			if (endIndex == -1) {
				if (startIndex < pString_.length()) {
					endIndex = pString_.length();
				} else {
					break;				}			}
			String sLine = pString_.substring(startIndex, endIndex);
			vRetVal.addElement((Object)sLine);
			startIndex = endIndex + 1;		}
		return vRetVal;	}
	public static Vector stringToLines(String pString_, char cCutter_) {
		return stringToLines(0, pString_, cCutter_);	}
	public static Vector stringToLines(String pString_) {
		return stringToLines(pString_, '\n');	}
	public static Vector stringToLines(int lines_, String pString_) {
		return stringToLines(lines_, pString_, '\n');	}
	public static boolean equalsCaseless(String sA_, String sB_) {
		String sFirst = sA_.toUpperCase();
		String sSecond = sB_.toUpperCase();
		return sFirst.equals(sSecond);	}
	public static String firstCharToUpperCase(String pString_) {
		String sRetVal = new String();
		if (pString_ == null || pString_.length() == 0) {
			return(sRetVal);		}
		sRetVal = pString_.substring(0, 1).toUpperCase() +				    pString_.substring(1, pString_.length());
		return(sRetVal);	}
	public static String firstCharToLowerCase(String pString_) {
		String sRetVal = new String();
		if (pString_ == null || pString_.length() == 0) {
			return(sRetVal);		}
		sRetVal = pString_.substring(0, 1).toLowerCase() +				    pString_.substring(1, pString_.length());
		return(sRetVal);	}
	public static String replace(String pString_,										  String sOld_, String sNew_)	{
		panicIf(sNew_ == null || sOld_ == null);
		if (pString_ == null) {
			return null;		}
		String sRetVal = new String(pString_);
		int indexNew = sNew_.length();
		int index = sRetVal.indexOf(sOld_);
		while(index > -1) {
			sRetVal = sRetVal.substring(0, index) +					 sNew_ + sRetVal.substring(index + sOld_.length(),														sRetVal.length());
			index += indexNew;
			if (index >= sRetVal.length()) {
				break;			}
			index = sRetVal.indexOf(sOld_, index + indexNew);		}
		return sRetVal;	}
	public static boolean isSpaceLine(String sLine_) {
		if (sLine_ == null || sLine_.length() == 0) {
			return true;		}
		for(int index = 0; index < sLine_.length(); index++) {
			char c = sLine_.charAt(index);
			if (c != ' ' && c != '\t' && c != '\n') {
				return false;			}		}
		return true;	}
	public static String getHeuteSortable() {
		return getTodaySortable();	}
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
		return FileUtil.concatPath(sPath_, sFile_);	}
	public static DataInputStream openFile(String sFile) {
		FileInputStream fis;
		try {			fis = new FileInputStream(sFile);
			if (fis != null) {
				DataInputStream dis = new DataInputStream(fis);
				return(dis);			}
		} catch (Exception e) {		}
		return(null);	}
	public static DataOutputStream openOutputFile(String sFile) {
		FileOutputStream fos;
		try {			fos = new FileOutputStream(sFile);
			if (fos != null) {
				DataOutputStream dos = new DataOutputStream(fos);
				return(dos);			}
		} catch (Exception e) {		}
		return(null);	}
   public static String readFile(String FileName)			 throws IOException,					  FileNotFoundException   {
		StringBuffer sb = new StringBuffer();
      FileInputStream fis;
      fis = new FileInputStream(FileName);
      int oneChar;
      while ((oneChar=fis.read()) != -1) {
			if (oneChar != 13) {
				sb.append((char)oneChar);			}		}
		fis.close();
      return sb.toString();   }
   public static String readFile(URL location)			 throws MalformedURLException,					  IOException   {
      InputStream is = location.openStream();
      int oneChar;
      StringBuffer sb = new StringBuffer();
      while ((oneChar=is.read()) != -1) {
         sb.append((char)oneChar);		}
		is.close();
		return(sb.toString());   }
	public static void writeFile(String sFileName, String sContent)			 throws IOException	{
		FileOutputStream fos = new FileOutputStream(sFileName);
      for (int i=0; i < sContent.length(); i++) {
			fos.write(sContent.charAt(i));		}
		fos.close();	}
	public static boolean equalsFile(String sFileNameA_,												String sFileNameB_)	{
		String sFileContentA = "";
		String sFileContentB = "";
		try {			sFileContentA = readFile(sFileNameA_);
			sFileContentB = readFile(sFileNameB_);
		} catch(Exception e) {
			return false;		}
		return(sFileContentA.equals(sFileContentB));	}
	protected static String _getFileName (Frame parent,													  String Title,													  String sFileName_,													  int Mode) {
		FileDialog fd;
		Frame f;
		String sRetVal = null;
		try {			f=null;
			if (parent == null) {
				f = new Frame(Title);
				f.pack();
				fd = new FileDialog(f, Title, Mode);			}
			else
				fd = new FileDialog(parent, Title, Mode);
			fd.setFile(sFileName_);
			fd.show();
			if (f != null)
				f.dispose();
			if (fd                != null &&				 fd.getDirectory() != null &&				 fd.getFile()      != null)			{
				sRetVal = fd.getDirectory() + fd.getFile();			}
		} catch(AWTError e) {			;
		} catch(Exception e) {			;		}
		return sRetVal;   }
	protected static String _getFileName (Frame parent, String Title,													  int Mode)	{
		return _getFileName(parent, Title, "*.*", Mode);   }
	public static String getFileName(Frame parent, String Title) {
       return _getFileName(parent, Title, FileDialog.LOAD);   }
   public static String getFileName(String Title, String sFileName) {
       return _getFileName(new Frame(), Title, sFileName, FileDialog.LOAD);   }
	public static boolean existsFile(String sFileName_) {
		panicIf(sFileName_ == null, "Util: existsFile");
		File pFile = new File(sFileName_);
		return(pFile.isFile());	}
	public static boolean existsDir(String sDirName_) {
		panicIf(sDirName_ == null, "Util: existsDir");
		File pFile = new File(sDirName_);
		return(pFile.isDirectory());	}
	public static boolean exists(String sFileOrDirName_) {
		panicIf(sFileOrDirName_ == null, "Util: exists");
		return(existsFile(sFileOrDirName_) ||				 existsDir(sFileOrDirName_));	}
	public static void fillRect(Graphics g_, int x_, int y_,										 int width_, int height_, Color color_)	{
		Color clrCurrent = g_.getColor();
		g_.setColor(color_);
		g_.fillRect(x_, y_, width_, height_);
		g_.setColor(clrCurrent);	}
	public static Dimension getScreenSize(Component comp) {
		Toolkit tlk = comp.getToolkit();
		Dimension dim = tlk.getScreenSize();
		return(dim);	}
	public static int width(Component component) {
		return getWidth(component);	}
	public static int getWidth(Component component) {
		Dimension dim = component.minimumSize();
		return dim.width;	}
	public static int height(Component component) {
		return getHeight(component);	}
	public static int getHeight(Component component) {
		Dimension dim = component.minimumSize();
		return dim.height;	}
	public static void maximizeWindow(Window win) {
		win.move(0, 0);
		win.resize(getScreenSize(win));	}
	public static void centerComponent(Component cmpObject) {
		Dimension dimObject = cmpObject.size();
		Dimension dimScreen = getScreenSize(cmpObject);
		int posX;
		int posY;
		posX = (dimScreen.width - dimObject.width)/2;
		if (posX < 0) {
			posX = 0;		}
		posY = (dimScreen.height - dimObject.height)/2;
		if (posY < 0) {
			posY = 0;		}
		cmpObject.move(posX, posY);	}
	public static boolean isOKOrCancel(String sMessage_) {
		CubbyHole ch = new CubbyHole();
		OKCancelDialog dlgOKCancel = new OKCancelDialog(ch, sMessage_);
		dlgOKCancel.dispose();
		return(ch.get() != 0);	}
	public static void showMessage(String sMessages_) {
		MessageBox dlgMessage = new MessageBox(sMessages_);
		dlgMessage.dispose();	}
	public static void showAboutDialog(Init pInit_) {
		CubbyHole ch = new CubbyHole();
		AboutDialog dlgAbout = new AboutDialog(ch, pInit_);
		dlgAbout.dispose();	}
	public static String inputCancel(String sPrint_) {
		return inputCancel(sPrint_, "");	}
	public static String inputCancel(String sPrint_, String sInit_) {
		InputCancelDialog dlgInput = new InputCancelDialog(sPrint_, sInit_);
		String sRetVal = dlgInput.getValue();
		if (!dlgInput.isOk()){
			sRetVal = null;		}
		dlgInput.dispose();
		return sRetVal;	}
	public static String inputListCancel(String sPrint_, Vector vsItems_) {
		ListCancelSelector dlgInput =				 new ListCancelSelector(sPrint_, vsItems_);
		String sRetVal = dlgInput.getValue();
		dlgInput.dispose();
		return sRetVal;	}
	public static boolean showDocument(Applet applet, String sUrl) {
	    return showDocument(applet.getAppletContext(), sUrl);	}
	public static boolean showDocument(AppletContext appcontext,												  String sUrl)	{
		try {			appcontext.showDocument(new URL(sUrl),"");
		} catch (Exception e) {
			return true;		}
		return false;	}
	public static void system (String sCommand)			 throws Exception	{
		try {			Process p  = Runtime.getRuntime().exec(sCommand);
		} catch (Exception e){
			throw e;		}	}
	private static Object	_objSwap;
	private static boolean	_bNochKeinSwap = true;
	public static Object swap(Object objFirst, Object objSecond) {
		panicIf(_bNochKeinSwap == false);
		_bNochKeinSwap = false;
		_objSwap = objFirst;
		return(objSecond);	}
	public static Object swap() {
		panicIf(_bNochKeinSwap == true);
		_bNochKeinSwap = true;
		return(_objSwap);	}
	private static int _swap;
	private static boolean	_bNochKeinIntSwap = true;
	public static int swapInt(int first, int second) {
		panicIf(_bNochKeinIntSwap == false);
		_bNochKeinIntSwap = false;
		_swap = first;
		return(second);	}
	public static int swapInt() {
		panicIf(_bNochKeinIntSwap == true);
		_bNochKeinIntSwap = true;
		return(_swap);	}
	public static Vector objectsToVector(Object apObjects[]) {
		Vector vRetVal = new Vector();
		if (apObjects != null && apObjects.length > 0) {
			for(int nr = 0; nr < apObjects.length; nr++) {
				vRetVal.addElement(apObjects[nr]);			}		}
		return vRetVal;	}
	public static Vector filter(Vector pVector_,										 final String sBadElement_)	{
		panicIf(sBadElement_ == null);
		Predicate pFilter = new Predicate() {
			public boolean test(Object pObject_) {
				return(!sBadElement_.equals((String)pObject_));			}		};
		return filter(pVector_, pFilter);	}
	public static Vector filter(Vector pVector_,										 Vector vBadElements_)	{
		Vector vRetVal = pVector_;
		panicIf(vBadElements_ == null);
		for(Enumeration e = vBadElements_.elements();			 e.hasMoreElements(); )		{
			String sBadElement = (String)e.nextElement();
			vRetVal = filter(vRetVal, sBadElement);		}
		return vRetVal;	}
	public static Vector filter(Vector pVector_,										 Predicate pFilter_)	{
		Vector vRetVal = new Vector();
		for(Enumeration e = pVector_.elements(); e.hasMoreElements(); ) {
			Object pObject = e.nextElement();
			if (pFilter_.test(pObject)) {
				vRetVal.addElement(pObject);			}		}
		return vRetVal;	}
	public static Vector map(Vector pVector_,									 Transformer pTransformer_)	{
		Vector vRetVal = new Vector();
		for(Enumeration e = pVector_.elements(); e.hasMoreElements(); ) {
			Object pObject = e.nextElement();
			vRetVal.addElement(pTransformer_.transform(pObject));		}
		return vRetVal;	}
	public static boolean contains(Vector pVector_,											 final String sFind_)	{
		panicIf(sFind_ == null);
		Predicate pFilter = new Predicate() {
			public boolean test(Object pObject_) {
				return(sFind_.equals((String)pObject_));			}		};
		return contains(pVector_, pFilter);	}
	public static boolean contains(Vector pVector_,											 Predicate pFilter_)	{
		Vector vRetVal = new Vector();
		for(Enumeration e = pVector_.elements(); e.hasMoreElements(); ) {
			Object pObject = e.nextElement();
			if (pFilter_.test(pObject)) {
				return true;			}		}
		return false;	}
	public static Vector sort(final Vector pVector_) {
		ObjectComparator classcmp = new ObjectComparator();
		return sort(pVector_, classcmp);	}
	public static void quickSort(Object s[], int lo, int hi, Comparator cmp) {
		if (lo >= hi)
			return;
		int mid = (lo + hi) / 2;
		if (cmp.compare(s[lo], s[mid]) > 0) {
			Object tmp = s[lo];
			s[lo] = s[mid];
			s[mid] = tmp; 		}
		if (cmp.compare(s[mid], s[hi]) > 0) {
			Object tmp = s[mid];
			s[mid] = s[hi];
			s[hi] = tmp; // swap
			if (cmp.compare(s[lo], s[mid]) > 0) {
				Object tmp2 = s[lo];
				s[lo] = s[mid];
				s[mid] = tmp2; 			}		}
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
				--right;			}
			else
				break;		}
		quickSort(s, lo, left, cmp);
		quickSort(s, left+1, hi, cmp);	}
	public static void quickSort(Vector s, int lo, int hi, Comparator cmp) {
		panicIf (s == null);
		if (lo >= hi)
			return;
		int mid = (lo + hi) / 2;
		if (cmp.compare(s.elementAt(lo), s.elementAt(mid)) > 0) {
			Object tmp = s.elementAt(lo);
			s.setElementAt(s.elementAt(mid), lo);
			s.setElementAt(tmp, mid);		}
		if (cmp.compare(s.elementAt(mid), s.elementAt(hi)) > 0) {
			Object tmp = s.elementAt(mid);
			s.setElementAt(s.elementAt(hi), mid);
			s.setElementAt(tmp, hi);
			if (cmp.compare(s.elementAt(lo), s.elementAt(mid)) > 0) {
				Object tmp2 = s.elementAt(lo);
				s.setElementAt(s.elementAt(mid), lo);
				s.setElementAt(tmp2, mid);			}		}
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
				--right;			}
			else
				break;		}
		quickSort(s, lo, left, cmp);
		quickSort(s, left+1, hi, cmp);	}
	public static Vector sort(final Vector vInput_, Comparator pComparator_) {
		panicIf(vInput_ == null);
		Vector vRetVal = (Vector)vInput_.clone();
		if (vInput_.size() > 0) {
			quickSort(vRetVal, 0, vRetVal.size() - 1, pComparator_);		}
		return vRetVal;	}
	public static Vector concat(Vector vFirst_, Vector vSecond_) {
		Vector vRetVal = (Vector)vFirst_.clone();
		for(Enumeration e = vSecond_.elements(); e.hasMoreElements(); ) {
			vRetVal.addElement(e.nextElement());		}
		return vRetVal;	}
	public static Vector subtract(Vector vSource_, Vector vToDelete_) {
		Vector vRetVal = (Vector)vSource_.clone();
		for(Enumeration e = vToDelete_.elements(); e.hasMoreElements(); ) {
			vRetVal.removeElement(e.nextElement());		}
		return vRetVal;	}}
