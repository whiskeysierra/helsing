package org.whiskeysierra.helsing.hadoop;

import com.google.inject.TypeLiteral;

interface Serializer {

    <T> String toString(T value, TypeLiteral<T> literal);

    <T> T fromString(String value, TypeLiteral<T> literal);

}
