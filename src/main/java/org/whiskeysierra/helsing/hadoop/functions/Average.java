package org.whiskeysierra.helsing.hadoop.functions;

@AggregateFunction("AVG")
final class Average extends AbstractSingleArgumentNumericAggregator {

    private long sum;
    private long count;

    @Override
    public void update(Long value) {
        sum += value;
        count++;
    }

    @Override
    public Long getResult() {
        return sum / count;
    }

}
