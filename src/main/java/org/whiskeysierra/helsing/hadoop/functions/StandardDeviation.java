package org.whiskeysierra.helsing.hadoop.functions;

@AggregateFunction("STDDEV")
final class StandardDeviation extends LongAggregator {

    private long sum;
    private long count;
    private long sumOfSquares;

    @Override
    protected void update(long value) {
        sum += value;
        sumOfSquares += value * value;
        count++;
    }

    @Override
    protected long getLongResult() {
        final long avg = sum / count;
        final long var = Math.abs(sumOfSquares - avg * sum) / count;
        return (long) Math.sqrt(var);
    }

}
