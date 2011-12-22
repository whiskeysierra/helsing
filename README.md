#Tenzing Prototype on Hadoop

## Installation

### Requirements
- Linux (or any posix compliant unix)
- JDK 7
- For installing JDK 7 on Mac OSX Lion follow [these](http://code.google.com/p/openjdk-osx-build/) instructions
- Maven 3

### Maven Dependencies
- Run `bin/install.sh` to install special dependencies to local maven repository and hadoop distribution to the `hadoop` directory

### Compilation
- `mvn clean package`

## Test drive
- Run `bin/run.sh`
