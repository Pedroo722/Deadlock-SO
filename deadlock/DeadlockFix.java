package deadlock;

public class DeadlockFix {
    // Dois recursos para 4 processos
    private static final Object recursoA = new Object();
    private static final Object recursoB = new Object();

    public static void main(String[] args) {
        // Gerenciador de recursos que garante a ordem fixa de aquisição
        ResourceManager rm = new ResourceManager(recursoA, recursoB);

        // Processo 1: solicita os recursos na ordem A, depois B
        Thread processo1 = new Thread(() -> {
            rm.acquire("Processo 1", recursoA, recursoB);
        });

        // Processo 3: solicita os recursos na ordem B, depois A
        Thread processo3 = new Thread(() -> {
            rm.acquire("Processo 3", recursoB, recursoA);
        });

        // Processo 2: solicita os recursos na ordem A, depois B
        Thread processo2 = new Thread(() -> {
            rm.acquire("Processo 2", recursoA, recursoB);
        });

        // Processo 4: solicita os recursos na ordem B, depois A
        Thread processo4 = new Thread(() -> {
            rm.acquire("Processo 4", recursoB, recursoA);
        });

        // Inicia os processos
        processo1.start();
        processo3.start();

        // Pequeno atraso para simular a concorrência
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        processo2.start();
        processo4.start();

        // Aguarda a finalização de todos os processos
        try {
            processo1.join();
            processo2.join();
            processo3.join();
            processo4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Mensagem final indicando que todos os processos foram executados
        System.out.println("Processos executados");
    }

    /**
     * Gerenciador de recursos que força a aquisição dos locks em ordem fixa
     * para evitar deadlock.
     */
    static class ResourceManager {
        private final Object resA;
        private final Object resB;

        public ResourceManager(Object resA, Object resB) {
            this.resA = resA;
            this.resB = resB;
        }

        /**
         * Método que realiza a aquisição dos recursos. Independentemente da
         * ordem solicitada (first, second), ele determina a ordem correta,
         * garantindo que todos os processos obtenham os locks na mesma sequência.
         *
         * @param processName Nome do processo para exibição
         * @param first Primeiro recurso solicitado pelo processo
         * @param second Segundo recurso solicitado pelo processo
         */
        public void acquire(String processName, Object first, Object second) {
            // Determina uma ordem global fixa: menor hashCode primeiro
            Object lock1, lock2;
            if (System.identityHashCode(first) < System.identityHashCode(second)) {
                lock1 = first;
                lock2 = second;
            } else {
                lock1 = second;
                lock2 = first;
            }

            // Adquire o primeiro lock
            synchronized (lock1) {
                System.out.println(processName + " adquiriu " + getResourceName(lock1));
                // Simula alguma operação
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(processName + " tentando adquirir " + getResourceName(lock2) + "...");
                // Adquire o segundo lock
                synchronized (lock2) {
                    System.out.println(processName + " adquiriu " + getResourceName(lock2));
                }
            }
        }

        /**
         * Retorna o nome do recurso com base na referência.
         */
        private String getResourceName(Object resource) {
            if (resource == resA) {
                return "recurso A";
            } else if (resource == resB) {
                return "recurso B";
            } else {
                return "recurso desconhecido";
            }
        }
    }
}