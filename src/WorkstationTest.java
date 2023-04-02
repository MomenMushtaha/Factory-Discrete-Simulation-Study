import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WorkstationTest {


        @Test
        void testWorkstation() {
            Workstation w = new Workstation(1);
            assertEquals(1, w.getId());
            assertEquals(0, w.getBuffer());

            Component c1 = new Component("C1", 1, 0, 1, 1, 1);
            w.addComponentToBuffer(c1);
            assertEquals(1, w.getBufferCountForComponentType("C1"));

            w.removeComponentFromBuffer(c1);
            assertEquals(0, w.getBufferCountForComponentType("C1"));
        }
    @Test
    void getId() {
    }

    @Test
    void getBuffer() {
    }

    @Test
    void getFinishTime() {
    }

    @Test
    void setFinishTime() {
    }

    @Test
    void addComponentToBuffer() {
    }

    @Test
    void removeComponentFromBuffer() {
    }

    @Test
    void getBufferCountForComponentType() {
    }

    @Test
    void isInspectorBlocked() {
    }
}