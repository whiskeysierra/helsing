package org.whiskeysierra.helsing.hadoop.functions;

import com.google.inject.Inject;

@AggregateFunction("VAR")
final class Variance extends SingleArgumentNumericAggregator {

    private final Average average;
    private final Sum sum;
    private final Count count;
    private final Sum sumOfSquares;

    @Inject
    public Variance(Average average, Sum sum, Count count, Sum sumOfSquares) {
        this.sum = sum;
        this.count = count;
        this.sumOfSquares = sumOfSquares;
        this.average = average;
    }

    @Override
    public void update(Long value) {
        average.update(value);
        sum.update(value);
        sumOfSquares.update(value * value);
        count.update();
    }

    @Override
    public Double getResult() {
        return Math.abs(sumOfSquares.getResult() - average.getResult() * sum.getResult()) / count.getResult();
    }

}
