package org.whiskeysierra.helsing.hadoop.functions;

import org.whiskeysierra.helsing.util.io.Line;

public interface Aggregator {

    void update(Line line);

    String getResult();

}
