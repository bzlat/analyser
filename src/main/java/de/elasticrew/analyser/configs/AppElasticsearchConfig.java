package de.elasticrew.analyser.configs;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;

import static de.elasticrew.analyser.configs.AppConstants.AppProperties.ELASTIC_TRANSPORT_CLIENT_CLUSTER_ADDRESSES;
import static de.elasticrew.analyser.configs.AppConstants.AppProperties.ELASTIC_TRANSPORT_CLIENT_SETTINGS;
import static java.util.stream.Collectors.toSet;

@Configuration
public class AppElasticsearchConfig {

    @Bean
    public TransportClient transportClient(final Environment env) throws IOException {
        final TransportClient transportClient = new PreBuiltTransportClient(transportClientSettings(env));
        transportClient.addTransportAddresses(transportAddresses(env));

        return transportClient;
    }

    Settings transportClientSettings(final Environment env) throws IOException {
        final String transportClientResourceName = env.getRequiredProperty(ELASTIC_TRANSPORT_CLIENT_SETTINGS);
        final ClassPathResource settingsResource = new ClassPathResource(transportClientResourceName);

        return Settings.builder()
                .loadFromStream(settingsResource.getFilename(), settingsResource.getInputStream(), false)
                .build();
    }

    TransportAddress[] transportAddresses(final Environment env) {
        final String transportAddresses = env.getRequiredProperty(ELASTIC_TRANSPORT_CLIENT_CLUSTER_ADDRESSES);

        return Arrays.stream(transportAddresses.split("\\s*,\\s*"))
                .filter(s -> !s.isEmpty())
                .map(String::trim)
                .map(this::toTransportAddress)
                .collect(toSet())
                .toArray(new TransportAddress[0]);
    }

    TransportAddress toTransportAddress(final String input) {
        final String[] hostPort = input.split(":");
        final String host = hostPort[0];
        final int port = (hostPort.length > 1) ? Integer.valueOf(hostPort[1]) : 9300;

        return new TransportAddress(new InetSocketAddress(host, port));
    }
}
