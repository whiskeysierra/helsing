package org.whiskeysierra.helsing.hadoop.functions;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.multibindings.MapBinder;
import org.reflections.Reflections;
import org.whiskeysierra.helsing.api.Functions;

import java.util.Map;
import java.util.Set;

public final class FunctionsModule extends AbstractModule {

    @Override
    protected void configure() {
        final MapBinder<String, Aggregator> binder = MapBinder.newMapBinder(binder(),
            String.class, Aggregator.class, Functions.class);

        final String name = FunctionsModule.class.getPackage().getName();
        final Reflections reflections = new Reflections(name);

        for (Class<?> aggregator : reflections.getTypesAnnotatedWith(AggregateFunction.class)) {
            final String function = aggregator.getAnnotation(AggregateFunction.class).value();
            binder.addBinding(function).to(aggregator.asSubclass(Aggregator.class));
        }
    }

    @Provides
    @Functions
    public Set<String> provideFunctionNames(@Functions Map<String, Aggregator> functions) {
        return functions.keySet();
    }

}
