CREATE SEQUENCE hibernate_sequence START 1;

CREATE TABLE git_repos
(
    id                   SERIAL PRIMARY KEY,
    created_at           timestamp,
    name                 varchar not null
);

CREATE TABLE language_statistics
(
    id            SERIAL PRIMARY KEY,
    created_at           timestamp,
    percentage           jsonb,
    git_repo_id          integer,

    FOREIGN KEY (git_repo_id) REFERENCES git_repos(id)
);

CREATE INDEX lang_stats_created_time_desc_idx ON language_statistics (created_at DESC NULLS LAST);
CREATE INDEX lang_stats_foreign_key_idx ON language_statistics (git_repo_id);

