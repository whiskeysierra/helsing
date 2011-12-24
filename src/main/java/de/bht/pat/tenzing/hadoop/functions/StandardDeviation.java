package de.bht.pat.tenzing.hadoop.functions;

import de.bht.pat.tenzing.hadoop.jobs.AggregateFunction;

@AggregateFunction("STDDEV")
final class StandardDeviation extends LongAggregator {

    private long sum = 0;
    private long count = 0;
    private long squared = 0;

    @Override
    protected void update(long value) {
        sum += value;
        squared += value * value;
        count++;
    }

    @Override
    protected long getLongResult() {
        final long avg = sum / count;
        final long var = Math.abs(squared - avg * sum) / count;
        return (long) Math.sqrt(var);
    }

}
