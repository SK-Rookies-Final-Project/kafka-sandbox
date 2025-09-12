package com.sk_rookies.kafkasandbox.schema;


import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SchemaRegistryFactory {

    @Value("${schema-registry.url}")
    private String srUrl;
    @Value("${schema-registry.basic-auth-user-info:}")
    private String basicAuthUserInfo;

    @Value("${schema-registry.basic-auth-credentials-source:USER_INFO}")
    private String basicAuthCredSource;

    public SchemaRegistryClient client() {
        Map<String, Object> cfg = new HashMap<>();
        if (!basicAuthUserInfo.isBlank()) {
            cfg.put("basic.auth.credentials.source", basicAuthCredSource);
            cfg.put("basic.auth.user.info", basicAuthUserInfo);
        }
        // Cached client: 1000은 캐시 용량
        return new CachedSchemaRegistryClient(srUrl, 1000, cfg);
    }
}