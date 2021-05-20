import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;

public class ServidorHundir {

    char[][] tablero = new char[10][10];
    int port = 5557;
    DatagramSocket socket;
    InetAddress inetAddress;
    boolean jugando = true;

    public void juego() {

        while (jugando) {
            try {

                DatagramPacket packet;

                socket = new DatagramSocket(5557);
                inetAddress = InetAddress.getByName("localhost");

                byte[] msgRecibir = new byte[1024];
                byte[] msgEnviar = new byte[1024];

                packet = new DatagramPacket(msgRecibir, msgRecibir.length);

                socket.receive(packet);

                System.out.println(new String(packet.getData()));

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
                System.out.print(tablero[i][j] + "   ");
            }
            System.out.print("\n");
        }
    }

    public static void main(String[] args) {

        ServidorHundir servidorHundir = new ServidorHundir();

        /*
        servidorHundir.nuevoTablero();

        servidorHundir.mostrarTablero();

         */

        servidorHundir.juego();


    }

}
