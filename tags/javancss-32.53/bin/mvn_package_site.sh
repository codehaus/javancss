#! /bin/sh

# assuming we are in javancss/trunk
# Chr. Clemens Lee
# 2009-02-07

VERSION=$1

ant clean
mvn clean package site

cd target
unzip -U javancss-$VERSION.zip
cd ..

ant jar

cp lib/javancss.jar target/javancss-$VERSION/lib/javancss.jar
chmod 644 target/javancss-$VERSION/lib/javancss.jar

cd target
rm javancss-$VERSION.zip
zip -r javancss-$VERSION.zip javancss-$VERSION
cd ..

ls -la target/javancss-$VERSION.zip
target/javancss-$VERSION/bin/javancss -version
