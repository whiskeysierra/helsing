package org.whiskeysierra.helsing.hadoop.functions;

import com.google.common.collect.Iterables;
import org.whiskeysierra.helsing.util.io.Line;

@AggregateFunction("LAST")
final class Last implements Aggregator {

    private Line last;

    @Override
    public void update(Line line) {
        last = line;
    }

    @Override
    public String getResult() {
        return Iterables.getOnlyElement(last);
    }

}
