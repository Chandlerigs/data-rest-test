package com.igs.ematf.config;

import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.ClientTlsStrategyBuilder;
import org.apache.hc.core5.function.Factory;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
import org.apache.hc.core5.reactor.ssl.TlsDetails;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.transport.OpenSearchTransport;
import org.opensearch.client.transport.httpclient5.ApacheHttpClient5TransportBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class OpenSearchConfig {

    @Value("${opensearch.host}")
    private String host;
    @Value("${opensearch.port}")
    private int port;
    @Value("${opensearch.username}")
    private String username;
    @Value("${opensearch.password}")
    private String password;
    @Value("${opensearch.protocol}")
    private String protocol;

    @Bean
    public OpenSearchClient opensearchClient() {
        System.setProperty("javax.net.ssl.trustStore", "R:\\WorkSpace_IGS\\IGS_WIKI\\opensearch\\yourkeystore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "P@ssw0rd");

        final HttpHost host = new HttpHost("https", "localhost", 9200);
        final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        // Only for demo purposes. Don't specify your credentials in code.
        credentialsProvider.setCredentials(new AuthScope(host), new UsernamePasswordCredentials("admin", "Myp@ssW0rd!IsS3cure".toCharArray()));

        final SSLContext sslContext;
        try {
            sslContext = SSLContextBuilder.create().loadTrustMaterial(null, (chains, authType) -> true).build();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        }


        final ApacheHttpClient5TransportBuilder builder = ApacheHttpClient5TransportBuilder
                .builder(host);
        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            final TlsStrategy tlsStrategy = ClientTlsStrategyBuilder
                    .create()
                    .setSslContext(sslContext)
                    // See https://issues.apache.org/jira/browse/HTTPCLIENT-2219
                    .setTlsDetailsFactory(new Factory<SSLEngine, TlsDetails>() {
                        @Override
                        public TlsDetails create(final SSLEngine sslEngine) {
                            return new TlsDetails(sslEngine.getSession(), sslEngine.getApplicationProtocol());
                        }
                    }).build();

            final PoolingAsyncClientConnectionManager connectionManager = PoolingAsyncClientConnectionManagerBuilder
                    .create()
                    .setTlsStrategy(tlsStrategy)
                    .build();

            return httpClientBuilder
                    .setDefaultCredentialsProvider(credentialsProvider)
                    .setConnectionManager(connectionManager);
        });

        final OpenSearchTransport transport = builder.build();
        OpenSearchClient client = new OpenSearchClient(transport);
        return client;
    }
}