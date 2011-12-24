package de.bht.pat.tenzing.hadoop.functions;

@AggregateFunction("STDDEV")
final class StandardDeviation extends LongAggregator {

    private long sum = 0;
    private long count = 0;
    private long sumOfSquares = 0;

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
