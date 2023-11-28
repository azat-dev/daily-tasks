package com.azatdev.dailytasks.presentation.api.rest.task;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

import com.jayway.jsonpath.JsonPath;

import com.azatdev.dailytasks.domain.models.Backlog;
import com.azatdev.dailytasks.domain.usecases.ListTasksInBacklogUseCase;
import com.azatdev.dailytasks.domain.usecases.TestDomainDataGenerator;
import com.azatdev.dailytasks.utils.Result;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private ListTasksInBacklogUseCase listTasksInBacklogUseCase;

    @Test
    void findAllTasksInBacklogShouldReturnTasksTest() {

        // Given
        LocalDate startDate = LocalDate.of(
            2023,
            11,
            11
        );

        var backlogDuration = Backlog.Duration.WEEK;

        final var tasks = List.of(
            TestDomainDataGenerator.anyTask(0L),
            TestDomainDataGenerator.anyTask(1L)
        );

        given(
            listTasksInBacklogUseCase.execute(
                any(LocalDate.class),
                any(Backlog.Duration.class)
            )
        ).willReturn(Result.success(tasks));

        // When
        var response = restTemplate.getForEntity(
            "/tasks/backlog/WEEK/for/2023-11-11",
            String.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        then(listTasksInBacklogUseCase).should(times(1))
            .execute(
                startDate,
                backlogDuration
            );

        final var responseBody = response.getBody();
        final var context = JsonPath.parse(responseBody);

        context.read(
            "$.length()",
            Integer.class
        )
            .equals(tasks.size());

        final var expectedTaskIds = tasks.stream()
            .map(task -> task.id())
            .toList();

        context.read("$.[*].id")
            .equals(expectedTaskIds);
    }
}
