package com.rds.pbrecruitment.dtos;

import java.util.Map;

public record RepoWithStatDto(String repo, Map<String, Float> stat) {}

