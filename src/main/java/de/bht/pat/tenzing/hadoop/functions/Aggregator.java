package de.bht.pat.tenzing.hadoop.functions;

public interface Aggregator {

    void update(String value);

    String getResult();

}
