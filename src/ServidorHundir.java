import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ServidorHundir {

    char[][] tablero = new char[10][10];
    char[][] tableroJ1 = new char[10][10];
    char[][] tableroJ2 = new char[10][10];

    //Info NET
    int port = 5557;
    DatagramSocket socket;
    InetAddress inetAddress;
    boolean jugando = true;
    String srvIp = "localhost";
    InetAddress inetAddressJ1;
    int portJ1;
    InetAddress inetAddressJ2;
    int portJ2;

    public void lobby() {
        try {
            int jugadores = 0;
            socket = new DatagramSocket(5557);
            byte[] msgReceive = new byte[1024];
            byte[] msgSend = new byte[1024];
            String auxiliar;

            DatagramPacket packet = new DatagramPacket(msgReceive, msgReceive.length);

            while (jugadores < 2) {
                socket.receive(packet);
                //Bienvenido tal cual sout

                //guarda IP jugador
                jugadores++;
                if (jugadores == 1) {
                    inetAddressJ1 = packet.getAddress();
                    portJ1 = packet.getPort();
                    String num = "1";
                    msgSend = num.getBytes();
                    socket.send(new DatagramPacket(msgSend, msgSend.length, inetAddressJ1, portJ1));

                    packet = new DatagramPacket(msgReceive, msgReceive.length);
                    socket.receive(packet);

                    auxiliar = new String(packet.getData(), 0, packet.getLength());
                    if (auxiliar.equals("empezar")){
                        System.out.println("Se empieza"); //WIP
                        break;

                    } else if (auxiliar.equals("esperar")) {
                        System.out.println("Se espera"); //WIP

                    }
                    //System.out.println(new String(packet.getData(),0, packet.getLength()));

                } else if (jugadores == 2) {
                    inetAddressJ2 = packet.getAddress();
                    portJ2 = packet.getPort();

                    String num = "2";
                    msgSend = num.getBytes();
                    socket.send(new DatagramPacket(msgSend, msgSend.length, inetAddressJ2, portJ2));
                    break;
                }
            }

        } catch (IOException e) {
        }

    }

    public void juego() {

        while (jugando) {
            try {

                DatagramPacket packet;

                socket = new DatagramSocket(5557);
                inetAddress = InetAddress.getByName(srvIp);

                byte[] msgRecibir = new byte[1024];
                byte[] msgEnviar = new byte[1024];

                packet = new DatagramPacket(msgRecibir, msgRecibir.length);

                socket.receive(packet);




                System.out.println(new String(packet.getData(),0, packet.getLength()));

                msgEnviar = "delocos".getBytes();

                packet = new DatagramPacket(msgEnviar, msgEnviar.length, packet.getAddress(), packet.getPort());
                socket.send(packet);

            } catch (IOException e) {
            }
        }



    }

    public void nuevoTablero() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                tablero[i][j] = '·';
            }
        }

        //Tablero-Jugador1
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                tableroJ1[i][j] = '·';
            }
        }

        //Tablero-Jugador2
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                tableroJ2[i][j] = '·';
            }
        }



        //Tablero barcos PC
        //barco1-4cuadriculas
        tablero[0][0] = '1';
        tablero[0][1] = '1';
        tablero[0][2] = '1';
        tablero[0][3] = '1';

        //barco2-3cuadriculas-1
        tablero[0][9] = '2';
        tablero[1][9] = '2';
        tablero[2][9] = '2';

        //barco2-3cuadriculas-2
        tablero[9][0] = '2';
        tablero[9][1] = '2';
        tablero[9][2] = '2';

        //barco3-2cuadriculas-1
        tablero[4][2] = '3';
        tablero[3][2] = '3';

        //barco3-2cuadriculas-2
        tablero[5][7] = '3';
        tablero[5][8] = '3';

        //barco3-2cuadriculas-3
        tablero[9][7] = '3';
        tablero[9][8] = '3';

        //barco4-1cuadricula-1
        tablero[7][4] = '4';

        //barco4-1cuadricula-2
        tablero[3][5] = '4';

        //barco4-1cuadricula-3
        tablero[1][7] = '4';

        //barco4-1cuadricula-3
        tablero[5][4] = '4';

    }

    public void mostrarTablero() { //WIP
        System.out.println("    A   B   C   D   E   F   G   H   I   J");
        for (int i = 0; i < 10; i++) {
            System.out.print(i + "   ");
            for (int j = 0; j < 10; j++) {
                System.out.print(tableroJ1[i][j] + "   ");
            }
            System.out.print("\n");
        }
    }

    public static void main(String[] args) {

        ServidorHundir servidorHundir = new ServidorHundir();
        servidorHundir.nuevoTablero();
        //servidorHundir.mostrarTablero();

        servidorHundir.lobby();

        //servidorHundir.juego();

    }

}
