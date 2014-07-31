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

import java.util.List;

import javancss.FunctionMetric;
import javancss.Javancss;
import ccl.util.Test;

public class CCNTest
    extends AbstractTest
{

    /**
     * Tests the cyclomatic complexity number measurement.
     */
    public void testCCN()
    {
        _enterSubTest( "ccn" );

        // CCN for return and throw
        Javancss pJavancss = measureTestFile( 40 );
        List<FunctionMetric> vFunctions = pJavancss.getFunctionMetrics();
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

    private void assertCCN( List<FunctionMetric> vFunctions, int methodIndex, int expectedCCN )
    {
        int ccn = vFunctions.get( methodIndex ).ccn;
        Assert( ccn == expectedCCN, "Expected ccn was " + expectedCCN + " but the result is: " + ccn );
    }

    public CCNTest()
    {
        super();
    }

    public CCNTest( Test pTest_ )
    {
        super( pTest_ );
    }

    /**
     * Test code goes here.
     */
    @Override
    protected void _doIt()
    {
        testCCN();
    }

    public static void main( String[] asArg_ )
    {
        new CCNTest().main();
    }

}
