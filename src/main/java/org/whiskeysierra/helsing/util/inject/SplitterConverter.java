package org.whiskeysierra.helsing.util.inject;

import com.google.common.base.Splitter;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeConverter;

final class SplitterConverter implements TypeConverter {

    @Override
    public Splitter convert(String value, TypeLiteral<?> toType) {
        return Splitter.on(value);
    }

}
