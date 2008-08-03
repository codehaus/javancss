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

package javancss;

/**
 * Basic data class to store all metrics attached to a package.
 *
 * @author  Chr. Clemens Lee <clemens@kclee.com>
 * @version $Id: PackageMetric.java,v 1.5 2006/04/16 11:42:24 clemens Exp clemens $
 */
public class PackageMetric 
{
    public String name    = ".";
    public int classes    = 0;
    public int functions  = 0;
    public int ncss       = 0;

    // added by SMS
    public int javadocs   = 0;
    public int javadocsLn = 0;
    public int singleLn   = 0;
    public int multiLn    = 0;

    public PackageMetric() 
    {
        super();
    }
    
    public void clear()
    {
        name      = ".";
        classes   = 0;
        functions = 0;
        ncss      = 0;

        // added by SMS
        javadocs   = 0;
        javadocsLn = 0;
        singleLn   = 0;
        multiLn    = 0;
    }

    public void add(PackageMetric pPackageMetric_) {
        if (pPackageMetric_ == null) {
            return;
        }
        classes    += pPackageMetric_.classes;
        functions  += pPackageMetric_.functions;
        ncss       += pPackageMetric_.ncss;

        javadocs   += pPackageMetric_.javadocs;
        javadocsLn += pPackageMetric_.javadocsLn;
        singleLn   += pPackageMetric_.singleLn;
        multiLn    += pPackageMetric_.multiLn;
    }
    
    public String toString() {
        return name;
    }
}
