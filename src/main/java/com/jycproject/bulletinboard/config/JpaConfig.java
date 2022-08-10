package com.jycproject.bulletinboard.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing
@Configuration // configuration bin이 된다.
public class JpaConfig {
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of("jyc"); // TODO: 스프링 시큐리티로 인증 기능을 붙이게 될 때, 수정할 것!
    }
}
