package Udp;
import java.net.*;
import java.nio.ByteBuffer;

public class EntityB extends Receiver {
    private DatagramSocket socket;
    private int expectedSeqNum = 0;
    private int port;
    private int lastNumSecACK;
    private DatagramPacket lastACK;
    private UnreliableNetworkSimulator network; // Simulador de red no fiable

    public EntityB(int port, UnreliableNetworkSimulator network) throws Exception {
        this.socket = new DatagramSocket(port);
        this.port = port;
        this.network = network;
        init();
    }

    @Override
    public void receive(DatagramPacket packet) throws Exception {
        entrada(); // Llama a la función entrada cuando recibe un paquete
    }
    public void init() {
        System.out.println("Inicializando B...");
    }

    public void entrada() throws Exception {
        byte[] buffer = new byte[1024];
        DatagramPacket receivedPacket = new DatagramPacket(buffer, buffer.length);
        socket.receive(receivedPacket);

        // Extraer datos y número de secuencia del paquete
        byte[] datosRecibidos = receivedPacket.getData();
        int longitudMensaje = receivedPacket.getLength();

        ByteBuffer byteBuffer = ByteBuffer.wrap(datosRecibidos);
        byte[] datosMensaje = new byte[longitudMensaje - 8]; // 8 bytes para checksum y número de secuencia
        byteBuffer.get(datosMensaje); // Obtener los datos del mensaje
        int checksumRecibido = byteBuffer.getInt(); // Obtener el checksum
        int numeroSecuenciaRecibido = byteBuffer.getInt(); // Obtener el número de secuencia

        // Convertir los bytes a un String usando UTF-8
        String mensaje = new String(datosMensaje, "UTF-8");

        // Verificar el checksum
        int checksumCalculado = calcularChecksum(datosMensaje);

        if (checksumRecibido != checksumCalculado) {
            System.out.println("B: Paquete recibido está corrupto, ignorando...");
            return;
        }

        if (numeroSecuenciaRecibido == expectedSeqNum) {
            System.out.println("B: Paquete recibido correctamente -> " + mensaje + " (SeqNum: " + expectedSeqNum + ")");
            enviarACK(expectedSeqNum, receivedPacket);
            // Registramos el ultimo numero de secuencia y ACK enviados
            lastNumSecACK = expectedSeqNum;
            lastACK = receivedPacket;
            expectedSeqNum++; // Incrementar número de secuencia esperado
        } else {
            System.out.println("B: Paquete duplicado recibido, reenviando último ACK...");
            enviarACK(lastNumSecACK, lastACK);
        }
    }

    private void enviarACK(int seqNum,DatagramPacket receivedPacket ) throws Exception {

        // Enviar ACK con el número de secuencia
        byte[] ack = new byte[1]; // Un solo byte para el número de secuencia
        ack[0] = (byte) seqNum;

        // Crear paquete de ACK
        InetAddress direccionEmisor = receivedPacket.getAddress();
        int puertoEmisor = receivedPacket.getPort();
        DatagramPacket paqueteACK = new DatagramPacket(ack, ack.length, direccionEmisor, puertoEmisor);
        System.out.println("B: Enviado ACK");
        network.sendPacket(paqueteACK, new Receiver() {
            @Override
            public void receive(DatagramPacket packetout) {
                try {
                    socket.send(packetout);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

