package org.whiskeysierra.helsing.hadoop.functions;

import com.google.common.collect.Iterables;

import java.util.List;

abstract class AbstractSingleArgumentNumericAggregator extends AbstractNumericAggregator
    implements SingleNumericAggregator {

    @Override
    public final void update(List<Long> values) {
        update(Iterables.getOnlyElement(values));
    }

}
