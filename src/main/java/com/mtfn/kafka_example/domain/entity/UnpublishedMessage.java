package com.mtfn.kafka_example.domain.entity;

import com.mtfn.kafka_example.enums.AggregateType;
import com.mtfn.kafka_example.enums.KafkaMessageStatus;
import jakarta.persistence.*;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
@Table(name = "unpublished_message", indexes = {
        @Index(columnList = "correlation_id", name = "ix_unpublished_message_correlation_id")
})
public class UnpublishedMessage extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 8200202251805383998L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean active = Boolean.TRUE;


    @Column(name = "failure_message", length = 500)
    private String failureMessage;

    @Column(name = "retry_count")
    private Integer retryCount;

    @Column(name = "correlation_id", nullable = false, updatable = false)
    private String correlationId;

    @NotNull(message = "dead letter message topic must not be null")
    @Column(name = "topic", nullable = false, length = 100)
    private String topic;

    @Enumerated(EnumType.STRING)
    @Column(name = "aggregate_type", nullable = false, length = 50)
    private AggregateType aggregateType;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_status", nullable = false)
    private KafkaMessageStatus kafkaMessageStatus;

    @NotNull(message = "message payload must not be null")
    @Column(name = "payload", nullable = false, columnDefinition = "json")
    @ToString.Exclude
    @JdbcTypeCode(SqlTypes.JSON)
    private String payload;
}

