package org.whiskeysierra.helsing.util.inject;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeConverter;

import java.util.Map;
import java.util.Map.Entry;

final class IntegerStringMapConverter implements TypeConverter {

    @Override
    public Map<Integer, String> convert(String value, TypeLiteral<?> toType) {
        final Builder<Integer, String> builder = ImmutableMap.builder();

        for (Entry<String, String> entry : Splitter.on(",").withKeyValueSeparator("=").split(value).entrySet()) {
            builder.put(Integer.valueOf(entry.getKey()), entry.getValue());
        }

        return builder.build();
    }

}
