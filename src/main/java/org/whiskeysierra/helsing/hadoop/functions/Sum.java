package org.whiskeysierra.helsing.hadoop.functions;

@AggregateFunction("SUM")
final class Sum extends SingleArgumentNumericAggregator {

    private long sum;

    @Override
    public void update(Long value) {
        sum += value;
    }

    @Override
    public Long getResult() {
        return sum;
    }

}
