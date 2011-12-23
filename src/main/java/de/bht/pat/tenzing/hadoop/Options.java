package de.bht.pat.tenzing.hadoop;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import java.io.File;

final class Options {

    @Option(name = "-i", aliases = "--input", metaVar = "output", required = true, usage = "Path to input file")
    private File input;

    @Option(name = "-o", aliases = "--output", metaVar = "input", required = true, usage = "Path to output directory")
    private File output;

    @Argument(metaVar = "SQL", required = true, multiValued = true, usage = "SQL query to be executed")
    private String query;

    public File getInput() {
        return input;
    }

    public File getOutput() {
        return output;
    }

    public String getQuery() {
        return query;
    }

}
