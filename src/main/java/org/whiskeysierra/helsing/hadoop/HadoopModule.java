package org.whiskeysierra.helsing.hadoop;

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
                return SideData.serialize(integers);
            }

            @Override
            public List<Integer> fromString(String s) throws IOException {
                return SideData.deserializeList(s);
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
                return SideData.serialize(integerStringMap);
            }

            @Override
            public Map<Integer, String> fromString(String s) throws IOException {
                return SideData.deserializeMap(s);
            }

            @Override
            public void close() throws IOException {

            }

        };
    }

}
