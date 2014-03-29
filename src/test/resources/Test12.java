import java.util.*;
public class Test12 {
public static String readFile(URL location)             throws MalformedURLException,                      IOException   {
      InputStream is = location.openStream();
      int oneChar;
      StringBuffer sb = new StringBuffer();
      while ((oneChar=is.read()) != -1) {
         sb.append((char)oneChar);        }
        return(sb.toString());   }
    public static void writeFile(String sFileName, String sContent)             throws IOException    {
        FileOutputStream fos = new FileOutputStream(sFileName);
      for (int i=0; i < sContent.length(); i++) {
            fos.write(sContent.charAt(i));        }
        fos.close();    }
    public static boolean equalsFile(String sFileNameA_,                                                String sFileNameB_)    {
        String sFileContentA = "";
        String sFileContentB = "";
        try {            sFileContentA = readFile(sFileNameA_);
            sFileContentB = readFile(sFileNameB_);
        } catch(Exception e) {
            return false;        }
        return(sFileContentA.equals(sFileContentB));    }
    protected static String _getFileName (Frame parent,                                                      String Title,                                                      String sFileName_,                                                      int Mode) {
        FileDialog fd;
        Frame f;
        String sRetVal = null;
        try {            f=null;
            if (parent == null) {
                f = new Frame(Title);
                f.pack();
                fd = new FileDialog(f, Title, Mode);            }
            else
                fd = new FileDialog(parent, Title, Mode);
            fd.setFile(sFileName_);
            fd.show();
            if (f != null)
                f.dispose();
            if (fd                != null &&                 fd.getDirectory() != null &&                 fd.getFile()      != null)            {
                sRetVal = fd.getDirectory() + fd.getFile();            }
        } catch(AWTError e) {            ;
        } catch(Exception e) {            ;        }
        return sRetVal;   }
    protected static String _getFileName (Frame parent, String Title,                                                      int Mode)    {
        return _getFileName(parent, Title, "*.*", Mode);   }
    public static String getFileName(Frame parent, String Title) {
       return _getFileName(parent, Title, FileDialog.LOAD);   }
   public static String getFileName(String Title, String sFileName) {
       return _getFileName(new Frame(), Title, sFileName, FileDialog.LOAD);   }
    public static boolean existsFile(String sFileName_) {
        panicIf(sFileName_ == null, "Util: existsFile");
        File pFile = new File(sFileName_);
        return(pFile.isFile());    }
    public static boolean existsDir(String sDirName_) {
        panicIf(sDirName_ == null, "Util: existsDir");
        File pFile = new File(sDirName_);
        return(pFile.isDirectory());    }
    public static boolean exists(String sFileOrDirName_) {
        panicIf(sFileOrDirName_ == null, "Util: exists");
        return(existsFile(sFileOrDirName_) ||                 existsDir(sFileOrDirName_));    }
    public static void fillRect(Graphics g_, int x_, int y_,                                         int width_, int height_, Color color_)    {
        Color clrCurrent = g_.getColor();
        g_.setColor(color_);
        g_.fillRect(x_, y_, width_, height_);
        g_.setColor(clrCurrent);    }
    public static Dimension getScreenSize(Component comp) {
        Toolkit tlk = comp.getToolkit();
        Dimension dim = tlk.getScreenSize();
        return(dim);    }
    public static int width(Component component) {
        return getWidth(component);    }
    public static int getWidth(Component component) {
        Dimension dim = component.minimumSize();
        return dim.width;    }
    public static int height(Component component) {
        return getHeight(component);    }
    public static int getHeight(Component component) {
        Dimension dim = component.minimumSize();
        return dim.height;    }
    public static void maximizeWindow(Window win) {
        win.move(0, 0);
        win.resize(getScreenSize(win));    }
    public static void centerComponent(Component cmpObject) {
        Dimension dimObject = cmpObject.size();
        Dimension dimScreen = getScreenSize(cmpObject);
        int posX;
        int posY;
        posX = (dimScreen.width - dimObject.width)/2;
        if (posX < 0) {
            posX = 0;        }
        posY = (dimScreen.height - dimObject.height)/2;
        if (posY < 0) {
            posY = 0;        }
        cmpObject.move(posX, posY);    }
    public static boolean isOKOrCancel(String sMessage_) {
        CubbyHole ch = new CubbyHole();
        OKCancelDialog dlgOKCancel = new OKCancelDialog(ch, sMessage_);
        dlgOKCancel.dispose();
        return(ch.get() != 0);    }
    public static void showMessage(String sMessages_) {
        MessageBox dlgMessage = new MessageBox(sMessages_);
        dlgMessage.dispose();    }
    public static void showAboutDialog(Init pInit_) {
        CubbyHole ch = new CubbyHole();
        AboutDialog dlgAbout = new AboutDialog(ch, pInit_);
        dlgAbout.dispose();    }
    public static String inputCancel(String sPrint_) {
        return inputCancel(sPrint_, "");    }
    public static String inputCancel(String sPrint_, String sInit_) {
        InputCancelDialog dlgInput = new InputCancelDialog(sPrint_, sInit_);
        String sRetVal = dlgInput.getValue();
        if (!dlgInput.isOk()){
            sRetVal = null;        }
        dlgInput.dispose();
        return sRetVal;    }
    public static String inputListCancel(String sPrint_, Vector vsItems_) {
        ListCancelSelector dlgInput =                 new ListCancelSelector(sPrint_, vsItems_);
        String sRetVal = dlgInput.getValue();
        dlgInput.dispose();
        return sRetVal;    }
    public static boolean showDocument(Applet applet, String sUrl) {
        return showDocument(applet.getAppletContext(), sUrl);    }
    public static boolean showDocument(AppletContext appcontext,                                                  String sUrl)    {
        try {            appcontext.showDocument(new URL(sUrl),"");
        } catch (Exception e) {
            return true;        }
        return false;    }
    public static void system (String sCommand)             throws Exception    {
        try {            Process p  = Runtime.getRuntime().exec(sCommand);
        } catch (Exception e){
            throw e;        }    }
    private static Object    _objSwap;
    private static boolean    _bNochKeinSwap = true;
    public static Object swap(Object objFirst, Object objSecond) {
        panicIf(_bNochKeinSwap == false);
        _bNochKeinSwap = false;
        _objSwap = objFirst;
        return(objSecond);    }
    public static Object swap() {
        panicIf(_bNochKeinSwap == true);
        _bNochKeinSwap = true;
        return(_objSwap);    }}
