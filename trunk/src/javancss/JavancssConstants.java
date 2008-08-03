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
 * Contains a couple of constants used to fill and access data
 * structures containing Java metric data.
 *
 * @author    Chr. Clemens Lee <clemens@kclee.com>
 * $Id: JavancssConstants.java,v 1.5 2006/04/16 11:42:20 clemens Exp clemens $
 */
public interface JavancssConstants
{
  static final int FCT_NAME = 0;
  static final int FCT_NCSS = 1;
  static final int FCT_CCN  = 2;
  static final int FCT_JVDC = 3;

  // added by SMS
  static final int FCT_JVDC_LINES   = 4;
  static final int FCT_SINGLE_LINES = 5;
  static final int FCT_MULTI_LINES  = 6;

  static final int OBJ_NAME  = 0;
  static final int OBJ_NCSS  = 1;
  static final int OBJ_FCTS  = 2;
  static final int OBJ_CLSSS = 3;
  //static final int OBJ_JVDCS = 4;
  //static final int OBJ_JVDCS = 5;
  static final int OBJ_JVDCS = 6;

  // added by SMS
  static final int OBJ_JVDC_LINES   = 7;
  static final int OBJ_SINGLE_LINES = 8;
  static final int OBJ_MULTI_LINES  = 9;
}
