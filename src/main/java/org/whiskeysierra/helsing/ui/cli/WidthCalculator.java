package org.whiskeysierra.helsing.ui.cli;

import com.google.common.collect.Maps;
import com.google.common.io.LineProcessor;
import com.google.inject.Inject;
import org.whiskeysierra.helsing.util.format.FileFormat;

import java.io.IOException;
import java.util.Map;

final class WidthCalculator implements LineProcessor<Map<Integer, Integer>> {

    private final FileFormat format;
    private final Map<Integer, Integer> widths = Maps.newLinkedHashMap();

    @Inject
    public WidthCalculator(FileFormat format) {
        this.format = format;
    }

    private int getWidth(int column) {
        final Integer width = widths.get(column);
        return width == null ? 0 : width;
    }

    @Override
    public boolean processLine(String line) throws IOException {
        int i = 0;

        for (String cell : format.lineOf(line)) {
            final int width = getWidth(i);
            widths.put(i, Math.max(width, cell.length()));
            i++;
        }

        return true;
    }

    @Override
    public Map<Integer, Integer> getResult() {
        return widths;
    }

}
