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

import javancss.Javancss;
import ccl.util.Test;

public class ParseTest
    extends AbstractTest
{

    private void testParse()
    {
        _enterSubTest( "parse" );

        _checkParse( 31 ); // java.net.Socket, why?
        _checkParse( 33 ); // java.text.Decimalformat, why?
        _checkParse( 34 ); // java.text.TextBoundaryData, why?
        
        _checkParse( 48 );

        _checkParse( 50 );

        _checkParse( 71 ); // class declared in method
        _checkParse( 133 ); // char.class
        _checkParse( 135 ); // annotation 

        _checkParse( 142 ); // JAVANCSS-12
        _checkParse( 143 ); // JAVANCSS-9
        _checkParse( 144 ); // JAVANCSS-13
        _checkParse( 145 ); // JAVANCSS-14
        _checkParse( 146 ); // JAVANCSS-17
        _checkParse( 147 ); // anonymous subcluss 
        _checkParse( 148 ); // JAVANCSS-49
        _checkParse( 149 ); // JAVANCSS-46
        _checkParse( 150 ); // JAVANCSS-53 
        _checkParse( 151 ); // JAVANCSS-45 
        _checkParse( 152 ); // JAVANCSS-57
        _checkParse( 153 ); // JAVANCSS-54
        _checkParse( 154 ); // JAVANCSS-52
        _checkParse( 155 ); // JAVANCSS-28
        _checkParse( 156 ); // hexadecimal floating-point literals
        _checkParse( 157 ); // Java 7 literals
        _checkParse(158); // JAVANCSS-48
        _checkParse(159); // default and static method in interface
        _checkParse(160); // java8 lambda and method reference

        _exitSubTest();
    }

    private void _checkParse( int testFile )
    {
        Javancss pJavancss = measureTestFile( testFile );
        bugIf( pJavancss.getNcss() <= 0, "Parsing file Test" + testFile + ".java failed!" );
    }

    public ParseTest()
    {
        super();
    }

    public ParseTest( Test pTest_ )
    {
        super( pTest_ );
    }

    /**
     * Test code goes here.
     */
    @Override
    protected void _doIt()
    {
        testParse();
    }

    public static void main( String[] asArg_ )
    {
        new ParseTest().main();
    }

}
