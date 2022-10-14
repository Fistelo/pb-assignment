package com.rds.pbrecruitment.services;


import com.rds.pbrecruitment.dtos.RepoWithStatDto;
import com.rds.pbrecruitment.persistence.entities.GitRepo;
import com.rds.pbrecruitment.persistence.entities.LanguageStatistics;
import com.rds.pbrecruitment.persistence.repositories.GitRepoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class LanguageStatisticsServiceTest {
    private static final Map<String, Float> LANGSTATS = Map.of("language", 0.4f);
    private static final Map<String, Float> LANGSTATS_2 = Map.of("language", 0.94f);
    private static final Map<String, Float> LANGSTATS_3 = Map.of("language", 0.88f);

    @Autowired
    public LanguageStatisticsService service;

    @Autowired
    public GitRepoRepository gitRepoRepository;

    @Test
    void should_get_language_latest_stats_by_id() {
        final GitRepo repo = createRepoWithStats("reponame", LANGSTATS);

        final Map<String, Float> statistics = service.getLatestLanguageStatsByRepoId(repo.getId());

        assertThat(statistics).isEqualTo(LANGSTATS);
    }

    @Test
    void should_get_language_latest_stats_by_name(){
        final String repoName = "reponame";
        createRepoWithStats(repoName, LANGSTATS);

        final Map<String, Float> statistics = service.getLatestLanguageStatsByRepoName(repoName);

        assertThat(statistics).isEqualTo(LANGSTATS);
    }

    @Test
    void should_save_language_statistics__create_new_repo_and_update_existing_ones() {
        final String existing_repo_name = "repo1";
        createRepoWithStats(existing_repo_name, LANGSTATS);

        final List<RepoWithStatDto> repoWithStats = List.of(
            new RepoWithStatDto("repo1", LANGSTATS_2),
            new RepoWithStatDto("repo2", LANGSTATS_3)
        );

        service.saveLanguageStatisticsBatch(repoWithStats);

        System.out.println(List.of(gitRepoRepository.findAll()));
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