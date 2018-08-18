package de.elasticrew.analyser.configs;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.springframework.core.env.Environment;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;

import static de.elasticrew.analyser.configs.AppConstants.AppProperties.ELASTIC_TRANSPORT_CLIENT_CLUSTER_ADDRESSES;
import static de.elasticrew.analyser.configs.AppConstants.AppProperties.ELASTIC_TRANSPORT_CLIENT_SETTINGS;
import static java.util.stream.Collectors.toSet;
import static org.elasticsearch.client.transport.TransportClient.*;
import static org.elasticsearch.cluster.ClusterName.CLUSTER_NAME_SETTING;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class AppElasticsearchConfigTest {

    private Environment mockEnv;

    private AppElasticsearchConfig uat;

    @BeforeClass
    public void setUpBeforeClass() {
        mockEnv = mock(Environment.class);
        uat = new AppElasticsearchConfig();
    }

    @AfterMethod
    public void afterMethod() {
        reset(mockEnv);
    }

    @Test
    public void testTransportClientSettingsConfig() throws IOException {
        when(mockEnv.getRequiredProperty(ELASTIC_TRANSPORT_CLIENT_SETTINGS)).thenReturn("transport-client.yaml");

        Settings actualSettings = uat.transportClientSettings(mockEnv);

        assertEquals(actualSettings.get(CLUSTER_NAME_SETTING.getKey()), "elasticsearch-test",
                "Incorrect elasticsearch transaction client property '" + CLUSTER_NAME_SETTING.getKey() + "'");

        assertTrue(actualSettings.getAsBoolean(CLIENT_TRANSPORT_SNIFF.getKey(), false),
                "Incorrect elasticsearch transaction client property '" + CLIENT_TRANSPORT_SNIFF.getKey() + "'");

        assertTrue(actualSettings.getAsBoolean(CLIENT_TRANSPORT_IGNORE_CLUSTER_NAME.getKey(), false),
                "Incorrect elasticsearch transaction client property '" + CLIENT_TRANSPORT_IGNORE_CLUSTER_NAME.getKey() + "'");

        assertEquals(actualSettings.get(CLIENT_TRANSPORT_PING_TIMEOUT.getKey()), "15s",
                "Incorrect elasticsearch transaction client property '" + CLIENT_TRANSPORT_PING_TIMEOUT.getKey() + "'");

        assertEquals(actualSettings.get(CLIENT_TRANSPORT_NODES_SAMPLER_INTERVAL.getKey()), "15s",
                "Incorrect elasticsearch transaction client property '" + CLIENT_TRANSPORT_NODES_SAMPLER_INTERVAL.getKey() + "'");
    }

    @Test(dataProvider = "toTransportAddressValidDataProvider")
    public void testToTransportAddress(final String testInput, final TransportAddress expectedAddress) {
        final TransportAddress actualAddress = uat.toTransportAddress(testInput);

        assertEquals(actualAddress, expectedAddress, "Incorrect convertion to TransportAddress");
    }

    @DataProvider
    public Object[][] toTransportAddressValidDataProvider() {
        return new Object[][]{
                {"localhost", new TransportAddress(new InetSocketAddress("localhost", 9300))},
                {"localhost:9300", new TransportAddress(new InetSocketAddress("localhost", 9300))},
                {"localhost:9876", new TransportAddress(new InetSocketAddress("localhost", 9876))},
                {"192.168.0.1", new TransportAddress(new InetSocketAddress("192.168.0.1", 9300))},
                {"192.168.0.1:9300", new TransportAddress(new InetSocketAddress("192.168.0.1", 9300))},
                {"192.168.0.1:9876", new TransportAddress(new InetSocketAddress("192.168.0.1", 9876))},
        };
    }

    @Test(dataProvider = "testTransactionAddressesValidDataProvider")
    public void testTransactionAddresses(final String testInput, final TransportAddress[] expectedAddresses) {
        when(mockEnv.getRequiredProperty(ELASTIC_TRANSPORT_CLIENT_CLUSTER_ADDRESSES)).thenReturn(testInput);

        final TransportAddress[] actualAddresses = uat.transportAddresses(mockEnv);

        assertEquals(actualAddresses, expectedAddresses,
                "Incorrect parsing of property " + ELASTIC_TRANSPORT_CLIENT_CLUSTER_ADDRESSES);
    }

    @DataProvider
    public Object[][] testTransactionAddressesValidDataProvider() {
        return new Object[][] {
                {"localhost", toExpectedArray(new InetSocketAddress("localhost", 9300))},
                {"127.0.0.1", toExpectedArray(new InetSocketAddress("127.0.0.1", 9300))},
                {"localhost:9300", toExpectedArray(new InetSocketAddress("localhost", 9300))},
                {"localhost:9900", toExpectedArray(new InetSocketAddress("localhost", 9900))},
                {"localhost,localhost:9300", toExpectedArray(new InetSocketAddress("localhost", 9300))},
                {"localhost , localhost:9300, ,", toExpectedArray(new InetSocketAddress("localhost", 9300))},
                {"localhost, localhost:9400,192.168.0.1 ,192.168.1.1:9300 , 172.32.16.1:9999,10.0.0.1", toExpectedArray(
                        new InetSocketAddress("localhost", 9300),
                        new InetSocketAddress("localhost", 9400),
                        new InetSocketAddress("192.168.0.1", 9300),
                        new InetSocketAddress("192.168.1.1", 9300),
                        new InetSocketAddress("172.32.16.1", 9999),
                        new InetSocketAddress("10.0.0.1", 9300))
                }
        };
    }

    static TransportAddress[] toExpectedArray(final InetSocketAddress... addresses) {
        return Arrays.stream(addresses)
                .map(TransportAddress::new)
                .collect(toSet())
                .toArray(new TransportAddress[0]);
    }
}
