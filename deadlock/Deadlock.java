package deadlock;

public class Deadlock {
    // Dois recursos para 4 processos
    private static final Object recurso1 = new Object();
    private static final Object recurso2 = new Object();

    public static void main(String[] args) {
        // Processo 1: tenta adquirir recurso A e depois recurso B
        Thread processo1 = new Thread(() -> {
            synchronized (recurso1) {
                System.out.println("Processo 1 adquiriu recurso A");
                // Aguarda para aumentar a chance do deadlock
                try { Thread.sleep(200); } catch (InterruptedException e) { }
                System.out.println("Processo 1 tentando adquirir recurso B...");
                synchronized (recurso2) {
                    System.out.println("Processo 1 adquiriu recurso B");
                }
            }
        });

        // Processo 3: tenta adquirir recurso A e depois recurso B
        Thread processo3 = new Thread(() -> {
            synchronized (recurso2) {
                System.out.println("Processo 3 adquiriu recurso B");
                try { Thread.sleep(200); } catch (InterruptedException e) { }
                System.out.println("Processo 3 tentando adquirir recurso A...");
                synchronized (recurso1) {
                    System.out.println("Processo 3 adquiriu recurso A");
                }
            }
        });

        // Processo 2: tenta adquirir recurso A e depois recurso B
        Thread processo2 = new Thread(() -> {
            synchronized (recurso1) {
                System.out.println("Processo 2 adquiriu recurso A");
                try { Thread.sleep(200); } catch (InterruptedException e) { }
                System.out.println("Processo 2 tentando adquirir recurso B...");
                synchronized (recurso2) {
                    System.out.println("Processo 2 adquiriu recurso B");
                }
            }
        });

        // Processo 4: tenta adquirir recurso A e depois recurso B
        Thread processo4 = new Thread(() -> {
            synchronized (recurso2) {
                System.out.println("Processo 4 adquiriu recurso B");
                try { Thread.sleep(200); } catch (InterruptedException e) { }
                System.out.println("Processo 4 tentando adquirir recurso A...");
                synchronized (recurso1) {
                    System.out.println("Processo 4 adquiriu recurso A");
                }
            }
        });

        // Inicia os processos 1 e 3 primeiro para provocar deadlock entre eles
        processo1.start();
        processo3.start();

        // Aguarda um curto período para garantir que processo1 e processo3 já estejam bloqueados
        try { Thread.sleep(50); } catch (InterruptedException e) { }

        // Inicia os processos 2 e 4, que também ficarão bloqueados
        processo2.start();
        processo4.start();

        // Aguarda um tempo para que o deadlock fique evidente
        try {
            processo1.join(3000);
            processo3.join(3000);
            processo2.join(3000);
            processo4.join(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Se após o tempo de espera os processos 2 e 4 ainda estiverem ativos,
        // mostra que eles ficaram travados (deadlock)
        if (processo2.isAlive()) {
            System.out.println("Deadlock detectado: Processo 2 está travado e não conseguiu adquirir o recurso B.");
        }
        if (processo4.isAlive()) {
            System.out.println("Deadlock detectado: Processo 4 está travado e não conseguiu adquirir o recurso A.");
        }

        System.out.println("Encerramento da simulação.");
    }
}
