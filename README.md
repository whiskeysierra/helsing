
Tenzing Prototype on Hadoop
===========================

Installation
------------

# Requirements
- Linux (posix compliant unix)
- JDK 7
- Maven 3

# Maven Dependencies
- Run `bin/install.sh` to install special dependencies to local maven repository

# Hadoop
- Download Apache Hadoop from [here](http://apache.prosite.de//hadoop/common/hadoop-0.20.203.0/hadoop-0.20.203.0rc1.tar.gz)
- Extract content to `hadoop` directory

# Compilation
- `mvn clean package`

# Test drive
- Run `bin/run.sh`
- You should see a prompt
- Start entering sql statements
