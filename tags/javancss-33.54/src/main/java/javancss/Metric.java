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

/**
 * Base data class to store all metrics common to packages, objects and functions.
 *
 * @author  Hervé Boutemy
 * @version $Id$
 */
public abstract class Metric
    implements Comparable<Metric>
{
    public String name = ".";

    /** Non Commenting Source Statements (NCSS). */
    public int ncss = 0;
    public int firstLine = 0;
    public int javadocs = 0;
    public int javadocsLn = 0;
    public int singleLn = 0;
    public int multiLn = 0;

    public Metric()
    {
        super();
    }

    public void clear()
    {
        name = ".";
        ncss = 0;
        javadocs = 0;
        javadocsLn = 0;
        singleLn = 0;
        multiLn = 0;
    }

    @Override
    public String toString()
    {
        return name;
    }

    public int compareTo( Metric m )
    {
        return name.compareTo( m.name );
    }

    public boolean equals( Metric m )
    {
        return compareTo( m ) == 0;
    }

    @Override
    public int hashCode()
    {
        return name.hashCode();
    }
}
