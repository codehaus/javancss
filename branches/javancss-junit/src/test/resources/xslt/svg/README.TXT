Thanks to Yves Coene <Yves.Coene@spacebel.be>
for these xslt stylesheets to produce svg diagrams out of javancss xml output.
I like them very much.
The svg files in this directory are example files.

Use e.g. batik as an svg viewer to see the content.

Generate your own with your favorite xslt transformer like:
javancss -xml -all -recursively java_source_dir > temp.xml
xslt -in temp.xml -xsl chart_XY.xsl > temp.svg
batik temp.svg

Chr. Clemens Lee, 2002-09-03.
