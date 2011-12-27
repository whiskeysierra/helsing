package org.whiskeysierra.helsing.hadoop.functions;

@AggregateFunction("VAR")
final class Variance extends AbstractSingleArgumentNumericAggregator {

    private long sum = 0;
    private long count = 0;
    private long squared = 0;

    @Override
    public void update(Long value) {
        sum += value;
        squared += value * value;
        count++;
    }

    @Override
    public Long getResult() {
        final long avg = sum / count;
        return Math.abs(squared - avg * sum) / count;
    }

}
