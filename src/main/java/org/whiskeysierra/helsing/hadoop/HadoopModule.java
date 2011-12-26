package org.whiskeysierra.helsing.hadoop;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.apache.hadoop.io.Stringifier;
import org.whiskeysierra.helsing.PropertiesModule;
import org.whiskeysierra.helsing.api.ApiModule;
import org.whiskeysierra.helsing.hadoop.functions.FunctionsModule;
import org.whiskeysierra.helsing.util.inject.InjectModule;
import org.whiskeysierra.helsing.util.io.InputOutputModule;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

final class HadoopModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new ApiModule());
        install(new FunctionsModule());
        install(new PropertiesModule());
        install(new InjectModule());
        install(new InputOutputModule());
    }

    @Provides
    public Stringifier<List<Integer>> provideIntegerListStringifier() {
        return new Stringifier<List<Integer>>() {

            @Override
            public String toString(List<Integer> integers) throws IOException {
                return SideData.JOINER.join(integers);
            }

            @Override
            public List<Integer> fromString(String s) throws IOException {
                if (s == null) {
                    return ImmutableList.of();
                } else {
                    final ImmutableList.Builder<Integer> builder = ImmutableList.builder();

                    for (String index : SideData.SPLITTER.split(s)) {
                        builder.add(Integer.valueOf(index));
                    }

                    return builder.build();
                }
            }

            @Override
            public void close() throws IOException {
                // nothing to do
            }

        };
    }

    @Provides
    public Stringifier<Map<Integer, String>> provideIntegerStringMapStringifier() {
        return new Stringifier<Map<Integer, String>>() {

            @Override
            public String toString(Map<Integer, String> integerStringMap) throws IOException {
                return SideData.MAP_JOINER.join(integerStringMap);
            }

            @Override
            public Map<Integer, String> fromString(String s) throws IOException {
                if (s == null) {
                    return ImmutableMap.of();
                } else {
                    final ImmutableMap.Builder<Integer, String> builder = ImmutableMap.builder();

                    for (Entry<String, String> entry : SideData.MAP_SPLITTER.split(s).entrySet()) {
                        builder.put(Integer.valueOf(entry.getKey()), entry.getValue());
                    }

                    return builder.build();
                }
            }

            @Override
            public void close() throws IOException {

            }

        };
    }

}
