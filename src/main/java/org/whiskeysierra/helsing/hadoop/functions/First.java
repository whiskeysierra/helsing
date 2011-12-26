package org.whiskeysierra.helsing.hadoop.functions;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import org.whiskeysierra.helsing.util.io.Line;

@AggregateFunction("FIRST")
final class First implements Aggregator {

    private Line first;

    @Override
    public void update(Line line) {
        first = Objects.firstNonNull(first, line);
    }

    @Override
    public String getResult() {
        return Iterables.getOnlyElement(first);
    }

}
