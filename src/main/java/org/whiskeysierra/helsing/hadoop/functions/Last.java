package org.whiskeysierra.helsing.hadoop.functions;

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
