import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComponentTest {

    @Test
    void testComponent() {
        Component c = new Component("C1", 1, 0.0, 1, 1, 1);
        assertEquals("C1", c.getType());
        assertEquals(1, c.getInspectorId());
        assertEquals(0.0, c.getArrivalTime());
        assertEquals(1, c.getRequiredForP1());
        assertEquals(1, c.getRequiredForP2());
        assertEquals(1, c.getRequiredForP3());
    }
    @Test
    void getType() {
    }

    @Test
    void getInspectorId() {
    }

    @Test
    void getArrivalTime() {
    }

    @Test
    void getRequiredForP1() {
    }

    @Test
    void getRequiredForP2() {
    }

    @Test
    void getRequiredForP3() {
    }
}