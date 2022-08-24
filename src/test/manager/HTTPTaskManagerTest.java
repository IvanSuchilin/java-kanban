package test.manager;

import main.Server.KVServer;
import main.manager.HTTPTaskManager;
import main.manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HTTPTaskManagerTest extends TaskManagerTest<HTTPTaskManager> {

    private KVServer kvServer;

    @BeforeEach
    void setUp() throws Exception {
        taskManager = new HTTPTaskManager("http://localhost:8078");
        kvServer = new KVServer();

        kvServer.start();
    }

    @AfterEach
    void tearDown() {
        kvServer.start();
    }

    @Disabled
    @Test
    void save() {
    }

    @Test
    void loadFromKVS() {
    }
}