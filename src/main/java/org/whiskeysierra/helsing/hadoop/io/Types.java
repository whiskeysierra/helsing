package org.whiskeysierra.helsing.hadoop.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class Types {

    private Types() {

    }

    public static final class Indices extends ArrayList<Integer> {

    }

    public static final class Functions extends HashMap<Integer, FunctionDefinition> {

    }

    public static final class FunctionDefinition {

        private final String name;
        private final List<Integer> columns;

        public FunctionDefinition(String name, List<Integer> columns) {
            this.name = name;
            this.columns = columns;
        }

        public String getName() {
            return name;
        }

        public List<Integer> getColumns() {
            return columns;
        }

    }

}
