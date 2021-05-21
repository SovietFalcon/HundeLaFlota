import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class ClienteHundir {

    char[][] tablero = new char[10][10];
    int port;
    DatagramSocket socket;
    InetAddress ip;
    Scanner sc = new Scanner(System.in);

    public void init () throws UnknownHostException, SocketException {
        System.out.println("Escribe la ip del servidor (Ej: 198.162.25.1)");
        ip = InetAddress.getByName(sc.next());
        System.out.println("Escribe el puerto del servidor (Ej: 5353):");
        port = sc.nextInt();
        socket = new DatagramSocket();
    }

    public void runClient () throws IOException {
        Scanner scanner = new Scanner(System.in);
        byte[] reciveData = new byte[1024];
        byte[] sendData = new byte[1024];
        String sendString;
        String receivedString;
        String auxiliar;

        //Enviar primer mensaje y lobby
        String s = "prueba";
        sendData = s.getBytes();
        DatagramPacket packet = new DatagramPacket(sendData, sendData.length, ip, port);
        socket.send(packet);

        //Recibir primer mensaje, if jugador 1, pregunta si quiere empezar la partida o esperar a otro jugadsor
        //If jugador 2, comenzar la partida
        packet = new DatagramPacket(reciveData, reciveData.length);

        socket.receive(packet);

        receivedString = new String(packet.getData(),0,packet.getLength());
        if (receivedString.equals("1")) {
            System.out.println("¿Quieres empezar la partida o esperar a que entre otro jugador?\n1. Empezar la partida\n2. Esperar a otro jugador");
            auxiliar = scanner.next();
            if (auxiliar.equals("1")) {
                sendString = "empezar";
                sendData = sendString.getBytes();
                packet = new DatagramPacket(sendData, sendData.length, ip, port);
                socket.send(packet);

            } else if (auxiliar.equals("2")) {

                sendString = "esperar";
                sendData = sendString.getBytes();
                packet = new DatagramPacket(sendData, sendData.length, ip, port);
                socket.send(packet);
            }
        } else if (receivedString.equals("2")) {
            System.out.println("no eres jugador 1");
        }






        //JUEGO

    }

    public static void main(String[] args) throws IOException {
        ClienteHundir c = new ClienteHundir();

        c.init();
        c.runClient();
    }
}
