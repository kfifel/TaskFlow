package com.taskflow.service;

import com.taskflow.entity.Task;
import com.taskflow.entity.User;
import com.taskflow.exception.UnauthorizedException;
import com.taskflow.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @InjectMocks
    private TaskServiceImpl taskServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void test_request_change_task() {

        User principal = new User();
        principal.setNumberOfChangeTokens(2); // Set an initial number of change tokens

        Task task = new Task();
        task.setId(1L);
        task.setUser(new User()); // Set some user to the task for testing

        // Call the method to test
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            taskServiceImpl.requestChangeTask(1L);
        });
    }


}
