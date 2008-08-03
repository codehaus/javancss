package javancss.test;

import ccl.util.FileUtil;
import ccl.util.Test;
import ccl.util.Util;
import ccl.xml.XMLUtil;
import java.io.File;
import javancss.Javancss;
import javancss.XmlFormatter;

/**
 * This test class checks that the xml output feature is
 * working properly.
 *
 * @version $Id: XmlFormatterTest.java,v 1.4 2006/06/12 21:37:41 clemens Exp clemens $
 * @author  Chr. Clemens Lee
 */
public class XmlFormatterTest extends Test {
    /**
     * Is it at least possible to properly parse generated xml code?
     */
    private void _checkParsing()
        throws Exception
    {
	Javancss pJavancss = new Javancss( FileUtil.concatPath( _sTestDir, "Test57.java" ) );
        pJavancss.setXML( true );

        String sXML = XmlFormatter.printStart()
               + pJavancss.printPackageNcss()
               + pJavancss.printObjectNcss()
               + pJavancss.printFunctionNcss()
               + pJavancss.printJavaNcss()
               + XmlFormatter.printEnd();

        String sXSLTFile = FileUtil.concatPath( _sTestDir
                                                , ".."
                                                  + File.separator  
                                                  + "xslt"
                                                  + File.separator
                                                + "xmltest.xsl" );
        try
        {
            String sText = XMLUtil.getXML( sXML
                                           , new File( sXSLTFile ) );
            Assert( sText.equals( "79" ), sText );
        }
        catch( NoClassDefFoundError error )
        {
            Util.print( "skipped: 'xalan.jar' and or 'xerces.jar' library missing." );
        }

	pJavancss = new Javancss( FileUtil.concatPath( _sTestDir, "Test117.java" ) );
        pJavancss.setXML( true );

        sXML = XmlFormatter.printStart()
               + pJavancss.printPackageNcss()
               + pJavancss.printObjectNcss()
               + pJavancss.printFunctionNcss()
               + pJavancss.printJavaNcss()
               + XmlFormatter.printEnd();
        Assert( Util.isEmpty( sXML ) == false );

	pJavancss = new Javancss( FileUtil.concatPath( _sTestDir, "Test118.java" ) );
        pJavancss.setXML( true );

        sXML = XmlFormatter.printStart()
               + pJavancss.printPackageNcss()
               + pJavancss.printObjectNcss()
               + pJavancss.printFunctionNcss()
               + pJavancss.printJavaNcss()
               + XmlFormatter.printEnd();
        Assert( Util.isEmpty( sXML ) == false );
    }

    /**
     * Is the transformed XML output identical to the standard ASCI output?
     */
    private void _checkXML2Text()
        throws Exception
    {
	Javancss pJavancss = new Javancss( FileUtil.concatPath( _sTestDir, "Test32.java" ) );
        pJavancss.setXML( true );

        String sXML = XmlFormatter.printStart()
               + pJavancss.printPackageNcss()
               + pJavancss.printObjectNcss()
               + pJavancss.printFunctionNcss()
               + pJavancss.printJavaNcss()
               + XmlFormatter.printEnd();

        String sXSLTFile = FileUtil.concatPath( _sTestDir
                                                , ".."
                                                  + File.separator  
                                                  + "xslt"
                                                  + File.separator  
                                                + "javancss2text.xsl" );
        try 
        {
            String sText = XMLUtil.getXML( sXML
                                           , new File( sXSLTFile ) );
            FileUtil.writeFile( "/tmp/t", sText );
            String sCompare = FileUtil.readFile( FileUtil.concatPath( _sTestDir
                                                                      , "Output32.txt" ) );
            Assert( sText.equals( sCompare ), sText );
        } 
        catch( NoClassDefFoundError error )
        {
            Util.print( "skipped: 'xalan.jar' and or 'xerces.jar' library missing." );
        }
    }

    public XmlFormatterTest() 
    {
        super();
    }

    public XmlFormatterTest( Test pTest_ ) 
    {
        super( pTest_ );
    }

    /**
     * Test code goes here.
     */
    protected void _doIt()
        throws Exception
    {
        _checkParsing ();
        _checkXML2Text();
    }

    public static void main( String[] asArg_ ) 
    {
        Test pTest = (Test)(new XmlFormatterTest());
        pTest.setVerbose( true );
        pTest.run();
        pTest.printResult();

        System.exit( 0 );
    }

    private String _sTestDir = null;

    public void setTestDir( String sTestDir_ ) {
        _sTestDir = sTestDir_;
    }
}
