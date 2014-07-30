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
import java.io.Writer;

/**
 * Interface that each class generating output has to implement.
 * The two main implementations to generate ascii and xml output.<p/>
 *
 * A formatter implementation will be invoked by the Javancss
 * class.
 *
 * @author  Chr. Clemens Lee <clemens@kclee.com>
 * @version $Id$
 */
public interface Formatter
{
    void printPackageNcss( Writer w )
        throws IOException;

    void printObjectNcss( Writer w )
        throws IOException;

    void printFunctionNcss( Writer w )
        throws IOException;

    void printJavaNcss( Writer w )
        throws IOException;

    void printStart( Writer w )
        throws IOException;

    void printEnd( Writer w )
        throws IOException;
}
