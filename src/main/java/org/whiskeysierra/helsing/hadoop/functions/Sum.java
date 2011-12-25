package org.whiskeysierra.helsing.hadoop.functions;

@AggregateFunction("SUM")
final class Sum extends LongAggregator {

    private long sum;

    @Override
    protected void update(long value) {
        sum += value;
    }

    @Override
    protected long getLongResult() {
        return sum;
    }

}
