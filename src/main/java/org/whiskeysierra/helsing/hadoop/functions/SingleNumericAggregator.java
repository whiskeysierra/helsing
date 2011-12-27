package org.whiskeysierra.helsing.hadoop.functions;

interface SingleNumericAggregator extends Aggregator {

    void update(Long value);

}
