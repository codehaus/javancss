/*
Copyright (C) 2000 Chr. Clemens Lee <clemens@kclee.com>.

This file is part of JavaNCSS 
(http://www.kclee.com/clemens/java/javancss/).

JavaNCSS is free software; you can redistribute it and/or modify it
under the terms of the GNU General Public License as published by the
Free Software Foundation; either version 2, or (at your option) any
later version.

JavaNCSS is distributed in the hope that it will be useful, but WITHOUT
ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
for more details.

You should have received a copy of the GNU General Public License
along with JavaNCSS; see the file COPYING.  If not, write to
the Free Software Foundation, Inc., 59 Temple Place - Suite 330,
Boston, MA 02111-1307, USA.  */

package javancss;

import ccl.swing.AboutDialog;
import ccl.swing.AnimationPanel;
import ccl.swing.AutoGridBagLayout;
import ccl.swing.MainJFrame;
import ccl.swing.SwingUtil;
import ccl.util.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;
import java.net.*;

import javax.swing.*;
import javax.swing.border.*;

/**
 * Main class used to start JavaNCSS in GUI mode from other
 * java applications. To start JavaNCSS from the command line,
 * gui mode or not, class 'Main' is used.
 *
 * @author  <a href="http://www.kclee.com/clemens/">Chr. Clemens Lee</a> (<a href="mailto:clemens@kclee.com"><i>clemens@kclee.com</i></a>)
 * @version $Id: JavancssFrame.java,v 1.12 2006/04/16 11:42:20 clemens Exp clemens $
 */
public class JavancssFrame extends MainJFrame {
    public static final String S_PACKAGES = "Packages";
    public static final String S_CLASSES = "Classes";
    public static final String S_METHODS = "Methods";
    
    private static final String S_MN_F_SAVE = "Save";
    
    private int _oldThreadPriority = -1; 
    
    private AnimationPanel _pAnimationPanel = null;
    
    private JTextArea _txtPackage;
    private JTextArea _txtObject;
    private JTextArea _txtFunction;
    private JTextArea _txtError;
    
    private JTabbedPane _pTabbedPane = null;
    
    private Font pFont = new Font("Monospaced", Font.PLAIN, 12);
    
    private boolean _bNoError = true;
    
    private String _sProjectName = null;
    private String _sProjectPath = null;
    
    private Init _pInit = null;

    public void save() {
        String sFullProjectName = FileUtil.concatPath
               (_sProjectPath, _sProjectName.toLowerCase());
        String sPackagesFullFileName = sFullProjectName +
               ".packages.txt";
        String sClassesFullFileName = sFullProjectName +
               ".classes.txt";
        String sMethodsFullFileName = sFullProjectName +
               ".methods.txt";

        String sSuccessMessage = "Data appended successfully to the following files:";
        
        try {
            FileUtil.appendFile(sPackagesFullFileName,
                                _txtPackage.getText());
            sSuccessMessage += "\n" + sPackagesFullFileName;
        } catch(Exception ePackages) {
            SwingUtil.showMessage(this, "Error: could not append to file '" +
                                sPackagesFullFileName + "'.\n" + ePackages);
        }
        
        try {
            FileUtil.appendFile(sClassesFullFileName,
                                _txtObject.getText());
			sSuccessMessage += "\n" + sClassesFullFileName;
        } catch(Exception eClasses) {
            SwingUtil.showMessage(this, "Error: could not append to file '" +
                                sClassesFullFileName + "'.\n" + eClasses);
        }
        
        try {
            FileUtil.appendFile(sMethodsFullFileName,
                                _txtFunction.getText());
            sSuccessMessage += "\n" + sMethodsFullFileName;
        } catch(Exception eMethods) {
            SwingUtil.showMessage(this, "Error: could not append to file '" +
                                sMethodsFullFileName + "'.\n" + eMethods);
        }
        
        SwingUtil.showMessage(this, sSuccessMessage);
    }

    private void _setMenuBar() {
        Vector vMenus = new Vector();
        
        Vector vFileMenu = new Vector();
        Vector vHelpMenu = new Vector();
        
        vFileMenu.addElement("File");
        vFileMenu.addElement(S_MN_F_SAVE);
        vFileMenu.addElement("Exit");

        vHelpMenu.addElement("Help");
        vHelpMenu.addElement("&Contents...");
        vHelpMenu.addElement("---");
        vHelpMenu.addElement("About...");

        vMenus.addElement(vFileMenu);
        vMenus.addElement(vHelpMenu);
		
        setMenuBar(vMenus);
    }

    /**
     * Returns init object provided with constructor.
     */
    public Init getInit() {
        return _pInit;
    }

    public JavancssFrame(Init pInit_) {
        super( "JavaNCSS: " + pInit_.getFileName() );
        
        _pInit = pInit_;
        getInit().setAuthor( "Chr. Clemens Lee" );

        super.setBackground( _pInit.getBackground() );

        _sProjectName = pInit_.getFileName();
        _sProjectPath = pInit_.getFilePath();
        if (Util.isEmpty(_sProjectName)) {
            _sProjectName = pInit_.getApplicationName();
            _sProjectPath = pInit_.getApplicationPath();
        }
        
        _setMenuBar();
        
        _bAboutSelected = false;
        
        AutoGridBagLayout pAutoGridBagLayout = new AutoGridBagLayout();
        
        getContentPane().setLayout(pAutoGridBagLayout);
        
        Image pImage = Toolkit.getDefaultToolkit().
               getImage( SwingUtil.createCCLBorder().getClass().getResource
                         ( "anim_recycle_brown.gif" ) );
        _pAnimationPanel = new AnimationPanel( pImage, 350 );
        
        JPanel pPanel = new JPanel();
        pPanel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
        pPanel.add(_pAnimationPanel, BorderLayout.CENTER);
        
        getContentPane().add(pPanel);
        
        
        pack();
        setSize(640, 480);
        SwingUtil.centerComponent(this);
    }
    
    public void showJavancss(Javancss pJavancss_) {
        _bStop = false;
        _bSave = false;
        if (_oldThreadPriority != -1) {
            Thread.currentThread().setPriority(_oldThreadPriority);
            _pAnimationPanel.stop();
        }
        getContentPane().removeAll();
        getContentPane().setLayout(new BorderLayout());
        _bNoError = true;
        if (pJavancss_.getLastErrorMessage() != null && pJavancss_.getNcss() <= 0) {
            _bNoError = false;
            JTextArea txtError = new JTextArea();
            String sError = "Error in Javancss: " +
                   pJavancss_.getLastErrorMessage();
            txtError.setText(sError);
            JScrollPane jspError = new JScrollPane(txtError);
            getContentPane().add(jspError, BorderLayout.CENTER);
        } else {
            Util.debug("JavancssFrame.showJavancss(..).NOERROR");
            JPanel pPanel = new JPanel(true);
            pPanel.setLayout(new BorderLayout());
            _pTabbedPane = new JTabbedPane();
            _pTabbedPane.setDoubleBuffered(true);
            
            _txtPackage = new JTextArea();
            _txtPackage.setFont(pFont);
            JScrollPane jspPackage = new JScrollPane(_txtPackage);
            int inset = 5;
            jspPackage.setBorder( BorderFactory.
                                  createEmptyBorder
                                  ( inset, inset, inset, inset ) ); 
            _pTabbedPane.addTab("Packages", null, jspPackage);
            
            _txtObject = new JTextArea();
            _txtObject.setFont(pFont);
            JScrollPane jspObject = new JScrollPane(_txtObject);
            jspObject.setBorder( BorderFactory.
                                  createEmptyBorder
                                  ( inset, inset, inset, inset ) ); 
            _pTabbedPane.addTab("Classes", null, jspObject);
            
            _txtFunction = new JTextArea();
            _txtFunction.setFont(pFont);
            JScrollPane jspFunction = new JScrollPane(_txtFunction);
            jspFunction.setBorder( BorderFactory.
                                  createEmptyBorder
                                  ( inset, inset, inset, inset ) ); 
            _pTabbedPane.addTab("Methods", null, jspFunction);
            
            // date and time
            String sTimeZoneID = System.getProperty("user.timezone");
            if (sTimeZoneID.equals("CET")) {
                sTimeZoneID = "ECT";
            }
            TimeZone pTimeZone = TimeZone.getTimeZone(sTimeZoneID);
            Util.debug("JavancssFrame.showJavancss(..).pTimeZone.getID(): " + pTimeZone.getID());
            
            SimpleDateFormat pSimpleDateFormat
                   = new SimpleDateFormat("EEE, MMM dd, yyyy  HH:mm:ss");//"yyyy.mm.dd e 'at' hh:mm:ss a z");
            pSimpleDateFormat.setTimeZone(pTimeZone);
            String sDate = pSimpleDateFormat.format(new Date()) + " " + pTimeZone.getID();
            
            _txtPackage.setText(sDate + "\n\n" + pJavancss_.printPackageNcss());
            _txtObject.setText(sDate + "\n\n" + pJavancss_.printObjectNcss());
            _txtFunction.setText(sDate + "\n\n" + pJavancss_.printFunctionNcss());
            
            if (pJavancss_.getLastErrorMessage() != null) {
                _txtError = new JTextArea();
                String sError = "Errors in Javancss:\n\n" +
                       pJavancss_.getLastErrorMessage();
                _txtError.setText(sError);
                JScrollPane jspError = new JScrollPane(_txtError);
                jspError.setBorder( BorderFactory.
                                  createEmptyBorder
                                  ( inset, inset, inset, inset ) ); 
                getContentPane().add(jspError, BorderLayout.CENTER);
                _pTabbedPane.addTab("Errors", null, jspError);
            }
            
            pPanel.add(_pTabbedPane, BorderLayout.CENTER);
            getContentPane().add(pPanel, BorderLayout.CENTER);
        }
        
        validate();
        repaint();
    }

    private boolean _bStop = false;
    private boolean _bSave = false;

    public void run() {
        _bSave = false;
        while(!_bStop) {
            if (_bSave) {
                save();
                _bSave = false;
            }
            
            if (isExitSet()) {
                exit();
                _bStop = true;
                break;
            }
            
            if (_bAboutSelected) {
                _bAboutSelected = false;
                AboutDialog dlgAbout = new AboutDialog
                    ( this,
                      getInit().getAuthor(),
                      javancss.Main.S_RCS_HEADER );
                dlgAbout.dispose();
                requestFocus();
            }
            
            try {
                Thread.currentThread().sleep(500);
            } catch (InterruptedException e) {
            }
        }
    }

	public void setVisible(boolean bVisible_) {
		if (bVisible_) {
			_oldThreadPriority = Thread.currentThread().getPriority();
			_pAnimationPanel.start();
			Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
		} else {
			_pAnimationPanel.stop();
		}

		super.setVisible(bVisible_);
	}

	public void setSelectedTab(String sTab_) {
		Util.panicIf(Util.isEmpty(sTab_));

		if (!_bNoError) {
			return;
		}
		if (sTab_.equals(S_METHODS)) {
			/*_pTabbedPane.setSelectedComponent(_txtFunction);*/
			_pTabbedPane.setSelectedIndex(2);
		} else if (sTab_.equals(S_CLASSES)) {
			/*_pTabbedPane.setSelectedComponent(_txtObject);*/
			_pTabbedPane.setSelectedIndex(1);
		} else {
			/*_pTabbedPane.setSelectedComponent(_txtPackage);*/
			_pTabbedPane.setSelectedIndex(0);
		}
	}

    private boolean _bAboutSelected = false;

    public void actionPerformed(ActionEvent pActionEvent_) {
        Util.debug("JavancssFrame.actionPerformed(..).1");
        Object oSource = pActionEvent_.getSource();
        if (oSource instanceof JMenuItem) {
            String sMenuItem = ((JMenuItem)oSource).getText();
            if (sMenuItem.equals("Beenden") || sMenuItem.equals("Exit")) {
                processWindowEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            } else if (sMenuItem.equals(S_MN_F_SAVE)) {
                _bSave = true;
            } else if (sMenuItem.equals("Info...") || sMenuItem.equals("About...") ||
                       sMenuItem.equals("Info") || sMenuItem.equals("About"))
            {
                _bAboutSelected = true;
            } else if (sMenuItem.equals("Inhalt...") || sMenuItem.equals("Contents...") ||
                       sMenuItem.equals("Inhalt") || sMenuItem.equals("Contents"))
            {
                String sStartURL = FileUtil.concatPath(FileUtil.getPackagePath("javancss"),
                                                       S_DOC_DIR) + File.separator +
                       "index.html";
                if (Util.isEmpty(sStartURL)) {
                    return;
                }
                sStartURL = sStartURL.replace('\\', '/');
                if (sStartURL.charAt(0) != '/') {
                    sStartURL = "/" + sStartURL;
                }
                sStartURL = "file:" + sStartURL;
                Util.debug("JavancssFrame.actionPerformed(): sStartURL: " + sStartURL);
                try {
                    URL urlHelpDocument = new URL(sStartURL);
                    //HtmlViewer pHtmlViewer = new HtmlViewer(urlHelpDocument);
                } catch(Exception pException) {
                    Util.debug("JavancssFrame.actionPerformed(..).pException: " + pException);
                }
            }
        }
    }
}
