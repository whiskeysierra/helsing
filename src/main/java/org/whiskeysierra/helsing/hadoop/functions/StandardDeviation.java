package org.whiskeysierra.helsing.hadoop.functions;

@AggregateFunction("STDDEV")
final class StandardDeviation extends AbstractSingleArgumentNumericAggregator {

    private long sum;
    private long count;
    private long sumOfSquares;

    @Override
    public void update(Long value) {
        sum += value;
        sumOfSquares += value * value;
        count++;
    }

    @Override
    public Long getResult() {
        final long avg = sum / count;
        final long var = Math.abs(sumOfSquares - avg * sum) / count;
        return (long) Math.sqrt(var);
    }

}
