<?xml version="1.0"?>
<!-- Stylesheet for converting JavaNCSS output to html for only exceptions. -->
<!-- This produces a much leaner report that can be used in continuous builds and identifying code issues -->
<!-- Modified from original JavaNCSS stylesheet by Rajesh Raheja mailto:Rajesh.Raheja@oracle.com -->
<!-- This template prints out only violations / parameters exceeded standards -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html"/>

  <xsl:param name="projectName">Project</xsl:param>
  <xsl:param name="ncssPerFuncMax">100</xsl:param>
  <xsl:param name="ccnPerFuncMax">25</xsl:param>
  <xsl:param name="classPerPkgMax">50</xsl:param>
  <xsl:param name="funcPerClassMax">50</xsl:param>
  <xsl:param name="jvdcPerFuncMin">1</xsl:param>
  <xsl:param name="buildLabel">Build Not Identified</xsl:param>

  <xsl:variable name="ncssMax"  select="$ncssPerFuncMax"  />
  <xsl:variable name="ccnMax"   select="$ccnPerFuncMax"   />
  <xsl:variable name="classMax" select="$classPerPkgMax"  />
  <xsl:variable name="funcMax"  select="$funcPerClassMax" />
  <xsl:variable name="jvdcMin"  select="$jvdcPerFuncMin"  />

  <xsl:template match="/">
    <html>
      <head>
        <title>JavaNCSS Analysis for <xsl:value-of select="$projectName"/> Java Source (Exceptions)</title>
        <style type="text/css">
          body {
          font:normal 68% verdana,arial,helvetica;
          color:#000000;
          }
          table tr td, tr th {
            font-size: 68%;
          }
          table.details tr th{
          font-weight: bold;
          text-align:left;
          background:#a6caf0;
          }
          table.details tr td{
          background:#eeeee0;
          }
          
          p {
          line-height:1.5em;
          margin-top:0.5em; margin-bottom:1.0em;
          margin-left:2em;
          margin-right:2em;
          }
          h1 {
          margin: 0px 0px 5px; font: 165% verdana,arial,helvetica
          }
          h2 {
          margin-top: 1em; margin-bottom: 0.5em; font: bold 125% verdana,arial,helvetica
          }
          h3 {
          margin-bottom: 0.5em; font: bold 115% verdana,arial,helvetica
          }
          h4 {
          margin-bottom: 0.5em; font: bold 100% verdana,arial,helvetica
          }
          h5 {
          margin-bottom: 0.5em; font: bold 100% verdana,arial,helvetica
          }
          h6 {
          margin-bottom: 0.5em; font: bold 100% verdana,arial,helvetica
          }
          .Error {
          font-weight:bold; color:red;
          }
          .Failure {
          font-weight:bold; color:purple;
          }
          .Properties {
          text-align:right;
          }
        </style>
      </head>  
      <body>
        <h1>
        <a name="top">JavaNCSS Analysis for <xsl:value-of select="$projectName"/> Java Source (Exceptions)</a>
        </h1>
        <p align="right">Build <xsl:value-of select="$buildLabel"/></p>
        Quick Links: <a href="#Packages">Packages</a> &#160; <a href="#Objects">Objects</a> &#160; <a href="#Functions">Functions</a><!-- &#160; <a href="javancss_full.html">See Full Report</a>-->
        <hr size="2"/>
        <xsl:apply-templates/>
        <hr size="2"/>
        <h6>Report generated using <a href="http://www.kclee.com/clemens/java/javancss/">JavaNCSS</a> and <a href="http://jakarta.apache.org">Ant</a>.</h6>
      </body>
    </html>
  </xsl:template>

  <xsl:template match="packages">
    <h2><a name="Packages">Packages with more than <xsl:value-of select="$classMax"/> classes</a></h2>
    <table class="details" border="0" width="100%">
      <tr>
        <th>Nr.</th>
        <th>Classes</th>
        <th>Functions</th>
        <th>NCSS</th>
        <th>Javadocs</th>
        <th>Package</th>
      </tr>
      <xsl:apply-templates select="package[classes &gt; $classMax]"/>
      <tr>
        <td>&#160;</td>
        <td>&#160;</td>
        <td>&#160;</td>
        <td>&#160;</td>
        <td>&#160;</td>
        <td>&#160;</td>
      </tr>
      <tr>
        <td>&#160;</td>
        <td><xsl:value-of select="total/classes"/></td>  
        <td><xsl:value-of select="total/functions"/></td>  
        <td><xsl:value-of select="total/ncss"/></td>  
        <td><xsl:value-of select="total/javadocs"/></td>
        <td>Total</td>  
      </tr>
    </table>
    <p/>
    <xsl:apply-templates select="table"/>
  </xsl:template>

  <xsl:template match="package">
    <xsl:variable name="classes-color">
      <xsl:choose>
        <xsl:when test="classes &gt; $classMax">#ff0000</xsl:when>
        <xsl:otherwise>#000000</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <tr>
      <td><xsl:value-of select="position()"/></td>
      <td><font color="{$classes-color}"><xsl:value-of select="classes"/></font></td>
      <td><xsl:value-of select="functions"/></td>
      <td><xsl:value-of select="ncss"/></td>
      <td><xsl:value-of select="javadocs"/></td>
      <td><xsl:value-of select="name"/></td>
    </tr>
  </xsl:template>

  <xsl:template match="table">
    <table class="details" border="0" width="100%">
      <xsl:apply-templates select="tr"/>
    </table>
    <p/>  
  </xsl:template>

  <xsl:template match="tr">
    <xsl:variable name="row"><xsl:value-of select="position()"/></xsl:variable>
    <tr>
      <xsl:apply-templates select="td">
        <xsl:with-param name="row"><xsl:value-of select="$row"/></xsl:with-param>
      </xsl:apply-templates>
    </tr>
  </xsl:template>

  <xsl:template match="td">
    <xsl:param name="row" select="3"/>
    <xsl:choose>
      <xsl:when test="$row='1'">
        <th>
          <xsl:if test="position()=6">
            <xsl:text>|</xsl:text>
          </xsl:if>
          <xsl:value-of select="."/>&#160;
        </th>
      </xsl:when>
      <xsl:otherwise>
        <td>
          <xsl:if test="position()=6">
            <xsl:text>| </xsl:text>
          </xsl:if>
          <xsl:value-of select="."/>&#160;
        </td>      
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="objects">
    <h2><a name="Objects">Objects with more than <xsl:value-of select="$funcMax"/> functions</a></h2>
    <table class="details" border="0" width="100%">
      <tr>
        <th>Nr.</th>
        <th>NCSS</th>
        <th>Functions</th>
        <th>Classes</th>
        <th>Javadocs</th>
        <th>Class</th>
      </tr>
      <xsl:apply-templates select="object[functions &gt; $funcMax]"/>
      <tr>
        <td colspan="5">Average Object NCSS:</td>
        <td><xsl:value-of select="averages/ncss"/></td>
      </tr>
      <tr>
        <td colspan="5">Average Object Functions:</td>
        <td><xsl:value-of select="averages/functions"/></td>
      </tr>
      <tr>
        <td colspan="5">Average Object Inner Classes:</td>
        <td><xsl:value-of select="averages/classes"/></td>
      </tr>
      <tr>
        <td colspan="5">Average Object Javadoc Comments:</td>
        <td><xsl:value-of select="averages/javadocs"/></td>
      </tr>
      <tr>
        <td colspan="5">Program NCSS:</td>
        <td><xsl:value-of select="ncss"/></td>
      </tr>
    </table>
    <p/>  
  </xsl:template>

  <xsl:template match="object">
    <xsl:variable name="functions-color">
      <xsl:choose>
        <xsl:when test="functions &gt; $funcMax">#ff0000</xsl:when>
        <xsl:otherwise>#000000</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <tr>
      <td><xsl:value-of select="position()"/></td>
      <td><xsl:value-of select="ncss"/></td>
      <td><font color="{$functions-color}"><xsl:value-of select="functions"/></font></td>
      <td><xsl:value-of select="classes"/></td>
      <td><xsl:value-of select="javadocs"/></td>
      <td><xsl:value-of select="name"/></td>
    </tr>
  </xsl:template>

  <xsl:variable name="ncssMax"  select="$ncssPerFuncMax"  />
  <xsl:variable name="ccnMax"   select="$ccnPerFuncMax"   />
  <xsl:template match="functions">
    <h2><a name="Functions">Functions with more than <xsl:value-of select="$ncssMax"/> NCSS / <xsl:value-of select="$ccnMax"/> CCN</a></h2>
    <table class="details" border="0" width="100%">
      <tr>
        <th>Nr.</th>
        <th>NCSS</th>
        <th>CCN</th>
        <th>Javadoc</th>
        <th>Function</th>
      </tr>
      <xsl:apply-templates select="function[ncss &gt; $ncssMax] | function[ccn &gt; $ccnMax]"/>
      <tr>
        <td colspan="4">Average Function NCSS:</td>
        <td><xsl:value-of select="function_averages/ncss"/></td>
      </tr>
      <tr>
        <td colspan="4">Average Function CCN:</td>
        <td><xsl:value-of select="function_averages/ccn"/></td>
      </tr>
      <tr>
        <td colspan="4">Average Function Javadocs:</td>
        <td><xsl:value-of select="function_averages/javadocs"/></td>
      </tr>
      <tr>
        <td colspan="4">Program NCSS:</td>
        <td><xsl:value-of select="ncss"/></td>
      </tr>
    </table>
    <p/>
  </xsl:template>

  <xsl:template match="function">
    <xsl:variable name="ncss-color">
      <xsl:choose>
        <xsl:when test="ncss &gt; $ncssMax">#ff0000</xsl:when>
        <xsl:otherwise>#000000</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:variable name="ccn-color">
      <xsl:choose>
        <xsl:when test="ccn &gt; $ccnMax">#ff0000</xsl:when>
        <xsl:otherwise>#000000</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:variable name="jdocs-color">
      <xsl:choose>
        <xsl:when test="javadocs &lt; $jvdcMin">#ff0000</xsl:when>
        <xsl:otherwise>#000000</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <tr>
      <td><xsl:value-of select="position()"/></td>
      <td><font color="{$ncss-color}"><xsl:value-of select="ncss"/></font></td>
      <td><font color="{$ccn-color}"><xsl:value-of select="ccn"/></font></td>
      <td><font color="{$jdocs-color}"><xsl:value-of select="javadocs"/></font></td>
      <td><xsl:value-of select="name"/></td>
    </tr>
  </xsl:template>

  <xsl:template match="text()"/>

</xsl:stylesheet>
