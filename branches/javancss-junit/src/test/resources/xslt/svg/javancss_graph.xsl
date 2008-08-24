<?xml version="1.0"?>  
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<!-- Abstract : Style sheet to format JavaNCSS XML output as frequency tables -->
<!-- Requires : XT or IE5 + MSXML 3.0                                         -->
<!-- History  : 10/08/02 Y. Coene                                             -->
<!--                   Original.  Shows tables in IE5 and NS4, and VML bar    -->
<!--                   chart with IE5 only.                                   -->

<!-- to do: add for-each and sort to know max value .... -->


<!-- global variables -->

     <xsl:variable name="metric">
        ncss
     </xsl:variable>

     <xsl:variable name="last_value_ncss">
        <xsl:value-of select="4 * sum(javancss/functions/function/ncss) div count(javancss/functions/function)"/>
     </xsl:variable>

     <xsl:variable name="step_value_ncss">
        <xsl:value-of select="floor($last_value_ncss div 10)+1"/>
     </xsl:variable>

     <xsl:variable name="last_value_ccn">
        <xsl:value-of select="4 * sum(javancss/functions/function/ccn) div count(javancss/functions/function)"/>
     </xsl:variable>

     <xsl:variable name="step_value_ccn">
        <xsl:value-of select="floor($last_value_ccn div 10)+1"/>
     </xsl:variable>


<xsl:template match="/">
  <html xmlns:v="urn:schemas-microsoft-com:vml"
        xmlns:o="urn:schemas-microsoft-com:office:office"
        xmlns="http://www.w3.org/TR/REC-html40">

  <head>
  <xsl:text disable-output-escaping="yes">
    &lt;!--[if !mso]&gt; 
  &lt;style&gt;
  v\:* {behavior:url(#default#VML);}
  o\:* {behavior:url(#default#VML);}
  .shape {behavior:url(#default#VML);}
  &lt;/style&gt;
  &lt;![endif]--&gt;
  
  </xsl:text>


    <title>JavaNCSS Analysis</title>
    </head>
    <body>
    <table width="100%">
        <tr>
        <td><h3>JavaNCSS Analysis</h3></td>
        <td>
        <p align="right">Designed for use with <a href="http://www.kclee.com/clemens/java/javancss/">JavaNCSS</a>.</p>
        </td>
        </tr>        
    </table>
    <hr size="2"/><p>

    <!-- table with distribution of ncss metric -->
    <table>
       <tr><td><b>NCSS</b></td><td><b>Functions</b></td><td><b>Percent</b></td></tr>
       <xsl:call-template name="LOOP_NCSS">
          <xsl:with-param name="n">0</xsl:with-param>
          <xsl:with-param name="last">10</xsl:with-param>
       </xsl:call-template>

    </table>

    <!-- table with distribution of ccn metric -->
    <table>
       <tr><td><b>CCN</b></td><td><b>Functions</b></td><td><b>Percent</b></td></tr>
       <xsl:call-template name="LOOP_CCN">
          <xsl:with-param name="n">0</xsl:with-param>
          <xsl:with-param name="last">10</xsl:with-param>
       </xsl:call-template>
    </table>

    </p>
    </body>
    </html>
</xsl:template>


<!-- named templates to simplify ...  Use recursion to implement loop  -->

<xsl:template name="ITEM">
   <xsl:param name="x">0</xsl:param>
   <xsl:param name="y">0</xsl:param>
   <xsl:param name="p">0</xsl:param>

   <tr><td><xsl:value-of select="$x"/></td>
   <td><p align="right"><xsl:value-of select="$y"/></p></td>
   <td><p align="right"><xsl:value-of select="format-number($p,'0.00%')"/></p></td>
   <td>
   <xsl:text disable-output-escaping="yes">
        &lt;v:rect style='width:
   </xsl:text>

    <xsl:value-of select="format-number($p*400,'0')"/>
    <xsl:text disable-output-escaping="yes">
        pt;height:8pt'fillcolor="red" &gt;&lt;/v:rect&gt;
    </xsl:text>
   </td>
   </tr>
</xsl:template>


<xsl:template name="LOOP_NCSS">
    <xsl:param name="n">0</xsl:param>
    <xsl:param name="last">0</xsl:param>

    <xsl:variable name="cnt">
        <xsl:choose>
        <xsl:when test="$n &lt; $last">
            <xsl:value-of select="count(javancss/functions/function[ncss >= $step_value_ncss*$n and ncss &lt; $step_value_ncss*($n+1) ])"/>
        </xsl:when>
        <xsl:otherwise>
            <xsl:value-of select="count(javancss/functions/function[ncss >= $step_value_ncss*$n])"/>
        </xsl:otherwise>
        </xsl:choose>
     </xsl:variable>

     <xsl:call-template name="ITEM">
          <xsl:with-param name="x"><xsl:value-of select="$step_value_ncss*$n"/></xsl:with-param>
          <xsl:with-param name="y"><xsl:value-of select="$cnt"/></xsl:with-param>
          <xsl:with-param name="p"><xsl:value-of select="$cnt div count(javancss/functions/function)"/></xsl:with-param>
     </xsl:call-template>

     <xsl:if test="$n &lt; $last">
        <xsl:call-template name="LOOP_NCSS">
            <xsl:with-param name="n">
              <xsl:value-of select="$n+1"/>
            </xsl:with-param>
            <xsl:with-param name="last">
              <xsl:value-of select="$last"/>
            </xsl:with-param>
        </xsl:call-template>
     </xsl:if>  

</xsl:template>


<xsl:template name="LOOP_CCN">
    <xsl:param name="n">0</xsl:param>
    <xsl:param name="last">0</xsl:param>

    <xsl:variable name="cnt">
        <xsl:choose>
        <xsl:when test="$n &lt; $last">
            <xsl:value-of select="count(javancss/functions/function[ccn >= $step_value_ccn*$n and ccn &lt; $step_value_ccn*($n+1) ])"/>
        </xsl:when>
        <xsl:otherwise>
            <xsl:value-of select="count(javancss/functions/function[ccn >= $step_value_ccn*$n])"/>
        </xsl:otherwise>
        </xsl:choose>
     </xsl:variable>

     <xsl:call-template name="ITEM">
          <xsl:with-param name="x"><xsl:value-of select="$step_value_ccn*$n"/></xsl:with-param>
          <xsl:with-param name="y"><xsl:value-of select="$cnt"/></xsl:with-param>
          <xsl:with-param name="p"><xsl:value-of select="$cnt div count(javancss/functions/function)"/></xsl:with-param>
     </xsl:call-template>

     <xsl:if test="$n &lt; $last">
        <xsl:call-template name="LOOP_CCN">
            <xsl:with-param name="n">
              <xsl:value-of select="$n+1"/>
            </xsl:with-param>
            <xsl:with-param name="last">
              <xsl:value-of select="$last"/>
            </xsl:with-param>
        </xsl:call-template>
     </xsl:if>  
</xsl:template>

</xsl:stylesheet> 