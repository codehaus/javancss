public class Jacob {
    public void selectMainClass() {
        // package holen, das FilePath entspricht
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
                break;
            }
        }
        if (pckThisPackage == null) {
            AWTUtil.showMessage("Your application directory is not a package directory!\nFirst create a package that corresponds to the application directory.");
            _pController.requestFocus();
            return;
        }

        Vector vClassNames = pckThisPackage.getClassNames();
        if (vClassNames.size() == 0) {
            AWTUtil.showMessage("There are no classes to select in the main package\n" +
                                      pckThisPackage.getName() + "!\nPlease first create at least one new class inside that package.");
            _pController.requestFocus();
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
        String sPackageName = pckSelected.getName();
        for(Enumeration eDelClasses = pckSelected.getClassNamesElements();
             eDelClasses.hasMoreElements(); )
        {
            String sClassName = (String)eDelClasses.nextElement();
            _htDependencies.remove(sPackageName + "." + sClassName);
        }
        pckSelected.removeClasses(vSelection);

        _showPackages();
        showClasses();
    }

    public void addClasses() {
        Vector vNewClassPackages = _pckMain.getPackagesWithNewClasses();

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
            _pController.requestFocus();
            return;
        }

        Vector vSelection = AWTUtil.
                 inputDoubleListCancel("Please select the Classes to add!",
                                              vPackages, vClasses, true);
        _pController.requestFocus();
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
        Util.debug("Jacob: exit");
        String sFileFullName = _pController.getInit().getFileFullName();
        if (sFileFullName != null && (!sFileFullName.equals(""))) {
            _gnudoit("(set-screen-width " +
                        _pController.getInit().
                        getKeyValue(S_EMACSORGWIDTH) + ")");
        }
    }

    // test funktionen -------------------------------------------------

    public void getMainPackage(Test pTest_) {
        Util.panicIf(pTest_ == null);
        pTest_.setValue(_pckMain);
    }
}
