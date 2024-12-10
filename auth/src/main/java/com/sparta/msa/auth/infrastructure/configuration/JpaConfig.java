package com.sparta.msa.auth.infrastructure.configuration;

import ch.qos.logback.core.net.SMTPAppenderBase;
import com.sparta.msa.auth.domain.model.User;
import com.sparta.msa.auth.presentation.request.SignUpRequestDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class JpaConfig {

}
