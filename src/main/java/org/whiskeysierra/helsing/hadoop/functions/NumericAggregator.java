package org.whiskeysierra.helsing.hadoop.functions;

import java.util.List;

interface NumericAggregator extends Aggregator {

    void update(List<Long> values);

    @Override
    Long getResult();

}
