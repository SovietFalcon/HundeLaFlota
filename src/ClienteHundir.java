import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class ClienteHundir {

    char[][] tablero = new char[10][10];
    int port;
    DatagramSocket socket;
    InetAddress ip;
    Scanner sc = new Scanner(System.in);
    boolean jugando = true;
    boolean coordsCorrectas = false;

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
        int[] coordenadas = {-1,-1};

        while (jugando) {
            packet = new DatagramPacket(reciveData, reciveData.length);
            socket.receive(packet);
            receivedString = new String(packet.getData(), 0, packet.getLength());

            if (receivedString.equals("turno")) {
                coordsCorrectas = false;
                while (!coordsCorrectas) {
                    mostrarTablero();
                    System.out.println("Indica las coordenadas para el ataque (ejemplo: D3): ");
                    coordenadas = procesarCoords(scanner.next());

                    if (coordenadas[0] != -1) {
                        coordsCorrectas = true;

                        //Enviar jugada

                        sendString = Integer.toString(coordenadas[0]) + Integer.toString(coordenadas[1]);
                        sendData = sendString.getBytes();
                        packet = new DatagramPacket(sendData, sendData.length, ip, port);
                        socket.send(packet); //Envia jugada

                        packet = new DatagramPacket(reciveData, reciveData.length);
                        socket.receive(packet); //Recibe resultado de jugada
                        receivedString = new String(packet.getData(), 0, packet.getLength());

                        if (receivedString.equals("0")) {
                            tablero[coordenadas[1]][coordenadas[0]] = 'x';
                            System.out.println("AGUA");
                        } else if (receivedString.equals("1")) {
                            tablero[coordenadas[1]][coordenadas[0]] = 'o';
                            System.out.println("TOCADO!\n+1 Punto");
                        } else if (receivedString.equals("2")) {
                            tablero[coordenadas[1]][coordenadas[0]] = 'o';
                            System.out.println("HUNDIDO!\n+5 Puntos");
                        }

                    } else {
                        System.out.println("Error de sintaxis!");
                        coordsCorrectas = false;
                    }

                }
            } else if (receivedString.equals("noturno")) {
                System.out.println("Espera tu turno!");

            } else {

                System.out.println(receivedString);

            }


        }

    }

    private int[] procesarCoords(String coordsString) {
        int[] coordenadas = new int[2];

        if (coordsString.length() == 2) {
            if (coordsString.charAt(0) == 'A' || coordsString.charAt(0) == 'B' || coordsString.charAt(0) == 'C' || coordsString.charAt(0) == 'D' || coordsString.charAt(0) == 'E' || coordsString.charAt(0) == 'F' || coordsString.charAt(0) == 'G' || coordsString.charAt(0) == 'H' || coordsString.charAt(0) == 'I' || coordsString.charAt(0) == 'J' || coordsString.charAt(0) == 'a' || coordsString.charAt(0) == 'b' || coordsString.charAt(0) == 'c' || coordsString.charAt(0) == 'd' || coordsString.charAt(0) == 'e' || coordsString.charAt(0) == 'f' || coordsString.charAt(0) == 'g' || coordsString.charAt(0) == 'h' || coordsString.charAt(0) == 'i' || coordsString.charAt(0) == 'j') {
                if (coordsString.charAt(1) == '0' || coordsString.charAt(1) == '1' || coordsString.charAt(1) == '2' || coordsString.charAt(1) == '3' || coordsString.charAt(1) == '4' || coordsString.charAt(1) == '5' || coordsString.charAt(1) == '6' || coordsString.charAt(1) == '7' || coordsString.charAt(1) == '8' || coordsString.charAt(1) == '9') {

                    if (coordsString.charAt(0) == 'A' || coordsString.charAt(0) == 'a') {
                        coordenadas[0] = 0;
                    } else if (coordsString.charAt(0) == 'B' || coordsString.charAt(0) == 'b') {
                        coordenadas[0] = 1;
                    } else if (coordsString.charAt(0) == 'C' || coordsString.charAt(0) == 'c') {
                        coordenadas[0] = 2;
                    } else if (coordsString.charAt(0) == 'D' || coordsString.charAt(0) == 'd') {
                        coordenadas[0] = 3;
                    } else if (coordsString.charAt(0) == 'E' || coordsString.charAt(0) == 'e') {
                        coordenadas[0] = 4;
                    } else if (coordsString.charAt(0) == 'F' || coordsString.charAt(0) == 'f') {
                        coordenadas[0] = 5;
                    } else if (coordsString.charAt(0) == 'G' || coordsString.charAt(0) == 'g') {
                        coordenadas[0] = 6;
                    } else if (coordsString.charAt(0) == 'H' || coordsString.charAt(0) == 'h') {
                        coordenadas[0] = 7;
                    } else if (coordsString.charAt(0) == 'I' || coordsString.charAt(0) == 'i') {
                        coordenadas[0] = 8;
                    } else if (coordsString.charAt(0) == 'J' || coordsString.charAt(0) == 'j') {
                        coordenadas[0] = 9;
                    }

                    coordenadas[1] = Character.getNumericValue(coordsString.charAt(1));
                    return coordenadas;

                }
            }
        }
        coordenadas[0] = -1;
        coordenadas[1] = -1;
        return coordenadas;
    }

    public static void main(String[] args) throws IOException {
        ClienteHundir c = new ClienteHundir();

        c.crearTablero();
        c.init();
        c.runClient();
    }
}
