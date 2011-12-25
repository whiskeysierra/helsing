package org.whiskeysierra.helsing.hadoop;

import com.google.common.collect.Maps;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import org.whiskeysierra.helsing.api.Functions;
import org.whiskeysierra.helsing.hadoop.functions.Aggregator;

import javax.inject.Named;
import java.util.Collections;
import java.util.Map;

final class SideDataModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(new TypeLiteral<Map<Integer, Provider<Aggregator>>>() {
        }).toProvider(AggregatorProvider.class);
    }

    private static final class AggregatorProvider implements Provider<Map<Integer, Provider<Aggregator>>> {

        @Inject
        @Functions
        private Map<String, Provider<Aggregator>> functions;

        @Inject(optional = true)
        @Named(SideData.FUNCTIONS)
        private Map<Integer, String> indices = Collections.emptyMap();

        @Override
        public Map<Integer, Provider<Aggregator>> get() {
            final Map<Integer, Provider<Aggregator>> aggregators = Maps.newHashMap();

            for (Map.Entry<Integer, String> entry : indices.entrySet()) {
                final int index = entry.getKey();
                final String functionName = entry.getValue();

                aggregators.put(index, functions.get(functionName));
            }

            return aggregators;
        }

    }

}
