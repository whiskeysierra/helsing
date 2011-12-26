package org.whiskeysierra.helsing.hadoop;

import com.google.common.base.Joiner;
import com.google.common.base.Joiner.MapJoiner;
import com.google.common.base.Splitter;
import com.google.common.base.Splitter.MapSplitter;
import com.google.inject.TypeLiteral;

final class DefaultSerializer implements Serializer {  
    
    private final Joiner joiner = Joiner.on(",");
    private final Splitter splitter = Splitter.on(",");
    private final MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");
    private final MapSplitter mapSplitter = splitter.withKeyValueSeparator("=");

    @Override
    public <T> String toString(T value, TypeLiteral<T> literal) {
        return null;
    }

    @Override
    public <T> T fromString(String value, TypeLiteral<T> literal) {
        return null;
    }
    
}
