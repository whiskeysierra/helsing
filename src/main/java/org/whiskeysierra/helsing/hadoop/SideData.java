package org.whiskeysierra.helsing.hadoop;

final class SideData {

    /**
     * Ordered list of indices where the value represents the index in the schema and the value
     * represents the index in the projection.
     */
    public static final String PROJECTION = "helsing.projection";

    /**
     * The index of the grouping key in the schema.
     */
    @Deprecated
    public static final String GROUP = "helsing.group_index";

    public static final String GROUPS = "helsing.group_indices";

    public static final String FUNCTIONS = "helsing.function_indices";

    private SideData() {

    }

}
