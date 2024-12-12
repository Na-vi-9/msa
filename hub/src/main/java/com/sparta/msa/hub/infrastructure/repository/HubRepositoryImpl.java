package com.sparta.msa.hub.infrastructure.repository;

import com.sparta.msa.hub.domain.entity.Hub;
import com.sparta.msa.hub.domain.repository.HubRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HubRepositoryImpl extends JpaRepository<Hub, UUID>, HubRepository {
}
