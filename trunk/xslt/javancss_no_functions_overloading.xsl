<?xml version="1.0"?>
<!--
Copyright (C) 2002 Chr. Clemens Lee <clemens@kclee.com>.

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
Boston, MA 02111-1307, USA.
-->
<!--
This stylesheet converts JavaNCSS XML output to a document format
which is very similar to its input but has different names
for the functions tag so no overloading of that name takes place.

Chr. Clemens Lee
2002-08-04
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="xml" indent="yes"/>

  <xsl:template match="/javancss/packages/package/functions">
    <pck_functions><xsl:value-of select="."/></pck_functions>
  </xsl:template>

  <xsl:template match="/javancss/packages/total/functions">
    <pck_total_functions><xsl:value-of select="."/></pck_total_functions>
  </xsl:template>

  <xsl:template match="/javancss/objects/object/functions">
    <obj_functions><xsl:value-of select="."/></obj_functions>
  </xsl:template>

  <xsl:template match="/javancss/objects/averages/functions">
    <obj_avg_functions><xsl:value-of select="."/></obj_avg_functions>
  </xsl:template>

  <xsl:template match="*">
    <xsl:copy>
      <xsl:apply-templates/>
    </xsl:copy>
  </xsl:template>

</xsl:stylesheet>
