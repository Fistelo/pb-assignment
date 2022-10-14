package com.rds.pbrecruitment.services;

import com.rds.pbrecruitment.dtos.RepoWithStatDto;
import com.rds.pbrecruitment.errors.ApiException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.rds.pbrecruitment.errors.ApiError.GITHUB_API_FAILED;
import static com.rds.pbrecruitment.services.GithubFetchingService.PB_ORGANIZATION_NAME;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GithubFetchingServiceTest {

    @Mock
    private LanguageStatisticsService languageStatisticsService;
    @Mock
    private GitHub githubApi;
    @InjectMocks
    private GithubFetchingService service;

    @Test
    void should_correctly_map_language_bytes_to_percentages () throws IOException {
        final Map<String, Long> languagesStats = Map.of(
            "language1", 3000L,
            "language2", 5000L,
            "language3", 2000L
        );
        final String repoName = "repo1";
        mock_github("repo1", languagesStats);

        service.fetchData();

        final var languagePercentages = Map.of(
            "language1", 0.3f,
            "language2", 0.5f,
            "language3", 0.2f
        );
        final var convertedStats = List.of(new RepoWithStatDto(repoName, languagePercentages));
        verify(languageStatisticsService).saveLanguageStatisticsBatch(convertedStats);
    }

    @Test
    void should_throw_api_exception_on_connection_problem() throws IOException {
        when(githubApi.getOrganization(PB_ORGANIZATION_NAME)).thenThrow(IOException.class);

        final ApiException exception = assertThrows(ApiException.class, () -> service.fetchGithubStatisticsEveryday());

        assertThat(exception.getError()).isEqualTo(GITHUB_API_FAILED);
    }

    private void mock_github(final String repoName, final Map<String, Long> languagesStats) throws IOException {
        final GHOrganization organizationMock = mock(GHOrganization.class);
        final GHRepository repositoryMock = mock(GHRepository.class);
        when(githubApi.getOrganization(PB_ORGANIZATION_NAME)).thenReturn(organizationMock);
        when(organizationMock.getRepositories()).thenReturn(Map.of(repoName, repositoryMock));
        when(repositoryMock.listLanguages()).thenReturn(languagesStats);
    }
}