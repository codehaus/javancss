package javancss.test;

import java.io.File;

import ccl.util.FileUtil;
import ccl.util.Test;
import ccl.util.Util;
import ccl.xml.XMLUtil;

import javancss.Javancss;
import javancss.XmlFormatter;

/**
 * This test class checks that the xml output feature is
 * working properly.
 *
 * @version $Id$
 * @author  Chr. Clemens Lee
 */
public class XmlFormatterTest extends AbstractTest {
    /**
     * Is it at least possible to properly parse generated xml code?
     */
    private void _checkParsing()
        throws Exception
    {
        Javancss pJavancss = new Javancss( new File( testDir, "Test57.java" ) );
        pJavancss.setXML( true );

        String sXML = XmlFormatter.printStart()
               + pJavancss.printPackageNcss()
               + pJavancss.printObjectNcss()
               + pJavancss.printFunctionNcss()
               + pJavancss.printJavaNcss()
               + XmlFormatter.printEnd();

        File xsltFile = new File( testDir, ".." + File.separator + "xslt" + File.separator + "xmltest.xsl" );
        try
        {
            String sText = XMLUtil.getXML( sXML, xsltFile );
            Assert( sText.equals( "79" ), sText );
        }
        catch( NoClassDefFoundError error )
        {
            Util.print( "skipped: 'xalan.jar' and or 'xerces.jar' library missing." );
        }

        pJavancss = new Javancss( new File( testDir, "Test117.java" ) );
        pJavancss.setXML( true );

        sXML = XmlFormatter.printStart()
               + pJavancss.printPackageNcss()
               + pJavancss.printObjectNcss()
               + pJavancss.printFunctionNcss()
               + pJavancss.printJavaNcss()
               + XmlFormatter.printEnd();
        Assert( Util.isEmpty( sXML ) == false );

        pJavancss = new Javancss( new File( testDir, "Test118.java" ) );
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
        Javancss pJavancss = new Javancss( new File( testDir, "Test32.java" ) );
        pJavancss.setXML( true );

        String sXML = XmlFormatter.printStart()
               + pJavancss.printPackageNcss()
               + pJavancss.printObjectNcss()
               + pJavancss.printFunctionNcss()
               + pJavancss.printJavaNcss()
               + XmlFormatter.printEnd();

        File xsltFile = new File( testDir, ".." + File.separator + "xslt" + File.separator + "javancss2text.xsl" );
        try 
        {
            String sText = XMLUtil.getXML( sXML, xsltFile );
            FileUtil.writeFile( "/tmp/t", sText );
            String sCompare = FileUtil.readFile( new File( testDir, "Output32.txt" ).getAbsolutePath() );
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
        new XmlFormatterTest().main();
    }
}
