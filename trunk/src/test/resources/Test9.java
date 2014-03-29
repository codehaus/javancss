package test;
import java.awt.*;
import java.util.*;
import java.io.*;
import ccl.util.*;
import ccl.awt.*;
import psp.timelog.Main;
public class Test9 {
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
    public static final String S_PROJSUFFIX = "ProjectSuffix";
    private Controller _pController;
    private Package _pckMain = new Package();
    private Package _pckActual = null;
    private String _sMainClass = "";
    private Hashtable _htDependencies = new Hashtable();
    private void _setGnuclientPath(){
    if (_sGnuclientPath == null) {
        _sGnuclientPath = _pController.getInit().getKeyValue(S_GNUCLIENTPATH);        }    }
    private boolean _bGnudoitFirstTime = true;
    private void _gnudoit(String sLispCommand) {
        _setGnuclientPath();
        try { Util.system(FileUtil.concatPath(_sGnuclientPath, STR_GNUDOIT) +                            " " + sLispCommand);
        } catch(Exception e) {
            if (_bGnudoitFirstTime) {
                _bGnudoitFirstTime = false;
                AWTUtil.showMessage("Error: the gnudoit helper application did not execute properly.\n" +                                          "Maybe it is not properly installed together with the Emacs editor.\n" +                                          "You can find the installing documentation of the gnuserv package for the ntemacs at:\n" +                                          "http://www.cs.washington.edu/homes/voelker/ntemacs.html#assoc");
                _pController.requestFocus();            }        }    }
    private void _showPackages() {
        Vector vsPackages = _pckMain.getPackageNames(true);
        _pController.getPackageList().removeAll();
        _pController.getPackageList().add(vsPackages);    }
    private void _setNewEmacsWidth() {
        Init pInit = _pController.getInit();
        int emacsNewWidth = Util.atoi(pInit.getKeyValue(S_EMACSNEWWIDTH));
        if (emacsNewWidth <= 0) {
            emacsNewWidth = EMACS_WORK_WIDTH;
            pInit.setKeyValue(S_EMACSNEWWIDTH,                Util.itoa(EMACS_WORK_WIDTH));        }
        int emacsOriginalWidth = Util.atoi(pInit.getKeyValue(S_EMACSORGWIDTH));
        if (emacsOriginalWidth <= 0) {
            emacsOriginalWidth = EMACS_ORIG_WIDTH;
            pInit.setKeyValue(S_EMACSORGWIDTH,                                    Util.itoa(EMACS_ORIG_WIDTH));        }
        _gnudoit("(set-screen-width " + emacsNewWidth + ")");
        String sProjectSuffix = pInit.getKeyValue(S_PROJSUFFIX);
        if (sProjectSuffix.equals("")) {
            pInit.setKeyValue(S_PROJSUFFIX, S_STANDARD_PROJSUFFIX);        }    }
    private void _setProjectSuffix() {
        Init pInit = _pController.getInit();
        String sProjectSuffix = pInit.getKeyValue(S_PROJSUFFIX);
        if (sProjectSuffix.equals("")) {
            pInit.setKeyValue(S_PROJSUFFIX, S_STANDARD_PROJSUFFIX);        }    }
    public Jacob(Controller pController_, String[] asArgs_) {
        super();
        _pController = pController_;
        Init pInit = _pController.getInit();
        String sFileFullName;
        if (asArgs_.length > 0) {
            pInit.setFileFullName(asArgs_[0]);        }
        sFileFullName = pInit.getFileFullName();
        _setProjectSuffix();
        if (sFileFullName == null || sFileFullName.equals("")) {
            sFileFullName = FileUtil.getFileName("Choose a Project",                                                             "*" + pInit.getKeyValue(S_PROJSUFFIX));
            Util.debug("Jacob: <init>: sFileFullName: " + sFileFullName);
            if (sFileFullName == null || sFileFullName.equals("")) {
                Util.println("\nNo Project File specified.");
                return;
            } else {
                pInit.setFileFullName(sFileFullName);            }        }
        _setNewEmacsWidth();    }
    public void loadProject() {
        String sProjectFile = null;
        _sMainClass = "";
        _pckActual = null;
        Init pInit = _pController.getInit();
        String sProjFile = FileUtil.concatPath(pInit.getFilePath(),                                                            pInit.getFileName().toLowerCase()) +                 pInit.getKeyValue(S_PROJSUFFIX);
        try {            FileInputStream pFileOutputStream = new FileInputStream(sProjFile);
            ObjectInputStream oisProject = new ObjectInputStream(pFileOutputStream);
            Integer iDummy = (Integer)oisProject.readObject();
            iDummy = (Integer)oisProject.readObject();
            _sMainClass = (String)oisProject.readObject();
            _pckMain = new Package();
            _pckMain.readExternal(oisProject);
            _htDependencies = (Hashtable)oisProject.readObject();
            oisProject.close();
        } catch(Exception e) {
            _sMainClass = "";
            _pckMain = new Package();
            _htDependencies = new Hashtable();        }
        _showPackages();    }
    public void save() {
        Init pInit = _pController.getInit();
        String sProjectFile = FileUtil.concatPath(pInit.getFilePath(),                                                                pInit.getFileName().toLowerCase()) +                 pInit.getKeyValue(S_PROJSUFFIX);
        try {            FileOutputStream pFileOutputStream = new FileOutputStream(sProjectFile);
            ObjectOutput pObjectOutput = new ObjectOutputStream(pFileOutputStream);
            int version = _pController.getInit().getVersion();
            int release = _pController.getInit().getRelease();
            pObjectOutput.writeObject(new Integer(version));
            pObjectOutput.writeObject(new Integer(release));
            pObjectOutput.writeObject(_sMainClass);
            _pckMain.writeExternal(pObjectOutput);
            pObjectOutput.writeObject(_htDependencies);
            pObjectOutput.flush();
            pObjectOutput.close();
        } catch(Exception e) {}    }
    public void showClasses() {
        ListBorder lstClasses = _pController.getClassesList();
        lstClasses.removeAll();
        ListBorder lstPackages = _pController.getPackageList();
        if (lstPackages.getSelectedIndex() == -1) {
            if (_pckActual == null) {
                return;            }
            int index = 0;
            for(Enumeration e = lstPackages.getItemsElements(); e.hasMoreElements(); ) {
                String sItem = (String)e.nextElement();
                if ( sItem.equals(_pckActual.getName()) ) {
                    lstPackages.select(index);
                    break;                }
                index++;            }        }
        String sPackageName = lstPackages.getSelectedItem();
        _pckActual = _pckMain.getPackage(sPackageName);
        Enumeration eClassNames = _pckActual.getClassNamesElements();
        lstClasses.add(eClassNames);    }
    public void editClass() {
        String sClassName = _pController.getClassesList().getSelectedItem();
        String sClassFileFullName = FileUtil.concatPath(_pckActual.getPath(),                                                                        sClassName + ".java");
        editFile(sClassFileFullName);    }
    public void openProject(String sProjectFullName) {
        _pController.getInit().setFileFullName(sProjectFullName);
        _pController.init();    }
    public void openProject() {
        String sProjectFullName = null;
        sProjectFullName = FileUtil.getFileName("Choose a Project", "*" +                                                             _pController.getInit().getKeyValue(S_PROJSUFFIX));
        if (sProjectFullName == null) {
            Util.println("No Project File specified.");
            return;        }
        openProject(sProjectFullName);    }
    public void openOldProject(String sOldProjectName) {
        String sOldProjectFullName = null;
        sOldProjectFullName = _pController.getInit().getOldFileFullName(sOldProjectName);
        Util.debug("OldProjektFullName: " + sOldProjectFullName);
        Util.panicIf(sOldProjectFullName == null, "Jacob: openOldProject");
        openProject(sOldProjectFullName);
        _pController.requestFocus();    }
    public synchronized void createMakefile() {
        Makefile pMakefile = new Makefile(_pController.getInit(),                                                     _pckMain, _sMainClass, _htDependencies);
        _pController.requestFocus();    }
    public synchronized void setClassDependencies() {
        Vector vAllClassNames = _pckMain.getAllFullClassNames(true, true);
        DependenciesDialog dlgDepends = new DependenciesDialog(_htDependencies,                                                                                 vAllClassNames);
        dlgDepends.show();
        if (dlgDepends.isOk()) {
            _htDependencies = dlgDepends.getDependencies();        }
        dlgDepends.dispose();
        _pController.requestFocus();    }
    public synchronized void editFile(String sFullFileName_) {
        _setGnuclientPath();
        try {     Util.system(FileUtil.concatPath(_sGnuclientPath, STR_EMACS) +                            " " + sFullFileName_);
        } catch(Exception e) {
            if (_bGnudoitFirstTime) {
                _bGnudoitFirstTime = false;
                AWTUtil.showMessage("Error: the gnuclient helper application did not execute properly.\n" +                                          "Maybe it is not properly installed together with the Emacs editor.\n" +                                          "You can find the install documentation of the gnuserv package for the ntemacs at:\n" +                                          "http://www.cs.washington.edu/homes/voelker/ntemacs.html#assoc");
                _pController.requestFocus();            }        }    }
    public void editProjectFile(String sFile_) {
        editFile(_pController.getInit().getFilePath() + sFile_);    }
    private String _sJavaLanguageSpec = "";
    public void startJavaLanguageSpecHelp() {
        if (_sJavaLanguageSpec.equals("")) {
            _sJavaLanguageSpec = _pController.getInit().getKeyValue("JavaLanguageSpec");        }
        try {            Util.system("winhelp " + _sJavaLanguageSpec);
        } catch(Exception e) {
            AWTUtil.showMessage("Error: winhelp did not work right.\n" + e);
            _pController.requestFocus();        }    }
    private boolean _bTimelog = false;
    public synchronized boolean isTimelog() {
        return _bTimelog;    }
    public synchronized void execTimelog() {
        if (_bTimelog) {
            return;        }
        _bTimelog = true;
        class TimelogFunctor implements Functor {
            public void exec() {
                Init pInit = _pController.getInit();
                psp.timelog.Main pTimelog = new psp.timelog.Main(pInit.getFilePath() +                                                                                 "psp" + Init.S_FILE_SEPARATOR +                                                                                 pInit.getFileName().toLowerCase() +                                                                                 ".timelog");
                _bTimelog = false;            }        }
        FunctionThread pFunctionThread = new FunctionThread(new TimelogFunctor());    }
    public void insertPackage() {
        Vector vCheckboxes = new Vector();
        Vector vValues = new Vector();
        vCheckboxes.addElement("Has source");
        vValues.addElement(new Boolean(true));
        vCheckboxes.addElement("Is supposed to be compiled");
        vValues.addElement(new Boolean(true));
        Vector vNewPackage = AWTUtil.inputCheckboxCancel(vCheckboxes,                                                                         vValues,                                                                         "Package: ");
        if (vNewPackage == null) {
            return;        }
        String sNewPackage = (String)vNewPackage.elementAt(0);
        if (_pckMain.contains(sNewPackage)) {
            AWTUtil.showMessage("Package is already there!");
            _pController.requestFocus();
            return;        }
        boolean bSource = ((Boolean)vNewPackage.elementAt(1)).booleanValue();
        boolean bCompile = ((Boolean)vNewPackage.elementAt(2)).booleanValue();
        Package pckNew = new Package(sNewPackage, bSource, bCompile);
        if (pckNew.getPath().equals("")) {
            AWTUtil.showMessage("Package not found!");
            _pController.requestFocus();
            return;        }
        _pckMain.insert(pckNew);
        _showPackages();
        showClasses();
        _pController.requestFocus();    }
    public void removePackage() {
        if (_pckMain.isEmpty()) {
            return;        }
        Vector vPackages = _pckMain.getAllPackageNames();
        vPackages.removeElementAt(0);
        String sDelPackage = AWTUtil.inputListCancel("Please select a package to remove!",                                                                    vPackages);
        _pController.requestFocus();
        if (sDelPackage == null) {
            return;        }
        Package pckDel = _pckMain.getPackage(sDelPackage);
        for(Enumeration eDelClasses = pckDel.getAllFullClassNames(true, true).elements();             eDelClasses.hasMoreElements(); )        {
            String sNextFullClassName = (String)eDelClasses.nextElement();
            _htDependencies.remove(sNextFullClassName);        }
        _pckMain.remove(sDelPackage);
        if (_pckActual != null && sDelPackage.equals(_pckActual.getName())) {
            _pckActual = null;        }
        _showPackages();
        showClasses();    }
    private Vector _selectClasses(String sMessage_) {
        return _selectClasses(sMessage_, false);}
    private Vector _selectClasses(String sMessage_, boolean bMultiSelect_) {
        Vector vClassPackages = _pckMain.getClassPackages();
        Vector vClasses = new Vector();
        Vector vPackages = new Vector();
        for(Enumeration e = vClassPackages.elements(); e.hasMoreElements(); ) {
            Package pckNext = (Package)e.nextElement();
            vPackages.addElement(pckNext.getName());
            vClasses.addElement(pckNext.getClassNames());        }
        Vector vSelection = AWTUtil.                 inputDoubleListCancel(sMessage_, vPackages, vClasses,                                              bMultiSelect_);
        _pController.requestFocus();
        return vSelection;    }
    public void selectMainClass() {
        String sThisPath = _pController.getInit().getFilePath();
        Util.debug("Jacob: selectMainClass: sThisPath: " + sThisPath);
        Package pckThisPackage = null;
        for(Enumeration e = _pckMain.elements(); e.hasMoreElements(); ) {
            Package pckNext = (Package)e.nextElement();
            String sPackagePath = (String)pckNext.getPath();
            Util.debug("Jacob: selectMainClass: sPackagePath: " + sPackagePath);
            if (FileUtil.equalsPath(sThisPath, sPackagePath)) {
                Util.debug("Jacob: selectMainClass: equals");
                pckThisPackage = pckNext;
                break;            }        }
        if (pckThisPackage == null) {
            AWTUtil.showMessage("Your application directory is not a package directory!\nFirst create a package that corresponds to the application directory.");
            _pController.requestFocus();
            return;        }
        Vector vClassNames = pckThisPackage.getClassNames();
        if (vClassNames.size() == 0) {
            AWTUtil.showMessage("There are no classes to select in the main package\n" +                                      pckThisPackage.getName() + "!\nPlease first create at least one new class inside that package.");
            _pController.requestFocus();
            return;        }
        String sSelection = AWTUtil.inputListCancel("Please select the Main Class\nout of the " +                                                                  pckThisPackage.getName() + " package!",                                                                  vClassNames);
        _pController.requestFocus();
        if (sSelection == null) {
            return;        }
        _sMainClass = pckThisPackage.getName() + "." + sSelection;    }
    public void removeClasses() {
        Vector vSelection = _selectClasses("Please select the Classes to remove!", true);
        if (vSelection == null) {
            return;        }
        Package pckSelected = _pckMain.getPackage((String)vSelection.elementAt(0));
        vSelection.removeElementAt(0);
        String sPackageName = pckSelected.getName();
        for(Enumeration eDelClasses = pckSelected.getClassNamesElements();             eDelClasses.hasMoreElements(); )        {
            String sClassName = (String)eDelClasses.nextElement();
            _htDependencies.remove(sPackageName + "." + sClassName);        }
        pckSelected.removeClasses(vSelection);
        _showPackages();
        showClasses();    }
    public void addClasses() {
        Vector vNewClassPackages = _pckMain.getPackagesWithNewClasses();
        Vector vClasses = new Vector();
        Vector vPackages = new Vector();
        for(Enumeration e = vNewClassPackages.elements(); e.hasMoreElements(); ) {
            Package pckNext = (Package)e.nextElement();
            vPackages.addElement(pckNext.getName());
            vClasses.addElement(pckNext.getClassNames());        }
        if (vClasses.size() == 0) {
            AWTUtil.showMessage("There is no Class to add!\nMaybe you want to add another Package.");
            _pController.requestFocus();
            return;        }
        Vector vSelection = AWTUtil.                 inputDoubleListCancel("Please select the Classes to add!",                                              vPackages, vClasses, true);
        _pController.requestFocus();
        if (vSelection == null) {
            return;        }
        Package pckSelected = _pckMain.getPackage((String)vSelection.elementAt(0));
        vSelection.removeElementAt(0);
        pckSelected.addClasses(vSelection);
        _showPackages();
        showClasses();    }
    public void exit() {
        Util.debug("Jacob: exit");
        String sFileFullName = _pController.getInit().getFileFullName();
        if (sFileFullName != null && (!sFileFullName.equals(""))) {
            _gnudoit("(set-screen-width " +                        _pController.getInit().                        getKeyValue(S_EMACSORGWIDTH) + ")");        }    }
    public void getMainPackage(Test pTest_) {
        Util.panicIf(pTest_ == null);
        pTest_.setValue(_pckMain);    }}
