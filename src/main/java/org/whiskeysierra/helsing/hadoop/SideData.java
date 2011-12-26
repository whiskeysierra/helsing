package org.whiskeysierra.helsing.hadoop;

import com.google.common.base.Joiner;
import com.google.common.base.Joiner.MapJoiner;
import com.google.common.base.Splitter;
import com.google.common.base.Splitter.MapSplitter;

final class SideData {

    /**
     * A list of column indices.
     */
    public static final String PROJECTION = "helsing.projection";

    /**
     * A list of selected group indices.
     */
    public static final String GROUPS = "helsing.groups";

    /**
     * A map from selected column indices to function names.
     */
    public static final String FUNCTIONS = "helsing.functions";

    public static final Joiner JOINER = Joiner.on(",");
    public static final Splitter SPLITTER = Splitter.on(",");
    public static final MapJoiner MAP_JOINER = JOINER.withKeyValueSeparator("=");
    public static final MapSplitter MAP_SPLITTER = SPLITTER.withKeyValueSeparator("=");

    private SideData() {

    }

}
