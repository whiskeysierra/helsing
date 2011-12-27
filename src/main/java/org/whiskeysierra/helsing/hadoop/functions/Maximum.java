package org.whiskeysierra.helsing.hadoop.functions;

@AggregateFunction("MAX")
final class Maximum extends SingleArgumentNumericAggregator {

    private long max = Integer.MIN_VALUE;

    @Override
    public void update(Long value) {
        max = Math.max(max, value);
    }

    @Override
    public Long getResult() {
        return max;
    }

}
