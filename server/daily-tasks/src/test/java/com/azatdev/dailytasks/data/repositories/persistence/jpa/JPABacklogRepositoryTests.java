package com.azatdev.dailytasks.data.repositories.persistence.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import com.azatdev.dailytasks.data.repositories.persistence.entities.BacklogData;
import java.time.LocalDate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class JPABacklogRepositoryTests {

  @Autowired private JPABacklogRepository jpaBacklogRepository;

  @Autowired TestEntityManager entityManager;

  private LocalDate anyDate() {
    return LocalDate.of(2021, 1, 1);
  }

  private BacklogData.Duration anyDuration() {
    return BacklogData.Duration.DAY;
  }

  void findByStartDateAndDurationEmptyDbShouldReturnNull() {

    // Given
    final var startDate = this.anyDate();
    final var duration = this.anyDuration();

    // Empty DB

    // When
    var result = jpaBacklogRepository.findByStartDateAndDuration(startDate, duration);

    // Then
    assertThat(result).isNull();
  }

  void findByStartDateAndDurationEmptyDbShouldReturnCorrectRecord() {

    // Given
    final var startDate = this.anyDate();
    final var duration = this.anyDuration();

    BacklogData expectedBacklog =
        entityManager.persistFlushFind(new BacklogData(startDate, duration));
    BacklogData backlogWithWrongDate =
        entityManager.persistFlushFind(new BacklogData(startDate.plusDays(1), duration));
    BacklogData backlogWithWrongDuration =
        entityManager.persistFlushFind(new BacklogData(startDate, BacklogData.Duration.WEEK));

    // When
    var result = jpaBacklogRepository.findByStartDateAndDuration(startDate, duration);

    // Then
    assertThat(result).isEqualTo(expectedBacklog);
    assertThat(result).isIn(backlogWithWrongDate, backlogWithWrongDuration);
  }
}
