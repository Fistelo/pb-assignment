package com.rds.pbrecruitment.persistence.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "git_repos")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class GitRepo {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Setter(value = AccessLevel.NONE)
  private Long id;

  private LocalDateTime createdAt;

  private String name;

  @OrderBy("createdAt DESC")
  @OneToMany(mappedBy = "gitRepo", cascade = CascadeType.PERSIST, orphanRemoval = true)
  @Setter(value = AccessLevel.NONE)
  private List<LanguageStatistics> languageStats = new ArrayList<>();

  public void addStat(final LanguageStatistics languageStatistics) {
    languageStatistics.setGitRepo(this);
    languageStats.add(languageStatistics);
  }

  public GitRepo(final String name) {
    this.name = name;
    this.createdAt = LocalDateTime.now();
  }
}
