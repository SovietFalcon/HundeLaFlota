import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class ClienteHundir {

    //char[][] tablero = new char[10][10];
    int port;
    DatagramSocket socket;
    InetAddress ip;
    Scanner sc;

    public void init () throws UnknownHostException, SocketException {
        System.out.println("Escribe la ip del servidor (Ej: 198.162.25.1)");
        ip = InetAddress.getByName(sc.next());
        System.out.println("Escribe el puerto del servidor (Ej: 5353):");
        port = sc.nextInt();
        socket = new DatagramSocket();
    }

    public void runClient () throws IOException {
        byte[] reciveData = new byte[1024];
        byte[] sendData;

        String s = "prueba";
        sendData = s.getBytes();

        DatagramPacket packet = new DatagramPacket(sendData, sendData.length, ip, port);
        socket.send(packet);
    }

    public static void main(String[] args) throws IOException {
        ClienteHundir c = new ClienteHundir();

        c.init();
        c.runClient();
    }
}
