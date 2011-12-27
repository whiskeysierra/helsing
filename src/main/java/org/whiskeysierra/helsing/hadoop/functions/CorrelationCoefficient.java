package org.whiskeysierra.helsing.hadoop.functions;

import com.google.inject.Inject;

import java.util.List;

@AggregateFunction("CORR")
final class CorrelationCoefficient extends AbstractNumericAggregator {

    private final Covariance covariance;
    private final StandardDeviation left;
    private final StandardDeviation right;

    @Inject
    public CorrelationCoefficient(Covariance covariance, StandardDeviation left, StandardDeviation right) {
        this.covariance = covariance;
        this.left = left;
        this.right = right;
    }

    @Override
    public void update(List<Long> values) {
        covariance.update(values);
        update(values.get(0), values.get(1));
    }

    private void update(Long left, Long right) {
        this.left.update(left);
        this.right.update(right);

    }

    @Override
    public Long getResult() {
        return covariance.getResult() / (left.getResult() * right.getResult());
    }

}
