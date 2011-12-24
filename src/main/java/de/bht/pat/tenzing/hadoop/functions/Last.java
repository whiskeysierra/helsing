package de.bht.pat.tenzing.hadoop.functions;

import de.bht.pat.tenzing.hadoop.jobs.AggregateFunction;

@AggregateFunction("LAST")
final class Last implements Aggregator {

    private String last;

    @Override
    public void update(String value) {
        last = value;
    }

    @Override
    public String getResult() {
        return last;
    }

}
