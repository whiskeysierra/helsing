package org.whiskeysierra.helsing.util.inject;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.TypeConverter;
import org.nnsoft.guice.rocoto.converters.FileConverter;
import org.nnsoft.guice.rocoto.converters.PatternConverter;

import java.util.List;
import java.util.Map;

public final class InjectModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FileConverter());
        install(new PatternConverter());

        bindConverter(Joiner.class, new JoinerConverter());
        bindConverter(Splitter.class, new SplitterConverter());
        bindConverter(new TypeLiteral<List<Integer>>() { }, new IntegerListConverter());
        bindConverter(new TypeLiteral<Map<Integer, String>>() { }, new IntegerStringMapConverter());
    }

    private void bindConverter(Class<?> type, TypeConverter converter) {
        bindConverter(TypeLiteral.get(type), converter);
    }

    private void bindConverter(TypeLiteral<?> literal, TypeConverter converter) {
        convertToTypes(Matchers.only(literal), converter);
    }

}
