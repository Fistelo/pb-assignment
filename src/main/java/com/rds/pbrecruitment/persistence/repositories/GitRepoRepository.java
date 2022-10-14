package com.rds.pbrecruitment.persistence.repositories;

import com.rds.pbrecruitment.persistence.entities.GitRepo;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface GitRepoRepository extends PagingAndSortingRepository<GitRepo, Long> {

    Optional<GitRepo> findByName(String name);
}
