/*
Copyright (C) 2001 Chr. Clemens Lee <clemens@kclee.com>.

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

package javancss;

/**
 * Interface that each class generating output has to implement.
 * The two main implementations to generate ascii and xml output.<p/>
 *
 * A formatter implementation will be invoked by the Javancss 
 * class.
 *
 * @author  Chr. Clemens Lee <clemens@kclee.com>
 * @version $Id: Formatter.java,v 1.2 2006/04/16 11:42:18 clemens Exp clemens $
 */
public interface Formatter
{
    public String printPackageNcss();

    public String printObjectNcss();

    public String printFunctionNcss();

    public String printJavaNcss();
}
