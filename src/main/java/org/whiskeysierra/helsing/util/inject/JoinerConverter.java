package org.whiskeysierra.helsing.util.inject;

import com.google.common.base.Joiner;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeConverter;

final class JoinerConverter implements TypeConverter {

    @Override
    public Joiner convert(String value, TypeLiteral<?> toType) {
        return Joiner.on(value);
    }

}
