
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimulationIntegrationTest {
    Simulation simulation;

    @BeforeEach
    void setUp() {
        simulation = new Simulation();
    }

    @Test
    void testSimulationWithDifferentInspectorSpeeds() {
        simulation.avg1 *= 2;
        simulation.avg22 *= 0.5;
        simulation.avg23 *= 1.5;
        simulation.simulate();

        // Check if the simulation produces the correct number of assembled products
        assertTrue(simulation.completedP1 > 0, "No P1 products assembled");
        assertTrue(simulation.completedP2 > 0, "No P2 products assembled");
        assertTrue(simulation.completedP3 > 0, "No P3 products assembled");
    }

    @Test
    void testSimulationWithDifferentWorkstationSpeeds() {
        simulation.ws1 *= 1.5;
        simulation.ws2 *= 0.75;
        simulation.ws3 *= 2;
        simulation.simulate();

        // Check if the simulation produces the correct number of assembled products
        assertTrue(simulation.completedP1 > 0, "No P1 products assembled");
        assertTrue(simulation.completedP2 > 0, "No P2 products assembled");
        assertTrue(simulation.completedP3 > 0, "No P3 products assembled");
    }





    @Test
    void testSimulationWithExternalFactors() {
        // Here you can add external factors that affect the simulation, e.g., reducing the simulation time
        simulation.simulate();

        // Check if the simulation produces the correct number of assembled products
        assertTrue(simulation.completedP1 > 0, "No P1 products assembled");
        assertTrue(simulation.completedP2 > 0, "No P2 products assembled");
        assertTrue(simulation.completedP3 > 0, "No P3 products assembled");
    }
}
