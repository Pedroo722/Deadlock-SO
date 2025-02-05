package banqueiro;

import java.util.Arrays;

public class BankerAlgorithm {
    private final int numProcesses;  // Número de processos
    private final int numResources;  // Número de tipos de recursos
    private final int[] available;   // Recursos disponíveis
    private final int[][] maximum;   // Máximo que cada processo pode pedir
    private final int[][] allocation;// Recursos atualmente alocados
    private final int[][] need;      // Recursos ainda necessários

    public BankerAlgorithm(int numProcesses, int numResources, int[] totalResources, int[][] maxClaim) {
        this.numProcesses = numProcesses;
        this.numResources = numResources;
        this.available = Arrays.copyOf(totalResources, numResources);
        this.maximum = maxClaim;
        this.allocation = new int[numProcesses][numResources];
        this.need = new int[numProcesses][numResources];

        for (int i = 0; i < numProcesses; i++) {
            for (int j = 0; j < numResources; j++) {
                need[i][j] = maximum[i][j]; // No início, necessidade é igual ao máximo declarado
            }
        }
    }

    public boolean requestResources(int process, int[] request) {
        System.out.println("Processo " + process + " solicitou recursos: " + Arrays.toString(request));

        // Verifica se o pedido excede a necessidade declarada
        for (int i = 0; i < numResources; i++) {
            if (request[i] > need[process][i]) {
                System.out.println("Erro: Processo solicitando mais do que declarou como necessário.");
                return false;
            }
        }

        // Verifica se os recursos estão disponíveis
        for (int i = 0; i < numResources; i++) {
            if (request[i] > available[i]) {
                System.out.println("Recursos insuficientes. Processo precisa esperar.");
                return false;
            }
        }

        // Aloca temporariamente os recursos
        for (int i = 0; i < numResources; i++) {
            available[i] -= request[i];
            allocation[process][i] += request[i];
            need[process][i] -= request[i];
        }

        // Verifica se o sistema continua em um estado seguro
        if (isSystemSafe()) {
            System.out.println("Recursos concedidos ao processo " + process + ".");
            return true;
        } else {
            // Reverte a alocação se o estado não for seguro
            for (int i = 0; i < numResources; i++) {
                available[i] += request[i];
                allocation[process][i] -= request[i];
                need[process][i] += request[i];
            }
            System.out.println("Sistema em estado inseguro! Solicitação negada.");
            return false;
        }
    }

    private boolean isSystemSafe() {
        boolean[] finished = new boolean[numProcesses];
        int[] work = Arrays.copyOf(available, numResources);
        int count = 0;

        while (count < numProcesses) {
            boolean allocated = false;
            for (int i = 0; i < numProcesses; i++) {
                if (!finished[i] && canProcessExecute(i, work)) {
                    for (int j = 0; j < numResources; j++) {
                        work[j] += allocation[i][j];
                    }
                    finished[i] = true;
                    allocated = true;
                    count++;
                }
            }
            if (!allocated) return false; // Se nenhum processo pôde ser finalizado, estado inseguro
        }
        return true;
    }

    private boolean canProcessExecute(int process, int[] work) {
        for (int i = 0; i < numResources; i++) {
            if (need[process][i] > work[i]) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        int numProcesses = 5;
        int numResources = 3;

        // Recursos totais disponíveis no sistema
        int[] totalResources = {10, 5, 7};

        // Máximo que cada processo pode solicitar
        int[][] maxClaim = {
            {7, 5, 3},
            {3, 2, 2},
            {9, 0, 2},
            {2, 2, 2},
            {4, 3, 3}
        };

        BankerAlgorithm banker = new BankerAlgorithm(numProcesses, numResources, totalResources, maxClaim);

        // Simulando solicitações de processos
        banker.requestResources(0, new int[]{0, 1, 0});
        banker.requestResources(1, new int[]{2, 0, 0});
        banker.requestResources(3, new int[]{1, 0, 1});
        banker.requestResources(4, new int[]{3, 3, 0});
        banker.requestResources(2, new int[]{3, 0, 2}); // Deve ser negado se for inseguro
    }
}
