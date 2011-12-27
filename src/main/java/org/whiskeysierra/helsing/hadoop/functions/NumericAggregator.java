package org.whiskeysierra.helsing.hadoop.functions;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.whiskeysierra.helsing.util.format.Line;

import java.util.List;

abstract class NumericAggregator implements Aggregator {

    private final Function<String,Long> toLong = new Function<String, Long>() {

        @Override
        public Long apply(String input) {
            return Long.valueOf(input);
        }

    };

    @Override
    public final void update(Line line) {
        update(Lists.transform(line, toLong));
    }

    public abstract void update(List<Long> values);

    @Override
    public abstract Number getResult();

}
