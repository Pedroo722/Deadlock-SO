package detector;

import java.util.*;

public class Detector {
    private final int numProcesses;
    private final List<List<Integer>> waitForGraph;

    public Detector(int numProcesses) {
        this.numProcesses = numProcesses;
        this.waitForGraph = new ArrayList<>();
        for (int i = 0; i < numProcesses; i++) {
            waitForGraph.add(new ArrayList<>());
        }
    }

    public void addEdge(int from, int to) {
        waitForGraph.get(from).add(to);
    }

    public boolean hasDeadlock() {
        boolean[] visited = new boolean[numProcesses];
        boolean[] recursionStack = new boolean[numProcesses];
        
        for (int i = 0; i < numProcesses; i++) {
            if (detectCycle(i, visited, recursionStack)) {
                return true;
            }
        }
        return false;
    }

    private boolean detectCycle(int process, boolean[] visited, boolean[] recursionStack) {
        if (recursionStack[process]) return true;
        if (visited[process]) return false;
        
        visited[process] = true;
        recursionStack[process] = true;
        
        for (int neighbor : waitForGraph.get(process)) {
            if (detectCycle(neighbor, visited, recursionStack)) {
                return true;
            }
        }
        
        recursionStack[process] = false;
        return false;
    }

    public static void main(String[] args) {
        Detector detector = new Detector(4);
        detector.addEdge(0, 1);
        detector.addEdge(1, 2);
        detector.addEdge(2, 3);
        detector.addEdge(3, 1); // Cria um ciclo (deadlock)
        
        if (detector.hasDeadlock()) {
            System.out.println("Deadlock detectado!");
        } else {
            System.out.println("Nenhum deadlock detectado.");
        }
    }
}
