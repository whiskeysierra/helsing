package org.whiskeysierra.helsing.ui.cli;

import com.google.common.collect.Lists;
import com.google.common.io.PatternFilenameFilter;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import jline.Completor;
import jline.MultiCompletor;
import jline.SimpleCompletor;

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

final class SchemaCompletor implements Completor {

    private final Completor completor;

    @Inject
    public SchemaCompletor(
        @Named("data.directory") File directory,
        @Named("schema.pattern") Pattern pattern) throws IOException {
        final List<Completor> completors = Lists.newLinkedList();

        final FilenameFilter filter = new PatternFilenameFilter(pattern);
        final File[] files = directory.listFiles(filter);

        for (File file : files) {
            completors.add(new SimpleCompletor(new FileReader(file)));
        }

        this.completor = new MultiCompletor(completors);
    }

    @Override
    public int complete(String buffer, int cursor, List candidates) {
        return completor.complete(buffer, cursor, candidates);
    }

}
