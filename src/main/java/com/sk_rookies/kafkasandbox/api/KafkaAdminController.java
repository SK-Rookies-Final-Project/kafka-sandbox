package com.sk_rookies.kafkasandbox.api;

import com.sk_rookies.kafkasandbox.schema.SchemaService;
import com.sk_rookies.kafkasandbox.topic.TopicService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.confluent.kafka.schemaregistry.client.SchemaMetadata;
import org.apache.kafka.clients.admin.TopicDescription;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class KafkaAdminController {

    private final TopicService topicService;
    private final SchemaService schemaService;

    // === 토픽 생성 ===
    @PostMapping("/topics")
    public ResponseEntity<?> createTopic(@RequestBody CreateTopicReq req) {
        try {
            topicService.createTopic(req.name, req.partitions, req.replicationFactor);
            return ResponseEntity.ok("Created topic: " + req.name);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // === Avro 스키마 등록 ===
    @PostMapping("/schemas/avro")
    public ResponseEntity<?> registerSchema(@RequestBody RegisterSchemaReq req) {
        try {
            int id = schemaService.registerAvro(req.subject, req.schema);
            return ResponseEntity.ok("Registered schema id=" + id);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Data
    public static class CreateTopicReq {
        public String name;
        public int partitions;
        public short replicationFactor;
    }

    @Data
    public static class RegisterSchemaReq {
        public String subject; // e.g. my-topic-value
        public String schema;  // Avro schema JSON string
    }

    // 1) 토픽 리스트
    @GetMapping("/topics")
    public ResponseEntity<?> listTopics() {
        try {
            List<String> topics = topicService.listTopics();
            return ResponseEntity.ok(topics);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // 1-추가) (선택) 토픽 상세
//    @PostMapping("/topics/describe")
//    public ResponseEntity<?> describeTopics(@RequestBody List<String> topicNames) {
//        try {
//            Map<String, TopicDescription> map = topicService.describeTopics(topicNames);
//            return ResponseEntity.ok(map);
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body(e.getMessage());
//        }
//    }

    // 2) 스키마 subject 리스트
    @GetMapping("/schemas/subjects")
    public ResponseEntity<?> listSubjects() {
        try {
            List<String> subjects = schemaService.listSubjects();
            return ResponseEntity.ok(subjects);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // 3) 특정 subject 최신 버전 스키마
    @GetMapping("/schemas/{subject}/latest")
    public ResponseEntity<?> getLatest(@PathVariable String subject) {
        try {
            SchemaMetadata meta = schemaService.getLatest(subject);
            // 필요한 정보만 골라서 반환(그대로 반환해도 됨)
            return ResponseEntity.ok(Map.of(
                    "subject", subject,
                    "id", meta.getId(),
                    "version", meta.getVersion(),
                    "schema", meta.getSchema()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}