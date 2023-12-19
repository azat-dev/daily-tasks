package com.azatdev.dailytasks.data.dao;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.Test;

import com.azatdev.dailytasks.data.dao.activitysession.DeleteAllActivitySessionsOfTaskDaoImpl;
import com.azatdev.dailytasks.data.jpa.JpaActivitySessionsRepository;
import com.azatdev.dailytasks.domain.interfaces.dao.DeleteAllActivitySessionsOfTaskDao;

public class DeleteAllActivitySessionsOfTaskDaoImplTest {

    private record SUT(
        DeleteAllActivitySessionsOfTaskDao dao,
        JpaActivitySessionsRepository jpaActivitySessionsRepository
    ) {
    }

    private static SUT createSUT() {
        final var jpaActivitySessionsRepository = mock(JpaActivitySessionsRepository.class);
        final var dao = new DeleteAllActivitySessionsOfTaskDaoImpl(jpaActivitySessionsRepository);

        return new SUT(
            dao,
            jpaActivitySessionsRepository
        );
    }

    @Test
    public void execute() {

        // Given
        final var taskId = 111L;
        final var sut = createSUT();

        // When
        sut.dao.execute(taskId);

        // Then

        then(sut.jpaActivitySessionsRepository).should(times(1))
            .deleteAllByTaskId(taskId);
    }
}
