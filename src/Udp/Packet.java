package Udp;

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

    // Setters y getters
    public void setChecksum(int checksum) {
        this.checksum = checksum;
    }

    public int getChecksum() {
        return checksum;
    }

    public String getData() {
        return data;
    }

    public int getSeqNum() {
        return seqNum;
    }

    // Deserializa un String en un paquete
    public static Packet fromString(String str) {
        String[] parts = str.split(";", 3); // Dividir el String en secuencia, checksum y datos
        Packet packet = new Packet(parts[2], Integer.parseInt(parts[0]));
        packet.setChecksum(Integer.parseInt(parts[1]));
        return packet;
    }
}
