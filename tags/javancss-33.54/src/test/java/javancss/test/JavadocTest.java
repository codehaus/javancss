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
import java.util.ArrayList;
import java.util.List;

import ccl.util.Test;

import javancss.Javancss;
import javancss.ObjectMetric;
import javancss.PackageMetric;

public class JavadocTest extends AbstractTest {

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
       _checkJvdcs( 46, 3 );
       _checkJvdcs( 47, 2 );
       _checkJvdcs( 68, 2 );
       _checkJvdcs( 121, 2 );
       _checkJvdcs( 122, 1 );
       _checkJvdcs( 139 , 3 ); // JAVANCSS-20
       _checkJvdcs( 140 , 2 ); // JAVANCSS-20
       _checkJvdcs( 141 , 1 ); // JAVANCSS-20

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
       List<File> files = new ArrayList<File>();
       for( int next : aTestFile )
       {
           files.add( getTestFile( next ) );
       }

       Javancss pJavancss = new Javancss( files );
       _checkJavadocLines( pJavancss, sPackage, javadocLines );
   }

   private void _checkJavadocLines( Javancss pJavancss, String sPackage, int javadocLines )
   {
       List<PackageMetric> vPackageMetrics = pJavancss.getPackageMetrics();
       Assert( vPackageMetrics.size() >= 1 );
       PackageMetric pmPackage = null;
       for ( PackageMetric pmNext : vPackageMetrics )
       {
           if ( pmNext.name.equals( sPackage ) )
           {
               pmPackage = pmNext;
           }
       }
       Assert( pmPackage != null );
       Assert( pmPackage.javadocsLn == javadocLines
               , "pmJacob.javadocsLn: " + pmPackage + ": " + pmPackage.javadocsLn );
   }

   private void _checkJvdcs( int testFileNumber, int expectedJvdcsResult )
   {
       Javancss pJavancss = measureTestFile( testFileNumber );
       List<ObjectMetric> vObjectMetrics = pJavancss.getObjectMetrics();
       ObjectMetric classMetric = vObjectMetrics.get( 0 );
       int jvdcs = classMetric.javadocs;
       /* int jvdc = pJavancss.getJvdc(); */
       bugIf( jvdcs != expectedJvdcsResult, "Parsing file Test" + testFileNumber + ".java failed. Jvdc is " + jvdcs
                       + " and not " + expectedJvdcsResult + "." );
   }

   public JavadocTest()
   {
       super();
   }

   public JavadocTest( Test pTest_ )
   {
       super( pTest_ );
   }

   /**
    * Test code goes here.
    */
   @Override
   protected void _doIt()
   {
       testJavadocLines();
       testJavadocs();
   }
   
   public static void main( String[] asArg_ )
   {
       new JavadocTest().main();
    }

}
