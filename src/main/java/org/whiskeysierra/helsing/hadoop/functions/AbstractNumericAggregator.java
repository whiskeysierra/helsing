package org.whiskeysierra.helsing.hadoop.functions;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.whiskeysierra.helsing.util.format.Line;

abstract class AbstractNumericAggregator implements NumericAggregator {

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

}
