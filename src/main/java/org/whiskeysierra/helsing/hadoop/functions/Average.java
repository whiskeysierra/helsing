package org.whiskeysierra.helsing.hadoop.functions;

@AggregateFunction("AVG")
final class Average extends LongAggregator {

    private long sum;
    private long count;

    @Override
    protected void update(long value) {
        sum += value;
        count++;
    }

    @Override
    protected long getLongResult() {
        return sum / count;
    }

}
