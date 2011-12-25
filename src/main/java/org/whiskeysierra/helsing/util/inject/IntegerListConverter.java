package org.whiskeysierra.helsing.util.inject;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeConverter;

import java.util.List;

final class IntegerListConverter implements TypeConverter {

    @Override
    public List<Integer> convert(String value, TypeLiteral<?> toType) {
        final Builder<Integer> builder = ImmutableList.builder();

        for (String index : Splitter.on(",").split(value)) {
            builder.add(Integer.valueOf(index));
        }

        return builder.build();
    }

}
