package com.rds.pbrecruitment.config;

import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@Slf4j
@Configuration
@EnableScheduling
public class AppConfig {

    @Value("${github.token}")
    protected String githubToken;

    @Bean
    public GitHub githubClient() throws IOException {
        if (githubToken.isBlank()) {
            log.info("Running app with anonymous Github user");
            return new GitHubBuilder().build();
        }

        log.info("Running app with Github token");
        return new GitHubBuilder()
                .withJwtToken(githubToken)
                .build();
    }
}
