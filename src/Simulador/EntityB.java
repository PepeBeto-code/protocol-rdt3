package Simulador;

public class EntityB implements Receiver {
    private UnreliableNetworkSimulator network;
    private int expectedSeqNum = 0; // Número de secuencia esperado para el próximo paquete
    private int lastNumSecACK;


    @Override
    public void receive(Packet packet) {
        entrada(packet); // Llama a la función entrada cuando recibe un paquete
    }

    public EntityB(UnreliableNetworkSimulator network) {
        this.network = network;
        init();
    }

    // Inicializa los componentes de B
    public void init() {
        System.out.println("Inicializando B");
    }

    // Recibe un paquete de A, verifica integridad y envía ACK si es válido
    public void entrada(Packet packet) {
        if (!packet.validateChecksum()) {
            System.out.println("B: Paquete recibido está corrupto, ignorando...");
            return;
        }

        if (packet.getSeqNum() == expectedSeqNum) {
            System.out.println("B: Paquete recibido correctamente -> " + packet.getData() + " (SeqNum: " + packet.getSeqNum() + ")");
            enviarACK(packet.getSeqNum());
            lastNumSecACK = expectedSeqNum;
            expectedSeqNum = 1 - expectedSeqNum; // Alternar número de secuencia esperado
        } else {
            System.out.println("B: Paquete duplicado recibido, reenviando último ACK...");
            enviarACK(lastNumSecACK);
        }
    }

    // Enviar ACK de vuelta a A
    public void enviarACK(int seqNum) {
        Packet ackPacket = new Packet("ACK", seqNum);
        System.out.println("B: Enviando ACK para SeqNum: " + seqNum);
        network.sendPacket(ackPacket, network.getEntityA());
    }
}
