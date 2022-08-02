package com.mongodb.examples;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import nl.altindag.ssl.SSLFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import java.nio.file.Paths;

@Configuration
public class MongoConfig   {
    @Value("${mongodb.uri}")
    private String uri;
    @Value("${mongodb.database}")
    private String databaseName;
    @Value("${truststore.path}")
    private String trustStorePath;
    @Value("${truststore.pwd}")
    private String trustStorePwd;

    @Bean
    public MongoClient mongo() {

        ConnectionString connectionString = new ConnectionString(uri);

        SSLFactory sslFactory = SSLFactory.builder()
                //.withIdentityMaterial(Paths.get(keystorePath), keystorePwd.toCharArray())
                .withTrustMaterial(Paths.get(trustStorePath), trustStorePwd.toCharArray())
                .build();

        SSLContext sslContext = sslFactory.getSslContext();
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .applyToSslSettings(builder -> {
                    builder.context(sslContext);
                    builder.invalidHostNameAllowed(true);
                    builder.enabled(true);

                })
                .build();

        return MongoClients.create(mongoClientSettings);
    }
}
