package org.whiskeysierra.helsing.hadoop.functions;

@AggregateFunction("MIN")
final class Minimum extends LongAggregator {

    private long min = Integer.MAX_VALUE;

    @Override
    protected void update(long value) {
        min = Math.min(min, value);
    }

    @Override
    protected long getLongResult() {
        return min;
    }

}
