import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class ClienteHundir {

    char[][] tablero = new char[10][10];
    int port;
    DatagramSocket socket;
    InetAddress ip;
    Scanner sc = new Scanner(System.in);

    public void crearTablero() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                tablero[i][j] = '·';
            }
        }
    }

    public void mostrarTablero() { //WIP
        System.out.println("    A   B   C   D   E   F   G   H   I   J");
        for (int i = 0; i < 10; i++) {
            System.out.print(i + "   ");
            for (int j = 0; j < 10; j++) {
                System.out.print(tablero[i][j] + "   ");
            }
            System.out.print("\n");
        }
    }

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
            System.out.println("Bienvenido a Hundir la Flota - Cooperativo: Jugador 1");
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
            System.out.println("Bienvenido a Hundir la Flota - Cooperativo: Jugador 2");
        }


        //INICIO PARTIDA

        packet = new DatagramPacket(reciveData, reciveData.length);
        socket.receive(packet);
        receivedString = new String(packet.getData(), 0, packet.getLength());

        if (receivedString.equals("start")) {
            System.out.println("Empieza la partida");
        }

        //JUEGO

    }

    public static void main(String[] args) throws IOException {
        ClienteHundir c = new ClienteHundir();

        c.crearTablero();
        c.init();
        c.runClient();
    }
}
