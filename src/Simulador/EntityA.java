package Simulador;

import java.util.Timer;
import java.util.TimerTask;
public class EntityA implements Receiver {
    private UnreliableNetworkSimulator network;
    private Timer timer;
    private int currentSeqNum = 0; // Número de secuencia actual
    private boolean ackReceived = false; // Indicador de recepción de ACK
    private int timerIncrement = 5000; // Tiempo en milisegundos para el temporizador
    private Packet lastPacket; // Último paquete enviado

    @Override
    public void receive(Packet packet) {
        entrada(packet); // Llama a la función entrada cuando recibe un paquete
    }

    public EntityA(UnreliableNetworkSimulator network) {
        this.network = network;
        init();
    }

    // Inicializa los componentes de A
    public void init() {
        System.out.println("Inicializando A");
    }

    // Envía un mensaje desde A a B
    public void salida(String mensaje) {
        startTimer(); // Iniciar temporizador

        // Crear un nuevo paquete con el número de secuencia actual y lo asignamos al ultimo paquete enviado
        lastPacket = new Packet(mensaje, currentSeqNum);
        System.out.println("A: Enviando paquete -> " + mensaje + " (SeqNum: " + currentSeqNum + ")");
        network.sendPacket(lastPacket, network.getEntityB());
    }

    // Recibe un ACK de B
    public void entrada(Packet ackPacket) {

        if (!ackPacket.validateChecksum()) {
            System.out.println("A: ACK recibido corrupto, ignorando...");
            return;
        }

        if (ackPacket.getSeqNum() == currentSeqNum) {
            System.out.println("A: ACK recibido correctamente para SeqNum: " + currentSeqNum);
            ackReceived = true;
            stopTimer();
            currentSeqNum = 1 - currentSeqNum; // Alternar el número de secuencia
        } else {
            System.out.println("A: ACK recibido con SeqNum incorrecto, ignorado...");
        }
    }

    // Temporizador: se activa en caso de que no se reciba el ACK
    public void startTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                interrupTimer();
            }
        }, timerIncrement);
        System.out.println("A: Temporizador iniciado.");
    }

    // Detiene el temporizador si se recibe el ACK
    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
            System.out.println("A: Temporizador detenido.");
        }
    }

    // Maneja el caso en que el temporizador se agota sin recibir un ACK
    public void interrupTimer() {
        System.out.println("A: Temporizador interrumpido, reenviando paquete...");
        startTimer();
        network.sendPacket(lastPacket, network.getEntityB()); // Reenvía el último paquete
    }

    // Métodos para verificar y resetear el estado de ackReceived
    public boolean isAckReceived() {
        return ackReceived;
    }

    public void setAckReceived(boolean ackReceived) {
        this.ackReceived = ackReceived;
    }
}
