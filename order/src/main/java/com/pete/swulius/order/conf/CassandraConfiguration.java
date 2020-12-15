package com.pete.swulius.order.conf;


import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createKeyspace;
import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.dropKeyspace;

import java.net.InetSocketAddress;
import java.nio.file.Paths;

import com.datastax.driver.extras.codecs.joda.InstantCodec;
import com.datastax.oss.driver.api.core.type.codec.TypeCodec;
import com.datastax.oss.driver.api.core.type.codec.registry.MutableCodecRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;


@Configuration
public class CassandraConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(CassandraConfiguration.class);

    // Contact point hostname, single host
    @Value("${cassandra.contactPoint:127.0.0.1}")
    protected String cassandraHost;

    // Contact point port
    @Value("${cassandra.port:9042}")
    protected int cassandraPort;

    // DataCenter name, required from v2.
    @Value("${cassandra.localDataCenterName:datacenter1}")
    protected String localDataCenterName = "datacenter1";

    // Keyspace Name
    @Value("${cassandra.keyspaceName:kara}")
    public String keyspaceName = "kara";

    // Option to drop schema and generate table again at startup
    @Value("${cassandra.dropSchema:false}")
    public boolean dropSchema;




    public CassandraConfiguration() {}

    public CassandraConfiguration(
            String cassandraHost, int cassandraPort, String localDataCenterName,
            String keyspaceName,  boolean dropSchema) {
        super();
        this.cassandraHost       = cassandraHost;
        this.cassandraPort       = cassandraPort;
        this.keyspaceName        = keyspaceName;
        this.dropSchema          = dropSchema;
        this.localDataCenterName = localDataCenterName;
    }


    @Bean
    public CqlIdentifier keyspace() {
        return CqlIdentifier.fromCql(keyspaceName);
    }

    @Bean
    public CqlSession cqlSession() {
        logger.info("Creating Keyspace and expected table in Cassandra if not present.");

        CqlSession session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress(getCassandraHost(), getCassandraPort()))
                //.withCloudSecureConnectBundle(Paths.get("secure-connect-killrvideocluster.zip"))
                .withAuthCredentials("KVUser","KVPassword")
                .withKeyspace(keyspace())
                //.addTypeCodecs(InstantCodec.instance)s
                .withLocalDatacenter(getLocalDataCenterName())
                .build();

        return session;
    }


    public String getCassandraHost() {
        return cassandraHost;
    }
    public void setCassandraHost(String cassandraHost) {
        this.cassandraHost = cassandraHost;
    }
    public int getCassandraPort() {
        return cassandraPort;
    }
    public void setCassandraPort(int cassandraPort) {
        this.cassandraPort = cassandraPort;
    }
    public String getLocalDataCenterName() {
        return localDataCenterName;
    }
    public void setLocalDataCenterName(String localDataCenterName) {
        this.localDataCenterName = localDataCenterName;
    }
    public String getKeyspaceName() {
        return keyspaceName;
    }
    public void setKeyspaceName(String keyspaceName) {
        this.keyspaceName = keyspaceName;
    }
    public boolean isDropSchema() {
        return dropSchema;
    }
    public void setDropSchema(boolean dropSchema) {
        this.dropSchema = dropSchema;
    }
}

