#! /bin/sh

# assuming we are in javancss/trunk
# Chr. Clemens Lee
# 2009-02-07

VERSION=$1

mvn clean
ant clean
mvn package
mvn site
cd target
unzip -U javancss-$VERSION.zip
chmod 755 javancss-$VERSION/bin/javancss
cd ..
ant jar
cp lib/javancss.jar target/javancss-$VERSION/lib/javancss.jar
chmod 644 target/javancss-$VERSION/lib/javancss.jar
rm target/javancss-$VERSION.zip
cd target
zip -r javancss-$VERSION.zip javancss-$VERSION
cd ..
ls -la target/javancss-$VERSION.zip
target/javancss-$VERSION/bin/javancss -version
