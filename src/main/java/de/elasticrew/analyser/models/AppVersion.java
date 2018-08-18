package de.elasticrew.analyser.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AppVersion {

    private final String name;
    private final String version;

    @JsonCreator
    public AppVersion(
            final @JsonProperty("name") String name,
            final @JsonProperty("version") String version) {

        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }
}
