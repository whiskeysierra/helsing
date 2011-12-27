package org.whiskeysierra.helsing.hadoop.io;

import com.google.gson.Gson;
import com.google.inject.Inject;

final class GsonSerializer implements Serializer {

    private final Gson gson;

    @Inject
    public GsonSerializer(Gson gson) {
        this.gson = gson;
    }

    @Override
    public <T> String serialize(T value, Class<? extends T> type) {
        return gson.toJson(value, type);
    }

    @Override
    public <T> T deserialize(String value, Class<T> type) {
        return gson.fromJson(value, type);
    }

}
