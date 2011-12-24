package de.bht.pat.tenzing.hadoop.functions;

import de.bht.pat.tenzing.hadoop.jobs.AggregateFunction;

@AggregateFunction("AVG")
final class Average extends LongAggregator {

    private long sum;
    private long count;

    @Override
    protected void update(long value) {
        sum += value;
        count++;
    }

    @Override
    protected long getLongResult() {
        return sum / count;
    }

}
