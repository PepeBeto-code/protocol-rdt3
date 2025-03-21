package Simulador;

import java.util.Random;

public class UnreliableNetworkSimulator {
    private double lossProbability;
    private double corruptionProbability;
    private int maxLosses; // Número máximo de pérdidas permitidas
    private int maxCorruptions; // Número máximo de corrupciones permitidas
    private int lossCount = 0; // Contador de pérdidas
    private int corruptionCount = 0; // Contador de corrupciones
    private Random random;
    private EntityA entityA;
    private EntityB entityB;

    public UnreliableNetworkSimulator(double lossProbability, double corruptionProbability, int maxLosses, int maxCorruptions) {
        this.lossProbability = lossProbability;
        this.corruptionProbability = corruptionProbability;
        this.maxLosses = maxLosses;
        this.maxCorruptions = maxCorruptions;
        this.random = new Random();
    }

    public void setEntities(EntityA entityA, EntityB entityB) {
        this.entityA = entityA;
        this.entityB = entityB;
    }

    public EntityA getEntityA() {
        return entityA;
    }

    public EntityB getEntityB() {
        return entityB;
    }

    // Simula el envío de un paquete a través de una red no confiable
    public void sendPacket(Packet packet, Receiver receiver) {
        Packet packetOut = new Packet(packet.getData(), packet.getSeqNum());;
        // Simular pérdida de paquete
        if (lossCount < maxLosses && random.nextDouble() < lossProbability) {
            System.out.println("Simulador: Paquete perdido.");
            lossCount++;
            return;
        }

        // Simular corrupción de paquete
        if (corruptionCount < maxCorruptions && random.nextDouble() < corruptionProbability) {
            packetOut.corrupt();
            System.out.println("Simulador: Paquete corrupto.");
            corruptionCount++;
        }

        // Entrega el paquete al receptor
        receiver.receive(packetOut);
    }
}
