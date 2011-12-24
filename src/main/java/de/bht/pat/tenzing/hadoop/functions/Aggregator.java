package de.bht.pat.tenzing.hadoop.functions;

interface Aggregator {

    void update(String value);

    String getResult();

}
