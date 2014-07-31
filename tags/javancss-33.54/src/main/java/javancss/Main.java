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

package javancss;

import java.io.IOException;
import java.util.Locale;

/**
 * Main class of the JavaNCSS application. It does nothing
 * than starting the batch process and immediately delegates
 * control to the Javancss class.
 *
 * @author    Chr. Clemens Lee <clemens@kclee.com>
 * @version   $Id$
 */
public class Main
{
    private static final String IMPL_VERSION = Main.class.getPackage().getImplementationVersion();

    public static final String S_RCS_HEADER = "$Header: /javancss/Main.java,v "
        + ( ( IMPL_VERSION == null ) ? "0.0 2001/01/01 00:00:00" : IMPL_VERSION ) + " clemens Exp clemens $";

    public static void main( String[] asArgs )
        throws IOException
    {
        Locale.setDefault( Locale.US );

        Javancss pJavancss = new Javancss( asArgs );

        if ( pJavancss.getLastErrorMessage() != null )
        {
            System.exit( 1 );
        }

        System.exit( 0 );
    }
}
