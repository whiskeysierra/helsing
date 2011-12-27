package org.whiskeysierra.helsing.hadoop.functions;

import java.util.List;

@AggregateFunction("COVAR")
final class Covariance extends AbstractNumericAggregator {

    private long sumOfProducts;
    private long leftSum;
    private long rightSum;
    private long count;

    @Override
    public void update(List<Long> values) {
        update(values.get(0), values.get(1));
    }

    private void update(Long left, Long right) {
        sumOfProducts += left * right;
        leftSum += left;
        rightSum += right;
        count++;
    }

    @Override
    public Long getResult() {
        return (sumOfProducts - leftSum * rightSum / count) / count;
    }

}
