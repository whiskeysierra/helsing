package org.whiskeysierra.helsing.hadoop.functions;

import org.whiskeysierra.helsing.util.format.Line;

@AggregateFunction("COUNT")
final class Count implements Aggregator {

    private long count;

    @Override
    public void update(Line line) {
        count++;
    }

    @Override
    public Long getResult() {
        return count;
    }
}
