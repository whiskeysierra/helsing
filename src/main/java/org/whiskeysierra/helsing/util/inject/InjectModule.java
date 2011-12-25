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

        convertToTypes(Matchers.only(TypeLiteral.get(Joiner.class)), new TypeConverter() {

            @Override
            public Joiner convert(String value, TypeLiteral<?> toType) {
                return Joiner.on(value);
            }

        });

        convertToTypes(Matchers.only(TypeLiteral.get(Splitter.class)), new TypeConverter() {

            @Override
            public Splitter convert(String value, TypeLiteral<?> toType) {
                return Splitter.on(value);
            }

        });
    }

}
