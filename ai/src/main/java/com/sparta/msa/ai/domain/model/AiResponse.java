package com.sparta.msa.ai.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "p_ai")
public class AiResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String answer;
}