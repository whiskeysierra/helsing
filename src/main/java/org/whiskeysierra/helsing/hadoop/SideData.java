package org.whiskeysierra.helsing.hadoop;

import com.google.common.base.Joiner;
import com.google.common.base.Joiner.MapJoiner;
import com.google.common.base.Splitter;
import com.google.common.base.Splitter.MapSplitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class SideData {

    /**
     * A list of column indices.
     */
    public static final String PROJECTION = "helsing.projection";

    /**
     * A map from selected column indices to column names.
     */
    public static final String COLUMNS = "helsing.columns";

    /**
     * A list of selected group indices.
     */
    public static final String GROUPS = "helsing.groups";

    /**
     * A map from selected column indices to function names.
     */
    public static final String FUNCTIONS = "helsing.functions";

    private static final Joiner JOINER = Joiner.on(",");
    private static final Splitter SPLITTER = Splitter.on(",");
    private static final MapJoiner MAP_JOINER = JOINER.withKeyValueSeparator("=");
    private static final MapSplitter MAP_SPLITTER = SPLITTER.withKeyValueSeparator("=");

    private SideData() {

    }

    public static String serialize(List<Integer> list) {
        return JOINER.join(list);
    }

    public static ImmutableList<Integer> deserializeList(String string) {
        if (string == null) {
            return ImmutableList.of();
        } else {
            final ImmutableList.Builder<Integer> builder = ImmutableList.builder();

            for (String index : SPLITTER.split(string)) {
                builder.add(Integer.valueOf(index));
            }

            return builder.build();
        }
    }

    public static String serialize(Map<Integer, String> map) {
        return MAP_JOINER.join(map);
    }

    public static ImmutableMap<Integer, String> deserializeMap(String string) {
        if (string == null) {
            return ImmutableMap.of();
        } else {
            final ImmutableMap.Builder<Integer, String> builder = ImmutableMap.builder();

            for (Entry<String, String> entry : MAP_SPLITTER.split(string).entrySet()) {
                builder.put(Integer.valueOf(entry.getKey()), entry.getValue());
            }

            return builder.build();
        }
    }

}
