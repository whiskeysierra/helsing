package org.whiskeysierra.helsing.inject;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.google.inject.Provider;

import java.util.Map;

public final class MoreProviders {

    private MoreProviders() {

    }

    public static <K, V> Map<K, V> get(Map<K, Provider<V>> map) {
        return Maps.newHashMap(Maps.transformValues(map, new Function<Provider<V>, V>() {

            @Override
            public V apply(Provider<V> provider) {
                return provider.get();
            }

        }));
    }

}
