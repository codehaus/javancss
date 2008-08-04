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
import java.io.PrintStream;
import java.io.StringBufferInputStream;
import java.util.Enumeration;
import java.util.Vector;
//import java.util.*;

import ccl.util.FileUtil;
import ccl.util.Test;
import ccl.util.Util;

import javancss.Javancss;
import javancss.JavancssConstants;
import javancss.PackageMetric;

/**
 * Test class for the JavaNCSS application.
 *
 *   $Id$
 *   3. 9. 1996
 */
public class JavancssTest extends    Test 
                          implements JavancssConstants 
{
    private String _sTestDir = null;

    private void _doNcssTest( int testNumber, int expectedNcss )
    {
        Javancss pJavancss = new Javancss( FileUtil.concatPath( _sTestDir
                                                                , "Test" 
                                                                  + testNumber 
                                                                  + ".java"    ) );
        int ncss = pJavancss.getNcss();
        bugIf( ncss != expectedNcss
               , "Parsing file Test"
                 + testNumber
                 + ".java failed. Ncss is "
                 + ncss 
                 + " and not "
                 + expectedNcss
                 + "."                      );
    }

    private void _doNcssTest( int testNumber )
    {
        Javancss pJavancss = new Javancss( FileUtil.concatPath( _sTestDir
                                                                , "Test" 
                                                                  + testNumber 
                                                                  + ".java"    ) );
        int ncss = pJavancss.getNcss();
        bugIf( ncss == 0
               , "Parsing file Test"
                 + testNumber
                 + ".java failed. Ncss is 0" );
    }

    /**
     * There has been a bug introduced for version 16.34 which
     * counts Javadoc comments (**) for fields as well as for
     * methods, while I am only in the later ones.
     * File Test20 has 6 methods and 6 + 1 ** comments.
     * This test should make sure that 3 attribute comments
     * won't be counted.
     */
    private void _checkJavadocs()
    {
        Javancss pJavancss = new Javancss( FileUtil.concatPath( _sTestDir
                                                                , "Test20.java" ) );
        
        Vector vObjectMetrics = pJavancss.getObjectMetrics();
        Vector vClassMetrics  = (Vector)vObjectMetrics.elementAt( 0 );
        int jvdcs = ((Integer)vClassMetrics.elementAt(OBJ_JVDCS)).intValue();
        Assert( jvdcs == 7
                , "Expected 7 Javadocs in in file Test20.java but got " + jvdcs + "!" );
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
    private void _checkJavadocLines()
    {
        _checkJavadocLines( FileUtil.concatPath( _sTestDir
                                                 , "Test28.java" )
                            , "jacob"
                            , 0 );
        
        //
        // same test with more files
        //

        Vector vArgs = new Vector();
        vArgs.addElement( FileUtil.concatPath( _sTestDir
                                               , "Test20.java" ) );
        vArgs.addElement( FileUtil.concatPath( _sTestDir
                                               , "Test21.java" ) );
        vArgs.addElement( FileUtil.concatPath( _sTestDir
                                               , "Test28.java" ) );
        _checkJavadocLines( vArgs
                            , "jacob"
                            , 0 );

        _checkJavadocLines( 68, ".", 6 );
        _checkJavadocLines( 69, ".", 4 );
        _checkJavadocLines( new int[] { 68, 69 }
                            , "."
                            , 10 );
        _checkJavadocLines( 65, "idebughc.testsuite", 14 );
    }

    private void _checkJavadocLines( Vector vJavaSources
                                     , String sPackage
                                     , int javadocLines )
    {
        Javancss pJavancss = new Javancss( vJavaSources );

        _checkJavadocLines( pJavancss
                            , sPackage
                            , javadocLines );
    }

    private void _checkJavadocLines( String sJavaSource
                                     , String sPackage
                                     , int javadocLines )
    {
        Javancss pJavancss = new Javancss( sJavaSource );
        
        _checkJavadocLines( pJavancss
                            , sPackage
                            , javadocLines );
    }

    private void _checkJavadocLines( int testFile
                                     , String sPackage
                                     , int javadocLines )
    {
        String sSourceFile = FileUtil.concatPath( _sTestDir
                                                  , "Test" + testFile + ".java" );

        _checkJavadocLines( sSourceFile
                            , sPackage
                            , javadocLines );
    }

    private void _checkJavadocLines( int[] aTestFile
                                     , String sPackage
                                     , int javadocLines )
    {
        Vector vArgs = new Vector();
        for( int i = 0; i < aTestFile.length; i++ )
        {
            int next = aTestFile[ i ];
            String sSourceFile = FileUtil.concatPath( _sTestDir
                                                      , "Test" + next + ".java" );
            vArgs.addElement( sSourceFile );
        }

        _checkJavadocLines( vArgs
                            , sPackage
                            , javadocLines );
    }

    private void _checkJavadocLines( Javancss pJavancss
                                     , String sPackage
                                     , int javadocLines )
    {
        Vector vPackageMetrics = pJavancss.getPackageMetrics();
        Assert( vPackageMetrics.size() >= 1 );
        PackageMetric pmPackage = null;
        Enumeration ePackageMetrics = vPackageMetrics.elements();
        while( ePackageMetrics.hasMoreElements() )
        {
            PackageMetric pmNext = (PackageMetric)ePackageMetrics.nextElement();
            if ( pmNext.name.equals( sPackage ) )
            {
                pmPackage = pmNext;
            }
        }
        Assert( pmPackage != null );
        Assert( pmPackage.javadocsLn == javadocLines
                , "pmJacob.javadocsLn: " 
                  + pmPackage
                  + ": " 
                  + pmPackage.javadocsLn );
    }

    /**
     * This method tries to reproduce a bug reported by
     * Chris Williamson. He reported problems with code
     * like this: F150MemoryMap f150Map = (F150MemoryMap) F150.super.memMap;
     */
    private void _checkInnerClasses()
    {
        _doNcssTest( 70, 4 );
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
        String sLocalPath = _sTestDir; //FileUtil.getClassPath(this);
        Util.debug( this, "_doIt()._sTestDir: " + _sTestDir );

        boolean bSkip = false;
        /*
        bSkip = true;
        // */

        Javancss pJavancss = null;

        if ( !bSkip ) {
                _checkJavadocLines();
                
        _checkInnerClasses();

        // Nr. 1
        pJavancss = new Javancss(FileUtil.concatPath(sLocalPath, "Test1.java"));
        int ncss1 = pJavancss.getNcss();
        bugIf(ncss1 != 318, "Ncss: " + ncss1);

        // Nr. 2
        pJavancss = new Javancss(FileUtil.concatPath(sLocalPath, "Test2.java"));
        bugIf(pJavancss.getNcss() != 8);
        // Nr. 3
        pJavancss = new Javancss(FileUtil.concatPath(sLocalPath, "Test3.java"));
        bugIf(pJavancss.getNcss() != 69);
        // Nr. 4
        pJavancss = new Javancss(FileUtil.concatPath(sLocalPath, "Test4.java"));
        bugIf(pJavancss.getNcss() != 11);
        // Nr. 5
        pJavancss = new Javancss(FileUtil.concatPath(sLocalPath, "Test5.java"));
        bugIf(pJavancss.getNcss() != 16);
        // Nr. 6
        pJavancss = new Javancss(FileUtil.concatPath(sLocalPath, "Test6.java"));
        int ncss6 = pJavancss.getNcss();
        bugIf(ncss6 != 565, "Ncss: " + ncss6);
        bugIf(pJavancss.getLOC() != 1254, "LOC: " + pJavancss.getLOC());
        // Nr. 8
        pJavancss = new Javancss(FileUtil.concatPath(sLocalPath, "Test7.java"));
        bugIf(pJavancss.getNcss() != 30, "Ncss: " + pJavancss.getNcss());
        // Nr. 9
        Javancss pJavancss8 = new Javancss(FileUtil.concatPath(sLocalPath, "Test8.java"));
        bugIf(pJavancss.getNcss() != pJavancss8.getNcss());
        // Nr. 10
        pJavancss = new Javancss(FileUtil.concatPath(sLocalPath, "Test9.java"));
        bugIf(ncss1 != pJavancss.getLOC(), "LOC: " + pJavancss.getLOC());
        // Nr. 11
        pJavancss = new Javancss(FileUtil.concatPath(sLocalPath, "Test10.java"));
        bugIf(pJavancss.getLOC() != ncss6, "LOC: " + pJavancss.getLOC());
        bugIf(pJavancss.getLOC() != pJavancss.getNcss(), "NCSS: " + pJavancss.getNcss());
        pJavancss = new Javancss(FileUtil.concatPath(sLocalPath, "Test11.java"));
        bugIf(pJavancss.getLOC() != pJavancss.getNcss(), "NCSS: " + pJavancss.getNcss() +
              ", LOC: " + pJavancss.getLOC());
        pJavancss = new Javancss(FileUtil.concatPath(sLocalPath, "Test12.java"));
        bugIf(pJavancss.getLOC() != pJavancss.getNcss(), "NCSS: " + pJavancss.getNcss() +
              ", LOC: " + pJavancss.getLOC());
        Vector vFunctions = pJavancss.getFunctionMetrics();
        String sFirstFunction = (String)((Vector)vFunctions.elementAt(0)).elementAt(FCT_NAME);
        bugIf( sFirstFunction == null );
        /*System.out.println( sFirstFunction );*/
        bugIf(!sFirstFunction.equals("Test12.readFile(URL)"), sFirstFunction);
        pJavancss = new Javancss(FileUtil.concatPath(sLocalPath, "Test13.java"));
        bugIf(pJavancss.getLOC() != pJavancss.getNcss(), "NCSS: " + pJavancss.getNcss() +
              ", LOC: " + pJavancss.getLOC());
        pJavancss = new Javancss(FileUtil.concatPath(sLocalPath, "Test14.java"));
        bugIf(pJavancss.getLOC() != pJavancss.getNcss(), "NCSS: " + pJavancss.getNcss() +
              ", LOC: " + pJavancss.getLOC());
        pJavancss = new Javancss(FileUtil.concatPath(sLocalPath, "Test15.java"));
        bugIf(pJavancss.getLOC() != pJavancss.getNcss(), "NCSS: " + pJavancss.getNcss() +
              ", LOC: " + pJavancss.getLOC());
        pJavancss = new Javancss(FileUtil.concatPath(sLocalPath, "Test16.java"));
        bugIf(pJavancss.getNcss() != 4);
        pJavancss = new Javancss(FileUtil.concatPath(sLocalPath, "Test17.java"));
        bugIf(pJavancss.getLOC() != pJavancss.getNcss(), "NCSS: " + pJavancss.getNcss() +
              ", LOC: " + pJavancss.getLOC());
        pJavancss = new Javancss(FileUtil.concatPath(sLocalPath, "Test18.java"));
        bugIf(pJavancss.getLOC() != pJavancss.getNcss(), "NCSS: " + pJavancss.getNcss() +
              ", LOC: " + pJavancss.getLOC());
        // Nr. 22
        pJavancss = new Javancss(FileUtil.concatPath(sLocalPath, "Test19.java"));
        vFunctions = pJavancss.getFunctionMetrics();
        sFirstFunction = (String)((Vector)vFunctions.elementAt(0)).elementAt(FCT_NAME);
        bugIf(!sFirstFunction.equals("test.Test19.foo(String[],Controller)"), sFirstFunction);
        sFirstFunction = (String)((Vector)vFunctions.elementAt(3)).elementAt(FCT_NAME);
        bugIf(!sFirstFunction.equals("test.Test19.main(String[])"));
        // Nr. 24
        pJavancss = new Javancss(FileUtil.concatPath(sLocalPath, "Test20.java"));
        bugIf(pJavancss.getNcss() != 46, "NCSS: " + pJavancss.getNcss());
        // Nr. 25
        pJavancss = new Javancss(FileUtil.concatPath(sLocalPath, "Test21.java"));
        bugIf(pJavancss.getNcss() != 67, "NCSS: " + pJavancss.getNcss());
        // Nr. 26
        pJavancss = new Javancss(FileUtil.concatPath(sLocalPath, "Test22.java"));
        bugIf(pJavancss.getNcss() != 283, "NCSS: " + pJavancss.getNcss());
        // Nr. 27
        pJavancss = new Javancss(FileUtil.concatPath(sLocalPath, "Test23.java"));
        bugIf(pJavancss.getNcss() != 10, "NCSS: " + pJavancss.getNcss());
        vFunctions = pJavancss.getFunctionMetrics();
        bugIf(vFunctions.size() != 7);
        bugIf(new Javancss(FileUtil.concatPath(sLocalPath, "Test24.java")).
            getFunctionMetrics().size() != vFunctions.size());
        // Nr. 30
        pJavancss = new Javancss(FileUtil.concatPath(sLocalPath, "Test25.java"));
        bugIf(pJavancss.getNcss() != 12);
        bugIf(pJavancss.getFunctionMetrics().size() != 9);
        // Nr. 32
        pJavancss = new Javancss(FileUtil.concatPath(sLocalPath, "Test26.java"));
        bugIf(pJavancss.getNcss() != 47, "NCSS: " + pJavancss.getNcss());
        // Nr. 33
        pJavancss = new Javancss(FileUtil.concatPath(sLocalPath, "Test27.java"));
        bugIf(pJavancss.getNcss() != 4, "NCSS: " + pJavancss.getNcss());
        // Nr. 34
        pJavancss = new Javancss(FileUtil.concatPath(sLocalPath, "Test28.java"));
        bugIf(pJavancss.getNcss() != 465, "NCSS: " + pJavancss.getNcss());
        // Nr. 35
        String sTogether;
        String sTest11 = "";
        String sTest12 = "";
        try {
            sTest11 = FileUtil.readFile(FileUtil.concatPath(sLocalPath, "Test11.java"));
            sTest12 = FileUtil.readFile(FileUtil.concatPath(sLocalPath, "Test12.java"));
            bugIf(false);
        } catch(Exception e) {
            bugIf(true);
        }
        sTogether = sTest11 + sTest12;
        pJavancss = new Javancss(new StringBufferInputStream(sTogether));
        vFunctions = pJavancss.getFunctionMetrics();
        Util.debug("JavancssTest._doIt().vFunctions: " + vFunctions);
        sFirstFunction = (String)((Vector)vFunctions.elementAt(0)).elementAt(FCT_NAME);
        bugIf(!sFirstFunction.equals("ccl.util.Test11.atoi(String)"));
        String sSomeFunction = (String)((Vector)vFunctions.elementAt(32)).elementAt(FCT_NAME);
        bugIf(!sSomeFunction.equals("Test12.readFile(URL)"), "Function: " + sSomeFunction);
        Vector vPackages = pJavancss.getPackageMetrics();
        bugIf(vPackages.size() != 2);
        String[] asArg = new String[3];
        asArg[0] = FileUtil.concatPath(sLocalPath, "Test11.java");
        asArg[1] = asArg[0];
        asArg[2] = FileUtil.concatPath(sLocalPath, "Test12.java");
        int ncss38 = pJavancss.getNcss();
        
        // turn stdout off
        PrintStream psStdout = System.out;
        System.setOut(new PrintStream(new ByteArrayOutputStream()));
        
        pJavancss = new Javancss(asArg, "$Header: /home/clemens/src/java/javancss/src/javancss/test/RCS/JavancssTest.java,v 1.34 2006/10/06 11:46:43 clemens Exp clemens $");
        
        // turn stdout on
        System.setOut(psStdout);
        
        vPackages = pJavancss.getPackageMetrics();
        bugIf(vPackages.size() != 2);
        bugIf(ncss38 == pJavancss.getNcss());
        // Nr. 41
        pJavancss = new Javancss(FileUtil.concatPath(sLocalPath, "Test29.java"));
        bugIf(pJavancss.getNcss() != 1, "NCSS: " + pJavancss.getNcss());
        // Nr. 42
        // missing lf in last line/<EOF> not in single line
        try 
        {
            pJavancss = new Javancss( FileUtil.concatPath( sLocalPath
                                                           , "Test35.java" ) );
            bugIf( pJavancss.getNcss() != 1 );
        } catch(Exception eEOF) {
            bugIf( true, "}<EOF>" );
        }
        try {
            pJavancss = new Javancss( FileUtil.concatPath( sLocalPath, "Test36.java" ) );
            bugIf( pJavancss.getNcss() != 1 );
        } catch(Error eEOF) {
            bugIf( true, "//<EOF>" );
        }
        try {
            pJavancss = new Javancss(FileUtil.concatPath(sLocalPath, "Test37.java"));
            bugIf(pJavancss.getNcss() != 1);
        } catch(Error eCTRLZ) {
            bugIf(true, "//ctrl-Z");
        }
        try {
            pJavancss = new Javancss(FileUtil.concatPath(sLocalPath, "Test38.java"));
            bugIf(pJavancss.getNcss() != 1);
        } catch(Error eCTRLZ) {
            bugIf(true, "0x0actrl-Z");
        }
        // Nr. 46
        // semicolons not allowed by JLS, but not counted anyway.
        try {
            pJavancss = new Javancss(FileUtil.concatPath(sLocalPath, "Test39.java"));
            bugIf(pJavancss.getNcss() != 5);
        } catch(Error eEmptyStatements) {
            bugIf(true, "Empty statments.");
        }
        // Nr. 47
        // ;; in java.sql.Connection
        try {
            pJavancss = new Javancss(FileUtil.concatPath(sLocalPath, "Test32.java"));
            bugIf( pJavancss.getNcss() != 26
                   , "Test32.java: ncss should be 26 but is: " + pJavancss.getNcss() );
        } catch(Error eJavaSQLConnection) {
            bugIf(true, "java.sql.Connection double semicolon");
        }

        testCCN(sLocalPath);

        // javancss parsed a file which it shouldn't
        pJavancss = new Javancss(FileUtil.concatPath(sLocalPath, "Test42.java"));
        bugIf(pJavancss.getLastErrorMessage() == null,
              "Test42 should be parsed *and* result in an exception.");
        // file containing just ;
        pJavancss = new Javancss(FileUtil.concatPath(sLocalPath, "Test43.java"));
        bugIf(pJavancss.getNcss() != 0);
        // Test if javancss continues after running across a parse error
        // Test42,java has an errror, so use two other file and this and
        // take a look if it finishes with right result.
        pJavancss = new Javancss(FileUtil.concatPath(sLocalPath, "Test1.java"));
        int ncss57 = pJavancss.getNcss();
        pJavancss = new Javancss(FileUtil.concatPath(sLocalPath, "Test2.java"));
        ncss57 += pJavancss.getNcss();
        Vector vFiles = new Vector();
        vFiles.addElement(FileUtil.concatPath(sLocalPath, "Test1.java"));
        vFiles.addElement(FileUtil.concatPath(sLocalPath, "Test42.java"));
        vFiles.addElement(FileUtil.concatPath(sLocalPath, "Test2.java"));
        pJavancss = new Javancss(vFiles);
        bugIf(pJavancss.getNcss() != ncss57, "ncss57: " + ncss57 +
              " pJavancss.getNcss(): " + pJavancss.getNcss());

        // Bug reported by .. .
        // Test48.java should be parsed.
        pJavancss = new Javancss(FileUtil.concatPath(sLocalPath, "Test48.java"));
        bugIf( pJavancss.getNcss() <= 0
               , "Parsing file Test48.java failed!" );

        pJavancss = new Javancss( FileUtil.concatPath(sLocalPath, "Test49.java") );
        bugIf( pJavancss.getNcss() != 3
               , "Parsing file Test49.java failed!" );
        pJavancss = new Javancss( FileUtil.concatPath(sLocalPath, "Test50.java") );
        bugIf( pJavancss.getNcss() <= 0
               , "Parsing file Test50.java failed!" );

        pJavancss = new Javancss( FileUtil.concatPath(sLocalPath, "Test51.java") );
        bugIf( pJavancss.getNcss() != 8
               , "Parsing file Test51.java failed!" );

        pJavancss = new Javancss( FileUtil.concatPath(sLocalPath, "Test52.java") );
        int test52ncss = pJavancss.getNcss();
        bugIf( test52ncss != 12
               , "Parsing file Test52.java failed. Ncss is " + test52ncss + " and not 12." );

        pJavancss = new Javancss( FileUtil.concatPath(sLocalPath, "Test53.java") );
        int test53ncss = pJavancss.getNcss();
        bugIf( test53ncss != 4
               , "Parsing file Test53.java failed. Ncss is " + test53ncss + " and not 4." );

        _doNcssTest( 54, 9 );
        _doNcssTest( 55, 5 );
        _doNcssTest( 56 );
        _doNcssTest( 57 );

        }

        pJavancss = measureTestFile( sLocalPath, 56 );
        String sOutput56 = pJavancss.printPackageNcss();
        sOutput56 += "\n";
        sOutput56 += pJavancss.printObjectNcss();
        sOutput56 += "\n";
        sOutput56 += pJavancss.printFunctionNcss();
        sOutput56 = Util.replace( sOutput56, "\r\n", "\n" );
        String sCompare56 = FileUtil.readFile( FileUtil.concatPath( sLocalPath, "Output56.txt" ) );
        Assert( sOutput56.equals( sCompare56 )
                , "File test/Output56.txt and javancss output differs:\n"
                + sOutput56 );
        FileUtil.writeFile( "t", sOutput56 );

        XmlFormatterTest xmlTest = new XmlFormatterTest( this );
        xmlTest.setTestDir( _sTestDir );
        xmlTest.run();
        setTests( xmlTest );

        _doNcssTest( 58,  37 );
        _doNcssTest( 59, 122 );
        _doNcssTest( 60,  35 );
        _doNcssTest( 61, 203 );
        _doNcssTest( 62, 616 );
        _doNcssTest( 63, 330 );
        _doNcssTest( 64,  70 );
        _doNcssTest( 65, 301 );
        _doNcssTest( 66,   3 );
        _doNcssTest( 67,  31 );

        // check that javadocs are countet correctly
        // after patches for additional comment counting
        pJavancss = new Javancss( FileUtil.concatPath( sLocalPath, "Test32.java" ) );
        String sOutput32 = pJavancss.printPackageNcss();
        sOutput32 += "\n";
        sOutput32 += pJavancss.printObjectNcss();
        sOutput32 += "\n";
        sOutput32 += pJavancss.printFunctionNcss();
        sOutput32 = Util.replace( sOutput32, "\r\n", "\n" );
        String sCompare32 = FileUtil.readFile( FileUtil.concatPath( sLocalPath, "Output32.txt" ) );
        Assert( sOutput32.equals( sCompare32 )
                , "File test/Output32.txt and javancss output differs:\n"
                + sOutput32 );

        // more comment counting
        _doNcssTest( 68, 3 );
        _doJvdcsTest(68, 2);

        _checkJavadocs();

        // zero methods one class javadoc comment, there should be no exception
        // because of divide by zero
        _doNcssTest( 69, 1 );

        // test for strictfp interface and static inner interface
        _doNcssTest( 73, 1 );
        _doNcssTest( 74, 2 );

        //
        // new Java 1.5 features
        //

        // @Deprecated annotation
        _doNcssTest( 75,  584 );
        // Class<?>
        _doNcssTest( 76,  404 );
        // Class<?,?>
        _doNcssTest( 77,  48 );
        // WeakHashMap<ImageInputStream, Object>
        _doNcssTest( 78,  35 );
        // Map<String, Map<String, Object>>
        _doNcssTest( 79,  1345 );
        // @Deprecated protected KeyStroke closeMenuKey;
        _doNcssTest( 80,  96 );
        // etc.
        _doNcssTest( 81,  92 );
        _doNcssTest( 82,  26 );
        _doNcssTest( 83,  2 );
        _doNcssTest( 84,  55 );
        _doNcssTest( 85,  242 );
        _doNcssTest( 86,  22 );
        _doNcssTest( 87,  8 );
        _doNcssTest( 88,  11 );
        _doNcssTest( 89,  65 );
        _doNcssTest( 90,  494 );
        _doNcssTest( 91,  30 );
        _doNcssTest( 92,  6 );
        _doNcssTest( 93,  38 );
        _doNcssTest( 94,  3 );
        _doNcssTest( 95,  10 );
        _doNcssTest( 96,  3 );
        _doNcssTest( 97,  3 );
        _doNcssTest( 98,  37 );
        _doNcssTest( 99,  243 );
        _doNcssTest( 100,  5 );
        _doNcssTest( 101,  256 );
        _doNcssTest( 102,  10 );
        _doNcssTest( 103,  3 );
        _doNcssTest( 104,  3 );
        _doNcssTest( 105,  5 );
        _doNcssTest( 106,  10 );
        _doNcssTest( 107,  9 );
        _doNcssTest( 108,  2 );
        _doNcssTest( 109,  2 );
        _doNcssTest( 110,  1 );
        _doNcssTest( 111,  4 );
        _doNcssTest( 112,  3 );
        _doNcssTest( 113,  13 );
        _doNcssTest( 114,  3 );
        _doNcssTest( 115,  11663 );
        _doNcssTest( 116,  12 );
        _doNcssTest( 117,  15 );
        _doNcssTest( 119,  2 );
        _doNcssTest( 120,  3 );
        _doNcssTest( 121,  5 );
        _doJvdcsTest( 121, 2 );
        _doJvdcsTest( 122, 1 );
        _doNcssTest( 123, 4 );
        _doNcssTest( 124, 7 );
        _doNcssTest( 125, 2 );
        _doNcssTest( 126, 13 );
        _doNcssTest( 127, 3 );
        _doNcssTest( 128, 3 );
        _doNcssTest( 129, 6 );
        _doNcssTest( 130, 5 );
        _doNcssTest( 131, 6 );
        _doNcssTest( 132, 12 );
        _doNcssTest( 134, 4 );
    }

    private void _doJvdcsTest(int testFileNumber, int expectedJvdcsResult) {
                Javancss pJavancss;
                pJavancss = new Javancss( FileUtil.concatPath( _sTestDir
                                                               , "Test" + testFileNumber + ".java"    ) );
                Vector vObjectMetrics = pJavancss.getObjectMetrics();
                Vector vClassMetrics = (Vector)vObjectMetrics.elementAt( 0 );
                int jvdcs      = ((Integer)vClassMetrics.elementAt(OBJ_JVDCS)).intValue();
                /*int jvdc = pJavancss.getJvdc();*/
                bugIf( jvdcs != expectedJvdcsResult
                   , "Parsing file Test" + testFileNumber + ".java failed. Jvdc is "
                     + jvdcs 
                     + " and not " + expectedJvdcsResult + "."
                       );
        }

    /**
     * Tests the cyclomatic complexity number measurement.
     */
    public void testCCN( String sLocalPath )
    {
        _enterSubTest( "ccn" );

        // CCN for return and throw
        Javancss pJavancss  = measureTestFile( sLocalPath, 40 );
        Vector   vFunctions = pJavancss.getFunctionMetrics();
        bugIf(vFunctions.size() != 1);
        int ccn = ((Integer)((Vector)vFunctions.elementAt(0)).elementAt(FCT_CCN)).intValue();
        bugIf(ccn != 3, "CCN in constructor of Test40 should be 3, it is: " + ccn);

        pJavancss = measureTestFile( sLocalPath, 41 );
        vFunctions = pJavancss.getFunctionMetrics();
        ccn = getCCN(vFunctions, 0);
        bugIf(ccn != 3, "CCN in constructor of Test41 should be 3, it is: " + ccn);
        ccn = getCCN(vFunctions, 1);
        bugIf(ccn != 1, "CCN in method of Test41 should be 1, it is: " + ccn);
        ccn = getCCN(vFunctions, 2);
        bugIf(ccn != 3, "CCN in method of Test41 should be 3, it is: " + ccn);
        ccn = getCCN(vFunctions, 3);
        bugIf(ccn != 3, "CCN in method of Test41 should be 3, it is: " + ccn);
        ccn = getCCN(vFunctions, 4);
        bugIf(ccn != 1, "CCN in method of Test41 should be 1, it is: " + ccn);

        pJavancss = measureTestFile( sLocalPath, 72 );
        vFunctions = pJavancss.getFunctionMetrics();
        int expectedCCN = 4;
                int methodIndex = 0;      
        assertCCN( vFunctions, methodIndex, expectedCCN );
        
        expectedCCN = 4 + 1;
        methodIndex = 1;
        assertCCN( vFunctions, methodIndex, expectedCCN );

        expectedCCN = 4;
        methodIndex = methodIndex + 1;
        assertCCN( vFunctions, methodIndex, expectedCCN );

        expectedCCN = 4;
        methodIndex = methodIndex + 1;
        assertCCN( vFunctions, methodIndex, expectedCCN );

        expectedCCN = 2;
        methodIndex = methodIndex + 1;
        assertCCN( vFunctions, methodIndex, expectedCCN );

        _exitSubTest();
    }

        private void assertCCN(Vector vFunctions, int methodIndex, int expectedCCN) {
                int ccn;
                ccn = getCCN( vFunctions, methodIndex );
                Assert( ccn == expectedCCN
                        , "Expected ccn was " + expectedCCN + " but the result is: " + ccn );                
        }

        private int getCCN(Vector vFunctions, int methodIndex) {
                return ((Integer)((Vector)vFunctions.elementAt(methodIndex)).elementAt(FCT_CCN)).intValue();
        }

        private Javancss measureTestFile(String sLocalPath, int testFileId) {
                return new Javancss(FileUtil.concatPath(sLocalPath, "Test" + testFileId + ".java"));
        }

    public void setTestDir( String sTestDir_ ) {
        _sTestDir = sTestDir_;
    }
}
