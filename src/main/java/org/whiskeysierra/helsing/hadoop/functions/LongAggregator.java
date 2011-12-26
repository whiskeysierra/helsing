package org.whiskeysierra.helsing.hadoop.functions;

import com.google.common.collect.Iterables;
import org.whiskeysierra.helsing.util.io.Line;

abstract class LongAggregator implements Aggregator {

    protected abstract void update(long value);

    @Override
    public final void update(Line line) {
        update(Long.parseLong(Iterables.getOnlyElement(line)));
    }

    protected abstract long getLongResult();

    @Override
    public final String getResult() {
        return Long.toString(getLongResult());
    }

}
