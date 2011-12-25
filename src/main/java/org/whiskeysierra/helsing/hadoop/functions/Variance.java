package org.whiskeysierra.helsing.hadoop.functions;

@AggregateFunction("VAR")
final class Variance extends LongAggregator {

    private long sum = 0;
    private long count = 0;
    private long squared = 0;

    @Override
    protected void update(long value) {
        sum += value;
        squared += value * value;
        count++;
    }

    @Override
    protected long getLongResult() {
        final long avg = sum / count;
        return Math.abs(squared - avg * sum) / count;
    }

}
