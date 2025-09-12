package com.sk_rookies.kafkasandbox.schema;

import io.confluent.kafka.schemaregistry.ParsedSchema;
import io.confluent.kafka.schemaregistry.avro.AvroSchema;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import io.confluent.kafka.schemaregistry.client.SchemaMetadata;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SchemaService {

    private final SchemaRegistryFactory factory;

    public int registerAvro(String subject, String rawSchema) throws Exception {
        SchemaRegistryClient client = factory.client();
        ParsedSchema schema = new AvroSchema(rawSchema);
        return client.register(subject, schema);
    }

    public List<String> listSubjects() throws Exception {
        return factory.client().getAllSubjects().stream().sorted().toList();
    }

    public SchemaMetadata getLatest(String subject) throws Exception {
        return factory.client().getLatestSchemaMetadata(subject);
    }
}
