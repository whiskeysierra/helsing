package org.whiskeysierra.helsing.hadoop;

import com.google.common.collect.Maps;
import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import org.whiskeysierra.helsing.api.Functions;
import org.whiskeysierra.helsing.hadoop.functions.Aggregator;

import java.util.Map;

final class SideDataModule extends AbstractModule {

    @Override
    protected void configure() {

    }

    @Provides
    public Map<Integer, Provider<Aggregator>> provideAggregators(
        @Functions Map<String, Provider<Aggregator>> functions,
        @Named(SideData.FUNCTIONS) Map<Integer, String> indices) {

        final Map<Integer, Provider<Aggregator>> aggregators = Maps.newHashMap();

        for (Map.Entry<Integer, String> entry : indices.entrySet()) {
            final int index = entry.getKey();
            final String functionName = entry.getValue();

            aggregators.put(index, functions.get(functionName));
        }

        return aggregators;
    }

}
