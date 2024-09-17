package com.mtfn.kafka_example.domain.repository;

import com.mtfn.kafka_example.domain.entity.UnpublishedMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnpublishedMessageRepository extends JpaRepository<UnpublishedMessage, Long> {

}
