package de.bht.pat.tenzing.hadoop.functions;

import com.google.common.base.Objects;
import de.bht.pat.tenzing.hadoop.jobs.AggregateFunction;

@AggregateFunction("FIRST")
final class First implements Aggregator {

    private String first;

    @Override
    public void update(String value) {
        first = Objects.firstNonNull(first, value);
    }

    @Override
    public String getResult() {
        return first;
    }

}
