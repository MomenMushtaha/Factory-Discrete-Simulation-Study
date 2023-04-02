import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InspectorTest {
    @Test
    void testInspector() {
        Inspector i = new Inspector(1, 0);
        assertEquals(1, i.getId());
        assertEquals(0, i.getQueue());
    }

    @Test
    void getQueue() {
    }

    @Test
    void setQueue() {
    }

    @Test
    void getFinishTime() {
    }

    @Test
    void setFinishTime() {
    }
}