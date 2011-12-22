package de.bht.pat.tenzing.events;

public final class UnsupportedFeatureEvent {

    private final String feature;

    public UnsupportedFeatureEvent(String feature) {
        this.feature = feature;
    }

    public String getFeature() {
        return feature;
    }

}
