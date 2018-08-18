package de.elasticrew.analyser.configs;

import javax.xml.ws.Endpoint;

public final class AppConstants {

    public static class AppProperties {

        public static final String APPLICATION_NAME = "application.name";
        public static final String APPLICATION_VERSION = "application.version";
        public static final String ELASTIC_TRANSPORT_CLIENT_SETTINGS = "elastic.transport-client.settings";
        public static final String ELASTIC_TRANSPORT_CLIENT_CLUSTER_ADDRESSES = "elastic.transport-client.cluster-addresses";

        private AppProperties() { /* No instances */ }
    }

    public static class EndPoints {

        public static final String VERSION = "/version";

        private EndPoints() { /* No instances */ }
    }

    private AppConstants() { /* No instances */ }
}
