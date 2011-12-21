#!/bin/sh

mvn install:install-file \
-Dfile=deps/com.inamik.utils.tableformatter-0.96.2.jar \
-DgroupId=com.inamik.utils \
-DartifactId=tableformatter \
-Dversion=0.96.2 \
-Dpackaging=jar

mvn install:install-file \
-Dfile=deps/jsqlparser-0.7.0.jar \
-DgroupId=jsqlparser \
-DartifactId=jsqlparser \
-Dversion=0.7.0 \
-Dpackaging=jar
