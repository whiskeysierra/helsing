package de.bht.pat.tenzing.ui;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import de.bht.pat.tenzing.inject.Data;
import de.bht.pat.tenzing.inject.Schema;
import jline.Completor;
import jline.SimpleCompletor;
import org.apache.commons.io.filefilter.AndFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;
import java.util.regex.Pattern;

final class DataCompletor implements Completor {

    private final Completor completor;

    @Inject
    public DataCompletor(@Data File directory, @Data Pattern data, @Schema Pattern schema) {
        final FilenameFilter filter = new AndFileFilter(
                new RegexFileFilter(data),
                new NotFileFilter(new RegexFileFilter(schema))
        );

        final String[] files = directory.list(filter);

        final List<String> candiates = Lists.newLinkedList();

        for (String file : files) {
            candiates.add(file);
        }

        this.completor = new SimpleCompletor(Iterables.toArray(candiates, String.class));
    }

    @Override
    public int complete(String buffer, int cursor, List candidates) {
        return completor.complete(buffer, cursor, candidates);
    }

}
