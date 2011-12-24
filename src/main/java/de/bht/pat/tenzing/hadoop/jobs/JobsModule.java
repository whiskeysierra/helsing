package de.bht.pat.tenzing.hadoop.jobs;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import org.apache.hadoop.mapreduce.Reducer;
import org.reflections.Reflections;

import java.util.Map;
import java.util.Set;

public final class JobsModule extends AbstractModule {

    @Override
    protected void configure() {
        final MapBinder<String, Class<? extends Reducer>> binder = MapBinder.newMapBinder(binder(),
            TypeLiteral.get(String.class), new TypeLiteral<Class<? extends Reducer>>() {
        }, Functions.class);

        final String name = JobsModule.class.getPackage().getName();
        final Reflections reflections = new Reflections(name);

        for (Class<?> reducer : reflections.getTypesAnnotatedWith(AggregateFunction.class)) {
            final String function = reducer.getAnnotation(AggregateFunction.class).value();
            binder.addBinding(function).toInstance(reducer.asSubclass(Reducer.class));
        }
    }

    @Provides
    @Functions
    public Set<String> provideFunctionNames(@Functions Map<String, Class<? extends Reducer>> functions) {
        return functions.keySet();
    }

}
