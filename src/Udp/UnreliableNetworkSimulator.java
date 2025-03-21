package Udp;

import java.net.DatagramPacket;
import java.util.Random;

public class UnreliableNetworkSimulator {
    private double lossProbability;
    private double corruptionProbability;
    private int maxLosses; // Número máximo de pérdidas permitidas
    private int maxCorruptions; // Número máximo de corrupciones permitidas
    private int lossCount = 0; // Contador de pérdidas
    private int corruptionCount = 0; // Contador de corrupciones
    private Random random;

    public UnreliableNetworkSimulator(double lossProbability, double corruptionProbability, int maxLosses, int maxCorruptions) {
        this.lossProbability = lossProbability;
        this.corruptionProbability = corruptionProbability;
        this.maxLosses = maxLosses;
        this.maxCorruptions = maxCorruptions;
        this.random = new Random();
    }

    // Simula el envío de un paquete a través de una red no confiable
    public void sendPacket(DatagramPacket packet, Receiver receiver) throws Exception {
        DatagramPacket packetOut = new DatagramPacket(packet.getData(), packet.getData().length, packet.getAddress(), packet.getPort());;
        // Simular pérdida de paquete
        if (lossCount < maxLosses && random.nextDouble() < lossProbability) {
            System.out.println("Simulador: Paquete perdido.");
            lossCount++;
            return;
        }

        // Simular corrupción de paquete
        if (corruptionCount < maxCorruptions && random.nextDouble() < corruptionProbability) {
            corruptPacket(packetOut);
            System.out.println("Simulador: Paquete corrupto.");
            corruptionCount++;
        }

        // Entrega el paquete al receptor
        receiver.receive(packetOut);
    }

    // Método para corromper el paquete
    private void corruptPacket(DatagramPacket packet) {
        byte[] data = packet.getData();
        int index = random.nextInt(data.length); // Elegir un índice aleatorio para corromper
        data[index] = (byte) (data[index] ^ 0xFF); // Invertir los bits del byte en el índice elegido
    }
}

