package org.whiskeysierra.helsing.hadoop.functions;

@AggregateFunction("MIN")
final class Minimum extends AbstractSingleArgumentNumericAggregator {

    private long min = Integer.MAX_VALUE;

    @Override
    public void update(Long value) {
        min = Math.min(min, value);
    }

    @Override
    public Long getResult() {
        return min;
    }

}
