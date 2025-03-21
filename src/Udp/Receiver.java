package Udp;


import java.net.DatagramPacket;

public abstract class Receiver {
    // Método para calcular el checksum sumando los bytes de los datos
    public static int calcularChecksum(byte[] datos) {
        int checksum = 0;
        for (byte b : datos) {
            checksum += b;
        }
        return ~checksum; // Complemento a 1
    }

    public abstract void receive(DatagramPacket packet) throws Exception;
}
