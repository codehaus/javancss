<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="text"/>

  <xsl:template match="/">
    <xsl:apply-templates select="javancss"/>
  </xsl:template>

  <xsl:template match="javancss">
	 <xsl:value-of select="ncss"/>
  </xsl:template>

  <xsl:template match="text()"/>

</xsl:stylesheet>
