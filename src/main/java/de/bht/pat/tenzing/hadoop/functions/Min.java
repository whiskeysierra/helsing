package de.bht.pat.tenzing.hadoop.functions;

import de.bht.pat.tenzing.hadoop.jobs.AggregateFunction;

@AggregateFunction("MIN")
final class Min extends LongAggregator {

    private long min = Integer.MAX_VALUE;

    @Override
    protected void update(long value) {
        min = Math.min(min, value);
    }

    @Override
    protected long getLongResult() {
        return min;
    }

}
