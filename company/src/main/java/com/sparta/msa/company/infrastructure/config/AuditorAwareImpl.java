package com.sparta.msa.company.infrastructure.config;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // 여기서는 "master"를 반환하는 예시로, 실제로는 Spring Security 등을 사용하여 로그인한 사용자 ID를 반환해야 합니다.
        return Optional.of("master");
    }
}
