package com.rds.pbrecruitment.services;

import com.rds.pbrecruitment.dtos.RepoWithStatDto;
import com.rds.pbrecruitment.errors.ApiException;
import com.rds.pbrecruitment.persistence.entities.GitRepo;
import com.rds.pbrecruitment.persistence.entities.LanguageStatistics;
import com.rds.pbrecruitment.persistence.repositories.GitRepoRepository;
import com.rds.pbrecruitment.persistence.repositories.LanguageStatsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.rds.pbrecruitment.errors.ApiError.RESOURCE_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class LanguageStatisticsService {

    private final GitRepoRepository gitRepoRepository;
    private final LanguageStatsRepository languageStatsRepository;

    public Object getLatestLanguageStatsByRepoId(final long id) {
        final LanguageStatistics statistics = languageStatsRepository.findFirstByGitRepoIdOrderByCreatedAtDesc(id)
                .orElseThrow(() -> new ApiException(RESOURCE_NOT_FOUND));

        return statistics.getPercentage();
    }

    public Object getLatestLanguageStatsByRepoName(final String name) {
        final LanguageStatistics statistics = languageStatsRepository.findFirstByGitRepoNameOrderByCreatedAtDesc(name)
                .orElseThrow(() -> new ApiException(RESOURCE_NOT_FOUND));

        return statistics.getPercentage();
    }

    @Transactional
    public void saveLanguageStatisticsBatch(final List<RepoWithStatDto> repoWithStats) {
        final List<GitRepo> repos_for_batch_update = new ArrayList<>();

        for (final RepoWithStatDto fetchedRepoWithStatistic: repoWithStats) {
            final LanguageStatistics statistics = new LanguageStatistics(fetchedRepoWithStatistic.stat());
            final Optional<GitRepo> existingRepo = gitRepoRepository.findByName(fetchedRepoWithStatistic.repo());

            final GitRepo gitRepo = existingRepo.orElseGet(() -> new GitRepo(fetchedRepoWithStatistic.repo()));
            gitRepo.addStat(statistics);

            repos_for_batch_update.add(gitRepo);
        }

        gitRepoRepository.saveAll(repos_for_batch_update);
    }
}
