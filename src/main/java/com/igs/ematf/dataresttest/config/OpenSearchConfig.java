package com.igs.ematf.dataresttest.config;

import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.ClientTlsStrategyBuilder;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
import org.apache.hc.core5.http.ssl.TLS;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.transport.httpclient5.ApacheHttpClient5TransportBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class OpenSearchConfig {

    @Value("${opensearch.protocol}")
    private String openSearchProtocol;
    @Value("${opensearch.host}")
    private String openSearchHost;
    @Value("${opensearch.port}")
    private int openSearchPort;
    @Value("${opensearch.username}")
    private String openSearchUsername;
    @Value("${opensearch.password}")
    private String openSearchPassword;

    @Value("${opensearch.settings}")
    private String openSearchSettings;
    @Value("${opensearch.mappings}")
    private String openSearchMappings;
    @Value("${opensearch.keyStorePath}")
    private String keyStorePath;
    @Value("${opensearch.keyStorePassword}")
    private String keyStorePassword;

    @Bean
    public OpenSearchClient openSearchClient2_8_1() throws NoSuchAlgorithmException, KeyStoreException,
            KeyManagementException {
        System.setProperty("javax.net.ssl.trustStore", keyStorePath);
        System.setProperty("javax.net.ssl.trustStorePassword", keyStorePassword);

        final HttpHost host = new HttpHost(openSearchProtocol, openSearchHost, openSearchPort);
        final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                new AuthScope(host),
                new UsernamePasswordCredentials(openSearchUsername, openSearchPassword.toCharArray())
        );

        final SSLContext sslcontext = SSLContextBuilder.create()
                .loadTrustMaterial(null, (chains, authType) -> true)
//                .loadTrustMaterial(TrustAllStrategy.INSTANCE) // 生产环境应使用真实证书
                .build();

        final ApacheHttpClient5TransportBuilder builder = ApacheHttpClient5TransportBuilder.builder(host);
        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            final TlsStrategy tlsStrategy = ClientTlsStrategyBuilder.create()
                    .setSslContext(sslcontext)
                    // See https://issues.apache.org/jira/browse/HTTPCLIENT-2219
//                    .setTlsDetailsFactory(sslEngine -> new TlsDetails(sslEngine.getSession(),
//                            sslEngine.getApplicationProtocol()))
                    .setTlsVersions(TLS.V_1_2, TLS.V_1_3) // 明确指定TLS版本
                    .build();

            final PoolingAsyncClientConnectionManager connectionManager = PoolingAsyncClientConnectionManagerBuilder
                    .create()
                    .setTlsStrategy(tlsStrategy)
                    .build();
            return httpClientBuilder
                    .setDefaultCredentialsProvider(credentialsProvider)
                    .setConnectionManager(connectionManager);
        });
        return new OpenSearchClient(builder.build());
    }
}