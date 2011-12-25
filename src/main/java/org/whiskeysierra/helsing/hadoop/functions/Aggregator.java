package org.whiskeysierra.helsing.hadoop.functions;

public interface Aggregator {

    void update(String value);

    String getResult();

}
