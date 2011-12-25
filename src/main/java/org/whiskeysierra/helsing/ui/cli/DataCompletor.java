package org.whiskeysierra.helsing.ui.cli;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import jline.Completor;
import jline.SimpleCompletor;
import org.apache.commons.io.filefilter.AndFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

final class DataCompletor implements Completor {

    private final Completor completor;

    @Inject
    public DataCompletor(
        @Named("data.directory") File directory,
        @Named("data.pattern") Pattern data,
        @Named("schema.pattern") Pattern schema) {

        this.completor = new SimpleCompletor(directory.list(new AndFileFilter(
            new RegexFileFilter(data),
            new NotFileFilter(new RegexFileFilter(schema))
        )));
    }

    @Override
    public int complete(String buffer, int cursor, List candidates) {
        return completor.complete(buffer, cursor, candidates);
    }

}
