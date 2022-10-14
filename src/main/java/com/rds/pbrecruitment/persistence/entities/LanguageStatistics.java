package com.rds.pbrecruitment.persistence.entities;

import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Table(name = "language_statistics")
@Getter
@Setter
@NoArgsConstructor
@Entity
@TypeDefs({
  @TypeDef(name = "json", typeClass = JsonType.class)
})
public class LanguageStatistics {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Setter(value = AccessLevel.NONE)
  private Long id;

  //  Index desc
  private LocalDateTime createdAt;

  @Type(type = "json")
  @Column(columnDefinition = "jsonb")
  private Map<String, Float> percentage = new HashMap<>();

  @ManyToOne
  private GitRepo gitRepo;

  public LanguageStatistics(final Map<String, Float> percentage) {
    this.percentage = percentage;
    this.createdAt = LocalDateTime.now();
  }
}
