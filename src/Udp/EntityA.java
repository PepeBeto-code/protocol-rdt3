package Udp;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Timer;
import java.util.TimerTask;

public class EntityA extends Receiver {
    private DatagramSocket socket;
    private InetAddress receiverAddress;
    private int receiverPort;
    private int sequenceNumber = 0;
    private Timer timer;
    private boolean ackReceived = false;
    private static final int TIMEOUT = 5000;
    private Packet lastPacket; // Último paquete enviado
    private UnreliableNetworkSimulator network; // Simulador de red no fiable

    public EntityA(String receiverHost, int receiverPort, UnreliableNetworkSimulator network) throws Exception {
        this.socket = new DatagramSocket();
        this.receiverAddress = InetAddress.getByName(receiverHost);
        this.receiverPort = receiverPort;
        this.network = network;
        init();
    }

    @Override
    public void receive(DatagramPacket packet) throws Exception {
        entrada(); // Llama a la función entrada cuando recibe un paquete
    }
    public void init() {
        System.out.println("Inicializando A...");
    }

    public void salida(String mensaje) throws Exception {
        lastPacket = new Packet(mensaje, sequenceNumber);
        startTimer();
        enviar(lastPacket);
    }

    private void enviar(Packet packet) throws Exception {
        byte[] data = packet.getData().getBytes();


        // Calcular checksum
        int checksum = calcularChecksum(data);

        // Crear un buffer para enviar los datos + checksum + numero de secuencia
        ByteBuffer buffer = ByteBuffer.allocate(data.length + 8); // 4 bytes para el checksum y 4 para el numSec
        buffer.put(data);
        buffer.putInt(checksum); // Agregar el checksum al mensaje
        buffer.putInt(packet.getSeqNum()); // Agregar el número de secuencia


        DatagramPacket datagramPacket = new DatagramPacket(buffer.array(), buffer.array().length, receiverAddress, receiverPort);
        System.out.println("A: Enviado -> " + packet.getData());
        network.sendPacket(datagramPacket, new Receiver() {
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

    public void entrada() throws Exception {
        // Esperar el ACK
        byte[] bufferACK = new byte[1];
        DatagramPacket paqueteACK = new DatagramPacket(bufferACK, bufferACK.length);
        socket.receive(paqueteACK);

        int ackRecibido = bufferACK[0]; // Leer el número de secuencia del ACK

        if (ackRecibido == sequenceNumber) {
            System.out.println("A: ACK recibido correctamente para SeqNum: " + sequenceNumber);
            ackReceived = true;
            stopTimer();
            sequenceNumber++;  // Incrementar el número de secuencia para el siguiente paquete
        } else {
            System.out.println("A: ACK recibido con SeqNum incorrecto, ignorado...");
        }
    }

    private void startTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    interrupTimer();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, TIMEOUT);
        System.out.println("A: Temporizador iniciado.");
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            System.out.println("A: Temporizador detenido.");
        }
    }

    // Maneja el caso en que el temporizador se agota sin recibir un ACK
    public void interrupTimer() throws Exception {
        System.out.println("A: Timeout, reenviando paquete...");
        startTimer();
        enviar(lastPacket);
    }

    // Métodos para verificar y resetear el estado de ackReceived
    public boolean isAckReceived() {
        return ackReceived;
    }

    public void setAckReceived(boolean ackReceived) {
        this.ackReceived = ackReceived;
    }
}

