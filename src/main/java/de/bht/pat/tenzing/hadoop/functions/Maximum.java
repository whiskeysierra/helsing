package de.bht.pat.tenzing.hadoop.functions;

@AggregateFunction("MAX")
final class Maximum extends LongAggregator {

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
