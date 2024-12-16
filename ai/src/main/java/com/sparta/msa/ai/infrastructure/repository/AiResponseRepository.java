package com.sparta.msa.ai.infrastructure.repository;

import com.sparta.msa.ai.domain.model.AiResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AiResponseRepository extends JpaRepository<AiResponse, UUID> {
}
