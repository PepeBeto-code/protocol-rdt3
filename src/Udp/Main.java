package Udp;

public class Main {
    public static void main(String[] args) throws Exception {
        // Configuración del simulador de red con las probabilidades requeridas
        double lossProbability = 0.3;
        double corruptionProbability = 0.2;
        int maxLosses = 3; // Número máximo de pérdidas permitidas
        int maxCorruptions = 3; // Número máximo de corrupciones permitidas

        // Crear simulador de red
        UnreliableNetworkSimulator network = new UnreliableNetworkSimulator(lossProbability, corruptionProbability, maxLosses, maxCorruptions);

        // Hilo para iniciar el receptor (EntityB)
        Thread receiverThread = new Thread(() -> {
            try {
                // Crear receptor en el puerto 9876
                EntityB entityB = new EntityB(9876, network);
                while (true) {
                    entityB.entrada(); // Esperar paquetes
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        receiverThread.start();

        // Esperar un poco para asegurarse de que el receptor esté listo
        Thread.sleep(1000);

        // Iniciar el emisor
        EntityA alicia = new EntityA("localhost", 9876, network);
        for (int i = 1; i <= 10; i++) {
            String mensaje = "Mensaje " + i + " desde ALICIA a BARTOLO.";
            System.out.println("\n=============================");
            System.out.println("Iniciando envío del: " + mensaje);
            alicia.salida(mensaje);

            alicia.entrada();
            // Esperar hasta que se reciba el ACK correcto antes de enviar el siguiente mensaje
            while (!alicia.isAckReceived()) {
                Thread.sleep(100); // Esperar un poco antes de verificar de nuevo
                alicia.entrada();
            }

            // Resetear el indicador de ACK recibido para el siguiente mensaje
            alicia.setAckReceived(false);
        }

        // Finalizar simulación
        System.out.println("\nSimulación completada.");
    }
}

