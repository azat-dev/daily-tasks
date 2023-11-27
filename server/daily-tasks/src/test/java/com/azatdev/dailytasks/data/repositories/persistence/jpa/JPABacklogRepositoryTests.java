package com.azatdev.dailytasks.data.repositories.persistence.jpa;

// @DataJpaTest
// class JPABacklogRepositoryTests {

//     @Autowired
//     TestEntityManager entityManager;

//     @Autowired
//     private JPABacklogRepository jpaBacklogRepository;

//     private LocalDate anyDate() {
//         return LocalDate.of(
//             2021,
//             1,
//             1
//         );
//     }

//     private BacklogData.Duration anyDuration() {
//         return BacklogData.Duration.DAY;
//     }

//     void findByStartDateAndDurationEmptyDbShouldReturnNull() {

//         // Given
//         final var startDate = this.anyDate();
//         final var duration = this.anyDuration();

//         // Empty DB

//         // When
//         var result = jpaBacklogRepository.findByStartDateAndDuration(
//             startDate,
//             duration
//         );

//         // Then
//         assertThat(result).isNull();
//     }

//     void findByStartDateAndDurationEmptyDbShouldReturnCorrectRecord() {

//         // Given
//         final var startDate = this.anyDate();
//         final var duration = this.anyDuration();

//         BacklogData expectedBacklog = entityManager.persistFlushFind(
//             new BacklogData(
//                 startDate,
//                 duration
//             )
//         );
//         BacklogData backlogWithWrongDate = entityManager.persistFlushFind(
//             new BacklogData(
//                 startDate.plusDays(1),
//                 duration
//             )
//         );
//         BacklogData backlogWithWrongDuration = entityManager.persistFlushFind(
//             new BacklogData(
//                 startDate,
//                 BacklogData.Duration.WEEK
//             )
//         );

//         // When
//         var result = jpaBacklogRepository.findByStartDateAndDuration(
//             startDate,
//             duration
//         );

//         // Then
//         assertThat(result).isEqualTo(expectedBacklog);
//         assertThat(result).isIn(
//             backlogWithWrongDate,
//             backlogWithWrongDuration
//         );
//     }
// }
