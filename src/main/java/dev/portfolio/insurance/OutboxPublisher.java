package dev.portfolio.insurance;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OutboxPublisher {
    private final OutboxEventRepository repository;
    private final KafkaTemplate<String, String> kafka;
    @Value("${app.kafka.topic}") private String topic;

    public OutboxPublisher(OutboxEventRepository repository, KafkaTemplate<String, String> kafka) {
        this.repository = repository; this.kafka = kafka;
    }

    @Scheduled(fixedDelay = 3000)
    @Transactional
    public void publishPending() {
        for (OutboxEvent event : repository.findTop50ByPublishedAtIsNullOrderByCreatedAtAsc()) {
            try {
                kafka.send(topic, event.getAggregateId(), event.getPayload()).get();
                event.markPublished();
            } catch (Exception ex) {
                // Leave it unpublished; the next scheduled run retries it.
                break;
            }
        }
    }
}
