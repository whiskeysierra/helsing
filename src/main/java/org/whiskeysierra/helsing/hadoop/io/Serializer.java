package org.whiskeysierra.helsing.hadoop.io;

public interface Serializer {

    <T> String serialize(T value, Class<? extends T> type);

    <T> T deserialize(String value, Class<T> type);

}
