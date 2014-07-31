/*
Copyright (C) 2000 Chr. Clemens Lee <clemens@kclee.com>.

This file is part of JavaNCSS
(http://www.kclee.de/clemens/java/javancss/).

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

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javancss.FunctionMetric;
import javancss.Javancss;
import ccl.util.FileUtil;
import ccl.util.Test;
import ccl.util.Util;

public class NcssTest
    extends AbstractTest
{

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
        _checkNcss( 30, 3 );

        // ;; in java.sql.Connection
        try
        {
            _checkNcss( 32, 26 );
        }
        catch ( Error eJavaSQLConnection )
        {
            bugIf( true, "java.sql.Connection double semicolon" );
        }
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
        // semicolons not allowed by JLS, but not counted anyway.
        try
        {
            _checkNcss( 39, 5 );
        }
        catch ( Error eEmptyStatements )
        {
            bugIf( true, "Empty statments." );
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
        List<File> vFiles = new ArrayList<File>();
        vFiles.add( getTestFile( 1 ) );
        vFiles.add( getTestFile( 42 ) );
        vFiles.add( getTestFile( 2 ) );
        pJavancss = new Javancss( vFiles );
        bugIf( pJavancss.getNcss() != ncss57, "ncss57: " + ncss57 + " pJavancss.getNcss(): " + pJavancss.getNcss() );

        _checkNcss( 49, 3 );
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

    public void testNcssAndMore()
        throws IOException
    {
        _enterSubTest( "ncss and more..." );

        Javancss pJavancss = null;

        final int ncss1 = 318;
        _checkNcss( 1, ncss1 );

        final int ncss6 = 565;
        _checkNcssAndLoc( 6, ncss6, 1254 );

        // Nr. 10
        pJavancss = measureTestFile( 9 );
        bugIf( ncss1 != pJavancss.getLOC(), "LOC: " + pJavancss.getLOC() );

        _checkNcssAndLoc( 10, ncss6 );
        _checkNcssAndLoc( 11 );

        pJavancss = _checkNcssAndLoc( 12 );
        List<FunctionMetric> vFunctions = pJavancss.getFunctionMetrics();
        String sFirstFunction = vFunctions.get( 0 ).name;
        bugIf( sFirstFunction == null );
        /* System.out.println( sFirstFunction ); */
        bugIf( !sFirstFunction.equals( "Test12.readFile(URL)" ), sFirstFunction );

        // Nr. 22
        pJavancss = measureTestFile( 19 );
        vFunctions = pJavancss.getFunctionMetrics();
        sFirstFunction = vFunctions.get( 0 ).name;
        bugIf( !sFirstFunction.equals( "test.Test19.foo(String[],Controller)" ), sFirstFunction );
        sFirstFunction = vFunctions.get( 3 ).name;
        bugIf( !sFirstFunction.equals( "test.Test19.main(String[])" ) );

        pJavancss = _checkNcss( 23, 10 );
        vFunctions = pJavancss.getFunctionMetrics();
        bugIf( vFunctions.size() != 7 );
        bugIf( measureTestFile( 24 ).getFunctionMetrics().size() != vFunctions.size() );

        // Nr. 30
        pJavancss = _checkNcss( 25, 12 );
        bugIf( pJavancss.getFunctionMetrics().size() != 9 );

        pJavancss = measureTestFile( 56 );
        StringWriter sw = new StringWriter();
        pJavancss.printPackageNcss( sw );
        sw.write( "\n" );
        pJavancss.printObjectNcss( sw );
        sw.write( "\n" );
        pJavancss.printFunctionNcss( sw );

        String sOutput56 = Util.replace( sw.toString(), "\r\n", "\n" );
        String sCompare56 = FileUtil.readFile( getTestFile( "Output56.txt" ).getAbsolutePath() );
        Assert( sOutput56.equals( sCompare56 ), "File test/Output56.txt and javancss output differs:\n" + sOutput56 );

        // check that javadocs are counted correctly
        // after patches for additional comment counting
        pJavancss = measureTestFile( 32 );
        sw = new StringWriter();
        pJavancss.printPackageNcss( sw );
        sw.write( "\n" );
        pJavancss.printObjectNcss( sw );
        sw.write( "\n" );
        pJavancss.printFunctionNcss( sw );

        String sOutput32 = Util.replace( sw.toString(), "\r\n", "\n" );
        String sCompare32 = FileUtil.readFile( getTestFile( "Output32.txt" ).getAbsolutePath() );
        Assert( sOutput32.equals( sCompare32 ), "File test/Output32.txt and javancss output differs:\n" + sOutput32 );

        _exitSubTest();
    }

    public NcssTest()
    {
        super();
    }

    public NcssTest( Test pTest_ )
    {
        super( pTest_ );
    }

    /**
     * Test code goes here.
     */
    @Override
    protected void _doIt()
        throws Exception
    {
        testNcss();
        testNcssAndMore();
    }

    public static void main( String[] asArg_ )
    {
        new NcssTest().main();
    }

}
