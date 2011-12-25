package org.whiskeysierra.helsing.util.concurrent.process;

import com.google.common.base.Function;

import java.io.IOException;

public interface RunningProcess {

    void await() throws IOException;

    String getResult() throws IOException;

    <V> V apply(Function<String, V> function) throws IOException;

    void cancel();

}
