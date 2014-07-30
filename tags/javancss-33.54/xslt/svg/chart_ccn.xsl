<?xml version="1.0"?>  
<!-- Abstract : Style sheet to present data generated by JavaNCSS as a        -->
<!--            frequency table in a bar chart in SVG format.                 -->
<!--            Range of metric on y-axis is 0 to 4*average.                  -->
<!-- File     : chart_ccn.xsl                                                 -->
<!-- Usage    : xt javancss_out.xml chart_ccn.xsl chart_ccn.svg               -->
<!-- Requires : XT and SVG plugin from www.adobe.com/svg                      -->
<!-- Input    : XML output of JavaNCSS tool.                                  -->
<!-- Output   : Frequency chart METRIC v UNIT in SVG format.                  --> 
<!-- History  : 16/08/02 Y. Coene (yves.coene13@yucom.be)                     -->
<!--                   Original.  Shows table in SVG format.                  -->

<!DOCTYPE BARCHART [
  <!ENTITY UNIT        "javancss/functions/function">
  <!ENTITY METRIC      "javancss/functions/function/ccn">
  <!ENTITY SHORTMETRIC "ccn">
]>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">


<!-- global variables -->

     <xsl:variable name="title1">Distribution Cyclomatic</xsl:variable>
     <xsl:variable name="title2">Complexity per Function (CCN)</xsl:variable>
     <xsl:variable name="labelx">Functions</xsl:variable>
     <xsl:variable name="labely">Cyclomatic Complexity</xsl:variable>

     <xsl:variable name="dx">   <!-- pixels between ticks on x axis = 10*dx -->
        35
     </xsl:variable>
     <xsl:variable name="dy">   <!-- pixels between ticks on y axis = dy    -->
        220
     </xsl:variable>
     <xsl:variable name="x0">   <!-- x offset of origin -->
        464
     </xsl:variable>

     <xsl:variable name="last_value">
        <xsl:value-of select="4 * sum(&METRIC;) div count(&UNIT;)"/>
     </xsl:variable>

     <xsl:variable name="step_value">
        <xsl:value-of select="floor($last_value div 10)+1"/>
     </xsl:variable>


<xsl:template match="/">
  <xsl:text disable-output-escaping="yes">&lt;!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 1.0//EN" "http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd"&gt;
</xsl:text>

 <svg width="436pt" height="327pt" viewBox="0 0 4360 3270">
 <rect x="40" y="30" width="4320" height="3240" style="fill:silver;"/>
 <rect x="3" y="3" width="4314" height="3234" style="fill:white; stroke-width:6; stroke:black"/>
 <g style="fill:#FFFFFF; stroke-width:15; stroke:black">
     <rect x="40" y="40" width="4240" height="3160"/>
 </g>

 <g style="fill:#000080; font-family:Times New Roman;font-style:italic; font-size:180; text-anchor:end;">
     <text id="Heading" x="4100" y="256"><xsl:value-of select="$title1"/></text>
     <text x="4100" y="456"><xsl:value-of select="$title2"/></text>
 </g>

 <g transform="translate(190 640) rotate(-90)" style="fill:#008080; font-family:Times New Roman;font-size:100; text-anchor:end; ">
     <text><xsl:value-of select="$labely"/></text>
 </g>
 <g style="fill:#008000; font-family:Arial;font-size:100;">
 <text id="Notes" x="2535" y="1080">Average <xsl:value-of select="$labely"/> = 
       <xsl:value-of select="format-number(sum(&METRIC;) div count(&UNIT;),'0.00')"/>
 </text>
 <text x="2535" y="1200">Total <xsl:value-of select="$labelx"/> = 
      <xsl:value-of select="count(&UNIT;)"/>
 </text>
 <text x="2535" y="1320">Total <xsl:value-of select="$labely"/> = 
      <xsl:value-of select="sum(&METRIC;)"/>
 </text>

 </g>
 <text x="4100" y="3110" style="fill:#008080; font-family:Times New Roman;font-size:100; text-anchor:end;"><xsl:value-of select="$labelx"/></text>

 <g style="font-family:Times New Roman;font-size:80; text-anchor:middle;">
  <text x="{($x0)+0}" y="2970"> 0%</text>
  <text x="{$x0+10*$dx}" y="2970">10%</text>
  <text x="{$x0+20*$dx}" y="2970">20%</text>
  <text x="{$x0+30*$dx}" y="2970">30%</text>
  <text x="{$x0+40*$dx}" y="2970">40%</text>
  <text x="{$x0+50*$dx}" y="2970">50%</text>
  <text x="{$x0+60*$dx}" y="2970">60%</text>
  <text x="{$x0+70*$dx}" y="2970">70%</text>
  <text x="{$x0+80*$dx}" y="2970">80%</text>
  <text x="{$x0+90*$dx}" y="2970">90%</text>
  <text x="{$x0+100*$dx}" y="2970">100%</text>
 </g>

 <!-- x-axis with ticks -->
 <path style="stroke:#000000; stroke-width:6" d="M {($x0)+0},2842 L {($x0)+0},2881 z M {($x0)+10*$dx},2842 L {($x0)+10*$dx},2881 z M {($x0)+20*$dx},2842 L {$x0+20*$dx},2881 z M {$x0+30*$dx},2842 L {$x0+30*$dx},2881 z M {$x0+40*$dx},2842 L {$x0+40*$dx},2881 z M {$x0+50*$dx},2842 L {$x0+50*$dx},2881 z M {$x0+60*$dx},2842 L {$x0+60*$dx},2881 z M {$x0+70*$dx},2842 L {$x0+70*$dx},2881 z M {$x0+80*$dx},2842 L {$x0+80*$dx},2881 z M {$x0+90*$dx},2842 L {$x0+90*$dx},2881 z M {$x0+100*$dx},2842 L {$x0+100*$dx},2881 z  M {$x0+100*$dx},2842 L {$x0+0*$dx},2842 z"/>

 <!-- y-axis without ticks -->
 <path style="stroke:#000000; stroke-width:6" d="M {($x0)+0},2842 L {($x0)+0},300 z"/>

       <xsl:call-template name="LOOP">
          <xsl:with-param name="n">0</xsl:with-param>
          <xsl:with-param name="last">10</xsl:with-param>  <!-- defines number of horizontal bars -->
       </xsl:call-template>

  </svg>
</xsl:template>


<!-- named templates to simplify ...  Use recursion to implement loop  -->

<xsl:template name="ITEM">
   <xsl:param name="i">0</xsl:param>
   <xsl:param name="x">0</xsl:param>
   <xsl:param name="y">0</xsl:param>
   <xsl:param name="p">0</xsl:param>

   <!-- tick on vertical axis -->
   <path style="stroke:#000000; stroke-width:6" d="M {($x0)-30},{2842-$i*$dy} L {($x0)-1},{2842-$i*$dy} z"/>

   <!-- label on vertical axis -->
   <g style="font-family:Times New Roman;font-size:80; text-anchor:end;">
       <text x='{($x0)-40}' y='{2842-$i*$dy}'>
           <xsl:value-of select="$x"/>
       </text>
   </g>

   <!-- rectangle representing frequency (0 to 100)-->
   <g style="fill:#DBDBED;stroke-width:4; stroke:black">
       <rect x="{($x0)+0}" y='{2842-$i*($dy)-176-24}' width="{format-number($p*$dx*100,'0')}" height="{($dy)-44}"/>
   </g>
</xsl:template>


<xsl:template name="LOOP">
    <xsl:param name="n">0</xsl:param>
    <xsl:param name="last">0</xsl:param>

    <xsl:variable name="cnt">
        <xsl:choose>
        <xsl:when test="$n &lt; $last">
            <xsl:value-of select="count(&UNIT;[&SHORTMETRIC; >= $step_value*$n and &SHORTMETRIC; &lt; $step_value*($n+1) ])"/>
        </xsl:when>
        <xsl:otherwise>
            <xsl:value-of select="count(&UNIT;[&SHORTMETRIC; >= $step_value*$n])"/>
        </xsl:otherwise>
        </xsl:choose>
     </xsl:variable>

     <xsl:call-template name="ITEM">
          <xsl:with-param name="i"><xsl:value-of select="$n"/></xsl:with-param>
          <xsl:with-param name="x"><xsl:value-of select="$step_value*$n"/></xsl:with-param>
          <xsl:with-param name="y"><xsl:value-of select="$cnt"/></xsl:with-param>
          <xsl:with-param name="p"><xsl:value-of select="$cnt div count(&UNIT;)"/></xsl:with-param>
     </xsl:call-template>

     <xsl:if test="$n &lt; $last">
        <xsl:call-template name="LOOP">
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