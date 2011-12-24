package de.bht.pat.tenzing.hadoop;

final class SideData {

    /**
     * Ordered list of indices where the value represents the index in the schema and the value
     * represents the index in the projection.
     */
    public static final String PROJECTION = "tenzing.projection";

    /**
     * The index of the grouping key in the schema.
     */
    public static final String GROUP = "tenzing.group_index";

    public static final String FUNCTIONS = "tenzing.function_indices";

    private SideData() {

    }

}
