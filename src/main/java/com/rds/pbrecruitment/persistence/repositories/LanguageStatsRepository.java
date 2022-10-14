package com.rds.pbrecruitment.persistence.repositories;

import com.rds.pbrecruitment.persistence.entities.LanguageStatistics;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LanguageStatsRepository extends PagingAndSortingRepository<LanguageStatistics, Long> {

    Optional<LanguageStatistics> findFirstByGitRepoIdOrderByCreatedAtDesc(final Long repoId);
    Optional<LanguageStatistics> findFirstByGitRepoNameOrderByCreatedAtDesc(final String name);


}
