#!/bin/sh

if [ ! -d hadoop ]; then
    rm -rvf hadoop

    wget http://apache.prosite.de//hadoop/common/hadoop-0.20.203.0/hadoop-0.20.203.0rc1.tar.gz || exit $?
    tar -xvzf hadoop-0.20.203.0rc1.tar.gz || exit $?
    mv hadoop-0.20.203.0 hadoop || exit $?
    rm -vf hadoop-0.20.203.0rc1.tar.gz || exit $?
fi

mvn install:install-file \
-Dfile=deps/com.inamik.utils.tableformatter-0.96.2.jar \
-DgroupId=com.inamik.utils \
-DartifactId=tableformatter \
-Dversion=0.96.2 \
-Dpackaging=jar || exit $?

mvn install:install-file \
-Dfile=deps/jsqlparser-0.7.0.jar \
-DgroupId=jsqlparser \
-DartifactId=jsqlparser \
-Dversion=0.7.0 \
-Dpackaging=jar || exit $?
