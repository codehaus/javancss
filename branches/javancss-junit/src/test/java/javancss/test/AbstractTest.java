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

import junit.framework.TestCase;

/**
 * Base JavaNCSS unit-tests class.
 *
 * @author  Hervé Boutemy
 * @version $Id$
 */
public abstract class AbstractTest extends TestCase
{
    private File testDir = new File( "target/test-classes/test" );

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
        File file = new File( testDir, filename );
        assertTrue( "file not found: " + file.getAbsolutePath(), file.exists() );
        return file;
    }

    protected File getTestFile( int testFileId )
    {
        return getTestFile( "Test" + testFileId + ".java" );
    }

    protected AbstractTest()
    {
        super();
    }

    protected void bugIf( boolean condition )
    {
        assertFalse( condition );
    }

    protected void bugIf( boolean condition, String message )
    {
        assertFalse( message, condition );
    }

    protected void Assert( boolean condition )
    {
        assertTrue( condition );
    }

    protected void Assert( boolean condition, String message )
    {
        assertTrue( message, condition );
    }
}
