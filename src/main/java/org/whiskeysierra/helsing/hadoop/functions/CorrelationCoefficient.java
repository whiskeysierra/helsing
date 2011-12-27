package org.whiskeysierra.helsing.hadoop.functions;

import com.google.inject.Inject;

@AggregateFunction("CORR")
final class CorrelationCoefficient extends TwoArgumentNumericAggregator {

    private final Covariance covariance;
    private final StandardDeviation leftDeviation;
    private final StandardDeviation rightDeviation;

    @Inject
    public CorrelationCoefficient(Covariance covariance,
        StandardDeviation leftDeviation, StandardDeviation rightDeviation) {

        this.covariance = covariance;
        this.leftDeviation = leftDeviation;
        this.rightDeviation = rightDeviation;
    }

    @Override
    public void update(Long left, Long right) {
        covariance.update(left, right);
        leftDeviation.update(left);
        rightDeviation.update(right);
    }

    @Override
    public Long getResult() {
        return covariance.getResult() / (leftDeviation.getResult() * rightDeviation.getResult());
    }

}
