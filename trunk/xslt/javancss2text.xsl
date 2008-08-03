<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
    xmlns:java="http://xml.apache.org/xslt/java"
    exclude-result-prefixes="java">
 
  <xsl:output method="text"/>

  <xsl:template match="packages">
    <xsl:value-of select="java:ccl.util.Util.paddWithSpace('Nr.',java:java.lang.Math.max(string-length(count(package)),3))"/>
    <xsl:text>   Classes Functions      NCSS  Javadocs Package
</xsl:text>

    <xsl:apply-templates select="package"/>

    <xsl:value-of select="java:ccl.util.Util.paddWithSpace('   ',java:java.lang.Math.max(string-length(count(package)),3))"/>
    <xsl:text> --------- --------- --------- ---------
</xsl:text>

    <xsl:value-of select="java:ccl.util.Util.paddWithSpace('   ',java:java.lang.Math.max(string-length(count(package)),4))"/>

    <xsl:value-of select="Util:paddWithSpace(total/classes,9)" xmlns:Util="xalan://ccl.util.Util"/>
    <xsl:text> </xsl:text>
    <xsl:value-of select="Util:paddWithSpace(total/functions,9)" xmlns:Util="xalan://ccl.util.Util"/>
    <xsl:text> </xsl:text>
    <xsl:value-of select="Util:paddWithSpace(total/ncss,9)" xmlns:Util="xalan://ccl.util.Util"/>
    <xsl:text> </xsl:text>
    <xsl:value-of select="Util:paddWithSpace(total/javadocs,9)" xmlns:Util="xalan://ccl.util.Util"/>
    <xsl:text> Total

</xsl:text>

    <xsl:apply-templates select="table"/>

    <xsl:text>
</xsl:text>


  </xsl:template>

  <xsl:template match="package">
    <xsl:value-of select="Util:paddWithSpace(substring-before(Util:dtoa(position()),'.0'),java:java.lang.Math.max(string-length(count(../package)),3))" xmlns:Util="xalan://ccl.util.Util" xmlns:String="xalan://java.lang.String" xmlns:Math="xalan://java.lang.Math"/>
    <xsl:text> </xsl:text>
    <xsl:value-of select="Util:paddWithSpace(classes,9)" xmlns:Util="xalan://ccl.util.Util"/>
    <xsl:text> </xsl:text>
    <xsl:value-of select="Util:paddWithSpace(functions,9)" xmlns:Util="xalan://ccl.util.Util"/>
    <xsl:text> </xsl:text>
    <xsl:value-of select="Util:paddWithSpace(ncss,9)" xmlns:Util="xalan://ccl.util.Util"/>
    <xsl:text> </xsl:text>
    <xsl:value-of select="Util:paddWithSpace(javadocs,9)" xmlns:Util="xalan://ccl.util.Util"/>
    <xsl:text> </xsl:text>
    <xsl:value-of select="name"/>
    <xsl:text>
</xsl:text>
  </xsl:template>

  <xsl:template match="table">
    <xsl:apply-templates select="tr[position()=1]"/>
    <xsl:text>-------------------------------------------------------------
</xsl:text>
    <xsl:apply-templates select="tr[position()!=1]"/>
  </xsl:template>

  <xsl:template match="tr">
    <xsl:apply-templates select="td[position()!=6]"/>
    <xsl:text>| </xsl:text>
    <xsl:value-of select="td[position()=6]"/>
    <xsl:text>
</xsl:text>
  </xsl:template>

  <xsl:template match="td">
    <xsl:value-of select="Util:paddWithSpace(.,9)" xmlns:Util="xalan://ccl.util.Util"/>
    <xsl:text> </xsl:text>
  </xsl:template>

  <xsl:template match="objects">
    <xsl:value-of select="java:ccl.util.Util.paddWithSpace('Nr.',java:java.lang.Math.max(string-length(count(object)),3))" xmlns:Util="xalan://ccl.util.Util" xmlns:String="xalan://java.lang.String" xmlns:Math="xalan://java.lang.Math"/>
    <xsl:text> NCSS Functions Classes Javadocs Class
</xsl:text>
    <xsl:apply-templates select="object"/>
    <xsl:text>Average Object NCSS:             </xsl:text>
    <xsl:value-of select="Util:paddWithSpace(averages/ncss,9)" xmlns:Util="xalan://ccl.util.Util"/>
    <xsl:text>
Average Object Functions:        </xsl:text>
    <xsl:value-of select="Util:paddWithSpace(averages/functions,9)" xmlns:Util="xalan://ccl.util.Util"/>
    <xsl:text>
Average Object Inner Classes:    </xsl:text>
    <xsl:value-of select="Util:paddWithSpace(averages/classes,9)" xmlns:Util="xalan://ccl.util.Util"/>
    <xsl:text>
Average Object Javadoc Comments: </xsl:text>
    <xsl:value-of select="Util:paddWithSpace(averages/javadocs,9)" xmlns:Util="xalan://ccl.util.Util"/>
    <xsl:text>
Program NCSS:                    </xsl:text>
    <xsl:value-of select="Util:paddWithSpace(ncss,9)" xmlns:Util="xalan://ccl.util.Util"/>
    <xsl:text>

</xsl:text>
  </xsl:template>

  <xsl:template match="object">
    <xsl:value-of select="java:ccl.util.Util.paddWithSpace(substring-before(java:ccl.util.Util.dtoa(position()),'.0'),java:java.lang.Math.max(string-length(count(../object)),3))"/>
    <xsl:text> </xsl:text>
    <xsl:value-of select="Util:paddWithSpace(ncss,4)" xmlns:Util="xalan://ccl.util.Util"/>
    <xsl:text> </xsl:text>
    <xsl:value-of select="Util:paddWithSpace(functions,9)" xmlns:Util="xalan://ccl.util.Util"/>
    <xsl:text> </xsl:text>
    <xsl:value-of select="Util:paddWithSpace(classes,7)" xmlns:Util="xalan://ccl.util.Util"/>
    <xsl:text> </xsl:text>
    <xsl:value-of select="Util:paddWithSpace(javadocs,8)" xmlns:Util="xalan://ccl.util.Util"/>
    <xsl:text> </xsl:text>
    <xsl:value-of select="name"/>
    <xsl:text>
</xsl:text>    
  </xsl:template>

  <xsl:template match="functions">
    <xsl:value-of select="java:ccl.util.Util.paddWithSpace('Nr.',java:java.lang.Math.max(string-length(count(function)),3))"/>
    <xsl:text> NCSS CCN JVDC Function
</xsl:text>
    <xsl:apply-templates select="function"/>
    <xsl:text>Average Function NCSS: </xsl:text>
    <xsl:value-of select="Util:paddWithSpace(function_averages/ncss,10)" xmlns:Util="xalan://ccl.util.Util"/>
    <xsl:text>
Average Function CCN:  </xsl:text>
    <xsl:value-of select="Util:paddWithSpace(function_averages/ccn,10)" xmlns:Util="xalan://ccl.util.Util"/>
    <xsl:text>
Average Function JVDC: </xsl:text>
    <xsl:value-of select="Util:paddWithSpace(function_averages/javadocs,10)" xmlns:Util="xalan://ccl.util.Util"/>
    <xsl:text>
Program NCSS:          </xsl:text>
    <xsl:value-of select="Util:paddWithSpace(ncss,10)" xmlns:Util="xalan://ccl.util.Util"/>
    <xsl:text>
</xsl:text>
  </xsl:template>

  <xsl:template match="function">
    <xsl:value-of select="java:ccl.util.Util.paddWithSpace(substring-before(java:ccl.util.Util.dtoa(position()),'.0'),java:java.lang.Math.max(string-length(count(../function)),3))"/>
    <xsl:text> </xsl:text>
    <xsl:value-of select="Util:paddWithSpace(ncss,4)" xmlns:Util="xalan://ccl.util.Util"/>
    <xsl:text> </xsl:text>
    <xsl:value-of select="Util:paddWithSpace(ccn,3)" xmlns:Util="xalan://ccl.util.Util"/>
    <xsl:text> </xsl:text>
    <xsl:value-of select="Util:paddWithSpace(javadocs,4)" xmlns:Util="xalan://ccl.util.Util"/>
    <xsl:text> </xsl:text>
    <xsl:value-of select="name"/>
    <xsl:text>
</xsl:text>    
  </xsl:template>

  <xsl:template match="text()"/>

</xsl:stylesheet>
