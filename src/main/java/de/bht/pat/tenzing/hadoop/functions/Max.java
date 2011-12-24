package de.bht.pat.tenzing.hadoop.functions;

import de.bht.pat.tenzing.hadoop.jobs.AggregateFunction;

@AggregateFunction("MAX")
final class Max extends LongAggregator {

    private long max = Integer.MIN_VALUE;

    @Override
    protected void update(long value) {
        max = Math.max(max, value);
    }

    @Override
    protected long getLongResult() {
        return max;
    }

}
