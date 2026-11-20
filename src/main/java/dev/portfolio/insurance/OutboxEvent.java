package dev.portfolio.insurance;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "outbox_events")
public class OutboxEvent {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false) private String aggregateId;
    @Column(nullable = false, length = 30) private String eventType;
    @Lob @Column(nullable = false) private String payload;
    @Column(nullable = false) private Instant createdAt;
    private Instant publishedAt;

    protected OutboxEvent() {}
    public OutboxEvent(String aggregateId, String eventType, String payload) {
        this.aggregateId = aggregateId; this.eventType = eventType;
        this.payload = payload; this.createdAt = Instant.now();
    }
    public Long getId() { return id; }
    public String getAggregateId() { return aggregateId; }
    public String getPayload() { return payload; }
    public Instant getPublishedAt() { return publishedAt; }
    public void markPublished() { this.publishedAt = Instant.now(); }
}
