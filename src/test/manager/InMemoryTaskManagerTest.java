package test.manager;

import main.manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;

public class InMemoryTaskManagerTest extends TaskManagerTest {


    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
    }
}