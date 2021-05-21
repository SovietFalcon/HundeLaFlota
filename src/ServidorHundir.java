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
    int jugadores = 0;
    int turno = 1;
    int puntosJ1 = 0;
    int puntosJ2 = 0;

    public void lobby() {
        try {

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
                        System.out.println("Se empieza el juego con 1 persona"); //WIP
                        break;

                    } else if (auxiliar.equals("esperar")) {
                        System.out.println("Se espera a otro jugador"); //WIP

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

    public void juegoCoop() {

        System.out.println("lololol");

            try {

                DatagramPacket packet;

                inetAddress = InetAddress.getByName(srvIp);

                byte[] msgRecibir = new byte[1024];
                byte[] msgEnviar = new byte[1024];
                String stringRecibir;
                String resultado;

                msgEnviar = new String("start").getBytes();
                packet = new DatagramPacket(msgEnviar, msgEnviar.length, inetAddressJ1, portJ1);
                socket.send(packet);
                packet = new DatagramPacket(msgEnviar, msgEnviar.length, inetAddressJ2, portJ2);
                socket.send(packet);
                System.out.println("Se empieza el juego");

                while (jugando) {
                    if (turno == 1) {
                        msgEnviar = new String("turno").getBytes();
                        packet = new DatagramPacket(msgEnviar, msgEnviar.length, inetAddressJ1, portJ1);
                        socket.send(packet);
                        msgEnviar = new String("noturno").getBytes();
                        packet = new DatagramPacket(msgEnviar, msgEnviar.length, inetAddressJ2, portJ2);
                        socket.send(packet);
                    } else if (turno == 2) {
                        msgEnviar = new String("turno").getBytes();
                        packet = new DatagramPacket(msgEnviar, msgEnviar.length, inetAddressJ2, portJ2);
                        socket.send(packet);
                        msgEnviar = new String("noturno").getBytes();
                        packet = new DatagramPacket(msgEnviar, msgEnviar.length, inetAddressJ1, portJ1);
                        socket.send(packet);
                    }

                    packet = new DatagramPacket(msgRecibir, msgRecibir.length);
                    socket.receive(packet);

                    stringRecibir = new String(packet.getData(), 0, packet.getLength());
                    resultado = comprobarCoords(stringRecibir);

                    msgEnviar = new String(resultado).getBytes();
                    if (turno == 1) {
                        packet = new DatagramPacket(msgEnviar, msgEnviar.length, inetAddressJ1, portJ1);
                    } else if (turno == 2) {
                        packet = new DatagramPacket(msgEnviar, msgEnviar.length, inetAddressJ2, portJ2);
                    }
                    socket.send(packet);

                    jugando = false;
                    for (int i = 0; i < 10; i++) {
                        for (int j = 0; j < 10; j++) {
                            if (tablero[i][j] != '·') {
                                jugando = true;
                            }
                        }
                    }

                    if (turno == 1) {
                        turno = 2;
                    } else if (turno == 2) {
                        turno = 1;
                    }






                }


            } catch (IOException e) {
            }



    }

    private String comprobarCoords(String coordenadas) {

        int n2 = Character.getNumericValue(coordenadas.charAt(0));
        int n1 = Character.getNumericValue(coordenadas.charAt(1));
        int barco;
        boolean destruido = true;

        if (tablero[n1][n2] == '·') {
            return "0";
        } else {
            barco = Character.getNumericValue(tablero[n1][n2]);
            tablero[n1][n2] = '·';

            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (Character.getNumericValue(tablero[i][j]) == barco) {
                        destruido = false;
                    }
                }
            }

            if (destruido) {
                if (turno == 1) {
                    puntosJ1 += 5;
                } else {
                    puntosJ2 += 5;
                }
                return "2"; //HUNDIDO
            } else {
                if (turno == 1) {
                    puntosJ1++;
                } else {
                    puntosJ2++;
                }
                return "1"; //TOCADO
            }

        }

        //TODO
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
        tablero[0][0] = '0';
        tablero[0][1] = '0';
        tablero[0][2] = '0';
        tablero[0][3] = '0';

        //barco2-3cuadriculas-1
        tablero[0][9] = '1';
        tablero[1][9] = '1';
        tablero[2][9] = '1';

        //barco2-3cuadriculas-2
        tablero[9][0] = '2';
        tablero[9][1] = '2';
        tablero[9][2] = '2';

        //barco3-2cuadriculas-1
        tablero[4][2] = '3';
        tablero[3][2] = '3';

        //barco3-2cuadriculas-2
        tablero[5][7] = '4';
        tablero[5][8] = '4';

        //barco3-2cuadriculas-3
        tablero[9][7] = '5';
        tablero[9][8] = '5';

        //barco4-1cuadricula-1
        tablero[7][4] = '6';

        //barco4-1cuadricula-2
        tablero[3][5] = '7';

        //barco4-1cuadricula-3
        tablero[1][7] = '8';

        //barco4-1cuadricula-3
        tablero[5][4] = '9';

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
        servidorHundir.nuevoTablero();
        //servidorHundir.mostrarTablero();

        servidorHundir.lobby();

        if (servidorHundir.jugadores == 1) {

        } else if (servidorHundir.jugadores == 2) {
            servidorHundir.juegoCoop();
        }

    }

}
