
package org.whiskeysierra.helsing.hadoop;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.whiskeysierra.helsing.api.sql.SelectStatement;
import org.whiskeysierra.helsing.api.sql.SqlParser;
import org.whiskeysierra.helsing.hadoop.io.Serializer;
import org.whiskeysierra.helsing.hadoop.io.Types;
import org.whiskeysierra.helsing.util.format.FileFormat;
import org.whiskeysierra.helsing.util.format.Line;

import java.io.IOException;
import java.util.List;

final class GroupByMapper extends DependencyInjectionMapper<LongWritable, Text, Text, Text> {

    private FileFormat format;
    private SqlParser parser;
    private Serializer serializer;

    private List<String> schema;
    private List<String> mapProjection;
    private Iterable<String> groups;

    @Inject
    public void setFormat(FileFormat format) {
        this.format = format;
    }

    @Inject
    public void setParser(SqlParser parser) {
        this.parser = parser;
    }

    @Inject
    public void setSerializer(Serializer serializer) {
        this.serializer = serializer;
    }

    @Override
    protected void configure(Context context) throws IOException, InterruptedException {
        final Configuration config = context.getConfiguration();
        final SelectStatement statement = parser.parse(config.get(SideData.QUERY));

        this.schema = serializer.deserialize(config.get(SideData.SCHEMA), Types.Columns.class);
        this.mapProjection = serializer.deserialize(config.get(SideData.MAP_PROJECTION), Types.Columns.class);
        this.groups = statement.groupBy().toStrings();
    }

    @Override
    protected void map(LongWritable ignored, Text value, Context context) throws IOException, InterruptedException {
        final Line line = format.lineOf(value);

        final Text group = line.select(toIndices(groups)).toText();
        final Text projection = line.select(toIndices(mapProjection)).toText();

        context.write(group, projection);
    }

    private List<Integer> toIndices(Iterable<String> columns) {
        final List<Integer> indices = Lists.newLinkedList();

        for (String column : columns) {
            indices.add(schema.indexOf(column));
        }

        return indices;
    }

}
