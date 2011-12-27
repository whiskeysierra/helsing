package org.whiskeysierra.helsing.hadoop.functions;

import com.google.common.collect.Iterables;

import java.util.List;

abstract class SingleArgumentNumericAggregator extends NumericAggregator {

    public abstract void update(Long value);

    @Override
    public final void update(List<Long> values) {
        update(Iterables.getOnlyElement(values));
    }

}
