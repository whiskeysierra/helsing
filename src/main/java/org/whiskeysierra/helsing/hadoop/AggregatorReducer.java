package org.whiskeysierra.helsing.hadoop;

import com.google.common.base.Objects;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.whiskeysierra.helsing.api.sql.SelectStatement;
import org.whiskeysierra.helsing.api.sql.SqlColumn;
import org.whiskeysierra.helsing.api.sql.SqlExpression;
import org.whiskeysierra.helsing.api.sql.SqlFunction;
import org.whiskeysierra.helsing.api.sql.SqlParser;
import org.whiskeysierra.helsing.hadoop.functions.Aggregator;
import org.whiskeysierra.helsing.hadoop.io.Serializer;
import org.whiskeysierra.helsing.hadoop.io.Types;
import org.whiskeysierra.helsing.util.format.FileFormat;
import org.whiskeysierra.helsing.util.format.Line;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

final class AggregatorReducer extends DependencyInjectionReducer<Writable, Text, NullWritable, Text> {

    private FileFormat format;
    private SqlParser parser;
    private Serializer serializer;
    private Map<String, Provider<Aggregator>> functions;

    private SelectStatement statement;
    private final Multimap<SqlFunction, String> aggregators = ArrayListMultimap.create();

    private List<String> mapProjection;

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

    @Inject
    public void setFunctions(@org.whiskeysierra.helsing.api.Functions Map<String, Provider<Aggregator>> functions) {
        this.functions = functions;
    }

    @Override
    protected void configure(Context context) throws IOException, InterruptedException {
        final Configuration config = context.getConfiguration();
        this.statement = parser.parse(config.get(SideData.QUERY));
        this.mapProjection = serializer.deserialize(config.get(SideData.MAP_PROJECTION), Types.Columns.class);

        for (SqlFunction function : Iterables.filter(statement.projection(), SqlFunction.class)) {
            for (SqlColumn column : function.columns()) {
                aggregators.put(function, column.name());
            }
        }
    }

    @Override
    protected void reduce(Writable ignored, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        if (statement.groupBy().isEmpty() && aggregators.isEmpty()) {
            identity(values, context);
        } else {
            try {
                aggregate(values, context);
            } catch (ExecutionException e) {
                throw new IOException(e);
            }
        }
    }

    private void identity(Iterable<Text> values, Context context) throws IOException, InterruptedException {
        for (Text value : values) {
            context.write(NullWritable.get(), value);
        }
    }


    private void aggregate(Iterable<Text> values, Context context) throws IOException, InterruptedException, ExecutionException {
        final LoadingCache<SqlFunction, Aggregator> cache = CacheBuilder.newBuilder().build(new CacheLoader<SqlFunction, Aggregator>() {

            @Override
            public Aggregator load(SqlFunction key) throws Exception {
                return functions.get(key.name()).get();
            }

        });

        Line first = null;

        for (Text value : values) {
            final Line line = format.lineOf(value);
            for (Entry<SqlFunction, Collection<String>> entry : aggregators.asMap().entrySet()) {
                final SqlFunction function = entry.getKey();
                final Aggregator aggregator = cache.get(function);
                aggregator.update(line.select(toIndices(entry.getValue())));
            }

            first = Objects.firstNonNull(first, line);
        }

        assert first != null;

        final Line result = format.lineOf();

        for (SqlExpression expression : statement.projection()) {
            if (expression.is(SqlColumn.class)) {
                final SqlColumn column = expression.as(SqlColumn.class);
                result.add(first.get(mapProjection.indexOf(column.name())));
            } else if (expression.is(SqlFunction.class)) {
                final SqlFunction function = expression.as(SqlFunction.class);
                final Aggregator aggregator = cache.get(function);
                result.add(aggregator.getResult().toString());
            }
        }

        context.write(NullWritable.get(), result.toText());
    }

    private List<Integer> toIndices(Iterable<String> columns) {
        final List<Integer> indices = Lists.newLinkedList();

        for (String column : columns) {
            indices.add(mapProjection.indexOf(column));
        }

        return indices;
    }
}
