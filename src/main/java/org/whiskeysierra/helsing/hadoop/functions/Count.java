package org.whiskeysierra.helsing.hadoop.functions;

import org.whiskeysierra.helsing.util.io.Line;

@AggregateFunction("COUNT")
final class Count implements Aggregator {

    private long count;

    @Override
    public void update(Line line) {
        count++;
    }

    @Override
    public String getResult() {
        return Long.toString(count);
    }
}
