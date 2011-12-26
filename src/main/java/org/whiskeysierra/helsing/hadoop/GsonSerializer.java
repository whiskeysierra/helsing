package org.whiskeysierra.helsing.hadoop;

import com.google.inject.TypeLiteral;

final class GsonSerializer implements Serializer {
    
    @Override
    public <T> String toString(T value, TypeLiteral<T> literal) {
        return null;
    }

    @Override
    public <T> T fromString(String value, TypeLiteral<T> literal) {
        return null;
    }
    
}
