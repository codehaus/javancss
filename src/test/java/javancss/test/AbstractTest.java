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

import java.io.File;

import javancss.Javancss;

import ccl.util.Test;

/**
 * Base JavaNCSS unit-tests class.
 *
 * @author  Hervé Boutemy
 * @version $Id$
 */
public abstract class AbstractTest extends Test
{
    private File testDir = null;

    public void setTestDir( File testDir_ )
    {
        testDir = testDir_;
    }

    public File getTestDir()
    {
        return testDir;
    }

    protected File getTestFile( String filename )
    {
        return new File( testDir, filename );
    }

    protected File getTestFile( int testFileId )
    {
        return getTestFile( "Test" + testFileId + ".java" );
    }

    protected Javancss measureTestFile( int testFileId )
    {
        return new Javancss( getTestFile( testFileId ) );
    }
    
    protected AbstractTest()
    {
        super();
    }

    protected AbstractTest( Test pTest_ )
    {
        super( pTest_ );
    }

    public void main()
    {
        main( new File( "." ) );
    }

    public void main( File baseDir )
    {
        setTestDir( new File( baseDir, "src/test/resources" ) );
        setVerbose( true );
        setTiming ( true );
        run();
        printResult();
    }
}
