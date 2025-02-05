package avestruz;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class OstrichAlgorithm {
    private final Map<Integer, Integer> resourceAllocation; // Mapeia processos para recursos
    private final Set<Integer> availableResources; // Conjunto de recursos disponíveis

    public OstrichAlgorithm(int totalResources) {
        this.resourceAllocation = new HashMap<>();
        this.availableResources = new HashSet<>();
        for (int i = 1; i <= totalResources; i++) {
            availableResources.add(i);
        }
    }

    public void requestResource(int processId, int resourceId) {
        if (availableResources.contains(resourceId)) {
            resourceAllocation.put(processId, resourceId);
            availableResources.remove(resourceId);
            System.out.println("Processo " + processId + " alocou o recurso " + resourceId);
        } else {
            System.out.println("Processo " + processId + " solicitou o recurso " + resourceId + ", mas ele já está em uso.");
        }
    }

    public void releaseResource(int processId) {
        if (resourceAllocation.containsKey(processId)) {
            int resourceId = resourceAllocation.get(processId);
            availableResources.add(resourceId);
            resourceAllocation.remove(processId);
            System.out.println("Processo " + processId + " liberou o recurso " + resourceId);
        }
    }

    public void executeProcess(int processId, int resourceId) {
        System.out.println("Processo " + processId + " iniciou.");
        requestResource(processId, resourceId);
        System.out.println("Processo " + processId + " executando...");
        releaseResource(processId);
        System.out.println("Processo " + processId + " finalizado.\n");
    }

    public static void main(String[] args) {
        OstrichAlgorithm system = new OstrichAlgorithm(3);

        system.executeProcess(1, 1);
        system.executeProcess(2, 2);
        system.executeProcess(3, 3);
        system.executeProcess(4, 1); // Simulação de solicitação de recurso já alocado
    }
}
