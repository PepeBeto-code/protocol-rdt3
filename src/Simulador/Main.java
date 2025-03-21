package Simulador;

public class Main {
    public static void main(String[] args) {
        // Configuración del simulador de red con las probabilidades requeridas
        double lossProbability = 0.3;
        double corruptionProbability = 0.2;
        int maxLosses = 3; // Número máximo de pérdidas permitidas
        int maxCorruptions = 3; // Número máximo de corrupciones permitidas

        // Crear simulador de red
        UnreliableNetworkSimulator network = new UnreliableNetworkSimulator(lossProbability, corruptionProbability, maxLosses, maxCorruptions);

        // Crear las entidades A y B
        EntityB bartolo = new EntityB(network);
        EntityA alicia = new EntityA(network);

        // Registrar las entidades en el simulador
        network.setEntities(alicia, bartolo);

        // Enviar 10 mensajes consecutivos desde A hacia B
        for (int i = 1; i <= 10; i++) {
            String message = "Mensaje #" + i + " desde ALICIA a BARTOLO.";
            System.out.println("\n=============================");
            System.out.println("Iniciando envío del: " + message);
            alicia.salida(message);

            // Esperar hasta que se reciba el ACK correcto antes de enviar el siguiente mensaje
            while (!alicia.isAckReceived()) {
                try {
                    Thread.sleep(100); // Esperar un poco antes de verificar de nuevo
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Resetear el indicador de ACK recibido para el siguiente mensaje
            alicia.setAckReceived(false);
        }

        // Finalizar simulación
        System.out.println("\nSimulación completada.");
    }
}
