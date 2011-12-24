package de.bht.pat.tenzing.hadoop.functions;

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
