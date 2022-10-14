package com.rds.pbrecruitment.api;

import com.rds.pbrecruitment.services.GithubFetchingService;
import com.rds.pbrecruitment.services.LanguageStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(GitRepoController.GIT_REPOS_PATH)
@RequiredArgsConstructor
public class GitRepoController {
    static final String GIT_REPOS_PATH = "/v1/repos";

    private final LanguageStatisticsService langStatisticsService;
    private final GithubFetchingService githubFetchingService;

    @GetMapping("/{id}/language_stats")
    public Object fetchGitRepoLanguageStatsById(@PathVariable final long id) {
        return langStatisticsService.getLatestLanguageStatsByRepoId(id);
    }

    @GetMapping("/language_stats")
    public Object fetchGitRepoLanguageStatsById(@RequestParam final String repo_name) {
        return langStatisticsService.getLatestLanguageStatsByRepoName(repo_name);
    }

    @PostMapping("/force_fetch_data")
    public void forceFetchData() {
    //  Endpoint to test the save from Github functionality
        githubFetchingService.fetchData();
    }
}
