package org.whiskeysierra.helsing.hadoop.functions;

import java.util.List;

abstract class TwoArgumentNumericAggregator extends NumericAggregator {

    public abstract void update(Long left, Long right);

    @Override
    public final void update(List<Long> values) {
        update(values.get(0), values.get(1));
    }

}
