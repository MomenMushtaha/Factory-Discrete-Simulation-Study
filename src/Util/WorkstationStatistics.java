package Util;

import Model.Workstation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkstationStatistics {
    private final List<Workstation> workstations;
    private final Map<Integer, Integer> componentsProcessed;

    public WorkstationStatistics() {
        workstations = new ArrayList<>();
        componentsProcessed = new HashMap<>();
    }

    public void addWorkstation(Workstation workstation) {
        workstations.add(workstation);
        componentsProcessed.put(workstation.getId(), 0);
    }

    public void incrementComponentsProcessed(int workstationId) {
        int currentCount = componentsProcessed.get(workstationId);
        componentsProcessed.put(workstationId, currentCount + 1);
    }

    public double getAverageFinishTime() {
        double totalFinishTime = 0;
        for (Workstation workstation : workstations) {
            totalFinishTime += workstation.getFinishTime();
        }
        return totalFinishTime / workstations.size();
    }

    public int getTotalComponentsProcessed() {
        int totalCount = 0;
        for (int count : componentsProcessed.values()) {
            totalCount += count;
        }
        return totalCount;
    }

    public Map<Integer, Integer> getComponentsProcessedPerWorkstation() {
        return componentsProcessed;
    }

    public double getWorkstationUtilization(int workstationId) {
        Workstation workstation = findWorkstationById(workstationId);
        if (workstation != null) {
            return (double) componentsProcessed.get(workstationId) / workstation.getFinishTime();
        }
        return 0;
    }

    public Workstation findWorkstationById(int workstationId) {
        for (Workstation workstation : workstations) {
            if (workstation.getId() == workstationId) {
                return workstation;
            }
        }
        return null;
    }
}
