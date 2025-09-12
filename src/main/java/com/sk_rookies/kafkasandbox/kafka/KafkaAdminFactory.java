package com.sk_rookies.kafkasandbox.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Slf4j
@Component
public class KafkaAdminFactory {

    @Value("${kafka.seoul.bootstrap-servers}")
    private String bootstrap;

    @Value("${kafka.seoul.username}")
    private String username;

    @Value("${kafka.seoul.password}")
    private String password;

    public AdminClient createAdminClient() {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
        props.put("security.protocol", "SASL_PLAINTEXT");
        props.put("sasl.mechanism", "SCRAM-SHA-512");
        props.put("sasl.jaas.config",
                "org.apache.kafka.common.security.scram.ScramLoginModule required " +
                        "username=\"" + username + "\" password=\"" + password + "\";");
        log.info("Creating AdminClient to {}", bootstrap);
        return AdminClient.create(props);
    }
}