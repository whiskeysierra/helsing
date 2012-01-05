# Helsing - A micro-prototype Tenzing clone for Hadoop

## Introduction
This project is part of an assignment for *Programming - Advanced Topics* at the [*Beuth University of Applied Sciences Berlin*](http://www.beuth-hochschule.de/).
The first steps included reading, describing, reflecting and rating the Google paper
[*Tenzing - A SQL Implementation On The MapReduce Framework*](http://research.google.com/pubs/pub37200.html).

## Installation

### Requirements
- Linux (or any posix compliant unix)
- JDK 7
- For installing JDK 7 on Mac OSX Lion follow [these](http://code.google.com/p/openjdk-osx-build/) instructions
- Maven 3

### Hadoop
- Run `bin/install-hadoop.sh` to install Hadoop 0.20.203.0 to the `hadoop` directory

### Compilation
- Run `mvn clean package`
- Should produce two jar files
    - `target/helsing-<version>.jar`
    - `target/helsing-<version>-job.jar`

## Test drive
- Run `bin/run.sh`
- An interactive prompt should appear which allows sql statements to be entered
- The sample file shiped with this project ([`data/countries.csv`](https://github.com/whiskeysierra/helsing/blob/master/data/countries.csv)) has the following schema:

        +---------------------------+------------------+------+------------+
        |                   country |        continent | year | population |
        +---------------------------+------------------+------+------------+
        |               Afghanistan |             Asia | 1950 |    8150368 |
        |               Afghanistan |             Asia | 1960 |    9829450 |
        |               Afghanistan |             Asia | 1970 |   12430623 |
        |               Afghanistan |             Asia | 1980 |   15112149 |
        |               Afghanistan |             Asia | 1990 |   14669339 |
        |               Afghanistan |             Asia | 2000 |   23898198 |
        |               Afghanistan |             Asia | 2010 |   34504794 |
        |               Afghanistan |             Asia | 2020 |   44546287 |
        |               Afghanistan |             Asia | 2030 |   56322744 |
        |               Afghanistan |             Asia | 2040 |   69094697 |
        |               Afghanistan |             Asia | 2050 |   81933479 |
        |                   Albania |           Europe | 1950 |    1227156 |
        |                   Albania |           Europe | 1960 |    1623114 |
        |                   Albania |           Europe | 1970 |    2156612 |
        |                   Albania |           Europe | 1980 |    2671412 |
        |                   Albania |           Europe | 1990 |    3250778 |
        |                   Albania |           Europe | 2000 |    3473835 |
        |                   Albania |           Europe | 2010 |    3659616 |
        ...

### Currently the following features are supported:

#### Projection
    SELECT population, country FROM countries.csv
#### Grouping
    SELECT year FROM countries.csv GROUP BY year
#### Grouping with multiple columns
    SELECT year, continent FROM countries.csv GROUP BY continent, year
#### Aggregation
    SELECT MAX(population) FROM countries.csv
#### Aggregation with multiple functions
    SELECT MIN(population), MAX(population) FROM countries.csv
#### Aggregation with multi-column function
    SELECT CORR(year, population) FROM countries.csv
#### Grouping and aggregation
    SELECT year, SUM(population) FROM countries.csv GROUP BY year
#### Grouping and aggregation with multiple columns
    SELECT year, continent, SUM(population) FROM countries.csv GROUP BY year, continent
#### Grouping with multiple columns and aggregation with multiple functions
    SELECT year, continent, MIN(population), AVG(population), MAX(population) FROM countries.csv GROUP BY year, continent
#### Aggregate functions:
- `AVG`
- `COUNT`
- `FIRST`
- `LAST`
- `MAX`
- `MIN`
- `STDDEV`
- `SUM`
- `VAR`
- `COVAR`
- `CORR`

## Remarks

Sample data has been collected on [GeoHive](http://www.geohive.com/) and [The World Bank Open Data](http://data.worldbank.org/).

