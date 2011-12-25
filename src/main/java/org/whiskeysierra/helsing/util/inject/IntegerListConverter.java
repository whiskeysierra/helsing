package org.whiskeysierra.helsing.util.inject;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeConverter;
import org.whiskeysierra.helsing.hadoop.SideData;

import java.util.List;

final class IntegerListConverter implements TypeConverter {

    @Override
    public List<Integer> convert(String value, TypeLiteral<?> toType) {
        return SideData.deserializeList(value);
    }

}
