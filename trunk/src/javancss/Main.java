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

import java.util.Locale;

/**
 * Main class of the JavaNCSS application. It does nothing
 * than starting the batch process and immediately delegates
 * control to the Javancss class.
 *
 * @author    Chr. Clemens Lee <clemens@kclee.com>
 * @version   $Id: Main.java,v 28.49 2006/10/06 11:46:24 clemens Exp clemens $
 */
public class Main {
    public static final String S_RCS_HEADER = "$Header: /home/clemens/src/java/javancss/src/javancss/RCS/Main.java,v 28.49 2006/10/06 11:46:24 clemens Exp clemens $";

    public static void main(String[] asArgs) {
        Locale.setDefault( Locale.US );

        Javancss pJavancss = new Javancss(asArgs, S_RCS_HEADER);
        
        if (pJavancss.getLastErrorMessage() != null) {
            System.exit(1);
        }
        
        System.exit(0);
    }
}
