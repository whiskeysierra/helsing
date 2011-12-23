package de.bht.pat.tenzing.util.io;

import com.google.common.io.LineProcessor;

public abstract class StatelessLineProcessor implements LineProcessor<Void> {

    @Override
    public Void getResult() {
        return null;
    }

}
