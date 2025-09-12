package com.sk_rookies.kafkasandbox.topic;

import com.sk_rookies.kafkasandbox.kafka.KafkaAdminFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.errors.TopicExistsException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopicService {

    private final KafkaAdminFactory factory;

    public void createTopic(String name, int partitions, short replicationFactor) throws Exception {
        try (AdminClient admin = factory.createAdminClient()) {
            NewTopic topic = new NewTopic(name, partitions, replicationFactor);
            try {
                admin.createTopics(Collections.singleton(topic)).all().get();
                log.info("✅ Created topic: {}", name);
            } catch (Exception e) {
                if (e.getCause() instanceof TopicExistsException) {
                    log.warn("⚠️ Topic already exists: {}", name);
                } else {
                    throw e;
                }
            }
        }
    }


    public List<String> listTopics() throws Exception {
        try (AdminClient admin = factory.createAdminClient()) {
            return admin.listTopics()
                    .names()
                    .get()
                    .stream()
                    .sorted()
                    .collect(Collectors.toList());
        }
    }
}