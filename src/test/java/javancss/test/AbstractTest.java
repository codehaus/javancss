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

import java.io.File;
import java.io.FileFilter;
import java.util.HashSet;
import java.util.Set;

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
    private static final Set<File> parsedFiles = new HashSet<File>();

    private File testDir = null;

    public void setTestDir( File testDir_ )
    {
        testDir = testDir_;
    }

    public File getTestDir()
    {
        return testDir;
    }

    protected File getTestFile( String filename_ )
    {
        File file = new File( testDir, filename_ );
        parsedFiles.add( file );
        return file;
    }

    protected File getTestFile( int testFileId )
    {
        return getTestFile( "Test" + testFileId + ".java" );
    }

    protected Javancss measureTestFile( int testFileId )
    {
        return new Javancss( getTestFile( testFileId ) );
    }
    
    protected void ensureAllTestFilesUsed()
    {
        File[] existingFiles = testDir.listFiles( new FileFilter()
        {
            public boolean accept( File path )
            {
                return path.getName().endsWith( ".java" );
            }
        } );
        for ( File ef : existingFiles )
        {
            if ( !parsedFiles.contains( ef ) )
            {
                bugIf( !parsedFiles.contains( ef ), "Test file " + ef + " has not been parsed!" );
            }
        }
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
