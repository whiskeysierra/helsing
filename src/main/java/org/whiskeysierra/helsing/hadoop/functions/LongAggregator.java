package org.whiskeysierra.helsing.hadoop.functions;

abstract class LongAggregator implements Aggregator {

    protected abstract void update(long value);

    @Override
    public final void update(String value) {
        update(Long.parseLong(value));
    }

    protected abstract long getLongResult();

    @Override
    public final String getResult() {
        return Long.toString(getLongResult());
    }

}
