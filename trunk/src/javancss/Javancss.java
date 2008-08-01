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

import ccl.util.*;
import java.util.*;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringBufferInputStream;
import java.awt.event.*;
import javancss.test.JavancssTest;

/**
 * While the Java parser class might be the heart of JavaNCSS,
 * this class is the brain. This class controls input and output and
 * invokes the Java parser.
 *
 * @author    Chr. Clemens Lee <clemens@kclee.com>
 *            , recursive feature by Pääkö Hannu
 *            , additional javadoc metrics by Emilio Gongora <emilio@sms.nl>
 *            , and Guillermo Rodriguez <guille@sms.nl>.
 * @version   $Id: Javancss.java,v 1.22 2006/04/16 11:42:19 clemens Exp clemens $
 */
public class Javancss implements Exitable,
                                 JavancssConstants
{
    static final int LEN_NR = 3;
    static final String S_INIT__FILE_CONTENT =
        "[Init]\n" +
        "Author=Chr. Clemens Lee\n" +
        "\n" +
        "[Help]\n"+
        "; Please do not edit the Help section\n"+
        "HelpUsage=@srcfiles.txt | *.java | <stdin>\n" +
        "Options=ncss,package,object,function,all,gui,xml,out,recursive,check\n" +
        "ncss=b,o,Counts the program NCSS (default).\n" +
        "package=b,o,Assembles a statistic on package level.\n" +
        "object=b,o,Counts the object NCSS.\n" +
        "function=b,o,Counts the function NCSS.\n" +
        "all=b,o,The same as '-function -object -package'.\n" +
        "gui=b,o,Opens a gui to present the '-all' output in tabbed panels.\n" +
        "xml=b,o,Output in xml format.\n" +
        "out=s,o,Output file name. By default output goes to standard out.\n"+
        "recursive=b,o,Recurse to subdirs.\n" +
        "check=b,o,Triggers a javancss self test.\n" +
        "\n" +
        "[Colors]\n" +
        "UseSystemColors=true\n";
    
    private boolean _bExit = false;
    private int _ncss = 0;
    private int _loc = 0;
    private JavaParser _pJavaParser = null;
    private Vector _vJavaSourceFiles = new Vector();
    private String _sErrorMessage = null;
    private Throwable _thrwError = null;
    private Vector _vFunctionMetrics = new Vector();
    private Vector _vObjectMetrics = new Vector();
    private Vector _vPackageMetrics = null;
    private Vector _vImports = null;
    private Hashtable _htPackages = null;
    private Hashtable _htProcessedAtFiles = new Hashtable();
    private Object[] _aoPackage = null;

    /**
     * Just used for parseImports.
     */
    private String _sJavaSourceFileName = null;

    private DataInputStream createInputStream( String sSourceFileName_ )
    {
        DataInputStream disSource = null;

        try {
            disSource = new DataInputStream
                   (new FileInputStream(sSourceFileName_));
        } catch(IOException pIOException) {
            if ( Util.isEmpty( _sErrorMessage ) )
            {
                _sErrorMessage = "";
            }
            else
            {
                _sErrorMessage += "\n";
            }
            _sErrorMessage += "File not found: " + sSourceFileName_;
            _thrwError = pIOException;

            return null;
        }

        return disSource;
    }

    private void _measureSource(String sSourceFileName_)
        throws IOException,
               ParseException,
               TokenMgrError
    {
        // take user.dir property in account
        sSourceFileName_ = FileUtil.normalizeFileName( sSourceFileName_ );

        DataInputStream disSource = null;

        // opens the file
        try 
        {
            disSource = new DataInputStream
                   (new FileInputStream(sSourceFileName_));
        }
        catch(IOException pIOException) 
        {
            if ( Util.isEmpty( _sErrorMessage ) )
            {
                _sErrorMessage = "";
            }
            else
            {
                _sErrorMessage += "\n";
            }
            _sErrorMessage += "File not found: " + sSourceFileName_;
            _thrwError = pIOException;

            throw pIOException;
        }

        String sTempErrorMessage = _sErrorMessage;
        try {
            // the same method but with a DataInputSream
            _measureSource(disSource);
        } catch(ParseException pParseException) {
            if (sTempErrorMessage == null) {
                sTempErrorMessage = "";
            }
            sTempErrorMessage += "ParseException in " + sSourceFileName_ + 
                   "\nLast useful checkpoint: \"" + _pJavaParser.getLastFunction() + "\"\n";
            sTempErrorMessage += pParseException.getMessage() + "\n";
            
            _sErrorMessage = sTempErrorMessage;
            _thrwError = pParseException;
            
            throw pParseException;
        } catch(TokenMgrError pTokenMgrError) {
            if (sTempErrorMessage == null) {
                sTempErrorMessage = "";
            }
            sTempErrorMessage += "TokenMgrError in " + sSourceFileName_ + 
                   "\n" + pTokenMgrError.getMessage() + "\n";
            _sErrorMessage = sTempErrorMessage;
            _thrwError = pTokenMgrError;
            
            throw pTokenMgrError;
        }
    }

    private void _measureSource(DataInputStream disSource_)
        throws IOException,
               ParseException,
               TokenMgrError
    {
        try {
            // create a parser object
            _pJavaParser = new JavaParser(disSource_);
            // execute the parser
            _pJavaParser.CompilationUnit();
            Util.debug
                   ( "Javancss._measureSource(DataInputStream).SUCCESSFULLY_PARSED" );
            _ncss += _pJavaParser.getNcss();       // increment the ncss
            _loc  += _pJavaParser.getLOC();        // and loc
            // add new data to global vector
            _vFunctionMetrics = Util.concat(_vFunctionMetrics,
                                            _pJavaParser.getFunction());
            _vObjectMetrics = Util.concat(_vObjectMetrics,
                                          _pJavaParser.getObject());
            Hashtable htNewPackages = _pJavaParser.getPackage();
            /*Vector vNewPackages = new Vector();*/
            for(Enumeration ePackages = htNewPackages.keys();
                ePackages.hasMoreElements(); )
            {
                String sPackage = (String)ePackages.nextElement();
                PackageMetric pckmNext = (PackageMetric)htNewPackages.
                       get(sPackage);
                pckmNext.name = sPackage;
                PackageMetric pckmPrevious =
                       (PackageMetric)_htPackages.get
                       (sPackage);
                pckmNext.add(pckmPrevious);
                _htPackages.put(sPackage, pckmNext);
            }
        } catch(ParseException pParseException) {
            if (_sErrorMessage == null) {
                _sErrorMessage = "";
            }
            _sErrorMessage += "ParseException in STDIN";
            if (_pJavaParser != null) {
                _sErrorMessage += "\nLast useful checkpoint: \"" + _pJavaParser.getLastFunction() + "\"\n";
            }
            _sErrorMessage += pParseException.getMessage() + "\n";
            _thrwError = pParseException;
            
            throw pParseException;
        } catch(TokenMgrError pTokenMgrError) {
            if (_sErrorMessage == null) {
                _sErrorMessage = "";
            }
            _sErrorMessage += "TokenMgrError in STDIN\n";
            _sErrorMessage += pTokenMgrError.getMessage() + "\n";
            _thrwError = pTokenMgrError;
            
            throw pTokenMgrError;
        }
    }

    private void _measureFiles(Vector vJavaSourceFiles_)
        throws IOException,
               ParseException,
               TokenMgrError
    {
        // for each file
        for(Enumeration e = vJavaSourceFiles_.elements(); e.hasMoreElements(); ) 
        {
            String sJavaFileName = (String)e.nextElement();

            // if the file specifies other files...
            if (sJavaFileName.charAt(0) == '@') 
            {
                if (sJavaFileName.length() > 1) 
                {
                    String sFileName = sJavaFileName.substring(1);
                    sFileName = FileUtil.normalizeFileName( sFileName );
                    if (_htProcessedAtFiles.get(sFileName) != null) 
                    {
                        continue;
                    }
                    _htProcessedAtFiles.put( sFileName, Util.getConstantObject() );
                    String sJavaSourceFileNames = null;
                    try 
                    {
                        sJavaSourceFileNames = FileUtil.readFile(sFileName);
                    }
                    catch(IOException pIOException) 
                    {
                        _sErrorMessage = "File Read Error: " + sFileName;
                        _thrwError = pIOException;
                        
                        throw pIOException;
                    }
                    Vector vTheseJavaSourceFiles =
                           Util.stringToLines(sJavaSourceFileNames);
                    _measureFiles(vTheseJavaSourceFiles);
                }
            } 
            else 
            {
                try 
                {
                    _measureSource( sJavaFileName );
                } catch( Throwable pThrowable ) 
                {
                    // hmm, do nothing? Use getLastError() or so to check for details.
                }
            }
        }
    }

    /**
     * If arguments were provided, they are used, otherwise
     * the input stream is used.
     */
    private void _measureRoot(InputStream pInputStream_)
        throws IOException,
               ParseException,
               TokenMgrError
    {
        _htPackages = new Hashtable();
        
        // either there are argument files, or stdin is used
        if (_vJavaSourceFiles.size() == 0) {
            DataInputStream disJava = new java.io.DataInputStream(pInputStream_);
            _measureSource(disJava);
        } else {
            // the collection of files get measured
            _measureFiles(_vJavaSourceFiles);
        }
        
        _vPackageMetrics = new Vector();
        for(Enumeration ePackages = _htPackages.keys();
            ePackages.hasMoreElements(); )
        {
            String sPackage = (String)ePackages.nextElement();
            PackageMetric pckmNext = (PackageMetric)_htPackages.
                   get(sPackage);
            _vPackageMetrics.addElement(pckmNext);
        }
        _vPackageMetrics = Util.sort(_vPackageMetrics);
    }

    public Vector getImports() {
        return _vImports;
    }

    /**
     * Return info about package statement.
     * First element has name of package,
     * then begin of line, etc.
     */
    public Object[] getPackage() {
        return _aoPackage;
    }

    /**
     * The same as getFunctionMetrics?!
     */
    public Vector getFunctions() {
        return _vFunctionMetrics;
    }

    public String printObjectNcss() {
	return getFormatter().printObjectNcss();
    }

    public String printFunctionNcss() {
	return getFormatter().printFunctionNcss();
    }

    public String printPackageNcss() {
	return getFormatter().printPackageNcss();
    }

    public String printJavaNcss() {
	return getFormatter().printJavaNcss();
    }

    public Javancss(Vector vJavaSourceFiles_) {
        _vJavaSourceFiles = vJavaSourceFiles_;
        try {
            _measureRoot(System.in);
        } catch(Exception e) {
        } catch(TokenMgrError pError) {
        }
    }

    public Javancss(String sJavaSourceFile_) {
        Util.debug( "Javancss.<init>(String).sJavaSourceFile_: " + sJavaSourceFile_ );
        _sErrorMessage = null;
        _vJavaSourceFiles = new Vector();
        _vJavaSourceFiles.addElement(sJavaSourceFile_);
        try {
            _measureRoot(System.in);
        } catch(Exception e) {
            Util.debug( "Javancss.<init>(String).e: " + e );
        } catch(TokenMgrError pError) {
            Util.debug( "Javancss.<init>(String).pError: " + pError );
        }
    }

    /**
     * Only way to create object that does not immediately
     * start to parse.
     */
    public Javancss() {
        super();

        _sErrorMessage = null;
        _thrwError = null;
    }

    public boolean parseImports() {
        if ( Util.isEmpty( _sJavaSourceFileName ) ) {
            Util.debug( "Javancss.parseImports().NO_FILE" );

            return true;
        }
        DataInputStream disSource = createInputStream
               ( _sJavaSourceFileName );
        if ( disSource == null ) {
            Util.debug( "Javancss.parseImports().NO_DIS" );

            return true;
        }

        try {
            Util.debug( "Javancss.parseImports().START_PARSING" );
            _pJavaParser = new JavaParser(disSource);
            _pJavaParser.ImportUnit();
            _vImports = _pJavaParser.getImports();
            _aoPackage = _pJavaParser.getPackageObjects();
            Util.debug( "Javancss.parseImports().END_PARSING" );
        } catch(ParseException pParseException) {
            Util.debug( "Javancss.parseImports().PARSE_EXCEPTION" );
            if (_sErrorMessage == null) {
                _sErrorMessage = "";
            }
            _sErrorMessage += "ParseException in STDIN";
            if (_pJavaParser != null) {
                _sErrorMessage += "\nLast useful checkpoint: \"" + _pJavaParser.getLastFunction() + "\"\n";
            }
            _sErrorMessage += pParseException.getMessage() + "\n";
            _thrwError = pParseException;

            return true;
        } catch(TokenMgrError pTokenMgrError) {
            Util.debug( "Javancss.parseImports().TOKEN_ERROR" );
            if (_sErrorMessage == null) {
                _sErrorMessage = "";
            }
            _sErrorMessage += "TokenMgrError in STDIN\n";
            _sErrorMessage += pTokenMgrError.getMessage() + "\n";
            _thrwError = pTokenMgrError;

            return true;
        }

        return false;
    }

    public void setSourceFile( String sJavaSourceFile_ ) {
        _sJavaSourceFileName = sJavaSourceFile_;
        _vJavaSourceFiles = new Vector();
        _vJavaSourceFiles.addElement(sJavaSourceFile_);
    }

    public Javancss(StringBufferInputStream pStringBufferInputStream_) {
        try {
            _measureRoot(pStringBufferInputStream_);
        } catch(Exception e) {
        } catch(TokenMgrError pError) {
        }
    }

    private void _addJavaFiles( File file, Vector v )
    {
        String sFile = FileUtil.normalizeFileName( file.getPath() );
        String[] files = new File( sFile ).list();
        if( files == null 
            || files.length == 0 )
        {
            return;
        }
	   
        for( int i = 0; i < files.length; i++ )
        {
            String newFileName = FileUtil.concatPath( sFile
                                                      , files[ i ] );
            File newFile = new File( newFileName );
            if( newFile.isDirectory() )
            {
                //Recurse!!!
                _addJavaFiles( newFile, v );
            }
            else
            {
                if( newFile.getAbsolutePath().endsWith( ".java" ) )
                {
                    v.addElement( newFile.getAbsolutePath() );
                }
            }
        }
    }

    private void _removeDirs( Vector vDirs )
    {
        if ( Util.isDebug() )
        {
            Util.debug( this, "_removeDirs(..).vDirs: " + Util.toString( vDirs ) );
        }
        // Do it in reverse order, or we will have a problem 
        // when removing elements.
        for( int i = vDirs.size() - 1; i >= 0; i-- )
        {
            String sFile = FileUtil.normalizeFileName( (String)vDirs.elementAt( i ) );
            Util.debug( this, "_removeDirs(..).sFile: " + sFile );
            if( FileUtil.existsDir( sFile ) )
            {
                vDirs.removeElementAt( i );
                Util.debug( this, "_removeDirs(..).removed: " + sFile );
            }
        }
    }

    private Init _pInit = null;

    /**
     * This is the constructor used in the main routine in
     * javancss.Main.
     * Other constructors might be helpful to use Javancss out
     * of other programs.
     */
    public Javancss(String[] asArgs_, String sRcsHeader_) {
        _pInit = new Init(this, asArgs_, sRcsHeader_, S_INIT__FILE_CONTENT);
        if (_bExit) {
            return;
        }
        Hashtable htOptions = _pInit.getOptions();

        if ( htOptions.get( "check" ) != null ) {
            JavancssTest pTest = new JavancssTest();
            pTest.setTestDir( FileUtil.concatPath( _pInit.getApplicationPath()
                                                   , "test" )                 );
            pTest.setVerbose( true );
            pTest.setTiming ( true );
            pTest.run();
            pTest.printResult();

            return;
        }

        // the arguments (the files) to be processed
        _vJavaSourceFiles = _pInit.getArguments();

        if ( Util.isDebug() )
        {
            Util.debug( "_vJavaSourceFiles: " + Util.toString( _vJavaSourceFiles ) );
        }
        if ( htOptions.get( "recursive" ) != null )
        {
            // If no files then add current directory!
            if ( _vJavaSourceFiles.size() == 0 )
            {
                _vJavaSourceFiles.addElement( "." );
            }
           
            Vector newFiles = new Vector();
            Enumeration enum = _vJavaSourceFiles.elements();
            while( enum.hasMoreElements() ) 
            {
                String fileName = FileUtil.normalizeFileName( (String)enum.nextElement() );
                File   file = new File( fileName );
                if( file.isDirectory() ) 
                {
                    _addJavaFiles( file, newFiles );
                }
            }
            if( newFiles.size() != 0 )
            {
                for( int i = 0; i < newFiles.size(); i++ )
                {
                    _vJavaSourceFiles.add( newFiles.elementAt( i ) );
                }
            }
           
            _removeDirs( _vJavaSourceFiles );
        }

        if ( Util.isDebug() )
        {
            Util.debug( "_vJavaSourceFiles: " + Util.toString( _vJavaSourceFiles ) );
        }

        if ( htOptions.get( "gui" ) != null ) 
        {
            final JavancssFrame pJavancssFrame = new JavancssFrame(_pInit);
            /*final Thread pThread = Thread.currentThread();*/
            pJavancssFrame.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent e_) {
                        Util.debug("JavancssAll.run().WindowAdapter.windowClosing().1");
                        pJavancssFrame.setVisible(false);
                        pJavancssFrame.dispose();
                    }
                });
            pJavancssFrame.setVisible(true);

            try {
                _measureRoot(System.in);
            } catch(Throwable pThrowable) {
                // shouldn't we print something here?
            }

            pJavancssFrame.showJavancss(this);
            pJavancssFrame.setSelectedTab(JavancssFrame.S_PACKAGES);
            pJavancssFrame.run();

            return;
        }

        // this initiates the measurement
        try
        {
            _measureRoot( System.in );
        }
        catch(Throwable pThrowable) 
        {
        }
        if ( getLastErrorMessage() != null ) 
        {
            Util.printlnErr( getLastErrorMessage() + "\n" );
            if ( getNcss() <= 0 ) 
            {
                return;
            }
        }

        boolean bNoNCSS = false;

        String sOutputFile = (String)htOptions.get( "out" );
        PrintStream ps = System.out;
        if (sOutputFile != null)
        {
            try 
            {
                ps = new PrintStream( new BufferedOutputStream( new FileOutputStream( FileUtil.normalizeFileName( sOutputFile ) ) ) );
            } catch ( Exception exception ) {
                Util.printlnErr( "Error opening output file '" 
                                 + sOutputFile 
                                 + "': " + exception.getMessage() );
                
                ps = System.out;
                sOutputFile = null;
            }
        }

	if ( useXML() )
	{
	    ps.print( XmlFormatter.printStart() );
	}

        if (htOptions.get( "package" ) != null ||
            htOptions.get( "all" ) != null)
        {
            ps.print( printPackageNcss() );
            bNoNCSS = true;
        }
        if (htOptions.get( "object" ) != null ||
            htOptions.get( "all" ) != null)
        {
            if ( bNoNCSS )
            {
                ps.println();
            }
            ps.print( printObjectNcss() );
            bNoNCSS = true;
        }
        if (htOptions.get( "function" ) != null ||
            htOptions.get( "all" ) != null)
        {
            if ( bNoNCSS )
            {
                ps.println();
            }
            ps.print( printFunctionNcss() );
            bNoNCSS = true;
        }
        if (!bNoNCSS) {
            ps.print( printJavaNcss() );
        }

	if ( useXML() )
	{
	    if ( !bNoNCSS )
            {
		ps.print( printJavaNcss() );
            }	    
	    ps.println( "</javancss>" );
	}

        if ( sOutputFile != null )
        {
            ps.close();
        } else
        {
            // standard out is used
            //ps.flush();
        }
        ps = null;
    }

    public int getNcss() {
        return _ncss;
    }

    public int getLOC() {
        return _loc;
    }

    // added by SMS
    public int getJvdc() {
        return _pJavaParser.getJvdc();
    }

    /**
     * JDCL stands for javadoc coment lines (while jvdc stands
     * for number of javadoc comments).
     */
    public int getJdcl() {
        return JavaParserTokenManager._iFormalComments;
    }
    
    public int getSl() {
        return JavaParserTokenManager._iSingleComments;
    }
    
    public int getMl() {
        return JavaParserTokenManager._iMultiComments;
    }
    //

    public Vector getFunctionMetrics() {
        return(_vFunctionMetrics);
    }

    public Vector getObjectMetrics() {
        return(_vObjectMetrics);
    }

    /**
     * Returns list of packages in the form
     * PackageMetric objects.
     */
    public Vector getPackageMetrics() {
        return(_vPackageMetrics);
    }

    public String getLastErrorMessage() {
        if (_sErrorMessage == null) {
            return null;
        }
        return(new String(_sErrorMessage));
    }

    public Throwable getLastError() {
        return _thrwError;
    }

    public void setExit() {
        _bExit = true;
    }

    private boolean _bXML = false;

    public void setXML( boolean bXML )
    {
        _bXML = bXML;
    }

    public boolean useXML()
    {
	return _bXML
               || (_pInit != null && _pInit.getOptions().get( "xml" ) != null );
    }

    public Formatter getFormatter()
    {
	if ( useXML() )
	{
	    return new XmlFormatter( this );
	}

	return new AsciiFormatter( this );
    }
}
