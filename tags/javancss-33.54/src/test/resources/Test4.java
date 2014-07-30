public class Jacob {
    public void removePackage() {
        for(Enumeration eDelClasses = pckDel.getAllFullClassNames(true, true).elements(); eDelClasses.hasMoreElements(); ) {
            String sNextFullClassName = (String)eDelClasses.nextElement();
            _htDependencies.remove(sNextFullClassName);
        }
        for(Enumeration eDelClasses = pckDel.getAllFullClassNames(true, true).elements();
             eDelClasses.hasMoreElements(); ) {
            String sNextFullClassName = (String)eDelClasses.nextElement();
            _htDependencies.remove(sNextFullClassName);
        }
        for(Enumeration eDelClasses = pckDel.getAllFullClassNames(true, true).elements();
             eDelClasses.hasMoreElements(); )
        {
            String sNextFullClassName = (String)eDelClasses.nextElement();
            _htDependencies.remove(sNextFullClassName);
        }
    }
}
