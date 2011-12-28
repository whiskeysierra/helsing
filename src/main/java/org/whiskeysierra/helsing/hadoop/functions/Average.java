package org.whiskeysierra.helsing.hadoop.functions;

import com.google.inject.Inject;

@AggregateFunction("AVG")
final class Average extends SingleArgumentNumericAggregator {

    private final Sum sum;
    private final Count count;

    @Inject
    public Average(Sum sum, Count count) {
        this.sum = sum;
        this.count = count;
    }

    @Override
    public void update(Long value) {
        sum.update(value);
        count.update();
    }

    @Override
    public Double getResult() {
        return (double) sum.getResult() / (double) count.getResult();
    }

}
