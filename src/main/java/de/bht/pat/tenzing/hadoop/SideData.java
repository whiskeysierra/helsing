package de.bht.pat.tenzing.hadoop;

public final class SideData {

    /**
     * Ordered list of indices where the value represents the index in the schema and the value
     * represents the index in the projection.
     */
    public static final String PROJECTION = "tenzing.projection";

    /**
     * The index of the grouping key in the schema.
     */
    public static final String GROUP_INDEX = "tenzing.group_index";

    /**
     * The index of the aggregate function in the projection.
     */
    public static final String FUNCTION_INDEX = "tenzing.function_index";

    private SideData() {

    }

}
