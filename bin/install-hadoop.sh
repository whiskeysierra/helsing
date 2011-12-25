#!/bin/sh

if [ ! -d hadoop ]; then
    rm -rvf hadoop

    wget http://apache.prosite.de//hadoop/common/hadoop-0.20.203.0/hadoop-0.20.203.0rc1.tar.gz || exit $?
    tar -xvzf hadoop-0.20.203.0rc1.tar.gz || exit $?
    mv hadoop-0.20.203.0 hadoop || exit $?
    rm -vf hadoop-0.20.203.0rc1.tar.gz || exit $?
fi
