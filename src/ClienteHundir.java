import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ClienteHundir {

    char[][] tablero = new char[10][10];
    int port = 5557;
    DatagramSocket socket;
    InetAddress inetAddress;

    public void juego() {

        try {
            socket = new DatagramSocket();
            inetAddress = InetAddress.getByName("localhost");

            String lol = "davis puto geis";
            byte[] delocos = lol.getBytes();
            DatagramPacket packet = new DatagramPacket(delocos, delocos.length, inetAddress, port);

            socket.send(packet);
            //socket.send(new DatagramPacket("delocos".getBytes(), "delocos".getBytes().length, inetAddress, port));

            System.out.println(packet.getData());

        } catch (IOException e) {
        }
    }


    public static void main(String[] args) {
        ClienteHundir ch = new ClienteHundir();
    }
}
