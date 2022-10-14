package com.rds.pbrecruitment.services;

import com.rds.pbrecruitment.dtos.RepoWithStatDto;
import com.rds.pbrecruitment.errors.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.rds.pbrecruitment.errors.ApiError.GITHUB_API_FAILED;

@Slf4j
@Service
@RequiredArgsConstructor
public class GithubFetchingService {

    private final LanguageStatisticsService languageStatisticsService;
    private final GitHub githubApi;

    private static final String PB_ORGANIZATION_NAME = "productboard";

    public void fetchData() {
        try {
            final Map<String, GHRepository> repositories = githubApi.getOrganization(PB_ORGANIZATION_NAME).getRepositories();
            final List<RepoWithStatDto> repoWithStatistics = new ArrayList<>();

            for (final Map.Entry<String, GHRepository> repo : repositories.entrySet()) {
                final Map<String, Float> language_percentages = convert_bytes_to_percentages(repo.getValue().listLanguages());
                repoWithStatistics.add(new RepoWithStatDto(repo.getKey(), language_percentages));
            }
            log.info("Going to save " + repoWithStatistics.size() + " repositories.");
            log.debug("Repositories to save" + repoWithStatistics);

            languageStatisticsService.saveLanguageStatisticsBatch(repoWithStatistics);

        } catch (IOException e) {
            throw new ApiException(GITHUB_API_FAILED);
        }
    }

    private Map<String, Float> convert_bytes_to_percentages(final Map<String, Long> languageToByteSize) {
        final long bytes_sum = languageToByteSize.values().stream()
                .mapToLong(Long::longValue)
                .sum();

        return languageToByteSize.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        lang -> (float) lang.getValue() / bytes_sum
                ));
    }
}
