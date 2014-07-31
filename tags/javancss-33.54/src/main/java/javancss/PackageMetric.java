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
 * Basic data class to store all metrics attached to a package.
 *
 * @author  Chr. Clemens Lee <clemens@kclee.com>
 * @version $Id$
 */
public class PackageMetric
    extends Metric
{
    public int classes = 0;
    public int functions = 0;

    public PackageMetric()
    {
        super();
    }

    @Override
    public void clear()
    {
        super.clear();
        classes = 0;
        functions = 0;
    }

    public void add( PackageMetric pPackageMetric_ )
    {
        if ( pPackageMetric_ == null )
        {
            return;
        }
        classes += pPackageMetric_.classes;
        functions += pPackageMetric_.functions;
        ncss += pPackageMetric_.ncss;

        javadocs += pPackageMetric_.javadocs;
        javadocsLn += pPackageMetric_.javadocsLn;
        singleLn += pPackageMetric_.singleLn;
        multiLn += pPackageMetric_.multiLn;
    }
}
