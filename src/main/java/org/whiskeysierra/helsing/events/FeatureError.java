package org.whiskeysierra.helsing.events;

public final class FeatureError {

    private final String feature;

    public FeatureError(String feature) {
        this.feature = feature;
    }

    public String getFeature() {
        return feature;
    }

}
