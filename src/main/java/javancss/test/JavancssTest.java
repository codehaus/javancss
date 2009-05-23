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

package javancss.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ccl.util.FileUtil;
import ccl.util.Test;
import ccl.util.Util;

import javancss.FunctionMetric;
import javancss.Javancss;
import javancss.ObjectMetric;
import javancss.PackageMetric;

/**
 * Test class for the JavaNCSS application.
 *
 *   $Id$
 *   3. 9. 1996
 */
public class JavancssTest extends AbstractTest
{
    private Javancss measureTestFile( int testFileId )
    {
        return new Javancss( getTestFile( testFileId ) );
    }

    private Javancss _checkNcss( int testNumber, int expectedNcss )
    {
        Javancss pJavancss = measureTestFile( testNumber );
        int ncss = pJavancss.getNcss();
        bugIf( ncss != expectedNcss, "Parsing file Test" + testNumber + ".java failed. Ncss is "
                 + ncss  + " and not " + expectedNcss + "." );
        return pJavancss;
    }

    private void _checkNcss( int testNumber )
    {
        Javancss pJavancss = measureTestFile( testNumber );
        int ncss = pJavancss.getNcss();
        bugIf( ncss == 0, "Parsing file Test" + testNumber + ".java failed. Ncss is 0" );
    }

    private Javancss _checkNcssAndLoc( int testNumber, int expectedNcss, int expectedLoc )
    {
        Javancss pJavancss = _checkNcss( testNumber, expectedNcss );
        int loc = pJavancss.getLOC();
        bugIf( loc != expectedLoc, "Parsing file Test" + testNumber + ".java failed. LOC is "
                 + loc  + " and not " + expectedLoc + "." );
        return pJavancss;
    }

    private Javancss _checkNcssAndLoc( int testNumber, int expectedNcssAndLoc )
    {
        return _checkNcssAndLoc( testNumber, expectedNcssAndLoc, expectedNcssAndLoc );
    }

    private Javancss _checkNcssAndLoc( int testNumber )
    {
        Javancss pJavancss = measureTestFile( testNumber );
        int ncss = pJavancss.getNcss();
        int loc = pJavancss.getLOC();
        bugIf( ncss != loc, "Parsing file Test" + testNumber + ".java failed. Ncss is "
                 + ncss  + ", LOC is " + loc + ": should be equal." );
        return pJavancss;
    }

    /**
     * There has been a bug introduced for version 16.34 which
     * counts Javadoc comments (**) for fields as well as for
     * methods, while I am only in the later ones.
     * File Test20 has 6 methods and 6 + 1 ** comments.
     * This test should make sure that 3 attribute comments
     * won't be counted.
     */
    public void testJavadocs()
    {
        _enterSubTest( "javadocs" );

        _checkJvdcs( 20, 7 );
        _checkJvdcs( 68, 2 );
        _checkJvdcs( 121, 2 );
        _checkJvdcs( 122, 1 );

        _exitSubTest();
    }

    /**
     * Check that Javadoc line counts are correct.
     * There is one bug where there are only two files with
     * a package jacob in the test directory (Test1.java and
     * Test28.java), and while both have no javadocs at all,
     * the count is still 11. The eleven seem to come from
     * files Test20.java and Test21.java.
     * This test shall trace this bug down and shall later asure
     * that it got fixed.
     */
    public void testJavadocLines()
    {
        _enterSubTest( "javadoc lines" );

        _checkJavadocLines( 28, "jacob", 0 );

        //
        // same test with more files
        //
        _checkJavadocLines( new int[] { 20, 21, 28 }, "jacob", 0 );

        _checkJavadocLines( 68, ".", 6 );
        _checkJavadocLines( 69, ".", 4 );
        _checkJavadocLines( new int[] { 68, 69 }, ".", 10 );
        _checkJavadocLines( 65, "idebughc.testsuite", 14 );

        _exitSubTest();
    }

    private void _checkJavadocLines( int testFile, String sPackage, int javadocLines )
    {
        Javancss pJavancss = measureTestFile( testFile );

        _checkJavadocLines( pJavancss, sPackage, javadocLines );
    }

    private void _checkJavadocLines( int[] aTestFile, String sPackage, int javadocLines )
    {
        List files = new ArrayList();
        for( int i = 0; i < aTestFile.length; i++ )
        {
            int next = aTestFile[ i ];
            files.add( getTestFile( next ) );
        }

        Javancss pJavancss = new Javancss( files );
        _checkJavadocLines( pJavancss, sPackage, javadocLines );
    }

    private void _checkJavadocLines( Javancss pJavancss, String sPackage, int javadocLines )
    {
        List vPackageMetrics = pJavancss.getPackageMetrics();
        Assert( vPackageMetrics.size() >= 1 );
        PackageMetric pmPackage = null;
        Iterator ePackageMetrics = vPackageMetrics.iterator();
        while( ePackageMetrics.hasNext() )
        {
            PackageMetric pmNext = (PackageMetric)ePackageMetrics.next();
            if ( pmNext.name.equals( sPackage ) )
            {
                pmPackage = pmNext;
            }
        }
        Assert( pmPackage != null );
        Assert( pmPackage.javadocsLn == javadocLines
                , "pmJacob.javadocsLn: " + pmPackage + ": " + pmPackage.javadocsLn );
    }

    public JavancssTest() {
        super();
    }

    public JavancssTest(Test pTest_) {
        super(pTest_);
    }

    protected void _doIt()
        throws Exception
    {
        Util.debug( this, "_doIt().testDir: " + getTestDir() );

        testNcss();

        testNcssAndMore();

        testJavadocLines();

        testJavadocs();

        testCCN();

        testEncoding();

        testVersion();

        testRecursive();

        XmlFormatterTest xmlTest = new XmlFormatterTest( this );
        xmlTest.setTestDir( getTestDir() );
        xmlTest.run();
        setTests( xmlTest );
    }

    public void testNcss()
    {
        _enterSubTest( "ncss" );

        Javancss pJavancss = null;

        _checkNcss( 2, 8 );
        _checkNcss( 3, 69 );
        _checkNcss( 4, 11 );
        _checkNcss( 5, 16 );
        _checkNcss( 7, 30 );
        _checkNcss( 8, 30 );

        _checkNcssAndLoc( 13 );
        _checkNcssAndLoc( 14 );
        _checkNcssAndLoc( 15 );

        _checkNcss( 16, 4 );
        _checkNcssAndLoc( 17 );
        _checkNcssAndLoc( 18 );

        _checkNcss( 20, 46 );
        _checkNcss( 21, 67 );
        _checkNcss( 22, 283 );
        _checkNcss( 26, 47 );
        _checkNcss( 27, 4 );
        _checkNcss( 28, 465 );
        _checkNcss( 29, 1 );

        // Nr. 42
        // missing lf in last line/<EOF> not in single line
        try
        {
            _checkNcss( 35, 1 );
        }
        catch ( Exception eEOF )
        {
            bugIf( true, "}<EOF>" );
        }
        try
        {
            _checkNcss( 36, 1 );
        }
        catch ( Error eEOF )
        {
            bugIf( true, "//<EOF>" );
        }
        try
        {
            _checkNcss( 37, 1 );
        }
        catch ( Error eCTRLZ )
        {
            bugIf( true, "//ctrl-Z" );
        }
        try
        {
            _checkNcss( 38, 1 );
        }
        catch ( Error eCTRLZ )
        {
            bugIf( true, "0x0actrl-Z" );
        }
        // Nr. 46
        // semicolons not allowed by JLS, but not counted anyway.
        try
        {
            _checkNcss( 39, 5 );
        }
        catch ( Error eEmptyStatements )
        {
            bugIf( true, "Empty statments." );
        }
        // Nr. 47
        // ;; in java.sql.Connection
        try
        {
            _checkNcss( 32, 26 );
        }
        catch ( Error eJavaSQLConnection )
        {
            bugIf( true, "java.sql.Connection double semicolon" );
        }

        // javancss parsed a file which it shouldn't
        pJavancss = measureTestFile( 42 );
        bugIf( pJavancss.getLastErrorMessage() == null, "Test42 should be parsed *and* result in an exception." );

        // file containing just ;
        _checkNcss( 43, 0 );

        // Test if javancss continues after running across a parse error
        // Test42,java has an error, so use two other file and this and
        // take a look if it finishes with right result.
        pJavancss = measureTestFile( 1 );
        int ncss57 = pJavancss.getNcss();
        pJavancss = measureTestFile( 2 );
        ncss57 += pJavancss.getNcss();
        List vFiles = new ArrayList();
        vFiles.add( getTestFile( 1 ) );
        vFiles.add( getTestFile( 42 ) );
        vFiles.add( getTestFile( 2 ) );
        pJavancss = new Javancss( vFiles );
        bugIf( pJavancss.getNcss() != ncss57, "ncss57: " + ncss57 + " pJavancss.getNcss(): " + pJavancss.getNcss() );

        // Bug reported by .. .
        // Test48.java should be parsed.
        pJavancss = measureTestFile( 48 );
        bugIf( pJavancss.getNcss() <= 0, "Parsing file Test48.java failed!" );

        _checkNcss( 49, 3 );

        pJavancss = measureTestFile( 50 );
        bugIf( pJavancss.getNcss() <= 0, "Parsing file Test50.java failed!" );

        _checkNcss( 51, 8 );
        _checkNcss( 52, 12 );
        _checkNcss( 53, 4 );
        _checkNcss( 54, 9 );
        _checkNcss( 55, 5 );
        _checkNcss( 56 );
        _checkNcss( 57 );
        _checkNcss( 58, 37 );
        _checkNcss( 59, 122 );
        _checkNcss( 60, 35 );
        _checkNcss( 61, 203 );
        _checkNcss( 62, 616 );
        _checkNcss( 63, 330 );
        _checkNcss( 64, 70 );
        _checkNcss( 65, 301 );
        _checkNcss( 66, 3 );
        _checkNcss( 67, 31 );

        // more comment counting
        _checkNcss( 68, 3 );

        // zero methods one class javadoc comment, there should be no exception
        // because of divide by zero
        _checkNcss( 69, 1 );

        /*
         * This method tries to reproduce a bug reported by
         * Chris Williamson. He reported problems with code
         * like this: F150MemoryMap f150Map = (F150MemoryMap) F150.super.memMap;
         */
        _checkNcss( 70, 4 );

        // test for strictfp interface and static inner interface
        _checkNcss( 73, 1 );
        _checkNcss( 74, 2 );

        //
        // new Java 1.5 features
        //

        // @Deprecated annotation
        _checkNcss( 75, 584 );
        // Class<?>
        _checkNcss( 76, 404 );
        // Class<?,?>
        _checkNcss( 77, 48 );
        // WeakHashMap<ImageInputStream, Object>
        _checkNcss( 78, 35 );
        // Map<String, Map<String, Object>>
        _checkNcss( 79, 1345 );
        // @Deprecated protected KeyStroke closeMenuKey;
        _checkNcss( 80, 96 );
        // etc.
        _checkNcss( 81, 92 );
        _checkNcss( 82, 26 );
        _checkNcss( 83, 2 );
        _checkNcss( 84, 55 );
        _checkNcss( 85, 242 );
        _checkNcss( 86, 22 );
        _checkNcss( 87, 8 );
        _checkNcss( 88, 11 );
        _checkNcss( 89, 65 );
        _checkNcss( 90, 494 );
        _checkNcss( 91, 30 );
        _checkNcss( 92, 6 );
        _checkNcss( 93, 38 );
        _checkNcss( 94, 3 );
        _checkNcss( 95, 10 );
        _checkNcss( 96, 3 );
        _checkNcss( 97, 3 );
        _checkNcss( 98, 37 );
        _checkNcss( 99, 243 );
        _checkNcss( 100, 5 );
        _checkNcss( 101, 256 );
        _checkNcss( 102, 10 );
        _checkNcss( 103, 3 );
        _checkNcss( 104, 3 );
        _checkNcss( 105, 5 );
        _checkNcss( 106, 10 );
        _checkNcss( 107, 9 );
        _checkNcss( 108, 2 );
        _checkNcss( 109, 2 );
        _checkNcss( 110, 1 );
        _checkNcss( 111, 4 );
        _checkNcss( 112, 3 );
        _checkNcss( 113, 13 );
        _checkNcss( 114, 3 );
        _checkNcss( 115, 11663 );
        _checkNcss( 116, 12 );
        _checkNcss( 117, 15 );
        _checkNcss( 119, 2 );
        _checkNcss( 120, 3 );
        _checkNcss( 121, 5 );
        _checkNcss( 123, 4 );
        _checkNcss( 124, 7 );
        _checkNcss( 125, 2 );
        _checkNcss( 126, 13 );
        _checkNcss( 127, 3 );
        _checkNcss( 128, 3 );
        _checkNcss( 129, 6 );
        _checkNcss( 130, 5 );
        _checkNcss( 131, 6 );
        _checkNcss( 132, 12 );
        _checkNcss( 134, 4 );
        _checkNcss( 136, 2 );
        _checkNcss( 138, 3 );

        _exitSubTest();
    }

    public void testNcssAndMore() throws IOException
    {
        _enterSubTest( "ncss and more..." );

        Javancss pJavancss = null;

        final int ncss1 = 318;
        _checkNcss( 1 , ncss1 );

        final int ncss6 = 565;
        _checkNcssAndLoc( 6, ncss6, 1254 );

        // Nr. 10
        pJavancss = measureTestFile( 9 );
        bugIf( ncss1 != pJavancss.getLOC(), "LOC: " + pJavancss.getLOC() );

        _checkNcssAndLoc( 10, ncss6 );
        _checkNcssAndLoc( 11 );

        pJavancss = _checkNcssAndLoc( 12 );
        List/*<FunctionMetric>*/ vFunctions = pJavancss.getFunctionMetrics();
        String sFirstFunction = ( (FunctionMetric) vFunctions.get( 0 ) ).name;
        bugIf( sFirstFunction == null );
        /* System.out.println( sFirstFunction ); */
        bugIf( !sFirstFunction.equals( "Test12.readFile(URL)" ), sFirstFunction );

        // Nr. 22
        pJavancss = measureTestFile( 19 );
        vFunctions = pJavancss.getFunctionMetrics();
        sFirstFunction = ( (FunctionMetric) vFunctions.get( 0 ) ).name;
        bugIf( !sFirstFunction.equals( "test.Test19.foo(String[],Controller)" ), sFirstFunction );
        sFirstFunction = ( (FunctionMetric) vFunctions.get( 3 ) ).name;
        bugIf( !sFirstFunction.equals( "test.Test19.main(String[])" ) );

        pJavancss = _checkNcss( 23, 10 );
        vFunctions = pJavancss.getFunctionMetrics();
        bugIf( vFunctions.size() != 7 );
        bugIf( new Javancss( getTestFile( 24 ) ).getFunctionMetrics().size() != vFunctions.size() );

        // Nr. 30
        pJavancss = _checkNcss( 25, 12 );
        bugIf( pJavancss.getFunctionMetrics().size() != 9 );

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
        pJavancss = new Javancss( new StringReader( sTogether ) );
        vFunctions = pJavancss.getFunctionMetrics();
        Util.debug( "JavancssTest._doIt().vFunctions: " + vFunctions );
        sFirstFunction = ( (FunctionMetric) vFunctions.get( 0 ) ).name;
        bugIf( !sFirstFunction.equals( "ccl.util.Test11.atoi(String)" ) );
        String sSomeFunction = ( (FunctionMetric) vFunctions.get( 32 ) ).name;
        bugIf( !sSomeFunction.equals( "Test12.readFile(URL)" ), "Function: " + sSomeFunction );
        List vPackages = pJavancss.getPackageMetrics();
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

        pJavancss = measureTestFile( 56 );
        String sOutput56 = pJavancss.printPackageNcss();
        sOutput56 += "\n";
        sOutput56 += pJavancss.printObjectNcss();
        sOutput56 += "\n";
        sOutput56 += pJavancss.printFunctionNcss();
        sOutput56 = Util.replace( sOutput56, "\r\n", "\n" );
        String sCompare56 = FileUtil.readFile( getTestFile( "Output56.txt" ).getAbsolutePath() );
        Assert( sOutput56.equals( sCompare56 ), "File test/Output56.txt and javancss output differs:\n" + sOutput56 );

        // check that javadocs are counted correctly
        // after patches for additional comment counting
        pJavancss = measureTestFile( 32 );
        String sOutput32 = pJavancss.printPackageNcss();
        sOutput32 += "\n";
        sOutput32 += pJavancss.printObjectNcss();
        sOutput32 += "\n";
        sOutput32 += pJavancss.printFunctionNcss();
        sOutput32 = Util.replace( sOutput32, "\r\n", "\n" );
        String sCompare32 = FileUtil.readFile( getTestFile( "Output32.txt" ).getAbsolutePath() );
        Assert( sOutput32.equals( sCompare32 ), "File test/Output32.txt and javancss output differs:\n" + sOutput32 );

        _exitSubTest();
    }

    private void _checkJvdcs( int testFileNumber, int expectedJvdcsResult )
    {
        Javancss pJavancss = measureTestFile( testFileNumber );
        List/*<ObjectMetric>*/ vObjectMetrics = pJavancss.getObjectMetrics();
        ObjectMetric classMetric = (ObjectMetric) vObjectMetrics.get( 0 );
        int jvdcs = classMetric.javadocs;
        /* int jvdc = pJavancss.getJvdc(); */
        bugIf( jvdcs != expectedJvdcsResult, "Parsing file Test" + testFileNumber + ".java failed. Jvdc is " + jvdcs
                        + " and not " + expectedJvdcsResult + "." );
    }

    /**
     * Tests the cyclomatic complexity number measurement.
     */
    public void testCCN()
    {
        _enterSubTest( "ccn" );

        // CCN for return and throw
        Javancss pJavancss = measureTestFile( 40 );
        List/*<FunctionMetric>*/ vFunctions = pJavancss.getFunctionMetrics();
        bugIf( vFunctions.size() != 1 );
        assertCCN( vFunctions, 0, 3 );

        pJavancss = measureTestFile( 41 );
        vFunctions = pJavancss.getFunctionMetrics();
        assertCCN( vFunctions, 0, 3 );
        assertCCN( vFunctions, 1, 1 );
        assertCCN( vFunctions, 2, 3 );
        assertCCN( vFunctions, 3, 3 );
        assertCCN( vFunctions, 4, 1 );

        pJavancss = measureTestFile( 72 );
        vFunctions = pJavancss.getFunctionMetrics();
        assertCCN( vFunctions, 0, 4 );
        assertCCN( vFunctions, 1, 5 );
        assertCCN( vFunctions, 2, 4 );
        assertCCN( vFunctions, 3, 4 );
        assertCCN( vFunctions, 4, 2 );

        _exitSubTest();
    }

    private void assertCCN( List vFunctions, int methodIndex, int expectedCCN )
    {
        int ccn = ( (FunctionMetric) vFunctions.get( methodIndex ) ).ccn;
        Assert( ccn == expectedCCN, "Expected ccn was " + expectedCCN + " but the result is: " + ccn );
    }

    public static void main( String[] asArg_ )
    {
        new JavancssTest().main();
    }

    private Javancss measureWithArgs( String[] args ) throws IOException
    {
        // turn stdout off
        PrintStream psStdout = System.out;

        try
        {
            System.setOut( new PrintStream( new ByteArrayOutputStream() ) );
            return new Javancss(args);
        }
        finally
        {
            // turn stdout back on
            System.setOut( psStdout );
        }
    }

    public void testEncoding() throws IOException
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

    public void testVersion() throws IOException
    {
        _enterSubTest( "version" );

        String[] args = new String[] { "-version" };
        Javancss pJavancss = measureWithArgs( args );

        _exitSubTest();
    }

    public void testRecursive() throws IOException
    {
        _enterSubTest( "recursive" );

        String[] args = new String[] { "-recursive", getTestFile( "../lib" ).getAbsolutePath()  };
        Javancss pJavancss = measureWithArgs( args );

        _exitSubTest();
    }
}
