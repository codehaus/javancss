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

import ccl.util.Test;
import javancss.Javancss;

public abstract class CommonJavancssTest extends AbstractTest 
{
   
   protected Javancss measureTestFile( int testFileId )
   {
       return new Javancss( getTestFile( testFileId ) );
   }

   protected Javancss measureWithArgs( String[] args ) throws IOException
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

   public CommonJavancssTest()
   {
       super();
   }

   public CommonJavancssTest( Test pTest_ )
   {
       super( pTest_ );
   }
   
}
