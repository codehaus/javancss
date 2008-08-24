package ccl.swing;

import ccl.util.FileObject;
import ccl.util.FileUtil;
import ccl.util.Init;
import ccl.util.Util;
import java.net.URL;
import java.util.Enumeration;
import java.util.Vector;
import javax.help.CSH;
import javax.help.HelpSet;
import javax.help.HelpBroker;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 * // ToDo: Info und Hilfe standardm��ig bearbeiten
 * Shortcut ctrl-s wird automatisch f�r save oder speichern eingef�gt
 * in MainMenu.MainMenu().
 *
 * @version $Id$
 */
public class MainJMenuBar extends JMenuBar {
    public static final String S_HELP_CONTENTS = "&Contents...";

    private MainJFrame _pMainJFrame = null;

    public MainJMenuBar(Vector vMenus_, MainJFrame pMainJFrame_) {
        super();

        Util.panicIf(vMenus_ == null || pMainJFrame_ == null);
        Util.debug("MainJMenuBar: init");
        
        _pMainJFrame = pMainJFrame_;
        
        for(Enumeration e = vMenus_.elements(); e.hasMoreElements(); ) {
            Vector vMenu = (Vector)e.nextElement();
            MainJMenu pMainJMenu = new MainJMenu(vMenu, pMainJFrame_);
            Util.debug("MainJMenuBar: pMainMenu: " + pMainJMenu);
            add(pMainJMenu);
        }
    }

    /**
     * In the file menu above the exit item is a section with
     * old projects which has been opened before.
     * This section is separated by menu separators. If there has
     * not been any previous project open, only one separator
     * exists above the exit item.
     * 
     * This method updates the list with old projects (files).
     * It removes the old list, and uses the init object to
     * fill the new list.
     */
    public synchronized void updateMenu() {
        MainJMenu pFileJMenu = (MainJMenu)getMenu(0);
        Util.debug( this, "updateMenu().pFileJMenu: " + pFileJMenu);
        pFileJMenu.removeOldFileItems();
        pFileJMenu.insertOldFiles(_pMainJFrame);
    }

    class MainJMenu extends JMenu {
        /** First index of old files (projects) presented in file menu. */
        private int _oldFilesStartIndex = -1;
        /** Number of old files (projects) presented in file menu. */
        private int _oldFilesCount      =  0;
    
        private Vector _getOldFileItems(Init pInit_) {
            Vector vRetVal = new Vector();
        
            Enumeration eOldFiles = pInit_.getOldFilesElements();
            if (eOldFiles.hasMoreElements()) {
                vRetVal.addElement("--------");
            }
        
            int nr = pInit_.getOldFilesSize();
            _oldFilesCount = nr;
            for( ; eOldFiles.hasMoreElements(); ) {
                vRetVal.insertElementAt("" + nr + " " +
                                        Util.firstCharToUpperCase
                                        (((FileObject)eOldFiles.
                                          nextElement())
                                         .getName()),
                                        0);
                nr--;
            }
            
            return vRetVal;
        }
        
        private Enumeration _getMenuBodyElements(Vector vMenu_, Init pInit_) {
            Enumeration eRetVal = null;
            String sLabel = (String)vMenu_.elementAt(0);
            
            if (sLabel.equals("Datei") || sLabel.equals("File") ||
                sLabel.equals("&Datei") || sLabel.equals("&File"))
            {
                Vector vFileMenu = (Vector)vMenu_.clone();
                
                // --- Alte Dateien in Menu einf�gen ---
                
                int menuSlot = vFileMenu.size() - 1;
                if (menuSlot > 1) {
                    vFileMenu.insertElementAt("--------", menuSlot);
                    menuSlot++;
                }
                _oldFilesStartIndex = menuSlot-1;
                
                Vector vOldFileItems = _getOldFileItems(pInit_);
                
                vFileMenu = Util.insert(vFileMenu, vOldFileItems, menuSlot);
                
                eRetVal = vFileMenu.elements();
            } else {
                eRetVal = vMenu_.elements();
            }
            eRetVal.nextElement();
            
            return eRetVal;
        }

        private HelpBroker _getHelpBroker( MainJFrame pMainJFrame_ ) {
            HelpSet pHelpSet = null;
            String  sHSFile  = pMainJFrame_.getInit()
                                           .getHelpBrokerURL();
            try {
                URL urlHelpSet = new URL( sHSFile );
                Util.debug( this, "_getHelpBroker(..).urlHelpSet: " +
                            urlHelpSet );
                pHelpSet = new HelpSet( null, urlHelpSet );
                
                /*sHSFile = pMainJFrame_.getClass().getName();
                int lastDot = sHSFile.lastIndexOf( '.' );
                if ( lastDot == -1 ) {
                    sHSFile = "/doc";
                } else {
                    sHSFile = "/" 
                               + sHSFile.substring( 0, lastDot )
                                        .replace( '.', '/' );
                }
                sHSFile += pMainJFrame_.getInit()
                                       .getApplicationName()
                                       .toLower()
                           + ".hs";
                pHelpSet = HelpSet.findHelpSet( null, sHSFile );*/
            } catch( Exception pException ) {
                Util.printlnErr( "HelpSet " + sHSFile +
                                 " not found!" );
            }
            HelpBroker pHelpBroker = pHelpSet.createHelpBroker();
            
            return pHelpBroker;
        }
        
        public MainJMenu(Vector vMenu_, MainJFrame pMainJFrame_) {
            super((String)vMenu_.elementAt(0));
            
            setName( getText() );
            
            if ( getText().equals( "File" ) ) {
                setMnemonic( 'F' );
            }
            if ( getText().equals( "Edit" ) ) {
                setMnemonic( 'E' );
            }
            if ( getText().equals( "Help" ) ) {
                setMnemonic( 'H' );
            }
            
            Util.panicIf(vMenu_ == null || vMenu_.size() < 2);
            Util.panicIf(pMainJFrame_ == null);
            
            for(Enumeration e = _getMenuBodyElements(vMenu_,
                                                     pMainJFrame_.getInit());
                e.hasMoreElements(); )
            {
                Object oNext = e.nextElement();
                
                if (oNext instanceof Vector) {
                    // Rekursiv unter Menues
                    JMenu mSub = new MainJMenu((Vector)oNext,
                                               pMainJFrame_);
                    add(mSub);
                } else {
                    if ( oNext instanceof RunnableAction ) {
                        RunnableAction pRunnableAction = (RunnableAction)oNext;
                        pRunnableAction.setMainFrame
                               ( pMainJFrame_ );
                        JMenuItem miTemp = add( pRunnableAction );
                        Character charMnemonic = 
                               pRunnableAction.getMnemonic();
                        if ( charMnemonic != null ) {
                            miTemp.setMnemonic( charMnemonic.charValue() );
                        }
                        
                        KeyStroke ksAccelerator = pRunnableAction.getAccelerator();
                        if ( ksAccelerator != null ) {
                            miTemp.setAccelerator( ksAccelerator );
                        }
                        miTemp.setName( miTemp.getText() );
                        Util.debug( this, "<init>.miTemp.name: " +
                                    miTemp.getName() );
                    } else {
                        String sActionCommand = (String)oNext;
                        String sMenuItem = sActionCommand;
                        
                        int indexMnemonic = sMenuItem.indexOf( '&' );
                        char cMnemonic = '&';
                        if ( indexMnemonic != -1 && 
                             indexMnemonic < sMenuItem.length() ) 
                        {
                            cMnemonic = sMenuItem.charAt( indexMnemonic + 1 );
                            sMenuItem = Util.replace( sMenuItem, "&", "" );
                        }
                        Util.debug(sMenuItem);
                        if (sMenuItem.charAt(0) == '-') {
                            Util.debug("---");
                            addSeparator();
                        } else {
                            JMenuItem miTemp = null;
                            if (sMenuItem.equals("Save") || sMenuItem.equals("Speichern")) {
                                /*miTemp.setShortcut(new MenuShortcut(KeyEvent.VK_S));
                                  KeyStroke ksCtrlS = KeyStroke.getKeyStroke('c', Event.CTRL_MASK);
                                  miTemp.registerKeyboardAction(pMainJFrame_*/
                                miTemp = new JMenuItem( sMenuItem, 'S' );
                            } else {
                                miTemp = new JMenuItem(sMenuItem);
                            }
                            if ( indexMnemonic != '&' ) {
                                miTemp.setMnemonic( cMnemonic );
                            }
                            miTemp.setActionCommand( sActionCommand );
                            miTemp.setName( miTemp.getText() );
                            // add help action listener?
                            if ( sActionCommand.equals( MainJMenuBar.S_HELP_CONTENTS ) ) 
                            {
                                Util.debug( this, "pMainJFrame_: " + pMainJFrame_ );
                                HelpBroker pHelpBroker = 
                                       _getHelpBroker( pMainJFrame_ );
                                miTemp.addActionListener
                                       ( new CSH.
                                         DisplayHelpFromSource
                                         ( pHelpBroker ) );
                            } else {
                                miTemp.addActionListener(pMainJFrame_);
                            }
                            add(miTemp);
                        }
                    }
                }
            }
        }
        
        /**
         * Remove list in file menu of old files (projects).
         */
        public void removeOldFileItems() {
            if (_oldFilesCount == 0) {
                return;
            }
            for(int i = 0; i < _oldFilesCount + 1; i++) {
                remove(_oldFilesStartIndex);
            }
            _oldFilesCount = 0;
    }
        
    public void insertOldFiles(MainJFrame pMainJFrame_) {
            Vector vOldFileItems = _getOldFileItems(pMainJFrame_.getInit());
            
            for(int i = 0; i < vOldFileItems.size(); i ++) {
                String sItem = (String)vOldFileItems.elementAt(i);
                if (sItem.charAt(0) == '-') {
                    insertSeparator(_oldFilesStartIndex + i);
                } else {
                    JMenuItem miTemp = new JMenuItem(sItem);
                    miTemp.addActionListener(pMainJFrame_);
                    insert(miTemp, _oldFilesStartIndex + i);
                }
            }
    }
    }
}
