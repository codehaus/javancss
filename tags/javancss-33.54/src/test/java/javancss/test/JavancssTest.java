/*
Copyright (C) 2014 Chr. Clemens Lee <clemens@kclee.com>.

This file is part of JavaNCSS
(http://javancss.codehaus.org/).

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA*/

package javancss.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.List;

import ccl.util.FileUtil;
import ccl.util.Test;
import ccl.util.Util;
import javancss.FunctionMetric;
import javancss.Javancss;
import javancss.PackageMetric;

/**
 * Test class for the JavaNCSS application.
 *
 *   $Id$
 *   3. 9. 1996
 */
public class JavancssTest
    extends AbstractTest
{

    private Javancss measureWithArgs( String[] args )
        throws IOException
    {
        // turn stdout off
        PrintStream psStdout = System.out;

        try
        {
            System.setOut( new PrintStream( new ByteArrayOutputStream() ) );
            return new Javancss( args );
        }
        finally
        {
            // turn stdout back on
            System.setOut( psStdout );
        }
    }

    public JavancssTest()
    {
        super();
    }

    public JavancssTest( Test pTest_ )
    {
        super( pTest_ );
    }

    @Override
    protected void _doIt()
        throws Exception
    {
        Util.debug( this, "_doIt().testDir: " + getTestDir() );

        NcssTest ncssTest = new NcssTest( this );
        ncssTest.setTestDir( getTestDir() );
        ncssTest.run();
        setTests( ncssTest );

        ParseTest parseTest = new ParseTest( this );
        parseTest.setTestDir( getTestDir() );
        parseTest.run();
        setTests( parseTest );

        JavadocTest javadocTest = new JavadocTest( this );
        javadocTest.setTestDir( getTestDir() );
        javadocTest.run();
        setTests( javadocTest );

        CCNTest ccnTest = new CCNTest( this );
        ccnTest.setTestDir( getTestDir() );
        ccnTest.run();
        setTests( ccnTest );

        testCummulating();

        testEncoding();

        testVersion();

        testRecursive();

        XmlFormatterTest xmlTest = new XmlFormatterTest( this );
        xmlTest.setTestDir( getTestDir() );
        xmlTest.run();
        setTests( xmlTest );
        
        ensureAllTestFilesUsed();
    }

    public static void main( String[] asArg_ )
    {
        new JavancssTest().main();
    }

    private void testCummulating()
        throws IOException
    {
        _enterSubTest( "cummulating" );

        // Nr. 35
        String sTogether;
        String sTest11 = "";
        String sTest12 = "";
        try
        {
            sTest11 = FileUtil.readFile( getTestFile( 11 ).getAbsolutePath() );
            sTest12 = FileUtil.readFile( getTestFile( 12 ).getAbsolutePath() );
        }
        catch ( IOException e )
        {
            bugIf( true );
        }
        sTogether = sTest11 + sTest12;
        Javancss pJavancss = new Javancss( new StringReader( sTogether ) );
        List<FunctionMetric> vFunctions = pJavancss.getFunctionMetrics();
        Util.debug( "JavancssTest._doIt().vFunctions: " + vFunctions );
        String sFirstFunction = vFunctions.get( 0 ).name;
        bugIf( !sFirstFunction.equals( "ccl.util.Test11.atoi(String)" ) );
        String sSomeFunction = vFunctions.get( 32 ).name;
        bugIf( !sSomeFunction.equals( "Test12.readFile(URL)" ), "Function: " + sSomeFunction );
        List<PackageMetric> vPackages = pJavancss.getPackageMetrics();
        bugIf( vPackages.size() != 2 );
        int ncss38 = pJavancss.getNcss();

        String[] asArg = new String[3];
        asArg[0] = getTestFile( 11 ).getAbsolutePath();
        asArg[1] = asArg[0];
        asArg[2] = getTestFile( 12 ).getAbsolutePath();
        pJavancss = measureWithArgs( asArg );
        vPackages = pJavancss.getPackageMetrics();
        bugIf( vPackages.size() != 2 );
        bugIf( ncss38 == pJavancss.getNcss() );

        _exitSubTest();
    }

    public void testEncoding()
        throws IOException
    {
        _enterSubTest( "encoding" );

        String[] args = new String[] { "-encoding", "UTF-16", getTestFile( "TestEncoding.java" ).getAbsolutePath() };
        Javancss pJavancss = measureWithArgs( args );

        int expectedNcss = 11;
        int ncss = pJavancss.getNcss();

        bugIf( ncss != expectedNcss,
               "Parsing file TestEncoding.java failed. Ncss is " + ncss + " and not " + expectedNcss + "." );

        _exitSubTest();
    }

    public void testVersion()
        throws IOException
    {
        _enterSubTest( "version" );

        String[] args = new String[] { "-version" };
        measureWithArgs( args );

        _exitSubTest();
    }

    public void testRecursive()
        throws IOException
    {
        _enterSubTest( "recursive" );

        String[] args = new String[] { "-recursive", getTestFile( "../../../lib" ).getAbsolutePath() };
        measureWithArgs( args );

        _exitSubTest();
    }
}
