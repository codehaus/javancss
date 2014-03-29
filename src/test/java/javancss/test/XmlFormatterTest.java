package javancss.test;

import java.io.File;
import java.io.StringWriter;

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
    public void testParsing()
        throws Exception
    {
        Javancss pJavancss = new Javancss( getTestFile( 57 ) );
        pJavancss.setXML( true );

        StringWriter sw = new StringWriter();

        XmlFormatter.printStart( sw );
        pJavancss.printPackageNcss( sw );
        pJavancss.printObjectNcss( sw );
        pJavancss.printFunctionNcss( sw );
        pJavancss.printJavaNcss( sw );
        XmlFormatter.printEnd( sw );

        String sXML = sw.toString(); 

        try
        {
            String sText = XMLUtil.getXML( sXML, getXslFile( "xmltest.xsl" ) );
            Assert( sText.equals( "79" ), sText );
        }
        catch( NoClassDefFoundError error )
        {
            Util.print( "skipped: 'xalan.jar' and or 'xerces.jar' library missing." );
        }

        pJavancss = new Javancss( getTestFile( 117 ) );
        pJavancss.setXML( true );

        XmlFormatter.printStart( sw );
        pJavancss.printPackageNcss( sw );
        pJavancss.printObjectNcss( sw );
        pJavancss.printFunctionNcss( sw );
        pJavancss.printJavaNcss( sw );
        XmlFormatter.printEnd( sw );

        sXML = sw.toString(); 
        Assert( Util.isEmpty( sXML ) == false );

        pJavancss = new Javancss( getTestFile( 118 ) );
        pJavancss.setXML( true );

        XmlFormatter.printStart( sw );
        pJavancss.printPackageNcss( sw );
        pJavancss.printObjectNcss( sw );
        pJavancss.printFunctionNcss( sw );
        pJavancss.printJavaNcss( sw );
        XmlFormatter.printEnd( sw );

        sXML = sw.toString(); 
        Assert( Util.isEmpty( sXML ) == false );
    }

    /**
     * Is the transformed XML output identical to the standard ASCI output?
     */
    public void testXML2Text()
        throws Exception
    {
        Javancss pJavancss = new Javancss( getTestFile( 32 ) );
        pJavancss.setXML( true );

        StringWriter sw = new StringWriter();

        XmlFormatter.printStart( sw );
        pJavancss.printPackageNcss( sw );
        pJavancss.printObjectNcss( sw );
        pJavancss.printFunctionNcss( sw );
        pJavancss.printJavaNcss( sw );
        XmlFormatter.printEnd( sw );

        String sXML = sw.toString(); 

        try
        {
            String sText = XMLUtil.getXML( sXML, getXslFile( "javancss2text.xsl" ) );
            FileUtil.writeFile( "./t", sText );
            sText = sText.replaceAll( "(?:\r\n|\n\r)", "\n" );
            String sCompare = FileUtil.readFile( getTestFile( "Output32.txt" ).getAbsolutePath() );
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

    private File getXslFile( String filename )
    {
        return new File( getTestDir(), ".." + File.separator + "xslt" + File.separator + filename );
    }

    /**
     * Test code goes here.
     */
    protected void _doIt()
        throws Exception
    {
        testParsing ();
        testXML2Text();
    }

    public static void main( String[] asArg_ )
    {
        new XmlFormatterTest().main();
    }
}
