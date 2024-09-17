package com.mtfn.kafka_example.domain.entity;

import com.mtfn.kafka_example.annotation.Crop;
import com.mtfn.kafka_example.util.FieldUtil;
import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "store", indexes = {@Index(name = "ix_store_code", columnList = "code")},
        uniqueConstraints = {@UniqueConstraint(columnNames = "code", name = "ix_unique_code")})
public class Store extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 3545963482642446666L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false, length = 30)
    private String code;

    @NotNull
    @Column(name = "name", nullable = false, length = 100)
    @Crop(100)
    private String name;

    @Column(name = "phone_number", length = 25)
    private String phoneNumber;

    @PreUpdate
    @PrePersist
    void cropField() {
        FieldUtil.cropValueWithAnnotation(this);
    }
}