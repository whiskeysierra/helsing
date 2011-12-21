package de.bht.pat.tenzing.cli;

import com.google.common.collect.Table;

import java.io.File;
import java.io.IOException;

public interface TablePrinter {

    void print(File file) throws IOException;

}
