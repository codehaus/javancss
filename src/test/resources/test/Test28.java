package jacob;
import java.awt.*;
import java.util.*;
import java.io.*;
import ccl.util.*;
import ccl.util.ObjectComparator;
import ccl.awt.*;
import psp.timelog.Main;
import pat.Regex;
public class Jacob3 {
    private static final String STR_GNUDOIT = "gnudoit ";
    private static final String STR_EMACS = "gnuclient -q";
    private static final String S_GNUCLIENTPATH = "GnuclientPath";
    private static final String S_EMACSNEWWIDTH = "EmacsNewWidth";
    private static final String S_EMACSORGWIDTH = "EmacsOriginalWidth";
    private String _sGnuclientPath = null;
    private static final String STR_JAVA = ".java";
    private static final int EMACS_WORK_WIDTH = 72;
    private static final int EMACS_ORIG_WIDTH = 98;
    private static final String S_STANDARD_PROJSUFFIX = ".proj";
    private static final String S_PROJSUFFIX = "ProjectSuffix";
    private static final String S_PROJECTTAG = "ProjectTag";
    private static final String S_JAVACFLAG = "JavacFlags";
    Controller _pController;
    private Package _pckMain = new Package();
    private Package _pckActual = null;
    private String _sMainClass = "";
    private void _setGnuclientPath(){
        if (_sGnuclientPath == null) {
            _sGnuclientPath = _pController.getInit().getKeyValue(S_GNUCLIENTPATH);        }    }
    private boolean _bGnudoitFirstTime = true;
    private void _gnudoit(String sLispCommand) {
        _setGnuclientPath();
        try {            Util.system(FileUtil.concatPath(_sGnuclientPath, STR_GNUDOIT) +                            " " + sLispCommand);
        } catch(Exception e) {
            if (_bGnudoitFirstTime) {
                _bGnudoitFirstTime = false;
                AWTUtil.showMessage("Error: the gnudoit helper application did not execute properly.\n" +                                          "Maybe it is not properly installed together with the Emacs editor.\n" +                                          "You can find the installing documentation of the gnuserv package for the ntemacs at:\n" +                                          "http://www.cs.washington.edu/homes/voelker/ntemacs.html#assoc");            }        }    }
    private void _showPackages() {
        Vector vsPackages = _pckMain.getPackageNames(true);
        _pController.getPackageList().removeAll();
        _pController.getPackageList().add(vsPackages);    }
    private String _getClassFile(Package pPackage_, String sClassName_) {
        String sClassFullFileName = FileUtil.                 concatPath(pPackage_.getPath(), sClassName_) + ".java";
        String sClassFileContent = null;
        try {            sClassFileContent = FileUtil.readFile(sClassFullFileName);
        } catch(Exception e) {
            AWTUtil.showMessage("Error: the class file\n" +                                      sClassFullFileName + "\ncould not be opened!");
            return null;        }
        return sClassFileContent;    }
    private String _getClassFile(String sPackageName_,                                          String sClassName_)    {
        Package pPackage = _pckMain.getPackage(sPackageName_);
        return _getClassFile(pPackage, sClassName_);    }
    private String _getClassFile(String sFullClassName_) {
        return _getClassFile(Package.getPackageName(sFullClassName_),                                    Package.getClassName(sFullClassName_));    }
    private String _getRcsVersion() {
        String sClassFileContent = _getClassFile(_sMainClass);
        if (sClassFileContent == null) {
            return (String)null;        }
        Regex pRegex = new Regex("\\$Header: [^ ]*\\.java[^ ]* (\\d+\\.\\d+) ");
        pRegex.search(sClassFileContent);
        String sVersion = pRegex.substring(0);
        return sVersion;    }
    private String _getRcs() {
        String sRcs = "";
        if (Jacob3.Main.B_PRIVATE && (!_sMainClass.equals(""))) {
            sRcs = "\ncm : \n";
            String sTag = _pController.getInit().                     getKeyValue(S_PROJECTTAG);;
            if (!sTag.equals("")) {
                sTag = "-n" + sTag;
                String sVersion = _getRcsVersion();
                if (sVersion == null) {
                    sVersion = "1";
                } else {
                    // z.B.: 1.13 -> 14
                    sVersion = "" + (Util.atoi(sVersion.substring(sVersion.indexOf('.') + 1,
                                                                                 sVersion.length())) + 1);
                }
                sTag += sVersion;
                String sClassFullFileName = _pckMain.getClassFullFileName(_sMainClass);
                sRcs += "\tci -l -f " + sClassFullFileName + "\n";
            }

            sRcs += "\tci -l " + sTag + " Makefile\n";
            sRcs += "\tci -l " + sTag + " " +
                     _pController.getInit().getFileName().toLowerCase() +
                     _pController.getInit().getKeyValue(S_PROJSUFFIX) + "\n";

            Vector vpckClassPackages = _pckMain.getClassPackages(true, true);
            for(Enumeration eClassPackages = vpckClassPackages.elements();
                 eClassPackages.hasMoreElements(); )
            {
                Package pckNext = (Package)eClassPackages.nextElement();
                String sPackagePath = pckNext.getPath();

                for(Enumeration eClasses = pckNext.getClassNamesElements();
                     eClasses.hasMoreElements(); )
                {
                    String sNextClass = (String)eClasses.nextElement();
                    sRcs += "\tci -l " + sTag + " " +
                             FileUtil.concatPath(sPackagePath,
                                                        sNextClass) + ".java\n";
                }
            }
        }

        return(sRcs + "\n");
    }

    private String _getMakefileAll() {
        String sAll = "all : ";
        String sClasses = "";
        //    jede Klasse mit vollem Pfad aneinander reihen mit " "
        Util.debug("Java_Home: " + System.getProperty("java.home"));
        for(Enumeration e = _pckMain.getAllClassPathNamesElements(true, true);
             e.hasMoreElements(); )
        {
            String sFullName = (String)e.nextElement();
            sAll += sFullName + ".class ";
            sClasses += sFullName + ".class : " + sFullName +
                     ".java\n\t" +
                     FileUtil.concatPath(System.getProperty("java.home"),
                                                "bin/javac") +
                     " " + _pController.getInit().getKeyValue(S_JAVACFLAG) +
                     " -classpath " +
                     System.getProperties().getProperty("java.class.path") +
                     " " + sFullName + ".java\n\n";
        }
        sAll += "\n\n" + sClasses;

        return sAll;
    }

    private String _getClean() {
        String sClean = "\nclean : \n";
        Vector vpckClassPackages = _pckMain.getClassPackages(true, true);
        for(Enumeration eClassPackages = vpckClassPackages.elements();
             eClassPackages.hasMoreElements(); )
        {
            Package pckNext = (Package)eClassPackages.nextElement();
            String sPackagePath = pckNext.getPath();

            sClean += "\tcd " + sPackagePath + "\n";

            for(Enumeration eClasses = pckNext.getClassNamesElements();
                 eClasses.hasMoreElements(); )
            {
                String sNextClass = (String)eClasses.nextElement();
                sClean += "\t-rm " + sNextClass + ".class\n";
                sClean += "\t-rm " + sNextClass + "$$*.class\n";
            }
        }
        sClean += "\tcd " + _pController.getInit().getApplicationPath() + "\n";

        return sClean;
    }

    private String _getMakefileZip() {
        String sZip = "zip : \n";

        Init pInit = _pController.getInit();
        String sFullProjPath = pInit.getFilePath();
        String sFileName = pInit.getFileName().toLowerCase();
        sZip += "\t-mv " + sFileName + ".zip " + sFileName + ".zip.bak\n";

        // Makefile and .proj
        // kucken ob projpath package entspricht
        // wenn nein, dann ein dir zurueck
        String sProjPath = null;
        for(Enumeration ePackages = _pckMain.elements(); ePackages.hasMoreElements(); ) {
            Package pckNext = (Package)ePackages.nextElement();
            if (FileUtil.equalsPath(sFullProjPath, pckNext.getPath())) {
                sProjPath = pckNext.getName();
                break;
            }
        }
        if (sProjPath != null) {
            //sProjPath = new String(_sMainClass);
            //sProjPath = sProjPath.substring(0, sProjPath.lastIndexOf('.'));
            sProjPath = sProjPath.replace('.', '/') + "/";
        } else {
            AWTUtil.showMessage("Error:\nThe project file is not inside an package.\n" +
                                      "The separation of the project data and\nthe source code is not yet supported.");
            return;
        }

        sZip += "\tcd ..";
        int dirLevel = Util.getOccurances(sProjPath, '/');
        for(int i = 0; i < dirLevel - 1; i++) {
            sZip += "/..";
        }
        sZip += "\n";
        sZip += "\t-zip " + sProjPath + sFileName + " " + sProjPath + "Makefile\n";
        sZip += "\t-zip " + sProjPath + sFileName + " " + sProjPath + sFileName + pInit.getKeyValue(S_PROJSUFFIX) + "\n";
        sZip += "\tcd " + sProjPath + "\n";

        Vector vpckClassPackages = _pckMain.getClassPackages(true, true);
        for(Enumeration eClassPackages = vpckClassPackages.elements();
             eClassPackages.hasMoreElements(); )
        {
            Package pckNext = (Package)eClassPackages.nextElement();
            String sPackagePath = pckNext.getPath();
            String sSubPath = pckNext.getName().replace('.', '/') + "/";
            sPackagePath = FileUtil.concatPath(sPackagePath, "..");
            dirLevel = Util.getOccurances(sSubPath, '/');
            for(int level = 0; level < dirLevel - 1; level++) {
                sPackagePath += "/..";
            }
            sPackagePath += "/";
            sZip += "\tcd " + sPackagePath + "\n";
            for(Enumeration eClasses = pckNext.getClassNamesElements();
                 eClasses.hasMoreElements(); )
            {
                String sNextClass = (String)eClasses.nextElement();
                sZip += "\tzip " + sFullProjPath + sFileName + " " +
                         sSubPath + sNextClass + ".java\n";
            }
        }
        sZip += "\tcd " + sFullProjPath + "\n";
        sZip = sZip.replace('\\', '/');

        return sZip;
    }

    private String _getMakePublic() {
        if (!Jacob3.Main.B_PRIVATE) {
            return "";
        }
        Init pInit = _pController.getInit();
        // Application.bat erzeugen
        String sBatFileContent = "@echo off\nREM Edit the environment variables in line 9 and 14 if they are not already properly set\n\n" +
                 "set _JAVA_HOME_ORIG=%JAVA_HOME%\nset _CLASSPATH_ORIG=%CLASSPATH%\n\n" +
                 "if NOT \"%JAVA_HOME%\"==\"\" goto endif1\n\tREM #################### EDIT THIS ENVIRONMENT VARIABLE IF NOT ALREADY SET #################\n" +
                 "\tset JAVA_HOME=" +
                 System.getProperty("java.home") + "\n" +":endif1\n\n" +
                 "if NOT \"%CLASSPATH%\"==\"\" goto endif2\n" +
                 "\tREM #################### EDIT THIS ENVIRONMENT VARIABLE IF NOT ALREADY SET #################\n" +
                 "\tset CLASSPATH=" +
                 System.getProperty("java.class.path") + "\n:endif2\n\n" +
                 "%JAVA_HOME%\\bin\\java -classpath %CLASSPATH% " + _sMainClass + "\n\n" +
                 "set JAVA_HOME=%_JAVA_HOME_ORIG%\nset CLASSPATH=%_CLASSPATH_ORIG%\n" +
                 "set _JAVA_HOME_ORIG=\nset _CLASSPATH_ORIG=\n";

        String sFullProjPath = pInit.getFilePath();
        String sFileName = pInit.getFileName().toLowerCase();

        String sBatFullFileName = FileUtil.concatPath(sFullProjPath,
                                                                     sFileName) + ".bat";
        try {
            FileUtil.writeFile(sBatFullFileName, sBatFileContent);
        } catch(Exception e) {
            AWTUtil.showMessage("Error: File \n" + sBatFullFileName + "\ncould not be created!");
            return "";
        }

        // zip erstellen
        String sPublic = "\npublic : \n";

        //    Name des zip files
        String sZipFileName = new String(sFileName);
        sZipFileName += _getRcsVersion();

        sZip += "\t-mv " + sZipFileName + ".zip " + sZipFileName + ".zip.bak\n";

        // .bat
        // readme.txt
        // help/*.*
        // kucken ob projpath package entspricht
        // wenn nein, dann ein dir zurueck
        String sProjPath = null;
        for(Enumeration ePackages = _pckMain.elements(); ePackages.hasMoreElements(); ) {
            Package pckNext = (Package)ePackages.nextElement();
            if (FileUtil.equalsPath(sFullProjPath, pckNext.getPath())) {
                sProjPath = pckNext.getName();
                break;
            }
        }
        if (sProjPath != null) {
            sProjPath = sProjPath.replace('.', '/') + "/";
        } else {
            AWTUtil.showMessage("Error:\nThe project file is not inside an package.\n" +
                                      "The separation of the project data and\nthe source code is not yet supported.");
            return;
        }

        sPublic += "\tcd ..";
        int dirLevel = Util.getOccurances(sProjPath, '/');
        for(int i = 0; i < dirLevel - 1; i++) {
            sPublic += "/..";
        }
        sPublic += "\n";
        sPublic += "\t-zip " + sProjPath + sZipFileName + " " + sProjPath + sFileName + ".bat\n";
        sPublic += "\t-zip " + sProjPath + sZipFileName + " " + sProjPath + sFileName + ".gif\n";
        sPublic += "\t-zip " + sProjPath + sZipFileName + " " + sProjPath + sFileName + "*.ico\n";
        sPublic += "\t-zip " + sProjPath + sZipFileName + " " + sProjPath + sFileName + "readme.txt\n";
        sPublic += "\t-zip " + sProjPath + sZipFileName + " " + sProjPath + "/help/*.*\n";
        sPublic += "\tcd " + sProjPath + "\n";

        Vector vpckClassPackages = _pckMain.getClassPackages(false, false);
        for(Enumeration eClassPackages = vpckClassPackages.elements();
             eClassPackages.hasMoreElements(); )
        {
            Package pckNext = (Package)eClassPackages.nextElement();
            String sPackagePath = pckNext.getPath();
            String sSubPath = pckNext.getName().replace('.', '/') + "/";
            sPackagePath = FileUtil.concatPath(sPackagePath, "..");
            dirLevel = Util.getOccurances(sSubPath, '/');
            for(int level = 0; level < dirLevel - 1; level++) {
                sPackagePath += "/..";
            }
            sPackagePath += "/";
            sPublic += "\tcd " + sPackagePath + "\n";
            for(Enumeration eClasses = pckNext.getClassNamesElements();
                 eClasses.hasMoreElements(); )
            {
                String sNextClass = (String)eClasses.nextElement();
                sPublic += "\tzip " + sFullProjPath + sZipFileName + " " +
                         sSubPath + sNextClass + ".class\n";
                sPublic += "\t-zip " + sFullProjPath + sZipFileName + " " +
                         sSubPath + sNextClass + "$$*.class\n";
            }
        }
        sPublic += "\tcd " + sFullProjPath + "\n";
        sPublic = sPublic.replace('\\', '/');

        return sPublic;
    }

    public Jacob3(Controller pController_, String[] asArgs_) {
        super();

        _pController = pController_;
        Init pInit = _pController.getInit();

        // get Project Name
        String sFileFullName;
        if (asArgs_.length > 0) {
            pInit.setFileFullName(asArgs_[0]);
        }
        sFileFullName = pInit.getFileFullName();

        // Wenn nicht ok, in SpezialFenster abfragen und in ini Datei speichern
        if (sFileFullName == null) {
            sFileFullName = FileUtil.getFileName("Choose a Project",
                                                             "*" + pInit.getKeyValue(S_PROJSUFFIX));
            if (sFileFullName == null) {
                // na gut dann eben nicht
                Util.println("No Project File specified.");
                _pController.exit();
            }
            pInit.setFileFullName(sFileFullName);
        }

        // Emacs Gr��e anpassen
        int emacsNewWidth = Util.atoi(pInit.getKeyValue(S_EMACSNEWWIDTH));
        if (emacsNewWidth <= 0) {
            emacsNewWidth = EMACS_WORK_WIDTH;
            pInit.setKeyValue(S_EMACSNEWWIDTH,
                                    Util.itoa(EMACS_WORK_WIDTH));
        }
        int emacsOriginalWidth = Util.atoi(pInit.getKeyValue(S_EMACSORGWIDTH));
        if (emacsOriginalWidth <= 0) {
            emacsOriginalWidth = EMACS_ORIG_WIDTH;
            pInit.setKeyValue(S_EMACSORGWIDTH,
                                    Util.itoa(EMACS_ORIG_WIDTH));
        }
        _gnudoit("(set-screen-width " + emacsNewWidth + ")");

        String sProjectSuffix = pInit.getKeyValue(S_PROJSUFFIX);
        if (sProjectSuffix.equals("")) {
            pInit.setKeyValue(S_PROJSUFFIX, S_STANDARD_PROJSUFFIX);
        }
    }

    public void loadProject() {
        String sProjectFile = null;
        _sMainClass = "";

        Init pInit = _pController.getInit();
        String sProjFile = FileUtil.concatPath(pInit.getFilePath(),
                                                            pInit.getFileName().toLowerCase()) +
                 pInit.getKeyValue(S_PROJSUFFIX);
        try {
            FileInputStream pFileOutputStream = new FileInputStream(sProjFile);
            ObjectInputStream oisProject = new ObjectInputStream(pFileOutputStream);
            _sMainClass = (String)oisProject.readObject();
            _pckMain = new Package();
            _pckMain.readExternal(oisProject);
            oisProject.close();
        } catch(Exception e) {
            _pckMain = new Package();
        }

        // Packages anzeigen
        _showPackages();
    }

    public void save() {
        Init pInit = _pController.getInit();
        String sProjectFile = FileUtil.concatPath(pInit.getFilePath(),
                                                                pInit.getFileName().toLowerCase()) +
                 pInit.getKeyValue(S_PROJSUFFIX);
        try {
            FileOutputStream pFileOutputStream = new FileOutputStream(sProjectFile);
            ObjectOutput pObjectOutput = new ObjectOutputStream(pFileOutputStream);
            pObjectOutput.writeObject(_sMainClass);
            _pckMain.writeExternal(pObjectOutput);
            pObjectOutput.flush();
            pObjectOutput.close();
        } catch(Exception e) {
        }
    }

    public void showClasses() {
        ListBorder lstClasses = _pController.getClassesList();
        lstClasses.removeAll();
        if (_pController.getPackageList().getSelectedIndex() == -1) {
            return;
        }

        // get PackageName
        String sPackageName = _pController.getPackageList().
                 getSelectedItem();
        // set actual Package
        _pckActual = _pckMain.getPackage(sPackageName);

        // get Classes from Package
        Enumeration eClassNames = _pckActual.getClassNamesElements();

        // show Classes
        lstClasses.add(eClassNames);
    }

    public void editClass() {
        String sClassName = _pController.getClassesList().getSelectedItem();
        String sClassFileFullName = FileUtil.concatPath(_pckActual.getPath(),
                                                                        sClassName + ".java");
        editFile(sClassFileFullName);
    }

    public void openProject(String sProjectFullName) {
        // Es gibt auf jeden Fall schon ein aktuelles Projekt / eigendlich doch nicht
        //_pController.getInit().makeThisFileOld(); ist jetzt innerhalb setFileFullName
        _pController.getInit().setFileFullName(sProjectFullName);
        _pController.init();
    }

    public void openProject() {
        String sProjectFullName = null;
        sProjectFullName = FileUtil.getFileName("Choose a Project", "*" +
                                                             _pController.getInit().
                                                             getKeyValue(S_PROJSUFFIX));
        if (sProjectFullName == null) {
            // na gut dann eben nicht
            Util.println("No Project File specified.");
            return;
        }

        openProject(sProjectFullName);
    }

    public void openOldProject(String sOldProjectName) {
        String sOldProjectFullName = null;

        sOldProjectFullName = _pController.getInit().getOldFileFullName(sOldProjectName);
        Util.debug("OldProjektFullName: " + sOldProjectFullName);

        Util.panicIf(sOldProjectFullName == null, "Jacob3: openOldProject");

        openProject(sOldProjectFullName);
    }

    public synchronized void createMakefile() {
        // testen ob makefile schon existiert
        boolean bOK = !FileUtil.exists(_pController.getInit().
                                                 getFilePath() +
                                                 Controller.STR_MAKEFILE);
        if (!bOK) {
            bOK = Util.isOKOrCancel("Ein Makefile existiert bereits. Wollen Sie trotzdem fortfahren?");
            Util.debug("Jacob3: createMakefile: Thread: " + Thread.currentThread().toString());
            Util.debug("Jacob3: createMakefile: bOK: " + bOK);
            if (!bOK) {
                return;
            }
        }

        // Erste Zeile: all
        String sAll = _getMakefileAll();

        // clean : --------------------------------------------------
        String sClean = _getClean();

        // rcs : --------------------------------------------------
        String sRcs = _getRcs();

        // zip : --------------------------------------------------
        String sZip = _getMakefileZip();

        // public release
        String sPublic = _getMakePublic();

        // run : --------------------------------------------------
        String sRun = "";
        if (!_sMainClass.equals("")) {
            sRun += "run : all\n\t" +
                     FileUtil.concatPath(System.getProperty("java.home"),
                                                "bin/java") +
                     " -classpath " +
                     System.getProperties().getProperty("java.class.path") +
                     " " + _sMainClass + "\n";
        }

        String sMakefile = sAll + sRun + sClean;
        if (File.separatorChar == '\\') {
            sMakefile = Util.replace(sMakefile, "/", Init.S_FILE_SEPARATOR);
        } else {
            sMakefile = Util.replace(sMakefile, "\\", Init.S_FILE_SEPARATOR);
        }

        sMakefile += sZip + sRcs + sPublic;

        Util.debug("Jacob3: createMakefile: sMakefile: " + sMakefile);
        try {
            FileUtil.writeFile(_pController.getInit().getFilePath() + Controller.STR_MAKEFILE,
                                     sMakefile);
        } catch(Exception e) {
            AWTUtil.showMessage(e.toString() + "\n\n" +
                                      "Das Makefile konnte nicht erzeugt werden.");
        }
    }

    public synchronized void editFile(String sFullFileName_) {
        _setGnuclientPath();
        try {
            Util.system(FileUtil.concatPath(_sGnuclientPath, STR_EMACS) +
                            " " + sFullFileName_);
        } catch(Exception e) {
            if (_bGnudoitFirstTime) {
                _bGnudoitFirstTime = false;
                AWTUtil.showMessage("Error: the gnuclient helper application did not execute properly.\n" +
                                          "Maybe it is not properly installed together with the Emacs editor.\n" +
                                          "You can find the install documentation of the gnuserv package for the ntemacs at:\n" +
                                          "http://www.cs.washington.edu/homes/voelker/ntemacs.html#assoc");
            }
        }
    }

    public void editProjectFile(String sFile_) {
        editFile(_pController.getInit().getFilePath() + sFile_);
    }

    private String _sJavaLanguageSpec = "";

    public void startJavaLanguageSpecHelp() {
        if (_sJavaLanguageSpec.equals("")) {
            _sJavaLanguageSpec = _pController.getInit().getKeyValue("JavaLanguageSpec");
        }
        try {
            Util.system("winhelp " + _sJavaLanguageSpec);
        } catch(Exception e) {
            AWTUtil.showMessage("Error: winhelp did not work right.\n" + e);
        }
    }

    private boolean _bTimelog = false;

    public synchronized boolean isTimelog() {
        return _bTimelog;
    }

    public synchronized void execTimelog() {
        if (_bTimelog) {
            return;
        }
        _bTimelog = true;
        class TimelogFunctor implements Functor {
            public void exec() {
                Init pInit = _pController.getInit();
                psp.timelog.Main pTimelog = new psp.timelog.Main(pInit.getFilePath() +
                                                                                 "psp" + Init.S_FILE_SEPARATOR +
                                                                                 pInit.getFileName().toLowerCase() +
                                                                                 ".timelog");
                _bTimelog = false;
            }
        }
        FunctionThread pFunctionThread = new FunctionThread(new TimelogFunctor());
    }

    public void insertPackage() {
        // Package Name erfragen
        Vector vCheckboxes = new Vector();
        Vector vValues = new Vector();
        vCheckboxes.addElement("Has source");
        vValues.addElement(new Boolean(true));
        vCheckboxes.addElement("Is supposed to be compiled");
        vValues.addElement(new Boolean(true));

        Vector vNewPackage = Util.inputCheckboxCancel(vCheckboxes,
                                                                     vValues,
                                                                     "Package: ");
        if (vNewPackage == null) {
            return;
        }
        String sNewPackage = (String)vNewPackage.elementAt(0);
        if (_pckMain.contains(sNewPackage)) {
            AWTUtil.showMessage("Package is already there!");
            _pController.requestFocus();
            return;
        }

        // Package Pfad suchen
        boolean bSource = ((Boolean)vNewPackage.elementAt(1)).booleanValue();
        boolean bCompile = ((Boolean)vNewPackage.elementAt(2)).booleanValue();
        Package pckNew = new Package(sNewPackage, bSource, bCompile);
        if (pckNew.getPath().equals("")) {
            AWTUtil.showMessage("Package not found!");
            return;
        }
        _pckMain.insert(pckNew);

        _showPackages();
        showClasses();
    }

    public void removePackage() {
        if (_pckMain.isEmpty()) {
            return;
        }
        // Package Name erfragen
        Vector vPackages = _pckMain.getAllPackageNames();
        // Root Package entfernen
        vPackages.removeElementAt(0);
        String sDelPackage = AWTUtil.inputListCancel("Please select a package to remove!",
                                                                    vPackages);
        if (sDelPackage == null) {
            return;
        }

        _pckMain.remove(sDelPackage);

        _showPackages();
        showClasses();
    }

    private Vector _selectClasses(String sMessage_) {
        return _selectClasses(sMessage_, false);
    }

    private Vector _selectClasses(String sMessage_, boolean bMultiSelect_) {
        // get all packages with classes
        Vector vClassPackages = _pckMain.getClassPackages();

        // Enumerieren und Classen holen in zweiten vector
        Vector vClasses = new Vector();
        Vector vPackages = new Vector();
        for(Enumeration e = vClassPackages.elements(); e.hasMoreElements(); ) {
            Package pckNext = (Package)e.nextElement();
            vPackages.addElement(pckNext.getName());
            vClasses.addElement(pckNext.getClassNames());
        }

        Vector vSelection = AWTUtil.
                 inputDoubleListCancel(sMessage_, vPackages, vClasses,
                                              bMultiSelect_);

        return vSelection;
    }

    public void selectMainClass() {
        // package holen, das FilePath entspricht
        String sThisPath = _pController.getInit().getFilePath();
        Util.debug("Jacob3: selectMainClass: sThisPath: " + sThisPath);

        Package pckThisPackage = null;
        for(Enumeration e = _pckMain.elements(); e.hasMoreElements(); ) {
            Package pckNext = (Package)e.nextElement();
            String sPackagePath = (String)pckNext.getPath();
            Util.debug("Jacob3: selectMainClass: sPackagePath: " + sPackagePath);
            if (FileUtil.equalsPath(sThisPath, sPackagePath)) {
                Util.debug("Jacob3: selectMainClass: equals");
                pckThisPackage = pckNext;
                break;
            }
        }
        if (pckThisPackage == null) {
            AWTUtil.showMessage("Your application directory is not a package directory!\nFirst create a package that corresponds to the application directory.");
            return;
        }

        Vector vClassNames = pckThisPackage.getClassNames();
        if (vClassNames.size() == 0) {
            AWTUtil.showMessage("There are no classes to select in the main package\n" +
                                      pckThisPackage.getName() + "!\nPlease first create at least one new class inside that package.");
            return;
        }
        String sSelection = AWTUtil.inputListCancel("Please select the Main Class\nout of the " +
                                                                  pckThisPackage.getName() + " package!",
                                                                  vClassNames);
        _pController.requestFocus();
        if (sSelection == null) {
            // na dann eben nicht
            return;
        }
        _sMainClass = pckThisPackage.getName() + "." + sSelection;
    }

    public void removeClasses() {
        Vector vSelection = _selectClasses("Please select the Classes to remove!", true);
        if (vSelection == null) {
            // na dann eben nicht
            return;
        }
        Package pckSelected = _pckMain.getPackage((String)vSelection.elementAt(0));
        vSelection.removeElementAt(0);
        pckSelected.removeClasses(vSelection);

        _showPackages();
        showClasses();
    }

    public void addClasses() {
        // get neue Packages
        Package pckNew = new Package();
        //    get S�hne
        for(Enumeration eSons = _pckMain.getSubPackagesElements(); eSons.hasMoreElements(); ) {
            Package pckNext = (Package)eSons.nextElement();
            Package pckNewSon = new Package(pckNext.getName(), true, true);
            pckNew.insert(pckNewSon);
        }
        // remove deleted Packages
        Vector vsPackagesToRemove = new Vector();
        for(Enumeration eNewPackages = pckNew.elements(); eNewPackages.hasMoreElements(); ) {
            Package pckNext = (Package)eNewPackages.nextElement();
            Util.debug("Jacob3: addClasses: pckNext.getName(): " + pckNext.getName());
            if (!_pckMain.contains(pckNext.getName())) {
                Util.debug("Jacob3: addClasses: remove");
                vsPackagesToRemove.addElement(pckNext.getName());
            }
        }
        for(Enumeration eRemove = vsPackagesToRemove.elements();
             eRemove.hasMoreElements(); )
        {
            pckNew.remove((String)eRemove.nextElement());
        }

        // get all packages with classes
        Vector vClassPackages = _pckMain.getClassPackages();

        // removeClasses in neuen Packages
        for(Enumeration eClassPackages = vClassPackages.elements(); eClassPackages.hasMoreElements(); ) {
            Package pckNext = (Package)eClassPackages.nextElement();
            String sPackageName = pckNext.getName();
            Vector vClassNames = pckNext.getClassNames();
            pckNew.getPackage(sPackageName).removeClasses(vClassNames);
        }

        // get all new packages with classes
        Vector vNewClassPackages = pckNew.getClassPackages();

        // Enumerieren und Classen holen in zweiten vector
        Vector vClasses = new Vector();
        Vector vPackages = new Vector();
        for(Enumeration e = vNewClassPackages.elements(); e.hasMoreElements(); ) {
            Package pckNext = (Package)e.nextElement();
            vPackages.addElement(pckNext.getName());
            vClasses.addElement(pckNext.getClassNames());
        }

        if (vClasses.size() == 0) {
            AWTUtil.showMessage("There is no Class to add!\nMaybe you want to add another Package.");
            return;
        }

        Vector vSelection = AWTUtil.
                 inputDoubleListCancel("Please select the Classes to add!",
                                              vPackages, vClasses, true);
        if (vSelection == null) {
            return;
        }
        Package pckSelected = _pckMain.getPackage((String)vSelection.elementAt(0));
        vSelection.removeElementAt(0);
        pckSelected.addClasses(vSelection);

        _showPackages();
        showClasses();

    }

    public void exit() {
        Util.debug("Jacob3: exit");
        _gnudoit("(set-screen-width " +
                    _pController.getInit().getKeyValue(S_EMACSORGWIDTH) + ")");
    }
}
