package com.sparta.msa.hub.domain.repository;

import com.sparta.msa.hub.domain.entity.Hub;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface HubRepository {

    Hub save(Hub hub);

    Optional<Hub> findByHubUUID(UUID hubUUID);

    //Base Entity 연결 이후 사용
//    @Modifying
//    @Query("UPDATE Hub h SET h.isDeleted = true, h.deletedAt = CURRENT_TIMESTAMP, h.deletedBy = :deletedBy WHERE h.id = :id")
//    void softDelete(UUID hubUUID, String deletedManagerId);
}
