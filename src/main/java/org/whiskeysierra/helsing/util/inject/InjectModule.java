package org.whiskeysierra.helsing.util.inject;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.TypeConverter;
import org.nnsoft.guice.rocoto.converters.FileConverter;
import org.nnsoft.guice.rocoto.converters.PatternConverter;

public final class InjectModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FileConverter());
        install(new PatternConverter());

        bindConverter(Joiner.class, new JoinerConverter());
        bindConverter(Splitter.class, new SplitterConverter());
    }

    private void bindConverter(Class<?> type, TypeConverter converter) {
        convertToTypes(Matchers.only(TypeLiteral.get(type)), converter);
    }

}
