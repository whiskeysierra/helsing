package de.bht.pat.tenzing.hadoop.functions;

import de.bht.pat.tenzing.hadoop.jobs.AggregateFunction;

@AggregateFunction("COUNT")
final class Count implements Aggregator {

    private long count;

    @Override
    public void update(String value) {
        count++;
    }

    @Override
    public String getResult() {
        return Long.toString(count);
    }
}
