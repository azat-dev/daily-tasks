package com.azatdev.dailytasks.data.repositories;

import static org.mockito.BDDMockito.*;

import java.util.UUID;

import com.azatdev.dailytasks.data.repositories.data.user.UserData;
import com.azatdev.dailytasks.data.repositories.persistence.entities.BacklogData;
import com.azatdev.dailytasks.data.repositories.persistence.entities.TaskData;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TestDataManager {

    public UserData givenUserData(UUID id) {
        final var userMock = mock(UserData.class);

        given(userMock.getId()).willReturn(id);
        return userMock;
    }

    public UserData givenUserDataWithId() {
        return givenUserData(UUID.randomUUID());
    }

    public BacklogData givenBacklogDataWithId() {
        return givenBacklogData(111L);
    }

    public BacklogData givenBacklogData(long id) {
        final var backlog = mock(BacklogData.class);
        given(backlog.getId()).willReturn(id);

        return backlog;
    }

    public TaskData givenTaskData(long id) {
        final var task = mock(TaskData.class);
        given(task.getId()).willReturn(id);

        return task;
    }

    public TaskData givenTaskDataWithId() {
        return givenTaskData(222L);
    }
}
