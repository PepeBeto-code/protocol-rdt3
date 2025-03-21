package Simulador;

public class Packet {
    private String data;
    private int seqNum;
    private int checksum;
    private boolean corrupted;

    public Packet(String data, int seqNum) {
        this.data = data;
        this.checksum = calculateChecksum(data, seqNum);
        this.corrupted = false;
        this.seqNum = seqNum;
    }

    // Calcula el checksum del paquete
    public int calculateChecksum(String data, int seqNum) {
        int checksum = seqNum;
        for (char ch : data.toCharArray()) {
            checksum += ch;
        }
        return checksum;
    }

    public String getData() {
        return data;
    }

    public int getSeqNum() {
        return seqNum;
    }

    // Marca el paquete como corrupto
    public void corrupt() {
        this.corrupted = true;
    }

    // Verifica si el paquete está corrupto
    public boolean validateChecksum() {
        return !corrupted && calculateChecksum(data, seqNum) == checksum;
    }
}
