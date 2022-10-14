package com.rds.pbrecruitment.services;


import com.rds.pbrecruitment.persistence.entities.GitRepo;
import com.rds.pbrecruitment.persistence.entities.LanguageStatistics;
import com.rds.pbrecruitment.persistence.repositories.GitRepoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class LanguageStatisticsServiceTest {
    @Autowired
    public LanguageStatisticsService service;

    @Autowired
    public GitRepoRepository gitRepoRepository;

    @Test
    void should_get_language_latest_stats_by_id() {
        final var langStats = Map.of("language", 0.4f);
        final GitRepo repo = createRepoWithStats("reponame", langStats);

        final Map<String, Float> statistics = service.getLatestLanguageStatsByRepoId(repo.getId());

        assertThat(statistics).isEqualTo(langStats);
    }

    @Test
    void should_get_language_latest_stats_by_name(){
        final String repoName = "reponame";
        final var langStats = Map.of("language", 0.4f);
        createRepoWithStats(repoName, langStats);

        final Map<String, Float> statistics = service.getLatestLanguageStatsByRepoName(repoName);

        assertThat(statistics).isEqualTo(langStats);
    }

    @Test
    void should_save_language_statistics_in_batch() {

    }

    private GitRepo createRepoWithStats(final String repoName, final Map<String, Float> latestStats){
        final GitRepo repo = new GitRepo(repoName);
        final LanguageStatistics youngerStats = new LanguageStatistics(latestStats);
        youngerStats.setCreatedAt(LocalDateTime.of(2022, 10 ,14, 10, 0));
        final LanguageStatistics olderStats = new LanguageStatistics(Map.of("otherLanguage", 0.243f));
        olderStats.setCreatedAt(LocalDateTime.of(2022, 10 ,13, 10, 0));
        repo.addStat(youngerStats);
        repo.addStat(olderStats);
        return gitRepoRepository.save(repo);
    }
}