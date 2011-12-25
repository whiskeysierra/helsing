package org.whiskeysierra.helsing.util.inject;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeConverter;
import org.whiskeysierra.helsing.hadoop.SideData;

import java.util.Map;

final class IntegerStringMapConverter implements TypeConverter {

    @Override
    public Map<Integer, String> convert(String value, TypeLiteral<?> toType) {
        return SideData.deserializeMap(value);
    }

}
