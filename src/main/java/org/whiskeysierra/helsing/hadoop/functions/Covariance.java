package org.whiskeysierra.helsing.hadoop.functions;

import com.google.inject.Inject;

@AggregateFunction("COVAR")
final class Covariance extends TwoArgumentNumericAggregator {

    private final Sum sumOfProducts;
    private final Sum leftSum;
    private final Sum rightSum;
    private final Count count;

    @Inject
    public Covariance(Sum sumOfProducts, Sum leftSum, Sum rightSum, Count count) {
        this.sumOfProducts = sumOfProducts;
        this.leftSum = leftSum;
        this.rightSum = rightSum;
        this.count = count;
    }

    @Override
    public void update(Long left, Long right) {
        sumOfProducts.update(left * right);
        leftSum.update(left);
        rightSum.update(right);
        count.update();
    }

    @Override
    public Double getResult() {
        return ((double ) sumOfProducts.getResult() - leftSum.getResult() * rightSum.getResult() / count.getResult()) / count.getResult();
    }

}
