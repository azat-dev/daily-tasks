package com.azatdev.dailytasks.presentation.api.rest.task;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.jayway.jsonpath.JsonPath;

import com.azatdev.dailytasks.domain.models.Backlog;
import com.azatdev.dailytasks.domain.models.Task;
import com.azatdev.dailytasks.domain.usecases.ListTasksInBacklogUseCase;
import com.azatdev.dailytasks.domain.usecases.TestDomainDataGenerator;
import com.azatdev.dailytasks.utils.Result;

record TaskResponse(Long id) {
}

@FunctionalInterface
interface MapTaskToResponse {

    TaskResponse map(Task task);
}

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class TaskControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private ListTasksInBacklogUseCase listTasksInBacklogUseCase;

    @MockBean
    private MapTaskToResponse mapTaskToResponse;

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
                startDate,
                backlogDuration
            )
        ).willReturn(Result.success(tasks));

        // When
        var response = restTemplate.getForEntity(
            "/tasks/2023-11-11/week",
            String.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        then(listTasksInBacklogUseCase).should(times(1))
            .execute(
                startDate,
                backlogDuration
            );

        then(mapTaskToResponse).should(times(tasks.size()))
            .map(any(Task.class));

        final var context = JsonPath.parse(response.getBody());

        context.read(
            "$.length()",
            Integer.class
        )
            .equals(tasks.size());

        // context.read("$.id", Long.class)
        // .equals(tasks.get(0).id());
    }
}
